package agents;

import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import sajas.core.behaviours.CyclicBehaviour;
import sajas.domain.DFService;

public class TaxiAgent extends Agent {
	private class TaxiBehavior extends CyclicBehaviour {
		public TaxiBehavior(Agent a) {
			super(a);
		}
		
		@Override
		public void action() {
			ACLMessage msg = receive();
			if (msg != null) {
				System.out.println(msg.getContent());
			}
		}
	}
	
	public TaxiAgent(int x, int y) {
		super(x, y);
	}

	@Override
	public void setup() {
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setName(getLocalName());
		sd.setType("TaxiAgent");
		dfd.addServices(sd);

		try {
			DFService.register(this, dfd);
		} catch (FIPAException e) {
			e.printStackTrace();
		}

		addBehaviour(new TaxiBehavior(this));
	}
}
