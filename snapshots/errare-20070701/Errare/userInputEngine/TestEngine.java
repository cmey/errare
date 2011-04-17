package userInputEngine;

import java.awt.Dimension;
import java.awt.MouseInfo;

import javax.swing.JFrame;
import javax.xml.parsers.ParserConfigurationException;

import main2.Engine;

import databaseEngine.DatabaseEngine;

public class TestEngine implements Engine{
	
	public TestEngine() throws ParserConfigurationException {
		DatabaseEngine database = new DatabaseEngine();
		UserInputController controller = new UserInputController("test", database);
		
		JFrame frame = new JFrame();
		frame.addKeyListener(controller);
		frame.getContentPane().addMouseListener(controller);
		frame.getContentPane().addMouseMotionListener(controller);
		frame.getContentPane().addMouseWheelListener(controller);
		frame.getContentPane().addKeyListener(controller);
		
		frame.getContentPane().setPreferredSize(new Dimension(200, 100));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
		
		controller.register(this, "up");
		controller.register(this, "down");
		controller.register(this, "right");
		controller.register(this, "left");
		controller.register(this, "jump");
		
		
	}

	public boolean invokeMouseEvent(String action, int x, int y) {
		System.out.println(action+" at("+x+":"+y+")");
		return true;
	}
	
	public boolean invokeKeyEvent(String action) {
		System.out.println(action);
		return true;
	}
	
	public static void main(String[] args) throws ParserConfigurationException {
		TestEngine test = new TestEngine(); 
	}

}
