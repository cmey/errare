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

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLDrawable;

public class Heightmap extends GraphicalRep{
	
	public int nb_texrepet;
	public boolean disable_edge_flag = false;
	public int MAP_SIZE = 1000; //unit�s OpenGL
	public float alpha=0.6f;
	private float CoefElev = 100; //unit�s OpenGL
	private int decalage;
	private int list_terrain;
	public int nb_div;
	protected float[][] inter; //contient les hauteurs
	private Point3D[][] inter_norm; //contient la normale en chaque point
	protected float step_world;
	protected int step;
	private boolean must_recompile;
	public Water water;
	
	
	
	public Heightmap(String filename, int map_size) {
		super(filename, GraphicalRep.FORMAT_TERRAIN);
		MAP_SIZE = map_size;
		decalage=0;
		unpack_texture();
		load_texture("data/images/floor/grass1.png"); //2eme texture (�crasant la 1ere)
		nb_div=16;
		
		water = new Water("data/images/floor/water1.png",map_size,this);
	}
	
	
	private void unpack_texture(){
		inter = new float[tWidth][tHeight];
		texture1.rewind();
		for (int i = 0; i < tWidth; i++) {
			for (int j = 0; j < tHeight; j++) {
				inter[i][j] = CoefElev * ((float)texture1.get() / 128); //byte
			}
		}
		
		prem=true;
	}
	
	
	private void compute_normals(){
		inter_norm = new Point3D[nb_div][nb_div];
		for(int i=0;i<nb_div;i++)
			for(int j=0;j<nb_div;j++){
				inter_norm[i][j] = new Point3D();
			}
		
		Point3D P1,P2,P3,P4,V1,V2,V3;
		P1=new Point3D();
		P2=new Point3D();
		P3=new Point3D();
		P4=new Point3D();
		V1=new Point3D();
		V2=new Point3D();
		V3=new Point3D();
		float incx,incy,incz,norme;
		
		for(int i=0;i<nb_div-1;i++)
			for(int j=0;j<nb_div-1;j++){
				getVector(i,j,P1);
				getVector(i+1,j,P2);
				getVector(i+1,j+1,P3);
				getVector(i,j+1,P4);
				
				// v1.x = longueur selon x d'un cot� du carr�
				V1.X=P2.X - P1.X; V1.Y=P2.Y - P1.Y; V1.Z=P2.Z - P1.Z;
				V2.X=P3.X - P1.X; V2.Y=P3.Y - P1.Y; V2.Z=P3.Z - P1.Z;
				V3.X=P4.X - P1.X; V3.Y=P4.Y - P1.Y; V3.Z=P4.Z - P1.Z;
				
				// v2 ^ v1 (produit vectoriel)
                // incx = composante en x d'un vecteur orthogonal � v1 et v2
				incx=V2.Y*V1.Z-V1.Y*V2.Z;
				incy=V2.Z*V1.X-V1.Z*V2.X;
				incz=V2.X*V1.Y-V1.X*V2.Y;
				
				norme=(float)Math.sqrt(incx*incx+incy*incy+incz*incz);
				incx/=norme; incy/=norme; incz/=norme;
				
				// normales en P1 P2 P3
				inter_norm[i][j].X-=incx; inter_norm[i][j].Y-=incy; inter_norm[i][j].Z-=incx;
				inter_norm[i+1][j].X-=incx; inter_norm[i+1][j].Y-=incy; inter_norm[i+1][j].Z-=incx;
				inter_norm[i+1][j+1].X-=incx; inter_norm[i+1][j+1].Y-=incy; inter_norm[i+1][j+1].Z-=incx;

				// v3 ^ v2
                // inc = vecteur orthogonal � v2 et v3
				incx=V3.Y*V2.Z-V2.Y*V3.Z;
				incy=V3.Z*V2.X-V2.Z*V3.X;
				incz=V3.X*V2.Y-V2.X*V3.Y;
				
				// ajout des normales en P1 P3 P4
				inter_norm[i][j].X-=incx; inter_norm[i][j].Y-=incy; inter_norm[i][j].Z-=incx;
				inter_norm[i+1][j+1].X-=incx; inter_norm[i+1][j+1].Y-=incy; inter_norm[i+1][j+1].Z-=incx;
				inter_norm[i][j+1].X-=incx; inter_norm[i][j+1].Y-=incy; inter_norm[i][j+1].Z-=incx;
				
				inter_norm[i][j].normalize();
			}
	}
	
	
	private void getVector(int i, int j, Point3D P){
		P.X = i * step_world + decalage;
		P.Y = inter[i*step][j*step];
		P.Z = j * step_world + decalage;
	}
	
	
	private void compile_terrain(GL2 gl){
		assert(0 != MAP_SIZE);
		nb_texrepet = MAP_SIZE / 512; assert(0 != nb_texrepet);
		step_world = MAP_SIZE / nb_div; assert(0 != step_world);
		assert(0 != inter.length);
		step = inter.length / nb_div; assert(0 != step);
		
		compute_normals();
		
		if(!gl.glIsTexture(list_terrain))
			list_terrain = gl.glGenLists(1);
		gl.glNewList(list_terrain, GL2.GL_COMPILE);
		for (int i=0;i<inter.length-step;i+=step) {
			gl.glBegin(GL.GL_TRIANGLE_STRIP);
			for (int j=0;j<inter[0].length-step;j+=step) {
				
				drawVertex(gl, i+step,j);
				drawVertex(gl, i,j);
				
			}
			gl.glEnd();
		}
		
		gl.glEndList();
	}
	
	
	private void drawVertex(GL2 gl, int i, int j){
		float alt = inter[i][j] / CoefElev + 0.7f;
		gl.glColor4f(alt,alt,alt,alpha);
		gl.glTexCoord2f((float)i/(inter.length-step)*nb_texrepet,(float)j/(inter[0].length-step)*nb_texrepet);
		gl.glNormal3f(inter_norm[i/step][j/step].X,inter_norm[i/step][j/step].Y,inter_norm[i/step][j/step].Z);
		gl.glVertex3f(i/step * step_world
				+ decalage
				, inter[i][j],
				j/step * step_world
				+ decalage);
		//System.out.println(" "+ (-MAP_SIZE/2 + i/step * step_world) +" "+ (inter[i][j]) +" "+ (-MAP_SIZE/2 + j/step * step_world));
	}
	
	
	/* note: la texture est forc�ment de cot� "puissance de 2"
	 * donc nous n'aurons pas de probl�me d'indice dans le tableau
	 */
	public void inc_resolution_terrain(){
		if(nb_div < tHeight){
			nb_div *= 2;
			must_recompile=true;
		}
	}
	public void dec_resolution_terrain(){
		if(nb_div > 1){
			nb_div /= 2;
			must_recompile=true;
		}
	}
	public void setHeightMap_fromPhysical(float[][] map, int world_step){
		assert(0 != map.length);
		assert(0 != world_step);
		
		if(map.length>128){
			Error.println("Your heightmap is too big and is NOT taken into account! Replaced by standard heightmap.");
			Error.println("Supported: max 128x128  yours: "+map.length+"x"+map.length);
		}else{
			MAP_SIZE=world_step*map.length;
			//decalage = map_size/4;
			
			inter = map;
			nb_div = map.length; assert(0 != nb_div);
			
			must_recompile=true;
			//compute_normals();
			water.setWater_fromPhysical(this,MAP_SIZE,decalage);
			GraphicsEngine.skybox.setMapSize(MAP_SIZE);
			GraphicsEngine.sun.setMapSize(MAP_SIZE);
		}
	}


	public void draw_terrain(GL2 gl){
		gl.glPushAttrib(GL2.GL_ALL_ATTRIB_BITS);
		//gl.glDisable(GL.GL_CULL_FACE);
		
		
		gl.glBindTexture(GL.GL_TEXTURE_2D, internal_texture1[0]);	
		
		
		gl.glPushMatrix();
		if(prem){
			prem=false;
			doExpandTexture(gl);
			compile_terrain(gl);
		}
		if(must_recompile){
			must_recompile=false;
			compile_terrain(gl);
		}
		
		/*************************************/
		if(KeyboardHelper.terrain_on)
		gl.glCallList(list_terrain); // Affichage du terrain
		/*************************************/
		gl.glColor4f(1,1,1,1);
		gl.glPopMatrix();
		
		gl.glPopAttrib();
		if(KeyboardHelper.terrain_on)
		GraphicsEngine.voxel_drawn += nb_div*nb_div*2;
		
	}
	
	
	@Override
	protected void draw(GLAutoDrawable gld, float px, float py, float pz, float rx, float ry, float rz) {
		GL2 gl = gld.getGL().getGL2();
		draw_terrain(gl);
	}

}
