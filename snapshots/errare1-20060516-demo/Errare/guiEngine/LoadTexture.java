/*Errare is a free and crossplatform MMORPG project.
Copyright (C) 2006  Arnaud KNOBLOCH

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.*/

package guiEngine;

import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Hashtable;
import java.util.LinkedList;
import main.Rep;
import javax.imageio.ImageIO;
import net.java.games.jogl.GL;
import physicsEngine.CharacterPRep;
import physicsEngine.PhysicalRep;


/**
 * Class LoadTexture
 * The same as the graphicsEngine
 * Use to bind texture on a quad
 */
public class LoadTexture{
	


	protected String texture_filename;
	protected boolean isAlphaTextured;
	private boolean png_texture;

	
	protected int tWidth; //texture size in width
	protected int tHeight;//texture size in height
	protected int[] internal_texture1; //index in video memory
	protected int[] internal_texture2; //index in video memory
	protected ByteBuffer texture; //the image converted to a format compatible with OpenGL
	protected boolean prem;
	
	// Texture Collector : internal_index of texture + data
	static protected Hashtable<String, TextureCollectorItem> tc = new Hashtable<String, TextureCollectorItem>();
	protected class TextureCollectorItem{
		public int[] internal_index;
		public boolean isalpha;
		public ByteBuffer data;
		public int tWidth, tHeight;
	}
	

	/**
	 * Loads a file into his representation in memory.
	 * Autodetects the format of the file and loads it the appropriate way.
	 * Note for models: the file must be accompagnated by a jpg texture file with the same name!
	 * i.e. If you want to load the file "~/model_001.md2" it must come with the texture "~/model_001.jpg"
	 * @param filename path to file
	 */
	public LoadTexture() {

		prem=true;
		//sera rempli au moment de l'"expand texture"
		internal_texture1 = new int[1];
		internal_texture2 = new int[1];

	}
	
	
	/**
	 * Basicaly Loads a texture into memory. 
	 * There is a helper: if it cannot find the file, it searches
	 * for the same basename but with a different extension.
	 * Memory and Speed Optimisation: if the texture has already been loaded in the past,
	 * it will use the that texture again (avoids loading the same file
	 * multiple times)
	 * @param filename physical name of the texture image
	 */
	public void load_texture(String filename){
		
		String ext = filename.substring(filename.length()-4,filename.length());
		if(ext.equals(".jpg") || ext.equals(".png")){
			load_texture2(filename);
		}else if(ext.equals(".md2")){
			filename = filename.substring(0,filename.length()-4);
			filename = filename.concat(".png");
			load_texture2(filename);
		}else{
			bad_texture("unsupported file extension");
		}
		
		if(texture==null){System.out.println("texture load ERROR BADDDD!"); System.exit(-1);}
		

	}
	
	
	private void load_texture2(String filename){
		texture_filename = filename;
		if(tc.containsKey(filename)){
			// ensuite le premier objet qui passera par openGL sera "expanded"
			texture = tc.get(filename).data;
			tWidth = tc.get(filename).tWidth;
			tHeight = tc.get(filename).tHeight;
			if(texture==null) bad_texture("je get un texture1 null!!");
		}else{				
			
			try {
				texture = loadTexture(filename);
				TextureCollectorItem texitem = new TextureCollectorItem();
				texitem.data = texture;
				texitem.isalpha = isAlphaTextured;
				texitem.tWidth=tWidth;
				texitem.tHeight=tHeight;
				//je suis dans un etat ou j'ai les donnees en RAM mais je dois encore m'occuper
				//de les envoyer sur la carte graphique : "expand texture"
				tc.put(filename,texitem);
			} catch (IOException e) {
				bad_texture("Error reading texture!");
			}
			
		}
	}
	
	
	
	/**
	 * Handles texture loading errors.
	 * @param msg Some information about the error
	 */
	private void bad_texture(String msg){
		System.out.println("\nTexture ("+texture_filename+") criticaly not loaded : "+msg);
		System.exit(-1);
	}
	
	protected void doPosition(GL gl, float px, float py, float pz, float rx, float ry, float rz){
		gl.glTranslatef(px, py, pz);
		gl.glRotatef(rx, 1, 0, 0);
		gl.glRotatef(ry, 0, 1, 0);
		gl.glRotatef(rz, 0, 0, 1);
	}
	
