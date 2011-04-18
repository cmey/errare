package main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;


public class LoginScreen implements ActionListener{

	JFrame jframe;
	JPanel jpanel;
	JTextField username;
	JPasswordField password;
	JButton button;
	
	public LoginScreen(){	
		
		jframe = new JFrame();
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.setTitle("..:: Errare Login Screen ::..");
		
		installElements();
		
		jframe.pack();
		jframe.setVisible(true);
		
	}
	
	private void installElements(){
		
		username = new JTextField();
		username.setColumns(20);
		username.setVisible(true);
		
		password = new JPasswordField();
		password.setColumns(20);
		password.setVisible(true);
		
		button = new JButton("Ok");
		button.setVisible(true);
		
		jpanel = new JPanel();
		jpanel.add(username);
		jpanel.add(password);
		jpanel.add(button);
		jframe.setContentPane(jpanel);
		button.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e){
            ClientMain.login = this.username.getText();
            ClientMain.password = this.password.getText();
            this.jframe.dispose();
            try {
                ClientMain.start();
            } catch (Exception ex) {
                Logger.getLogger(LoginScreen.class.getName()).log(Level.SEVERE, null, ex);
            }
	}

}
