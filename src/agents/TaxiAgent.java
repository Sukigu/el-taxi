package agents;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import elements.Map;
import elements.MapSpace;
import elements.PassengerElement;
import elements.TaxiStopElement;
import jade.lang.acl.ACLMessage;
import sajas.core.behaviours.CyclicBehaviour;

public abstract class TaxiAgent extends Agent {
	protected MapSpace goalSpace;
	protected PassengerAgent carriedPassenger;
	protected boolean requested;

	private class GoToGoalBehavior extends CyclicBehaviour {
		public GoToGoalBehavior() {
			super(TaxiAgent.this);
		}

		@Override
		public void action() {
			if (goalSpace != null) {
				MapSpace currentSpace = elementMap.getSpaceAt(x, y);
				MapSpace nextMove = elementMap.getNearestMoveBetween(currentSpace, goalSpace);
				elementMap.moveElement(currentSpace.searchByAgent(TaxiAgent.this), currentSpace, nextMove);
				
				if (nextMove == goalSpace) {
					if (goalSpace.getStaticElement() instanceof TaxiStopElement) { // If this taxi is going to a stop, mark it as not requested. It might be going to its passenger or the passenger might not be there anymore
						requested = false;
						((TaxiStopElement) goalSpace.getStaticElement()).getTaxiQueue().add(TaxiAgent.this);
					}
					
					goalSpace = null;
				}
				
				if (carriedPassenger != null) {
					System.out.println("Taxi " + getLocalName() + " is moving passenger " + carriedPassenger.getLocalName() + " to (" + nextMove.getStaticElement().getX() + ", " + nextMove.getStaticElement().getY() + ").");
					PassengerElement myPassengerElement = (PassengerElement) currentSpace.searchByAgent(carriedPassenger);
					if (myPassengerElement == null) carriedPassenger = null;
					else elementMap.moveElement(currentSpace.searchByAgent(carriedPassenger), currentSpace, nextMove);
				}
			}
		}
	}
	
	private class ListenRequestsBehavior extends CyclicBehaviour {
		public ListenRequestsBehavior() {
			super(TaxiAgent.this);
		}
		
		@Override
		public void action() {
			ACLMessage msg = receive();
            if (msg != null && carriedPassenger == null && !requested) {
            	if (msg.getContent().equals("Where are you?")) {
            		ACLMessage reply = msg.createReply();
                    reply.setPerformative(ACLMessage.INFORM);
                    reply.setContent("I'm at (" + x + ", " + y + ").");
                    send(reply);
                    System.out.println("[" + getLocalName() + "] Sent \"" + reply.getContent() + "\"");
            	}
            	else if (msg.getContent().matches("^Come get me! I'm at \\(\\d+, \\d+\\)\\.$")) {
            		requested = true;
            		
            		Pattern p = Pattern.compile("^Come get me! I'm at \\((\\d+), (\\d+)\\)\\.$");
	            	Matcher m = p.matcher(msg.getContent());
	            	int requestedX = Integer.parseInt(m.group(1));
	            	int requestedY = Integer.parseInt(m.group(2));
	            	
	            	goalSpace = elementMap.getSpaceAt(requestedX, requestedY);
            	}
            }
		}
	}

	protected TaxiAgent(int x, int y, Map elementMap) {
		super(x, y, elementMap);
		goalSpace = null;
		carriedPassenger = null;
		requested = false;
	}
	
	public PassengerAgent getcarriedPassenger() {
		return carriedPassenger;
	}
	
	public void setCarriedPassenger(PassengerAgent passenger) {
		carriedPassenger = passenger;
		if (passenger != null) goalSpace = carriedPassenger.getDestinationSpace();
		requested = false;
	}

	@Override
	protected void setup() {
		super.setup("TaxiAgent");
		addBehaviour(new GoToGoalBehavior());
		addBehaviour(new ListenRequestsBehavior());
	}
}
