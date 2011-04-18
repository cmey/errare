package mapEditor;

/*

import genericEngine.Engine;
import geom.Point;
import geom.Sphere;
import geom.Vector;
import geom.Triangle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import main.EditorMain;

import com.sun.jmx.remote.internal.ArrayQueue;

import physicsEngine.Octree;


public class EditorEngine implements Engine{
	
	public static final String PATH_TO_POINT_ID="maps/point";
	public static final int PATCH_LEVEL = 3;
	public static final int SIGHT = 5000;
	private Octree octree;
	private EditorMain main;
	private Point selectedPoint;
	private ArrayList<String> actions;
	

	public EditorEngine(EditorMain main) throws FileNotFoundException, IOException, ClassNotFoundException {
		this.main=main;
		Point.loadStaticID(new File(PATH_TO_POINT_ID));
		actions = new ArrayList<String>();
		octree = Octree.load("0");
		main.getEditorGraphicsEngine().setDisplayList(octree);
		main.getEditorGraphicsEngine().setPatchToCompile(octree);
	}

	public boolean invokeKeyEvent(String action) {
		synchronized (actions) {
			actions.add(action);
		}
		return false;
	}

	private void processActions() {
		synchronized (actions) {
			for(String action : actions) {
				if(action.equalsIgnoreCase("push")) {
					Vector direction = new Vector(main.getEditorGraphicsEngine().getCameraPosition(), selectedPoint);
					direction.normalize();
					direction.mult(50);
					movePoint(selectedPoint, direction);
				}
				else if(action.equalsIgnoreCase("pull")) {
					Vector direction = new Vector(selectedPoint, main.getEditorGraphicsEngine().getCameraPosition());
					direction.normalize();
					direction.mult(50);
					movePoint(selectedPoint, direction);
				}
				else if(action.equalsIgnoreCase("exit")) {
					System.out.println("EXIT");
					try {
						Point.saveStaticID(new File(PATH_TO_POINT_ID));
						octree.unload();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
					System.exit(0);
				}
			}
			actions.clear();
		}
	}
	
	
	private void movePoint(Point point, Vector direction) {
		
		point.translate(direction);
		if(point.x<octree.getBoundingBox().getLeftFrontBottom().x)
			point.x=octree.getBoundingBox().getLeftFrontBottom().x;
		if(point.y<octree.getBoundingBox().getLeftFrontBottom().y)
			point.y=octree.getBoundingBox().getLeftFrontBottom().y;
		if(point.z<octree.getBoundingBox().getLeftFrontBottom().z)
			point.z=octree.getBoundingBox().getLeftFrontBottom().z;
		
		if(point.x>octree.getBoundingBox().getRightBackTop().x)
			point.x=octree.getBoundingBox().getRightBackTop().x;
		if(point.y>octree.getBoundingBox().getRightBackTop().y)
			point.y=octree.getBoundingBox().getRightBackTop().y;
		if(point.z>octree.getBoundingBox().getRightBackTop().z)
			point.z=octree.getBoundingBox().getRightBackTop().z;

		HashSet<Octree> patchesToCompile = new HashSet<Octree>();

		for(Triangle t : Octree.getTriangleList(point)) {

			Octree removedFrom = octree.removeTriangle(t);
			
			while(removedFrom.getLevel()<PATCH_LEVEL) {
				removedFrom=removedFrom.getParent();
			}
			patchesToCompile.add(removedFrom);

			for(Point p : t.points) {
				if(p.equals(point)) {
					p.x=point.x;
					p.y=point.y;
					p.z=point.z;
					
					
				}
			}

			if(!octree.getBoundingBox().contains(t)) {
				System.out.println(t);
			}
			Octree addedFrom = octree.addTriangle(t);
			while(addedFrom.getLevel()<PATCH_LEVEL) {
				addedFrom=addedFrom.getParent();
			}
			patchesToCompile.add(addedFrom);
		}
		
			
			
		for(Octree  o : patchesToCompile)
			main.getEditorGraphicsEngine().setPatchToCompile(o);

	}

	public boolean invokeMouseEvent(String action, int x, int y) {

		if(action.equalsIgnoreCase("pick")) {
			main.getEditorGraphicsEngine().askPickedPoint(x, y);
		}
		return false;
	}

	public void receivePickPoint(Point p) {
		selectedPoint=p;
	}

	public void run() throws FileNotFoundException, IOException, ClassNotFoundException{

		processActions();

		Point cameraPos = main.getEditorGraphicsEngine().getCameraPosition();


		loadOctree(octree, cameraPos);

//testTree(octree);
	}


	@SuppressWarnings("unused")
	private void testTree(Octree octree) {
		System.out.println("level="+octree.getLevel()+" "+octree.getTriangles());
			if(octree.getLevel()>1) {
				for(Octree child : octree.getChildren()) {
					if(child!=null) {
						testTree(child);
					}
				}
			}
		
	}

	private void loadOctree(Octree octree, Point cameraPos) throws FileNotFoundException, IOException, ClassNotFoundException {
		
		
		if(octree.getLevel()>1) {
			Sphere sight = new Sphere(cameraPos, SIGHT);

			if(sight.instersectsOrContains(octree.getBoundingBox())) {
				for(int i=0; i<8; i++) {
					if(octree.getChildren()[i]==null) {
						Octree child = Octree.load(octree.getPath()+i);
						child.setParent(octree);
						octree.getChildren()[i]=child;

						if(child.getLevel()<=PATCH_LEVEL) {
							recursLoad(child);
						}
						
						main.getEditorGraphicsEngine().setPatchToCompile(child);
						return;
					}

					if(octree.getLevel()>PATCH_LEVEL) {
						loadOctree(octree.getChildren()[i], cameraPos);
					}
					
				}
			}
			else {
				Sphere halfSight = new Sphere(cameraPos, SIGHT*4);
				if(!halfSight.instersectsOrContains(octree.getBoundingBox()))
					for(int i=0; i<8; i++) {
						if(octree.getChildren()[i]!=null) {
							octree.getChildren()[i].unload();
						}
					}

			}
		}

	}
	
	

	private void recursLoad(Octree octree) throws FileNotFoundException, IOException, ClassNotFoundException {
		if(octree.getLevel()>1) {
			for(int i=0; i<8; i++) {
				Octree child = Octree.load(octree.getPath()+i);
				child.setParent(octree);
				octree.getChildren()[i]=child;
				
				recursLoad(child);
			}
		}
		
	}

	public void quit() {
		// TODO Auto-generated method stub
	}

}
*/