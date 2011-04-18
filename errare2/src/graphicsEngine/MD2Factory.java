/*Errare is a free and crossplatform MMORPG project.
Copyright (C) 2006  Christophe MEYER

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.*/

package graphicsEngine;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Vector;

import geom.Point;

/**
 * Loads a 3D model stored in a file which has the format "md2".
 * @author cyberchrist
 *
 */
public class MD2Factory {

	transient static private Point[] MD2_Std_Normals = new Point[162];
	
	private static float[] uvs;
	private static MeshData[] keyFrames;
	private static int[] indices;
	private static int[] uvIndices;
	
	private static Vector<Integer> newtopology;
	
	private static int[] topology;
	private static float[] texcoords;
	
	
	static {
		init_MD2_normals();
	}
	
	//public MD2Factory(){
	//	if(MD2_Std_Normals[0]==null) init_MD2_normals();
	//}
	
	/**
	 * Loads a MD2 model.
	 * @param filePath Path to model
	 * @throws IOException 
	 */
	public static GraphicalRep loadMD2(String filePath) throws IOException 
	{
		// Attempt to get URL
		URL url = MD2Factory.class.getClassLoader().getResource(filePath);  
		InputStream is = url.openStream();
		// Open buffered stream for speed            
		BufferedInputStream bis = new BufferedInputStream(is);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] data = new byte[1024 * 8];
		int read = 0;
		while(true) 
		{
			read = bis.read(data);
			if (read < 0) break;
			baos.write(data, 0, read);
		}
		data = baos.toByteArray(); 

		// Close stream
		bis.close();
		baos.close();
		is.close();

		// New byte reader
		ByteLoader byteLoader = new ByteLoader(data);

		// Read header
		int magic            = byteLoader.readInt();
		int version          = byteLoader.readInt();
		int skinWidth        = byteLoader.readInt();
		int skinHeight       = byteLoader.readInt();
		int frameSize        = byteLoader.readInt();
		int numSkins         = byteLoader.readInt();
		int numVertices      = byteLoader.readInt();
		int numTexCoords     = byteLoader.readInt();
		int numTriangles     = byteLoader.readInt();
		//texcoords = new float[numTriangles * 3 * 2]; // uv floats per point per triangle
		int numGlCommands    = byteLoader.readInt();
		int numFrames        = byteLoader.readInt();
		keyFrames = new MeshData[numFrames];
		int offsetSkins      = byteLoader.readInt();
		int offsetTexCoords  = byteLoader.readInt();
		int offsetTriangles	 = byteLoader.readInt();
		int offsetFrames     = byteLoader.readInt();

		// Check header version
		if (version != 8)
		{
			throw new IOException("MD2 version error : version 8 expected");
		}

		// Read in uv coords
		uvs = new float[numTexCoords * 2];
		byteLoader.setFileOffset(offsetTexCoords);
		for (int i = 0; i < numTexCoords; i++)
		{                
			// Convert
			uvs[(i*2)] = (float)byteLoader.readShort() / skinWidth;
			uvs[(i*2)+1] = 1 - (float)byteLoader.readShort() / skinHeight;                                               
		}

		// Read in faces
		byteLoader.setFileOffset(offsetTriangles);
		indices = new int[numTriangles * 3];
		uvIndices = new int[numTriangles * 3];
		int curIndex = 0;
		for (int i = 0; i < numTriangles; i++)
		{
			// Read face (changing to correct GL winding)
			indices[curIndex+2] = byteLoader.readShort();
			indices[curIndex+1] = byteLoader.readShort();
			indices[curIndex] = byteLoader.readShort();
			uvIndices[curIndex+2] = byteLoader.readShort();
			uvIndices[curIndex+1] = byteLoader.readShort();
			uvIndices[curIndex] = byteLoader.readShort();
			curIndex += 3;
		}

		// Move file pointer to frames
		byteLoader.setFileOffset(offsetFrames);

		// Store some vars outside for speed
		float[] scale = new float[3];
		float[] translate = new float[3];                        
		int tempx;
		int tempy;
		int tempz;     
		int normalindex;
		float[] tempvertices;
		float[] tempnormals;
		String name;

