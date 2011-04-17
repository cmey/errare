import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.StringTokenizer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.Border;

import sun.awt.image.PixelConverter.Bgrx;

/**
 * Interface 2D pour le simulateur
 * @author Yannick
 *
 */
@SuppressWarnings("serial")
public class InterfaceGraphics extends JFrame{
	private LinkedList<Area> listOfArea;
	private LinkedList<Player>playersToAdd;
	private int lastnbConnectedPlayer;
	private Simulateur simulateur;
	
	private JButton playPause;
	private JButton nextStep;
	private JButton addChar;
	private JButton addPlayer;
	private JButton addArea;
	private JButton increase;
	private JButton reduce;
	private JButton deletePlayer;
	
	private JCheckBox showActiveArea;
	private JCheckBox showMobs;
	private JCheckBox showAllPlayers;
	
	private JTextArea currentPlayerField;
	private JTextArea carryMonsterField;
	private JTextArea actionCurrentPlayerField;
	private JTextArea currentPlayerFieldXYZPosition;
	private JTextArea currentPlayerFieldYPosition;
	private JTextArea currentPlayerFieldZPosition;
	
	private JComboBox currentPlayer;
	private JComboBox carryMonster;
	private JComboBox actionCurrentPlayer;
	
	private JScrollPane worldmap;
	
	private JMenuBar menuBar;
	
	private Console console;
	private AddPlayerFrame addPlayerFrame;

	private JCheckBoxMenuItem displayConsoleMenuItem;
	private JCheckBox showAllAreas;
	private JCheckBox showCurrentPlayer;	
	
	private DisplayMap2D displayMap2D;
	private JCheckBox showActiveAreaXY;
	private JTextArea currentPlayerFieldPosition;
	private JRadioButton showNameRadioButton;
	private JRadioButton showCoordRadioButton;
	
	private JPanel movingPanel;
	
	public InterfaceGraphics(){
		super("Simulateur/Editeur");
		playersToAdd=new LinkedList<Player>();
		addPlayerFrame=new AddPlayerFrame();
		movingPanel=new Joystick();
		displayMap2D=new DisplayMap2D();
		displayMap2D.addMouseListener(new MouseListener(){
			public void mouseClicked(MouseEvent e) {
				if(addPlayerFrame.isTakeWithMouse()){
					addPlayerFrame.setX(/*Math.abs(*/(int) ((e.getPoint().x-displayMap2D.getXPos())/displayMap2D.getFactor())/*)*/);
					addPlayerFrame.setY(/*Math.abs(*/(int) ((e.getPoint().y-displayMap2D.getYPos())/displayMap2D.getFactor())/*)*/);
					addPlayerFrame.setZ(Math.abs(0));
				}
				if(addPlayerFrame.takeWithMouseBoolean){
					addPlayerFrame.addPlayer();
				}
			}public void mouseEntered(MouseEvent e) {
			
			}
			;public void mousePressed(MouseEvent e) {
			
				
			}public void mouseExited(MouseEvent e) {
			
			}
			;public void mouseReleased(MouseEvent e) {
			
			}
		});
		menuBar=new JMenuBar();
		this.setJMenuBar(menuBar);
		JMenu menu=new JMenu("Fichier");
		JMenu displayMenu=new JMenu("Affichage");
		menuBar.add(menu);
		menuBar.add(displayMenu);
		JMenuItem menuItem=new JMenuItem("Quitter");
		displayConsoleMenuItem=new JCheckBoxMenuItem("Afficher la console");
		displayConsoleMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				InterfaceGraphics.this.displayConsole(displayConsoleMenuItem.isSelected());
			}
		});
		menu.add(menuItem);
		displayMenu.add(displayConsoleMenuItem);
		menuItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				InterfaceGraphics.this.processWindowEvent(new WindowEvent(InterfaceGraphics.this,WindowEvent.WINDOW_CLOSING));			
			}
		});
		JPanel gestSimu=new JPanel();
		playPause=new JButton("Play");
		playPause.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if(simulateur==null){
					JOptionPane.showMessageDialog(InterfaceGraphics.this, "Pas de Simulateur pour demarrer la simulation !","Simulateur ?", JOptionPane.ERROR_MESSAGE);
				}else{
					if(InterfaceGraphics.this.playPause.getText().equals("Play")){
						InterfaceGraphics.this.simulateur.play();
						InterfaceGraphics.this.playPause.setText("Pause");
						InterfaceGraphics.this.nextStep.setEnabled(false);
						InterfaceGraphics.this.console.printMessage("Simulation played !");
					}else{
						InterfaceGraphics.this.simulateur.pause();
						InterfaceGraphics.this.playPause.setText("Play");
						InterfaceGraphics.this.nextStep.setEnabled(true);
						InterfaceGraphics.this.console.printMessage("Simulation paused !");
					}
				}
			}
		});
		nextStep=new JButton("Prochaine Etape");
		nextStep.setEnabled(true);
		nextStep.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent arg0) {
					if(simulateur==null){
						JOptionPane.showMessageDialog(InterfaceGraphics.this, "Pas de Simulateur pour demarrer la simulation !","Simulateur ?", JOptionPane.ERROR_MESSAGE);
					}else{
						InterfaceGraphics.this.simulateur.step();
						InterfaceGraphics.this.console.printMessage("Next Step in simulation !");
					}
				}
			;}
		);
		addChar=new JButton("Ajouter un Character");
		addChar.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if(simulateur==null){
					JOptionPane.showMessageDialog(InterfaceGraphics.this, "Pas de Simulateur pour demarrer la simulation !","Simulateur ?", JOptionPane.ERROR_MESSAGE);
				}else{
					
				}
			}
		});
		addPlayer=new JButton("Ajouter un player");
		addPlayer.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if(simulateur==null){
					JOptionPane.showMessageDialog(InterfaceGraphics.this, "Pas de Simulateur pour demarrer la simulation !","Simulateur ?", JOptionPane.ERROR_MESSAGE);
				}else{
					//InterfaceGraphics.this.simulateur.pause();
					addPlayerFrame.setVisible(true);
				}
			}
		});
		addArea=new JButton("Ajouter une zone");
		addArea.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if(simulateur==null){
					JOptionPane.showMessageDialog(InterfaceGraphics.this, "Pas de Simulateur pour demarrer la simulation !","Simulateur ?", JOptionPane.ERROR_MESSAGE);
				}else{
					
				}
			}
		});
		increase=new JButton("Zoom +");
		increase.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if(simulateur==null){
					JOptionPane.showMessageDialog(InterfaceGraphics.this, "Pas de Simulateur pour demarrer la simulation !","Simulateur ?", JOptionPane.ERROR_MESSAGE);
				}else{
					displayMap2D.increaseFactor();
					repaint();
				}
			}
		});
		reduce=new JButton("Zoom -");
		reduce.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if(simulateur==null){
					JOptionPane.showMessageDialog(InterfaceGraphics.this, "Pas de Simulateur pour demarrer la simulation !","Simulateur ?", JOptionPane.ERROR_MESSAGE);
				}else{
					displayMap2D.reduceFactor();
					repaint();
				}
			}
		});
		gestSimu.setLayout(new GridBagLayout());
		GridBagConstraints gb1=new GridBagConstraints();
