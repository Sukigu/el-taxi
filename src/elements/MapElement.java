package elements;

import java.awt.Color;

import uchicago.src.sim.engine.Stepable;
import uchicago.src.sim.gui.Drawable;
import uchicago.src.sim.gui.SimGraphics;

public abstract class MapElement implements Drawable, Stepable {
	protected int x, y;
	
	public MapElement(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public void step() {
	}

	@Override
	public void draw(SimGraphics g) {
		g.drawString("Taxi", new Color(1, 1, 1));
	}

	@Override
	public int getX() {
		return x;
	}

	@Override
	public int getY() {
		return y;
	}

	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
}
