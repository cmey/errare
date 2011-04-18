/*Errare is a free and crossplatform MMORPG project.
 Copyright (C) 2009  Christophe MEYER: matrix-based rotation, physics bug fix, comments
 Copyright (C) 2006  Martin DELEMOTTE
This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.
This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.*/
package physicsEngine;

import java.io.Serializable;
import java.util.ArrayList;

import genericReps.Rep;
import geom.AABox;
import geom.Matrix;
import geom.Point;
import geom.Triangle;
import geom.Vector;

public class PhysicalRep extends AABox implements Serializable {

    private static final Vector UPWARDS = new Vector(0, 1, 0);
    private static final float GRAVITY = 0.4f;
    public static final float JUMPPOWER = 2.0f;
    transient private Rep rep;
    private Octree tree;
    private Vector move;
    private Vector gravitySpeed;
    private boolean inTheAir;
    private transient PhysicsEngine physicsEngine;
    private float[] matrix;
    private float[] initialMatrix;

    
    /**
     * Creates a physical model, potentialy moving, with it's given BoundingBox.
     * @param leftFrontBottom left-front-bottom corner of the BoundingBox
     * @param rightBackTop right-back-top corner of the BoundingBox
     */
    public PhysicalRep(Point leftFrontBottom, Point rightBackTop) {
        super(leftFrontBottom, rightBackTop);
        move = new Vector(0, 0, 0);
        gravitySpeed = new Vector(0, 0, 0);
        matrix = new float[]{1, 0, 0, 0,
            0, 1, 0, 0,
            0, 0, 1, 0,
            0, 0, 0, 1
        };
    }

    
    /**
     * ? TODO
     * @param f
     */
    public void setInitialMatrix(float[] f) {
        initialMatrix = f;
        setMatrix(matrix);
        setLookingDirection(new Vector(1, 0, 0));
    }

    
    public void setPhysicsEngine(PhysicsEngine physicsEngine) {
        this.physicsEngine = physicsEngine;
    }

    
    /**
     * Effectively move (translate) the model, and place it in the correct octree node.
     * @param v a vector telling how much to move and in which direction
     */
    @Override
    public void translate(Vector v) {
        translate(v.x, v.y, v.z);
    }

    
    /**
     * Effectively move (translate) the model, and place it in the correct octree node.
     * @param x how much in x
     * @param y how much in y
     * @param z how much in z
     */
    @Override
    public void translate(float x, float y, float z) {
        super.translate(x, y, z);
        tree.moveContent(this);
        matrix[12] = getCenter().x;
        matrix[13] = getCenter().y;
        matrix[14] = getCenter().z;
        /*Matrix trans = Matrix.identityMatrix(4);
        trans.setElement(3, 0, getCenter().x);
        trans.setElement(3, 1, getCenter().y);
        trans.setElement(3, 2, getCenter().z);
        Matrix m = Matrix.identityMatrix(4);
        m.setArray(matrix);
        m = m.times(trans);
        matrix = m.GetArray();*/
    }

    
    /**
     * ? TODO
     * @param center
     */
    @Override
    public void setCenter(Point center) {
        super.setCenter(center);
        tree.moveContent(this);
    }

    
    /**
     * Gives access to the other representations of this object.
     * @return the generic rep for this object
     */
    public Rep getRep() {
        return rep;
    }

    
    /**
     * Once every representations of this object are correctly constructed,
     * set the generic rep of this object.
     * @param rep
     */
    public void setRep(Rep rep) {
        this.rep = rep;
    }

    
    /**
     * The octree calls this method when the model get's placed in an octree node.
     * Called by Octree.addContent(PhysicalRep pr)
     * @param t
     */
    public void setOctree(Octree t) {
        tree = t;
    }

    
    /**
     * Sets the orientation of the model.
     * @param direction a vector indicating the direction (the length of the vector has no meaning)
     */
    public void setLookingDirection(Vector direction) {
        float yOrientation;
        if (direction.z > 0) {
            yOrientation = -(float) Math.acos(direction.x / Math.sqrt(direction.x * direction.x + direction.z * direction.z));
        } else {
            yOrientation = (float) Math.acos(direction.x / Math.sqrt(direction.x * direction.x + direction.z * direction.z));
        }
        Matrix rot = Matrix.identityMatrix(4);
        rot.setElement(0, 0, Math.cos(yOrientation));
        rot.setElement(0, 2, -Math.sin(yOrientation));
        rot.setElement(2, 0, Math.sin(yOrientation));
        rot.setElement(2, 2, Math.cos(yOrientation));

        Matrix s = Matrix.identityMatrix(4);
        s.setElement(0, 0, initialMatrix[0]);
        s.setElement(1, 1, initialMatrix[5]);
        s.setElement(2, 2, initialMatrix[10]);
        /*s.setElement(3, 0, getCenter().x);
        s.setElement(3, 1, getCenter().y);
        s.setElement(3, 2, getCenter().z);*/
        rot = rot.times(s);
        matrix = rot.GetArray();
        matrix[12] = getCenter().x;
        matrix[13] = getCenter().y;
        matrix[14] = getCenter().z;
    }

