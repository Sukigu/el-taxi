package elements;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import uchicago.src.sim.gui.Drawable;
import uchicago.src.sim.gui.SimGraphics;

public abstract class StreetElement implements Drawable {
	public static ArrayList<ArrayList<Integer>> street;
	
	public StreetElement (String file){
		
		try (BufferedReader br = new BufferedReader(new FileReader(file)))
		{
			String sCurrentLine;
			
			while ((sCurrentLine = br.readLine()) != null) {
				ArrayList<Integer> line = new ArrayList<Integer>();
				for (int i = 0; i < sCurrentLine.length(); i++) {
					line.add((int) sCurrentLine.charAt(i));
				}
				street.add(line);
			}

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
					graphics.drawFastRect(Color.blue);
				}
				else if (street.get(i).get(j) == 2) {
					graphics.setDrawingCoordinates(i * graphics.getCurWidth(), j * graphics.getCurHeight(), 0);
					graphics.drawFastRect(Color.red);
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
	
}
