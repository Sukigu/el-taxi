package elements;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Map {
	public ArrayList<ArrayList<Integer>> map;
	public ArrayList<ArrayList<Integer>> mapTransposed;
	
	public Map (String file) {
		map = new ArrayList<ArrayList<Integer>>();
		
		try (BufferedReader br = new BufferedReader(new FileReader(file)))
		{
			String sCurrentLine = null;
			
			while ((sCurrentLine = br.readLine()) != null) {
				ArrayList<Integer> line = new ArrayList<Integer>();
				for (int i = 0; i < sCurrentLine.length(); i++) {
					line.add((int) sCurrentLine.charAt(i) - (int) '0'); //-48
				}
				map.add(line);
			}
			
			mapTransposed = transpose(map);

			br.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		} 

	}
	
	public ArrayList<ArrayList<Integer>> getMapTransposed() {
		return mapTransposed;
	}

	public int getMapCols() {
		return map.size();
	}

	public int getMapLines() {
		return map.get(0).size();
	}

	public ArrayList<ArrayList<Integer>> getMap() {
		return map;
	}

	public void setMap(ArrayList<ArrayList<Integer>> map) {
		this.map = map;
	}
	
	public static ArrayList<ArrayList<Integer>> transpose(ArrayList<ArrayList<Integer>> list) {
		int r = list.size();
		int c = list.get(r-1).size();
		ArrayList<ArrayList<Integer>> t = new ArrayList<ArrayList<Integer>>();
		
		  for(int i = 0; i < r; ++i) {
			 ArrayList<Integer> line = new ArrayList<Integer>();
		     for(int j = 0; j < c; ++j) {
		    	 line.add(list.get(j).get(i));
		     }
		     t.add(line);
		  }
		  
		return t;
	}
}
