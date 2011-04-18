package userInputEngine;

import genericEngine.Engine;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.xml.parsers.ParserConfigurationException;

import xmlEngine.XmlEngine;



public class TestEngine implements Engine{
	
	public TestEngine() throws ParserConfigurationException {
		XmlEngine database = new XmlEngine();
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
		controller.register(this, "zoom_in");
		controller.register(this, "zoom_out");
		
		
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

	public void quit() {
		// TODO Auto-generated method stub
		
	}

	public void run() {
		// TODO Auto-generated method stub
		
	}

}
