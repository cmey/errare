package mapEditor;
/*
import geom.Point;
import geom.Triangle;
import geom.Vector;
import graphicsEngine.GraphicsEngine;
import graphicsEngine.Terrain;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.HashSet;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;
import javax.swing.JFrame;

import net.java.games.joal.util.BufferUtils;

import main.ClientMain;
import main.EditorMain;
import physicsEngine.Octree;

public class EditorGraphicsEngine extends GraphicsEngine{

	private EditorMain editormain;

	public EditorGraphicsEngine(JFrame f, EditorMain main) {
		super(main);
		editormain = main;
	}

	public Vector getCameraDirection() {
		Point eye = new Point(world.cam.location);
		Point look = new Point(world.cam.lookingAt);

		return new Vector(eye, look);
	}

	public void setPatchToCompile(Octree patchToCompile) {

		if(patchToCompile==null){
			System.out.println("Tu me passes un null !!!");
		}else{
			Terrain terrain = octree2terrain.get(patchToCompile);
			if(terrain == null) { // this octree has no terrain : just loaded
				terrain = new Terrain();
				terrain.setOctreeAssocied(patchToCompile); // associate this octree and his children !
				octree2terrain.put(patchToCompile, terrain);
			}else{ // this terrain has been updated
				terrain.must_recompile = true;
			}
		}
	}


	public Point getCameraPosition() {
		return new Point(world.cam.location);
	}

	public void askPickedPoint(int x, int y) {
		selection_mouse_x = x;
		selection_mouse_y = y;
		selection_mouse_pick_done = true;
	}

	protected void start_Display_event(GL gl){

		if(selection_mouse_pick_done){

			final int bufferSize  = 512;
			ByteBuffer byteBuffer = ByteBuffer.allocateDirect(
					BufferUtils.SIZEOF_INT*bufferSize);
			byteBuffer.order(ByteOrder.nativeOrder());
			selectionBuffer  = byteBuffer.asIntBuffer();
			int buffsize = 512;
			viewPort = new int[4];
			hits = 0;
			gl.glGetIntegerv(GL.GL_VIEWPORT, viewPort,0);
			gl.glSelectBuffer(buffsize, selectionBuffer);		

			gl.glRenderMode(GL.GL_SELECT);
		}

	}

	protected void start_3D_event(GL gl, GLU glu){
		if(selection_mouse_pick_done){
			gl.glInitNames();
			gl.glPushName(0);
			//	gl.glMatrixMode(GL.GL_PROJECTION);
			//gl.glPushMatrix();
			//gl.glLoadIdentity();
			glu.gluPickMatrix(selection_mouse_x, (double) viewPort[3] - selection_mouse_y, 1.0d, 1.0d, viewPort,0);
			//glu.gluOrtho2D(0.0d, 1.0d, 0.0d, 1.0d);
		}
	}

	protected void end_3D_event(GL gl){
		if(selection_mouse_pick_done){
			gl.glMatrixMode(GL.GL_PROJECTION);
			//gl.glPopMatrix();
			gl.glFlush();
			hits = gl.glRenderMode(GL.GL_RENDER);

			processHits(hits, selectionBuffer);

			selection_mouse_pick_done = false;
			selectionHit2Triangle.clear();
		}
	}



	protected void processHits(int hits, IntBuffer buffer)
	{
		int id = 0;
		long miniZ = Long.MAX_VALUE;
		for(int i=0; i<hits; i++) {
			int nbnames = buffer.get();
			//System.out.println("nb names="+nbnames);
			long z1 = Converter.uint2long(buffer.get());
			long z2 = Converter.uint2long(buffer.get());
			//System.out.println("z1="+z1);
			//System.out.println("z2="+z2);

			for(int j=0; j<nbnames; j++){
				int nam = buffer.get();
				if(z1 < miniZ){
					miniZ = z1;
					id = nam;
				}
				//System.out.println("name="+nam);
			}

		}
		//System.out.println("----------------------");
		Triangle t = selectionHit2Triangle.get(id);

		if(t!=null){
			Point p = t.closestPoint(new Point((float)intersectX,(float)intersectY,(float)intersectZ));

			editormain.getEditor().receivePickPoint(p);
		}
	}


}
*/