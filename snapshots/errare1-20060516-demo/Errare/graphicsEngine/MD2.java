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
import java.io.InputStream;
import java.net.URL;
import java.util.LinkedList;

import net.java.games.jogl.GL;
import net.java.games.jogl.GLDrawable;
import net.java.games.jogl.GLU;
import net.java.games.jogl.GLUquadric;

public class MD2 extends GraphicalRep {
	
	static private Point3D[] MD2_Normals = new Point3D[162];
	static private int IDmd2 = 0;
	static private GLUquadric quadric;
	
	private Blood myBlood;
	
	public MD2(String filename) {
		super(filename, GraphicalRep.FORMAT_MD2);
		
		if(IDmd2==0) init_MD2_normals();
		
		IDmd2++;
		cast_reflection = true;
		
		String extension = filename.substring(filename.length()-4,filename.length());
		if(extension.compareTo(".md2")==0) {
			mesh_filename = filename;
			if(mc.containsKey(mesh_filename)) {
				if(DEBUG)System.out.println("j'ai economise un meshload !");
				frames = mc.get(mesh_filename).frames;
				indices = mc.get(mesh_filename).indices;
				uvIndices = mc.get(mesh_filename).uvIndices;
				uvs = mc.get(mesh_filename).uvs;
				verticeXmaximum = mc.get(mesh_filename).verticeXmaximum;
				verticeYmaximum = mc.get(mesh_filename).verticeYmaximum;
				verticeZmaximum = mc.get(mesh_filename).verticeZmaximum;
				verticeXminimum = mc.get(mesh_filename).verticeXminimum;
				verticeYminimum = mc.get(mesh_filename).verticeYminimum;
				verticeZminimum = mc.get(mesh_filename).verticeZminimum;
				interpolatedFrame = mc.get(mesh_filename).interpolatedFrame;
				sphereCenter = mc.get(mesh_filename).sphereCenter;
				radius = mc.get(mesh_filename).radius;
			} else {
				loadFileMD2(mesh_filename); //              << load mesh
				MeshCollectorItem meshitem = new MeshCollectorItem();
				meshitem.frames = frames;
				meshitem.indices = indices;
				meshitem.uvIndices = uvIndices;
				meshitem.uvs = uvs;
				meshitem.interpolatedFrame = interpolatedFrame;
				meshitem.verticeXmaximum = verticeXmaximum;
				meshitem.verticeYmaximum = verticeYmaximum;
				meshitem.verticeZmaximum = verticeZmaximum;
				meshitem.verticeXminimum = verticeXminimum;
				meshitem.verticeYminimum = verticeYminimum;
				meshitem.verticeZminimum = verticeZminimum;
				meshitem.sphereCenter = sphereCenter;
				meshitem.radius = radius;
				mc.put(mesh_filename,meshitem);
			}
			endFrame = frames.length-1;
			//setFrameRange(0, 40);
			setFPS(30);
			toggleAnim(true);
		}
		/*
		System.out.println();
		System.out.println(mesh_filename);
		System.out.println(texture_filename);
		System.out.println("found in collector: "+tc.containsKey(texture_filename));
		System.out.println(filename);
		System.out.println("found in collector: "+tc.containsKey(filename));
		System.out.println(texture1);
		System.out.println();
		*/
		
		//	list_frames = new int[frames.length];
		
		//if(DEBUG)System.out.println("Objet "+ID+" - "+"("+mesh_filename+") (type "+format+") "+"(taille "+verticeXmaximum+" "+verticeYmaximum+" "+verticeZmaximum+")");
	}
	
	
	private static void init_MD2_normals() {
		MD2_Normals[0]=	new Point3D(-0.525731f,  0.000000f,  0.850651f ); 
		MD2_Normals[1]= new Point3D(-0.442863f,  0.238856f,  0.864188f );
		MD2_Normals[2]= new Point3D(-0.295242f,  0.000000f,  0.955423f ); 
		MD2_Normals[3]= new Point3D(-0.309017f,  0.500000f,  0.809017f ); 
		MD2_Normals[4]= new Point3D(-0.162460f,  0.262866f,  0.951056f ); 
		MD2_Normals[5]= new Point3D( 0.000000f,  0.000000f,  1.000000f );
		MD2_Normals[6]= new Point3D( 0.000000f,  0.850651f,  0.525731f ); 
		MD2_Normals[7]= new Point3D(-0.147621f,  0.716567f,  0.681718f ); 
		MD2_Normals[8]= new Point3D( 0.147621f,  0.716567f,  0.681718f ); 
		MD2_Normals[9]= new Point3D( 0.000000f,  0.525731f,  0.850651f ); 
		MD2_Normals[10]= new Point3D( 0.309017f,  0.500000f,  0.809017f ); 
		MD2_Normals[11]= new Point3D( 0.525731f,  0.000000f,  0.850651f ); 
		MD2_Normals[12]= new Point3D( 0.295242f,  0.000000f,  0.955423f ); 
		MD2_Normals[13]= new Point3D( 0.442863f,  0.238856f,  0.864188f ); 
		MD2_Normals[14]= new Point3D( 0.162460f,  0.262866f,  0.951056f );
		MD2_Normals[15]= new Point3D(-0.681718f,  0.147621f,  0.716567f ); 
		MD2_Normals[16]= new Point3D(-0.809017f,  0.309017f,  0.500000f );
		MD2_Normals[17]= new Point3D(-0.587785f,  0.425325f,  0.688191f ); 
		MD2_Normals[18]= new Point3D(-0.850651f,  0.525731f,  0.000000f ); 
		MD2_Normals[19]= new Point3D(-0.864188f,  0.442863f,  0.238856f ); 
		MD2_Normals[20]= new Point3D(-0.716567f,  0.681718f,  0.147621f ); 
		MD2_Normals[21]= new Point3D(-0.688191f,  0.587785f,  0.425325f ); 
		MD2_Normals[22]= new Point3D(-0.500000f,  0.809017f,  0.309017f ); 
		MD2_Normals[23]= new Point3D(-0.238856f,  0.864188f,  0.442863f ); 
		MD2_Normals[24]= new Point3D(-0.425325f,  0.688191f,  0.587785f ); 
		MD2_Normals[25]= new Point3D(-0.716567f,  0.681718f, -0.147621f );
		MD2_Normals[26]= new Point3D(-0.500000f,  0.809017f, -0.309017f ); 
		MD2_Normals[27]= new Point3D(-0.525731f,  0.850651f,  0.000000f ); 
		MD2_Normals[28]= new Point3D( 0.000000f,  0.850651f, -0.525731f ); 
		MD2_Normals[29]= new Point3D(-0.238856f,  0.864188f, -0.442863f ); 
		MD2_Normals[30]= new Point3D( 0.000000f,  0.955423f, -0.295242f ); 
		MD2_Normals[31]= new Point3D(-0.262866f,  0.951056f, -0.162460f );
		MD2_Normals[32]= new Point3D( 0.000000f,  1.000000f,  0.000000f ); 
		MD2_Normals[33]= new Point3D( 0.000000f,  0.955423f,  0.295242f );
		MD2_Normals[34]= new Point3D(-0.262866f,  0.951056f,  0.162460f ); 
		MD2_Normals[35]= new Point3D( 0.238856f,  0.864188f,  0.442863f ); 
		MD2_Normals[36]= new Point3D( 0.262866f,  0.951056f,  0.162460f ); 
		MD2_Normals[37]= new Point3D( 0.500000f,  0.809017f,  0.309017f ); 
		MD2_Normals[38]= new Point3D( 0.238856f,  0.864188f, -0.442863f ); 
		MD2_Normals[39]= new Point3D( 0.262866f,  0.951056f, -0.162460f ); 
		MD2_Normals[40]= new Point3D( 0.500000f,  0.809017f, -0.309017f ); 
		MD2_Normals[41]= new Point3D( 0.850651f,  0.525731f,  0.000000f ); 
		MD2_Normals[42]= new Point3D( 0.716567f,  0.681718f,  0.147621f ); 
		MD2_Normals[43]= new Point3D( 0.716567f,  0.681718f, -0.147621f ); 
		MD2_Normals[44]= new Point3D( 0.525731f,  0.850651f,  0.000000f ); 
		MD2_Normals[45]= new Point3D( 0.425325f,  0.688191f,  0.587785f ); 
		MD2_Normals[46]= new Point3D( 0.864188f,  0.442863f,  0.238856f ); 
		MD2_Normals[47]= new Point3D( 0.688191f,  0.587785f,  0.425325f ); 
		MD2_Normals[48]= new Point3D( 0.809017f,  0.309017f,  0.500000f ); 
		MD2_Normals[49]= new Point3D( 0.681718f,  0.147621f,  0.716567f ); 
		MD2_Normals[50]= new Point3D( 0.587785f,  0.425325f,  0.688191f ); 
		MD2_Normals[51]= new Point3D( 0.955423f,  0.295242f,  0.000000f ); 
		MD2_Normals[52]= new Point3D( 1.000000f,  0.000000f,  0.000000f ); 
		MD2_Normals[53]= new Point3D( 0.951056f,  0.162460f,  0.262866f ); 
		MD2_Normals[54]= new Point3D( 0.850651f, -0.525731f,  0.000000f ); 
		MD2_Normals[55]= new Point3D( 0.955423f, -0.295242f,  0.000000f ); 
		MD2_Normals[56]= new Point3D( 0.864188f, -0.442863f,  0.238856f ); 
		MD2_Normals[57]= new Point3D( 0.951056f, -0.162460f,  0.262866f ); 
		MD2_Normals[58]= new Point3D( 0.809017f, -0.309017f,  0.500000f ); 
		MD2_Normals[59]= new Point3D( 0.681718f, -0.147621f,  0.716567f ); 
		MD2_Normals[60]= new Point3D( 0.850651f,  0.000000f,  0.525731f ); 
		MD2_Normals[61]= new Point3D( 0.864188f,  0.442863f, -0.238856f ); 
		MD2_Normals[62]= new Point3D( 0.809017f,  0.309017f, -0.500000f ); 
		MD2_Normals[63]= new Point3D( 0.951056f,  0.162460f, -0.262866f ); 
		MD2_Normals[64]= new Point3D( 0.525731f,  0.000000f, -0.850651f ); 
		MD2_Normals[65]= new Point3D( 0.681718f,  0.147621f, -0.716567f ); 
		MD2_Normals[66]= new Point3D( 0.681718f, -0.147621f, -0.716567f ); 
		MD2_Normals[67]= new Point3D( 0.850651f,  0.000000f, -0.525731f ); 
		MD2_Normals[68]= new Point3D( 0.809017f, -0.309017f, -0.500000f ); 
		MD2_Normals[69]= new Point3D( 0.864188f, -0.442863f, -0.238856f ); 
		MD2_Normals[70]= new Point3D( 0.951056f, -0.162460f, -0.262866f ); 
		MD2_Normals[71]= new Point3D( 0.147621f,  0.716567f, -0.681718f ); 
		MD2_Normals[72]= new Point3D( 0.309017f,  0.500000f, -0.809017f ); 
		MD2_Normals[73]= new Point3D( 0.425325f,  0.688191f, -0.587785f ); 
		MD2_Normals[74]= new Point3D( 0.442863f,  0.238856f, -0.864188f ); 
		MD2_Normals[75]= new Point3D( 0.587785f,  0.425325f, -0.688191f ); 
		MD2_Normals[76]= new Point3D( 0.688191f,  0.587785f, -0.425325f ); 
		MD2_Normals[77]= new Point3D(-0.147621f,  0.716567f, -0.681718f ); 
		MD2_Normals[78]= new Point3D(-0.309017f,  0.500000f, -0.809017f ); 
		MD2_Normals[79]= new Point3D( 0.000000f,  0.525731f, -0.850651f ); 
		MD2_Normals[80]= new Point3D(-0.525731f,  0.000000f, -0.850651f ); 
		MD2_Normals[81]= new Point3D(-0.442863f,  0.238856f, -0.864188f ); 
		MD2_Normals[82]= new Point3D(-0.295242f,  0.000000f, -0.955423f ); 
		MD2_Normals[83]= new Point3D(-0.162460f,  0.262866f, -0.951056f ); 
		MD2_Normals[84]= new Point3D( 0.000000f,  0.000000f, -1.000000f ); 
		MD2_Normals[85]= new Point3D( 0.295242f,  0.000000f, -0.955423f ); 
		MD2_Normals[86]= new Point3D( 0.162460f,  0.262866f, -0.951056f ); 
		MD2_Normals[87]= new Point3D(-0.442863f, -0.238856f, -0.864188f ); 
		MD2_Normals[88]= new Point3D(-0.309017f, -0.500000f, -0.809017f ); 
		MD2_Normals[89]= new Point3D(-0.162460f, -0.262866f, -0.951056f ); 
		MD2_Normals[90]= new Point3D( 0.000000f, -0.850651f, -0.525731f ); 
		MD2_Normals[91]= new Point3D(-0.147621f, -0.716567f, -0.681718f ); 
		MD2_Normals[92]= new Point3D( 0.147621f, -0.716567f, -0.681718f ); 
		MD2_Normals[93]= new Point3D( 0.000000f, -0.525731f, -0.850651f ); 
		MD2_Normals[94]= new Point3D( 0.309017f, -0.500000f, -0.809017f ); 
		MD2_Normals[95]= new Point3D( 0.442863f, -0.238856f, -0.864188f ); 
		MD2_Normals[96]= new Point3D( 0.162460f, -0.262866f, -0.951056f ); 
		MD2_Normals[97]= new Point3D( 0.238856f, -0.864188f, -0.442863f ); 
		MD2_Normals[98]= new Point3D( 0.500000f, -0.809017f, -0.309017f ); 
		MD2_Normals[99]= new Point3D( 0.425325f, -0.688191f, -0.587785f ); 
		MD2_Normals[100]= new Point3D( 0.716567f, -0.681718f, -0.147621f ); 
		MD2_Normals[101]= new Point3D( 0.688191f, -0.587785f, -0.425325f ); 
		MD2_Normals[102]= new Point3D( 0.587785f, -0.425325f, -0.688191f ); 
		MD2_Normals[103]= new Point3D( 0.000000f, -0.955423f, -0.295242f ); 
		MD2_Normals[104]= new Point3D( 0.000000f, -1.000000f,  0.000000f ); 
		MD2_Normals[105]= new Point3D( 0.262866f, -0.951056f, -0.162460f ); 
		MD2_Normals[106]= new Point3D( 0.000000f, -0.850651f,  0.525731f ); 
		MD2_Normals[107]= new Point3D( 0.000000f, -0.955423f,  0.295242f ); 
		MD2_Normals[108]= new Point3D( 0.238856f, -0.864188f,  0.442863f ); 
		MD2_Normals[109]= new Point3D( 0.262866f, -0.951056f,  0.162460f ); 
		MD2_Normals[110]= new Point3D( 0.500000f, -0.809017f,  0.309017f ); 
		MD2_Normals[111]= new Point3D( 0.716567f, -0.681718f,  0.147621f ); 
		MD2_Normals[112]= new Point3D( 0.525731f, -0.850651f,  0.000000f ); 
		MD2_Normals[113]= new Point3D(-0.238856f, -0.864188f, -0.442863f ); 
		MD2_Normals[114]= new Point3D(-0.500000f, -0.809017f, -0.309017f ); 
		MD2_Normals[115]= new Point3D(-0.262866f, -0.951056f, -0.162460f ); 
		MD2_Normals[116]= new Point3D(-0.850651f, -0.525731f,  0.000000f ); 
		MD2_Normals[117]= new Point3D(-0.716567f, -0.681718f, -0.147621f ); 
		MD2_Normals[118]= new Point3D(-0.716567f, -0.681718f,  0.147621f ); 
		MD2_Normals[119]= new Point3D(-0.525731f, -0.850651f,  0.000000f ); 
		MD2_Normals[120]= new Point3D(-0.500000f, -0.809017f,  0.309017f ); 
		MD2_Normals[121]= new Point3D(-0.238856f, -0.864188f,  0.442863f ); 
		MD2_Normals[122]= new Point3D(-0.262866f, -0.951056f,  0.162460f ); 
		MD2_Normals[123]= new Point3D(-0.864188f, -0.442863f,  0.238856f ); 
		MD2_Normals[124]= new Point3D(-0.809017f, -0.309017f,  0.500000f ); 
		MD2_Normals[125]= new Point3D(-0.688191f, -0.587785f,  0.425325f ); 
		MD2_Normals[126]= new Point3D(-0.681718f, -0.147621f,  0.716567f ); 
		MD2_Normals[127]= new Point3D(-0.442863f, -0.238856f,  0.864188f ); 
		MD2_Normals[128]= new Point3D(-0.587785f, -0.425325f,  0.688191f ); 
		MD2_Normals[129]= new Point3D(-0.309017f, -0.500000f,  0.809017f ); 
		MD2_Normals[130]= new Point3D(-0.147621f, -0.716567f,  0.681718f ); 
		MD2_Normals[131]= new Point3D(-0.425325f, -0.688191f,  0.587785f ); 
		MD2_Normals[132]= new Point3D(-0.162460f, -0.262866f,  0.951056f ); 
		MD2_Normals[133]= new Point3D( 0.442863f, -0.238856f,  0.864188f ); 
		MD2_Normals[134]= new Point3D( 0.162460f, -0.262866f,  0.951056f ); 
		MD2_Normals[135]= new Point3D( 0.309017f, -0.500000f,  0.809017f ); 
		MD2_Normals[136]= new Point3D( 0.147621f, -0.716567f,  0.681718f ); 
		MD2_Normals[137]= new Point3D( 0.000000f, -0.525731f,  0.850651f ); 
		MD2_Normals[138]= new Point3D( 0.425325f, -0.688191f,  0.587785f ); 
		MD2_Normals[139]= new Point3D( 0.587785f, -0.425325f,  0.688191f ); 
		MD2_Normals[140]= new Point3D( 0.688191f, -0.587785f,  0.425325f ); 
		MD2_Normals[141]= new Point3D(-0.955423f,  0.295242f,  0.000000f ); 
		MD2_Normals[142]= new Point3D(-0.951056f,  0.162460f,  0.262866f ); 
		MD2_Normals[143]= new Point3D(-1.000000f,  0.000000f,  0.000000f ); 
		MD2_Normals[144]= new Point3D(-0.850651f,  0.000000f,  0.525731f ); 
		MD2_Normals[145]= new Point3D(-0.955423f, -0.295242f,  0.000000f ); 
		MD2_Normals[146]= new Point3D(-0.951056f, -0.162460f,  0.262866f ); 
		MD2_Normals[147]= new Point3D(-0.864188f,  0.442863f, -0.238856f ); 
		MD2_Normals[148]= new Point3D(-0.951056f,  0.162460f, -0.262866f ); 
		MD2_Normals[149]= new Point3D(-0.809017f,  0.309017f, -0.500000f ); 
		MD2_Normals[150]= new Point3D(-0.864188f, -0.442863f, -0.238856f ); 
		MD2_Normals[151]= new Point3D(-0.951056f, -0.162460f, -0.262866f ); 
		MD2_Normals[152]= new Point3D(-0.809017f, -0.309017f, -0.500000f ); 
		MD2_Normals[153]= new Point3D(-0.681718f,  0.147621f, -0.716567f ); 
		MD2_Normals[154]= new Point3D(-0.681718f, -0.147621f, -0.716567f ); 
		MD2_Normals[155]= new Point3D(-0.850651f,  0.000000f, -0.525731f ); 
		MD2_Normals[156]= new Point3D(-0.688191f,  0.587785f, -0.425325f ); 
		MD2_Normals[157]= new Point3D(-0.587785f,  0.425325f, -0.688191f ); 
		MD2_Normals[158]= new Point3D(-0.425325f,  0.688191f, -0.587785f ); 
		MD2_Normals[159]= new Point3D(-0.425325f, -0.688191f, -0.587785f ); 
		MD2_Normals[160]= new Point3D(-0.587785f, -0.425325f, -0.688191f ); 
		MD2_Normals[161]= new Point3D(-0.688191f, -0.587785f, -0.425325f );
	}
	
	
	/**
	 * Load MD2 model.
	 * @param filePath Path to model
	 * @return true on success, false on failure
	 */
	private boolean loadFileMD2(String filePath) 
	{
		try
		{
			// Attempt to get URL
			URL url = this.getClass().getClassLoader().getResource(filePath);  
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
			int numGlCommands    = byteLoader.readInt();
			int numFrames        = byteLoader.readInt();
			int offsetSkins      = byteLoader.readInt();
			int offsetTexCoords  = byteLoader.readInt();
			int offsetTriangles	 = byteLoader.readInt();
			int offsetFrames     = byteLoader.readInt();
			
			// Check header version
			if (version != 8)
			{
				System.out.println("MD2 version error : version 8 expected");
				return false;
			}
			
			// Read in uv coords
			byteLoader.setFileOffset(offsetTexCoords);                        
			uvs = new float[numTexCoords * 2];
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
			
			// Allocate frames
			frames = new AnimFrame[numFrames];
			
			// Allocate interpolated frame
			interpolatedFrame = new AnimFrame();
			interpolatedFrame.vertices = new float[numVertices * 3];
			interpolatedFrame.normal = new int[numVertices];
			
			// Move file pointer to frames
			byteLoader.setFileOffset(offsetFrames);
			
			// Store some vars outside for speed
			float[] scale = new float[3];
			float[] translate = new float[3];                        
			AnimFrame curFrame;
			int tempx;
			int tempy;
			int tempz;     
			int normalindex;
			
			// Read in all frames
			for (int i = 0; i < numFrames; i++)
			{
				// Allocate space for frame
				frames[i] = new AnimFrame();
				
				// Allocate vertices
				frames[i].vertices = new float[numVertices * 3];
				frames[i].normal = new int[numVertices];
				// Get current frame for readability
				curFrame = frames[i];                                
				
				// Read scale, translation and name
				scale[0] = byteLoader.readFloat();
				scale[1] = byteLoader.readFloat();
				scale[2] = byteLoader.readFloat();
				translate[0] = byteLoader.readFloat();
				translate[1] = byteLoader.readFloat();
				translate[2] = byteLoader.readFloat();
				curFrame.name = byteLoader.readString(16);
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
					// Convert and add vertex (swapping z and y)
					curFrame.vertices[(v*3)] = tempx * scale[0] + translate[0];
					curFrame.vertices[(v*3)+2] = -1 * (tempy * scale[1] + translate[1]);
					curFrame.vertices[(v*3)+1] = tempz * scale[2] + translate[2];
					
					//set maximums bounds
					if(curFrame.vertices[(v*3)]>verticeXmaximum)
						verticeXmaximum=curFrame.vertices[(v*3)];
					if(curFrame.vertices[(v*3)+1]>verticeYmaximum)
						verticeYmaximum=curFrame.vertices[(v*3)+1];
					if(curFrame.vertices[(v*3)+2]>verticeZmaximum)
						verticeZmaximum=curFrame.vertices[(v*3)+2];
					
					//set minimums bounds
					if(curFrame.vertices[(v*3)]<verticeXminimum)
						verticeXminimum=curFrame.vertices[(v*3)];
					if(curFrame.vertices[(v*3)+1]<verticeYminimum)
						verticeYminimum=curFrame.vertices[(v*3)+1];
					if(curFrame.vertices[(v*3)+2]<verticeZminimum)
						verticeZminimum=curFrame.vertices[(v*3)+2];
					
					curFrame.normal[v] = normalindex;
				}
			}
			if(DEBUG) System.out.println("nb points: "+numVertices);
			
			// calculate bounding sphere
			float xcenter = verticeXminimum+(verticeXmaximum-verticeXminimum)/2;
			float ycenter = verticeYminimum+(verticeYmaximum-verticeYminimum)/2;
			float zcenter = verticeZminimum+(verticeZmaximum-verticeZminimum)/2;
			sphereCenter = new Point3D(xcenter, ycenter, zcenter);
			radius = verticeXmaximum-xcenter;
			float temp_radius = verticeYmaximum-ycenter;
			if(temp_radius > radius) radius = temp_radius;
			temp_radius = verticeZmaximum-zcenter;
			if(temp_radius > radius) radius = temp_radius;
		}
		catch (Exception e)
		{
			System.out.println("GE:loadFileMD2 says file not found: "+filePath+"\n");
			return false;
		}
		// Success
		return true;
	}
	
	
	
	
	
	
	/**
	 * Allows animation remote launching. The start frame will be the first frame of the animation
	 * with the same name. The last frame will be the first to not have the same name...
	 * @param name the name of the animation inside the MD2 structure (you can get a list of all possible 
	 * names with the method getAnimationsNames)
	 */
	public void startAnimation(String aname) {
		boolean found=false;
		boolean found_end=false;
		
		for(int i=0; i<frames.length;i++) {
			if(frames[i].name.startsWith(aname)){
				startFrame=i;
				found=true;
				break;
			}
		}
		for(int i=startFrame; i<frames.length;i++) {
			if(!frames[i].name.startsWith(aname)){
				endFrame=i-1;
				found_end=true;
				break;
			}
			if(i==frames.length-1)
				i=frames.length-1;
		}
		
		if(!found || !found_end){
			Error.println("animation in "+mesh_filename+" not found: "+aname
					+"\n debug pour chris: startFound="+found+" endFound="+found_end);
			//System.exit(-1);
		}
		if(startFrame==endFrame){
			Error.println("animation in "+mesh_filename+" contains only ONE frame: "+aname);
			//System.exit(-1);
		}
		
		setFrameRange(startFrame,endFrame);
	}
	
	
	/**
	 * Set animation FPS.
	 * @param fps FPS to play animation by
	 */
	private void setFPS(int fps)
	{        
		this.fps = fps;
	}
	
	/**
	 * Toggle animation.
	 * @param enable Enable animation switch
	 */
	private void toggleAnim(boolean enable)
	{
	}
	
	/**
	 * Reset animation.
	 */
	public void resetAnim()
	{
		// Setup current and next anim frames
		curAnimFrame = startFrame;
		nextAnimFrame = startFrame+1;
		lastAnimTime = 0;
	}
	
	
	/**
	 * Set start and end frames.
	 * @param startFrame First animation frame
	 * @param endFrame Last animation frame
	 */
	private void setFrameRange(int startFrame, int endFrame)
	{
		this.startFrame = startFrame;
		this.endFrame = endFrame;
		
		// Limit for the dummies
		if (startFrame < 0) startFrame = 0;
		if (startFrame > frames.length) startFrame = frames.length;
		if (endFrame < 0) endFrame = 0;
		if (endFrame > frames.length) endFrame = frames.length;
		
		// Setup current and next anim frames
		curAnimFrame = startFrame;
	}
	
	/** 
	 * Create interpolated frame.
	 */
	private void createInterpolatedFrame()
	{
		long curTime = System.currentTimeMillis();
		long elapsedTime = curTime - lastAnimTime;
		
		// Get interpolation time
		//float t = elapsedTime / (1000.0f / fps);
		
		// Get next frame
		
		
		// Calculate current and next frames
		if(elapsedTime > (1000.0f / fps))
		{
			curAnimFrame = curAnimFrame + 1; 
			if (curAnimFrame > endFrame)
				curAnimFrame = startFrame;
			lastAnimTime = curTime;
		}
		
		
		/*
		 // Interpolate frames and store in interpolatedFrame
		  for (int v = 0; v < frames[curAnimFrame].vertices.length; v++)
		  {
		  interpolatedFrame.vertices[v] =
		  (frames[curAnimFrame].vertices[v] + 
		  t * (frames[nextAnimFrame].vertices[v] 
		  - frames[curAnimFrame].vertices[v]));
		  }
		  */	
	}
	
	
	
	
	/**
	 * Gives a list of all animations names you can further call with the method startAnimation.
	 * @return a list containing the names of every animation.
	 */
	public LinkedList<String> getAnimationsNames() {
		LinkedList<String> res = new LinkedList<String>();
		for(int i=0; i<frames.length;i++){
			res.add(frames[i].name);
		}
		return res;
	}
	
	
	public String getCurrentAnimationName(){
		return frames[curAnimFrame].name;
	}
	
	
	
	
	
	
	/**
	 * Draws the object (of type 3DS) to the screen given it's position and orientation.
	 * @param gl gl context (given by the Graphicsengine's display method)
	 * @param px position on x axis
	 * @param py position on y axis
	 * @param pz position on z axis
	 * @param rx rotation over x axis
	 * @param ry rotation over y axis
	 * @param rz rotation over z axis
	 */
	public static boolean forward; /* advance in animation ? */
	public void draw(GLDrawable gld, float px, float py, float pz, float rx, float ry, float rz)
	{
		GL gl = gld.getGL();
		GLU glu = gld.getGLU();
		
		gl.glPushMatrix();
		
		doPosition(gl, px, py, pz, rx, ry, rz);
		
		AnimFrame frame;
		
		if(forward)
		createInterpolatedFrame();
		
		frame = frames[curAnimFrame];
		
		
		if(prem){
			prem=false;
			quadric=glu.gluNewQuadric();
			doExpandTexture(gl);
			
			
			//for(int n=0;n<frames.length;n++){
			//	list_frames[n] = gl.glGenLists(1);
			//	gl.glNewList(list_frames[n], GL.GL_COMPILE);
			
			
			
			//	gl.glEndList();
			//}
		}
		
		
		gl.glBindTexture(GL.GL_TEXTURE_2D, internal_texture1[0]);
		if(isAlphaTextured){
			gl.glEnable(GL.GL_BLEND);   // Enable blending for transparency
		}
		if(DEBUG)
			gl.glTranslatef(0,25,0);
		// Building the mesh
		if(!frame.initialized){
			if(!gl.glIsList(frame.displaylist)){
				frame.displaylist = gl.glGenLists(1);
			}
			gl.glNewList(frame.displaylist,GL.GL_COMPILE);
			gl.glBegin(GL.GL_TRIANGLES);
			for (int i = 0; i < indices.length; i++)
			{
				gl.glNormal3f(MD2_Normals[frame.normal[indices[i]]].X, MD2_Normals[frame.normal[indices[i]]].Y, MD2_Normals[frame.normal[indices[i]]].Z);
				gl.glTexCoord2f(uvs[(uvIndices[i]*2)], uvs[(uvIndices[i]*2)+1]);
				gl.glVertex3f(frame.vertices[(indices[i]*3)], frame.vertices[(indices[i]*3)+1], frame.vertices[(indices[i]*3)+2]);
			}
			gl.glEnd();
			gl.glEndList();
			gl.glCallList(frame.displaylist);
			frame.initialized=true;
		}else{
			gl.glCallList(frame.displaylist);
		}
		
		if(isAlphaTextured){
			gl.glDisable(GL.GL_BLEND);   // Enable blending for transparency
		}
		
		if(myBlood != null){
			myBlood.drawInLoop(gld,
					getPhysicalRep().x(),
					getPhysicalRep().z(),
					getPhysicalRep().y(),
					0,0,0);
			myBlood.tick();
		}
		
		if(GraphicsEngine.keyboardHelper.draw_culling){
			gl.glPushAttrib(GL.GL_ALL_ATTRIB_BITS);
			gl.glDisable(GL.GL_TEXTURE_2D);
			gl.glDisable(GL.GL_DEPTH_TEST);
			gl.glDisable(GL.GL_ALPHA_TEST);
			gl.glColor4f(1f,0f,1f,0.05f);
			gl.glEnable(GL.GL_BLEND);
			gl.glTranslatef(sphereCenter.X,sphereCenter.Y,sphereCenter.Z);
			glu.gluSphere(quadric,radius,5,5);
			gl.glPopAttrib();
		}
		
		gl.glPopMatrix();
		GraphicsEngine.voxel_drawn += indices.length;
	}
	
	
	
	public void DisplayAnimNames(){
		for(int i=0;i<frames.length;i++)
			System.out.println(frames[i].name);
	}
	
	public void Blood(int amount){
		myBlood = new Blood(amount);
	}
}
