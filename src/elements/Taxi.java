package elements;

import java.awt.Image;

import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import sajas.core.behaviours.CyclicBehaviour;
import sajas.domain.DFService;
import uchicago.src.sim.gui.SimGraphics;

public class Taxi extends MapAgent {
	private Image img;
	
	private class TaxiBehavior extends CyclicBehaviour {
		public void action() {
			ACLMessage msg = receive();
			if (msg != null) {
				System.out.println(msg.getContent());
			}
			else {
				block();
			}
		}
	}
	
	public Taxi(int x, int y, Image img) {
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
		sd.setType("TaxiAgent");
		dfd.addServices(sd);

		try {
			DFService.register(this, dfd);
		} catch (FIPAException e) {
			e.printStackTrace();
		}

		addBehaviour(new TaxiBehavior());
	}
}
