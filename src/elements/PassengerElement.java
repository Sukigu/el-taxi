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
	
	public PassengerElement(PassengerAgent agent) {
		super(agent.getX(), agent.getY());
		this.agent = agent;
	}
	
	@Override
	protected Image getImg() {
		return img;
	}
	
	@Override
	public boolean canHaveElementOnTop() {
		return false;
	}
	
	@Override
	public int getX() {
		return agent.getX();
	}
	
	@Override
	public void setX(int x) {
		agent.setX(x);
	}
	
	@Override
	public int getY() {
		return agent.getY();
	}
	
	@Override
	public void setY(int y) {
		agent.setY(y);
	}
	
	public PassengerAgent getAgent() {
		return agent;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof PassengerElement) {
			PassengerElement t = (PassengerElement) o;
			return agent == t.agent;
		}
		else return false;
	}
}
