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

import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import javax.imageio.ImageIO;
import javax.media.opengl.GL;
import logger.Logger;
import main.ResourceLocator;


/**
 * Recycling already loaded textures is done in TextureFactory.
 * @author cyberchrist
 */
@SuppressWarnings("serial")
public class Texture implements Aspect, Serializable{

	public int tWidth; //texture size in width
	public int tHeight;//texture size in height
	transient public int[] internal_index; //index in video memory
	transient public ByteBuffer texturedata; //the image converted to a format compatible with OpenGL
	public boolean isAlpha; // true = alpha chanel present
	public String filename;
	
	
	
	/**
	 * Constructor of a Texture. You should use TextureFactory. If you contruct from here, it is your responsibility to manage memory.
	 * With this constructor, you can have multiple identical textures into memory.
	 * @param filename disk file
	 */
	protected Texture(String filename) throws IOException{
		this.filename = filename;
		// will be set up with "expandTexture"
		this.internal_index = null;
		this.loadTexturedata(filename);
		if(GraphicsEngine.DEBUG) Logger.printINFO("Texture loaded - "+filename+" (Alpha:"+isAlpha+") "+"(size ="+tWidth+"x"+tHeight+")");
	}
	
	protected Texture(int size) throws IOException{
		this.internal_index = null;
		this.tWidth = size;
		this.tHeight = size;
		//this.isAlpha = true;
		if(GraphicsEngine.DEBUG) Logger.printINFO("Texture empty created - "+"(size ="+tWidth+"x"+tHeight+")");
	}
	
	
	/**
	 * Copy a buffer from main memory to video memory as a texture.
	 * It tries to re-use the texture of a precedently loaded buffer.
	 * @param gl
	 */
	public void doExpandTexture(GL gl){
		
		if(internal_index != null && gl.glIsTexture(internal_index[0])){
			if(GraphicsEngine.DEBUG) Logger.printINFO("("+filename+") Texture optimisation stage avoided one Texture expand!");
			gl.glBindTexture(GL.GL_TEXTURE_2D, internal_index[0]);
		}else{
			internal_index = new int[1];
			gl.glGenTextures(1, internal_index,0);
			gl.glBindTexture(GL.GL_TEXTURE_2D, internal_index[0]);
			
			if(texturedata == null){ // render to empty texture
				if(isAlpha)
					gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA8, tWidth, tHeight, 0, GL.GL_RGBA, GL.GL_INT, null);
				else
					gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGB8, tWidth, tHeight, 0, GL.GL_RGB, GL.GL_INT, null);
			}else{ // normal texture
				if(isAlpha){
					gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA8, tWidth, tHeight, 0, GL.GL_RGBA, GL.GL_UNSIGNED_BYTE, texturedata.rewind());
				}else{
					gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGB8, tWidth, tHeight, 0, GL.GL_RGB, GL.GL_UNSIGNED_BYTE, texturedata.rewind());
				}
			}
			
			gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
			gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
			// free the data buffer (texture is now in VRAM)
			texturedata = null;
		}			
	}
	
	
	/**
	 * Loads a texture file into memory (RAM).
	 * @param fn filename of the texture. > ITS WIDTH AND HEIGHT MUST BE 2^x PIXELS ! (OpenGL usable format) <
	 * @return a buffer containing the texture
	 * @throws IOException 
	 */
	private void loadTexturedata(String fn) throws IOException {
		// get access to file
		InputStream is = ResourceLocator.getRessourceAsStream(fn);
		if(is==null){
			throw new IOException("File not found!");
		}
		// read data to buffer
		BufferedImage buff = ImageIO.read(is); is.close();
		// change orientation of image
		java.awt.geom.AffineTransform tx = java.awt.geom.AffineTransform.getScaleInstance(1, -1); 
		tx.translate(0, -buff.getHeight(null)); 
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR); 
		buff = op.filter(buff, null);
		// get size and verify power-of-two
		tHeight = buff.getHeight();
		tWidth = buff.getWidth();
		if(tWidth <=1 || tHeight <=1){
			throw new IOException("Java returned an empty image buffer!");
		}
		if(!isPowerOfTwo(tWidth) || !isPowerOfTwo(tHeight)){
			throw new IOException("Size non-power of 2 !");
		}
		// from BufferedImage to ByteBuffer...
		Raster raster = buff.getRaster();
		int[] img = null;
		img = raster.getPixels(0, 0, tWidth, tHeight, img);
		if(buff.getType()==BufferedImage.TYPE_CUSTOM ){
			if(buff.getColorModel().hasAlpha()){
			// here we have a RGB (3 channels) + Alpha (1 channel) image
			isAlpha=true;
			texturedata = ByteBuffer.allocateDirect(tWidth * tHeight * 4);
			for (int y=0; y<tHeight; y++)
				for (int x=0; x<tWidth; x++) {
					texturedata.put((byte) img[(y * tWidth + x) * 4 + 0]);
					texturedata.put((byte) img[(y * tWidth + x) * 4 + 1]);
					texturedata.put((byte) img[(y * tWidth + x) * 4 + 2]);
					texturedata.put((byte) img[(y * tWidth + x) * 4 + 3]);
				}
			}else{
				// no alpha, probably an ordinary RGB image
				// TODO or unsupported file format!
				texturedata = ByteBuffer.allocateDirect(tWidth * tHeight * 3);
				for (int y=0; y<tHeight; y++)
					for (int x=0; x<tWidth; x++) {
						texturedata.put((byte) img[(y * tWidth + x) * 3 + 0]);
						texturedata.put((byte) img[(y * tWidth + x) * 3 + 1]);
						texturedata.put((byte) img[(y * tWidth + x) * 3 + 2]);
					}
			}
		}else{
			// type is not CUSTOM
			if(buff.getType()==BufferedImage.TYPE_BYTE_GRAY){
				// a level-of-gray image (only 1 channel)
				isAlpha=false;
				texturedata = ByteBuffer.allocateDirect(tWidth * tHeight * 1);
				for (int y=0; y<tHeight; y++)
					for (int x=0; x<tWidth; x++) {
						texturedata.put((byte) img[(y * tWidth + x)]);
					}
			}else{
				// probably an ordinary RGB image
				//TODO: or unsupported file format!
				texturedata = ByteBuffer.allocateDirect(tWidth * tHeight * 3);
				for (int y=0; y<tHeight; y++)
					for (int x=0; x<tWidth; x++) {
						texturedata.put((byte) img[(y * tWidth + x) * 3 + 0]);
						texturedata.put((byte) img[(y * tWidth + x) * 3 + 1]);
						texturedata.put((byte) img[(y * tWidth + x) * 3 + 2]);
					}
			}
		}
	}
	
	
	private boolean isPowerOfTwo(int v){
		if(Math.log(v)/Math.log(2) == (int)(Math.log(v)/Math.log(2))){
			return true;
		}else
			return false;
	}
	
	
	public void free(GL gl){
		// free OpenGL texture memory
		gl.glDeleteTextures(1, internal_index,0);
		// free all the rest
		this.filename = null;
		this.texturedata = null;
		this.internal_index = null;
	}
	
	
	/* SERIALIZATION */
	private byte[] temptexturedata; // hack to serialize the ByteBuffer of texturedata
	private void writeObject(ObjectOutputStream out) throws IOException{
		temptexturedata = new byte[texturedata.capacity()];
		texturedata.rewind();
		texturedata.get(temptexturedata);
		out.defaultWriteObject();
	}
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException{
		in.defaultReadObject();
		texturedata = ByteBuffer.wrap(temptexturedata);
	}

}
