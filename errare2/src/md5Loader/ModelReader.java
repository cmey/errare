package md5Loader;

import graphicsEngine.GraphicalRep;
import graphicsEngine.Mesh;
import graphicsEngine.StaticMesh;
import graphicsEngine.Texture;
import graphicsEngine.TextureFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;

import javax.vecmath.Point3f;
import javax.vecmath.Quat4f;
import javax.vecmath.Tuple3f;

import loader3DS.Node;

import logger.Logger;
import static md5Loader.MD5_Constants.*;

public class ModelReader {

	int md5version;

	ArrayList<Point3f> vertices;

	ArrayList<Integer> geometry;

	private Scanner scanner;

	private String file;

	private BufferedReader in;

	private String line;

	private String commandLine;

	private int numJoints;

	private int numMeshes;

	private String name;

	private String shader;

	private int numVerts;

	private int numTris;

	private int numWeights;

	private int parent;

	private ArrayList<Joint> jointList;

	private Joint root;

	private ArrayList<Weight> weightList;

	private ArrayList<Vert> vertList;

	private ArrayList<Point3f> allVertices;

	private ArrayList<Integer> triangleList;

	private GraphicalRep loaded;

	public ModelReader(String file) {
		vertices = new ArrayList<Point3f>();
		geometry = new ArrayList<Integer>();
		this.file = file;
		jointList = new ArrayList<Joint>();
		weightList = new ArrayList<Weight>();
		vertList = new ArrayList<Vert>();
		allVertices = new ArrayList<Point3f>();
		triangleList = new ArrayList<Integer>();
		line = "";
	}

	public void readModel() throws MalformedURLException, IOException {

		URL url = getClass().getClassLoader().getResource(file);
		in = new BufferedReader(new InputStreamReader(url.openStream()));
		line = in.readLine();

		while (line != null) {
			if (!line.equals("")) // on saute les lignes vides
				processLine(line);
			line = in.readLine();
		}
		processMeshCreation();

	}

	private void processLine(String line) throws IOException {
		scanner = new Scanner(line);
		String tag = scanner.next();

		if (tag.equals(MD5_VERSION_TAG)) {
			md5version = scanner.nextInt();
			return;
		}
		if (tag.equals(MD5_COMMAND_LINE_TAG)) {
			commandLine = scanner.next();
			return;
		}

		if (tag.equals(MD5_JOINTS_COUNT)) {
			numJoints = scanner.nextInt();
			return;
		}

		if (tag.equals(MD5_MESHES_COUNT)) {
			numMeshes = scanner.nextInt();
			return;
		}

		if (tag.equals(MD5_JOINT_TAG)) {
			processJoins();
			return;
		}

		if (tag.equals(MD5_MESH_TAG)) {
			processMesh();
			return;
		}

		processUnknownTag(tag);

	}

	private void processJoins() throws IOException {
		String name;
		float z;
		float y;
		float x;
		float orientX, orientY, orientZ;
		Joint j;

		for (int i = 0; i < numJoints; i++) {
			line = in.readLine();
			scanner = new Scanner(line);
			name = readString();
			parent = scanner.nextInt();
			scanner.next();
			x = Float.parseFloat(scanner.next());
			y = Float.parseFloat(scanner.next());
			z = Float.parseFloat(scanner.next());
			scanner.next();
			scanner.next();
			orientX = Float.parseFloat(scanner.next());
			orientY = Float.parseFloat(scanner.next());
			orientZ = Float.parseFloat(scanner.next());
			j = new Joint(name, parent, new Point3f(x, y, z), orientX, orientY,
					orientZ);
			jointList.add(j);
		}
		in.readLine();
	}

