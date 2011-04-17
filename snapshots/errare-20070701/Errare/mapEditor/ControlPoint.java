package mapEditor;

import java.awt.Color;

import geom.Point;
import physicsEngine2.TreeCube;


public class ControlPoint extends TreeCube {

	private Point point;

	public ControlPoint(Point leftFrontBottom, Point rightBackTop, Point point) {
		super(leftFrontBottom, rightBackTop);
		this.point = point;
		
		this.getSphere().setVisible(true);
		this.getSphere().setColor(Color.BLUE);
		
		
	}
	
	public Point getControlPoint(){
		return point;
	}

	

}
