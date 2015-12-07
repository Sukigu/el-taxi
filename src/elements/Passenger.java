package elements;

import java.awt.Image;

import uchicago.src.sim.gui.SimGraphics;

public class Passenger extends MapAgent{
	
	private Image img;
	
	public Passenger(int x, int y, Image img) {
		super(x, y);
		this.img = img;
	}

	@Override
	public void draw(SimGraphics g) {
		g.drawImageToFit(img);
		
	}

}