//		gb1.gridx = 0;
//		gb1.gridy = 0;
	    gb1.fill = GridBagConstraints.HORIZONTAL;
	    gb1.anchor = GridBagConstraints.CENTER;
	    gb1.ipadx = 0;
	    gb1.ipady = 0;
		gb1.gridwidth=GridBagConstraints.REMAINDER;
//		gb1.gridheight=0;
		gb1.insets=new Insets(5,5,5,5);
		gb1.weightx=1;
		gb1.weighty=0;
		GridBagConstraints gb2=new GridBagConstraints();
//		gb2.gridx = 0;
//		gb2.gridy = 0;
	    gb2.fill = GridBagConstraints.HORIZONTAL;
	    gb2.anchor = GridBagConstraints.CENTER;
	    gb2.ipadx = 0;
	    gb2.ipady = 0;
		gb2.gridwidth=GridBagConstraints.RELATIVE;
//		gb2.gridheight=0;
		gb2.insets=new Insets(5,5,5,5);
		gb2.weightx=1;
		gb2.weighty=0;
		gestSimu.add(playPause,gb1);
		gestSimu.add(nextStep,gb1);
		gestSimu.add(addChar,gb1);
		gestSimu.add(addPlayer,gb1);
		gestSimu.add(addArea,gb1);
		gestSimu.add(increase,gb2);
		gestSimu.add(reduce,gb1);
		
		showActiveArea=new JCheckBox("Afficher les Zones Actives");
		showActiveAreaXY=new JCheckBox("Afficher X,Y des Zones Actives");
		showAllAreas=new JCheckBox("Afficher toutes les Zones");
		showMobs=new JCheckBox("Afficher les Mobs");
		showCurrentPlayer=new JCheckBox("Afficher le joueur selectionn�");
		showAllPlayers=new JCheckBox("Afficher tous les joueurs");
		gestSimu.add(showAllAreas,gb1);
		gestSimu.add(showActiveArea,gb1);
		gestSimu.add(showMobs,gb1);
		gestSimu.add(showCurrentPlayer,gb1);
		gestSimu.add(showAllPlayers,gb1);
		JPanel selectedPlayer=new JPanel();
		currentPlayer=new JComboBox();
		currentPlayer.addItemListener(new ItemListener(){

			public void itemStateChanged(ItemEvent arg0) {
				// TODO Auto-generated method stub
				if(arg0.getItem() instanceof Player){
					currentPlayerFieldXYZPosition.setText("X:"+((Player)(arg0.getItem())).getX()+
							" Y:"+((Player)(arg0.getItem())).getY()+
							" Z:"+((Player)(arg0.getItem())).getZ());
				}else{
					currentPlayerFieldXYZPosition.setText("N/A");
				}
			}
			
		});
		carryMonster=new JComboBox();
		actionCurrentPlayer=new JComboBox();
		movingPanel.setVisible(false);
		currentPlayerField=new JTextArea("CPlayer");
		currentPlayerField.setEnabled(false);
		currentPlayerField.setDisabledTextColor(Color.BLACK);
		currentPlayerField.setBackground(this.getBackground());
		currentPlayerFieldPosition=new JTextArea("Position");
		currentPlayerFieldPosition.setEnabled(false);
		currentPlayerFieldPosition.setDisabledTextColor(Color.BLACK);
		currentPlayerFieldPosition.setBackground(this.getBackground());
		currentPlayerFieldXYZPosition=new JTextArea("N/A");
		currentPlayerFieldXYZPosition.setEnabled(false);
		currentPlayerFieldXYZPosition.setDisabledTextColor(Color.BLACK);
		currentPlayerFieldXYZPosition.setBackground(Color.WHITE);
		carryMonsterField=new JTextArea("CarryMonster");
		carryMonsterField.setEnabled(false);
		carryMonsterField.setDisabledTextColor(Color.BLACK);
		carryMonsterField.setBackground(this.getBackground());
		actionCurrentPlayerField=new JTextArea("Action Player");
		actionCurrentPlayerField.setEnabled(false);
		actionCurrentPlayerField.setDisabledTextColor(Color.BLACK);
		actionCurrentPlayerField.setBackground(this.getBackground());
		deletePlayer=new JButton("Supprimer");
