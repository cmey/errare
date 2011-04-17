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
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.imageio.ImageIO;

import net.java.games.jogl.GL;
import net.java.games.jogl.GLDrawable;
import net.java.games.jogl.GLU;
import net.java.games.jogl.util.BufferUtils;

public class static3DS extends GraphicalRep {

	
	protected int list;	//3DS only: C'est dans list que l'on charge le fichier 3DS
	
	
	
	public static3DS(String filename) {
		super(filename, GraphicalRep.FORMAT_3DS);
		if(DEBUG)System.out.println("Objet "+ID+" - "+"("+mesh_filename+") (type "+format+") "+"(taille "+verticeXmaximum+" "+verticeYmaximum+" "+verticeZmaximum+")");
	}

	
	
	
	private void loadFile3DS(String filePath, GLDrawable glDrawable) {
		GL gl = glDrawable.getGL();
		Loader3DS loader=new Loader3DS();
		loader.loadData(filePath);
		if(!gl.glIsTexture(list))
			list = gl.glGenLists(1);
		gl.glNewList(list, GL.GL_COMPILE);
		
		charge3dsDansListe(glDrawable, loader);
		
		gl.glEndList();  
	}
	
	
	
	
	/*Cette methode permet de charger le fichier 3ds du loader
	 * et le met dans la liste list
	 */
	public void charge3dsDansListe(GLDrawable gLDrawable, Loader3DS loader){
		
		final GL gl = gLDrawable.getGL();
		
		
		
		for (int i = 0; i < loader.objectCount; i++){//Pour chaque maillage
			Mesh mesh = ((Mesh)loader.getMeshArray().get(i));
			
			for (int j = 0; j < mesh.materialAppliArray.size(); j++){//Pour chaque materiel (texture) du maillage
				MaterialAppli materialAppli = ((MaterialAppli) mesh.materialAppliArray.get(j));
				String nomMaterial = materialAppli.name;
				if (nomMaterial != null){
					//On recupere la texture associ� au materiel
					Material materiel = ((Material)loader.getMaterialArray().get(nomMaterial));
					String laTexture = materiel.texture;
					
					/*Naturellement si il n'y a pas de texture
					 *il ne faut donc charger aucune image
					 */
					if (laTexture != null){
						/*On a des probleme pour ouvrir certains fichier d'image alors on ne prend que des fichiers .png */
						laTexture = laTexture.substring(0, laTexture.length() - 3);//On retire l'extention du fichier
						laTexture = laTexture + "png";//On ajoute l'extention ".png" aux nom du fichier
						BufferedImage img =null;
						
						//Maintenant on charge la texture
						chargerTexture(gLDrawable, img, laTexture);
					}
					
					/*Il y a des textures dans les materiaux mais aussi des couleurs
					 * on les charge donc ici*/
					if (materiel.color){
						/*Les couleur peuvent �tre coder sous forme de bytes ou de float*/
						if (materiel.enByte){
							gl.glColor3ubv(materiel.RGBb);
						}
						else
							gl.glColor3fv(materiel.RGB);
					}
					
					
				}
				/*Les facettes sont sous forme de triangle
				 * On les trace donc comme tel*/
				gl.glBegin(GL.GL_TRIANGLES);
				
				/*Pour chaque facette sur laquelle s'applique le materiel
				 * on indique les 3 points pour faire le triangle */
				for(int z = 0; z < materialAppli.lesFaces.length ; z++){
					int [] lesFaces = materialAppli.lesFaces;
					int [] font = mesh.faceArray[lesFaces[z]].array;
					
					/*Le tableau coordonnee contient les coordonn�es d'un point
					 * ici celles du premier sommet du triangle */
					float[] coordonnee = mesh.vertexArray[font[0]].array;
					
					//On place la texture en utilisant les coordonn�es u et v des 3 sommet de chaque facettes (des triangles) 
					//Premier sommet
					/*Il est possible que la texture est �t� appliqu�e sans utiliser les coordonn�e u et v
					 * pour �viter les erreurs du � l'absence de ses coordonn�es on fait un try catch */
					try{
						gl.glTexCoord2f(mesh.mapCoordArray[font[0]].u, mesh.mapCoordArray[font[0]].v);
					}
					catch (ArrayIndexOutOfBoundsException e){}
					gl.glVertex3fv(coordonnee);
					
					//Second sommet
					coordonnee = mesh.vertexArray[font[1]].array;
					try{
						gl.glTexCoord2f(mesh.mapCoordArray[font[1]].u, mesh.mapCoordArray[font[1]].v);
					}
					catch (ArrayIndexOutOfBoundsException e){}
					gl.glVertex3fv(coordonnee);
					
					//Troisieme sommet
					coordonnee = mesh.vertexArray[font[2]].array;
					try{
						gl.glTexCoord2f(mesh.mapCoordArray[font[2]].u, mesh.mapCoordArray[font[2]].v);
					}
					catch (ArrayIndexOutOfBoundsException e){}
					gl.glVertex3fv(coordonnee);
					
				}
				gl.glEnd();
				/*On d�sactive la texture pour que si l'objet suivant n'a pas de texture
				 il ne prend pas la texture de l'objet pr�cedent */
				gl.glDisable(GL.GL_TEXTURE_2D); 
			}
		}
		
	}
	
	
	
