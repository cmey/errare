package geom;

import java.util.Random;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Random rand = new Random();
		
		int n = 10;

		AABox[] box = new AABox[n];
		Triangle[] tri = new Triangle[n];
		
		for(int i=0; i<n; i++) {
			box[i] = new AABox(new Point(rand.nextFloat()*1000, rand.nextFloat()*1000, rand.nextFloat()*1000), new Point(rand.nextFloat()*1000, rand.nextFloat()*1000, rand.nextFloat()*1000));
			tri[i] = new Triangle(new Point(rand.nextFloat()*1000, rand.nextFloat()*1000, rand.nextFloat()*1000), new Point(rand.nextFloat()*1000, rand.nextFloat()*1000, rand.nextFloat()*1000), new Point(rand.nextFloat()*1000, rand.nextFloat()*1000, rand.nextFloat()*1000));	
		}
		
		long time = System.nanoTime();
		for(int i=0; i<n; i++) {
			tri[i].overlap(box[i]);
		}
		
		System.out.println((System.nanoTime()-time)/10E6);
	}

}
