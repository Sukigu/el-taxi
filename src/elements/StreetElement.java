package elements;

import java.awt.Color;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import uchicago.src.sim.engine.Stepable;
import uchicago.src.sim.gui.Drawable;
import uchicago.src.sim.gui.SimGraphics;

public class StreetElement implements Stepable, Drawable {
	public static ArrayList<ArrayList<Integer>> street;
	private static Image imgRoad;
	private static Image imgGas;
	private static Image imgStop;
	private static Image imgBuilding;
	public StreetElement (String file){
		try {
			imgRoad = ImageIO.read(new File("images/road.jpg"));
			imgGas = ImageIO.read(new File("images/gas.png"));
			imgStop = ImageIO.read(new File("images/StopTaxi.jpg"));
			imgBuilding = ImageIO.read(new File("images/building.png"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	    street = new ArrayList<ArrayList<Integer>>();
		
		try (BufferedReader br = new BufferedReader(new FileReader(file)))
		{
			String sCurrentLine = null;
			
			while ((sCurrentLine = br.readLine()) != null) {
				ArrayList<Integer> line = new ArrayList<Integer>();
				for (int i = 0; i < sCurrentLine.length(); i++) {
					line.add((int) sCurrentLine.charAt(i) - (int) '0'); //-48
				}
				street.add(line);
			}
			
			br.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		} 

	}
	
	@Override
	public void draw(SimGraphics graphics) {
		for (int i = 0; i < street.size(); i++) {
			for (int j = 0; j < street.get(i).size(); j++) {
				if (street.get(i).get(j) == 1) {
					graphics.setDrawingCoordinates(i * graphics.getCurWidth(), j * graphics.getCurHeight(), 0);
					graphics.drawImageToFit(imgRoad);
					
				}
				else if (street.get(i).get(j) == 2) {
					graphics.setDrawingCoordinates(i * graphics.getCurWidth(), j * graphics.getCurHeight(), 0);
					graphics.drawImageToFit(imgStop);
				}
				
				else if (street.get(i).get(j) == 3) {
					graphics.setDrawingCoordinates(i * graphics.getCurWidth(), j * graphics.getCurHeight(), 0);
					graphics.drawImageToFit(imgGas);
				}
				
				else if (street.get(i).get(j) == 0) {
					graphics.setDrawingCoordinates(i * graphics.getCurWidth(), j * graphics.getCurHeight(), 0);
					graphics.drawImageToFit(imgBuilding);
				}
				
			}
		}
	}
	
	public int getStreetCols() {
		return street.size();
	}

	public int getStreetLines() {
		return street.get(0).size();
	}

	@Override
	public int getX() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getY() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void step() {
		// TODO Auto-generated method stub
		
	}
	
}
