package freeworldLoader;


import gameEngine.GameCharacter;
import gameEngine.GameRep;
import genericReps.CharacterRep;
import genericReps.SettingRep;
import geom.Point;
import geom.Triangle;
import geom.Vector;
import graphicsEngine.FollowCamera;
import graphicsEngine.GraphicalRep;
import graphicsEngine.GraphicalRepFactory;
import graphicsEngine.GraphicsEngine;
import graphicsEngine.Heightmap;
import graphicsEngine.Skydome;
import graphicsEngine.World;

import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.util.Hashtable;
import java.util.List;

import javax.imageio.ImageIO;

import logger.Logger;
import main.ResourceLocator;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import physicsEngine.PhysicalRep;
import physicsEngine.PhysicsEngine;

public class FreeWorld3DLoader {
	
	private Hashtable<String, String> meshes;
	private PhysicsEngine pe;
	private GraphicsEngine ge;
	private World world;
	
	public FreeWorld3DLoader( PhysicsEngine pe, GraphicsEngine ge) {
		this.pe = pe;
		this.ge = ge;
	}
	
	public void loadWorld(String xmlPath) throws Exception {
		SAXBuilder builder = new SAXBuilder();
        Document document = builder.build(Thread.currentThread().getContextClassLoader().getResourceAsStream(xmlPath));

        Element root = document.getRootElement();
        
        meshes = new Hashtable<String, String>();
        
        
        loadTerrainAndWater(root);
        loadEnvironment(root);
        loadCachedMeshGroups(root);
        loadSceneLayers(root);
        loadEntityTypes(root);
        loadEntities(root);
        //loadSKYMESH(root);
        loadVegetation(root);
        

        ge.changeWorld(world);
		
		
	}
	
	private void loadEnvironment(Element root) {

	}

	private void loadTerrainAndWater(Element root) throws IOException {
		String heightmap = root.getChild("Terrain").getChild("Heightmap").getAttributeValue("File");
		float zScale = Float.parseFloat(root.getChild("Terrain").getChild("Heightmap").getAttributeValue("Scale"));
		
		String texture = root.getChild("TerrainLayers").getChild("TerrainLayer").getChild("Texture").getAttributeValue("File");
		float scaleU = Float.parseFloat(root.getChild("TerrainLayers").getChild("TerrainLayer").getChild("Texture").getAttributeValue("ScaleU"));
		float scaleV = Float.parseFloat(root.getChild("TerrainLayers").getChild("TerrainLayer").getChild("Texture").getAttributeValue("ScaleV"));
		int mapSize = Integer.parseInt(root.getChild("Terrain").getAttributeValue("Size"));
		float step = Float.parseFloat(root.getChild("Terrain").getAttributeValue("Step"));
		
		float waterHeight = Float.parseFloat(root.getChild("Water").getAttributeValue("Height"));
		float waterTransparency = Float.parseFloat(root.getChild("Water").getAttributeValue("Transparency"));
		
		float[][] heights = getHeights(heightmap, mapSize);
		
		float min = Float.MAX_VALUE;
		float max = Float.MIN_VALUE;
		for(int i=0; i<heights.length; i++) {
			for(int j=0; j<heights[0].length; j++) {
				if(heights[i][j]>max)
					max = heights[i][j];
				if(heights[i][j]<min)
					min = heights[i][j];
			}
		}
		
		pe.createOctree(-step, min-step, -step, 
				Math.max(heights.length*step+step*2, Math.max(max+step, heights[0].length*step+step*2)), 3);
		
		//System.out.println("Heightmap:"+heights.length+"x"+heights[0].length+" step="+step+" zScale="+zScale+" waterHeight="+waterHeight+" texture="+texture);
		world = new World(pe.getOctree(), new Skydome(Skydome.SKY_CLEAR), new FollowCamera(new Point(0, 0, 0)));
        
		world.setHeightmap(new Heightmap(heights, zScale, step, texture, waterHeight, null, waterTransparency));
		
		for(int i=0; i<heights.length-1; i++) {
			for(int j=0; j<heights[0].length-1; j++) {
				Point p1 = new Point(i*step, heights[i][j], j*step);
				Point p2 = new Point(i*step, heights[i][j+1], (j+1)*step);
				Point p3 = new Point((i+1)*step, heights[i+1][j], j*step);
				Point p4 = new Point((i+1)*step, heights[i+1][j+1], (j+1)*step);

				Triangle t1 = new Triangle(p1, p2, p3);
				Triangle t2 = new Triangle(p2, p3, p4);
				pe.getOctree().addTriangle(t1);
				pe.getOctree().addTriangle(t2);
			}
		}
	}
	
