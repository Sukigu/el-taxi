package agents;

import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import sajas.core.behaviours.SimpleBehaviour;
import sajas.domain.DFService;

public class PassengerAgent extends Agent {
	private class PassengerBehavior extends SimpleBehaviour {
		public PassengerBehavior(Agent a) {
			super(a);
		}
		
		@Override
		public void action() {
			DFAgentDescription template = new DFAgentDescription();
			ServiceDescription sd1 = new ServiceDescription();
			sd1.setType("TaxiAgent");
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
		}

		@Override
		public boolean done() {
			return true;
		}
	}

	public PassengerAgent(int x, int y) {
		super(x, y);
	}

	@Override
	public void setup() {
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setName(getLocalName());
		sd.setType("PassengerAgent");
		dfd.addServices(sd);

		try {
			DFService.register(this, dfd);
		} catch (FIPAException e) {
			e.printStackTrace();
		}
		
		addBehaviour(new PassengerBehavior(this));
	}
}
