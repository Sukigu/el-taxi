package agents;

import elements.Map;
import elements.MapSpace;
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
					goalSpace = null;
				}
				
				if (carriedPassenger != null) {
					elementMap.moveElement(currentSpace.searchByAgent(carriedPassenger), currentSpace, nextMove);
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
            	}
            	else if (msg.getContent().matches("^Come get me! I'm at \\(\\d+, \\d+\\)\\.$")) {
            		requested = true;
            		// TODO set goalSpace
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
		try {
			if (carriedPassenger != null) throw new Exception();
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		goalSpace = carriedPassenger.getDestinationSpace();
		carriedPassenger = passenger;
		requested = false;
	}

	@Override
	protected void setup() {
		super.setup("TaxiAgent");
		addBehaviour(new GoToGoalBehavior());
		addBehaviour(new ListenRequestsBehavior());
	}
}
