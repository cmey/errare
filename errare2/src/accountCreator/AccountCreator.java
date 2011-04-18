package accountCreator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import logger.Logger;

import persistenceEngine.Account;
import persistenceEngine.AccountManager;
import persistenceEngine.PersistenceClient;


public class AccountCreator implements ActionListener{

	AccountManager manager;
	JFrame jframe;
	JPanel jpanel;
	JTextField username;
	JPasswordField password;
	JButton button;
	
	public AccountCreator(){
		jframe = new JFrame();
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.setTitle("..:: Errare Account Creator : v.2 ::..");
		
		installElements();
		
		jframe.pack();
		jframe.setVisible(true);
		
		PersistenceClient dbClient;
		try {
			dbClient = new PersistenceClient();
			manager = new AccountManager(dbClient);
		} catch (IOException e) {
			String errormsg = "PersistenceClient could not connected to PersistenceServer !";
			Logger.printFATAL(errormsg);
			displayFailure(errormsg);
		}
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

	public void actionPerformed(ActionEvent e) {
		boolean status = manager.addAccount(new Account(username.getText(), password.getText()));
		if(status)
			displaySuccess();
		else
			displayFailure("Account already exists");
	}
	
	private void displaySuccess(){
		JFrame popup = new JFrame("Success");
		popup.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel popo = new JPanel();
		JLabel text = new JLabel("Your account has been registered.");
		JLabel text2 = new JLabel("Now you can log in and play!");
		popo.add(text);
		popo.add(text2);
		popup.add(popo);
		popup.pack();
		popup.setVisible(true);
	}
	
	private void displayFailure(String status){
		JFrame popup = new JFrame("Failure");
		popup.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel popo = new JPanel();
		JLabel text = new JLabel(status);
		popo.add(text);
		popup.add(popo);
		popup.pack();
		popup.setVisible(true);
	}
	
	
	public static void main(String[] args){
		new AccountCreator();
	}
}
