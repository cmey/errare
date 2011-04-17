package main;

import java.awt.Point;

import javax.swing.JFrame;

import graphicsEngine.GraphicalRep;
import graphicsEngine.GraphicsEngine;
import physicsEngine.AstarMovement;
import physicsEngine.PhysicalRep;
import physicsEngine.PhysicsEngine;
import physicsEngine.Character;

public class Main {

	private PhysicsEngine physicsEngine;
	private GraphicsEngine graphicsEngine;
	
	public Main() {
		physicsEngine = new PhysicsEngine(this);
		graphicsEngine = new GraphicsEngine(this);
		
		Rep mainChar = new Rep(this, "models/terminator.md2", PhysicalRep.type.MAINCHARACTER, 100, 100, 10, 10);
		
		physicsEngine.setMainChar(mainChar);
		graphicsEngine.setMainChar(mainChar);
		
		for(int j=0; j<2; j++)
			for(int i=0; i<10; i++) {
				new Rep(this, "models/terminator.md2", PhysicalRep.type.CHARACTER, 10*i, 10*j, 10, 10);
			}
	
		JFrame jf=new JFrame();
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setContentPane(graphicsEngine);
		jf.setVisible(true);
		jf.pack();
		
		
	}
	
	public PhysicsEngine getPhysicsEngine() {
		return physicsEngine;
	}
	
	public GraphicsEngine getGraphicsEngine() {
		return graphicsEngine;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Main();
		
		
	}

}
