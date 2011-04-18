/*Errare is a free and crossplatform MMORPG project.
Copyright (C) 2006  Christophe MEYER
This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.
This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.*/
package graphicsEngine;

import genericReps.CharacterRep;
import genericReps.Rep;
import geom.Point;
import geom.Triangle;
import geom.Vector;

import java.io.Serializable;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.glu.GLU;


import gameEngine.GameCharacter;

@SuppressWarnings("serial")
public class GraphicalRep extends Graphics implements Serializable {

    transient /*TODO: maybe not transient*/ private Rep rep; // a link to the super class so here we can access all the other reps of this object
    public Mesh mesh; 	// a graphicalRep contains one mesh (static or animated)
    public Texture[] textures; // a graphicalRep contains several textures
    private int displaylist;
    public boolean cast_shadow;
    public boolean cast_reflection;
    transient static private int IDModel3D = 0;
    public boolean isWireframe;
    private boolean showAABox;
    private boolean isHighlighted;
    private boolean isCharacterRep;
    private float permRotateX,  permRotateY,  permRotateZ;

    /**
     * This method should only be used internaly by ModelLoader!
     */
    public GraphicalRep(Mesh mesh, Texture[] textures) {
        this.mesh = mesh;
        this.textures = textures;
        IDModel3D++;
        if (this.textures == null) {
            this.textures = new Texture[0];

        }
        if (this.textures.length == 0 || this.textures[0] == null) {
            if(this.mesh!=null) this.mesh.textured = false;
        }
        isWireframe = false;
        isHighlighted = false;
        showAABox = false;
    }

    protected void drawByTriangles(GLAutoDrawable gld) {

    }

    /**
     * Draws the object to the screen given it's position and orientation.
     * @param gl gl context (given by the Graphicsengine's display method)
     */
    public void draw(GLAutoDrawable gld) {
        GL gl = gld.getGL();

        if (prem) {
            prem = false;
            for (Texture tex : textures) {
                if (tex != null) {
                    tex.doExpandTexture(gl);
                }
            }

            if (Extensions.isVBOSupported) {
                this.mesh.createVBOs(gl);
            }
        }

        if (textures.length == 1 && textures[0] != null) {
            gl.glBindTexture(GL.GL_TEXTURE_2D, textures[0].internal_index[0]);
        //if(textures[0].isAlpha)	gl.glEnable(GL.GL_BLEND);   // Enable blending for transparency
        //}else{

        //TODO: bind multiples textures (several textures units)
        }

        gl.glPushAttrib(GL.GL_POLYGON_BIT | GL.GL_ENABLE_BIT);
        if (this.isWireframe) {
            gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_LINE);
        }
        if (!this.mesh.textured) {
            gl.glDisable(GL.GL_TEXTURE_2D);
        }

        gl.glPushMatrix();
        gl.glRotatef(permRotateX, 1, 0, 0);
        gl.glRotatef(permRotateY, 0, 1, 0);
        gl.glRotatef(permRotateZ, 0, 0, 1);

        if (KeyboardHelper.VBOs) {
            gl.glBindBufferARB(GL.GL_ARRAY_BUFFER_ARB, mesh.getMeshData().VBOvertices[0]);
            gl.glVertexPointer(3, GL.GL_FLOAT, 0, 0);
            if (mesh.textured) {
                gl.glEnableClientState(GL.GL_TEXTURE_COORD_ARRAY);
            }
            if (mesh.textured) {
                gl.glBindBufferARB(GL.GL_ARRAY_BUFFER_ARB, mesh.VBOtexcoords[0]);
            }
            if (mesh.textured) {
                gl.glTexCoordPointer(2, GL.GL_FLOAT, 0, 0);
            }
            gl.glBindBufferARB(GL.GL_ELEMENT_ARRAY_BUFFER_ARB, mesh.VBOindices[0]);
            gl.glDrawElements(GL.GL_TRIANGLES, mesh.topology.length, GL.GL_UNSIGNED_INT, 0);
            gl.glBindBufferARB(GL.GL_ELEMENT_ARRAY_BUFFER_ARB, 0);
            gl.glBindBufferARB(GL.GL_ARRAY_BUFFER_ARB, 0);
            if (mesh.textured) {
                gl.glDisableClientState(GL.GL_TEXTURE_COORD_ARRAY);
            }
        } else {
            if(!isDisplayListCreated){
                // create and call list
                //displaylist = gl.glGenLists(1); //TODO: gerer les animations (plusieurs meshes)
                //gl.glNewList(displaylist,GL.GL_COMPILE);
                /** BEGIN OF DISPLAY WITH IMMEDIATE MODE **/
                gl.glBegin(GL.GL_TRIANGLES);
                for (int i = 0; i < mesh.topology.length; i++) // for each vertex
                {
                    if (textures.length == 1) {
                        gl.glTexCoord2f(mesh.texcoords[0][2 * mesh.topology[i] + 0], mesh.texcoords[0][2 * mesh.topology[i] + 1]); // I set the texcoord of that vertex
                //}else{
                //	//TODO: multi tex coords
                    }
                // set the normal of that vertex
                //gl.glNormal3f(mesh.getMeshData().normal[mesh.topology[i]+0], mesh.getMeshData().normal[mesh.topology[i]+1], mesh.getMeshData().normal[mesh.topology[i]+2]);
                    gl.glVertex3f(mesh.getMeshData().vertices[3 * mesh.topology[i] + 0], mesh.getMeshData().vertices[3 * mesh.topology[i] + 1], mesh.getMeshData().vertices[3 * mesh.topology[i] + 2]);
                }
                gl.glEnd();
                /** END OF DISPLAY **/
                //gl.glEndList();
                //isDisplayListCreated = true;
                //gl.glCallList(displaylist);
            }else{
                //System.out.println("list");
                // call list
                gl.glCallList(displaylist);
            }
        }

