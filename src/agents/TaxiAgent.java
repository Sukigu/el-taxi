package agents;

import java.util.ArrayList;
import java.util.Random;

import elements.Element;
import elements.Map;
import elements.MapSpace;
import sajas.core.behaviours.CyclicBehaviour;

public class TaxiAgent extends Agent {
	private class TaxiBehavior extends CyclicBehaviour {
		public TaxiBehavior(Agent a) {
			super(a);
		}
		
		@Override
		public void action() {
			MapSpace currentSpace = elementMap.getSpaceAt(x, y);
			ArrayList<MapSpace> possibleNextMoves = elementMap.getPossibleMovesFrom(currentSpace);
			int selectedMoveIndex = new Random().nextInt(possibleNextMoves.size());
			Element selectedMoveStaticElement = possibleNextMoves.get(selectedMoveIndex).getStaticElement();
			MapSpace selectedMove = elementMap.getSpaceAt(selectedMoveStaticElement.getX(), selectedMoveStaticElement.getY());
			
			Element thisElement = currentSpace.searchByAgent(TaxiAgent.this);
			currentSpace.removeTopElement(thisElement);
			thisElement.setX(selectedMove.getStaticElement().getX());
			thisElement.setY(selectedMove.getStaticElement().getY());
			selectedMove.addTopElement(thisElement);
		}
	}
	
	public TaxiAgent(int x, int y, Map elementMap) {
		super(x, y, elementMap);
	}

	@Override
	public void setup() {
		super.setup("TaxiAgent");

		addBehaviour(new TaxiBehavior(this));
	}
}
