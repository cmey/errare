package mapEditor;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.Hashtable;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import physicsEngine2.Octree;
import physicsEngine2.TreeCube;

import net.java.games.jogl.GL;
import net.java.games.jogl.GLDrawable;
import net.java.games.jogl.GLU;
import net.java.games.jogl.util.BufferUtils;

import geom.Point;
import geom.Sphere;
import graphicsEngine.GraphicsEngine;
import graphicsEngine.Heightmap;

public class EditorGraphicsEngine extends GraphicsEngine{
	
	BaseGrid baseGrid;
	boolean settingOccluder = false;
	Hashtable<Integer, Sphere> spheres_list_id;
	boolean gl_selection_mode;
	double mouse_x,mouse_y;
	int last_x, last_y;
	int[] viewPort;
	int hits;
	IntBuffer selectBuffer;
	
	public EditorGraphicsEngine(JFrame f) {
		super(f);
		spheres_list_id = new Hashtable<Integer, Sphere>();
		
		keyboardHelper.skybox_on = false;
		baseGrid = new BaseGrid(512,100,this);
		
		setHeightMap(baseGrid.getGrid(), (int) baseGrid.getSize());
		
		this.setDebugTreeOverlay(baseGrid.getOctree());
		
		getGLCanvas().addKeyListener(baseGrid);
		
	}
	
	protected void start_Display_event(GL gl){
		
		if(gl_selection_mode){
			
			final int bufferSize  = 512;
			ByteBuffer byteBuffer = ByteBuffer.allocateDirect(
					BufferUtils.SIZEOF_INT*bufferSize);
			byteBuffer.order(ByteOrder.nativeOrder());
			selectBuffer  = byteBuffer.asIntBuffer();
			int buffsize = 512;
			viewPort = new int[4];
			hits = 0;
			gl.glGetIntegerv(GL.GL_VIEWPORT, viewPort);
			gl.glSelectBuffer(buffsize, selectBuffer);		
			
			gl.glRenderMode(GL.GL_SELECT);
		}
		
	}
	
	protected void start_3D_event(GL gl, GLU glu){
		if(gl_selection_mode){
		gl.glInitNames();
		gl.glPushName(0);
	//	gl.glMatrixMode(GL.GL_PROJECTION);
		//gl.glPushMatrix();
		//gl.glLoadIdentity();
		glu.gluPickMatrix(mouse_x, (double) viewPort[3] - mouse_y, 1.0d, 1.0d, viewPort);
		//glu.gluOrtho2D(0.0d, 1.0d, 0.0d, 1.0d);
		}
	}
	
	protected void end_3D_event(GL gl){
		if(gl_selection_mode){
			gl.glMatrixMode(GL.GL_PROJECTION);
			//gl.glPopMatrix();
			gl.glFlush();
			hits = gl.glRenderMode(GL.GL_RENDER);
			
			processHits(hits, selectBuffer);
			
			gl_selection_mode = false;
			spheres_list_id.clear();
		}
	}
	
	
	
	public void processHits(int hits, IntBuffer buffer)
	{
		int id = 0;
		long miniZ = Long.MAX_VALUE;
		for(int i=0; i<hits; i++) {
			int nbnames = buffer.get();
			//System.out.println("nb names="+nbnames);
			long z1 = Converter.uint2long(buffer.get());
			long z2 = Converter.uint2long(buffer.get());
			//System.out.println("z1="+z1);
			//System.out.println("z2="+z2);
			
			for(int j=0; j<nbnames; j++){
				int nam = buffer.get();
				if(z1 < miniZ){
					miniZ = z1;
					id = nam;
				}
				//System.out.println("name="+nam);
			}
			
		}
		//System.out.println("----------------------");
		Sphere s = spheres_list_id.get(id);
		baseGrid.setSelectedControlPoint(s);
		
	}
	
	
	
