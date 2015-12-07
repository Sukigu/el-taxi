package elements;

import java.awt.Image;

import uchicago.src.sim.engine.Stepable;
import uchicago.src.sim.gui.Drawable;
import uchicago.src.sim.gui.SimGraphics;
import uchicago.src.sim.space.Object2DGrid;

public abstract class MapElement implements Stepable, Drawable {
	
	protected int x;
	protected int y;
	protected Object2DGrid map;
	protected Image img;
	
	public MapElement (int x, int y, Object2DGrid map, Image img){
		this.x = x;
		this.y = y;
		this.map = map;
		this.img = img;
	}
	
	@Override
	public abstract void draw(SimGraphics graphics);

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
}
