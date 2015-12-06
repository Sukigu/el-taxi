package elements;

import java.awt.Color;

import sajas.core.behaviours.SimpleBehaviour;
import uchicago.src.sim.gui.SimGraphics;

public class Taxi extends MapAgent {
	private class TaxiBehavior extends SimpleBehaviour {
		@Override
		public void action() {
			System.out.println("¡Le voy a hacer la historia a Pitbull y a Sensato pa' que la conozcan!");
		}

		@Override
		public boolean done() {
			return false;
		}
	}
	
	public Taxi(int x, int y) {
		super(x, y);
		
		addBehaviour(new TaxiBehavior());
	}

	@Override
	public void draw(SimGraphics g) {
		g.drawString("Taxi", new Color(255, 255, 255));
	}
}