		// Read in all frames
		for (int i = 0; i < numFrames; i++)
		{
			tempvertices = new float[numVertices *3]; // xyz floats per vertex
			tempnormals = new float[numVertices *3]; //nx ny nz floats per normal, one normal per vertex!

			// Read scale, translation and name
			scale[0] = byteLoader.readFloat();
			scale[1] = byteLoader.readFloat();
			scale[2] = byteLoader.readFloat();
			translate[0] = byteLoader.readFloat();
			translate[1] = byteLoader.readFloat();
			translate[2] = byteLoader.readFloat();
			name = byteLoader.readString(16);
			//System.out.println(curFrame.name);
			byteLoader.addToFileOffset(16);

			// Read and convert vertices
			for (int v = 0; v < numVertices; v++)
			{
				// Read packed vertex
				tempx = byteLoader.readByte();
				tempy = byteLoader.readByte();
				tempz = byteLoader.readByte();			
				// Read light normal index
				normalindex = byteLoader.readByte();
				tempnormals
				[(v*3)]
				 = MD2_Std_Normals
				 [normalindex]
				  .
				  x;
				tempnormals[(v*3)+2] = MD2_Std_Normals[normalindex].y;
				tempnormals[(v*3)+1] = MD2_Std_Normals[normalindex].z;
				// Convert and add vertex (swapping z and y)
				tempvertices[(v*3)] = tempx * scale[0] + translate[0];
				tempvertices[(v*3)+2] = -1 * (tempy * scale[1] + translate[1]);
				tempvertices[(v*3)+1] = tempz * scale[2] + translate[2];

				//set maximums bounds
				/*if(tempvertices[(v*3)]>verticeXmaximum)
						verticeXmaximum=curFrame.vertices[(v*3)];
					if(curFrame.vertices[(v*3)+1]>verticeYmaximum)
						verticeYmaximum=curFrame.vertices[(v*3)+1];
					if(curFrame.vertices[(v*3)+2]>verticeZmaximum)
						verticeZmaximum=curFrame.vertices[(v*3)+2];
				 */
				//set minimums bounds
				/*if(curFrame.vertices[(v*3)]<verticeXminimum)
						verticeXminimum=curFrame.vertices[(v*3)];
					if(curFrame.vertices[(v*3)+1]<verticeYminimum)
						verticeYminimum=curFrame.vertices[(v*3)+1];
					if(curFrame.vertices[(v*3)+2]<verticeZminimum)
						verticeZminimum=curFrame.vertices[(v*3)+2];
				 */
			}

			// Allocate space for frame
			keyFrames[i] = new MeshData(tempvertices, tempnormals);
			keyFrames[i].name = name;
		}
 
		//TODO: galere starts here:
		float vx, vy, vz, nx, ny, nz, tu, tv;
		MD2ChunkVertNorm[] minichunkofkeyframes = new MD2ChunkVertNorm[numFrames];
		ArrayList<MD2Chunk> chunksList = new ArrayList<MD2Chunk>();
		newtopology = new Vector<Integer>();
		for(int index = 0; index < indices.length; index++){ // also index in uvIndices !
			// construct the chunk
			tu = uvs[uvIndices[index]*2];
			tv = uvs[uvIndices[index]*2+1];
			minichunkofkeyframes = new MD2ChunkVertNorm[numFrames];
			int offset = 0;
			for(MeshData kf : keyFrames){
				vx = kf.vertices[indices[index]*3];
				vy = kf.vertices[indices[index]*3+1];
				vz = kf.vertices[indices[index]*3+2];
				nx = kf.normals[indices[index]*3];
				ny = kf.normals[indices[index]*3+1];
				nz = kf.normals[indices[index]*3+2];
				minichunkofkeyframes[offset] =  new MD2ChunkVertNorm(vx,vy,vz,nx,ny,nz);
				offset++; // next keyframe in minichunk
			}
			
			MD2Chunk myChunk = new MD2Chunk(minichunkofkeyframes,tu,tv);
			
			if(chunksList.contains(myChunk)){
				int previousInd = chunksList.indexOf(myChunk);
				newtopology.add(previousInd);
			}else{
				chunksList.add(myChunk);
				newtopology.add(chunksList.indexOf(myChunk));
			}
		}
		
		// extract final topology
		topology = new int[newtopology.size()];
		int offset = 0;
		for(Integer integ : newtopology){
			topology[offset] = integ;
			offset++;
		}
		newtopology = null;
		
