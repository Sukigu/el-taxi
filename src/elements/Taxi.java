package elements;

import java.awt.Color;

import uchicago.src.sim.gui.SimGraphics;

public class Taxi extends MapAgent {
	public Taxi(int x, int y) {
		super(x, y);
	}

	@Override
	public void draw(SimGraphics g) {
		g.drawString("Taxi", new Color(255, 255, 255));
	}
}
