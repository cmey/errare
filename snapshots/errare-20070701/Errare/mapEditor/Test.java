package mapEditor;

import graphicsEngine.GraphicalRep;
import java.awt.Dimension;
import java.util.LinkedList;
import javax.swing.JFrame;

public class Test implements Runnable{
	
	static EditorGraphicsEngine ge;
	public static long period = (long)((1.0/60.0)*1E9);
	
	public static void main (String[] args) {
		
		JFrame window = new JFrame("Graphics Engine");
		ge = new EditorGraphicsEngine(window);
		window.setUndecorated(true);
		window.setSize(new Dimension(800,600));
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.requestFocusInWindow();
		
		LinkedList<GraphicalRep> glist = new LinkedList<GraphicalRep>();
				
		
		
		//ge.debugsetDisplayList(glist);
		
		window.add(ge.getGLCanvas());
		window.setVisible(true);
		Thread graphicsEngineMainLoop = new Thread(new Test());
		graphicsEngineMainLoop.start();
	}
	
	
	
	public void run(){
		long begin, end;
		int sleeptime=0;
		System.out.println("Engine running !");
		while(true){
			begin = System.nanoTime();
			ge.run();
			end = System.nanoTime();
			sleeptime = (int) (period - (end-begin));
			//System.out.println("must sleep:"+sleeptime);
			ge.info_sleeptime(sleeptime);
			if(sleeptime < 0) sleeptime = 0;
			if(sleeptime>0) {
				long sleepstart = System.nanoTime();
				try {
					
					Thread.sleep(sleeptime/1000000, sleeptime%1000000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				sleeptime = (int) (sleeptime - (System.nanoTime()-sleepstart));
				//System.out.println("missed: "+sleeptime);
			}
		}
	}
}