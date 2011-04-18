package loader3DS;

import java.io.IOException;

public class Main {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		new ModelLoader("loader3DS/man_export.3DS").loadObject();
		//new ModelLoader("loader3DS/4mur1sol.3ds").loadObject();
		//new ModelLoader("loader3DS/cube.3ds").loadObject();
		//new ModelLoader("loader3DS/sphere.3ds").loadObject();
		//new ModelLoader("loader3DS/plane.3ds").loadObject();
		//new ModelLoader("loader3DS/plan.3DS").loadObject();
		//new ModelLoader("loader3DS/terrain.3ds").loadObject();
		//new ModelLoader("loader3DS/terrainv1.3ds").loadObject();
		//new ModelLoader("loader3DS/Tower.3ds").loadObject();
		//new ModelLoader("loader3DS/Animation1.3ds").loadObject();
		//new ModelLoader("loader3DS/box_bone.3ds").loadObject();
	}

}
