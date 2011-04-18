package geom;

public class Solver {
	
	public static void solve(float[][] system) {
		float ratio;
		int x = system.length;
		int y = system[0].length;
		
		for(int i=0; i<x; i++) {
			if(system[i][i]!=1) {
				for(int j=0; j<i; j++) {
					system[i][j] = system[i][j]/system[i][i];
				}
				for(int j=i+1; j<y; j++) {
					system[i][j] = system[i][j]/system[i][i];
				}
				system[i][i]=1;
			}
			
			for(int j=0; j<i; j++) {
				ratio = system[j][i]/system[i][i];
				for(int k=i+1; k<y; k++) {
					system[j][k] = system[j][k] - ratio*system[i][k]; 
				}
			}
			for(int j=i+1; j<x; j++) {
				ratio = system[j][i]/system[i][i];
				for(int k=i+1; k<y; k++) {
					system[j][k] = system[j][k] - ratio*system[i][k]; 
				}
			}
		}
	}
	
	public static void print(float[][] sys) {
		for(int i=0; i<sys.length; i++) {
			for(int j=0; j<sys[0].length; j++) {
				System.out.print(sys[i][j]+" ");
			}
			System.out.println();
		}
		System.out.println("----------------------");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		float[][] sys = new float[][] {
				{1, 1, 2, 1, 4, 1, 6, 1, 2},
				{2, 5, 2, 2, 2, 2, 5, 3, 1},
				{1, 3, 3, 3, 3, 2, 4, 7, 2},
				{0, 1, 2, 4, 0, 4, 3, 1, 2},
				{5, 5, 5, 8, 5, 5, 2, 5, 2},
				{5, 5, 5, 8, 5, 2, 2, 5, 4},
				{5, 3, 5, 8, 1, 5, 2, 5, 3},
				{3, 2, 6, 2, 5, 6, 1, 7, 2}
		};
		
		print(sys);
		
		long time = System.nanoTime();
		Solver.solve(sys);
		System.out.println("took: "+(System.nanoTime()-time)/1E6+" ms");
		
		print(sys);
	}

}





