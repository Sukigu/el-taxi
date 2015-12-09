package elements;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class BuildingElement extends Element {
	private static Image img;
	
	static {
		try {
			img = ImageIO.read(new File("res/img/building.png"));
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
	
	public BuildingElement(int x, int y) {
		super(x, y);
	}
}