	private void buildHierarchy(){	

		int oldhierarchy = -1;
		int indexoldmodel = 0;

		for (int i = 0; i < jointList.size(); i++) {
			jointList.get(i).children.clear();
			jointList.get(i).setParent(null);
		}

		for (int i = 0; i < jointList.size(); i++) {
			Joint j = jointList.get(i);

			if (j.getParent() == -1) {
				oldhierarchy = -1; // on a le root
				indexoldmodel = i;
				j.setParent(null);
				root = j;
			} else {
				if (j.getParent() > oldhierarchy)// L'objet en cours est
				// le fils du pr�c�dent
				{
					// le parent du courant est le precedent
					j.setParent(jointList.get(indexoldmodel));

					jointList.get(indexoldmodel).addChild(j);

					oldhierarchy = j.getParent();
					indexoldmodel = i;
				}else// L'objet courant n'est pas le fils du pr�c�dent,
				// recherche de son anc�tre
				{
					Joint parent = jointList.get(indexoldmodel);

					while (parent.getParent() >= j.getParent())
						// recherche dans les ancetre d'une hierarchie
						// immediatement superieure de celle de l'objet en cours
						parent = parent.dad; // on remonte

					
					parent.addChild(j);
					j.dad = parent;
					oldhierarchy = j.getParent();

					indexoldmodel = i;
				}
			}
		}
	}

	private void processMesh() throws IOException {
		line = in.readLine();
		if(line.startsWith("//"))
		readName();
		if(line.contains(MD5_SHADER_TAG))
		readShader();
		
		readVertices();
		readTriangles();
		readWeight();
		in.readLine();
	}

	private void processUnknownTag(String tag) {
		Logger.printWARNING("Unknown tag : " + tag);
	}

	private void readName() throws IOException {
		//line = in.readLine();
		scanner = new Scanner(line);
		scanner.next();
		scanner.next();
		name = scanner.next();
	}

	private void readShader() throws IOException {
		//while((line = in.readLine()).isEmpty());
		scanner = new Scanner(line);
		scanner.next();
		shader = scanner.nextLine();
		//System.out.println("tag "+shader);
	}

	private void readVertices() throws IOException {
		while (!line.contains(MD5_VERTEX_COUNT_TAG)) {
			line = in.readLine();
		}
		scanner = new Scanner(line);
		scanner.next();
		numVerts = scanner.nextInt();
		float s, t;
		int startWeight, countWeight;
		Vert v;

		for (int i = 0; i < numVerts; i++) {
			line = in.readLine();
			scanner = new Scanner(line);
			scanner.next();
			scanner.next();
			scanner.next();
			s = Float.parseFloat(scanner.next());
			t = Float.parseFloat(scanner.next());
			scanner.next();
			startWeight = scanner.nextInt();
			countWeight = scanner.nextInt();
			// TODO ajouter un vert
			v = new Vert(s,t,startWeight,countWeight);
			vertList.add(v);
		}
	}

	private void computeVertices(){
		
		/* setup vertices */
		for (int i = 0; i < numVerts; ++i)
		  {
		    Point3f finalVertex = new Point3f(0,0,0);

		    /* calculate final vertex to draw with weights */
		    for (int j = 0; j < vertList.get(i).countWeight; j++)
		      {
		    	//++j
		    	//System.out.println(j);
		       Weight weight = weightList.get(vertList.get(i).startWeight+j);
		       Joint joint = jointList.get(weight.joint);
		       //System.out.println(joint);

		        /* calculate transformed vertex for this weight */
		       
		        Quat4f rotPoint = rotatePoint(joint.orient,weight.pos);
		        
		        /* the sum of all weight->bias should be 1.0 */
		        finalVertex.x += (joint.pos.x + rotPoint.x) * weight.bias;
		        finalVertex.y += (joint.pos.y + rotPoint.y) * weight.bias;
		        finalVertex.z += (joint.pos.z + rotPoint.z) * weight.bias;
		      }
		    allVertices.add(finalVertex);
		  }
		
	}
	
	
	private Quat4f rotatePoint(Quat4f orient, Point3f pos) {	
		
		//R = Q.P.Q*
		Quat4f point = new Quat4f(pos.x,pos.y,pos.z,0);
		Quat4f conj = new Quat4f(orient.x,orient.y,orient.z,orient.w);
		Quat4f q = new Quat4f(orient.x,orient.y,orient.z,orient.w);
		conj.conjugate();
		q.mul(point);
		q.mul(conj);
		return q;
	}
	
	
	private void readTriangles() throws IOException {
		while (!line.contains(MD5_TRI_COUNT)) {
			line = in.readLine();
		}
		scanner = new Scanner(line);
		scanner.next();
		numTris = scanner.nextInt();
		int triIndex, index0, index1, index2;

		for (int i = 0; i < numTris; i++) {
			line = in.readLine();
			scanner = new Scanner(line);
			scanner.next();
			triIndex = scanner.nextInt();
			index0 = scanner.nextInt();
			index1 = scanner.nextInt();
			index2 = scanner.nextInt();
			triangleList.add(index0);
			triangleList.add(index1);
			triangleList.add(index2);
			//System.out.println("Triangle "+triIndex+" index 0 : "+index0*3+" index 1 : "+index1*3+" index 2 : "+index2*3);
		}
	}

