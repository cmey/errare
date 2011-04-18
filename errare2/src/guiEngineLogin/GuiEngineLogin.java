package guiEngineLogin;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GuiEngineLogin implements ActionListener{

	LoginFrame frame;
	
	public GuiEngineLogin(){
		frame = new LoginFrame(this);
	}

	
	public void actionPerformed(ActionEvent e) {
		
	}
	
}
