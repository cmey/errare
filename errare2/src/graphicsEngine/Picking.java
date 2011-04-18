package graphicsEngine;

import geom.Triangle;
import java.awt.event.MouseEvent;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Hashtable;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.glu.GLU;
import logger.Logger;
import mapEditor.Converter;
import net.java.games.joal.util.BufferUtils;

/**
 * @author christophe
 * @date 26 mars 2008
 */
public class Picking extends Graphics {

    boolean selection_mouse_pick_done;
    Hashtable<Integer, GraphicalRep> selectionHit2graphicalRep;
    Hashtable<Integer, Triangle> selectionHit2Triangle;
    int hits;
    IntBuffer selectionBuffer;
    static int nextTriangleName_selection;
    int[] viewPort;
    int selection_mouse_x;
    int selection_mouse_y;
    double intersectX;
    double intersectY;
    double intersectZ;
    World world;

    Picking (World world) { 
        this.world = world;
        final int bufferSize = 512;
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(BufferUtils.SIZEOF_INT * bufferSize);
        byteBuffer.order(ByteOrder.nativeOrder());
        selectionBuffer = byteBuffer.asIntBuffer();
        viewPort = new int[4];
    }

    void click(MouseEvent e) {
        if(!ge.terrain_found) Logger.printWARNING("picking is unstable without a terrain !");
		this.selection_mouse_pick_done = true;
		this.selection_mouse_x = e.getX();
		this.selection_mouse_y = e.getY();
    }

    void drawCursor(GL gl) {
        gl.glPushMatrix();
		gl.glColor4f(1,1,1,1);
		gl.glTranslated(intersectX, intersectY, intersectZ);
		gl.glEnable(GL.GL_TEXTURE_2D);
		//cursor.draw(gld);
		gl.glPopMatrix();
    }

    void initGLPickingBuffer(GL gl) {
        gl.glSelectBuffer(selectionBuffer.capacity(), selectionBuffer);
    }

    
    void selectionPass(GL gl, GLU glu) {
        /*******************************/
	/** SELECTION PASS FOR PICKING**/
		if(selection_mouse_pick_done) {
			// get the pick coords
			gestionPicking(selection_mouse_x, selection_mouse_y, gl, glu);
			// get the picked object
			pickingPass(gl, glu, selection_mouse_x, selection_mouse_y);
			selection_mouse_pick_done = false;
		}
    }
    
    protected void gestionPicking(int mousex, int mousey, GL gl, GLU glu) {
		glu.gluLookAt(world.cam.location.x, world.cam.location.y, world.cam.location.z, world.cam.lookingAt.x, world.cam.lookingAt.y, world.cam.lookingAt.z, 0,1,0);
		double[] modelview = new double[16];
		double[] projection = new double[16];
		int[] viewport = new int[4];

		gl.glGetDoublev(GL.GL_MODELVIEW_MATRIX, modelview,0);
		gl.glGetDoublev(GL.GL_PROJECTION_MATRIX, projection,0);
		gl.glGetIntegerv(GL.GL_VIEWPORT, viewport,0);
		
		double winX; winX=mousex;
		double winY; winY=mousey;
		winY = (double)viewport[3] - (double)winY;
		float[] winZ = new float[1];
		gl.glReadPixels ((int)winX, (int)winY, 1, 1, GL.GL_DEPTH_COMPONENT, GL.GL_FLOAT, FloatBuffer.wrap(winZ));
		
		//System.out.print("mouse pos: "+winX +" "+winY+" "+winZ[0]);    	  
		
		double[] objXYZ = new double[3];
		glu.gluUnProject(
				winX, winY, winZ[0],modelview,0,
				projection,0,
				viewport,0,
				objXYZ,0
		);
		
		//if(DEBUG) System.out.print("   3Dclik: "+(int)objXYZ[0] + " " + (int)objXYZ[1] + " " + (int)objXYZ[2]);
		
		intersectX = objXYZ[0];
		intersectY = objXYZ[1];
		intersectZ = objXYZ[2];		
	}
    
    
    protected void pickingPass(GL gl, GLU glu, int cursorX, int cursorY){
		// enter GL selection mode
		gl.glRenderMode(GL.GL_SELECT);
		
		// no need to really draw : accelerate rendering
		gl.glDisable(GL.GL_TEXTURE_2D);
		gl.glDisable(GL.GL_BLEND);
		gl.glDisable(GL.GL_DITHER);
		gl.glDisable(GL.GL_LIGHTING);
		gl.glShadeModel(GL.GL_FASTEST);
		
		// select in a tiny area around the cursor
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glPushMatrix();
		gl.glLoadIdentity();
		gl.glGetIntegerv(GL.GL_VIEWPORT, viewPort,0);
		glu.gluPickMatrix(selection_mouse_x, (double) viewPort[3] - selection_mouse_y, 1.0d, 1.0d, viewPort,0);
		ge.setPerspective(gl, glu); // returns in MODELVIEW
		
		// init names stack
		gl.glInitNames();
		gl.glPushName(-1);
		
		// render pickable objects (eventually only bounding boxes)
		//processOctreeForPicking(gld, gl, glu, world.root); // does calls to glLoadName(model_id) or glPushName and glPopName;
		
		// exit selection mode and restore original state
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glPopMatrix();
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glFlush();
		hits = gl.glRenderMode(GL.GL_RENDER);
		
		// treat hits
		processHits(hits, selectionBuffer);
		
		// clear working buffers
		selectionHit2Triangle.clear();
		selectionHit2graphicalRep.clear();
		selectionBuffer.clear();
	}
    
    
    protected void processHits(int hits, IntBuffer buffer)
	{
		int id = 0;
		long miniZglob = Long.MAX_VALUE;
		for(int i=0; i<hits; i++) {
			int nbnames = buffer.get();
			//System.out.println("nb names="+nbnames);
			long miniZ = Converter.uint2long(buffer.get());
			long maxiZ = Converter.uint2long(buffer.get());
			//System.out.println("z1="+z1);
			//System.out.println("z2="+z2);
			
			for(int j=0; j<nbnames; j++){
				int nam = buffer.get();
				if(miniZ < miniZglob){
					miniZglob = miniZ;
					id = nam;
				}
				//System.out.println("name="+nam);
			}
		}
		//System.out.println("----------------------");
		GraphicalRep gr = selectionHit2graphicalRep.get(id);

		if(gr!=null){
			gr.setHighlighted(true);
		}
	}
    
    protected void addTriangleNameInSelectionMode(Triangle t){
		selectionHit2Triangle.put(nextTriangleName_selection,t);
	}

    @Override
    protected void draw(GLAutoDrawable gld) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void drawGeometryOnly(GLAutoDrawable gld) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void drawByTriangles(GLAutoDrawable gld) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
