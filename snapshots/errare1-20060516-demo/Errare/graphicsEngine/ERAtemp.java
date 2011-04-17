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

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.StringTokenizer;
import net.java.games.jogl.GL;
import net.java.games.jogl.GLDrawable;



public class ERAtemp extends GraphicalRep{
	
	protected int nb_Points;
	protected int nb_Faces;
	protected int nb_Normals;
	protected int nb_TexCoords;
	final static int MAX_POINTS3D=100000;
	final static int MAX_FACES3D=100000;
	final static int MAX_NORMALS=100000;
	final static int MAX_TEXCOORDS=100000;
	protected Point3D[] TPoints = new Point3D[MAX_POINTS3D];
	protected Face3D[] TFaces = new Face3D[MAX_FACES3D];
	protected Point3D[] TNormals = new Point3D[MAX_NORMALS];
	protected Point3D[] TTexCoords = new Point3D[MAX_TEXCOORDS];
	
	
	
	
	
	public ERAtemp(String filename) {
		super(filename, GraphicalRep.FORMAT_ERRARE);
		System.err.println("this is obsolete!");
		System.exit(0);
		loadFileERRA();
	}





	private boolean loadFileERRA(){
		int index;
		int cas;
		String ligne="";
		String token;
		StringTokenizer temp=new StringTokenizer("");
		BufferedReader br;
		try{
			br=new BufferedReader(new FileReader(mesh_filename));
		} catch (Exception e){
			return false;
		}
		
		while(ligne!=null){
			cas=0;
			try{
				ligne=br.readLine();
			} catch (Exception e){
				return false;
			}
			if (ligne==null) break;
			temp=new StringTokenizer(ligne);
			token=temp.nextToken();
			
			if (token.compareTo("Nombre_de_points:")==0){
				token=temp.nextToken();
				this.nb_Points=(new Integer(token)).intValue();
				//this.TPoints = new Point3D[nb_Points];
				//this.TPoints = new Point3D[100000];
				System.out.println("parse"+nb_Points);
			}
			else if (token.compareTo("Nombre_de_faces:")==0){
				token=temp.nextToken();
				this.nb_Faces=(new Integer(token)).intValue();
				//this.TFaces = new Face3D[nb_Faces];
				//this.TFaces = new Face3D[100000];
				System.out.println("parse"+nb_Faces);
			}
			else if (token.compareTo("Nombre_de_normales:")==0){
				token=temp.nextToken();
				this.nb_Normals=(new Integer(token)).intValue();
				//this.TNormals = new Point3D[nb_Normals];
				//this.TNormals = new Point3D[100000];
				System.out.println("parse"+nb_Normals);
			}
			else if (token.compareTo("Nombre_de_coordtex:")==0){
				token=temp.nextToken();
				this.nb_TexCoords=(new Integer(token)).intValue();
				//this.TTexCoords = new Point3D[nb_TexCoords];
				//this.TTexCoords = new Point3D[100000];
				System.out.println("parse"+nb_TexCoords);
			}		
			// else if (token.compareTo("Nombre_de_facetex:")==0){
			//	token=temp.nextToken();
			//}
			else if (token.compareTo("Points:")==0)
				cas=1;
			else if (token.compareTo("Faces:")==0)
				cas=2;
			else if (token.compareTo("Normal:")==0)
				cas=3;
			else if (token.compareTo("Textures:")==0)
				cas=4;
			else if (token.compareTo("FacesTextures:")==0)
				cas=5;
			
			switch(cas){
			case 0:
				break;
			case 1:
				token=temp.nextToken();
				index=(new Integer(token)).intValue();
				token=temp.nextToken();
				TPoints[index]= new Point3D();
				TPoints[index].X= new Float(token).floatValue();
				token=temp.nextToken();
				TPoints[index].Y= new Float(token).floatValue();
				token=temp.nextToken();
				TPoints[index].Z= new Float(token).floatValue();
				break;
			case 2:
				token=temp.nextToken();
				index=(new Integer(token)).intValue();
				TFaces[index]=new Face3D();
				token=temp.nextToken();
				TFaces[index].TabIndexPoint[0]=(new Integer(token).intValue());
				token=temp.nextToken();
				TFaces[index].TabIndexPoint[1]=(new Integer(token).intValue());
				token=temp.nextToken();
				TFaces[index].TabIndexPoint[2]=(new Integer(token).intValue());
				break;
			case 3:
				token=temp.nextToken();
				index=(new Integer(token)).intValue();
				TNormals[index]=new Point3D();
				token=temp.nextToken();
				TNormals[index].X= new Float(token).floatValue();
				token=temp.nextToken();
				TNormals[index].Y= new Float(token).floatValue();
				token=temp.nextToken();
				TNormals[index].Z= new Float(token).floatValue();
				break;
			case 4:
				token=temp.nextToken();
				index=(new Integer(token)).intValue();
				TTexCoords[index]=new Point3D();
				token=temp.nextToken();
				TTexCoords[index].X= new Float(token).floatValue();
				token=temp.nextToken();
				TTexCoords[index].Y= new Float(token).floatValue();
				token=temp.nextToken();
				TTexCoords[index].Z= new Float(token).floatValue();
				break;
				//case 5:
				//	token=temp.nextToken();
				//	index=(new Integer(token)).intValue();
				//	token=temp.nextToken();
				//	tab_faces[index].tab_index_pts[0]=(new Integer(token).intValue());
				//	token=temp.nextToken();
				//	tab_faces[index].tab_index_pts[1]=(new Integer(token).intValue());
				//	token=temp.nextToken();
				//	tab_faces[index].tab_index_pts[2]=(new Integer(token).intValue());
				//	nbFaces++;
				//	break;
			}
		}
		try{
			br.close();
		} catch ( Exception e){
			return false;
		}
		return true;
	}
	
	
	
	
	
	
	public void draw(GLDrawable gld,float px, float py, float pz, float rx, float ry, float rz){
		GL gl = gld.getGL();
		gl.glPushMatrix();
		doPosition(gl,px,py,pz,rx,ry,rz);
		if(prem){
			prem=false;
			doExpandTexture(gl);
		}
		
		
		
		gl.glBegin(GL.GL_TRIANGLES);
		int j, whichVertex;
		for(j=0; j<nb_Faces; j++) {
			for(whichVertex=0; whichVertex<3; whichVertex++) {
				int Index = TFaces[j].TabIndexPoint[whichVertex];
				gl.glNormal3f(TNormals[Index].X, TNormals[Index].Y, TNormals[Index].Z);
				gl.glTexCoord3f(TTexCoords[Index].X, TTexCoords[Index].Y, TTexCoords[Index].Z);
				gl.glVertex3f(TPoints[Index].X, TPoints[Index].Y, TPoints[Index].Z);
			}
		}
		gl.glEnd();
		gl.glPopMatrix();
	}
	
}
