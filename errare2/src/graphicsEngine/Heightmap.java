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

import java.io.IOException;
import java.nio.DoubleBuffer;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;

import geom.Point;
import geom.Vector;
import graphicsEngine.glsl.GLSLShaderFactory;
import graphicsEngine.glsl.LinkedShader;
import logger.Logger;

/**
 * Represents a terrain, constructed from a heightmap (a simple array of heights). It also manages water.
 * @author Christophe
 */
public class Heightmap extends Graphics{
	
	// quality :
	static final public int Patch_size = 32;
	
	// shape :
	transient protected static float MAP_STEP_WORLD = 10; //OpenGL units between two consecutive points in the map
	private float ZScale = 100; //units OpenGL
	
	// color :
	protected Texture terrainTex;
	
	//transient private int list_herbes;
	//public int nb_lists;
	
	public int nb_texrepet;
	public boolean disable_edge_flag = false;
	
	
	transient private int list_terrain;
	//transient private int list_terrain_patched;
	protected float[][] heightmap; //contient les hauteurs
	private Point[][] inter_norm; //contient la normale en chaque point
	transient private boolean must_recompile;
	transient private boolean must_recompile_partial;
	transient private int isx,iex,isy,iey; //bounds to update (index of patch)
	transient public Water water;

	/**
	 * Create a terrain from a heightmap and a texture.
	 * @param heights array of heights (values should be in [0;1] where 0 means low and 1 means high)
	 * @param ZScale maximum height value in OpenGL units atteigned when a height in the array is equal to 1
	 * @param step_world space of 2 consecutive points of the terrain in OpenGL units
	 * @param terrainTexture path to the texture of the terrain (it is scaled as 1 onto the terrain)
	 * @param sea_level height of the sea level in OpenGL units
	 * @param waterTexture path to the texture of the water (you can further set the scale which defaults to 1 onto the terrain)
	 * @param waterTransparency transparency of the water
	 */
	public Heightmap(float[][] heights, float ZScale, float step_world, String terrainTexture, float sea_level, String waterTexture, float waterTransparency) {
		MAP_STEP_WORLD = step_world;
		this.ZScale = ZScale;
		this.heightmap = heights;
		try {
			this.terrainTex = TextureFactory.loadTexture(terrainTexture);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.water = new Water(sea_level);
	}
	
	/**
	 * Change the color of the water. The color is used together with the water texture, usually you will want the color here to be r=1,b=1,g=1.
	 * @param r red componant
	 * @param g green componant
	 * @param b blue componant
	 */
	public void setWaterColor(float r, float g, float b){
		if(this.water!=null){ // if we have Water in this world
			this.water.setColor(r,g,b); // and recompile water
		}
	}
	
	public void setWaterScale(float s){
		if(this.water!=null){ // if we have Water in this world
			this.water.setScale(s); // and recompile water
		}
	}
	
	private void compute_normals(){
		inter_norm = new Point[heightmap.length][heightmap[0].length];
		for(int i=0;i<heightmap.length;i++){
			for(int j=0;j<heightmap[0].length;j++){
				inter_norm[i][j] = new Point();
			}
		}
		Point P1,P2,P3,P4,V1,V2,V3;
		P1=new Point();
		P2=new Point();
		P3=new Point();
		P4=new Point();
		V1=new Point();
		V2=new Point();
		V3=new Point();
		float incx,incy,incz,norme;
		
		for(int i=0;i<heightmap.length-1;i++)
			for(int j=0;j<heightmap[0].length-1;j++){
				getVector(i,j,P1);
				getVector(i+1,j,P2);
				getVector(i+1,j+1,P3);
				getVector(i,j+1,P4);
				
				// v1.x = longueur selon x d'un cote du carre
				V1.x=P2.x - P1.x; V1.y=P2.y - P1.y; V1.z=P2.z - P1.z;
				V2.x=P3.x - P1.x; V2.y=P3.y - P1.y; V2.z=P3.z - P1.z;
				V3.x=P4.x - P1.x; V3.y=P4.y - P1.y; V3.z=P4.z - P1.z;
				
				// v2 ^ v1 (produit vectoriel)
                // incx = composante en x d'un vecteur orthogonal a v1 et v2
				incx=V2.y*V1.z-V1.y*V2.z;
				incy=V2.z*V1.x-V1.z*V2.x;
				incz=V2.x*V1.y-V1.x*V2.y;
				
				norme=(float)Math.sqrt(incx*incx+incy*incy+incz*incz);
				incx/=norme; incy/=norme; incz/=norme;
				
				// normales en P1 P2 P3
				inter_norm[i][j].x-=incx; inter_norm[i][j].y-=incy; inter_norm[i][j].z-=incx;
				inter_norm[i+1][j].x-=incx; inter_norm[i+1][j].y-=incy; inter_norm[i+1][j].z-=incx;
				inter_norm[i+1][j+1].x-=incx; inter_norm[i+1][j+1].y-=incy; inter_norm[i+1][j+1].z-=incx;

				// v3 ^ v2
                // inc = vecteur orthogonal a v2 et v3
				incx=V3.y*V2.z-V2.y*V3.z;
				incy=V3.z*V2.x-V2.z*V3.x;
				incz=V3.x*V2.y-V2.x*V3.y;
				
				// ajout des normales en P1 P3 P4
				inter_norm[i][j].x-=incx; inter_norm[i][j].y-=incy; inter_norm[i][j].z-=incx;
				inter_norm[i+1][j+1].x-=incx; inter_norm[i+1][j+1].y-=incy; inter_norm[i+1][j+1].z-=incx;
				inter_norm[i][j+1].x-=incx; inter_norm[i][j+1].y-=incy; inter_norm[i][j+1].z-=incx;
				
				Vector vtemp = new Vector(inter_norm[i][j]);
				vtemp.normalize();
				inter_norm[i][j].x = vtemp.x;
				inter_norm[i][j].y = vtemp.y;
				inter_norm[i][j].z = vtemp.z;
			}
	}
	
	
	private void getVector(int i, int j, Point P){
		P.x = i * MAP_STEP_WORLD;
		P.y = heightmap[i][j];
		P.z = j * MAP_STEP_WORLD;
	}
	
	/*public void test(){
		for (int i=0;i<heightmap.length-1;i++) {
			for (int j=0;j<heightmap[0].length;j++) {
				testdrawVertex(i+1,j);
				testdrawVertex(i,j);	
			}
		}
	}
	private void testdrawVertex(int i, int j){
		float alt = heightmap[i][j] * ZScale;
		float t = (float)i/heightmap.length;
		t = 1f - (float)j/heightmap[0].length;
		//gl.glNormal3f(inter_norm[i][j].x,inter_norm[i][j].y,inter_norm[i][j].z);
		t=i * MAP_STEP
				+ decalage
				;
		t = alt;
		t = 		j * MAP_STEP
				+ decalage;
	}*/
	
	private void compile_terrain(GL gl){
		compute_normals();
		
		if(!gl.glIsList(list_terrain))
			list_terrain = gl.glGenLists(1);
		
		gl.glBindTexture(GL.GL_TEXTURE_2D, terrainTex.internal_index[0]);
		gl.glNewList(list_terrain, GL.GL_COMPILE);
		for (int i=0;i<heightmap.length-1;i++) {
			gl.glBegin(GL.GL_TRIANGLE_STRIP);
			for (int j=0;j<heightmap[0].length;j++) {
				
				drawVertex(gl, i+1,j);
				drawVertex(gl, i,j);
					
			}
			gl.glEnd();
		}
		gl.glEndList();
	}
	
	/*private void compile_terrain_partial(GL gl){
		int nb_patches_x = nb_div/Patch_size;
		int nb_patches_y = nb_div/Patch_size;
		nb_lists = (nb_div/Patch_size) * (nb_div/Patch_size);
		int list_iterator = 0;
		
		if(!gl.glIsList(list_terrain_patched)){
			list_terrain_patched = gl.glGenLists(nb_lists);
			for(int x=0;x<nb_patches_x;x++){
				for(int y=0;y<nb_patches_y;y++){	
					compile_this_partial(gl, list_iterator, x,y);
					list_iterator++;
				}
			}
		}else{ //gl.glIsList(list_terrain_patched)
			
			for(int x=isx;x<iex;x++){
				for(int y=isy;y<iey;y++){
					list_iterator = x*nb_patches_x + y;
					compile_this_partial(gl, list_iterator, x, y);
				}
			}
		}
	}
	*/
	
	/*protected void compile_this_partial(GL gl, int list_index, int x, int y){
		gl.glNewList(list_terrain_patched + list_index, GL.GL_COMPILE);
		for (int i=x*Patch_size;i<=(x+1)*Patch_size;i++) {
			gl.glBegin(GL.GL_TRIANGLE_STRIP);
			for (int j=y*Patch_size;j<=(y+1)*Patch_size;j++) {
				
				if((i+1)<heightmap.length && j<heightmap[0].length){
				drawVertex(gl, i+1,j);
				drawVertex(gl, i,j);
				}
			}
			gl.glEnd();
		}
		
		gl.glEndList();
	}
	*/
	
	protected void drawVertex(GL gl, int i, int j){
		float alt = heightmap[i][j] * ZScale;
		gl.glTexCoord2f((float)i/heightmap.length,1f-(float)j/heightmap[0].length);
		//gl.glNormal3f(inter_norm[i][j].x,inter_norm[i][j].y,inter_norm[i][j].z);
		gl.glVertex3f(i * MAP_STEP_WORLD
				, alt,
				j * MAP_STEP_WORLD
				);
	}


	protected void draw_terrain(GL gl){
		gl.glPushMatrix();
		
		if(prem){
			prem=false;
			terrainTex.doExpandTexture(gl);
			compile_terrain(gl);
		}
		if(must_recompile){
			must_recompile=false;
			//compile_terrain(gl);
		}
		if(must_recompile_partial){
			must_recompile_partial=false;
			//compile_terrain_partial(gl);
		}
		
		/*************************************/
		if(KeyboardHelper.terrain){
		//if(gl.glIsList(list_terrain_patched)){
		//	for(int it=0;it<nb_lists;it++){
		//		gl.glCallList(list_terrain_patched+it); // Affichage du terrain
		//	}
		//}else{ // terrain not patched
			gl.glBindTexture(GL.GL_TEXTURE_2D, terrainTex.internal_index[0]);
			
			gl.glCallList(list_terrain); // Affichage du terrain
		//}
		GraphicsEngine.triangles_drawn += heightmap.length*heightmap[0].length*2; // triangles strip
		}
		/*************************************/
		gl.glPopMatrix();
	}
	
	
	@Override
	protected void draw(GLAutoDrawable gld) {
		GL gl = gld.getGL();
		draw_terrain(gl);
		water.draw(gld);
	}

	public void notifyModifiedHeightmaps(int indexstartx, int indexendx, int indexstarty, int indexendy) {
		isx=indexstartx;
		iex=indexendx;
		isy=indexstarty;
		iey=indexendy;
		must_recompile_partial = true;
	}
	
	public String toString(){
		return "Heightmap";
	}


	@Override
	protected void drawByTriangles(GLAutoDrawable gld) {
		// TODO Auto-generated method stub
		
	}


	@Override
	protected void drawGeometryOnly(GLAutoDrawable gld) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
	
	
	
	
	
	public class Water extends Graphics {
		public static final String STD_WATER = "data/images/floor/water2.png";
		transient public float sea_level = -2; // which altitude is the water at, in opengl units
		transient private float phase = 0f; // for waves
		private Texture tex;
		transient private int list_water; // display list  handle
		transient private boolean must_recompile; // rebuild the display list ?
		transient private float scale = 1;
		private float[] color = {1f,1f,1f,1f};
		private float transparency; // how much transparent the water is [0;1]

		String shadername = "graphicsEngine/glsl/water";
		private String VERTEX_SHADER_SOURCE_FILE = shadername + ".vert";
		private String FRAGMENT_SHADER_SOURCE_FILE = shadername + ".frag";
		LinkedShader shader; // the water shader !
		int _lightPos, _cameraPos, _waterColor; // parameters to the water shader
		private Texture nmap,dudvmap,water; // textures used by the water shader
		public int texture_quality = 512;
		
		
		/**
		 * Place water in the world.
		 * @param sea_level altitude of water in world coordinate
		 * @param transparency value in [0;1] where 0 is opaque and 1 is fully transparent
		 * @param texturepath path to texture file representing color of water
		 * @param scaleU 
		 * @param scaleV
		 */
		public Water(float sea_level, float transparency, String texturepath, float scaleU, float scaleV) {
			try {
				tex = TextureFactory.loadTexture(texturepath);
			} catch (IOException e) {
				Logger.printERROR("STD_WATER "+STD_WATER+ ": file not found !");
				System.exit(0);
			}
			this.sea_level = sea_level;
			
			try {
				nmap = TextureFactory.loadTexture("graphicsEngine/glsl/water_nmap.png");
				dudvmap = TextureFactory.loadTexture("graphicsEngine/glsl/water_dudvmap.png");
				water = TextureFactory.loadTexture("graphicsEngine/glsl/water.png");
			} catch (IOException e) {
				Logger.printERROR("Water : some texture for the shader cannot be loaded !");
				Logger.printExceptionERROR(e);
			}
			this.clipPlaneBuffer = DoubleBuffer.wrap(new double[]{0.0,1.0,0.0,-sea_level});
		}
		
		/**
		 * Easy water with default texture, texture repetition and transparency. 
		 * @param sea_level altitude of water in world coordinate
		 */
		public Water(float sea_level){
			// 50% transparency and repeat texture 10 * 10 times
			this(sea_level, 0.5f, STD_WATER, 5, 5);
		}
		
		public void setColor(float r, float g, float b){
			this.color = new float[]{r,g,b,transparency};
			this.must_recompile = true;
		}
		
		public void setScale(float s){
			this.scale = s;
			this.must_recompile = true;
		}		
		
		
		
		float kNormalMapScale = 0.25f;
		float g_WaterFlow = 0.03f;
		float g_WaterUV = 12.0f;
		
		@Override
		public void draw(GLAutoDrawable gld){
			GL gl = gld.getGL();
			gl.glPushAttrib(GL.GL_ALL_ATTRIB_BITS);
			gl.glPushMatrix();
			if(prem){
				prem=false;
				tex.doExpandTexture(gl);
				nmap.doExpandTexture(gl);
				dudvmap.doExpandTexture(gl);
				compile_water(gl);
				if(Extensions.isGLSLSupported) this.shader = GLSLShaderFactory.loadGLSLShader(VERTEX_SHADER_SOURCE_FILE, FRAGMENT_SHADER_SOURCE_FILE);
				if(Extensions.isGLSLSupported) initShaders(gl);
			}
			if(must_recompile){
				must_recompile=false;
				compile_water(gl);
			}
			
			
			if(KeyboardHelper.watershader){
				shader.bind(gl);
				// TEXTURE-UNIT #0
		        gl.glActiveTexture(GL.GL_TEXTURE0);
		        gl.glEnable(GL.GL_TEXTURE_2D);
		        gl.glBindTexture(GL.GL_TEXTURE_2D, ge.reflection.reflection.internal_index[0]);
		        int reflecloc = gl.glGetUniformLocationARB(shader.getHandle(), "reflection");
		        gl.glUniform1iARB(reflecloc,0);
		        // TEXTURE-UNIT #1:
		        gl.glActiveTexture(GL.GL_TEXTURE1);
		        gl.glEnable(GL.GL_TEXTURE_2D);
		       // gl.glBindTexture(GL.GL_TEXTURE_2D, ge.refraction.internal_index[0]);
		       // int refracloc = gl.glGetUniformLocationARB(shader.getHandle(), "refraction");
		       // gl.glUniform1iARB(refracloc,1);
		        // TEXTURE-UNIT #2
		        gl.glActiveTexture(GL.GL_TEXTURE2);
		        gl.glEnable(GL.GL_TEXTURE_2D);
		        gl.glBindTexture(GL.GL_TEXTURE_2D, nmap.internal_index[0]);
		        int nmaploc = gl.glGetUniformLocationARB(shader.getHandle(), "normalMap");
		        gl.glUniform1iARB(nmaploc,2);
		        // TEXTURE-UNIT #3:
		        gl.glActiveTexture(GL.GL_TEXTURE3);
		        gl.glEnable(GL.GL_TEXTURE_2D);
		        gl.glBindTexture(GL.GL_TEXTURE_2D, dudvmap.internal_index[0]);
		        int dudvloc = gl.glGetUniformLocationARB(shader.getHandle(), "dudvMap");
		        gl.glUniform1iARB(dudvloc,3);
				
		        
		        gl.glUniform4fvARB(_lightPos,1,ge.world.sun.getPositionBufferInvertedY(),0); //TODO: I think there is an error in the shader water.vert or water.frag : I should not need to take -y  !
				gl.glUniform4fvARB(_cameraPos,1,ge.world.cam.getPositionBuffer(),0);
				gl.glUniform4fvARB(_waterColor,1,this.color,0);
				
				
				// Use this variable for the normal map and make it slower
				// than the refraction map's speed.  We want the refraction
				// map to be jittery, but not the normal map's waviness.
				float move2 = phase * kNormalMapScale;

				// Set the refraction map's UV coordinates to our global g_WaterUV
				float refrUV = g_WaterUV;

				// Set our normal map's UV scale and shrink it by kNormalMapScale
				float normalUV = g_WaterUV * kNormalMapScale;

				float low=-100;
				float high=300;

				// Move the water by our global speed
				phase += (g_WaterFlow*0.1f);
				
				gl.glBegin(GL.GL_QUADS);

//				 The back left vertice for the water
				gl.glMultiTexCoord2f(GL.GL_TEXTURE0, 0.0f, g_WaterUV);				// Reflection texture				
				gl. glMultiTexCoord2f(GL.GL_TEXTURE1, 0.0f, refrUV - phase);			// Refraction texture
				gl. glMultiTexCoord2f(GL.GL_TEXTURE2, 0.0f, normalUV + move2);		// Normal map texture
				gl. glMultiTexCoord2f(GL.GL_TEXTURE3, 0, 0);						// DUDV map texture
				gl. glVertex3f(low, sea_level, low);

//				 The front left vertice for the water
				gl. glMultiTexCoord2f(GL.GL_TEXTURE0, 0.0f, 0.0f);					// Reflection texture
				gl. glMultiTexCoord2f(GL.GL_TEXTURE1, 0.0f, 0.0f - phase);			// Refraction texture
				gl. glMultiTexCoord2f(GL.GL_TEXTURE2, 0.0f, 0.0f + move2);			// Normal map texture
				gl. glMultiTexCoord2f(GL.GL_TEXTURE3, 0, 0);						// DUDV map texture
				gl. glVertex3f(low, sea_level, high);

//				 The front right vertice for the water
				gl. glMultiTexCoord2f(GL.GL_TEXTURE0, g_WaterUV, 0.0f);				// Reflection texture
				gl. glMultiTexCoord2f(GL.GL_TEXTURE1, refrUV, 0.0f - phase);			// Refraction texture
				gl. glMultiTexCoord2f(GL.GL_TEXTURE2, normalUV, 0.0f + move2);		// Normal map texture
				gl. glMultiTexCoord2f(GL.GL_TEXTURE3, 0, 0);						// DUDV map texture
				gl. glVertex3f(high, sea_level, high);

//				 The back right vertice for the water
				gl. glMultiTexCoord2f(GL.GL_TEXTURE0, g_WaterUV, g_WaterUV);		// Reflection texture
				gl. glMultiTexCoord2f(GL.GL_TEXTURE1, refrUV, refrUV - phase);		// Refraction texture
				gl. glMultiTexCoord2f(GL.GL_TEXTURE2, normalUV, normalUV + move2);	// Normal map texture
				gl. glMultiTexCoord2f(GL.GL_TEXTURE3, 0, 0);						// DUDV map texture
				gl. glVertex3f(high, sea_level, low);

				gl.glEnd();
				
				shader.unbind(gl);
				
				gl.glActiveTexture(GL.GL_TEXTURE0);
		        //gl.glDisable(GL.GL_TEXTURE_2D);
		        gl.glActiveTexture(GL.GL_TEXTURE1);
		        gl.glDisable(GL.GL_TEXTURE_2D);
		        gl.glActiveTexture(GL.GL_TEXTURE2);
		        gl.glDisable(GL.GL_TEXTURE_2D);
		        gl.glActiveTexture(GL.GL_TEXTURE3);
		        gl.glDisable(GL.GL_TEXTURE_2D);
			}else{

				gl.glColor4f(1,1,1,1);
				//gl.glEnable(GL.GL_BLEND);
				gl.glBindTexture(GL.GL_TEXTURE_2D, tex.internal_index[0]);

				gl.glCallList(list_water);
			}
			gl.glPopMatrix();
			gl.glPopAttrib();
//			GraphicsEngine.triangles_drawn += nb_div*nb_div*2;
		}

		
		private void initShaders(GL gl){
			shader.create(gl);
			shader.compile(gl);
			shader.bind(gl);
			_lightPos = gl.glGetUniformLocationARB(shader.getHandle(), "lightPos");
			_cameraPos = gl.glGetUniformLocationARB(shader.getHandle(), "cameraPos");
			_waterColor = gl.glGetUniformLocationARB(shader.getHandle(), "waterColor");
			shader.unbind(gl);
		}
		
		DoubleBuffer clipPlaneBuffer;
		public DoubleBuffer getClipPlane(){
			return this.clipPlaneBuffer;
		}
		
		
		
		protected void tick(){
			/*
			count = (count+1) % 60;
			if(count == 0){
				phase += 0.005f;
				must_recompile =true;
			}
			*/
		}
		
		private void drawVertex_Water(GL gl, int i, int j){
			gl.glTexCoord2f((float)i/heightmap.length,(float)j/heightmap[0].length);
			//gl.glNormal3f(inter_norm[i][j].x,inter_norm[i][j].y,inter_norm[i][j].z);
			gl.glVertex3f(i * MAP_STEP_WORLD
					, sea_level,
					j * MAP_STEP_WORLD
					);
		}
		
		private void compile_water(GL gl){
			if(!gl.glIsList(list_water)){
				list_water = gl.glGenLists(1);
			}

			gl.glNewList(list_water, GL.GL_COMPILE);
			
			gl.glBegin(GL.GL_TRIANGLES);
			for (int i=0;i<heightmap.length;i++) {
				for (int j=0;j<heightmap[0].length;j++) {
					if(areaBelowSeaLevel(heightmap,i,j,1)){
						// triangle 1
						drawVertex_Water(gl, i,j);
						drawVertex_Water(gl, i+1,j+1);
						drawVertex_Water(gl, i+1,j);
						
						
						// triangle 2
						drawVertex_Water(gl, i,j);
						drawVertex_Water(gl, i,j+1);
						drawVertex_Water(gl, i+1,j+1);
						
					}
				}
			}
			gl.glEnd();
			
			/*
			gl.glTexCoord2d(Math.sin(phase)+0,Math.cos(phase)+0);
			gl.glVertex3f(0 +decalage,sea_level,0 +decalage);
			gl.glTexCoord2d(Math.sin(phase)+0,Math.cos(phase)+texrepet);
			gl.glVertex3f(0 +decalage,sea_level,MAP_SIZE +decalage);
			gl.glTexCoord2d(Math.sin(phase)+texrepet,Math.cos(phase)+texrepet);
			gl.glVertex3f(MAP_SIZE +decalage,sea_level,MAP_SIZE +decalage);
			gl.glTexCoord2d(Math.sin(phase)+texrepet,Math.cos(phase)+0);
			gl.glVertex3f(MAP_SIZE +decalage,sea_level,0 +decalage);
			*/
			
			gl.glEndList();
		}
		
		
		/**
		 * 
		 * @param inter
		 * @param i
		 * @param j
		 * @param d heightmap step
		 * @return
		 */
		private boolean areaBelowSeaLevel(float[][] inter, int i, int j, int d){
			if(inter[i][j]<sea_level){
				return true;
			//}else if(i-d>=0 && j-d>=0 && inter[i-d][j-d]<image_sea_threshold){
			//	return true;
			//}else if(i>0 && inter[i-d][j]<image_sea_threshold){
			//	return true;
			//}else if(j-d>=0 && inter[i][j-d]<image_sea_threshold){
			//	return true;
			}else if((i+d)<inter.length && inter[i+d][j]<sea_level){
				return true;
			}else if((j+d)<inter[0].length && inter[i][j+d]<sea_level){
				return true;
			}else if((i+d)<inter.length && (j+d)<inter[0].length && inter[i+d][j+d]<sea_level){
				return true;
			//}else if(i-d>=0 && (j+d)<inter[0].length && inter[i-d][j+d]<image_sea_threshold){
			//	return true;
			//}else if(j-d>0 && (i+d)<inter.length && inter[i+d][j-d]<image_sea_threshold){
			//	return true;
			}else
				return false;
		}
		
		public String toString(){
			return "Water";
		}

		@Override
		protected void drawByTriangles(GLAutoDrawable gld) {
			// TODO Auto-generated method stub
			
		}
		@Override
		protected void drawGeometryOnly(GLAutoDrawable gld) {
			// TODO Auto-generated method stub	
		}
	}
	
	
}
