/*Errare is a free and crossplatform MMORPG project.
Copyright (C) 2006  Arnaud KNOBLOCH

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.*/

package guiEngine;

import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.Timer;

/**
 * Class GuiLoad
 * For V2 of the GUI
 */
@SuppressWarnings("serial")

public class Loading extends JFrame {

	private Timer timer;
	JProgressBar loading;

	public Loading() {


		this.setTitle("Title");
		this.setUndecorated(true);
//		this.setBounds((dimScreen.width-(dimScreen.width/2))/2,(dimScreen.height-(dimScreen.height/2))/2,(dimScreen.width/2)+100,(dimScreen.height/2)+100);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		loading = new JProgressBar(JProgressBar.HORIZONTAL,0,100);
		loading.setPreferredSize(new Dimension (600,25));
		loading.setValue(0);

		timer = new Timer(18, action());
		timer.start();

		this.getContentPane().add(loading);

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
		Loading home = new Loading();
	}
}