	/**
	 * dessine un geom.Sphere
	 * @param gl
	 * @param glu
	 * @param sphere
	 */
	protected void drawThisSphere(GL gl, GLU glu, geom.Sphere sphere){
		if(!sphere.isVisible()) 
			return;
		gl.glPushAttrib(GL.GL_ALL_ATTRIB_BITS);
		gl.glPushMatrix();
		gl.glDisable(GL.GL_TEXTURE_2D);
		if(sphere.isWire())
			gl.glPolygonMode( GL.GL_FRONT_AND_BACK, GL.GL_LINE );
		Color color = sphere.getColor();
		if(sphere.isTransparent()){
			gl.glDisable(GL.GL_DEPTH_TEST);
			gl.glEnable(GL.GL_BLEND);
			gl.glColor4f(color.getRed(),color.getGreen(),color.getBlue(),0.2f);
		}else{
			gl.glDisable(GL.GL_BLEND);
			gl.glColor4f(color.getRed(),color.getGreen(),color.getBlue(),1);
		}
		geom.Point center = sphere.center;
		gl.glTranslatef(center.x,center.y,center.z);
		//System.out.println("sphere center: "+center.x);
		gl.glLoadName(sphere.hashCode());
		spheres_list_id.put(sphere.hashCode(),sphere); /* couteux */
		glu.gluSphere(quadric,sphere.rad,10,10);
		gl.glLoadName(0);
		gl.glPopMatrix();
		gl.glPopAttrib();
	}
	
	
	
	protected void gestionPicking(MouseEvent e, GL gl, GLU glu) {
		glu.gluLookAt(mcx+camX, mcy+camY, mcz+camZ, mcx+lookX, mcy+lookY, mcz+lookZ, 0,1,0);
		double[] modelview = new double[16];
		double[] projection = new double[16];
		int[] viewport = new int[4];
		
		gl.glGetDoublev(GL.GL_MODELVIEW_MATRIX, modelview);
		gl.glGetDoublev(GL.GL_PROJECTION_MATRIX, projection);
		gl.glGetIntegerv(GL.GL_VIEWPORT, viewport);
		
		
		double winX; winX=xMouse;
		double winY; winY=yMouse;
		winY = (double)viewport[3] - (double)winY;
		float[] winZ = new float[1];
		gl.glReadPixels ((int)winX, (int)winY, 1, 1, GL.GL_DEPTH_COMPONENT, GL.GL_FLOAT, winZ);
		//winZ[0]=100;
		double[] objX = new double[1];
		double[] objY = new double[1];
		double[] objZ = new double[1];
		//System.out.print("mouse pos: "+winX +" "+winY+" "+winZ[0]);    	  
		
		glu.gluUnProject(
				winX, winY, winZ[0],
				modelview,
				projection,
				viewport,
				objX, objY, objZ
		);
		
		//if(DEBUG) System.out.print("   3Dclik: "+(int)objX[0] + " " + (int)objY[0] + " " + (int)objZ[0]);
		
		//intersecX=(mcx+camX)+(camY/(camY-objY[0]))*(objX[0]-(mcx+camX));
		//intersecZ=(mcz+camZ)+(camY/(camY-objY[0]))*(objZ[0]-(mcz+camZ));
		
		intersecX = objX[0];
		intersecY = objY[0];
		intersecZ = objZ[0];
		
		//if(DEBUG) System.out.println("   intersecX: "+(int)intersecX+" intersecZ: "+(int)intersecZ);
		
		if(keyboardHelper.settingOccluder){
			if(SwingUtilities.isRightMouseButton(e)){
				baseGrid.startAddingOccluder((float)intersecX, (float)intersecY, (float)intersecZ);
			}else if(SwingUtilities.isLeftMouseButton(e)){
				this.mouse_x = (double)e.getX();
				this.mouse_y = (double)e.getY();
				this.gl_selection_mode = true;
				if((e.getModifiers() & MouseEvent.MOUSE_DRAGGED) != 0){
					baseGrid.moveSelectedControlPoint(e.getX()-last_x, e.getY()-last_y);
					last_x = e.getX(); last_y = e.getY();
				}
			}
		}else{
			baseGrid.modifyElevation(intersecX, intersecZ, e);
			//setHeightMap(baseGrid.getGrid(), (int) baseGrid.getSize());
		}
		
		
	}
	
}
