package elements;

import sajas.core.Agent;
import uchicago.src.sim.gui.Drawable;

public abstract class MapAgent extends Agent implements Drawable{
	private int x, y;
	
	public MapAgent(int x, int y) {
		this.x = x;
		this.y = y;
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