        //drawGraphicalBoundingSphere(gl,glu);

        if (GraphicsEngine.keyboardHelper.normals && mesh.getMeshData().normals != null) {
            drawNormals(gl);
        }

        gl.glPopMatrix();


        if (isCharacterRep) {
            try { // try to print the name of the caracter (if this is not a caracter : exception)
                String name = null;
                name = ((GameCharacter) ((CharacterRep) this.getRep()).getGameRep()).getName();
                if (name != null) {
                    ge.text3d.glPrint(gl, "Player: " + name);
                }
            } catch (Exception e) {
            // this is not a GameCharacter, no problem : dont display his name
            }
        }



        gl.glPopAttrib();
        if (textures.length == 1) {
        //if(textures[0].isAlpha)	gl.glEnable(GL.GL_BLEND);   // End of blending for transparency
        //}else{
        //	//TODO: disable blend for texture units with an alpha texture
        }

        if (GraphicsEngine.keyboardHelper.draw_oculled) {
        //gl.glPushAttrib(GL.GL_ALL_ATTRIB_BITS);
        //gl.glDisable(GL.GL_DEPTH_TEST);
        //drawBoundingBox(gl,glu);
        //gl.glPopAttrib();
        }

        GraphicsEngine.triangles_drawn += mesh.topology.length / 3;

