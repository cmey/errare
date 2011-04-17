package graphicsEngine;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.util.ArrayList;

import javax.swing.JFrame;

import physicsEngine.PhysicalRep;

import main.Main;
import main.Rep;



public class Test {
	
	public static void main(String[] args) {
		GraphicsEnvironment ge=GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();
	    JFrame jf=new JFrame();
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//gd.setFullScreenWindow(jf);
		//ArrayList<Rep> li = new ArrayList<Rep>();	//on crée la liste à passer au moteur
		//GraphicalRep grep = new GraphicalRep("models/terminator.md2");	//on charge la Rep
		//li.add(grep);									//on ajoute la Rep à la liste à passer
		//GraphicsEngine app =new GraphicsEngine();	//on démarre le moteur
		//app.setDisplayList(li);						//on dit au moteur quoi afficher en passant la liste
		jf.setVisible(true);
		jf.pack();
		Main m = new Main();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Rep r = new Rep(m,"models/terminator.md2",PhysicalRep.type.CHARACTER,  0, 0, 10, 10);
	}
}