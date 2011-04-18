package geom;

import java.awt.Color;
import java.io.Serializable;

public class Debug implements Serializable {
	
	private static final long serialVersionUID = 1629417097800802346L;
	
	private boolean visible;
	private Color color;
	private boolean transparent;
	private boolean wire;
	
	public Debug() {
		visible = false;
		color = Color.WHITE;
		transparent = false;
		wire = false;
	}
	
	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}
	public boolean isTransparent() {
		return transparent;
	}
	public void setTransparent(boolean transparent) {
		this.transparent = transparent;
	}
	public boolean isVisible() {
		return visible;
	}
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	public boolean isWire() {
		return wire;
	}
	public void setWire(boolean wire) {
		this.wire = wire;
	}
	
	

}
