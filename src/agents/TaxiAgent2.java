package agents;

import java.util.ArrayList;
import java.util.Random;

import elements.Map;
import elements.MapSpace;
import elements.TaxiStopElement;
import sajas.core.behaviours.OneShotBehaviour;

public class TaxiAgent2 extends TaxiAgent {
	private class IdleBehavior extends OneShotBehaviour {
		public IdleBehavior() {
			super(TaxiAgent2.this);
		}

		@Override
		public void action() {
			ArrayList<TaxiStopElement> taxiStops = elementMap.getTaxiStops();
			ArrayList<MapSpace> stopSpaces = new ArrayList<MapSpace>();
			for (TaxiStopElement taxiStop : taxiStops) {
				stopSpaces.add(elementMap.getSpaceAt(taxiStop.getX(), taxiStop.getY()));
			}
			
			int minDistanceToStop = Integer.MAX_VALUE;
			for (MapSpace stopSpace : stopSpaces) {
				int distanceToStop = elementMap.getDistanceBetween(elementMap.getSpaceAt(x, y), stopSpace);
				if (distanceToStop < minDistanceToStop) {
					goalSpace = stopSpace;
					minDistanceToStop = distanceToStop;
				}
				else if (distanceToStop == minDistanceToStop) {
					goalSpace = new Random().nextBoolean() ? stopSpace : goalSpace;
				}
			}
		}
	}
	
	public TaxiAgent2(int x, int y, Map elementMap) {
		super(x, y, elementMap);
	}

	@Override
	public void setup() {
		super.setup();
		addBehaviour(new IdleBehavior());
	}
}
