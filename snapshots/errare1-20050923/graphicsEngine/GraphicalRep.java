package graphicsEngine;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;

import net.java.games.jogl.GL;
import net.java.games.jogl.GLU;

/**
 * @author Cyberchrist
 */
public class GraphicalRep {
	private String rep3D_filename;
	private short format; /* 1=.md2  2=.errare */
	
	//	Texture
	private int tex;
	
	private int nb_Points;
	private int nb_Faces;
	private int nb_Normals;
	private int nb_TexCoords;
	final static int MAX_POINTS3D=100000;
	final static int MAX_FACES3D=100000;
	final static int MAX_NORMALS=100000;
	final static int MAX_TEXCOORDS=100000;
	private Point3D[] TPoints = new Point3D[MAX_POINTS3D];
	private Face3D[] TFaces = new Face3D[MAX_FACES3D];
	private Point3D[] TNormals = new Point3D[MAX_NORMALS];
	private Point3D[] TTexCoords = new Point3D[MAX_TEXCOORDS];
	// Animation frame information
	private class AnimFrame{
		public String name;
		public float[] vertices;
		public int [] normal;
	}
	// Index lists for vertices
	private int[] indices;
	private int[] uvIndices;
	// Model base info    
	private int[] normalsind;
	private float[] uvs;	
	private AnimFrame interpolatedFrame;
	// Frame array
	private AnimFrame[] frames;
	// Start and end frames
	private int startFrame = 0;
	private int endFrame = 0;
	// Current and next animation frame
	private int curAnimFrame = 0;
	private int nextAnimFrame = 1;
	// Animation timings
	private long lastAnimTime = 0;
	// Animation FPS for number of interpolations
	private int fps = 30;
	private boolean animate = false;        
	private int tWidth;
	private int tHeight;
	private int[] textures;
	private BufferedImage image;
	private ByteBuffer texture1;
	private boolean prem = true;
	private List texturelist1 = new ArrayList();
	private List texturelist2 = new ArrayList();
	private Point3D[] MD2_Normals = new Point3D[162];
	
