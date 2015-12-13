package agents;

import java.util.ArrayList;
import java.util.Random;

import elements.Element;
import elements.Map;
import elements.MapSpace;
import elements.TaxiStopElement;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import sajas.core.behaviours.OneShotBehaviour;
import sajas.core.behaviours.SimpleBehaviour;
import sajas.domain.DFService;

public class PassengerAgent extends Agent {
	private MapSpace goalSpace; // Current stop the passenger wants to reach. Same as destinationSpace after arriving at first stop
	private MapSpace destinationSpace;
	private boolean madeRequest;

	private class GoToStopBehavior extends SimpleBehaviour {
		private boolean arrived;
		
		public GoToStopBehavior() {
			super(PassengerAgent.this);
			arrived = false;
		}

		@Override
		public void action() {
			MapSpace currentSpace = elementMap.getSpaceAt(x, y);
			MapSpace nextMove = elementMap.getNearestMoveBetween(currentSpace, goalSpace);
			elementMap.moveElement(currentSpace.searchByAgent(PassengerAgent.this), currentSpace, nextMove);
			if (nextMove == goalSpace) {
				arrived = true;
				goalSpace = destinationSpace;
				addBehaviour(new ArrivedAtStopBehavior());
			}
		}

		@Override
		public boolean done() {
			return arrived;
		}
	}
	
	private class ArrivedAtStopBehavior extends OneShotBehaviour {
		public ArrivedAtStopBehavior() {
			super(PassengerAgent.this);
		}
		
		@Override
		public void action() {
			TaxiStopElement currentStop = (TaxiStopElement) elementMap.getSpaceAt(x, y).getStaticElement();
			if (!currentStop.getTaxiQueue().isEmpty()) {
				TaxiAgent firstTaxi = currentStop.getTaxiQueue().remove();
				firstTaxi.setCarriedPassenger(PassengerAgent.this);
				addBehaviour(new InTaxiBehavior());
			}
			else {
				addBehaviour(new RequestTaxiBehavior());
			}
		}
	}
	
	private class InTaxiBehavior extends SimpleBehaviour {
		private boolean arrived;
		
		public InTaxiBehavior() {
			super(PassengerAgent.this);
			arrived = false;
		}
		
		@Override
		public void action() {
			if (x == goalSpace.getStaticElement().getX() && y == goalSpace.getStaticElement().getY()) { // If arrived at destination
				arrived = true;
				MapSpace thisSpace = elementMap.getSpaceAt(x, y);
				Element thisElement = thisSpace.searchByAgent(PassengerAgent.this);
				thisSpace.removeTopElement(thisElement);
				elementMap.getDrawList().remove(thisElement);
				elementMap.createNewPassenger();
				doDelete();
			}
		}
		
		@Override
		public boolean done() {
			return arrived;
		}
	}

	private class RequestTaxiBehavior extends OneShotBehaviour {
		// TODO: Após solicitar, só apanhar o táxi solicitado
		// TODO: Timeout se não receber resposta
		
		public RequestTaxiBehavior() {
			super(PassengerAgent.this);
		}
		
		@Override
		public void action() {
			if (!madeRequest) {
				DFAgentDescription template = new DFAgentDescription();
				ServiceDescription sd1 = new ServiceDescription();
				sd1.setType("TaxiAgent");
				template.addServices(sd1);
				
				try {
					DFAgentDescription[] result = DFService.search(PassengerAgent.this, template);
					ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
					
					for (int i = 0; i < result.length; ++i) {
						msg.addReceiver(result[i].getName());
					}
	
					msg.setContent("Come get me! I'm at (" + x + ", " + y + ")");
					send(msg);
				} catch (FIPAException e) {
					e.printStackTrace();
				}
			}
			else {
				
			}
		}
	}

	public PassengerAgent(int x, int y, Map elementMap) {
		super(x, y, elementMap);
		madeRequest = false;

		ArrayList<TaxiStopElement> taxiStops = elementMap.getTaxiStops();
		ArrayList<MapSpace> stopSpaces = new ArrayList<MapSpace>();
		for (TaxiStopElement taxiStop : taxiStops) {
			stopSpaces.add(elementMap.getSpaceAt(taxiStop.getX(), taxiStop.getY()));
		}

		// Separar em OneShotBehaviour?
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
		
		int maxDistanceToStop = -1;
		for (MapSpace stopSpace : stopSpaces) {
			int distanceToStop = elementMap.getDistanceBetween(goalSpace, stopSpace);
			if (distanceToStop > maxDistanceToStop) {
				destinationSpace = stopSpace;
				maxDistanceToStop = distanceToStop;
			}
			else if (distanceToStop == maxDistanceToStop) {
				destinationSpace = new Random().nextBoolean() ? stopSpace : destinationSpace;
			}
		}
	}

	@Override
	public void setup() {
		super.setup("PassengerAgent");

		addBehaviour(new GoToStopBehavior());
	}
	
	public MapSpace getDestinationSpace() {
		return destinationSpace;
	}
}
