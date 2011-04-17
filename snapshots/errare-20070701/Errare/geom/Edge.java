package geom;

import java.util.ArrayList;

public class Edge {
	
	public Point p1;
	public Point p2;
	
	public Edge(Point p1, Point p2){
		this.p1 = p1;
		this.p2 = p2;
	}
	
	/**
	 * It supposes all the edges from the list come from one and only one object !
	 * ie. if 2 points have the same coords, they are the same underlying object.
	 * ie. p1==p2 <=> p1.xyz==p2.xyz
	 */
	public static Point[] getPointsFromEdgesList(ArrayList<Edge> edges){
		ArrayList<Point> points = new ArrayList<Point>();
		
		for(Edge ed : edges){
			if(!points.contains(ed.p1))
				points.add(ed.p1);
			if(!points.contains(ed.p2))
				points.add(ed.p2);
		}
		
		return (Point[]) points.toArray();
	}
}
