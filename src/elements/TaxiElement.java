package elements;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import agents.TaxiAgent;

public class TaxiElement extends Element {
	private static Image img;
	private TaxiAgent agent;
	
	static {
		try {
			img = ImageIO.read(new File("res/img/taxi.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected Image getImg() {
		return img;
	}
	
	@Override
	public boolean canHaveElementOnTop() {
		return false;
	}
	
	public TaxiElement(TaxiAgent agent) {
		super(agent.getX(), agent.getY());
		this.agent = agent;
	}
	
	public TaxiAgent getAgent() {
		return agent;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o.getClass() == TaxiElement.class) {
			TaxiElement t = (TaxiElement) o;
			return agent == t.agent;
		}
		else return false;
	}
}
