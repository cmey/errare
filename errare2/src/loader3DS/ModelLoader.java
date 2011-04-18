package loader3DS;

import java.awt.Color;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.DataInputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.vecmath.AxisAngle4f;
import javax.vecmath.Matrix4f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import geom.Matrix;
import geom.Triangle;
import geom.Vector;
import graphicsEngine.AnimatedMesh;
import graphicsEngine.Mesh;
import graphicsEngine.MeshData;
import graphicsEngine.GraphicalRep;
import graphicsEngine.StaticMesh;
import graphicsEngine.Texture;
import graphicsEngine.TextureFactory;
import java.io.DataInputStream;
import logger.Logger;
import main.ResourceLocator;

public class ModelLoader {

	private Mesh finalMesh;

	private static final int MAX_SURFACES = 33;

	private ArrayList<TempMesh> meshList = new ArrayList<TempMesh>();

	private String path;

	private boolean surfacesCreated = false;

	private String instanceName;

	private String matName;

	private float shininess;

	private final int verbosity = 2;

	private int noofVertices;

	private float[] vertices;

	private float[][] texcoords;

	private java.util.Vector[] sharedFaces;

	private Surface[] surfaces = null;

	private int noofFaces;

	private int[] topology;

	private Face[] faces;

	private Triangle[] geometry;

	private int topoIndex = 0;

	private int topoNbr = 0;

	private ArrayList<Float> verticesList = new ArrayList<Float>();

	private ArrayList<Integer> topoList = new ArrayList<Integer>();

	private boolean pointExists;

	private String imageName;

	private ArrayList<Texture> textureList = new ArrayList<Texture>();

	private ArrayList<ArrayList<Float>> texc = new ArrayList<ArrayList<Float>>();

	private GraphicalRep finalModel;

	private Texture[] textureTab;

	private Hashtable<String, TempMesh> meshes = new Hashtable<String, TempMesh>();

	private Node root;

	private boolean animated;

	private String objectName;

	private String nodeName;

	private float[] temptc;

	private ArrayList<Vector> normalsList = new ArrayList<Vector>();

	private Vector3f translation = null;

	private Matrix4f orientation = null;

	private Vector3f scale = null;

	private Node currentNode;

	private ArrayList<Node> nodeList;

	private int nbFrames;

	private MeshData[] mdTab;

	// private Hashtable<String, Transform> instanceTable;

	public ModelLoader(String path) {
		this.path = path;
	}

	public void loadObject() throws IOException {
		Logger.printINFO("Load3DS: using path = " + path);
               
		//DataInputStream in = new DataInputStream(RessourceLocator.getFile(path), "r");
               // File f = new File("df");
               DataInputStream in = new DataInputStream(ResourceLocator.getRessourceAsStream(path));
		nodeList = new ArrayList<Node>();
		try {
			for (;;) {
				processChunk(in);
			}

		} catch (EOFException e) {
			processMeshConcat();
		}
	}

	private void prepareForNewObject() {
		/*
		 * object = null; shape = null; geometry = null; noofVertices = 0;
		 * vertices = null; sharedFaces = null; noofFaces = 0; faces = null;
		 * surfaces = null;
		 */
		surfacesCreated = false;
		/*
		 * textureCoords = null; mat = null; matName = null; material = null;
		 * shininess = 0.0f; // component = null; nodeName = null; instanceName =
		 * null; translation = null; orientation = null; scale = null;
		 */
		nodeName = null;
		instanceName = null;
		translation = null;
		orientation = null;
		scale = null;
	}