	protected void doExpandTexture(GL gl){
		if(!tc.containsKey(texture_filename)) {
			
			System.exit(-1);
		}
		isAlphaTextured = tc.get(texture_filename).isalpha;
		
		if(tc.get(texture_filename).internal_index != null){
			internal_texture1=tc.get(texture_filename).internal_index;
			gl.glBindTexture(GL.GL_TEXTURE_2D, internal_texture1[0]);
		}else{
			gl.glGenTextures(1, internal_texture1);
			gl.glBindTexture(GL.GL_TEXTURE_2D, internal_texture1[0]);
			
			if(isAlphaTextured)
				gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, tWidth, tHeight, 0, GL.GL_RGBA, GL.GL_UNSIGNED_BYTE, texture);
			else
				gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGB, tWidth, tHeight, 0, GL.GL_RGB, GL.GL_UNSIGNED_BYTE, texture);
			
			gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
			gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
			
			//je fais un set par effet de bord
			tc.get(texture_filename).internal_index=internal_texture1;
		}			
	}
	
	
	/**
	 * Loads a texture file into memory (RAM)
	 * @param texture the global variable ByteBuffer I/O'ed (usable by openGL)
	 * @param fn filename of the texture. > ITS WIDTH AND HEIGHT MUST BE 2^x PIXELS ! (openGL usable format) <
	 * @return
	 * @throws IOException 
	 */
	private ByteBuffer loadTexture(String fn) throws IOException {
		if( fn.substring(fn.length()-4,fn.length()) .equals(".png"))
			png_texture=true;
		else
			png_texture=false;
		
		ByteBuffer texture =null;
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(fn);
		if(is==null){
			bad_texture("file not found!");
		}
		BufferedImage buff = ImageIO.read(is);
		java.awt.geom.AffineTransform tx = java.awt.geom.AffineTransform.getScaleInstance(1, -1); 
		tx.translate(0, -buff.getHeight(null)); 
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR); 
		buff = op.filter(buff, null);
		tHeight = buff.getHeight();
		tWidth = buff.getWidth();
		if(tWidth <=1 || tHeight <=1){
			bad_texture("Java returned an empty image buffer!");
		}
		if(!isPowerOfTwo(tWidth) || !isPowerOfTwo(tHeight)){
			bad_texture("size non-power of 2 !");
		}
		Raster raster = buff.getRaster();
		int[] img = null;
		img = raster.getPixels(0, 0, tWidth, tHeight, img);
		if(buff.getType()==BufferedImage.TYPE_CUSTOM ){
			/* here we have a RGB (3 channels) + Alpha (1 channel) image */
			isAlphaTextured=true;
			texture = ByteBuffer.allocateDirect(tWidth * tHeight * 4);
			for (int y=0; y<tHeight; y++)
				for (int x=0; x<tWidth; x++) {
					texture.put((byte) img[(y * tWidth + x) * 4 + 0]);
					texture.put((byte) img[(y * tWidth + x) * 4 + 1]);
					texture.put((byte) img[(y * tWidth + x) * 4 + 2]);
					texture.put((byte) img[(y * tWidth + x) * 4 + 3]);
				}
		}else{
			/* no alpha channel */
			isAlphaTextured=false;
			if(buff.getType()==BufferedImage.TYPE_BYTE_GRAY){
				/* a level-of-gray image (only 1 channel) */
				texture = ByteBuffer.allocateDirect(tWidth * tHeight * 1);
				for (int y=0; y<tHeight; y++)
					for (int x=0; x<tWidth; x++) {
						texture.put((byte) img[(y * tWidth + x)]);
					}
			}else{
				/* ordinary RGB image */
				/** TODO or unsupported file format! */
				texture = ByteBuffer.allocateDirect(tWidth * tHeight * 3);
				for (int y=0; y<tHeight; y++)
					for (int x=0; x<tWidth; x++) {
						texture.put((byte) img[(y * tWidth + x) * 3 + 0]);
						texture.put((byte) img[(y * tWidth + x) * 3 + 1]);
						texture.put((byte) img[(y * tWidth + x) * 3 + 2]);
					}
			}
		}
		
		return texture;
	}
	
	
	private boolean isPowerOfTwo(int v){
		if(Math.log(v)/Math.log(2) == (int)(Math.log(v)/Math.log(2))){
			return true;
		}else
			return false;
	}
	
	

	/*************************************** GETTERS SETTERS ************************************/


	public ByteBuffer getTexture() {
		return texture;
	}


	public void setTexture(ByteBuffer texture) {
		this.texture = texture;
	}


	public int getTHeight() {
		return tHeight;
	}


	public void setTHeight(int height) {
		tHeight = height;
	}


	public int getTWidth() {
		return tWidth;
	}


	public void setTWidth(int width) {
		tWidth = width;
	}

	
}
