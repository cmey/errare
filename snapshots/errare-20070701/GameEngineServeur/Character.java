import java.awt.Point;
import java.util.LinkedList;


public class Character {
	private int x;
	private int y;
	private int z;
	public Character(int x,int y,int z){
		this.x=x;
		this.y=y;
		this.z=z;
	}
	/**
	 * this methode permit to interact in the area
	 *
	 */
	public void interact() {
		
	}
	/**
	 * This methode get y position
	 * @return
	 */
	public int getY() {
		return y;
	}
	/**
	 * This methode get x position
	 * @return
	 */
	public int getX() {
		return x;
	}
	/**
	 * This methode get z position
	 * @return
	 */
	public int getZ() {
		return z;
	}
	public void setX(int x) {
		this.x = x;
	}
	public void setY(int y) {
		this.y = y;
	}
	public void setZ(int z) {
		this.z = z;
	}
}