//		selectedPlayer.setPreferredSize(new Dimension(180,200));
		selectedPlayer.setLayout(new GridBagLayout());
		GridBagConstraints gbComboxSPlayer=new GridBagConstraints();
		//  gbComboxSPlayer.gridx = 0;
		//  gbComboxSPlayer.gridy = 0;
	    gbComboxSPlayer.fill = GridBagConstraints.HORIZONTAL;
	    gbComboxSPlayer.anchor = GridBagConstraints.CENTER;
	    gbComboxSPlayer.ipadx = 0;
	    gbComboxSPlayer.ipady = 0;
		gbComboxSPlayer.gridwidth=GridBagConstraints.REMAINDER;
		//	gbComboxSPlayer.gridheight=0;
		gbComboxSPlayer.insets=new Insets(5,5,5,5);
		gbComboxSPlayer.weightx=1;
		gbComboxSPlayer.weighty=0;
		GridBagConstraints gbTextSPlayer=new GridBagConstraints();
		//  gbTextSPlayer.gridx = 0;
		//  gbTextSPlayer.gridy = 0;
	    gbTextSPlayer.fill = GridBagConstraints.HORIZONTAL;
	    gbTextSPlayer.anchor = GridBagConstraints.CENTER;
	    gbTextSPlayer.ipadx = 0;
	    gbTextSPlayer.ipady = 0;
		gbTextSPlayer.gridwidth=GridBagConstraints.RELATIVE;
		//	gbTextSPlayer.gridheight=0;
		gbTextSPlayer.insets=new Insets(5,5,5,5);
		gbTextSPlayer.weightx=0;
		gbTextSPlayer.weighty=0;
		selectedPlayer.add(currentPlayerField,gbTextSPlayer);
		selectedPlayer.add(currentPlayer,gbComboxSPlayer);
		selectedPlayer.add(currentPlayerFieldPosition,gbTextSPlayer);
		GridBagConstraints gbPosition=new GridBagConstraints();
//		gbPosition.gridx = 0;
//		gbPosition.gridy = 0;
		gbPosition.fill = GridBagConstraints.HORIZONTAL;
		gbPosition.anchor = GridBagConstraints.CENTER;
		gbPosition.ipadx = 0;
	    gbPosition.ipady = 0;
	    gbPosition.gridwidth=GridBagConstraints.REMAINDER;
//		gbPosition.gridheight=0;
	    gbPosition.insets=new Insets(5,5,5,5);
	    gbPosition.weightx=0;
	    gbPosition.weighty=0;
	    GridBagConstraints gbMovingPanel=new GridBagConstraints();
		//	gridBagSPlayer.gridx = 0;
		//  gridBagSPlayer.gridy = 0;
	    gbMovingPanel.fill = GridBagConstraints.HORIZONTAL;
	    gbMovingPanel.anchor = GridBagConstraints.CENTER;
	    gbMovingPanel.ipadx = 0;
	    gbMovingPanel.ipady = 0;
		gbMovingPanel.gridwidth=GridBagConstraints.REMAINDER;
		//	gridBagSPlayer.gridheight=0;
		// gbMovingPanel.insets=new Insets(5,5,5,5);
		gbMovingPanel.weightx=0;
		gbMovingPanel.weighty=1;
		selectedPlayer.add(currentPlayerFieldXYZPosition,gbPosition);
		selectedPlayer.add(carryMonsterField,gbTextSPlayer);
		selectedPlayer.add(carryMonster,gbComboxSPlayer);
		selectedPlayer.add(actionCurrentPlayerField,gbTextSPlayer);
		selectedPlayer.add(actionCurrentPlayer,gbComboxSPlayer);
		selectedPlayer.add(movingPanel,gbMovingPanel);
		JPanel eastPanel=new JPanel();
		eastPanel.setLayout(new GridBagLayout());
		GridBagConstraints gridBagSPlayer=new GridBagConstraints();
		//	gridBagSPlayer.gridx = 0;
		//  gridBagSPlayer.gridy = 0;
	    gridBagSPlayer.fill = GridBagConstraints.HORIZONTAL;
	    gridBagSPlayer.anchor = GridBagConstraints.CENTER;
	    gridBagSPlayer.ipadx = 0;
	    gridBagSPlayer.ipady = 0;
		gridBagSPlayer.gridwidth=GridBagConstraints.REMAINDER;
		//	gridBagSPlayer.gridheight=0;
		gridBagSPlayer.insets=new Insets(5,5,5,5);
		gridBagSPlayer.weightx=0;
		gridBagSPlayer.weighty=1;
		eastPanel.add(gestSimu,gbMovingPanel);
		selectedPlayer.setBorder(BorderFactory.createBevelBorder(1));
		eastPanel.add(selectedPlayer,gbComboxSPlayer);
		JScrollPane scrollPane=new JScrollPane(displayMap2D);
		this.setLayout(new GridBagLayout());
		GridBagConstraints gb4=new GridBagConstraints();
