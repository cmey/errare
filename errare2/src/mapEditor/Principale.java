package mapEditor;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.media.opengl.GLCanvas;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

@SuppressWarnings("serial")
public class Principale extends JFrame{
	
	private GLCanvas glc;
	private MyListener myListener;
	private JMenuBar menuBar;
	private JMenu fichier;
	private JMenuItem open,save,quitter;
	private JPanel gauchePanel, topPanel;
	private JLabel placing_label, sculpture_label;
	private JButton translate_button,rotate_button,scale_button,import_object_button,elevation_button,smooth_button,CSG_box_button,CSG_substract;
	private JSlider complexity;
	
	public Principale (String titre){
		super(titre);
		setLeTitre();
		
		this.setLayout(new BorderLayout());
		
		gauchePanel = new JPanel();
		gauchePanel.setLayout(new GridLayout(5,1));
		this.add(gauchePanel,BorderLayout.WEST);
		setLesBoutonsPlacement(gauchePanel);
		
		topPanel = new JPanel();
		topPanel.setLayout(new GridLayout(1,5));
		this.add(topPanel,BorderLayout.NORTH);
		setLesBoutonsSculpture(gauchePanel);
		
		setSize(new Dimension(800,600));
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		requestFocusInWindow();
		pack();
		setVisible(true);
	}
	
	private void setLesBoutonsPlacement(JPanel where){
		placing_label = new JLabel("Placing tools:");
		import_object_button = new JButton("Import OBJ");
		import_object_button.addActionListener(myListener);
		import_object_button.setMnemonic('I');
		
		translate_button = new JButton("Translate");
		translate_button.addActionListener(myListener);
		translate_button.setMnemonic('T');
		
		rotate_button = new JButton("Rotate");
		rotate_button.addActionListener(myListener);
		rotate_button.setMnemonic('R');
		
		scale_button = new JButton("Scale");
		scale_button.addActionListener(myListener);
		scale_button.setMnemonic('S');
		
		where.add(placing_label);
		where.add(import_object_button);
		where.add(translate_button);
		where.add(rotate_button);
		where.add(scale_button);
	}
	
	private void setLesBoutonsSculpture(JPanel where){
		sculpture_label = new JLabel("Sculpting tools:");
		elevation_button = new JButton("Elevation");
		elevation_button.addActionListener(myListener);
		elevation_button.setMnemonic('E');
		
		smooth_button = new JButton("Smooth");
		smooth_button.addActionListener(myListener);
		smooth_button.setMnemonic('M');
		
		CSG_box_button = new JButton("CSG Box");
		CSG_box_button.addActionListener(myListener);
		CSG_box_button.setMnemonic('B');
		
		CSG_substract = new JButton("Substract");
		CSG_substract.addActionListener(myListener);
		CSG_substract.setMnemonic('-');
		
		where.add(sculpture_label);
		where.add(smooth_button);
		where.add(CSG_box_button);
		where.add(CSG_substract);
	}
	
	private void setLeTitre(){
		myListener = new MyListener();
		menuBar = new JMenuBar();
		fichier = new JMenu("Fichier");
		
		open = new JMenuItem("Open map...");
		open.addActionListener(myListener);
		open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,ActionEvent.CTRL_MASK));
		open.setMnemonic('O');
		
		save = new JMenuItem("Save map...");
		save.addActionListener(myListener);
		save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,ActionEvent.CTRL_MASK));
		save.setMnemonic('S');
		
		quitter = new JMenuItem("Quitter");
		quitter.addActionListener(myListener);
		
		this.setJMenuBar(menuBar);
		menuBar.add(fichier);
		
		fichier.add(open);
		fichier.add(save);
		fichier.add(quitter);
	}
	
	public void setGLCanvas(GLCanvas glc){
		this.glc = glc;
		glc.setSize(1024,768);
		this.add(glc,BorderLayout.CENTER);
		pack();
	}
	
	/*
	public void setModel(Model model){
		this.model = model;
		if(complexity!=null) gauchePanel.remove(complexity);
		complexity = new JSlider(JSlider.HORIZONTAL,0,model.render_num,model.render_num);
		complexity.setMajorTickSpacing(100);
		complexity.setMinorTickSpacing(10);
		complexity.setPaintLabels(true);
		complexity.setPaintTicks(true);
		complexity.addChangeListener(myListener);
		complexity.setEnabled(false);
		gauchePanel.add(complexity);
		incrementB.setEnabled(false);
		decrementB.setEnabled(false);
		extractB.setEnabled(true);
		pack();
	}
	*/
	
	private class MyListener implements ActionListener,ChangeListener{
		
		public void actionPerformed(ActionEvent ae) {
			if (ae.getSource()==open) {
				// ouvrir un fichier et lancer son chargement
				new OuvrirFichier(null);
			}else if (ae.getSource()==save) {
				new SaveFile(null);
			}else if (ae.getSource()==quitter) {
				System.exit(0);
			}/*else if (ae.getSource()==extractB) {
				model.doProgMesh();
				extractB.setEnabled(false);
				incrementB.setEnabled(true);
				decrementB.setEnabled(true);
				complexity.setEnabled(true);
			}else if (ae.getSource()==incrementB) {
				if(model.render_num<model.frames[model.currentFrame].vertices.length)
				model.render_num++;
			}else if (ae.getSource()==decrementB) {
				if(model.render_num>0);
				model.render_num--;
			}*/
		}

		public void stateChanged(ChangeEvent ce) {
			JSlider source = (JSlider)ce.getSource();
		    int n = (int)source.getValue();
		   // model.render_num=n;
		}	
	}
	
}
