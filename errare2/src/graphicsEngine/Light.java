package graphicsEngine;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.glu.GLU;

import geom.Matrix;
import geom.Point;

public class Light extends Graphics {

    transient public int posX,  posY,  posZ;
    Matrix lightProjectionMatrix = new Matrix(4, 4);
    Matrix lightViewMatrix = new Matrix(4, 4);
    float[] lightProjectionMatrixBuffer = new float[16];
    float[] lightViewMatrixBuffer = new float[16];
    float[] LightdimAmbient = {0.1f, 0.1f, 0.1f};
    float[] LightdimDiffuse = {0.2f, 0.2f, 0.2f};
    float[] LightbrightAmbient = {0.8f, 0.8f, 0.8f};
    float[] LightbrightDiffuse = {1.0f, 1.0f, 1.0f};
    float[] LightBlack = {0.0f, 0.0f, 0.0f};
    float[] LightWhite = {1.0f, 1.0f, 1.0f};
    float[] LightSpecular = { 1.0f , 1.0f , 1.0f , 1.0f };
    float[] global_ambient = new float[]{0.2f, 0.2f, 0.2f, 1.0f};

    
    void enableLighting(GL gl){
        //Set up materials
        gl.glColorMaterial(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE);
        gl.glEnable(GL.GL_COLOR_MATERIAL);
        gl.glMaterialfv(GL.GL_FRONT, GL.GL_SPECULAR, LightWhite, 0);
        gl.glMaterialf(GL.GL_FRONT, GL.GL_SHININESS, 16.0f);
        
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_AMBIENT, LightdimAmbient, 0);
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_DIFFUSE, LightdimDiffuse, 0);
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_SPECULAR, LightBlack, 0);
        gl.glLightfv(GL.GL_LIGHT1, GL.GL_AMBIENT, LightbrightAmbient, 0);
        gl.glLightfv(GL.GL_LIGHT1, GL.GL_DIFFUSE, LightbrightDiffuse, 0);
        gl.glLightfv(GL.GL_LIGHT1, GL.GL_SPECULAR, LightWhite, 0);
        gl.glLightModelfv(GL.GL_LIGHT_MODEL_AMBIENT, global_ambient, 0);
        
        gl.glEnable(GL.GL_LIGHT0);
        gl.glEnable(GL.GL_LIGHT1);
        gl.glEnable(GL.GL_LIGHTING);
    }
    
    @Override
    protected void draw(GLAutoDrawable gld) {
    // TODO Auto-generated method stub

    }

    @Override
    protected void drawByTriangles(GLAutoDrawable gld) {
    // TODO Auto-generated method stub

    }

    protected void calculateAndSaveMatrices(GLAutoDrawable gld) {
        GL gl = gld.getGL();
        GLU glu = new GLU();

        gl.glPushMatrix();
        gl.glLoadIdentity();
        glu.gluPerspective(45.0f, 1.0f, ge.shadowmapping.nearZ, ge.shadowmapping.farZ);
        gl.glGetFloatv(GL.GL_MODELVIEW_MATRIX, lightProjectionMatrixBuffer, 0);
        lightProjectionMatrix.setArray(lightProjectionMatrixBuffer);
        gl.glLoadIdentity();
        glu.gluLookAt(posX, posY, posZ,
                0.0f, 0.0f, 0.0f,
                0.0f, 1.0f, 0.0f);
        gl.glGetFloatv(GL.GL_MODELVIEW_MATRIX, lightViewMatrixBuffer, 0);
        lightViewMatrix.setArray(lightViewMatrixBuffer);
        gl.glPopMatrix();
    }

    public Point getPosition() {
        return new Point(posX, posY, posZ);
    }

    public float[] getPositionBuffer() {
        float[] positionbuffer = {posX, posY, posZ, 1};
        return positionbuffer;
    }

    public float[] getPositionBufferInvertedY() {
        float[] positionbuffer = {posX, -posY, posZ, 1};
        return positionbuffer;
    }

    @Override
    protected void drawGeometryOnly(GLAutoDrawable gld) {
    // TODO Auto-generated method stub

    }
}
