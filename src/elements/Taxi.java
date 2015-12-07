package elements;
import java.awt.Image;

import sajas.core.behaviours.SimpleBehaviour;
import uchicago.src.sim.gui.SimGraphics;

public class Taxi extends MapAgent {
	private Image img;
	private class TaxiBehavior extends SimpleBehaviour {
		@Override
		public void action() {
		//	System.out.println("¡Le voy a hacer la historia a Pitbull y a Sensato pa' que la conozcan!");
		}

		@Override
		public boolean done() {
			return false;
		}
	}
	
	public Taxi(int x, int y, Image img) {
		super(x, y);
		this.img = img;
		
		addBehaviour(new TaxiBehavior());
	}

	@Override
	public void draw(SimGraphics g) {
		g.drawImageToFit(img);
	}
}
