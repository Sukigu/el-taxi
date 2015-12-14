package agents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import elements.Element;
import elements.Map;
import elements.MapSpace;
import elements.TaxiStopElement;
import jade.core.AID;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import sajas.core.behaviours.OneShotBehaviour;
import sajas.core.behaviours.SimpleBehaviour;
import sajas.domain.DFService;

public class PassengerAgent extends Agent {
	private static int id = -1;
	private MapSpace goalSpace; // Current stop the passenger wants to reach. Same as destinationSpace after arriving at first stop
	private MapSpace destinationSpace;
	private TaxiAgent myTaxi;

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
				myTaxi = firstTaxi;
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
				myTaxi.setCarriedPassenger(null);
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
	
	private class RequestTaxiBehavior extends SimpleBehaviour {
		private boolean madeRequest;
		
		private HashMap<AID, int[]> responses;
		boolean noMoreReading;
		boolean done;
		
		public RequestTaxiBehavior() {
			super(PassengerAgent.this);
			madeRequest = false;
			responses = new HashMap<AID, int[]>();
			noMoreReading = false;
			done = false;
		}
		
		@Override
		public void action() {
			if (!madeRequest) {
				madeRequest = true;
				
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
	
					msg.setContent("Where are you?");
					send(msg);
                    System.out.println("[" + getLocalName() + "] Sent \"" + msg.getContent() + "\"");
				} catch (FIPAException e) {
					e.printStackTrace();
				}
				
				try {
					System.out.println("Zzzz...");
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			else {
				if (!noMoreReading) {
					ACLMessage msg = receive();
		            if (msg != null) {
		            	if(msg.getContent().matches("^I'm at \\((\\d+), (\\d+)\\)\\.$")) {
			            	int[] coords = new int[2];
			            	
			            	Pattern p = Pattern.compile("^I'm at \\((\\d+), (\\d+)\\)\\.$");
			            	Matcher m = p.matcher(msg.getContent());
			            	m.find();
			            	coords[0] = Integer.parseInt(m.group(1));
			            	coords[1] = Integer.parseInt(m.group(2));
			            	
			            	responses.put(msg.getSender(), coords);
		            	}
		            }
		            else noMoreReading = true;
				}
				else { // No more messages to read
					if (responses.size() == 0) {
						madeRequest = false; // Request again
						noMoreReading = false;
					}
					else {
						int minDistance = Integer.MAX_VALUE;
						AID chosenTaxi = null;
						for (AID response : responses.keySet()) {
							int distance = elementMap.getDistanceBetween(responses.get(response)[0], responses.get(response)[1], destinationSpace);
							if (distance < minDistance) {
								chosenTaxi = response;
								minDistance = distance;
							}
						}
						
						ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
						msg.addReceiver(chosenTaxi);
						msg.setContent("Come get me! I'm at (" + x + ", " + y + ")");
						send(msg);
	                    System.out.println("[" + getLocalName() + "] Sent \"" + msg.getContent() + "\" to " + chosenTaxi.getLocalName());
						
						addBehaviour(new WaitingAtStopBehavior());
						done = true;
					}
				}
			}
		}
		
		@Override
		public boolean done() {
			return done;
		}
	}
	
	private class WaitingAtStopBehavior extends SimpleBehaviour {
		private boolean done;
		
		public WaitingAtStopBehavior() {
			super(PassengerAgent.this);
			done = false;
		}
		
		@Override
		public void action() {
			TaxiStopElement currentStop = (TaxiStopElement) elementMap.getSpaceAt(x, y).getStaticElement();
			if (!currentStop.getTaxiQueue().isEmpty()) {
				TaxiAgent firstTaxi = currentStop.getTaxiQueue().remove();
				firstTaxi.setCarriedPassenger(PassengerAgent.this);
				myTaxi = firstTaxi;
				addBehaviour(new InTaxiBehavior());
				done = true;
			}
		}
		
		@Override
		public boolean done() {
			return done;
		}
		
		// TODO: Refazer o pedido caso ninguém venha?
	}

	public PassengerAgent(int x, int y, Map elementMap) {
		super(x, y, elementMap);
		++id;
		myTaxi = null;

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
	
	public static int getLastId() {
		return id;
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
