import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.LinkedList;

import javax.swing.JPanel;

public class DisplayMap2D extends JPanel{
	private LinkedList<Area> areas;
	private boolean showArea;
	private boolean showActiveArea;
	private Color drawActiveArea, drawArea, drawPlayer,drawOtherChar;
	private double factor;
	private int XPos;
	private int YPos;
	private boolean displayXYActiveArea;
	public DisplayMap2D() {
		XPos=0;
		YPos=0;
		factor=1;
		this.areas=new LinkedList<Area>();
		this.drawActiveArea=Color.RED;
		this.drawArea=Color.BLACK;
		this.drawOtherChar=Color.GRAY;
		this.drawPlayer=Color.BLUE;
		this.setCursor(new Cursor(Cursor.MOVE_CURSOR));
		this.addMouseMotionListener(
				new MouseMotionListener(){
					Point p=new Point();
					public void mouseDragged(MouseEvent e) {
						XPos-=p.x-e.getPoint().x;
						YPos-=p.y-e.getPoint().y;
						p=e.getPoint();
						DisplayMap2D.this.repaint();
					}
					public void mouseMoved(MouseEvent e) {
						p=e.getPoint();
					}
		});
		showArea=true;
		showActiveArea=true;
		displayXYActiveArea=false;
	}
	/**
	 * This methode permit to update Area form 2D map
	 * @param linkedList
	 */
	public void updateAreas(LinkedList<Area> linkedList){
		this.areas=linkedList;
	}
//	/**
//	 * This methode permit to update noPlayer Charactere form 2D map
//	 * @param linkedList
//	 */
//	public void updateCharacters(LinkedList<Character> linkedList){
//		this.noPlayerCharactere=linkedList;
//	}
//	/**
//	 * This methode permit to update Player from 2D map
//	 * @param linkedList
//	 */
//	public void updatePlayers(LinkedList<Player> linkedList, Player selected){
//		this.players=linkedList;
//		this.selected=selected;
//	}
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		//	 Copy de la liste pour travailler avec 
		LinkedList<Area> workingListArea=(LinkedList<Area>) areas.clone();
		for(Area a:workingListArea){
			Polygon polygon=new Polygon();
			int[] x=a.getPolygone().xpoints;
			int[] y=a.getPolygone().ypoints;
			int fin=a.getPolygone().npoints;
			for(int i=0;i<fin;i++){
				polygon.addPoint((int)(x[i]*factor+XPos),(int)( y[i]*factor+YPos));
				char [] cs=new String(x[i]+","+y[i]).toCharArray();
				if(displayXYActiveArea){
					graphics.drawChars(cs,0, cs.length, (int)(x[i]*factor+XPos)+5, (int)(y[i]*factor+YPos)+15);
					graphics.drawLine((int)(x[i]*factor+5+XPos),(int)( y[i]*factor+5+YPos),(int)( x[i]*factor-5+XPos), (int)(y[i]*factor-5+YPos));
					graphics.drawLine((int)(x[i]*factor+5+XPos),(int)( y[i]*factor-5+YPos),(int)( x[i]*factor-5+XPos), (int)(y[i]*factor+5+YPos));
				}
			}
			if(showActiveArea && a.isActive()){
				graphics.setColor(drawActiveArea);
				graphics.fillPolygon(polygon);
				// Copy de la liste pour travailler avec 
				LinkedList<Player> workingListPlayer=(LinkedList<Player>) a.getPlayers().clone();
				for(Player player:workingListPlayer){
					graphics.setColor(drawPlayer);
					char [] cs=new String("("+player.getX()+","+player.getY()+") ["+player.getName()+"]").toCharArray();
					graphics.drawChars(cs,0, cs.length, (int)(player.getX()*factor+XPos)+5, (int)(player.getY()*factor+YPos)+15);
					graphics.fillOval((int)(player.getX()*factor)-5+XPos,(int)(player.getY()*factor)-5+YPos, 10,10);
				}
			}else if(showArea){
				graphics.setColor(drawArea);
				graphics.drawPolygon(polygon);
			}
		}
		graphics.setColor(Color.BLACK);
		char [] cs={'0',',','0'};
		graphics.drawChars(cs,0, 3, 0+XPos+5, 0+YPos+15);
		/*
		 * placement des Characters (player, monstre non joueur);
		 */
		for(Area a:workingListArea){
			// Copy de la liste pour travailler avec 
			LinkedList<Player> workingListPlayer=(LinkedList<Player>) a.getPlayers().clone();
			for(Player player:workingListPlayer){
				graphics.setColor(drawPlayer);
				cs=new String("("+player.getX()+","+player.getY()+") ["+player.getName()+"]").toCharArray();
				graphics.drawChars(cs,0, cs.length, (int)(player.getX()*factor+XPos)+5, (int)(player.getY()*factor+YPos)+15);
				graphics.fillOval((int)(player.getX()*factor)-5+XPos,(int)(player.getY()*factor)-5+YPos, 10,10);
			}
			// Copy de la liste pour travailler avec 
			LinkedList<Character> workingListInactiveChar=(LinkedList<Character>) a.getInactiveCharacter().clone();
			for(Character character:workingListInactiveChar){
				graphics.setColor(drawOtherChar);
				cs=new String("("+character.getX()+","+character.getY()+") [inactive]").toCharArray();
				graphics.drawChars(cs,0, cs.length, (int)(character.getX()*factor+XPos)+5, (int)(character.getY()*factor+YPos)+15);
				graphics.fillOval((int)(character.getX()*factor)-5+XPos,(int)(character.getY()*factor)-5+YPos, 10,10);
			}
			// Copy de la liste pour travailler avec 
			LinkedList<Character> workingListActiveChar=(LinkedList<Character>) a.getActiveCharacter().clone();
			for(Character character:workingListActiveChar){
				graphics.setColor(drawOtherChar);
				cs=new String("("+character.getX()+","+character.getY()+") [active]").toCharArray();
				graphics.drawChars(cs,0, cs.length, (int)(character.getX()*factor+XPos)+5, (int)(character.getY()*factor+YPos)+15);
				graphics.fillOval((int)(character.getX()*factor)-5+XPos,(int)(character.getY()*factor)-5+YPos, 10,10);
			}
		}
	}
	public void increaseFactor(){
		factor*=1.5;
		System.out.println("new factor :"+factor);
	}
	public void reduceFactor(){
		factor*=2.0/3.0;
		System.out.println("new factor :"+factor);
	}
	public int getXPos() {
		return XPos;
	}
	public int getYPos() {
		return YPos;
	}
	public double getFactor() {
		return factor;
	}
}