	private float[][] getHeights(String heightmap, int size) throws IOException {
		float map[][] = new float[size][size];
		WritableRaster image = ImageIO.read(ResourceLocator.getRessourceAsStream(heightmap)).getRaster();
		for(int i=0; i<size; i++) {
			for(int j=0; j<size; j++) {
				map[i][j] = image.getSampleFloat(i, j, 0);
			}
		}
		
		return map;
	}

	private void loadCachedMeshGroups(Element root) {
		for(Element group : (List<Element>)root.getChild("CachedMeshGroups").getChildren("CachedMeshGroup")) {
			for(Element mesh : (List<Element>)group.getChildren()) {
				meshes.put(mesh.getAttributeValue("Id"),mesh.getAttributeValue("Filename"));
			}
		}
	}

	private void loadSceneLayers(Element root) throws IOException {
		for(Element layer : (List<Element>)root.getChild("SceneLayers").getChildren("SceneLayer")) {
			for(Element mesh : (List<Element>)layer.getChildren()) {

				try {

					//System.out.println(mesh.getAttributeValue("Name")+" id="+mesh.getAttributeValue("CachedMeshId"));

					String stringMatrix = mesh.getAttributeValue("Matrix");
					int i=0;
					float[] matrix = new float[16];

					for(String val : stringMatrix.split(",")) {
						matrix[i] = Float.parseFloat(val);

						//System.out.print(matrix[i]+" ");
						i++;
					}

					if(i!=16)
						throw new IOException("Matrix contains more or less than 16 values");

					GraphicalRep grep = GraphicalRepFactory.load(meshes.get(mesh.getAttributeValue("CachedMeshId")));
					
					Triangle[] triangles = grep.getTriangles();
					Point[] minMax = computeMinMax(triangles, matrix);
					
					PhysicalRep prep = new PhysicalRep(minMax[0], minMax[1]);
					
					prep.setMatrix(matrix);
					
					
					/*if(mesh.getAttributeValue("Name").equalsIgnoreCase("mainChar")) {
						CharacterRep<GameRep, PhysicalRep, GraphicalRep> rep = new CharacterRep<GameRep, PhysicalRep, GraphicalRep>(null, prep, grep, 0);
						prep.setRep(rep);
						grep.setRep(rep);

						pe.add(prep, triangles);

						pe.setMainChar(prep);
						ge.setMainChar(rep);
						world.cam = new FollowCamera(prep.getCenter());


					}
					else {*/
						SettingRep<PhysicalRep, GraphicalRep> rep = new SettingRep<PhysicalRep, GraphicalRep>(prep, grep, 0);
						prep.setRep(rep);
						grep.setRep(rep);

						pe.add(prep, triangles);
					/*}*/

				}catch(Exception e) {
					Logger.printERROR("Error in "+mesh.getAttributeValue("Name")+" id="+mesh.getAttributeValue("CachedMeshId"));
					Logger.printExceptionERROR(e);
				}

			}
		}
		makeMainChar();
	}
	
	
	
	private void makeMainChar() throws IOException {
		GraphicalRep grep = GraphicalRepFactory.load("data/md2/pknight/tris.md2");
		//grep.setShowAABox(true);
                PhysicalRep prep = new PhysicalRep(new Point(10,10,10), new Point(11, 12.5f, 11));
		prep.setInitialMatrix(new float[]{0.05f,0,0,0,0,0.05f,0,0,0,0,0.05f,11,12.5f,11,0,1});
		
		GameCharacter garep = new GameCharacter("Cyberchrist the Knight");
		
		CharacterRep<GameRep, PhysicalRep, GraphicalRep> rep = new CharacterRep<GameRep, PhysicalRep, GraphicalRep>(garep, prep, grep, 0);
		prep.setRep(rep);
		grep.setRep(rep);
		garep.setRep(rep);
		
		pe.add(prep, null);

		pe.setMainChar(prep);
		ge.setMainChar(rep);
		world.cam = new FollowCamera(prep.getCenter());
	}