		// extract final texcoords
		texcoords = new float[chunksList.size()*2]; // *2 because there are 2 texcoords per point
		offset = 0;
		for(MD2Chunk md2c : chunksList){
			texcoords[offset] = md2c.tu;
			offset++;
			texcoords[offset] = md2c.tv;
			offset++;
		}
		
		// create arrays of the correct size
		for(MeshData meshdata : keyFrames){
			meshdata.vertices = new float[chunksList.size()*3];
			meshdata.normals = new float[chunksList.size()*3];
		}
		// extract final vertices and normals
		offset = 0;
		for(MD2Chunk md2c : chunksList){
			int minichunknumber = 0;
			for(MD2ChunkVertNorm minichunk: md2c.oneChunk){
				MeshData meshdata = keyFrames[minichunknumber];
				meshdata.vertices[offset*3] = minichunk.vx;
				meshdata.vertices[offset*3+1] = minichunk.vy;
				meshdata.vertices[offset*3+2] = minichunk.vz;
				meshdata.normals[offset*3] = minichunk.nx;
				meshdata.normals[offset*3+1] = minichunk.ny;
				meshdata.normals[offset*3+2] = minichunk.nz;
				minichunknumber++;
			}
			offset++;
		}
		
		
		Mesh mesh;
		float[][] texcoordsarray = {texcoords};
		if(numFrames>1){
			mesh = new AnimatedMesh(topology, keyFrames, texcoordsarray);
			mesh.path = filePath;
			((AnimatedMesh)mesh).startAnimation(AnimatedMesh.DEFAULT_ANIM_NAME);
		}else{
			mesh = new StaticMesh(topology, keyFrames[0], texcoordsarray);
			mesh.path = filePath;
		}
		
		Texture[] texs = new Texture[1];
		String texturename = filePath.replace(".md2", ".png");
		texs[0] = TextureFactory.loadTexture(texturename);
		GraphicalRep model = new GraphicalRep(mesh, texs);
		return model;
		
		/*	// calculate bounding sphere
			float xcenter = verticeXminimum+(verticeXmaximum-verticeXminimum)/2;
			float ycenter = verticeYminimum+(verticeYmaximum-verticeYminimum)/2;
			float zcenter = verticeZminimum+(verticeZmaximum-verticeZminimum)/2;
			sphereCenter = new Point(xcenter, ycenter, zcenter);
			radius = verticeXmaximum-xcenter;
			float temp_radius = verticeYmaximum-ycenter;
			if(temp_radius > radius) radius = temp_radius;
			temp_radius = verticeZmaximum-zcenter;
			if(temp_radius > radius) radius = temp_radius;
                 */
	}


/** start of structure for the algorithm to convert a MD2 into ERRARE **/
	static class MD2ChunkVertNorm{
		float vx,vy,vz,nx,ny,nz;
		public MD2ChunkVertNorm(float vx, float vy, float vz, float nx, float ny, float nz){
			this.vx = vx; this.vy = vy; this.vz = vz; this.nx = nx; this.ny = ny; this.nz = nz;
		}
	}

	static class MD2Chunk{
		float tu,tv;
		MD2ChunkVertNorm[] oneChunk;
		public MD2Chunk(MD2ChunkVertNorm[] oneChunk, float tu, float tv){
			this.oneChunk  = oneChunk; this.tu = tu; this.tv = tv;
		}
		public boolean equals(Object o){
			MD2Chunk other = (MD2Chunk)o;
			if(this.tu != other.tu || this.tv != other.tv)
				return false;
			for(int i = 0; i< oneChunk.length; i++){
				if(oneChunk[i].vx != other.oneChunk[i].vx 
						|| oneChunk[i].vy != other.oneChunk[i].vy
						|| oneChunk[i].vz != other.oneChunk[i].vz
						|| oneChunk[i].nx != other.oneChunk[i].nx
						|| oneChunk[i].ny != other.oneChunk[i].ny
						|| oneChunk[i].nz != other.oneChunk[i].nz)
					return false;
			}
			return true;
		}
	}