//		gb4.gridx = 0;
//		gb4.gridy = 0;
	    gb4.fill = GridBagConstraints.BOTH;
	    gb4.anchor = GridBagConstraints.CENTER;
	    gb4.ipadx = 0;
	    gb4.ipady = 0;
		gb4.gridwidth=GridBagConstraints.RELATIVE;
		gb4.gridheight=GridBagConstraints.REMAINDER;
		gb4.insets=new Insets(0,0,0,0);
		gb4.weightx=0;
		gb4.weighty=1;
		GridBagConstraints gb3=new GridBagConstraints();
//		gb3.gridx = 0;
//		gb3.gridy = 0;
	    gb3.fill = GridBagConstraints.BOTH;
	    gb3.anchor = GridBagConstraints.CENTER;
	    gb3.ipadx = 0;
	    gb3.ipady = 0;
		gb3.gridwidth=GridBagConstraints.REMAINDER;
		gb3.gridheight=GridBagConstraints.REMAINDER;
		gb3.insets=new Insets(0,0,0,0);
		gb3.weightx=1;
		gb3.weighty=1;
		this.add(eastPanel,gb4);
		this.add(scrollPane,gb3);
		//this.add(displayMap2D,gb3);
		console=new Console();
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		lastnbConnectedPlayer=-1;
	}
	/**
	 * this methode task WindowsEvent !
	 */
	public void processWindowEvent(WindowEvent e){
		if(e.paramString().contains("WINDOW_CLOSING")){
			// chose a faire si on quit
			// JOptionPane.showMessageDialog(this, "fermeture l'application !");
		}
		super.processWindowEvent(e);
	}
	/**
	 * This methode update the view of map
	 * @param gameEngine 
	 *
	 */
	public void updateData(GameEngine gameEngine) {
		LinkedList<Player> players=gameEngine.getConnectedPlayer();
		if(players.size()!=lastnbConnectedPlayer){
			lastnbConnectedPlayer=players.size();
			if(players.size()>0){
				Object object=currentPlayer.getSelectedItem();
				// TODO a changer pas efficace : pour un changement reload tt !
				currentPlayer.removeAllItems();
				currentPlayer.setEnabled(true);
				for(Player player:gameEngine.getConnectedPlayer()){
					currentPlayer.addItem(player);
				}
				currentPlayer.setSelectedItem(object);
				movingPanel.setVisible(true);
			}else{
				// comme la liste est vide on supprime tous le contenue
				movingPanel.setVisible(false);
				currentPlayer.setEnabled(false);
				currentPlayer.removeAllItems();
				currentPlayer.addItem(new String("Pas de joueur"));
				carryMonster.removeAllItems();
				carryMonster.setEnabled(false);
				carryMonster.addItem(new String("Aucun joueur selectionne"));
				actionCurrentPlayer.removeAllItems();
				actionCurrentPlayer.setEnabled(false);
				actionCurrentPlayer.addItem(new String("Aucun joueur selectionne"));
			}
		}
		listOfArea=gameEngine.getWorldAreas();
		displayMap2D.updateAreas(listOfArea);
		while(!playersToAdd.isEmpty()){
			gameEngine.connectPlayer(playersToAdd.poll());
		}
		this.repaint();
	}
	public void setSimulateur(Simulateur simulateur2) {
		this.simulateur=simulateur2;
	}
	public void displayConsole(boolean b){
		console.setVisible(b);
		displayConsoleMenuItem.setSelected(b);
	}
	public void printToConsole(String string){
		console.printMessage(string);
	}
	/**
	 * this class is a console for InterfaceGraphics
	 * @author Yannick
	 *
	 */
	protected class Console extends JFrame{
		private JTextPane output;
		private JTextField inputCommand;
		/**
		 * this booleen is true if Console Display the command excute
		 */
		private boolean echo;
		
		public Console(){
			
			super("Console");
			this.setLayout(new BorderLayout());
			output=new JTextPane();
			output.setEditable(false);
			JScrollPane scrollPane=new JScrollPane(output);
			//scrollPane.setPreferredSize(new Dimension(300,150));
			inputCommand=new JTextField();
			inputCommand.addKeyListener(new KeyListener(){
				public void keyPressed(KeyEvent e) {
					if(e.getKeyCode()==KeyEvent.VK_ENTER){
						if(!inputCommand.getText().equals("")){
							Console.this.execute(inputCommand.getText());
							inputCommand.setText("");
						}
					}else if(e.getKeyCode()==KeyEvent.VK_UP){
						//Console.this.inputCommand.setText("up");
					}else if(e.getKeyCode()==KeyEvent.VK_DOWN){
						//Console.this.inputCommand.setText("down");
					}
				}
				public void keyReleased(KeyEvent e){}
				public void keyTyped(KeyEvent e) {}
			});
			inputCommand.setPreferredSize(new Dimension(300,25));
			this.setAlwaysOnTop(true);
			this.add(scrollPane,BorderLayout.CENTER);
			this.add(inputCommand,BorderLayout.SOUTH);
			this.setPreferredSize(new Dimension(640,480));
			this.pack();
			this.inputCommand.requestFocus();
		}
		public void printMessage(String string) {
			// TODO Auto-generated method stub
			output.setText(output.getText()+string+"\n");
			output.setCaretPosition(output.getText().length());
		}
		protected void execute(String text) {
			// Script d'analyse pour la l'excution
			if(echo){
				output.setText(output.getText()+">"+inputCommand.getText()+"\n");
			}
			text=text.toLowerCase();
			StringTokenizer stringTokenizer=new StringTokenizer(text," ");
			String command=stringTokenizer.nextToken();
			if(command.equals("help")){
				output.setText(output.getText()+this.getHelpMessage()+"\n");
			}else if(command.equals("display")){
				
			}else if(command.equals("printmessage")){
				while(stringTokenizer.hasMoreTokens()){
					output.setText(output.getText()+stringTokenizer.nextToken()+" ");
				}
				output.setText(output.getText()+"\n");
			}else if(command.equals("echo")){
				if(stringTokenizer.hasMoreTokens()){
					String next=stringTokenizer.nextToken();
					if(next.equals("on")){
						echo=true;
					}else if(next.equals("off")){
						echo=false;
					}else{
						output.setText(output.getText()+this.getUnknowCommand(next,text)+"\n");
						return;
					}
				}
				if(echo){
					printMessage("echo is on");
				}else{
					printMessage("echo is off");
				}
			}else if(command.equals("create")){
				
			}else if(command.equals("delete")){
				
			}else if(command.equals("play")){
				printMessage("simulation played");
				InterfaceGraphics.this.playPause.setText("Pause");
				InterfaceGraphics.this.nextStep.setEnabled(false);
				InterfaceGraphics.this.console.printMessage("Simulation played !");
				InterfaceGraphics.this.simulateur.play();
			}else if(command.equals("pause")){
				InterfaceGraphics.this.playPause.setText("Play");
				InterfaceGraphics.this.nextStep.setEnabled(true);
				InterfaceGraphics.this.console.printMessage("Simulation paused !");
				InterfaceGraphics.this.simulateur.pause();
			}else if(command.equals("step")){
				printMessage("next simulation step");
				InterfaceGraphics.this.console.printMessage("Next Step in simulation !");
				InterfaceGraphics.this.simulateur.step();
			}else if(command.equals("separator")){
				printMessage("______________________________________________________________");
			}else if(command.equals("clear")){
				output.setText("Console clear !\n");
			}else if(command.equals("set_position")){
								
			}else{
				output.setText(output.getText()+this.getUnknowCommand(command,text)+"\n");
			}
			
		}
		private String getUnknowCommand(String command, String text) {
			return "unknow command :["+command+"] in : ["+text+"]";
		}
		private String getHelpMessage(){
			return "----------- Help -------------\n"
			+"Command List :\n"
			+"-help : display this message\n"
			+"-printmessage [Message]: print [Message] on the console\n"
			+"-display : not implement\n"
			+"-echo : display the display of the commands statut\n"
			+"-echo on: active diplay of enter command\n"
			+"-echo off: disactive diplay of enter command\n"
			+"-set_position [name] [x]:[y] : not implement"
			+"-create : not implement\n"
			+"-delete : not implement\n"
			+"-separator : print a separator\n"
			+"-clear : clear console";
		}
		public void processWindowEvent(WindowEvent e){
			if(e.paramString().contains("WINDOW_CLOSING")){
				// chose a faire si on ferme la console
				InterfaceGraphics.this.displayConsoleMenuItem.setSelected(false);
				InterfaceGraphics.this.requestFocus();
			}
			super.processWindowEvent(e);
		}
		
	}
	public class Joystick extends JPanel{
		private JButton goNorth;
		private JButton goNorthEast;
		private JButton goNorthWeast;
		private JButton goSouth;
		private JButton goSouthEast;
		private JButton goSouthWeast;
		private JButton goEast;
		private JButton goWeast;
		private JButton goTP;
		private static final long TIME=50;
		public Joystick() {
			goNorth=new JButton("^");
			goNorthEast=new JButton("/");
			goNorthWeast=new JButton("\\");
			goEast=new JButton(">");
			goTP=new JButton("TP");
			goWeast=new JButton("<");
			goSouth=new JButton("v");
			goSouthEast=new JButton("\\");
			goSouthWeast=new JButton("/");
			this.setLayout(new GridLayout(3,3));
			this.add(goNorthWeast);
			this.add(goNorth);
			this.add(goNorthEast);
			this.add(goWeast);
			this.add(goTP);
			this.add(goEast);
			this.add(goSouthWeast);
			this.add(goSouth);
			this.add(goSouthEast);
			
			goNorth.addMouseListener(new MouseListener(){
				private Thread deplacement;
				private boolean release;
				{(deplacement=new Thread(new Runnable(){public void run() {while(true){
								// PASTE THE CODE TO DO
					if(currentPlayer.getSelectedItem() instanceof Player){
						Player player=((Player)(currentPlayer.getSelectedItem()));
						player.setY(player.getY()-1);
						try {
							Thread.sleep(TIME);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
							}}})).start();deplacement.suspend();release=true;}
				public void mouseClicked(MouseEvent e) {}
				public void mouseEntered(MouseEvent e) {}
				public void mouseExited(MouseEvent e) {}
				public void mousePressed(MouseEvent e) {
					if(release){deplacement.resume();release=false;}}
				public void mouseReleased(MouseEvent e) {
					if(!release){deplacement.suspend();release=true;}}});
			
			goNorthEast.addMouseListener(new MouseListener(){
				private Thread deplacement;
				private boolean release;
				{(deplacement=new Thread(new Runnable(){public void run() {while(true){
								// PASTE THE CODE TO DO
					if(currentPlayer.getSelectedItem() instanceof Player){
						Player player=((Player)(currentPlayer.getSelectedItem()));
						player.setY(player.getY()-1);
						player.setX(player.getX()+1);
						try {
							Thread.sleep(TIME);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}}})).start();deplacement.suspend();release=true;}
				public void mouseClicked(MouseEvent e) {}
				public void mouseEntered(MouseEvent e) {}
				public void mouseExited(MouseEvent e) {}
				public void mousePressed(MouseEvent e) {
					if(release){deplacement.resume();release=false;}}
				public void mouseReleased(MouseEvent e) {
					if(!release){deplacement.suspend();release=true;}}});
			goNorthWeast.addMouseListener(new MouseListener(){
				private Thread deplacement;
				private boolean release;
				// PARTIE DU SIMULATEUR QUI REMPLACE LE CODE DE DEPLACEMENT
				{(deplacement=new Thread(new Runnable(){public void run() {while(true){
								// PASTE THE CODE TO DO
					if(currentPlayer.getSelectedItem() instanceof Player){
						Player player=((Player)(currentPlayer.getSelectedItem()));
						player.setY(player.getY()-1);
						player.setX(player.getX()-1);
						try {
							Thread.sleep(TIME);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}}})).start();deplacement.suspend();release=true;}
				public void mouseClicked(MouseEvent e) {}
				public void mouseEntered(MouseEvent e) {}
				public void mouseExited(MouseEvent e) {}
				public void mousePressed(MouseEvent e) {
					if(release){deplacement.resume();release=false;}}
				public void mouseReleased(MouseEvent e) {
					if(!release){deplacement.suspend();release=true;}}});
			goEast.addMouseListener(new MouseListener(){
				private Thread deplacement;
				private boolean release;
				{(deplacement=new Thread(new Runnable(){public void run() {while(true){
								// PASTE THE CODE TO DO
					if(currentPlayer.getSelectedItem() instanceof Player){
						Player player=((Player)(currentPlayer.getSelectedItem()));
						player.setX(player.getX()+1);
						try {
							Thread.sleep(TIME);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}}})).start();deplacement.suspend();release=true;}
				public void mouseClicked(MouseEvent e) {}
				public void mouseEntered(MouseEvent e) {}
				public void mouseExited(MouseEvent e) {}
				public void mousePressed(MouseEvent e) {
					if(release){deplacement.resume();release=false;}}
				public void mouseReleased(MouseEvent e) {
					if(!release){deplacement.suspend();release=true;}}});
			goTP.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					if(currentPlayer.getSelectedItem() instanceof Player){
						// TODO encore un truc pas prevu a implementé
					}
				}
			});
			goWeast.addMouseListener(new MouseListener(){
				private Thread deplacement;
				private boolean release;
				{(deplacement=new Thread(new Runnable(){public void run() {while(true){
								// PASTE THE CODE TO DO
					if(currentPlayer.getSelectedItem() instanceof Player){
						Player player=((Player)(currentPlayer.getSelectedItem()));
						player.setX(player.getX()-1);
						try {
							Thread.sleep(TIME);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}}})).start();deplacement.suspend();release=true;}
				public void mouseClicked(MouseEvent e) {}
				public void mouseEntered(MouseEvent e) {}
				public void mouseExited(MouseEvent e) {}
				public void mousePressed(MouseEvent e) {
					if(release){deplacement.resume();release=false;}}
				public void mouseReleased(MouseEvent e) {
					if(!release){deplacement.suspend();release=true;}}});
			goSouth.addMouseListener(new MouseListener(){
				private Thread deplacement;
				private boolean release;
				{(deplacement=new Thread(new Runnable(){public void run() {while(true){
								// PASTE THE CODE TO DO
					if(currentPlayer.getSelectedItem() instanceof Player){
						Player player=((Player)(currentPlayer.getSelectedItem()));
						player.setY(player.getY()+1);
						try {
							Thread.sleep(TIME);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}}})).start();deplacement.suspend();release=true;}
				public void mouseClicked(MouseEvent e) {}
				public void mouseEntered(MouseEvent e) {}
				public void mouseExited(MouseEvent e) {}
				public void mousePressed(MouseEvent e) {
					if(release){deplacement.resume();release=false;}}
				public void mouseReleased(MouseEvent e) {
					if(!release){deplacement.suspend();release=true;}}});
			goSouthEast.addMouseListener(new MouseListener(){
				private Thread deplacement;
				private boolean release;
				{(deplacement=new Thread(new Runnable(){public void run() {while(true){
								// PASTE THE CODE TO DO
					if(currentPlayer.getSelectedItem() instanceof Player){
						Player player=((Player)(currentPlayer.getSelectedItem()));
						player.setY(player.getY()+1);
						player.setX(player.getX()+1);
						try {
							Thread.sleep(TIME);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}}})).start();deplacement.suspend();release=true;}
				public void mouseClicked(MouseEvent e) {}
				public void mouseEntered(MouseEvent e) {}
				public void mouseExited(MouseEvent e) {}
				public void mousePressed(MouseEvent e) {
					if(release){deplacement.resume();release=false;}}
				public void mouseReleased(MouseEvent e) {
					if(!release){deplacement.suspend();release=true;}}});
			goSouthWeast.addMouseListener(new MouseListener(){
				private Thread deplacement;
				private boolean release;
				{(deplacement=new Thread(new Runnable(){
				public void run() {while(true){
								// PASTE THE CODE TO DO
					if(currentPlayer.getSelectedItem() instanceof Player){
						Player player=((Player)(currentPlayer.getSelectedItem()));
						player.setY(player.getY()+1);
						player.setX(player.getX()-1);
						try {
							Thread.sleep(TIME);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}}})).start();deplacement.suspend();release=true;}
				public void mouseClicked(MouseEvent e) {}
				public void mouseEntered(MouseEvent e) {}
				public void mouseExited(MouseEvent e) {}
				public void mousePressed(MouseEvent e) {
					if(release){deplacement.resume();release=false;}}
				public void mouseReleased(MouseEvent e) {
					if(!release){deplacement.suspend();release=true;}}});
			
			
//			.addMouseListener(new MouseListener(){
//			private Thread deplacement;
//			private boolean release;
//			{(deplacement=new Thread(new Runnable(){public void run() {while(true){
//							// PASTE THE CODE TO DO
//						}}})).start();deplacement.suspend();release=true;}
//			public void mouseClicked(MouseEvent e) {}
//			public void mouseEntered(MouseEvent e) {}
//			public void mouseExited(MouseEvent e) {}
//			public void mousePressed(MouseEvent e) {
//				if(release){deplacement.resume();release=false;}}
//			public void mouseReleased(MouseEvent e) {
//				if(!release){deplacement.suspend();release=true;}}});
		}
	}
	public class AddPlayerFrame extends JFrame{
		private JTextField name;
		
		private JComboBox comboBox;
		
		private JTextArea positionX;
		private JTextArea positionY;
		private JTextArea positionZ;
		
		private JButton create;
		private JButton takeWithMouse;
		
		private JTextField positionXField;

		private JTextField positionYField;

		private JTextField positionZField;
		private int x=0,y=0,z=0;

		private boolean takeWithMouseBoolean;
		private int i;
		
		public AddPlayerFrame(){
			takeWithMouseBoolean=false;
			GridBagConstraints gb1=new GridBagConstraints();
//			gb1.gridx = 0;
//			gb1.gridy = 0;
		    gb1.fill = GridBagConstraints.HORIZONTAL;
		    gb1.anchor = GridBagConstraints.CENTER;
		    gb1.ipadx = 0;
		    gb1.ipady = 0;
			gb1.gridwidth=GridBagConstraints.REMAINDER;
//			gb1.gridheight=0;
			gb1.insets=new Insets(10,10,10,10);
			gb1.weightx=1;
			gb1.weighty=0;
			GridBagConstraints gb2=new GridBagConstraints();
//			gb2.gridx = 0;
//			gb2.gridy = 0;
		    gb2.fill = GridBagConstraints.HORIZONTAL;
		    gb2.anchor = GridBagConstraints.CENTER;
		    gb2.ipadx = 0;
		    gb2.ipady = 0;
			gb2.gridwidth=GridBagConstraints.RELATIVE;
//			gb2.gridheight=0;
			gb2.insets=new Insets(10,10,10,10);
			gb2.weightx=1;
			gb2.weighty=0;
			name=new JTextField("Entre le nom ");
			create=new JButton("Cr�er et ajouter");
			create.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					AddPlayerFrame.this.addPlayer();
				}
			});
			positionX=new JTextArea("Position X :");
			positionX.setEnabled(false);
			positionX.setDisabledTextColor(Color.BLACK);
			positionX.setBackground(this.getBackground());
			positionY=new JTextArea("Position Y :");
			positionY.setEnabled(false);
			positionY.setDisabledTextColor(Color.BLACK);
			positionY.setBackground(this.getBackground());
			positionZ=new JTextArea("Position Z :");
			positionZ.setEnabled(false);
			positionZ.setDisabledTextColor(Color.BLACK);
			positionZ.setBackground(this.getBackground());
			positionXField=new JTextField("0");
			positionXField.addKeyListener(new InAreaKeyListener());
			positionYField=new JTextField("0");
			positionYField.addKeyListener(new InAreaKeyListener());
			positionZField=new JTextField("0");
			positionZField.addKeyListener(new InAreaKeyListener());
			comboBox=new JComboBox();
			takeWithMouse=new JButton("Placer a la souris : Off");
			takeWithMouse.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					if(takeWithMouseBoolean){
						takeWithMouse.setText("Placer a la souris : Off");
					}else{
						takeWithMouse.setText("Placer a la souris : On");
					}
					takeWithMouseBoolean=!takeWithMouseBoolean;
				}
			});
			this.setLayout(new GridBagLayout());
			this.add(name,gb1);
			this.add(create,gb1);
			this.add(positionX,gb2);
			this.add(positionXField,gb1);
			this.add(positionY,gb2);
			this.add(positionYField,gb1);
			this.add(positionZ,gb2);
			this.add(positionZField,gb1);
			this.add(comboBox,gb1);
			this.add(takeWithMouse,gb1);
			this.pack();
			this.setAlwaysOnTop(true);
			this.setDefaultCloseOperation(HIDE_ON_CLOSE);
			this.i=1;
		}
		protected void addPlayer() {
			InterfaceGraphics.this.playersToAdd.add(new Player((Area) comboBox.getSelectedItem(),name.getText(),x,y,z));
			i++;
			name.setText("Player#"+i);
		}
		@Override
		public void setVisible(boolean b) {
			positionXField.setText(x+"");
			positionYField.setText(y+"");
			positionZField.setText(z+"");
			this.name.setText("Player#"+i);
			comboBox.removeAllItems();
			new InAreaKeyListener().keyReleased(null);
			takeWithMouse.setText("Placer a la souris : Off");
			takeWithMouseBoolean=false;
			super.setVisible(b);			
		}
		public class InAreaKeyListener implements KeyListener {

			

			public void keyPressed(KeyEvent arg0) {}

			public void keyReleased(KeyEvent arg0) {
				int x=0,y=0,z=0;
				try{
					x=Integer.parseInt(positionXField.getText());
				}catch(NumberFormatException e){
					positionXField.setText("0");
				}try{
					y=Integer.parseInt(positionYField.getText());
				}catch(NumberFormatException e){
					positionYField.setText("0");
				}try{
					z=Integer.parseInt(positionZField.getText());
				}catch(NumberFormatException e){
					positionZField.setText("0");
				}
				AddPlayerFrame.this.setX(x);
				AddPlayerFrame.this.setY(y);
				AddPlayerFrame.this.setZ(z);
			}
			public void keyTyped(KeyEvent arg0) {
				
			}
		
		}
		public boolean isTakeWithMouse() {
			return takeWithMouseBoolean;
		}
		public void setX(int x) {
			positionXField.setText(x+"");
			if(listOfArea != null){
				comboBox.removeAllItems();
				for(Area area: listOfArea){
					if(area.containPosition(x,y,z)){
						comboBox.addItem(area);
					}
				}	
				create.setEnabled(true);
			}else{
				create.setEnabled(false);
				comboBox.setEnabled(false);
				comboBox.addItem(new String("No Area Loaded"));
			}
			InterfaceGraphics.AddPlayerFrame.this.x=x;
		}
		public void setY(int y) {
			positionYField.setText(y+"");
			try{
				y=Integer.parseInt(positionYField.getText());
			}catch(NumberFormatException e){
				positionYField.setText("0");
			}
			if(listOfArea != null){
				comboBox.removeAllItems();
				for(Area area: listOfArea){
					if(area.containPosition(x,y,z)){
						comboBox.addItem(area);
					}
				}	
				create.setEnabled(true);
			}else{
				create.setEnabled(false);
				comboBox.setEnabled(false);
				comboBox.addItem(new String("No Area Loaded"));
			}
			InterfaceGraphics.AddPlayerFrame.this.y=y;
		}
		public void setZ(int z) {
			positionZField.setText(z+"");
			
			try{
				z=Integer.parseInt(positionZField.getText());
			}catch(NumberFormatException e){
				positionZField.setText("0");
			}
			if(listOfArea != null){
				comboBox.removeAllItems();
				for(Area area: listOfArea){
					if(area.containPosition(x,y,z)){
						comboBox.addItem(area);
					}
				}	
				create.setEnabled(true);
			}else{
				create.setEnabled(false);
				comboBox.setEnabled(false);
				comboBox.addItem(new String("No Area Loaded"));
			}
			InterfaceGraphics.AddPlayerFrame.this.z=z;
		}
	}
}
