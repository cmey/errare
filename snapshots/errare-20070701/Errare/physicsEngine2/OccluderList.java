package physicsEngine2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.PriorityQueue;

import geom.Cube;
import geom.Triangle;

public class OccluderList {
	
	private PriorityQueue<Element> queue;
	
	public OccluderList() {
		queue = new PriorityQueue<Element>();
	}
	
	public PriorityQueue<Element> getList() {
		return queue;
	}
	
	public void add(Cube c, geom.Point cameraPos) {
		ArrayList<Triangle> visibleTris = c.getVisibleTriangles(cameraPos);
		Iterator<Triangle> candidates = visibleTris.iterator();
		while(candidates.hasNext()) {
			Triangle newOcculer = candidates.next();
			boolean occuled=false;
			Iterator<Element> elems = queue.iterator();
			while(elems.hasNext() && !occuled) {
				Element e = elems.next();
					if(newOcculer.points[0].isOccludedBy(e.getTriangles(), cameraPos) 
							&& newOcculer.points[1].isOccludedBy(e.getTriangles(), cameraPos) 
							&& newOcculer.points[2].isOccludedBy(e.getTriangles(), cameraPos)) {
						candidates.remove();
						occuled=true;
						break;
					}
				
			}
		}
		
		queue.offer(new Element(visibleTris, 0));
	}
	
	public boolean isEmpty(){
		return queue.isEmpty();
	}
	
	public void incrementPriority(Element e) {
		queue.remove(e);
		e.priority++;
		queue.offer(e);
	}
	
	public class Element implements Comparable<Element> {
		private ArrayList<Triangle> triangles;
		private int priority;
		
		private Element(ArrayList<Triangle> t, int p) {
			triangles = t;
			priority = p;
		}
		
		public ArrayList<Triangle> getTriangles() {
			return triangles;
		}
		
		public int compareTo(Element e) {
			return e.priority-priority;
		}
		
		public boolean equals(Object o) {
			if(o instanceof Element) {
				return ((Element)o).priority==this.priority;
			}
			return false;
		}
		
	}
}