    public Octree getOctree() {
        return tree;
    }

    /**
     * Apply all movements (move + gravity) to the model.
     * @return isNoLongerMoving ?
     */
    public boolean applyMovements() {
        applyMoveMovement(move, new ArrayList<Triangle>(), 0);

        gravitySpeed.y -= GRAVITY;
        boolean isStillMoving = applyGravity();
        return isStillMoving;
    }


    private void applyMoveMovement(Vector move, ArrayList<Triangle> alreadyProcessedTriangles, int level) {
        if (move.getMagnitude()>0) {
            rep.getGraphicalRep().playAnimationOnce("run");
        }

        // try move
        this.translate(move);
        
        // test for collision
        ArrayList<Triangle> triList = physicsEngine.getOverlapingTriangles(this);
        if (!triList.isEmpty()) { // there is collision
            // cancel the move try
            this.translate(-move.x, -move.y, -move.z);

            for (Triangle tri : triList) {
                if (!alreadyProcessedTriangles.contains(tri)) {
                    float dotProd = Vector.dotProduct(move, tri.getNormal());
                    if (dotProd < 0) {
                        Vector inter = Vector.mult(tri.getNormal(), -dotProd);
                        // substract the collided component normal from the move
                        move.add(inter);

                        alreadyProcessedTriangles.add(tri);
                        applyMoveMovement(move, alreadyProcessedTriangles, ++level);
                        return;
                    }
                }
            }
        }
    }

    /**
     *
     * @param gravity
     * @return isStillFalling ?
     */
    private boolean applyGravity() {
        boolean groundHit = false;
        // fast fall management
        // 1) whith this we are sure to detect the collision
        int nbSteps = (int) (-gravitySpeed.y/getSphere().rad);
        nbSteps = Math.max(1, nbSteps);
        float verticalStep = -gravitySpeed.y / nbSteps;
        for (int i = 0; i < nbSteps; ++i) {
            // try collision
            this.translate(0, -verticalStep, 0);

            if (!physicsEngine.getOverlapingTriangles(this).isEmpty()) {
                // collision happened
                groundHit = true;
                // return to previous location
                this.translate(0, verticalStep, 0);
                // stop 
                gravitySpeed.y = 0;
                move.x = 0;
                move.y = 0;
                move.z = 0;
                inTheAir = false;
                break; // char hit the ground: no longer moving
            }
        }
        if(!groundHit){
            inTheAir = true;
            return true;
        }

        // 2) approach close to the ground !
        float maxLevitationHeight = 0.01f;
        boolean tooHigh = verticalStep > maxLevitationHeight;
        while(tooHigh){
            // try collision
            this.translate(0, -verticalStep, 0);

            if (!physicsEngine.getOverlapingTriangles(this).isEmpty()) {
                // collision happened
                // return to previous location
                this.translate(0, verticalStep, 0);
                verticalStep /= 2;
                tooHigh = verticalStep > maxLevitationHeight;
            }else{
                // we got down enough, close to the ground
                break;
            }
        }

        inTheAir = false;
        return false;
    }

    public boolean isInTheAir() {
        return inTheAir;
    }

    /**
     * Apply a force vector to a model
     * @param param the vector of force to apply
     */
    public void addMove(Vector param) {
        move.add(param);
    }

    /**
     * Apply a jump to a model.
     * Does a lot of condition testing.
     * @param power
     * @return isNowMoving ?
     */
    public boolean addJump(float power) {
        if(inTheAir)
            return true;
        else {
            rep.getGraphicalRep().playAnimationOnce("jump");
            addMove(new Vector(0, power, 0));
            return true;
        }
    }

    public float[] getMatrix() {
        return matrix;
    }

    public void setMatrix(float[] matrix) {
        this.matrix = matrix;
    }

    //TODO: can be made a lot more precise
    /**
     * Returns the distance between the PhysicalRep and another.
     * The returned distance will be an <b>aproximation</b> of the distance between the 
     * two closest points of each PhysicalRep.
     * @param other the other PhysicalRep
     * @return distance to the other PhysicalRep
     */
    public float distance(PhysicalRep other) {
        return other.getCenter().distance(this.getCenter());
    }

    //TODO: can be made a lot more precise
    /**
     * Returns the square distance between the PhysicalRep and another.
     * The returned square distance will be an <b>aproximation</b> of the square distance between the 
     * two closest points of each PhysicalRep.<br><br>
     * This method is a bit more efficient than the distance(PhysicalRep rep) because there is no need to calculate a square root.
     * @param other the other PhysicalRep
     * @return distance to the other PhysicalRep
     */
    public float sqrtDistance(PhysicalRep other) {
        return other.getCenter().distanceSquare(this.getCenter());
    }
}
