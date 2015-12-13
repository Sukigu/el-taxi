package elements;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

import javax.imageio.ImageIO;

import agents.TaxiAgent;

public class TaxiStopElement extends Element {
	private static Image img;
	private Queue<TaxiAgent> taxiQueue;
	
	static {
		try {
			img = ImageIO.read(new File("res/img/StopTaxi.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public TaxiStopElement(int x, int y) {
		super(x, y);
		taxiQueue = new LinkedList<TaxiAgent>();
	}
	
	@Override
	protected Image getImg() {
		return img;
	}
	
	public Queue<TaxiAgent> getTaxiQueue() {
		return taxiQueue;
	}
	
	@Override
	public boolean canHaveElementOnTop() {
		return true;
	}
	
	@Override
	public boolean canBeTraveled() {
		return true;
	}
}
