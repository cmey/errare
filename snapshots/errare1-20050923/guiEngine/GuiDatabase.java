package guiEngine;

import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.*;
import javax.swing.table.*;

import databaseEngine.DatabaseEngine;

/**
 * Class GuiSgbd
 * @author Ak
 * @version 0.1
 */

@SuppressWarnings("serial")

public class GuiDatabase extends JFrame {
	
	
	
	/**
	 * GuiSgbd Constructor
	 * Create a JFrame whitch the user can organise the SGBD
	 */
	
	public GuiDatabase() {
		
		
		
		// The JFrame options
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension dimScreen = tk.getScreenSize();
		this.setTitle("Gestion de la Base de Donnee d'Errare");
		this.setBounds((dimScreen.width-256)-10,0+10,256,4);
		this.setSize(800,400);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		this.setLocation((dimScreen.width - this.getSize().width)/2,(dimScreen.height - this.getSize().height)/2); 
		this.setLayout(new BorderLayout());
		
		final JFrame frame = this;
		
		//JMenu
		JMenuBar menu = new JMenuBar();
		
		//File
		JMenu file = new JMenu("File");
		menu.add(file);
		JMenuItem exit = new JMenuItem("Exit");
		file.add(exit);
		exit.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
			
		});
		
		//Help
		JMenu help = new JMenu("Help");
		menu.add(help);
		JMenuItem about = new JMenuItem("About");
		help.add(about);
		about.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(frame,"By everybody !",
						"About", JOptionPane.INFORMATION_MESSAGE);
			}
			
		});
		
		
		this.getContentPane().add(menu, BorderLayout.NORTH);
		
		
		//JTabbedPane that has one tab for each table of the database
		JTabbedPane tabs = new JTabbedPane();
		
		
		
		
		DatabaseEngine db = new DatabaseEngine();
		ResultSet tableNames;
		try {
			tableNames = db.sendRequest("show tables");
			
			while(tableNames.next()) {
				String name = tableNames.getString(1);
				
				tabs.add(name, new GuiDatatable(name, db));
			}
			
		} catch (SQLException e1) {
			System.out.println("Error when accessing the database : "+e1.getMessage());;
		}
		
		
		this.getContentPane().add(tabs, BorderLayout.CENTER);
		
		
		
		JLabel message = new JLabel("Cliquez pour commencer ;-)");
		this.getContentPane().add(message, BorderLayout.SOUTH);
		
		this.setVisible(true);
		
		
		
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		GuiDatabase b = new GuiDatabase();
		
	}
	
	
	
	
	
}
