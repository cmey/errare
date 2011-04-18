package guiEngineLogin;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;


public class LoginFrame extends JFrame{

	private JTextField username;
	private JPasswordField password;
	private JPanel panel;
	private GuiEngineLogin gel;
	
	public LoginFrame(GuiEngineLogin gel){
		this.gel = gel;
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle(" ..:: Errare Login : v.2 ::..");
		
		installElements();
		
		this.setVisible(true);
		this.pack();
	}
	
	
	private void installElements(){
		username = new JTextField();
		username.setColumns(20);
		username.setVisible(true);
		password = new JPasswordField();
		password.setColumns(20);
		password.setVisible(true);
		
		panel = new JPanel();
		panel.add(username);
		panel.add(password);
		this.setContentPane(panel);
		
		username.addActionListener(gel);
		password.addActionListener(gel);
	}
	
	public String getUsernameText(){
		return username.getText();
	}
	
	public String getPasswordText(){
		return password.getText();
	}
	
}