	private Point[] computeMinMax(Triangle[] triangles, float[] m) {
		Point[] res = new Point[2];
		res[0] = new Point(Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE);
		res[1] = new Point(Float.MIN_VALUE, Float.MIN_VALUE, Float.MIN_VALUE);
		
		for(Triangle tri : triangles) {
			for(Point p : tri.points) {
				float tx = p.x;
				float ty = p.y;
				float tz = p.z;
				
				p.x = m[0]*tx+m[4]*ty+m[8]*tz+m[12];
				p.y = m[1]*tx+m[5]*ty+m[9]*tz+m[13];
				p.z = m[2]*tx+m[6]*ty+m[10]*tz+m[14];
				
				float w = m[3]*tx+m[7]*ty+m[11]*tz+m[15];
				
				p.x /= w;
				p.y /= w;
				p.z /= w;
				
				if(p.x<res[0].x)
					res[0].x=p.x;
				if(p.y<res[0].y)
					res[0].y=p.y;
				if(p.z<res[0].z)
					res[0].z=p.z;
				
				if(p.x>res[1].x)
					res[1].x=p.x;
				if(p.y>res[1].y)
					res[1].y=p.y;
				if(p.z>res[1].z)
					res[1].z=p.z;
			}
		}
		
		return res;
	}

	private void loadEntityTypes(Element root) {

	}

	private void loadEntities(Element root) {

	}

	private void loadSKYMESH(Element root) {

	}
	
	private void loadVegetation(Element root) throws IOException {
		
		for(Element layer : (List<Element>)root.getChild("Vegetation").getChildren("Layer")) {
			
			float vegetationWidth = Float.parseFloat(layer.getAttributeValue("Width"));
			float vegetationHeight = Float.parseFloat(layer.getAttributeValue("Height"));
			FileChannel fc = new FileInputStream(ResourceLocator.getFile(layer.getAttributeValue("DataFile"))).getChannel();
			ByteBuffer buf = ByteBuffer.allocate(4);
			fc.read(buf);
			buf.rewind();
			buf.order(ByteOrder.LITTLE_ENDIAN);
			
			int width = buf.getInt();
			buf.rewind();
			fc.read(buf);
			buf.rewind();
			buf.order(ByteOrder.LITTLE_ENDIAN);
			int height = buf.getInt();
			
			for(int y = 0; y < height; y++)
			{
			    for(int x = 0; x < width; x++)
			    {
			    	buf.rewind();
					fc.read(buf);
					buf.rewind();
					buf.order(ByteOrder.LITTLE_ENDIAN);
			    	int density = buf.getInt();
	                for(int d = 0; d < density; d++)
	                {
	                	buf.rewind();
						fc.read(buf);
						buf.rewind();
						buf.order(ByteOrder.LITTLE_ENDIAN);
	                	float xpos = buf.getFloat();
	                	buf.rewind();
						fc.read(buf);
						buf.rewind();
						buf.order(ByteOrder.LITTLE_ENDIAN);
	                	float ypos = buf.getFloat();
	                	buf.rewind();
						fc.read(buf);
						buf.rewind();
						buf.order(ByteOrder.LITTLE_ENDIAN);
	                	float zpos = buf.getFloat();
	                	
	                	//System.out.println("Vegetation at ("+xpos+";"+ypos+";"+zpos+")");
	                	
	                	GraphicalRep grep = GraphicalRepFactory.load(layer.getAttributeValue("Mesh"));
	    				PhysicalRep prep = new PhysicalRep(new Point(xpos, ypos, zpos), new Point(xpos, ypos, zpos));
	    			
	    				prep.setMatrix(new float[]{ vegetationWidth,0,0,0,
	    											0,vegetationHeight,0,0,
	    											0,0,vegetationWidth,0,
	    											xpos,ypos,zpos,1});
	                	
	                	SettingRep<PhysicalRep, GraphicalRep> rep = new SettingRep<PhysicalRep, GraphicalRep>(prep, grep, 0);
						prep.setRep(rep);
						grep.setRep(rep);

						pe.add(prep, null);
	                 }
			    }
			}
		}
	}

	
	public static Vector getTranslation(float[] matrix)	{
		return new Vector(matrix[12], matrix[13], matrix[14]);
	}

