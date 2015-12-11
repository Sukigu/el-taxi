package agents;

import java.util.ArrayList;

import elements.Map;
import elements.MapSpace;
import elements.TaxiStopElement;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import sajas.core.behaviours.SimpleBehaviour;
import sajas.domain.DFService;

public class PassengerAgent extends Agent {
	TaxiStopElement initStop;

	private class PassengerBehavior extends SimpleBehaviour {
		public PassengerBehavior(Agent a) {
			super(a);
		}

		@Override
		public void action() { // TODO: Concluir
			MapSpace currentSpace = elementMap.getSpaceAt(x, y);
			ArrayList<MapSpace> possibleNextMoves = elementMap.getPossibleMovesFrom(currentSpace);
		}

		/*@Override
		public void action() {
			DFAgentDescription template = new DFAgentDescription();
			ServiceDescription sd1 = new ServiceDescription();
			sd1.setType("TaxiStopAgent");
			template.addServices(sd1);

			try {
				DFAgentDescription[] result = DFService.search(PassengerAgent.this, template);
				ACLMessage msg = new ACLMessage(ACLMessage.INFORM);

				for (int i = 0; i < result.length; ++i) {
					if (!result[i].getName().getLocalName().equals(getLocalName())) {
						msg.addReceiver(result[i].getName());
					}
				}

				msg.setContent("Come get me!");
				send(msg);
			} catch (FIPAException e) {
				e.printStackTrace();
			}
		}*/

		@Override
		public boolean done() {
			return true;
		}
	}

	public PassengerAgent(int x, int y, Map elementMap) { // TODO: Verificar redundância com novos métodos do Map
		super(x, y, elementMap);

		ArrayList<TaxiStopElement> taxiStops = elementMap.getTaxiStops();
		int minDistanceToStop = Integer.MAX_VALUE;
		for (TaxiStopElement taxiStop : taxiStops) {
			int distanceToStop = Math.abs(taxiStop.getX() - x) + Math.abs(taxiStop.getY() - y);
			if (distanceToStop < minDistanceToStop) {
				initStop = taxiStop;
				minDistanceToStop = distanceToStop;
			}
		}
	}

	@Override
	public void setup() {
		super.setup("PassengerAgent");

		addBehaviour(new PassengerBehavior(this));
	}
}
