package agents;

import java.util.Random;

import elements.Map;
import elements.TaxiStopElement;
import sajas.core.behaviours.CyclicBehaviour;

public class TaxiAgent1 extends TaxiAgent {
	private final TaxiStopElement homeStop;

	private class IdleBehavior extends CyclicBehaviour {
		public IdleBehavior() {
			super(TaxiAgent1.this);
		}

		@Override
		public void action() {
			if (goalSpace == null) {
				if (carriedPassenger == null) goalSpace = elementMap.getSpaceAt(homeStop.getX(), homeStop.getY());
				else goalSpace = carriedPassenger.getDestinationSpace();
			}
		}
	}

	public TaxiAgent1(int x, int y, Map elementMap) {
		super(x, y, elementMap);

		int numOfTaxiStops = elementMap.getTaxiStops().size();
		homeStop = elementMap.getTaxiStops().get(new Random().nextInt(numOfTaxiStops));
	}

	@Override
	public void setup() {
		super.setup();
		addBehaviour(new IdleBehavior());
	}
}
