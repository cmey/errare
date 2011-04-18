package physicsEngine;

import genericReps.SettingRep;
import geom.Point;
import graphicsEngine.Camera;
import graphicsEngine.EyeCamera;
import graphicsEngine.FollowCamera;
import graphicsEngine.GraphicalRep;
import graphicsEngine.GraphicsEngine;
import graphicsEngine.GraphicalRepFactory;
import graphicsEngine.MD2Factory;
import graphicsEngine.Skydome;
import graphicsEngine.World;

import java.awt.Color;
import java.io.IOException;

import javax.swing.JFrame;
import javax.xml.parsers.ParserConfigurationException;

import userInputEngine.UserInputController;
import xmlEngine.XmlEngine;

public class Test {
	
	public static final long PERIOD = (long)((1.0/60.0)*1E9);

	
	public static void main(String[] args) throws Exception {
		JFrame jf = new JFrame();
		jf.setTitle("Physics Engine Test");
		
		XmlEngine xmlEngine = new XmlEngine();
		
		UserInputController userInputController = new UserInputController("test", xmlEngine);
		
		GraphicsEngine ge = new GraphicsEngine(jf, userInputController, false);
		PhysicsEngine pe = new PhysicsEngine(userInputController, ge);		
		
		PhysicalRep pr = new PhysicalRep(new Point(0, 100, 0), new Point(30, 145, 30));
		
		
		GraphicalRep md2 = GraphicalRepFactory.load("data/md2/hobgoblin/hobgoblin.md2");
		md2.setShowAABox(true);
		SettingRep mainChar = new SettingRep(pr, md2, 0);
		
		pr.setRep(mainChar);
		md2.setRep(mainChar);
		
		pe.add(mainChar.getPhysicalRep(), null);
		pe.setMainChar(pr);
		
		GraphicalRep md2Terrain = GraphicalRepFactory.load("data/md2/terrain/terrain.md2");
		float[] terrBounds = md2Terrain.getGraphicalBoundaries();
		PhysicalRep pTerrain = new PhysicalRep(new Point(terrBounds[0], terrBounds[2], terrBounds[4]), new Point(terrBounds[1], terrBounds[3], terrBounds[5]));
		
		SettingRep terrain = new SettingRep(pTerrain, md2Terrain, 1);
		pTerrain.setRep(terrain);
		md2Terrain.setRep(terrain);
		
		pe.add(terrain.getPhysicalRep(), null);
		
		
		Skydome sky = new Skydome(Skydome.SKY_CLEAR);
		
		Camera cam = new FollowCamera(mainChar.getPhysicalRep().getCenter());
		
		World world1 = new World(pe.getOctree(), sky, cam);
		ge.changeWorld(world1);

		jf.add(ge.getGLComponent());
		jf.setUndecorated(true);
		jf.setBounds(0,0,800,600);
		jf.getContentPane().add(ge.getGLComponent());
		jf.validate();
		jf.setVisible(true);
		
		
		long begin, end;
		long tmp=0;
		long sleeptime=0;
		long runtime=0;
		
		begin = System.nanoTime();
		
		while(true) {
			
			if(sleeptime>0) {
				try {
					Thread.sleep((long)(sleeptime/1000000), (int)(sleeptime%1000000));
				} catch (InterruptedException ex) {}
			}
			
			
			pe.run();
			ge.run();
			
			tmp = begin;
			begin = System.nanoTime();
			end = System.nanoTime();
			runtime = end - tmp;
			sleeptime += PERIOD - runtime;
			
		}
		
	}
	
}