	void processChunk(DataInputStream in) throws IOException {
		int tag = readUnsignedShort(in);
		int length = readInt(in);

		try {
			Class temp = getClass();
			for (Field f : temp.getFields()) {
				if (verbosity > 1 && f.getInt(this) == tag){
                                    
                                }
                        }
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		switch (tag) {
		case S3D_M3DMAGIC: // 3DS file
		case S3D_MMAGIC: // Editor chunks
		case S3D_MAT_ENTRY: // 3DS material
		case S3D_KEYFRAME_NODE: // Object instance

			break;
		case KEYFRAME_SEGMENT:
			int startFrame = readInt(in);
			nbFrames = readInt(in);
			break;
		case KEYFRAME_CURRENT_TIME:
			int time = readInt(in);
			break;
		case S3D_N_TRI_OBJECT: // Object definition
			processChunk(in);
			break;
		case S3D_KEYFRAME: // Start of the instance tree
			if (verbosity > 0){
				//Logger.printINFO("Starting key frame");
                        }
                        if (surfacesCreated == false) {
				createUnsmoothedFaces();
				prepareForNewObject();
			}
			processChunk(in);
			break;

		case S3D_KEYFRAME_LINK: // Details of an object instance
			processLink(in);
			break;

		case S3D_NODE_ID: // Node ID - unused.
			processNodeID(in);
			break;

		case S3D_POS_TRACK_TAG: // Contains object's initial position
			processPosTrackTag(in);
			break;

		case S3D_ROT_TRACK_TAG: // Contains object's initial orientation
			processRotTrackTag(in);
			break;

		case S3D_SCL_TRACK_TAG: // Contains object's initial scale
			processSclTrackTag(in);
			break;

		case S3D_INSTANCE_NAME: // Name given to object instance
			instanceName = readName(in);
			// System.out.println("Instance name is : " + instanceName);
			break;

		case S3D_AMBIENT_LIGHT: // Ambient light
			processAmbientLight(in);
			break;

		case S3D_MASTER_SCALE: // Global scaling factor
			processMasterScale(in);
			break;

		case S3D_MAT_NAME: // Start of a 3DS material
			matName = readName(in);
			// System.out.println("Material name is : " + matName);
			// mat = new Appearance();
			// material = new Material();
			// material.setLightingEnable(true);
			// mat.setMaterial(material);
			// mats.put(matName, mat);

			break;

		case S3D_MAT_AMBIENT: // Ambient colour of material
			Color ambient = readColor(in);
			break;

		case S3D_MAT_DIFFUSE: // Diffuse colour of material
			Color diffuse = readColor(in);
			break;

		case S3D_MAT_SPECULAR: // Specular colour of material
			readColor(in);
			break;

		case S3D_MAT_SHININESS: // 3DS shininess percentage
			shininess = readPercentage(in);
			// System.out.println("== Shininess: " + shininess);
			break;

		case S3D_MAT_SHININESS_STRENGTH: // 3DS shininess strength
			float shininessStrength = readPercentage(in);
			break;

		case S3D_MAT_TRANSPARENCY: // Transparency percentage
			float transparency = readPercentage(in);
                        break;

		case S3D_MAT_SHADING: // Type of rendering
			int style;
			int mode = readUnsignedShort(in);
			break;

		case S3D_MAT_WIRE: // Another way of enforcing wireframe
			
			break;

		case S3D_MAT_WIRESIZE: // Wireframe line width
			float width = readFloat(in);
			
			break;

		case S3D_MAT_TWO_SIDE: // Face culling
			
			break;

		case S3D_MAT_TEXMAP: // Image for texture map
			// float matPercent = readPercentage(in) * 100;
			tag = readUnsignedShort(in);
			length = readInt(in);
			float percentage = 0;
			String image = null;
			switch (tag) {
			case S3D_INT_PERCENTAGE:
				percentage = (float) readUnsignedShort(in) / 100;
				break;
			case S3D_FLOAT_PERCENTAGE:
				percentage = readFloat(in);
				break;
			case S3D_MAT_MAPNAME:
				image = readName(in);
				break;

			default:
				throw new IOException(
						"INT_PERCENTAGE/FLOAT_PERCENTAGE expected: " + tag);
			}
			if (image == null)
				image = readMatName(in);
                        int indexLastSlash = path.lastIndexOf("/");
                        String pathparent = path.substring(0, indexLastSlash);
			imageName = pathparent + (pathparent.endsWith("/")?"":"/") + image;
                        //pathfile = new File(imageName);
                        try{
                            Texture textureMap = TextureFactory.loadTexture(imageName);
                            textureList.add(textureMap);
                        }catch(IOException ioe){
                            Logger.printWARNING("** Texturing has been disabled, error caused by : "+ ioe);
                        }
			break;

		case S3D_TEX_VERTS: // 2D Texture coordinates
			processTextureCoordinates(in);
			break;
		case S3D_MAT_MAPNAME:
			image = readName(in);
			//Logger.printINFO("##########################" + image);
			break;

		case S3D_NAMED_OBJECT: // Start of 3DS object
			if (surfacesCreated == false) {
				// createUnsmoothedFaces();
				prepareForNewObject();
			}

			objectName = readName(in);

			if (hiddenObject(objectName)) {
				skipChunk(in, length - objectName.length() - 1);
				//Logger.printINFO("(Skipping hidden object '" + objectName+ "')");
				break;
			}

			if (verbosity > 0){
				//Logger.printINFO("Processing object '" + objectName + "'");
                        }
			// object = new SharedGroup();
			// shape = new Shape3D();
			processChunk(in);
                        // objectTable.put(objectName, object); // TODO
			break;

		case S3D_POINT_ARRAY: // Vertex list
			processPointArray(in);
			break;

		case S3D_FACE_ARRAY: // Face list
			processFaceArray(in);
			break;

		case S3D_MSH_MAT_GROUP: // Materials used by object
			processMaterial(in);
			break;

		case S3D_SMOOTH_GROUP: // List of surfaces
			processSmoothGroup(in);
			prepareForNewObject();
			break;

		case S3D_MESH_MATRIX: // Object transform
			processMeshMatrix(in);
			break;

		case S3D_POINT_FLAG_ARRAY: // Not much use to us
		case S3D_PIVOT:
			processPivotPoint(in);
			break;

		default:
			// if (verbosity > 0) {
			// System.out.println("UNKNOWN_TAG: 0x" + Integer.toHexString(tag)
			// + " LEN: " + length);
			// }
			skipChunk(in, length);
			break;
		}
	}

	private void processPivotPoint(DataInputStream in) throws IOException {
		currentNode.pivotX = readFloat(in);
		currentNode.pivotY = readFloat(in);
		currentNode.pivotZ = readFloat(in);
	}

	//
	// Take the last position specified in this keyframe list
	// as the initial position of the object.
	//
	private void processPosTrackTag(DataInputStream in) throws IOException {
		int dummy, keys, i;

		// Creation d'un MeshData
		int vertexPos = 0;

		dummy = readUnsignedShort(in);
		dummy = readUnsignedShort(in);
		dummy = readUnsignedShort(in);
		dummy = readUnsignedShort(in);
		dummy = readUnsignedShort(in);
		keys = readUnsignedShort(in);
		dummy = readUnsignedShort(in);

		if (keys > 1) {
			animated = true;
		}
		
		int time;
		for (i = 0; i < keys; i++) {
			time = readUnsignedShort(in);
			dummy = readInt(in);

			//
			// Reverse the Y and Z coordinates, negate Z coordinates
			//

			float x = readFloat(in), y = readFloat(in), z = readFloat(in);

			// translation = new Vector(x, z, -y); TODO degager
			// translation = new Vector3f( x, y, z ); comment�e d'origine
			// TODO attention au reverse
			/*
			 * md.vertices[vertexPos] = x; md.vertices[vertexPos++] = y;
			 * md.vertices[vertexPos++] = z;
			 */
			vertexPos++;

			currentNode.addTransform(time, new Translation(x, y, z));
			if (verbosity > 1) {
				// System.out.println(" Position: " + translation);
			}
		}

	}

	//
	// Take the last orientation specified in this keyframe list
	// as the initial orientation of the object.
	//

	private void processRotTrackTag(DataInputStream in) throws IOException {
		int dummy, keys, i;

		dummy = readUnsignedShort(in);
		dummy = readUnsignedShort(in);
		dummy = readUnsignedShort(in);
		dummy = readUnsignedShort(in);
		dummy = readUnsignedShort(in);
		keys = readUnsignedShort(in);
		dummy = readUnsignedShort(in);
		
		int debut;
		for (i = 0; i < keys; i++) {
			debut = readUnsignedShort(in);
			dummy = readInt(in);
			float rot = readFloat(in);
			float x = readFloat(in);
			float y = readFloat(in);
			float z = readFloat(in);

			//
			// Convert the orientation between 3DS and
			// Java3D coordinate systems.
			//
			/*
			 * AxisAngle4f axes = new AxisAngle4f(x, y, z, -rot); Matrix4f m =
			 * new Matrix4f(); Matrix4f rm = new Matrix4f();
			 * 
			 * m.set(axes); System.err.println(rm); rm.rotX((float) -Math.PI /
			 * 2); System.err.println("ROT :\n"+rm); orientation = new
			 * Matrix4f(); orientation.mul(rm, m); System.out.println("MUL
			 * :\n"+orientation);
			 */
			if (verbosity > 0) {
				// System.out.println(" Rotation: " + orientation);
			}

			currentNode.addTransform(debut, new Rotation(rot, x, y, z));

		}
	}

	//
	// Processed out of curiosity.
	//

	private void processMasterScale(DataInputStream in) throws IOException {
		float scale = readFloat(in);
		if (verbosity > 0) {
			//System.out.println("== Master scale: " + scale);
		}
	}

	//
	// Read in the definition of the ambient light.
	//

	private void processAmbientLight(DataInputStream in) throws IOException {
		Color ambient = readColor(in);

		//System.out.println("Ambient Light: " + ambient);
	}

	//
	// Take the last scale specified in this keyframe list
	// as the initial scale of the object. Also take this
	// as the queue to finish instancing the object.
	//

	private void processSclTrackTag(DataInputStream in) throws IOException {
		int dummy, keys, i;
		ArrayList<Scalling> templst = new ArrayList<Scalling>();
		dummy = readUnsignedShort(in);
		dummy = readUnsignedShort(in);
		dummy = readUnsignedShort(in);
		dummy = readUnsignedShort(in);
		dummy = readUnsignedShort(in);
		keys = readUnsignedShort(in);
		dummy = readUnsignedShort(in);
		int debut;
		for (i = 0; i < keys; i++) {
			debut = readUnsignedShort(in);
			dummy = readInt(in);

			//
			// Reverse the Y and Z coordinates
			//

			float x = readFloat(in), y = readFloat(in), z = readFloat(in);

			// scale = new Vector(x, z, y); TODO a degager
			currentNode.addTransform(debut, new Scalling(x, y, z, orientation,
					translation));
			templst.add(new Scalling(x, y, z, orientation, translation));
		}

		if (verbosity > 0) {
			//System.out.println("Scale : " + keys + " keys");
		}

		nodeList.add(currentNode);

		if (hiddenObject(nodeName)) {
			return;
		}
		// TODO ajouter les scales dans current
		/*
		 * if (hiddenObject(nodeName)) { return; } Matrix4f m = new Matrix4f();
		 * m.set(orientation); m.setTranslation(translation);
		 * 
		 * Transform3D transform = new Transform3D(m); TransformGroup instance =
		 * new TransformGroup(transform); SharedGroup shared = null;
		 * 
		 * if (instanceName == null) // Use the node name. {
		 * System.out.println("Instancing '" + nodeName + "'"); //
		 * instanceTable.put( nodeName, instance ); } else {
		 * System.out.println("Instancing '" + instanceName + "' (->'" +
		 * nodeName + "')"); // instanceTable.put( instanceName, instance ); }
		 */

		// shared = findObject( nodeName );
		/*
		 * if ( shared == null ) { throw new IOException( "Can't locate
		 * referenced object." ); }
		 * 
		 * Link link = new Link( shared );
		 * 
		 * instance.addChild( link ); root.addChild( instance ); instanceName =
		 * null;
		 */
		if (instanceName == null) {
			// instanceTable.put( nodeName, null);
		} else {

		}
	}

	private void processLink(DataInputStream in) throws IOException {
		nodeName = readName(in);
		//System.out.println("New link for keyframe: " + nodeName);
		int flags1 = readUnsignedShort(in);
		int flags2 = readUnsignedShort(in);
		int posInTree = readUnsignedShort(in);
		//System.out.println("Position : " + posInTree);
		currentNode.hierarchy = posInTree;

		currentNode.objectName = nodeName;
		if (verbosity > 0) {
			/*System.out.println("== Link for object '" + nodeName + "': 0x"
					+ Integer.toHexString(flags1) + ", 0x"
					+ Integer.toHexString(flags2) + ", 0x");*/
		}
	}

	private void processPointArray(DataInputStream in) throws IOException {
		int i;
		float x, y, z;
		boolean pointExists = false;
		noofVertices = readUnsignedShort(in);
		// vertices = new Point3f[noofVertices];
		vertices = new float[noofVertices * 3];
		sharedFaces = new java.util.Vector[noofVertices];
		verticesList = new ArrayList<Float>();
		// System.out.println(" " + noofVertices + " vertices.");
		// totalVertices += noofVertices;
		for (i = 0; i < noofVertices; i++) {
			x = readFloat(in);
			y = readFloat(in);
			z = readFloat(in);
			verticesList.add(x);
			verticesList.add(y);
			verticesList.add(z);
			// System.out.println("x:"+x+" y:"+y+"z: "+z);
			sharedFaces[i] = new java.util.Vector(); // Filled in
			// processFaceArray();
		}

		/*
		 * for (int k = 0; k < verticesList.size(); k += 3) { for (int l = 0; l <
		 * verticesList.size(); l += 3) { if (k != l) { if (verticesList.get(k) ==
		 * verticesList.get(l) && verticesList.get(k + 1) == verticesList .get(l +
		 * 1) && verticesList.get(k + 2) == verticesList .get(l + 2)) {
		 * System.out.println("fuck"); // System.exit(0); } } } }
		 */
		for (int k = 0; k < noofVertices * 3; k++) {
			vertices[k] = verticesList.get(k);
			// System.out.println(vertices[k]);
		}

	}

	private void processFaceArray(DataInputStream in) throws IOException {
		int i;
		// int vertexFormat = GeometryArray.COORDINATES | GeometryArray.NORMALS;

		/*
		 * if (textureCoords != null) { if (verbosity > 1) {
		 * System.out.println(" Object is TEXTURED"); } vertexFormat |=
		 * GeometryArray.TEXTURE_COORDINATE_2; }
		 */
		noofFaces = readUnsignedShort(in);
		faces = new Face[noofFaces];
		// geometry = new TriangleArray(noofFaces * 3, vertexFormat);
		faces = new Face[noofFaces];
		geometry = new Triangle[noofFaces * 3];

		// System.out.println(" " + noofFaces + " faces.");
		// totalPolygons += noofFaces;
		topology = new int[noofFaces * 3];
		topoIndex = 0;
		for (i = 0; i < noofFaces; i++) {
			int a = readUnsignedShort(in);
			int b = readUnsignedShort(in);
			int c = readUnsignedShort(in);
			int flags = readUnsignedShort(in);
			topology[topoIndex] = a;
			// System.out.println(topology[topoIndex]);
			topology[topoIndex + 1] = b;
			// System.out.println(topology[topoIndex+1]);
			topology[topoIndex + 2] = c;
			// System.out.println(topology[topoIndex+2]);
			topoIndex += 3;
			faces[i] = new Face(a, b, c);
			// faces[i] = new Face(a*3, b*3, c*3);
			// faces[i].TabIndexPoint = new int[] { a * 3, b * 3, c * 3 };

		}

		if (verbosity > 1) {
			//System.out.println("== Adding geometry to shape");
		}
		TempMesh child = new TempMesh(vertices, topology, temptc);
		meshList.add(child);
		meshes.put(objectName, child);
		// shape.setGeometry(geometry);
		temptc = null;
	}

	//
	// Read in the 2D texture coordinates - note these are
	// only valid if planar mapping was used in 3DS.
	//

	void processTextureCoordinates(DataInputStream in) throws IOException {
		int vertexCount = readUnsignedShort(in);
		int i;

		if (vertexCount != noofVertices) {
			//System.out.println("** Number of texture vertices = #model vertices");
			return;
		}

		// if (verbosity > 0) {
		// System.out.println(" Texture coordinates: #" + vertexCount);
		// }
		temptc = new float[vertexCount * 2];
		ArrayList<Float> tcoords = new ArrayList<Float>();
		for (i = 0; i < vertexCount; i++) {
			// tcoords.add(readFloat(in));
			// tcoords.add(readFloat(in));
			temptc[i * 2] = readFloat(in);
			// System.out.println(temptc[i*2]);
			temptc[i * 2 + 1] = readFloat(in);
			// System.out.println( "== " + textureCoords[i] );
		}

		texc.add(tcoords);
		// System.out.println(tcoords);
		/*
		 * textureCoords = new Point2f[vertexCount];
		 * 
		 * for (i = 0; i < vertexCount; i++) { textureCoords[i] = new
		 * Point2f(readFloat(in), readFloat(in)); // System.out.println( "== " +
		 * textureCoords[i] ); }
		 */
		// readFloat(in); readFloat(in); //pour faire semblant
		// finalMesh.texcoords = texCoords;
	}

	private void processNodeID(DataInputStream in) throws IOException {
		int id = readUnsignedShort(in);

		currentNode = new Node(id);
		if (verbosity > 0) {
			//System.out.println("== NodeID: " + id);
		}
	}

	private void skipChunk(DataInputStream in, int length) throws IOException {
		int bytesToSkip = length - 6;

		if (bytesToSkip > 0) {
			in.skipBytes(bytesToSkip);
		}
	}

	Color readColor(DataInputStream in) throws IOException {
		int tag = readUnsignedShort(in);
		int length = readInt(in);

		switch (tag) {
		case S3D_COLOR_F:
			return new Color(readFloat(in), readFloat(in), readFloat(in));

		case S3D_COLOR_24:
			return new Color((float) in.readUnsignedByte() / 255, (float) in
					.readUnsignedByte() / 255,
					(float) in.readUnsignedByte() / 255);

		default:
			throw new IOException("COLOR_F/COLOR_24 expected");
		}
	}

	//
	// Read in a float or int percentage and return it
	// as a number between 0.0 and 1.0
	//

	float readPercentage(DataInputStream in) throws IOException {
		int tag = readUnsignedShort(in);
		int length = readInt(in);

		switch (tag) {
		case S3D_INT_PERCENTAGE:
			return (float) readUnsignedShort(in) / 100;

		case S3D_FLOAT_PERCENTAGE:
			return readFloat(in);

		case S3D_MAT_MAPNAME:
                    readName(in);
			return 0;
		default:
			throw new IOException("INT_PERCENTAGE/FLOAT_PERCENTAGE expected: "
					+ tag);
		}
	}

	//
	// Read in material name.
	//

	String readMatName(DataInputStream in) throws IOException {
		int tag = readUnsignedShort(in);
		int length = readInt(in);

		return readName(in);
	}

	//
	// Read in the string used to specify a name in
	// many different chunks.
	//

	String readName(DataInputStream in) throws IOException {
		StringBuffer buf = new StringBuffer();
		char c;

		while ((c = (char) in.readUnsignedByte()) != '\0') {
			buf.append(c);
		}

		return buf.toString();
	}

	//
	// Read in an unsigned short (16 bits).
	//

	int readUnsignedShort(DataInputStream in) throws IOException {
		int num = in.readUnsignedShort();
		num = ((num << 8) & 0xFF00) | ((num >> 8) & 0x00FF);
		if (num == 65535)
			num = -1;
		return num;
	}

	//
	// Read in a 32 bit integer (unsigned).
	//

	int readInt(DataInputStream in) throws IOException {
		int num = in.readInt();

		return ((num << 24) & 0xFF000000) | ((num << 8) & 0x00FF0000)
				| ((num >> 8) & 0x0000FF00) | ((num >> 24) & 0x000000FF);
	}

	//
	// Read in a 32 bit floating point number.
	//

	float readFloat(DataInputStream in) throws IOException {
		return Float.intBitsToFloat(readInt(in));
	}

	//
	// S3D_OBJ_HIDDEN doesn't seem to be set by 3DSMax, so
	// objects that have names beginning with '$' are taken
	// as hidden.
	//

	private boolean hiddenObject(String name) {
		return name.charAt(0) == '$';
	}

	//
	// Read in the transformation matrix and inverse transform
	// the vertex coordinates so they are back where they were
	// when 3DS initially created the object. They are
	// transformed back again with the information found in
	// the position, rotation and scale keyframe tracks.
	//

	private void processMeshMatrix(DataInputStream in) throws IOException {
		// Matrix4f m = new Matrix4f();
		Matrix mat = new Matrix(4, 4);
		int x, y;

		for (y = 0; y < 4; y++) {
			for (x = 0; x < 3; x++) {
				if (y == 3) {
					// m.setElement( x, y, readFloat( in ) );
					mat.setElement(x, y, readFloat(in));
				} else {
					// m.setElement( x, y, readFloat( in ) );
					mat.setElement(x, y, readFloat(in));
				}
			}
		}

		// m.setElement( 3, 3, 1.0f );
		mat.setElement(3, 3, 1.0f);

		// float transY = m.getElement( 1, 3 );
		// float transZ = m.getElement( 2, 3 );

		// float transY = mat.getElement( 1, 3 );
		// float transZ = mat.getElement( 2, 3 );

		/*
		 * //d origine // Reverse the Y and Z coordinates, negate Z coordinates // //
		 * m.setElement( 1, 3, transZ ); // m.setElement( 2, 3, -transY );
		 */

		/*
		 * Transform3D t = new Transform3D( m );
		 * 
		 * if ( verbosity > 1 ) { System.out.println( " Transform: " + t ); }
		 * 
		 * t.invert();
		 * 
		 * for ( x = 0; x < noofVertices; x++ ) { t.transform( vertices[x] ); }
		 */
	}

	//
	// Associate material with the object.
	//

	void processMaterial(DataInputStream in) throws IOException {
		String name = readName(in);
		// System.out.println("new material: " + name);
		/*
		 * Appearance m = (Appearance) mats.get(name);
		 * 
		 * if (m == null) { System.err.println("** Can't find referenced
		 * material"); return; }
		 * 
		 * System.out.println(" Attaching material '" + name + "'"); if
		 * (verbosity > 0) { System.out.println(" " + m.getMaterial()); }
		 * 
		 * shape.setAppearance(m);
		 */
		int faceCount = readUnsignedShort(in);
		int i;

		for (i = 0; i < faceCount; i++) {
			int dummy = readUnsignedShort(in);
		}
	}

	//
	// Contains a list of smoothed surfaces. Construct each surface
	// at a time, specifying vertex normals and texture coordinates
	// (if present).
	//

	void processSmoothGroup(DataInputStream in) throws IOException {
		double log2 = Math.log(2);
		int i;

		surfaces = new Surface[MAX_SURFACES];

		for (i = 0; i < MAX_SURFACES; i++) {
			surfaces[i] = new Surface();
		}

		for (i = 0; i < noofFaces; i++) {
			int group = readInt(in);
			long b = 0x1;
			int index;

			for (index = 0; index < 32; index++) {
				if ((group & b) > 0) {
					break;
				}
			}

			// System.out.println( "== group " + Long.toHexString( group ) + ",
			// index = " + index );

			faces[i].group = (int) group;
			surfaces[index].add(faces[i]);
		}

		int surface;

		i = 0;
		for (surface = 0; surface < MAX_SURFACES; surface++) {
			if (verbosity > 1 && surfaces[surface].noofFaces() > 0) {
				//System.out.println("    Constructing surface " + surface);
			}

			Enumeration iter = surfaces[surface].faces();

			while (iter.hasMoreElements()) {
				Face f = (Face) iter.nextElement();

				Vector normalA = calculateVertexNormal(f.a, f,
						surfaces[surface]);
				Vector normalB = calculateVertexNormal(f.b, f,
						surfaces[surface]);
				Vector normalC = calculateVertexNormal(f.c, f,
						surfaces[surface]);
				normalsList.add(normalA);
				normalsList.add(normalB);
				normalsList.add(normalC);
				// System.out.println( "Face [" + f.a + "] " + f.a() + ", [" +
				// f.b + "] " +
				// f.b() + ", [" + f.c + "] " + f.c() );
				// System.out.println( "Norm " + normalA + ", " + normalB + ", "
				// +
				// normalC );

				/*
				 * geometry.setCoordinate(i * 3, f.a());
				 * geometry.setCoordinate((i * 3) + 1, f.b());
				 * geometry.setCoordinate((i * 3) + 2, f.c());
				 */

				// geometry.setNormal(i * 3, normalA);
				// geometry.setNormal((i * 3) + 1, normalB);
				// geometry.setNormal((i * 3) + 2, normalC);
				/*
				 * if (textureCoords != null) { // System.out.println( "== Tex
				 * Coords: " + // textureCoords[f.a] + // ", " +
				 * textureCoords[f.b] + ", " + // textureCoords[f.c] );
				 * geometry.setTextureCoordinate(i * 3, textureCoords[f.a]);
				 * geometry.setTextureCoordinate((i * 3) + 1,
				 * textureCoords[f.b]); geometry.setTextureCoordinate((i * 3) +
				 * 2, textureCoords[f.c]); }
				 */

				i++;
			}
		}

		surfacesCreated = true;
	}

	//
	// Calculates a normalised vertex normal for <vertex> in
	// <thisFace> within <surface>.
	//

	Vector calculateVertexNormal(int vertex, Face thisFace, Surface surface) {
		Enumeration otherFaces = surface.faces();
		Vector normal = new Vector(thisFace.normal);
		int noofNormals = 1;

		while (otherFaces.hasMoreElements()) {
			Face otherFace = (Face) otherFaces.nextElement();

			if (sharesVertex(otherFace, vertex)) {
				normal.add(otherFace.normal);
				noofNormals++;
			}
		}

		if (noofNormals != 1) {
			normal.x /= noofNormals;
			normal.y /= noofNormals;
			normal.z /= noofNormals;
		}

		normal.normalize();

		return normal;
	}

	//
	// Checks if <vertex> is used by <face>.
	//

	boolean sharesVertex(Face face, int vertex) {
		return face.a == vertex || face.b == vertex || face.c == vertex;
	}

	//
	// Fill the TriangleArray with the raw polygon information,
	// don't calculate vertex normals but do give texture coordinates
	// if present.
	//

	void createUnsmoothedFaces() {
		int i;

		for (i = 0; i < noofFaces; i++) {
			Face f = faces[i];

			// System.out.println( "Face [" + f.a + "], [" + f.b + "], [" + f.c
			// + "]" );

			/*
			 * geometry.setCoordinate(i * 3, f.a()); geometry.setCoordinate((i *
			 * 3) + 1, f.b()); geometry.setCoordinate((i * 3) + 2, f.c());
			 * 
			 * geometry.setNormal(i * 3, f.normal); geometry.setNormal((i * 3) +
			 * 1, f.normal); geometry.setNormal((i * 3) + 2, f.normal);
			 */
			// geometry[i].points[0].x = f.TabIndexPoint[0];
			// geometry[i].points[1].x = f.TabIndexPoint[1];
			// geometry[i].points[2].x = f.TabIndexPoint[2];
			// TODO les normales
			if (texcoords != null) {
				// System.out.println( "== Tex Coords: " + textureCoords[f.a] +
				// ", " + textureCoords[f.b] + ", " +
				// textureCoords[f.c] );
				/*
				 * geometry.setTextureCoordinate(i * 3, textureCoords[f.a]);
				 * geometry.setTextureCoordinate((i * 3) + 1,
				 * textureCoords[f.b]); geometry.setTextureCoordinate((i * 3) +
				 * 2, textureCoords[f.c]);
				 */
			}
		}
	}

	public Mesh getMesh() {
		return this.finalMesh;
	}

	public GraphicalRep getModel3D() {
		return this.finalModel;
	}

	/*
	 * private void processMeshConcat() {
	 * 
	 * ArrayList<Float> tc = new ArrayList<Float>();
	 * 
	 * verticesList = new ArrayList<Float>(); ArrayList<Float> texcList;
	 * topoList = new ArrayList<Integer>(); float x, y, z; int pos = 0;
	 * TempMesh tm1; boolean textured = false; if (texc.size() > 0) textured =
	 * true;
	 * 
	 * for (int j = 0; j < meshList.size(); j++) { tm1 = meshList.get(j);
	 * float[] tex = tm1.getTexCoords(); if (tex != null) { // texcList =
	 * texc.get(j); texcList = new ArrayList<Float>();
	 * 
	 * for (int t = 0; t < tex.length; t++) texcList.add(tex[t]); //
	 * System.out.println(texcList); } else texcList = null; for (int i = 0; i <
	 * tm1.getTopology().length; i++) { // pour chaque point du mesh x =
	 * tm1.getVertices()[tm1.getTopology()[i]]; y =
	 * tm1.getVertices()[tm1.getTopology()[i] + 1]; z =
	 * tm1.getVertices()[tm1.getTopology()[i] + 2];
	 * 
	 * pointExists = false; for (int tempi = 0; tempi < i; tempi++) { float tx =
	 * verticesList.get(topoList.get(tempi)); float ty =
	 * verticesList.get(topoList.get(tempi) + 1); float tz =
	 * verticesList.get(topoList.get(tempi) + 2); if (tx == x && ty == y && tz ==
	 * z) { pos = topoList.get(tempi); // position du x du point // qui existe
	 * deja if (texcList != null) { if (i < texcList.size()) { tc.add(texcList
	 * .get(tm1.getTopology()[i] * 2)); tc.add(texcList
	 * .get(tm1.getTopology()[i] * 2 + 1)); } else { tc.add(tc.get(tempi * 2));
	 * tc.add(tc.get(tempi * 2 + 1)); } } pointExists = true; break; } }
	 * 
	 * //if (pointExists) { //topoList.add(pos); // } else { if (texcList !=
	 * null) { tc.add(texcList.get(tm1.getTopology()[i] * 2));
	 * tc.add(texcList.get(tm1.getTopology()[i] * 2 + 1)); }
	 * topoList.add(verticesList.size()); verticesList.add(x);
	 * verticesList.add(y); verticesList.add(z); //} } } vertices = new
	 * float[verticesList.size()]; topology = new int[topoList.size()]; int i =
	 * 0; for (Float f : verticesList) { vertices[i] = f; i++; } i = 0; for
	 * (Integer inte : topoList) { topology[i] = inte; i++; }
	 * 
	 * if (!texc.isEmpty()) { texcoords = new float[texc.size()][]; texcoords[0] =
	 * new float[tc.size()]; for (int k = 0; k < tc.size(); k++) {
	 * texcoords[0][k] = tc.get(k); }
	 * 
	 * textureTab = new Texture[textureList.size()]; for (int j = 0; j <
	 * textureList.size(); j++) textureTab[j] = textureList.get(j); } //
	 * calculating normals: // first we get face normals Vector[] faceNormals =
	 * new Vector[topology.length / 3]; for (int t = 0; t < topology.length; t +=
	 * 3) { // faceNormals[t/3]=new //
	 * Vector(vertices[topology[t]],vertices[topology[t]+1],vertices[topology[t]+2]);
	 * //faceNormals[t / 3] = calculateFaceNormal(t, t + 1, t + 2);
	 * //System.out.println("Face normal : " + faceNormals[t / 3]); }
	 * 
	 * Vector[] verticesNormals = new Vector[vertices.length / 3]; for (int vn =
	 * 0; vn < verticesNormals.length; vn++) verticesNormals[vn] = new Vector();
	 * 
	 * float[] normals = new float[verticesNormals.length * 3]; /*for (int top =
	 * 0; top < topology.length; top += 3) { //System.out.println(topology[top] /
	 * 3); verticesNormals[topology[top]].add(faceNormals[top]);
	 * verticesNormals[topology[top + 1]].add(faceNormals[top]);
	 * verticesNormals[topology[top + 2]].add(faceNormals[top]); }
	 * 
	 * for (int n = 0; n < verticesNormals.length; n++)
	 * verticesNormals[n].normalize();
	 * 
	 * for (int norm = 0; norm < verticesNormals.length; norm++) { normals[norm /
	 * 3] = verticesNormals[norm].x; normals[norm / 3 + 1] =
	 * verticesNormals[norm].y; normals[norm / 3 + 2] = verticesNormals[norm].z;
	 * 
	 * //System.out.println("Vertex normal : " + verticesNormals[norm]); }
	 */

	/*
	 * //float[] normals = new float[topology.length * 3];
	 * System.out.println("#####################"+normalsList.size()); float[]
	 * normals = new float[normalsList.size()]; for(int no=0;no<normalsList.size();no++){
	 * normals[no/3] = normalsList.get(no).x; normals[no/3+1] =
	 * normalsList.get(no).y; normals[no/3+2] = normalsList.get(no).z; } if
	 * (animated) { MeshData md = new MeshData(vertices, normals); MeshData[]
	 * mdTab = { md }; finalMesh = new AnimatedMesh(topology, mdTab, texcoords); }
	 * else { finalMesh = new StaticMesh(topology, new MeshData(vertices,
	 * normals), texcoords); } finalMesh.setFilename(this.path); finalModel =
	 * new GraphicalRep(finalMesh, textureTab); }
	 */

	private void processMeshConcat() {
		buildHierarchy();
		
		ArrayList<Float> allVertices = new ArrayList<Float>();
		ArrayList<Integer> topoList = new ArrayList<Integer>();
		ArrayList<Float> texcList;
		ArrayList<Float> tc = new ArrayList<Float>();
		int[] topo;
		int nov = 0;

		for (TempMesh tm : meshList) {
			float[] tex = tm.getTexCoords();
			if (tex != null) {
				texcList = new ArrayList<Float>();

				for (int t = 0; t < tex.length; t++)
					texcList.add(tex[t]);
			} else
				texcList = null;

			topo = tm.getTopology();

			if (allVertices.isEmpty())
				nov = 0;
			else
				nov = allVertices.size() / 3;

			for (int i = 0; i < tm.getVertices().length; i++) {
				allVertices.add(tm.getVertices()[i]);
			}

			for (int top = 0; top < topo.length; top++) {
				topoList.add(nov + topo[top]);
			}

			if (texcList != null)
				for (int texc = 0; texc < texcList.size(); texc++) {
					tc.add(texcList.get(texc));
				}

		}

		if (!texc.isEmpty()) {
			texcoords = new float[texc.size()][];
			texcoords[0] = new float[tc.size()];
			for (int k = 0; k < tc.size(); k++) {
				texcoords[0][k] = tc.get(k);
			}

			textureTab = new Texture[textureList.size()];
			for (int j = 0; j < textureList.size(); j++)
				textureTab[j] = textureList.get(j);
		}

		vertices = new float[allVertices.size()];
		for (int v = 0; v < allVertices.size(); v++)
			vertices[v] = allVertices.get(v);

		topology = new int[topoList.size()];
		for (int t = 0; t < topoList.size(); t++) {
			topology[t] = topoList.get(t);
		}

		// calculating normals:

		// first we get face normals
		Vector[] faceNormals = new Vector[topology.length / 3];
		for (int t = 0; t < topology.length; t += 3) {
			// faceNormals[t/3]=new
			// Vector(vertices[topology[t]],vertices[topology[t]+1],vertices[topology[t]+2]);
			// faceNormals[t/3] = calculateFaceNormal(t, t + 1, t + 2);
			faceNormals[t / 3] = new Vector();
			// System.out.println("Face normal : " + faceNormals[t / 3]);
		}

		Vector[] verticesNormals = new Vector[vertices.length / 3];
		for (int vn = 0; vn < verticesNormals.length; vn++)
			verticesNormals[vn] = new Vector();

		float[] normals = new float[verticesNormals.length * 3];
		processKeyframes();
		if (animated) {
			MeshData md = new MeshData(vertices, normals);
			// mdTab = { md };
			// md.name = nodeName;
			// currentAnimation.setGeometry(topology);
			finalMesh = new AnimatedMesh(topology, mdTab, texcoords);
			// System.out.println(currentAnimation);
		} else {
			finalMesh = new StaticMesh(topology,
					new MeshData(vertices, normals), texcoords);
		}
		finalMesh.setFilename(this.path);
		finalModel = new GraphicalRep(finalMesh, textureTab);
	}

	private void buildHierarchy() {
		int oldhierarchy = -1;
		int indexoldmodel = 0;

		for (int i = 0; i < nodeList.size(); i++) {
			nodeList.get(i).children.clear();
			nodeList.get(i).parent = null;
		}

		for (int i = 0; i < nodeList.size(); i++) {
			Node model1 = nodeList.get(i);

			if (model1.hierarchy == -1) {
				oldhierarchy = -1; // on a le root
				indexoldmodel = i;
				model1.parent = null;
				root = model1;
			} else {
				if (model1.hierarchy > oldhierarchy)// L'objet en cours est
				// le fils du precedent
				{
					// le parent du courant est le precedent
					model1.parent = nodeList.get(indexoldmodel);

					// on prend le parent
					Node parent = model1.parent;

					if (parent.objectName == model1.objectName) {
						Logger.printWARNING("Hierarchie incorrecte, il faut reconstruire la hierarchie dans 3DS");
						return;
					}

					model1.hasparent = true;
					parent.children.add(model1);
					
					while (parent != null) {
						// louche : ajouter le courant comme enfant de tous les
						// precedents
						// parent.children.add(model1);
						// bien pour propager toutes les transfos
						parent = parent.parent;
					}

					oldhierarchy = model1.hierarchy;
					indexoldmodel = i;
				}else// L'objet courant n'est pas le fils du precedent,
				// recherche de son ancetre
				{
					Node parent = nodeList.get(indexoldmodel);

					while (parent.hierarchy >= model1.hierarchy)
						// recherche dans les ancetre d'une hierarchie
						// immediatement superieure a celle de l'objet en cours
						parent = parent.parent; // on remonte

					
					parent.children.add(model1);
					model1.parent = parent;
					model1.hasparent = true;
					oldhierarchy = model1.hierarchy;

					
					// je najoute qu a un seul node
					/*while (parent != null)// Parcours des ancetres de
					// l'ancetre trouve, puis ajout des
					// objets fils
					{
						parent.children.add(model1);
						parent = parent.parent;
					}*/

					indexoldmodel = i;
				}
			}
		}
	}

	private void processKeyframes() {

		mdTab = new MeshData[nbFrames + 2];
		ArrayList<Translation> tr;
		ArrayList<Rotation> rot;
		ArrayList<Scalling> scl;
		TempMesh object;
		float[] vertices;
		float[] ov;
		Translation t;
		Rotation r;
		Scalling s;
		Transform trans;
		for (int i = 0; i <= nbFrames; i++) {
			for (Node n : nodeList) {
				// System.out.println(n.objectName+" "+n.isBone());
				object = meshes.get(n.objectName);
				if (object != null) {
					if (i == 0) {
						// prendre les vertices de base et concatener
						// processMeshConcat
						// object.addVertices(0,object.getVertices());
						object.lastVertices = object.getVertices();
					} else {
						// travailler sur i-1
						// ov = object.getVertices(i-1);
						ov = object.lastVertices;

						trans = n.getTransformAt(i);
						if (trans instanceof Translation) {
							t = (Translation) trans;

							for (int j = 0; j < ov.length; j += 3) {
								ov[j] += t.x;
								ov[j + 1] += t.y;
								ov[j + 2] += t.z;
							}
						} else if (trans instanceof Rotation) {
							r = (Rotation) trans;

							Matrix4f mrot = new Matrix4f();
							mrot.set(new AxisAngle4f(r.x, r.y, r.z, r.rot));
							Point3f point;
							for (int j = 0; j < ov.length; j += 3) {
								point = new Point3f(ov[j], ov[j + 1], ov[j + 2]);
								mrot.transform(point);
								ov[j] = point.x;
								ov[j + 1] = point.y;
								ov[j + 2] = point.z;
							}

						} else if (trans instanceof Scalling) {
							s = (Scalling) trans;
							s.getMatrix();
							for (int j = 0; j < ov.length; j += 3) {
								ov[j] *= s.x;
								ov[j + 1] *= s.y;
								ov[j + 2] *= s.z;
							}
						}
						object.lastVertices = ov;
					}
				}
			}

			float[] v = processMeshConcat(i);
			float[] normals = new float[v.length];
			// concatener tous les vertices pour chaque keyFrame i
			mdTab[i] = new MeshData(v, normals);
			mdTab[i].name = "anim" + i;
		}
		mdTab[mdTab.length - 1] = mdTab[mdTab.length - 2];
		mdTab[mdTab.length - 1].name = "####";
	}

	private float[] processMeshConcat(int i) {
		ArrayList<Float> allVertices = new ArrayList<Float>();
		for (TempMesh tm : meshList) {

			for (int t = 0; t < tm.getVertices().length; t++) {
				allVertices.add(tm.getVertices(i)[t]);
			}
		}

		float[] res = new float[allVertices.size()];
		for (int v = 0; v < allVertices.size(); v++)
			res[v] = allVertices.get(v);

		return res;

	}

	private Vector calculateFaceNormal(int a, int b, int c) {
		/*
		 * Vector vertexA = new Vector(vertices[topology[a]],
		 * vertices[topology[a] + 1], vertices[topology[a] + 2]); Vector vertexB =
		 * new Vector(vertices[topology[b]], vertices[topology[b] + 1],
		 * vertices[topology[b] + 2]); Vector vertexC = new
		 * Vector(vertices[topology[c]], vertices[topology[c] + 1],
		 * vertices[topology[c] + 2]);
		 */
		Vector vertexA = new Vector(vertices[a], vertices[a + 1],
				vertices[a + 2]);
		Vector vertexB = new Vector(vertices[b], vertices[b + 1],
				vertices[b + 2]);
		Vector vertexC = new Vector(vertices[c], vertices[c + 1],
				vertices[c + 2]);

		Vector normal = new Vector();

		vertexB.substract(vertexA);
		vertexC.substract(vertexA);
		// vertexA.substract(vertexB);
		// vertexB.substract(vertexC);

		normal = Vector.crossProduct(vertexB, vertexC);
		normal.normalize();
		return normal;
	}

	class Face {
		int a, b, c;

		Vector normal = null;

		int group;

		public Face(int vertexA, int vertexB, int vertexC) {
			a = vertexA;
			b = vertexB;
			c = vertexC;
			normal = calculateFaceNormal(a, b, c);

			sharedFaces[a].addElement(this);
			sharedFaces[b].addElement(this);
			sharedFaces[c].addElement(this);
		}

		/*
		 * public Point3f a() { return vertices[a]; }
		 * 
		 * 
		 * public Point3f b() { return vertices[b]; }
		 * 
		 * 
		 * public Point3f c() { return vertices[c]; }
		 */
	};

	class Surface {
		java.util.Vector faces = new java.util.Vector();

		public Surface() {
		}

		public void add(Face f) {
			faces.addElement(f);
		}

		public Enumeration faces() {
			return faces.elements();
		}

		public int noofFaces() {
			return faces.size();
		}
	};

	//
	// List of chunks contained in a .3DS file that we
	// are interested in.
	//

	// .3DS file magic number

	public static final int S3D_M3DMAGIC = 0x4d4d;

	// Tag IDs

	public static final int S3D_MMAGIC = 0x3d3d;

	public static final int S3D_MESH_VERSION = 0x0001;

	public static final int S3D_M3D_VERSION = 0x0002;

	public static final int S3D_COLOR_F = 0x0010;

	public static final int S3D_COLOR_24 = 0x0011;

	public static final int S3D_INT_PERCENTAGE = 0x0030;

	public static final int S3D_FLOAT_PERCENTAGE = 0x0031;

	public static final int S3D_MASTER_SCALE = 0x0100;

	public static final int S3D_BIT_MAP = 0x1100;

	public static final int S3D_USE_BIT_MAP = 0x1101;

	public static final int S3D_SOLID_BGND = 0x1200;

	public static final int S3D_USE_SOLID_BGND = 0x1201;

	public static final int S3D_V_GRADIENT = 0x1300;

	public static final int S3D_USE_V_GRADIENT = 0x1301;

	public static final int S3D_LO_SHADOW_BIAS = 0x1400;

	public static final int S3D_HI_SHADOW_BIAS = 0x1410;

	public static final int S3D_SHADOW_MAP_SIZE = 0x1420;

	public static final int S3D_SHADOW_SAMPLES = 0x1430;

	public static final int S3D_SHADOW_RANGE = 0x1440;

	public static final int S3D_AMBIENT_LIGHT = 0x2100;

	public static final int S3D_FOG = 0x2200;

	public static final int S3D_USE_FOG = 0x2201;

	public static final int S3D_FOG_BGND = 0x2210;

	public static final int S3D_DISTANCE_CUE = 0x2300;

	public static final int S3D_USE_DISTANCE_CUE = 0x2301;

	public static final int S3D_DCUE_BGND = 0x2310;

	public static final int S3D_DEFAULT_VIEW = 0x3000;

	public static final int S3D_VIEW_TOP = 0x3010;

	public static final int S3D_VIEW_BOTTOM = 0x3020;

	public static final int S3D_VIEW_LEFT = 0x3030;

	public static final int S3D_VIEW_RIGHT = 0x3040;

	public static final int S3D_VIEW_FRONT = 0x3050;

	public static final int S3D_VIEW_BACK = 0x3060;

	public static final int S3D_VIEW_USER = 0x3070;

	public static final int S3D_VIEW_CAMERA = 0x3080;

	public static final int S3D_VIEW_WINDOW = 0x3090;

	public static final int S3D_NAMED_OBJECT = 0x4000;

	public static final int S3D_OBJ_HIDDEN = 0x4010;

	public static final int S3D_OBJ_VIS_LOFTER = 0x4011;

	public static final int S3D_OBJ_DOESNT_CAST = 0x4012;

	public static final int S3D_OBJ_MATTE = 0x4013;

	public static final int S3D_N_TRI_OBJECT = 0x4100;

	public static final int S3D_POINT_ARRAY = 0x4110;

	public static final int S3D_POINT_FLAG_ARRAY = 0x4111;

	public static final int S3D_FACE_ARRAY = 0x4120;

	public static final int S3D_MSH_MAT_GROUP = 0x4130;

	public static final int S3D_TEX_VERTS = 0x4140;

	public static final int S3D_SMOOTH_GROUP = 0x4150;

	public static final int S3D_MESH_MATRIX = 0x4160;

	public static final int S3D_N_DIRECT_LIGHT = 0x4600;

	public static final int S3D_DL_SPOTLIGHT = 0x4610;

	public static final int S3D_DL_OFF = 0x4620;

	public static final int S3D_DL_SHADOWED = 0x4630;

	public static final int S3D_N_CAMERA = 0x4700;

	// Material file Chunk IDs

	public static final int S3D_MAT_ENTRY = 0xafff;

	public static final int S3D_MAT_NAME = 0xa000;

	public static final int S3D_MAT_AMBIENT = 0xa010;

	public static final int S3D_MAT_DIFFUSE = 0xa020;

	public static final int S3D_MAT_SPECULAR = 0xa030;

	public static final int S3D_MAT_SHININESS = 0xa040;

	public static final int S3D_MAT_SHININESS_STRENGTH = 0xa041;

	public static final int S3D_MAT_TRANSPARENCY = 0xa050;

	public static final int S3D_MAT_WIRE = 0xa085;

	public static final int S3D_MAT_WIRESIZE = 0xa087;

	public static final int S3D_MAT_SELF_ILLUM = 0xa080;

	public static final int S3D_MAT_TWO_SIDE = 0xa081;

	public static final int S3D_MAT_DECAL = 0xa082;

	public static final int S3D_MAT_ADDITIVE = 0xa083;

	public static final int S3D_MAT_SHADING = 0xa100;

	public static final int S3D_MAT_TEXMAP = 0xa200;

	public static final int S3D_MAT_OPACMAP = 0xa210;

	public static final int S3D_MAT_REFLMAP = 0xa220;

	public static final int S3D_MAT_BUMPMAP = 0xa230;

	public static final int S3D_MAT_MAPNAME = 0xa300;// TODO

	// Reverse engineered hierarchy information //keyframe

	public static final int S3D_KEYFRAME = 0xb000;

	public static final int S3D_KEYFRAME_NODE = 0xb002;

	public static final int S3D_KEYFRAME_LINK = 0xb010;

	public static final int KEYFRAME_SEGMENT = 0xB008;

	public static final int KEYFRAME_CURRENT_TIME = 0xB009;

	private static final int S3D_KEYFRAME_HEADER = 0xB00A;

	public static final int CAMERA_NODE_TAG = 0xB003;

	public static final int S3D_INSTANCE_NAME = 0xb011;

	public static final int S3D_PIVOT = 0xb013;

	public static final int S3D_POS_TRACK_TAG = 0xb020;

	public static final int S3D_ROT_TRACK_TAG = 0xb021;

	public static final int S3D_SCL_TRACK_TAG = 0xb022;

	public static final int S3D_NODE_ID = 0xb030;

	public static final int S3D_OBJECT_LINK_NULL = 0xffff;

	// Dummy Chunk ID

	public static final int S3D_DUMMY_CHUNK = 0xffff;

	// These chunks are found in the .PRJ file (only as far as I know)

	public static final int S3D_PROJECT_FILE = 0xc23d;

	public static final int S3D_MAPPING_RETILE = 0xc4b0;

	public static final int S3D_MAPPING_CENTRE = 0xc4c0;

	public static final int S3D_MAPPING_SCALE = 0xc4d0;

	public static final int S3D_MAPPING_ORIENTATION = 0xc4e1;

}