        // go forward in animation (speed is managed)
        if (mesh instanceof AnimatedMesh) {
            ((AnimatedMesh) mesh).advanceNextAnimationFrame();
        }
    }

    protected void drawNormals(GL gl) {
        gl.glPushAttrib(GL.GL_ENABLE_BIT | GL.GL_LINE_BIT | GL.GL_CURRENT_BIT);
        gl.glDisable(GL.GL_TEXTURE_2D);
        gl.glDisable(GL.GL_BLEND);
        gl.glLineWidth(4);
        gl.glDisable(GL.GL_DEPTH_TEST);
        for (int i = 0; i < mesh.topology.length; i++) { // for each normal
            gl.glPushMatrix();
            gl.glTranslatef(mesh.getMeshData().vertices[mesh.topology[i] * 3 + 0], mesh.getMeshData().vertices[mesh.topology[i] * 3 + 1], mesh.getMeshData().vertices[mesh.topology[i] * 3 + 2]);
            gl.glBegin(GL.GL_POINTS);
            gl.glColor4f(0f, 0f, 0.8f, 1f);
            gl.glVertex3f(0f, 0f, 0f);
            gl.glColor4f(1f, 0.2f, 0.2f, 1f);
            gl.glVertex3f(mesh.getMeshData().normals[mesh.topology[i] * 3 + 0], mesh.getMeshData().normals[mesh.topology[i] * 3 + 1], mesh.getMeshData().normals[mesh.topology[i] * 3 + 2]);
            gl.glEnd();
            gl.glColor4f(0f, 0f, 1f, 1f);
            gl.glBegin(GL.GL_LINES);
            gl.glVertex3f(0f, 0f, 0f);
            gl.glVertex3f(mesh.getMeshData().normals[mesh.topology[i] * 3 + 0], mesh.getMeshData().normals[mesh.topology[i] * 3 + 1], mesh.getMeshData().normals[mesh.topology[i] * 3 + 2]);
            gl.glEnd();

            gl.glPopMatrix();
        }
        gl.glPopAttrib();
    }

    @Override
    protected void drawGeometryOnly(GLAutoDrawable gld) {
        GL gl = gld.getGL();

        /** BEGIN OF DISPLAY WITH IMMEDIATE MODE **/
        gl.glBegin(GL.GL_TRIANGLES);
        for (int i = 0; i < mesh.topology.length; i++) // for each vertex
        {
            gl.glVertex3f(mesh.getMeshData().vertices[mesh.topology[i] * 3 + 0], mesh.getMeshData().vertices[mesh.topology[i] * 3 + 1], mesh.getMeshData().vertices[mesh.topology[i] * 3 + 2]);

        }
        gl.glEnd();
        /** END OF DISPLAY **/
        GraphicsEngine.triangles_drawn += mesh.topology.length / 3;
    }

    
    /**
     * In world space (absolution position).
     * @param gl
     */
    protected void drawPhysicalAABox(GL gl) {
        gl.glPushAttrib(GL.GL_CURRENT_BIT | GL.GL_ENABLE_BIT | GL.GL_POLYGON_BIT);
        gl.glDisable(GL.GL_TEXTURE_2D);
        //gl.glDisable(GL.GL_DEPTH_TEST);
        gl.glColor4f(0.6f, 0.2f, 0.2f, 1f);
        //gl.glDepthMask(false);
        //gl.glDisable(GL.GL_CULL_FACE);
        gl.glFrontFace(GL.GL_CCW);
        gl.glCullFace(GL.GL_BACK);

        //float[] b = this.getPhysicalBoundaries();
        //AABox box = new AABox(new Point(b[0], b[2], b[4]), new Point(b[1], b[3], b[5]));
        //Point[] points = box.getPoints8();

        Point[] points = this.getRep().getPhysicalRep().getPoints8();

        gl.glBegin(GL.GL_QUADS);
        gl.glVertex3f(points[0].x, points[0].y, points[0].z); // front face
        gl.glVertex3f(points[2].x, points[2].y, points[2].z);
        gl.glVertex3f(points[3].x, points[3].y, points[3].z);
        gl.glVertex3f(points[1].x, points[1].y, points[1].z);

        gl.glVertex3f(points[1].x, points[1].y, points[1].z); // top face
        gl.glVertex3f(points[3].x, points[3].y, points[3].z);
        gl.glVertex3f(points[5].x, points[5].y, points[5].z);
        gl.glVertex3f(points[7].x, points[7].y, points[7].z);

        gl.glVertex3f(points[6].x, points[6].y, points[6].z); // back face
        gl.glVertex3f(points[7].x, points[7].y, points[7].z);
        gl.glVertex3f(points[5].x, points[5].y, points[5].z);
        gl.glVertex3f(points[4].x, points[4].y, points[4].z);

        gl.glVertex3f(points[2].x, points[2].y, points[2].z); // right face
        gl.glVertex3f(points[4].x, points[4].y, points[4].z);
        gl.glVertex3f(points[5].x, points[5].y, points[5].z);
        gl.glVertex3f(points[3].x, points[3].y, points[3].z);

        gl.glVertex3f(points[0].x, points[0].y, points[0].z); // left face
        gl.glVertex3f(points[1].x, points[1].y, points[1].z);
        gl.glVertex3f(points[7].x, points[7].y, points[7].z);
        gl.glVertex3f(points[6].x, points[6].y, points[6].z);

        gl.glVertex3f(points[0].x, points[0].y, points[0].z); // bottom face
        gl.glVertex3f(points[6].x, points[6].y, points[6].z);
        gl.glVertex3f(points[4].x, points[4].y, points[4].z);
        gl.glVertex3f(points[2].x, points[2].y, points[2].z);
        gl.glEnd();

        gl.glPopAttrib();
    }

    protected void drawGraphicalBoundingSphere(GL gl, GLU glu) {
        gl.glPushAttrib(GL.GL_CURRENT_BIT | GL.GL_ENABLE_BIT);
        gl.glPushMatrix();
        gl.glDisable(GL.GL_TEXTURE_2D);
        gl.glEnable(GL.GL_BLEND);
        gl.glColor4f(1, 0.4f, 0.6f, 0.4f);
        glu.gluSphere(quadric, this.mesh.getMeshData().radius, 10, 10);
        gl.glPopMatrix();
        gl.glPopAttrib();
    }

    /**
     * Gives the triangles list of this model's actual keyframe state. Each invocation is costly !
     * @return
     */
    public Triangle[] getTriangles() {
        Triangle[] triangles = new Triangle[mesh.topology.length / 3];
        Point p1, p2, p3;
        Vector n1, n2, n3;
        Vector n;
        int topoind = 0;
        for (int i = 0; i < triangles.length; i++) {
            p1 = new Point(mesh.getMeshData().vertices[mesh.topology[topoind] * 3], mesh.getMeshData().vertices[mesh.getTopology()[topoind] * 3 + 1], mesh.getMeshData().vertices[mesh.getTopology()[topoind] * 3 + 2]);
            //n1 = new Vector(mesh.getMeshData().normals[mesh.topology[topoind]], mesh.getMeshData().normals[mesh.getTopology()[topoind]+1], mesh.getMeshData().normals[mesh.getTopology()[topoind]+2]);
            topoind++;
            p2 = new Point(mesh.getMeshData().vertices[mesh.topology[topoind] * 3], mesh.getMeshData().vertices[mesh.getTopology()[topoind] * 3 + 1], mesh.getMeshData().vertices[mesh.getTopology()[topoind] * 3 + 2]);
            //n2 = new Vector(mesh.getMeshData().normals[mesh.topology[topoind]], mesh.getMeshData().normals[mesh.getTopology()[topoind]+1], mesh.getMeshData().normals[mesh.getTopology()[topoind]+2]);
            topoind++;
            p3 = new Point(mesh.getMeshData().vertices[mesh.topology[topoind] * 3], mesh.getMeshData().vertices[mesh.getTopology()[topoind] * 3 + 1], mesh.getMeshData().vertices[mesh.getTopology()[topoind] * 3 + 2]);
            //n3 = new Vector(mesh.getMeshData().normals[mesh.topology[topoind]], mesh.getMeshData().normals[mesh.getTopology()[topoind]+1], mesh.getMeshData().normals[mesh.getTopology()[topoind]+2]);
            triangles[i] = new Triangle(p1, p2, p3);

            // set the personalized normal :
            //n = Vector.add(n1,n2); // new Vector
            //n.add(n3);	// modify receiving object
            //n.normalize();
            //triangles[i].setNormal(n);

            topoind++;
        }
        return triangles;
    }

    public Point getCenter() {
        return this.mesh.getMeshData().sphereCenter;
    }

    /**
     * Gives the bounds of the object's mesh. Respectively : x then y then z and respectively minimum
     * then maximum. For a 2D rep, only the first four values are useful.
     * @return Xmin, Xmax, Ymin, Ymax, Zmin, Zmax
     */
    public float[] getGraphicalBoundaries() {
        return this.mesh.getGraphicalBoundaries();
    }

    public void setRep(Rep rep) {
        this.rep = rep;
    }

    public Rep getRep() {
        return this.rep;
    }

    @Override
    public String toString() {
        return "GraphicalRep";
    }

    public void playAnimationOnce(String animName) {
        if (mesh instanceof AnimatedMesh) {
            ((AnimatedMesh) mesh).playAnimationOnce(animName);
        }
    }

    public void playAnimationInLoop(String animName) {
        if (mesh instanceof AnimatedMesh) {
            ((AnimatedMesh) mesh).playAnimationInLoop(animName);
        }
    }

    public void setAnimationFPS(int fps) {
        if (mesh instanceof AnimatedMesh) {
            ((AnimatedMesh) mesh).setAnimationFPS(fps);
        }
    }

    public String getCurrentAnimation() {
        if (mesh instanceof AnimatedMesh) {
            return (((AnimatedMesh) mesh).getCurrentAnimation());
        } else {
            return null;
        }
    }

    public void setShowAABox(boolean yn) {
        this.showAABox = yn;
    }

    public boolean getShowAABox() {
        return this.showAABox;
    }

    public void setIsWireframe(boolean yn) {
        this.isWireframe = yn;
    }

    public boolean getIsWireframe() {
        return this.isWireframe;
    }

    public void setHighlighted(boolean yn) {
        this.isHighlighted = yn;
    }

    public boolean getIsHighlighted() {
        return this.isHighlighted;
    }

    /**
     * Apply a permanant rotation to the model (an OpenGL rotatef in the order: X then Y then Z).
     * @param xrot how much around X axis
     * @param yrot how much around Y axis
     * @param zrot how much around Z axis
     */
    public void rotate(float xrot, float yrot, float zrot) {
        this.permRotateX = xrot;
        this.permRotateY = yrot;
        this.permRotateZ = zrot;
    }

    public GraphicalRep collectorClone() {
        GraphicalRep ret = new GraphicalRep(this.mesh.collectorClone(), this.textures);
        ret.rotate(permRotateX, permRotateY, permRotateZ);
        ret.cast_reflection = this.cast_reflection;
        ret.cast_shadow = this.cast_shadow;
        ret.isWireframe = this.isWireframe;
        ret.showAABox = this.showAABox;
        ret.prem = this.prem;
        return ret;
    //TODO: recup les VBO's aussi
    }

    public boolean isCharacterRep() {
        return isCharacterRep;
    }

    public void setCharacterRep(boolean isCharacterRep) {
        this.isCharacterRep = isCharacterRep;
    }
}
