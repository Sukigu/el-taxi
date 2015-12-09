package elements;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Map {
	private ArrayList<ArrayList<MapSpace>> structure;
	
	public Map (String file) {
		structure = new ArrayList<ArrayList<MapSpace>>();
		
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String sCurrentLine;
			
			for (int y = 0; ((sCurrentLine = br.readLine()) != null); ++y) {
				ArrayList<MapSpace> line = new ArrayList<MapSpace>();
				
				for (int x = 0; x < sCurrentLine.length(); ++x) {
					MapSpace newSpace = null;
					
					switch (sCurrentLine.charAt(x)) {
					case '0':
						newSpace = new MapSpace(new BuildingElement(y, x));
						break;
					case '1':
						newSpace = new MapSpace(new RoadElement(y, x));
						break;
					case '2':
						newSpace = new MapSpace(new TaxiStopElement(y, x));
						break;
					case '3':
						newSpace = new MapSpace(new GasStationElement(y, x));
						break;
					}
					
					line.add(newSpace);
				}
				
				structure.add(line);
			}

			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		structure = transpose(structure);
	}
	
	public MapSpace getSpaceAt(int x, int y) {
		return structure.get(y).get(x);
	}

	public int getDimX() {
		return structure.get(0).size();
	}

	public int getDimY() {
		return structure.size();
	}
	
	private ArrayList<ArrayList<MapSpace>> transpose(ArrayList<ArrayList<MapSpace>> list) {
		int r = list.size();
		int c = list.get(r-1).size();
		ArrayList<ArrayList<MapSpace>> t = new ArrayList<ArrayList<MapSpace>>();
		
		  for(int i = 0; i < r; ++i) {
			 ArrayList<MapSpace> line = new ArrayList<MapSpace>();
		     for(int j = 0; j < c; ++j) {
		    	 line.add(list.get(j).get(i));
		     }
		     t.add(line);
		  }
		  
		return t;
	}
}