	public GraphicalRep(String filename) {
		this.rep3D_filename=filename;
		String extension = filename.substring(filename.length()-3,filename.length());
		if(extension.compareTo("md2")==0) {
			format = 1;
			loadFileMD2(filename);
		}
		if(extension.compareTo("err")==0) {
			format = 2; //errare self format
			loadFile();
		}
		
		String textureFilename = filename.substring(0, filename.length()-3);
		textureFilename= textureFilename.concat("jpg");
		
		//texture1 = loadTexture(texture1,"models/blood.jpg");
		
		texture1 = loadTexture(texture1,textureFilename);
		textures = new int[1];
		
		setFrameRange(0, 150);
		setFPS(30);
		toggleAnim(true);
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
	
	public short getFormat(){
		return format;
	}
	
	
	public void drawMD2(GL gl, GLU glu, int px, int py, int pz, int rx, int ry, int rz)
	{
		gl.glTranslatef(px, py, pz);
		gl.glRotatef(rx, 1, 0, 0);
		gl.glRotatef(ry, 0, 1, 0);
		gl.glRotatef(rz, 0, 0, 1);
		
		if(prem) { 
			//image = TextureTool.loadTexture(gl, glu, "models/blood.png",  false);
			prem=false;
			gl.glGenTextures(1, textures);
			gl.glBindTexture(GL.GL_TEXTURE_2D, textures[0]);
			gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, 3, tWidth, tHeight, 0, GL.GL_RGB, GL.GL_UNSIGNED_BYTE, texture1);
			gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
			gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
		}
		
		AnimFrame frame = frames[startFrame];
		
		createInterpolatedFrame();
		frame = interpolatedFrame;
		
		gl.glBindTexture(GL.GL_TEXTURE_2D, textures[0]);
		
		// Building the mesh
		gl.glBegin(GL.GL_TRIANGLES);
		for (int i = 0; i < indices.length; i++)
		{
			if(i%3==0) gl.glNormal3f(MD2_Normals[frame.normal[indices[i]]].X, MD2_Normals[frame.normal[indices[i]]].Y, MD2_Normals[frame.normal[indices[i]]].Z);
			gl.glTexCoord2f(uvs[(uvIndices[i]*2)], uvs[(uvIndices[i]*2)+1]);
			gl.glVertex3f(frame.vertices[(indices[i]*3)], frame.vertices[(indices[i]*3)+1], frame.vertices[(indices[i]*3)+2]);
		}
		gl.glEnd();
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
					curFrame.normal[v] = normalindex;
				}                                                                
			}                                   
		}
		catch (Exception e)
		{
			return false;
		}
		// Success
		return true;
	}
	
	/**
	 * Set start and end frames.
	 * @param startFrame First animation frame
	 * @param endFrame Last animation frame
	 */
	public void setFrameRange(int startFrame, int endFrame)
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
		nextAnimFrame = startFrame+1;
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
		this.animate = enable;
	}
	
	private int getTex(){
		return tex;
	}
	/**
	 * Reset animation.
	 */
	private void resetAnim()
	{
		// Setup current and next anim frames
		curAnimFrame = startFrame;
		nextAnimFrame = startFrame+1;
		lastAnimTime = 0;
	}
	
	/** 
	 * Create interpolated frame.
	 */
	private void createInterpolatedFrame()
	{
		long curTime = System.currentTimeMillis();
		long elapsedTime = curTime - lastAnimTime;
		
		// Get interpolation time
		float t = elapsedTime / (1000.0f / fps);
		
		// Get next frame
		nextAnimFrame = (curAnimFrame + 1) % endFrame;
		
		if (nextAnimFrame > endFrame)
		{
			System.out.println("auto");
			nextAnimFrame = startFrame;
		}	
		
		// Calculate current and next frames
		if(elapsedTime > (1000.0f / fps))
		{
			curAnimFrame = nextAnimFrame;                                   
			lastAnimTime = curTime;
		}
		
		// Interpolate frames and store in interpolatedFrame
		for (int v = 0; v < frames[curAnimFrame].vertices.length; v++)
		{
			interpolatedFrame.vertices[v] = (frames[curAnimFrame].vertices[v] + t * (frames[nextAnimFrame].vertices[v] - frames[curAnimFrame].vertices[v]));
		}	
	}
	
	
	private boolean loadFile(){
		int index;
		int cas;
		String ligne="";
		String token;
		StringTokenizer temp=new StringTokenizer("");
		BufferedReader br;
		try{
			br=new BufferedReader(new FileReader(rep3D_filename));
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
			/* else if (token.compareTo("Nombre_de_facetex:")==0){
				token=temp.nextToken();
			} */
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
			case 5:/*
				token=temp.nextToken();
				index=(new Integer(token)).intValue();
				token=temp.nextToken();
				tab_faces[index].tab_index_pts[0]=(new Integer(token).intValue());
				token=temp.nextToken();
				tab_faces[index].tab_index_pts[1]=(new Integer(token).intValue());
				token=temp.nextToken();
				tab_faces[index].tab_index_pts[2]=(new Integer(token).intValue());
				nbFaces++;*/
				break;
			}
		}
		try{
			br.close();
		} catch ( Exception e){
			return false;
		}
		return true;
	}
	
	public void drawERA(GL gl, GLU glu, float px, float py, float pz, float rx, float ry, float rz){
		gl.glPushMatrix();
		gl.glTranslatef(px, py, pz);
		gl.glRotatef(rx, 1, 0, 0);
		gl.glRotatef(ry, 0, 1, 0);
		gl.glRotatef(rz, 0, 0, 1);
		textures = new int[1];
		gl.glGenTextures(1, textures);
		//truc à faire ici avec texture: charger texture
		gl.glBindTexture(GL.GL_TEXTURE_2D, textures[0]);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, 3, tWidth, tHeight, 0, GL.GL_RGB, GL.GL_UNSIGNED_BYTE, texture1);
		
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
	
	/**
	 * Loads a texture file into memory (RAM)
	 * @param texture the global variable ByteBuffer I/O'ed (usable by openGL)
	 * @param fn filename of the texture. > ITS WIDTH AND HEIGHT MUST BE 2^x PIXELS ! (openGL usable format) <
	 * @return
	 */
	private ByteBuffer loadTexture(ByteBuffer texture, String fn) {
		if(texturelist1.contains(fn)){
			int where = texturelist1.indexOf(fn);
			return (ByteBuffer) texturelist2.get(where);
		}else{
			texturelist1.add(fn);
		try {
			BufferedImage buff = ImageIO.read(new File(fn));
			tHeight = buff.getHeight();
			tWidth = buff.getWidth();
			Raster raster = buff.getRaster();
			int[] img = null;
			img = raster.getPixels(0, 0, tWidth, tHeight, img);
			
			texture = ByteBuffer.allocateDirect(tWidth * tHeight * 3);
			for (int y = 0; y < tHeight; y++)
				for (int x = 0; x < tWidth; x++) {
					texture.put((byte) img[(y * tWidth + x) * 3]);
					texture.put((byte) img[(y * tWidth + x) * 3 + 1]);
					texture.put((byte) img[(y * tWidth + x) * 3 + 2]);
				}

		} catch (IOException e) {
			e.printStackTrace();  //To change body of catch statement use Options | File Templates.
		}
		texturelist2.add(texture);
		return texture;
		}
	}
}