	/*Si la texture existe alors elle sera chargee comme texture courante
	 * et on active l'utilisation de texture */
	public void chargerTexture(GLDrawable gLDrawable,BufferedImage img, String laTexture){
		final GL gl = gLDrawable.getGL();
		int texture;
		/*Si il y a un probleme dans l'existence d'un fichier de texture
		 Alors "existe" passera � false */
		boolean existe = true;
		
		
		try{
			img = readPNGImage(laTexture);
		}
		catch(Exception e){
			System.out.println("Le fichier " + laTexture + " n'a pas �t� trouv�.");
			existe = false;
		}
		
		
		/*Si il y a une texture et que le fichier de celle-ci existe
		 * alors on charge cette texture en tant que texture courante*/
		if (existe){	 
			texture = genTexture(gl);
			gl.glBindTexture(GL.GL_TEXTURE_2D, texture);
			//On charge l'image
			makeRGBTexture(gl, gLDrawable.getGLU(), img, GL.GL_TEXTURE_2D, false);
			gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
			gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
			gl.glEnable(GL.GL_TEXTURE_2D);//On active l'utilisation de texture
		}
		
	}
	
	
	
	
	private int genTexture(GL gl) {
		final int[] tmp = new int[1];
		gl.glGenTextures(1, tmp);
		return tmp[0];
	}
	
	
	
	
	/* Fonctions utilisees pour le chargement d'image */
	private BufferedImage readPNGImage(String resourceName) throws IOException
	{
		URL url = getResource(resourceName);
		if (url == null)
		{
			throw new RuntimeException("Error reading resource " + resourceName);
		}
		BufferedImage img = ImageIO.read(url);
		java.awt.geom.AffineTransform tx = java.awt.geom.AffineTransform.getScaleInstance(1, -1); 
		tx.translate(0, -img.getHeight(null)); 
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR); 
		img = op.filter(img, null); 
		return img;
	}
	
	private void makeRGBTexture(GL gl, GLU glu, BufferedImage img, int target, boolean mipmapped)
	{
		ByteBuffer dest = null;
		switch (img.getType())
		{
		case BufferedImage.TYPE_3BYTE_BGR:
		case BufferedImage.TYPE_CUSTOM:
		{
			
			byte[] data = ((DataBufferByte) img.getRaster().getDataBuffer()).getData();
			dest = ByteBuffer.allocateDirect(data.length);
			dest.order(ByteOrder.nativeOrder());
			dest.put(data, 0, data.length);
			break;
		}
		case BufferedImage.TYPE_INT_RGB:
		{
			
			int[] data = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
			dest = ByteBuffer.allocateDirect(data.length * BufferUtils.SIZEOF_INT);
			dest.order(ByteOrder.nativeOrder());
			dest.asIntBuffer().put(data, 0, data.length);
			break;
		}
		default:
			
			throw new RuntimeException("Unsupported image type " + img.getType());
		}
		
		if (mipmapped)
		{
			glu.gluBuild2DMipmaps(target, GL.GL_RGB8, img.getWidth(), img.getHeight(), GL.GL_RGB, GL.GL_UNSIGNED_BYTE, dest);
		}
		else
		{
			gl.glTexImage2D(target, 0, GL.GL_RGB, img.getWidth(), img.getHeight(), 0, GL.GL_RGB, GL.GL_UNSIGNED_BYTE, dest);
		}
	}
	
	public static final URL getResource (final String filename)
	{
		// Try to load resource from jar
		URL url = ClassLoader.getSystemResource(filename);
		// If not found in jar, then load from disk
		if (url == null)
		{
			try
			{
				url = new URL("file", "localhost", filename);
			}
			catch (Exception urlException){
			} // ignore
		}
		return url;
	}
	
	
	
	
	
	/**
	 * Draws the object (of type 3DS) to the screen given it's position and orientation.
	 * @param glDrawable gl context (given by the Graphicsengine's display method)
	 * @param px position on x axis
	 * @param py position on y axis
	 * @param pz position on z axis
	 * @param rx rotation over x axis
	 * @param ry rotation over y axis
	 * @param rz rotation over z axis
	 */
	public void draw(GLDrawable glDrawable, int px, int py, int pz, int rx, int ry, int rz)
	{
		GL gl = glDrawable.getGL();
		if(prem) {
			prem=false;
			loadFile3DS(mesh_filename, glDrawable);
			//	gl.glGenTextures(1, textures);
			//	gl.glBindTexture(GL.GL_TEXTURE_2D, textures[0]);
			//	gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, 3, tWidth, tHeight, 0, GL.GL_RGB, GL.GL_UNSIGNED_BYTE, texture1);
			//	gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
			//	gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
		}
		
		gl.glPushMatrix();
		/*On doit faire une rotation de 90 degres pour avoir
		 * l'objet 3ds dans le bon sens
		 * En effet dans JOGL c'est l'axe des Y qui donne la hauteur
		 * mais pour un fichier 3ds c'est l'axe des Z
		 * Pour avoir le meme repere il suffit de faire une rotation 
		 * de 90 degres par rapport a l'axe des X */
		gl.glRotatef(90.0f,1.0f,0.0f,0.0f);
		gl.glCallList(list);//On call l'objet 3DS prealablement compile
		gl.glPopMatrix();
	}

	@Override
	public void draw(GLDrawable gld, float px, float py, float pz, float rx, float ry, float rz) {
		// TODO Auto-generated method stub
		
	}
}
