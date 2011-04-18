package main;
/*
import genericEngine.Engine;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

import mapEditor.EditorEngine;
import mapEditor.EditorGraphicsEngine;
import mapEditor.Principale;
import userInputEngine.UserInputController;
import xmlEngine.XmlEngine;

public class EditorMain implements Engine{

	private JFrame jf;
	private UserInputController userInputController;
	private EditorGraphicsEngine graphicsEngine;
	private EditorEngine editor;
	private XmlEngine xmlEngine;

	public EditorMain() throws Exception {
		jf = new Principale("- Errare Map Editor -");
		
		xmlEngine = new XmlEngine();
		userInputController = new UserInputController("editor", xmlEngine);
		userInputController.register(this, "exit");
		graphicsEngine = new EditorGraphicsEngine(jf, this);
		editor = new EditorEngine(this);
		
		setWindowedMode(jf);
		jf.getContentPane().add(graphicsEngine.getGLCanvas());
		jf.validate();
		jf.setVisible(true);

		run();
	}

	private void setWindowedMode(JFrame jf) {
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension dimScreen = tk.getScreenSize();
		jf.setBounds(0,0,dimScreen.width,dimScreen.height);
	}


	public boolean invokeKeyEvent(String action) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean invokeMouseEvent(String action, int x, int y) {
		// TODO Auto-generated method stub
		return false;
	}

	public void quit() {
		// TODO Auto-generated method stub

	}

	public static final long PERIOD = (long)((1.0/60.0)*1E9);


	public void run(){	

		long begin, end;
		long tmp=0;
		long sleeptime=0;
		long runtime=0;

		begin = System.nanoTime();

		while(true) {

			if(sleeptime>0) {
				try {
					Thread.sleep((long)(sleeptime/1000000), (int)(sleeptime%1000000));
				} catch (InterruptedException ex) {}

				graphicsEngine.run();
				try{
					editor.run();
				}catch(Exception e){
					e.printStackTrace();
				}
			}



			tmp = begin;
			begin = System.nanoTime();
			end = System.nanoTime();
			runtime = end - tmp;
			sleeptime += PERIOD - runtime;
		}

	}
	
	public EditorGraphicsEngine getEditorGraphicsEngine(){
		return this.graphicsEngine;
	}
	
	public EditorEngine getEditor(){
		return this.editor;
	}
	
	public JFrame getJFrame(){
		return this.jf;
	}
	
	public UserInputController getUserInputController(){
		return this.userInputController;
	}
}
*/