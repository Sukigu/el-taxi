package elements;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class TaxiStopElement extends Element {
	private static Image img;
	
	static {
		try {
			img = ImageIO.read(new File("res/img/StopTaxi.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public TaxiStopElement(int x, int y) {
		super(x, y);
	}
	
	@Override
	protected Image getImg() {
		return img;
	}
	
	@Override
	public boolean canHaveElementOnTop() {
		return true;
	}
}
