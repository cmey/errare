package guiEngine;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/*
 * Created on 13 juin 2005
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

public class Options extends JPanel {
	
	private JFrame mainWindow;
	private int width,height;
	private Image imageMenu1280x1024,imageExit2;
	
	/** FAIRE DES ONGLETS VIDEO AUDIO JEU
	 * 
	 * VIDEO : RESOLUTION, ECLAIRAGE, GAMMA, CONTRASTE
	 * AUDIO : VOLUME SON
	 * JEU : DIFFICULTE
	 */
	
	
	public Options(int width, int height, JFrame mainWindow) {
		
		//this.setBackground(Color.blue);
		
		this.mainWindow=mainWindow;
		this.width=width;
		this.height=height;
		

		
		/* --------------  First Panel :  Game --------------------*/
		JPanel jeu = new JPanel();
		
		final JLabel textDifficulte = new JLabel("Normal");
		textDifficulte.setFont(new Font("Diaolog", Font.BOLD, 25));
		
		JSlider sliderDifficulte = new JSlider(0,2,1); 
		sliderDifficulte.setPaintTicks(true);
		sliderDifficulte.setMajorTickSpacing(1);
		sliderDifficulte.setSnapToTicks(true);
		sliderDifficulte.addChangeListener(new ChangeListener () {	
			public void stateChanged(ChangeEvent e) {
				JSlider listener = (JSlider)e.getSource();
				if (listener.getValue()==0) {
					textDifficulte.setText("Facile");
				}else if (listener.getValue()==1) {
					textDifficulte.setText("Normal");
				}else if (listener.getValue()==2) {
					textDifficulte.setText("Difficile");
				}
		}}
		);
		
		
		jeu.add(sliderDifficulte);
		jeu.add(textDifficulte);
		jeu.setBackground(Color.black);
		
		/* --------------  Second Panel :  Video --------------------*/
		JPanel video = new JPanel();
		JCheckBox j = new JCheckBox();
		j.setIcon(new ImageIcon("draw/charger1.png"));
		
		video.add(j);
		
		/* --------------  Third Panel :  Audio --------------------*/
		JPanel audio = new JPanel();
		
	
		JSlider sliderVolume = new JSlider(0,5,1); 
		sliderVolume.setPaintTicks(true);
		sliderVolume.setMajorTickSpacing(1);
		sliderVolume.setSnapToTicks(true);
		sliderVolume.setPaintLabels(true);
		sliderVolume.addChangeListener(new ChangeListener () {	
			public void stateChanged(ChangeEvent e) {
				JSlider listener = (JSlider)e.getSource();
				if (listener.getValue()==0) {
				}else if (listener.getValue()==1) {
				}else if (listener.getValue()==2) {

				}
		}}
		);
		
		
		audio.add(sliderVolume);

		
		drawInitialisation();
		this.setPreferredSize(new Dimension(this.width,this.height));
		
		/* Les Onglets */
		
		this.setLayout(new BorderLayout());
		JTabbedPane tabOptions = new JTabbedPane();
		//tabOptions.setBackground(Color.black);
		tabOptions.setFont(new Font("Diaolog", Font.BOLD, 25));
		tabOptions.add("Jeu",jeu);
		tabOptions.add("Video",video);
		tabOptions.add("Audio",audio);
		tabOptions.setTabPlacement(JTabbedPane.TOP);
		this.add(tabOptions, BorderLayout.CENTER);
		repaint();
	}
	
	private void drawInitialisation() {
		
		imageMenu1280x1024 = (new ImageIcon("draw/menu1280x1024.png")).getImage();
		imageExit2 = (new ImageIcon("draw/quitter2.png")).getImage();
	}
	
	/**
	 * Methode paintComponent
	 * Display objects on the JPanel  
	 */
	
	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		
		g.drawImage(imageMenu1280x1024, 0, 0, null);
		g.drawString("aaa",100,100);
		
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
	 */

	
	/*	public static void main (String args[]) {
	 Toolkit tk = Toolkit.getDefaultToolkit();
	 Dimension dimScreen = tk.getScreenSize();
	 JFrame window = new JFrame("Title");
	 window.setSize(200,300);
	 //window.setUndecorated(true);
	  window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	  //window.setContentPane(new Options(dimScreen.width,dimScreen.height));
	   window.setIconImage((new ImageIcon("????.png")). getImage());
	   //window.pack();
	    
	    JSlider sliderResolution = new JSlider(0,6,1); 
	    sliderResolution.setPaintTicks(true);
	    sliderResolution.setMajorTickSpacing(1);
	    sliderResolution.setSnapToTicks(true);
	    window.add(sliderResolution);
	    JTabbedPane j = new JTabbedPane();
	    j.add("aa",new JLabel("ee"));
	    j.add("aaa",new JLabel("eee"));
	    window.add(j);
	    window.setVisible(true);
	    }*/
}
