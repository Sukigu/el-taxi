package elements;

import java.awt.Image;

import uchicago.src.sim.gui.Drawable;
import uchicago.src.sim.gui.SimGraphics;

public abstract class Element implements Drawable {
	protected int x;
	protected int y;
	
	public Element (int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	protected abstract Image getImg();
	
	@Override
	public final void draw(SimGraphics graphics) {
		graphics.drawImageToFit(getImg());
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
