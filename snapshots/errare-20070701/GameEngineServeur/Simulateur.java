import java.awt.Dimension;
import java.awt.Point;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

/**
 * 
 * @author Yannick
 *
 */
public class Simulateur {
	public static final long PERIOD = (long)((1.0/60.0)*1E9);
	private GameEngine gameEngine;
	private boolean quit;
	private LinkedList<Area> listOfArea;
	private InterfaceGraphics graphics;
	private boolean play;
	private boolean step;
	public Simulateur(GameEngine gameEngine, InterfaceGraphics graphics) {
		this.gameEngine=gameEngine;
		this.listOfArea=gameEngine.getWorldAreas();
		this.graphics=graphics;
		graphics.setSimulateur(this);
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		InterfaceGraphics interfaceGraphics=new InterfaceGraphics();
		interfaceGraphics.pack();
		interfaceGraphics.setVisible(true);
		interfaceGraphics.displayConsole(true);
		interfaceGraphics.printToConsole("Loading data ... ");
		Area area1=new Area(new Point(0,0),new Point(0,110),new Point(210,110),new Point(210,0));
		Area area2=new Area(new Point(190,0),new Point(190,110),new Point(410,110),new Point(410,0));
		Area area3=new Area(new Point(390,0),new Point(390,110),new Point(610,110),new Point(610,0));
		Area area4=new Area(new Point(590,0),new Point(590,110),new Point(800,110),new Point(800,0));
		
		Area area5=new Area(new Point(0,90),new Point(0,210),new Point(210,210),new Point(210,90));
		Area area6=new Area(new Point(190,90),new Point(190,210),new Point(410,210),new Point(410,90));
		Area area7=new Area(new Point(390,90),new Point(390,210),new Point(610,210),new Point(610,90));
		Area area8=new Area(new Point(590,90),new Point(590,210),new Point(800,210),new Point(800,90));
		
		Area area9=new Area(new Point(0,190),new Point(0,310),new Point(210,310),new Point(210,190));
		Area area10=new Area(new Point(190,190),new Point(190,310),new Point(410,310),new Point(410,190));
		Area area11=new Area(new Point(390,190),new Point(390,310),new Point(610,310),new Point(610,190));
		Area area12=new Area(new Point(590,190),new Point(590,310),new Point(800,310),new Point(800,190));
		
		Area area13=new Area(new Point(0,290),new Point(0,410),new Point(210,410),new Point(210,290));
		Area area14=new Area(new Point(190,290),new Point(190,410),new Point(410,410),new Point(410,290));
		Area area15=new Area(new Point(390,290),new Point(390,410),new Point(610,410),new Point(610,290));
		Area area16=new Area(new Point(590,290),new Point(590,410),new Point(800,410),new Point(800,290));
		
		Area area17=new Area(new Point(0,390),new Point(0,510),new Point(210,510),new Point(210,390));
		Area area18=new Area(new Point(190,390),new Point(190,510),new Point(410,510),new Point(410,390));
		Area area19=new Area(new Point(390,390),new Point(390,510),new Point(610,510),new Point(610,390));
		Area area20=new Area(new Point(590,390),new Point(590,510),new Point(800,510),new Point(800,390));
		
		Area area21=new Area(new Point(0,490),new Point(0,610),new Point(210,610),new Point(210,490));
		Area area22=new Area(new Point(190,490),new Point(190,610),new Point(410,610),new Point(410,490));
		Area area23=new Area(new Point(390,490),new Point(390,610),new Point(610,610),new Point(610,490));
		Area area24=new Area(new Point(590,490),new Point(590,610),new Point(800,610),new Point(800,490));
		
		Area area25=new Area(new Point(0,590),new Point(0,710),new Point(210,710),new Point(210,590));
		Area area26=new Area(new Point(190,590),new Point(190,710),new Point(410,710),new Point(410,590));
		Area area27=new Area(new Point(390,590),new Point(390,710),new Point(610,710),new Point(610,590));
		Area area28=new Area(new Point(590,590),new Point(590,710),new Point(800,710),new Point(800,590));
		
		Area area29=new Area(new Point(0,690),new Point(0,810),new Point(210,810),new Point(210,690));
		Area area30=new Area(new Point(190,690),new Point(190,810),new Point(410,810),new Point(410,690));
		Area area31=new Area(new Point(390,690),new Point(390,810),new Point(610,810),new Point(610,690));
		Area area32=new Area(new Point(590,690),new Point(590,810),new Point(800,810),new Point(800,690));
		
		Area area33=new Area(new Point(0,790),new Point(0,900),new Point(210,900),new Point(210,790));
		Area area34=new Area(new Point(190,790),new Point(190,900),new Point(410,900),new Point(410,790));
		Area area35=new Area(new Point(390,790),new Point(390,900),new Point(610,900),new Point(610,790));
		Area area36=new Area(new Point(590,790),new Point(590,900),new Point(800,900),new Point(800,790));
		/***** Premier ligne *****/
		area1.addAroundArea(area5); /* bas */
		area1.addAroundArea(area2); /* droite */
		area1.addAroundArea(area6); /* angle bas droite */
		
		area2.addAroundArea(area6); /* bas */
		area2.addAroundArea(area1); /* gauche */
		area2.addAroundArea(area3); /* droite */
		area2.addAroundArea(area5); /* angle bas gauche */
		area2.addAroundArea(area7); /* angle bas droite */

		area3.addAroundArea(area7); /* bas */
		area3.addAroundArea(area2); /* gauche */
		area3.addAroundArea(area4); /* droite */
		area3.addAroundArea(area6); /* angle bas gauche */
		area3.addAroundArea(area8); /* angle bas droite */
		
		area4.addAroundArea(area8); /* bas */
		area4.addAroundArea(area3); /* gauche */
		area4.addAroundArea(area7); /* angle bas gauche */
		
		/***** Deuxieme ligne ****/
		area5.addAroundArea(area1); /* haut */
		area5.addAroundArea(area9); /* bas */
		area5.addAroundArea(area6); /* droite */
		area5.addAroundArea(area2); /* angle haut droite */
		area5.addAroundArea(area10); /* angle bas droite */
		
		area6.addAroundArea(area2); /* haut */
		area6.addAroundArea(area10); /* bas */
		area6.addAroundArea(area5); /* gauche */
		area6.addAroundArea(area7); /* droite */
		area6.addAroundArea(area1); /* angle haut gauche */ 
		area6.addAroundArea(area3); /* angle haut droite */
		area6.addAroundArea(area9); /* angle bas gauche */
		area6.addAroundArea(area11); /* angle bas droite */
				
		area7.addAroundArea(area3); /* haut */
		area7.addAroundArea(area11); /* bas */
		area7.addAroundArea(area6); /* gauche */
		area7.addAroundArea(area8); /* droite */
		area7.addAroundArea(area2); /* angle haut gauche */ 
		area7.addAroundArea(area4); /* angle haut droite */
		area7.addAroundArea(area10); /* angle bas gauche */
		area7.addAroundArea(area12); /* angle bas droite */
		
		area8.addAroundArea(area4); /* haut */
		area8.addAroundArea(area12); /* bas */
		area8.addAroundArea(area7); /* gauche */
		area8.addAroundArea(area3); /* angle haut gauche */ 
		area8.addAroundArea(area11); /* angle bas gauche */


		/**** Troisieme ligne ****/
		area9.addAroundArea(area5); /* haut */
		area9.addAroundArea(area13); /* bas */
		area9.addAroundArea(area10); /* droite */
		area9.addAroundArea(area6); /* angle haut droite */
		area9.addAroundArea(area14); /* angle bas droite */
		
		area10.addAroundArea(area6); /* haut */
		area10.addAroundArea(area14); /* bas */
		area10.addAroundArea(area9); /* gauche */
		area10.addAroundArea(area11); /* droite */
		area10.addAroundArea(area5); /* angle haut gauche */ 
		area10.addAroundArea(area7); /* angle haut droite */
		area10.addAroundArea(area13); /* angle bas gauche */
		area10.addAroundArea(area15); /* angle bas droite */
		
		area11.addAroundArea(area7); /* haut */
		area11.addAroundArea(area15); /* bas */
		area11.addAroundArea(area10); /* gauche */
		area11.addAroundArea(area12); /* droite */
		area11.addAroundArea(area6); /* angle haut gauche */ 
		area11.addAroundArea(area8); /* angle haut droite */
		area11.addAroundArea(area14); /* angle bas gauche */
		area11.addAroundArea(area16); /* angle bas droite */
	
		area12.addAroundArea(area8); /* haut */
		area12.addAroundArea(area16); /* bas */
		area12.addAroundArea(area11); /* gauche */
		area12.addAroundArea(area7); /* angle haut gauche */
		area12.addAroundArea(area15); /* angle bas gauche */
		
		/***** Quatrieme ligne ****/
		area13.addAroundArea(area9); /* haut */
		area13.addAroundArea(area17); /* bas */
		area13.addAroundArea(area14); /* droite */
		area13.addAroundArea(area10); /* angle haut droite */
		area13.addAroundArea(area18); /* angle bas droite */
		
		area14.addAroundArea(area10); /* haut */
		area14.addAroundArea(area18); /* bas */
		area14.addAroundArea(area13); /* gauche */
		area14.addAroundArea(area15); /* droite */
		area14.addAroundArea(area9); /* angle haut gauche */ 
		area14.addAroundArea(area11); /* angle haut droite */
		area14.addAroundArea(area17); /* angle bas gauche */
		area14.addAroundArea(area19); /* angle bas droite */
				
		area15.addAroundArea(area11); /* haut */
		area15.addAroundArea(area19); /* bas */
		area15.addAroundArea(area14); /* gauche */
		area15.addAroundArea(area16); /* droite */
		area15.addAroundArea(area10); /* angle haut gauche */ 
		area15.addAroundArea(area12); /* angle haut droite */
		area15.addAroundArea(area18); /* angle bas gauche */
		area15.addAroundArea(area20); /* angle bas droite */
		
		area16.addAroundArea(area12); /* haut */
		area16.addAroundArea(area20); /* bas */
		area16.addAroundArea(area15); /* gauche */
		area16.addAroundArea(area11); /* angle haut gauche */ 
		area16.addAroundArea(area19); /* angle bas gauche */
		
		/***** Cinquieme ligne ****/
		area17.addAroundArea(area13); /* haut */
		area17.addAroundArea(area21); /* bas */
		area17.addAroundArea(area18); /* droite */
		area17.addAroundArea(area14); /* angle haut droite */
		area17.addAroundArea(area22); /* angle bas droite */
		
		area18.addAroundArea(area14); /* haut */
		area18.addAroundArea(area22); /* bas */
		area18.addAroundArea(area17); /* gauche */
		area18.addAroundArea(area19); /* droite */
		area18.addAroundArea(area13); /* angle haut gauche */ 
		area18.addAroundArea(area15); /* angle haut droite */
		area18.addAroundArea(area21); /* angle bas gauche */
		area18.addAroundArea(area23); /* angle bas droite */
				
		area19.addAroundArea(area15); /* haut */
		area19.addAroundArea(area23); /* bas */
		area19.addAroundArea(area18); /* gauche */
		area19.addAroundArea(area20); /* droite */
		area19.addAroundArea(area14); /* angle haut gauche */ 
		area19.addAroundArea(area16); /* angle haut droite */
		area19.addAroundArea(area22); /* angle bas gauche */
		area19.addAroundArea(area24); /* angle bas droite */
		
		area20.addAroundArea(area16); /* haut */
		area20.addAroundArea(area24); /* bas */
		area20.addAroundArea(area19); /* gauche */
		area20.addAroundArea(area15); /* angle haut gauche */ 
		area20.addAroundArea(area23); /* angle bas gauche */
		
		/***** Sixieme ligne ******/
		area21.addAroundArea(area17); /* haut */
		area21.addAroundArea(area25); /* bas */
		area21.addAroundArea(area22); /* droite */
		area21.addAroundArea(area18); /* angle haut droite */
		area21.addAroundArea(area26); /* angle bas droite */
		
		area22.addAroundArea(area18); /* haut */
		area22.addAroundArea(area26); /* bas */
		area22.addAroundArea(area21); /* gauche */
		area22.addAroundArea(area23); /* droite */
		area22.addAroundArea(area17); /* angle haut gauche */ 
		area22.addAroundArea(area19); /* angle haut droite */
		area22.addAroundArea(area25); /* angle bas gauche */
		area22.addAroundArea(area27); /* angle bas droite */
				
		area23.addAroundArea(area19); /* haut */
		area23.addAroundArea(area27); /* bas */
		area23.addAroundArea(area22); /* gauche */
		area23.addAroundArea(area24); /* droite */
		area23.addAroundArea(area18); /* angle haut gauche */ 
		area23.addAroundArea(area20); /* angle haut droite */
		area23.addAroundArea(area26); /* angle bas gauche */
		area23.addAroundArea(area28); /* angle bas droite */
		
		area24.addAroundArea(area20); /* haut */
		area24.addAroundArea(area28); /* bas */
		area24.addAroundArea(area23); /* gauche */
		area24.addAroundArea(area19); /* angle haut gauche */ 
		area24.addAroundArea(area27); /* angle bas gauche */
		
		/***** Septieme ligne *****/
		area25.addAroundArea(area21); /* haut */
		area25.addAroundArea(area29); /* bas */
		area25.addAroundArea(area26); /* droite */
		area25.addAroundArea(area18); /* angle haut droite */
		area25.addAroundArea(area26); /* angle bas droite */
		
		area26.addAroundArea(area22); /* haut */
		area26.addAroundArea(area30); /* bas */
		area26.addAroundArea(area25); /* gauche */
		area26.addAroundArea(area27); /* droite */
		area26.addAroundArea(area21); /* angle haut gauche */ 
		area26.addAroundArea(area23); /* angle haut droite */
		area26.addAroundArea(area25); /* angle bas gauche */
		area26.addAroundArea(area27); /* angle bas droite */
		
		area27.addAroundArea(area23); /* haut */
		area27.addAroundArea(area31); /* bas */
		area27.addAroundArea(area26); /* gauche */
		area27.addAroundArea(area28); /* droite */
		area27.addAroundArea(area22); /* angle haut gauche */ 
		area27.addAroundArea(area24); /* angle haut droite */
		area27.addAroundArea(area30); /* angle bas gauche */
		area27.addAroundArea(area32); /* angle bas droite */
		
		area28.addAroundArea(area24); /* haut */
		area28.addAroundArea(area32); /* bas */
		area28.addAroundArea(area27); /* gauche */
		area28.addAroundArea(area23); /* angle haut gauche */ 
		area28.addAroundArea(area31); /* angle bas gauche */
		
		/***** huitieme ligne *****/
		area29.addAroundArea(area25); /* haut */
		area29.addAroundArea(area33); /* bas */
		area29.addAroundArea(area30); /* droite */
		area29.addAroundArea(area26); /* angle haut droite */
		area29.addAroundArea(area34); /* angle bas droite */
		
		area30.addAroundArea(area26); /* haut */
		area30.addAroundArea(area34); /* bas */
		area30.addAroundArea(area29); /* gauche */
		area30.addAroundArea(area31); /* droite */
		area30.addAroundArea(area25); /* angle haut gauche */ 
		area30.addAroundArea(area27); /* angle haut droite */
		area30.addAroundArea(area33); /* angle bas gauche */
		area30.addAroundArea(area35); /* angle bas droite */
		
		area31.addAroundArea(area27); /* haut */
		area31.addAroundArea(area35); /* bas */
		area31.addAroundArea(area30); /* gauche */
		area31.addAroundArea(area32); /* droite */
		area31.addAroundArea(area26); /* angle haut gauche */ 
		area31.addAroundArea(area28); /* angle haut droite */
		area31.addAroundArea(area34); /* angle bas gauche */
		area31.addAroundArea(area36); /* angle bas droite */
		
		area32.addAroundArea(area28); /* haut */
		area32.addAroundArea(area36); /* bas */
		area32.addAroundArea(area31); /* gauche */
		area32.addAroundArea(area27); /* angle haut gauche */ 
		area32.addAroundArea(area35); /* angle bas gauche */
		
		/***** neuvieme ligne *****/
		area33.addAroundArea(area29); /* haut */
		area33.addAroundArea(area34); /* droite */
		area33.addAroundArea(area30); /* angle haut droite */
		
		area34.addAroundArea(area30); /* haut */
		area34.addAroundArea(area29); /* gauche */
		area34.addAroundArea(area35); /* droite */
		area34.addAroundArea(area29); /* angle haut gauche */
		area34.addAroundArea(area31); /* angle haut droit */
		
		area35.addAroundArea(area31); /* haut */
		area35.addAroundArea(area34); /* gauche */
		area35.addAroundArea(area36); /* droite */
		area35.addAroundArea(area30); /* angle haut gauche */
		area35.addAroundArea(area32); /* angle haut droit */
		
		area36.addAroundArea(area32); /* haut */
		area36.addAroundArea(area35); /* gauche */
		area36.addAroundArea(area31); /* angle haut gauche */
		
		/////////////////////
		LinkedList<Area> listOfArea=new LinkedList<Area>();
		listOfArea.add(area1);
		listOfArea.add(area2);
		listOfArea.add(area3);
		listOfArea.add(area4);
		listOfArea.add(area5);
		listOfArea.add(area6);
		listOfArea.add(area7);
		listOfArea.add(area8);
		listOfArea.add(area9);
		listOfArea.add(area10);
		listOfArea.add(area11);
		listOfArea.add(area12);
		listOfArea.add(area13);
		listOfArea.add(area14);
		listOfArea.add(area15);
		listOfArea.add(area16);
		listOfArea.add(area17);
		listOfArea.add(area18);
		listOfArea.add(area19);
		listOfArea.add(area20);
		listOfArea.add(area21);
		listOfArea.add(area22);
		listOfArea.add(area23);
		listOfArea.add(area24);
		listOfArea.add(area25);
		listOfArea.add(area26);
		listOfArea.add(area27);
		listOfArea.add(area28);
		listOfArea.add(area29);
		listOfArea.add(area30);
		listOfArea.add(area31);
		listOfArea.add(area32);
		listOfArea.add(area33);
		listOfArea.add(area34);
		listOfArea.add(area35);
		listOfArea.add(area36);
		interfaceGraphics.printToConsole("Data loaded at "+new Date(System.currentTimeMillis()));
		interfaceGraphics.printToConsole("Create gameEngine ...");
		GameEngine gameEngine=new GameEngine(listOfArea);
		interfaceGraphics.printToConsole("gameEngine created at "+new Date(System.currentTimeMillis()));
		interfaceGraphics.printToConsole("Create simutator ...");
		Simulateur simulateur=new Simulateur(gameEngine,interfaceGraphics);
		interfaceGraphics.displayConsole(false);
		interfaceGraphics.setPreferredSize(new Dimension(1000,800));
		interfaceGraphics.pack();
		simulateur.run();
	}
	public void run(){
		graphics.updateData(gameEngine);
		graphics.printToConsole("Simulation ready at "+new Date(System.currentTimeMillis()).toString());
		graphics.printToConsole("______________________________________________________________");
		long begin, end;
		long tmp=0;
		long sleeptime=0;
		long runtime=0;
		begin = System.nanoTime();
		while(!quit){
			if(play || step){
				try{
					gameEngine.run();
					graphics.updateData(gameEngine);
				}catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					graphics.printToConsole("Simulation crash :"+e+" =>");
					for(StackTraceElement s:e.getStackTrace()){
						graphics.printToConsole("\t"+s.toString());
					}
				}
			}
			step=false;
			if(sleeptime>0) {
				try {
					Thread.sleep((long)(sleeptime/1000000), (int)(sleeptime%1000000));
				} catch (InterruptedException ex) {}
			}
			tmp = begin;
			begin = System.nanoTime();
			end = System.nanoTime();
			runtime = end - tmp;
			sleeptime += PERIOD - runtime;
		}
	}
	public void play() {
		play=true;
	}
	public void pause() {
		play=false;
	}
	public void step() {
		step=true;
	}
}
