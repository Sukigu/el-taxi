package elements;

import java.awt.Image;

import uchicago.src.sim.gui.SimGraphics;
import uchicago.src.sim.space.Object2DGrid;

public class Stop extends MapElement{

	public Stop(int x, int y, Object2DGrid map, Image img) {
		super(x, y, map, img);
	}

	@Override
	public void step() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(SimGraphics graphics) {
		graphics.drawImageToFit(img);
		
	}

}
