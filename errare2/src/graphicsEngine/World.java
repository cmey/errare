package graphicsEngine;

import physicsEngine.Octree;

public class World {
	
	Octree root;
	Skydome sky;
	public Camera cam;
	Sun sun;
	public Heightmap heightmap;
	
	public World(Octree root){
		this.root = root;
		this.sky = new Skydome(Skydome.SKY_CLEAR);
		this.cam = new EyeCamera();
		sun = new Sun();
	}
	
	public World(Octree root, Camera cam){
		this.root = root;
		this.sky = new Skydome(Skydome.SKY_CLEAR);
		this.cam = cam;
		sun = new Sun();
	}
	
	public World(Octree root, Skydome sky, Camera cam){
		this.root = root;
		this.sky = sky;
		this.cam = cam;
		sun = new Sun();
	}
	
	public World(Octree root, Skydome sky, Camera cam, Sun sun){
		this.root = root;
		this.sky = sky;
		this.cam = cam;
		this.sun = sun;
	}
	
	public void setHeightmap(Heightmap h){
		this.heightmap = h;
	}

}
