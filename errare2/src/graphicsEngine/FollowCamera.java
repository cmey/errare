package graphicsEngine;

import geom.Point;

public class FollowCamera extends Camera {

    private float floorDist;
    private float baseFloorDist;
    private float floorAngle;
    private float verticalAngle;

    public FollowCamera(Point followed) {
        this.lookingAt = followed;
        floorAngle = 180;
        verticalAngle = 1;
        baseFloorDist = 20;
    }
    
    public FollowCamera(Point followed, float distanceAway) {
        this(followed);
        baseFloorDist = distanceAway;
    }

    @Override
    public void rotateY(float angle) {
        floorAngle -= angle;

        location.z = lookingAt.z + (float) Math.sin(floorAngle) * floorDist;
        location.x = lookingAt.x + (float) Math.cos(floorAngle) * floorDist;
    }

    public void rotateXZ(float angle) {
        verticalAngle += angle;
        if (verticalAngle >= Math.PI / 2) {
            verticalAngle = (float) Math.PI / 2 - 0.0001f;
        } else if (verticalAngle <= 0) {
            verticalAngle = 0.0001f;
        }

        location.y = lookingAt.y + (float) Math.sin(verticalAngle) * baseFloorDist;

        floorDist = (float) Math.cos(verticalAngle) * baseFloorDist;

        rotateY(0);
    }

    public void zoomin() {
        baseFloorDist /= 1.1f;
        rotateXZ(0);
    }

    public void zoomout() {
        baseFloorDist *= 1.1f;
        rotateXZ(0);
    }

    public void update() {
        rotateXZ(0);
    }
}
