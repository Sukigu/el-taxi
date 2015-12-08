package elements;

import java.awt.Image;

import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import sajas.core.behaviours.SimpleBehaviour;
import sajas.domain.DFService;
import uchicago.src.sim.gui.SimGraphics;

public class Passenger extends MapAgent {
	private Image img;
	
	private class PassengerBehavior extends SimpleBehaviour {
		public void action() {
			DFAgentDescription template = new DFAgentDescription();
			ServiceDescription sd1 = new ServiceDescription();
			sd1.setType("TaxiAgent");
			template.addServices(sd1);

			try {
				DFAgentDescription[] result = DFService.search(Passenger.this, template);
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

		public boolean done() {
			return true;
		}
	}

	public Passenger(int x, int y, Image img) {
		super(x, y);
		this.img = img;
	}

	@Override
	public void draw(SimGraphics g) {
		g.drawImageToFit(img);
	}

	@Override
	protected void setup() {
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setName(getName());
		sd.setType("PassengerAgent");
		dfd.addServices(sd);

		try {
			DFService.register(this, dfd);
		} catch (FIPAException e) {
			e.printStackTrace();
		}
		
		addBehaviour(new PassengerBehavior());
	}
}
