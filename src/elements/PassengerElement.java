package elements;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import agents.PassengerAgent;

public class PassengerElement extends Element {
	private static Image img;
	private PassengerAgent agent;
	
	static {
		try {
			img = ImageIO.read(new File("res/img/passenger.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected Image getImg() {
		return img;
	}
	
	@Override
	public boolean canHaveElementOnTop() {
		return false;
	}
	
	public PassengerElement(PassengerAgent agent) {
		super(agent.getX(), agent.getY());
		this.agent = agent;
	}
	
	public PassengerAgent getAgent() {
		return agent;
	}
}