	public static float[] getRotation(float[] matrix) {
		float[] euler= new float[3];
		
		float a = (float) Math.asin(matrix[2]);
	    float ca = (float) Math.cos(a);
	    euler[1] = rad2Deg(a);  /* Calculate Y-axis angle */

	    if (Math.abs(ca) > 0.005) /* Gimbal lock? */
	    {
	      /* No, so get Z-axis angle */
	      euler[2] = rad2Deg((float)Math.atan2(-matrix[1] / ca, matrix[0]/ ca));

	      /* Get X-axis angle */
	      euler[0] = rad2Deg((float)Math.atan2(-matrix[6] / ca, matrix[10] / ca));
	    }
	    else /* Gimbal lock has occurred */
	    {
	      /* Set Z-axis angle to zero */
	      euler[2]  = 0;

	      /* And calculate X-axis angle */
	      euler[0] = rad2Deg((float)Math.atan2(-matrix[9], matrix[5]));
	    }
	    
	    if (euler[0] < 0) euler[0] += 360;
	    if (euler[1] < 0) euler[1] += 360;
	    if (euler[2] < 0) euler[2] += 360;
	    
	    return euler;
	}

	public static float[] getAndRemoveScale(float[] matrix) {
		float[] scale = new float[3];
		scale[0] = (float) Math.sqrt(matrix[0]*matrix[0]+matrix[4]*matrix[4]+matrix[8]*matrix[8]);
		scale[1] = (float) Math.sqrt(matrix[1]*matrix[1]+matrix[5]*matrix[5]+matrix[9]*matrix[9]);
		scale[2] = (float) Math.sqrt(matrix[2]*matrix[2]+matrix[6]*matrix[6]+matrix[10]*matrix[10]);

		matrix[0] /= scale[0];  matrix[4] /= scale[0]; matrix[8] /= scale[0];
		matrix[1] /= scale[1]; matrix[5] /= scale[1];   matrix[9] /= scale[1];
		matrix[2] /= scale[2];     matrix[6] /= scale[2];           matrix[10] /= scale[2];
		
		return scale;
	}
	
	public static float rad2Deg(float a) {
		return (float) (a*(180/Math.PI));
	}
	
	public static float deg2Rad(float a) {
		return (float) (a*(Math.PI/180));
	}
	
	

	public static void main(String[] args) throws Exception {
		/*FreeWorld3DLoader loader = new FreeWorld3DLoader(null, null);
		loader.loadWorld("freeworld/world.xml");*/
		
		float cx = (float)Math.cos(0.1);
		float sx = (float)Math.sin(0.1);
		
		float cy = (float)Math.cos(0.2);
		float sy = (float)Math.sin(0.2);
		
		float cz = (float)Math.cos(0.3);
		float sz = (float)Math.sin(0.3);
		
		float[] m = new float[16];
		
		float scx = 0.1f;
		float scy = 0.2f;
		float scz = 0.3f;
		
		m[0] = cy*cz*scx;  m[4] = -sx*-sy*cz+cx*sz*scx; m[8] = cx*-sy*sz+sx*cz*scx;  m[12] = 0;
		m[1] = -cy*sz*scy; m[5] = sx*sy*sz+cx*cz*scy;   m[9] = -cx*-sy*sz+sx*cz*scy; m[13] = 0;
		m[2] = sy*scz;     m[6] = -sx*cy*scz;           m[10] = cx*cy*scz;           m[14] = 0;
		m[3] = 0;      m[7] = 0;                m[11] = 0;               m[15] = 1;
		

		//System.out.println(getAndRemoveScale(m)[1]);
		//System.out.println(deg2Rad(getRotation(m)[1]));
	}

}
