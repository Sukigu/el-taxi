package elements;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import agents.PassengerAgent;
import jade.wrapper.StaleProxyException;
import sajas.wrapper.ContainerController;
import uchicago.src.sim.gui.Drawable;

public class Map {
	private ContainerController agentContainer;
	private ArrayList<Drawable> drawList;
	private ArrayList<ArrayList<MapSpace>> structure;
	private ArrayList<TaxiStopElement> taxiStops;
	
	public ArrayList<Drawable> getDrawList() {
		return drawList;
	}
	
	public void createNewPassenger() {
		int x, y;
		
		do {
			x = uchicago.src.sim.util.Random.uniform.nextIntFromTo(0, getDimX() - 1);
			y = uchicago.src.sim.util.Random.uniform.nextIntFromTo(0, getDimY() - 1);
		} while (!getSpaceAt(x, y).getStaticElement().canHaveElementOnTop());
		
		PassengerAgent passenger = new PassengerAgent(x, y, this);
		
		PassengerElement newElement = new PassengerElement(passenger);
		getSpaceAt(x, y).addTopElement(newElement);
		drawList.add(newElement);
		
		try {
			agentContainer.acceptNewAgent("passenger" + PassengerAgent.getLastId(), passenger).start();
		} catch (StaleProxyException e) {
			e.printStackTrace();
		}
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

	public ArrayList<TaxiStopElement> getTaxiStops() {
		return taxiStops;
	}
	
	public void setAgentContainer(ContainerController agentContainer) {
		this.agentContainer = agentContainer;
	}

	public void moveElement(Element elem, MapSpace spaceOrigin, MapSpace spaceDestination) {
		spaceOrigin.removeTopElement(elem);
		elem.setX(spaceDestination.getStaticElement().getX());
		elem.setY(spaceDestination.getStaticElement().getY());
		spaceDestination.addTopElement(elem);
	}

	public ArrayList<MapSpace> getPossibleMovesFrom(MapSpace space) {
		ArrayList<MapSpace> possibleMoves = new ArrayList<MapSpace>();

		int x = space.getStaticElement().getX(), y = space.getStaticElement().getY();

		possibleMoves.add(getSpaceAt(x, y));
		if (y - 1 >= 0 && canTravelBetween(space, getSpaceAt(x, y - 1))) possibleMoves.add(getSpaceAt(x, y - 1));
		if (y + 1 < getDimY() && canTravelBetween(space, getSpaceAt(x, y + 1))) possibleMoves.add(getSpaceAt(x, y + 1));
		if (x - 1 >= 0 && canTravelBetween(space, getSpaceAt(x - 1, y))) possibleMoves.add(getSpaceAt(x - 1, y));
		if (x + 1 < getDimX() && canTravelBetween(space, getSpaceAt(x + 1, y))) possibleMoves.add(getSpaceAt(x + 1, y));

		return possibleMoves;
	}

	public MapSpace getNearestMoveBetween(MapSpace spaceOrigin, MapSpace spaceDestination) {
		MapSpace nearestMove = null;
		ArrayList<MapSpace> originNeighbors = getPossibleMovesFrom(spaceOrigin);

		int minDistance = Integer.MAX_VALUE;
		for (MapSpace neighbor : originNeighbors) {
			int distance = getDistanceBetween(neighbor, spaceDestination);
			if (distance == 0) return neighbor;
			else if (distance < minDistance) {
				nearestMove = neighbor;
				minDistance = distance;
			}
			else if (distance == minDistance) {
				nearestMove = new Random().nextBoolean() ? neighbor : nearestMove;
			}
		}

		try {
			if (nearestMove == null) throw new Exception();
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return nearestMove;
	}

	public int getDistanceBetween(MapSpace spaceOrigin, MapSpace spaceDestination) {
		return Math.abs(spaceDestination.getStaticElement().getX() - spaceOrigin.getStaticElement().getX()) + Math.abs(spaceDestination.getStaticElement().getY() - spaceOrigin.getStaticElement().getY());
	}
	
	public int getDistanceBetween(int x, int y, MapSpace spaceDestination) {
		return Math.abs(spaceDestination.getStaticElement().getX() - x) + Math.abs(spaceDestination.getStaticElement().getY() - y);
	}

	private boolean canTravelBetween(MapSpace space1, MapSpace space2) {
		return space1.getStaticElement().canBeTraveled() && space2.getStaticElement().canBeTraveled();
	}

	public Map(String file, ArrayList<Drawable> drawList) {
		this.drawList = drawList;
		structure = new ArrayList<ArrayList<MapSpace>>();
		taxiStops = new ArrayList<TaxiStopElement>();

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
						TaxiStopElement taxiStop = new TaxiStopElement(y, x);
						newSpace = new MapSpace(taxiStop);
						taxiStops.add(taxiStop);
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