/** end of structure for the algorithm to convert a MD2 into ERRARE **/

	
	private static void init_MD2_normals() {
		MD2_Std_Normals[0]=	new Point(-0.525731f,  0.000000f,  0.850651f ); 
		MD2_Std_Normals[1]= new Point(-0.442863f,  0.238856f,  0.864188f );
		MD2_Std_Normals[2]= new Point(-0.295242f,  0.000000f,  0.955423f ); 
		MD2_Std_Normals[3]= new Point(-0.309017f,  0.500000f,  0.809017f ); 
		MD2_Std_Normals[4]= new Point(-0.162460f,  0.262866f,  0.951056f ); 
		MD2_Std_Normals[5]= new Point( 0.000000f,  0.000000f,  1.000000f );
		MD2_Std_Normals[6]= new Point( 0.000000f,  0.850651f,  0.525731f ); 
		MD2_Std_Normals[7]= new Point(-0.147621f,  0.716567f,  0.681718f ); 
		MD2_Std_Normals[8]= new Point( 0.147621f,  0.716567f,  0.681718f ); 
		MD2_Std_Normals[9]= new Point( 0.000000f,  0.525731f,  0.850651f ); 
		MD2_Std_Normals[10]= new Point( 0.309017f,  0.500000f,  0.809017f ); 
		MD2_Std_Normals[11]= new Point( 0.525731f,  0.000000f,  0.850651f ); 
		MD2_Std_Normals[12]= new Point( 0.295242f,  0.000000f,  0.955423f ); 
		MD2_Std_Normals[13]= new Point( 0.442863f,  0.238856f,  0.864188f ); 
		MD2_Std_Normals[14]= new Point( 0.162460f,  0.262866f,  0.951056f );
		MD2_Std_Normals[15]= new Point(-0.681718f,  0.147621f,  0.716567f ); 
		MD2_Std_Normals[16]= new Point(-0.809017f,  0.309017f,  0.500000f );
		MD2_Std_Normals[17]= new Point(-0.587785f,  0.425325f,  0.688191f ); 
		MD2_Std_Normals[18]= new Point(-0.850651f,  0.525731f,  0.000000f ); 
		MD2_Std_Normals[19]= new Point(-0.864188f,  0.442863f,  0.238856f ); 
		MD2_Std_Normals[20]= new Point(-0.716567f,  0.681718f,  0.147621f ); 
		MD2_Std_Normals[21]= new Point(-0.688191f,  0.587785f,  0.425325f ); 
		MD2_Std_Normals[22]= new Point(-0.500000f,  0.809017f,  0.309017f ); 
		MD2_Std_Normals[23]= new Point(-0.238856f,  0.864188f,  0.442863f ); 
		MD2_Std_Normals[24]= new Point(-0.425325f,  0.688191f,  0.587785f ); 
		MD2_Std_Normals[25]= new Point(-0.716567f,  0.681718f, -0.147621f );
		MD2_Std_Normals[26]= new Point(-0.500000f,  0.809017f, -0.309017f ); 
		MD2_Std_Normals[27]= new Point(-0.525731f,  0.850651f,  0.000000f ); 
		MD2_Std_Normals[28]= new Point( 0.000000f,  0.850651f, -0.525731f ); 
		MD2_Std_Normals[29]= new Point(-0.238856f,  0.864188f, -0.442863f ); 
		MD2_Std_Normals[30]= new Point( 0.000000f,  0.955423f, -0.295242f ); 
		MD2_Std_Normals[31]= new Point(-0.262866f,  0.951056f, -0.162460f );
		MD2_Std_Normals[32]= new Point( 0.000000f,  1.000000f,  0.000000f ); 
		MD2_Std_Normals[33]= new Point( 0.000000f,  0.955423f,  0.295242f );
		MD2_Std_Normals[34]= new Point(-0.262866f,  0.951056f,  0.162460f ); 
		MD2_Std_Normals[35]= new Point( 0.238856f,  0.864188f,  0.442863f ); 
		MD2_Std_Normals[36]= new Point( 0.262866f,  0.951056f,  0.162460f ); 
		MD2_Std_Normals[37]= new Point( 0.500000f,  0.809017f,  0.309017f ); 
		MD2_Std_Normals[38]= new Point( 0.238856f,  0.864188f, -0.442863f ); 
		MD2_Std_Normals[39]= new Point( 0.262866f,  0.951056f, -0.162460f ); 
		MD2_Std_Normals[40]= new Point( 0.500000f,  0.809017f, -0.309017f ); 
		MD2_Std_Normals[41]= new Point( 0.850651f,  0.525731f,  0.000000f ); 
		MD2_Std_Normals[42]= new Point( 0.716567f,  0.681718f,  0.147621f ); 
		MD2_Std_Normals[43]= new Point( 0.716567f,  0.681718f, -0.147621f ); 
		MD2_Std_Normals[44]= new Point( 0.525731f,  0.850651f,  0.000000f ); 
		MD2_Std_Normals[45]= new Point( 0.425325f,  0.688191f,  0.587785f ); 
		MD2_Std_Normals[46]= new Point( 0.864188f,  0.442863f,  0.238856f ); 
		MD2_Std_Normals[47]= new Point( 0.688191f,  0.587785f,  0.425325f ); 
		MD2_Std_Normals[48]= new Point( 0.809017f,  0.309017f,  0.500000f ); 
		MD2_Std_Normals[49]= new Point( 0.681718f,  0.147621f,  0.716567f ); 
		MD2_Std_Normals[50]= new Point( 0.587785f,  0.425325f,  0.688191f ); 
		MD2_Std_Normals[51]= new Point( 0.955423f,  0.295242f,  0.000000f ); 
		MD2_Std_Normals[52]= new Point( 1.000000f,  0.000000f,  0.000000f ); 
		MD2_Std_Normals[53]= new Point( 0.951056f,  0.162460f,  0.262866f ); 
		MD2_Std_Normals[54]= new Point( 0.850651f, -0.525731f,  0.000000f ); 
		MD2_Std_Normals[55]= new Point( 0.955423f, -0.295242f,  0.000000f ); 
		MD2_Std_Normals[56]= new Point( 0.864188f, -0.442863f,  0.238856f ); 
		MD2_Std_Normals[57]= new Point( 0.951056f, -0.162460f,  0.262866f ); 
		MD2_Std_Normals[58]= new Point( 0.809017f, -0.309017f,  0.500000f ); 
		MD2_Std_Normals[59]= new Point( 0.681718f, -0.147621f,  0.716567f ); 
		MD2_Std_Normals[60]= new Point( 0.850651f,  0.000000f,  0.525731f ); 
		MD2_Std_Normals[61]= new Point( 0.864188f,  0.442863f, -0.238856f ); 
		MD2_Std_Normals[62]= new Point( 0.809017f,  0.309017f, -0.500000f ); 
		MD2_Std_Normals[63]= new Point( 0.951056f,  0.162460f, -0.262866f ); 
		MD2_Std_Normals[64]= new Point( 0.525731f,  0.000000f, -0.850651f ); 
		MD2_Std_Normals[65]= new Point( 0.681718f,  0.147621f, -0.716567f ); 
		MD2_Std_Normals[66]= new Point( 0.681718f, -0.147621f, -0.716567f ); 
		MD2_Std_Normals[67]= new Point( 0.850651f,  0.000000f, -0.525731f ); 
		MD2_Std_Normals[68]= new Point( 0.809017f, -0.309017f, -0.500000f ); 
		MD2_Std_Normals[69]= new Point( 0.864188f, -0.442863f, -0.238856f ); 
		MD2_Std_Normals[70]= new Point( 0.951056f, -0.162460f, -0.262866f ); 
		MD2_Std_Normals[71]= new Point( 0.147621f,  0.716567f, -0.681718f ); 
		MD2_Std_Normals[72]= new Point( 0.309017f,  0.500000f, -0.809017f ); 
		MD2_Std_Normals[73]= new Point( 0.425325f,  0.688191f, -0.587785f ); 
		MD2_Std_Normals[74]= new Point( 0.442863f,  0.238856f, -0.864188f ); 
		MD2_Std_Normals[75]= new Point( 0.587785f,  0.425325f, -0.688191f ); 
		MD2_Std_Normals[76]= new Point( 0.688191f,  0.587785f, -0.425325f ); 
		MD2_Std_Normals[77]= new Point(-0.147621f,  0.716567f, -0.681718f ); 
		MD2_Std_Normals[78]= new Point(-0.309017f,  0.500000f, -0.809017f ); 
		MD2_Std_Normals[79]= new Point( 0.000000f,  0.525731f, -0.850651f ); 
		MD2_Std_Normals[80]= new Point(-0.525731f,  0.000000f, -0.850651f ); 
		MD2_Std_Normals[81]= new Point(-0.442863f,  0.238856f, -0.864188f ); 
		MD2_Std_Normals[82]= new Point(-0.295242f,  0.000000f, -0.955423f ); 
		MD2_Std_Normals[83]= new Point(-0.162460f,  0.262866f, -0.951056f ); 
		MD2_Std_Normals[84]= new Point( 0.000000f,  0.000000f, -1.000000f ); 
		MD2_Std_Normals[85]= new Point( 0.295242f,  0.000000f, -0.955423f ); 
		MD2_Std_Normals[86]= new Point( 0.162460f,  0.262866f, -0.951056f ); 
		MD2_Std_Normals[87]= new Point(-0.442863f, -0.238856f, -0.864188f ); 
		MD2_Std_Normals[88]= new Point(-0.309017f, -0.500000f, -0.809017f ); 
		MD2_Std_Normals[89]= new Point(-0.162460f, -0.262866f, -0.951056f ); 
		MD2_Std_Normals[90]= new Point( 0.000000f, -0.850651f, -0.525731f ); 
		MD2_Std_Normals[91]= new Point(-0.147621f, -0.716567f, -0.681718f ); 
		MD2_Std_Normals[92]= new Point( 0.147621f, -0.716567f, -0.681718f ); 
		MD2_Std_Normals[93]= new Point( 0.000000f, -0.525731f, -0.850651f ); 
		MD2_Std_Normals[94]= new Point( 0.309017f, -0.500000f, -0.809017f ); 
		MD2_Std_Normals[95]= new Point( 0.442863f, -0.238856f, -0.864188f ); 
		MD2_Std_Normals[96]= new Point( 0.162460f, -0.262866f, -0.951056f ); 
		MD2_Std_Normals[97]= new Point( 0.238856f, -0.864188f, -0.442863f ); 
		MD2_Std_Normals[98]= new Point( 0.500000f, -0.809017f, -0.309017f ); 
		MD2_Std_Normals[99]= new Point( 0.425325f, -0.688191f, -0.587785f ); 
		MD2_Std_Normals[100]= new Point( 0.716567f, -0.681718f, -0.147621f ); 
		MD2_Std_Normals[101]= new Point( 0.688191f, -0.587785f, -0.425325f ); 
		MD2_Std_Normals[102]= new Point( 0.587785f, -0.425325f, -0.688191f ); 
		MD2_Std_Normals[103]= new Point( 0.000000f, -0.955423f, -0.295242f ); 
		MD2_Std_Normals[104]= new Point( 0.000000f, -1.000000f,  0.000000f ); 
		MD2_Std_Normals[105]= new Point( 0.262866f, -0.951056f, -0.162460f ); 
		MD2_Std_Normals[106]= new Point( 0.000000f, -0.850651f,  0.525731f ); 
		MD2_Std_Normals[107]= new Point( 0.000000f, -0.955423f,  0.295242f ); 
		MD2_Std_Normals[108]= new Point( 0.238856f, -0.864188f,  0.442863f ); 
		MD2_Std_Normals[109]= new Point( 0.262866f, -0.951056f,  0.162460f ); 
		MD2_Std_Normals[110]= new Point( 0.500000f, -0.809017f,  0.309017f ); 
		MD2_Std_Normals[111]= new Point( 0.716567f, -0.681718f,  0.147621f ); 
		MD2_Std_Normals[112]= new Point( 0.525731f, -0.850651f,  0.000000f ); 
		MD2_Std_Normals[113]= new Point(-0.238856f, -0.864188f, -0.442863f ); 
		MD2_Std_Normals[114]= new Point(-0.500000f, -0.809017f, -0.309017f ); 
		MD2_Std_Normals[115]= new Point(-0.262866f, -0.951056f, -0.162460f ); 
		MD2_Std_Normals[116]= new Point(-0.850651f, -0.525731f,  0.000000f ); 
		MD2_Std_Normals[117]= new Point(-0.716567f, -0.681718f, -0.147621f ); 
		MD2_Std_Normals[118]= new Point(-0.716567f, -0.681718f,  0.147621f ); 
		MD2_Std_Normals[119]= new Point(-0.525731f, -0.850651f,  0.000000f ); 
		MD2_Std_Normals[120]= new Point(-0.500000f, -0.809017f,  0.309017f ); 
		MD2_Std_Normals[121]= new Point(-0.238856f, -0.864188f,  0.442863f ); 
		MD2_Std_Normals[122]= new Point(-0.262866f, -0.951056f,  0.162460f ); 
		MD2_Std_Normals[123]= new Point(-0.864188f, -0.442863f,  0.238856f ); 
		MD2_Std_Normals[124]= new Point(-0.809017f, -0.309017f,  0.500000f ); 
		MD2_Std_Normals[125]= new Point(-0.688191f, -0.587785f,  0.425325f ); 
		MD2_Std_Normals[126]= new Point(-0.681718f, -0.147621f,  0.716567f ); 
		MD2_Std_Normals[127]= new Point(-0.442863f, -0.238856f,  0.864188f ); 
		MD2_Std_Normals[128]= new Point(-0.587785f, -0.425325f,  0.688191f ); 
		MD2_Std_Normals[129]= new Point(-0.309017f, -0.500000f,  0.809017f ); 
		MD2_Std_Normals[130]= new Point(-0.147621f, -0.716567f,  0.681718f ); 
		MD2_Std_Normals[131]= new Point(-0.425325f, -0.688191f,  0.587785f ); 
		MD2_Std_Normals[132]= new Point(-0.162460f, -0.262866f,  0.951056f ); 
		MD2_Std_Normals[133]= new Point( 0.442863f, -0.238856f,  0.864188f ); 
		MD2_Std_Normals[134]= new Point( 0.162460f, -0.262866f,  0.951056f ); 
		MD2_Std_Normals[135]= new Point( 0.309017f, -0.500000f,  0.809017f ); 
		MD2_Std_Normals[136]= new Point( 0.147621f, -0.716567f,  0.681718f ); 
		MD2_Std_Normals[137]= new Point( 0.000000f, -0.525731f,  0.850651f ); 
		MD2_Std_Normals[138]= new Point( 0.425325f, -0.688191f,  0.587785f ); 
		MD2_Std_Normals[139]= new Point( 0.587785f, -0.425325f,  0.688191f ); 
		MD2_Std_Normals[140]= new Point( 0.688191f, -0.587785f,  0.425325f ); 
		MD2_Std_Normals[141]= new Point(-0.955423f,  0.295242f,  0.000000f ); 
		MD2_Std_Normals[142]= new Point(-0.951056f,  0.162460f,  0.262866f ); 
		MD2_Std_Normals[143]= new Point(-1.000000f,  0.000000f,  0.000000f ); 
		MD2_Std_Normals[144]= new Point(-0.850651f,  0.000000f,  0.525731f ); 
		MD2_Std_Normals[145]= new Point(-0.955423f, -0.295242f,  0.000000f ); 
		MD2_Std_Normals[146]= new Point(-0.951056f, -0.162460f,  0.262866f ); 
		MD2_Std_Normals[147]= new Point(-0.864188f,  0.442863f, -0.238856f ); 
		MD2_Std_Normals[148]= new Point(-0.951056f,  0.162460f, -0.262866f ); 
		MD2_Std_Normals[149]= new Point(-0.809017f,  0.309017f, -0.500000f ); 
		MD2_Std_Normals[150]= new Point(-0.864188f, -0.442863f, -0.238856f ); 
		MD2_Std_Normals[151]= new Point(-0.951056f, -0.162460f, -0.262866f ); 
		MD2_Std_Normals[152]= new Point(-0.809017f, -0.309017f, -0.500000f ); 
		MD2_Std_Normals[153]= new Point(-0.681718f,  0.147621f, -0.716567f ); 
		MD2_Std_Normals[154]= new Point(-0.681718f, -0.147621f, -0.716567f ); 
		MD2_Std_Normals[155]= new Point(-0.850651f,  0.000000f, -0.525731f ); 
		MD2_Std_Normals[156]= new Point(-0.688191f,  0.587785f, -0.425325f ); 
		MD2_Std_Normals[157]= new Point(-0.587785f,  0.425325f, -0.688191f ); 
		MD2_Std_Normals[158]= new Point(-0.425325f,  0.688191f, -0.587785f ); 
		MD2_Std_Normals[159]= new Point(-0.425325f, -0.688191f, -0.587785f ); 
		MD2_Std_Normals[160]= new Point(-0.587785f, -0.425325f, -0.688191f ); 
		MD2_Std_Normals[161]= new Point(-0.688191f, -0.587785f, -0.425325f );
	}
}
