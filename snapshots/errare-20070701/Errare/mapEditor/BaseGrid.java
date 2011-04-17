package mapEditor;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.util.Hashtable;

import javax.swing.SwingUtilities;

import com.sun.org.apache.xpath.internal.axes.PathComponent;

import physicsEngine2.Octree;
import physicsEngine2.TreeCube;

import net.java.games.jogl.GL;
import net.java.games.jogl.GLDrawable;
import geom.Point;
import geom.Sphere;
import graphicsEngine.Heightmap;

public class BaseGrid implements KeyListener{
	
	private Hashtable<Sphere, ControlPoint> controlPoints;
	
	/* inter cell spacing */
	private float spacing;
	private float[][] grid;
	EditorGraphicsEngine engine;
	
	private int brushSize = 2;
	private Octree octree;
	private float sharpness=0.3f;

	public final static int mousespeed = 10;
	private ControlPoint selectedControlPoint;
	
	public BaseGrid(int count, float spacing, EditorGraphicsEngine engine){
		this.grid = new float[count][count];
		this.spacing = spacing;
		this.engine = engine;
		
		controlPoints = new Hashtable<Sphere, ControlPoint>();
		
		float size = spacing*(count-1);
		octree = new Octree(new TreeCube(new Point(0, -size/2, 0), new Point(size, size/2, size)), size/8, null);
		
	}
	

	
	public void modifyElevation(double x, double z, MouseEvent e){
		
		int gx = (int) Math.round(x / spacing);
		int gy = (int) Math.round(z / spacing);
		
		if (SwingUtilities.isMiddleMouseButton(e)){
			parabolize(gx, gy, brushSize, -1);
		}
		else if(SwingUtilities.isLeftMouseButton(e)){
			parabolize(gx, gy, brushSize, 1);
		}
		
	}
	
	public void startAddingOccluder(float x, float y, float z){
		TreeCube c = new TreeCube(new Point(x, 0, z), new Point(x+1000, 1000, z+1000));
		c.setVisible(true);
		c.setColor(Color.RED);
		c.setWire(true);
		octree.addOccluder(c);
		
		Point c11 = new Point(c.getLeftFrontBottom());
		c11.translate(-50, -50, -50);
		Point c12 = new Point(c.getLeftFrontBottom());
		c12.translate(50, 50, 50);
		
		ControlPoint c1 = new ControlPoint(c11,c12,c.getLeftFrontBottom());
		octree.addContent(c1);
		controlPoints.put(c1.getSphere(), c1);
		
		
		Point c21 = new Point(c.getRightBackTop());
		c21.translate(-50, -50, -50);
		Point c22 = new Point(c.getRightBackTop());
		c22.translate(50, 50, 50);
		
		
		ControlPoint c2 = new ControlPoint(c21,c22,c.getRightBackTop());
		octree.addContent(c2);
		controlPoints.put(c2.getSphere(), c2);
	
	}
	
	public void setSelectedControlPoint(Sphere s) {
		System.out.println(s);
		if(s!=null)
			selectedControlPoint = controlPoints.get(s);
	}
	
	public void moveSelectedControlPoint(int mousex, int mousey) {
		if(selectedControlPoint!=null)
		selectedControlPoint.getControlPoint().translate(mousex*mousespeed, 0, mousey*mousespeed);
	}
	
	
	public void parabolize(int cx, int cy, int rad, int direction) {
		int startx = cx-rad;
		int endx=cx+rad;
		int starty=cy-rad;
		int endy=cy+rad;
		if(startx<0)
			startx=0;
		if(starty<0)
			starty=0;
		if(endx>grid.length)
			endx=grid.length;
		if(endy>grid[0].length)
			endy=grid[0].length;
		
		/* compute index of patches that are modified */
		int indexstartx = startx / Heightmap.Patch_size;
		int indexendx = endx / Heightmap.Patch_size;
		if(endx % Heightmap.Patch_size != 0) indexendx++;
		int indexstarty = starty / Heightmap.Patch_size;
		int indexendy = endy / Heightmap.Patch_size;
		if(endy % Heightmap.Patch_size != 0) indexendy++;
		
		
		rad*=rad;
		
		for(int x=startx; x<endx; x++) {
			for(int y=starty; y<endy; y++) {
				int z = rad - ((x-cx)*(x-cx) + (y-cy)*(y-cy));
				
				if(z>0) {
					grid[x][y]+=z*direction/(rad*sharpness);
				}
				
			}
		}
		
		/* actually tell the engine to recompile heightmaps */
		engine.setModifiedHeightmaps(indexstartx,indexendx,indexstarty,indexendy);
	}

	public Octree getOctree(){
		return octree;
	}


	public float[][] getGrid() {
		return grid;
	}
	
	public float getSize() {
		return spacing;
	}



	public void keyTyped(KeyEvent e) {
	}



	public void keyPressed(KeyEvent e) {

		if(e.getKeyCode()==KeyEvent.VK_UP) {
			brushSize++;
		}
		else if(e.getKeyCode()==KeyEvent.VK_DOWN) {
			if(brushSize>0)
				brushSize--;
		}
		else if(e.getKeyCode()==KeyEvent.VK_LEFT) {
			sharpness+=0.1;
		}
		else if(e.getKeyCode()==KeyEvent.VK_RIGHT) {
			if(sharpness>0.1)
				sharpness-=0.1;
		}
		else if(e.getKeyCode()==KeyEvent.VK_F6 && selectedControlPoint!=null) {
			selectedControlPoint.translate(100, 100, 100);
		}
	}



	public void keyReleased(KeyEvent e) {
	}
	
}