	private void readWeight() throws IOException {
		while (!line.contains(MD5_WEIGHT_COUNT_TAG)) {
			line = in.readLine();
		}
		int weightIndex, join;
		float bias, x, y, z;
		Weight w;
		
		scanner = new Scanner(line);
		scanner.next();
		numWeights = scanner.nextInt();

		for (int i = 0; i < numWeights; i++) {
			line = in.readLine();
			scanner = new Scanner(line);
			scanner.next();
			weightIndex = scanner.nextInt();
			join = scanner.nextInt();
			bias = Float.parseFloat(scanner.next());
			scanner.next();
			x = Float.parseFloat(scanner.next());
			y = Float.parseFloat(scanner.next());
			z = Float.parseFloat(scanner.next());
			w = new Weight(weightIndex,join,bias,x,y,z);
			weightList.add(w);
			//System.out.println(w);
		}
	}
	


	private void processMeshCreation() throws IOException {
		computeVertices();
		buildHierarchy();
		//System.out.println(allVertices);
		float[] verts = new float[numVerts*3];
		for(int i=0;i<allVertices.size();i++){
			verts[i*3] = allVertices.get(i).x;
			verts[i*3+1] = allVertices.get(i).y;
			verts[i*3+2] = allVertices.get(i).z;
			//System.out.println(verts[i*3]+" "+verts[i*3+1]+" "+verts[i*3+2]);
		}
		
		int[] topology = new int[numTris*3];
		//System.out.println(numTris*3);
		for(int i=0;i<topology.length;i++){
			topology[i] = triangleList.get(i);
		}
		
		
		float [][] tex = new float[1][numVerts*2];
		for(int i=0;i<numVerts;i++){
			tex[0][i*2] = vertList.get(i).s;
			tex[0][i*2+1] = vertList.get(i).t;
		}
		float[] normals = new float[numVerts];
		StaticMesh mod = new StaticMesh(topology,verts,normals,tex);
		String textfile = file.substring(0,file.indexOf("."))+".jpg";
		
		if(new File(textfile).exists()){
		Texture[] tab = {TextureFactory.loadTexture(textfile)};
		loaded = new GraphicalRep(mod, tab);
		}else
			loaded = new GraphicalRep(mod, null);
	}

	public GraphicalRep getGraphicalRep() {
		// TODO Auto-generated method stub
		return loaded;
	}

	private String readString(){
		int start, end;
		start = -1;
		end = -1;
		String res = null;
		char c;
		for(int i = 0;i<line.length();i++){
			c = line.charAt(i);
			if(c=='"'){
				if(start == -1) start = i;
				else{
				end = i;
				break;
				}
			}
		}
			
		if(end!=-1){
			res = line.substring(start+1,end);
			line = line.substring(end+1);
			scanner = new Scanner(line);
		}
	
		return res;
		
	}
}
