package guiEngine;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.Timer;

/*
 * Created on 14 juin 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author Ak
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

@SuppressWarnings("serial")

public class Home extends JFrame {
	
	private Timer timer;
	JProgressBar loading;

	public Home() {
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension dimScreen = tk.getScreenSize();
this.setTitle("Title");
this.setUndecorated(true);
//this.setBounds((dimScreen.width-(dimScreen.width/2))/2,(dimScreen.height-(dimScreen.height/2))/2,(dimScreen.width/2)+100,(dimScreen.height/2)+100);
this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
/*loading = new JProgressBar(JProgressBar.HORIZONTAL,0,100);
loading.setPreferredSize(new Dimension (600,25));
loading.setValue(0);

timer = new Timer(18, action());
timer.start();*/

//this.getContentPane().add(loading);
this.setContentPane(new Menu(dimScreen.width,dimScreen.height,this));
this.setIconImage((new ImageIcon("????.png")). getImage());
this.pack();

this.setVisible(true);

	}
	
	public Action action() {
		
		return new AbstractAction("h") {
			public void actionPerformed(ActionEvent e) {
				if (loading.getValue()<loading.getMaximum()) {
					loading.setValue(loading.getValue()+1);
				}else {
					if (timer!=null) {
						timer.stop();
						timer=null;
					}
				}
			}
	};
}
	
	public static void main (String args[]) {
		Home home = new Home();
	}
}
