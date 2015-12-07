import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import elements.Building;
import elements.Gas;
import elements.MapAgent;
import elements.Map;
import elements.MapElement;
import elements.Passenger;
import elements.Road;
import elements.Stop;
import elements.Taxi;
import sajas.sim.repast3.Repast3Launcher;
import uchicago.src.sim.gui.DisplaySurface;
import uchicago.src.sim.gui.Drawable;
import uchicago.src.sim.gui.Object2DDisplay;
import uchicago.src.sim.space.Object2DGrid;
import uchicago.src.sim.util.Random;

public class Launcher extends Repast3Launcher {
	private ArrayList<Drawable> drawList;
	private ArrayList<MapAgent> agentList;
	private ArrayList<MapElement> elementsList;
	private DisplaySurface dsurf;
	private Object2DGrid space;
	private Map map;

	private Image imgTaxi;
	private Image imgPassenger;
	private Image imgRoad;
	private Image imgGas;
	private Image imgStop;
	private Image imgBuilding;
	
	
	private int numberOfTaxis;
	private int numberOfPassengers;

	public Launcher() {
		super();
		numberOfTaxis = 5;
		numberOfPassengers = 5;
	}

	@Override
	public String getName() {
		return "Taxi Simulation";
	}

	@Override
	public String[] getInitParam() {
		return new String[] {"numberOfTaxis"};
	}

	@Override
	public void setup() {
		super.setup();
		dsurf = new DisplaySurface(this, getName());
		registerDisplaySurface(getName(), dsurf);
	}

	@Override
	public void begin() {
		super.begin();
		buildModel();
		buildDisplay();
		buildSchedule();
	}

	@Override
	protected void launchJADE() {
	}

	public void buildModel() {
		drawList = new ArrayList<Drawable>();
		agentList = new ArrayList<MapAgent>();
		elementsList = new ArrayList<MapElement>();
		map = new Map("map.txt");
		space = new Object2DGrid(map.getMapLines(), map.getMapCols());
		try {
			imgTaxi = ImageIO.read(new File("images/taxi.png"));
			imgPassenger = ImageIO.read(new File("images/passenger.png"));
			imgRoad = ImageIO.read(new File("images/road.jpg"));
			imgGas = ImageIO.read(new File("images/gas.png"));
			imgStop = ImageIO.read(new File("images/StopTaxi.jpg"));
			imgBuilding = ImageIO.read(new File("images/building.png"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		for (int i = 0; i < map.getMap().size(); i++) {
			for (int j = 0; j < map.getMap().get(i).size(); j++) {
				if (map.getMap().get(i).get(j) == 1) {
					Road road = new Road(i, j, space, imgRoad);
					drawList.add(road);
					elementsList.add(road);
					
				}
				else if (map.getMap().get(i).get(j) == 2) {
					Stop stop = new Stop(i, j, space, imgStop);
					drawList.add(stop);
					elementsList.add(stop);
				}
				
				else if (map.getMap().get(i).get(j) == 3) {
					Gas gas = new Gas(i, j, space, imgGas);
					drawList.add(gas);
					elementsList.add(gas);
				}
				
				else if (map.getMap().get(i).get(j) == 0) {
					Building building = new Building(i, j, space, imgBuilding);
					drawList.add(building);
					elementsList.add(building);
				}
				
			}
		}
		

		for (int i = 0; i < numberOfTaxis; ++i) {
			int x, y;
			
			do {
				x = Random.uniform.nextIntFromTo(0, space.getSizeX() - 1);
				y = Random.uniform.nextIntFromTo(0, space.getSizeY() - 1);
			} while (map.getMapTransposed().get(y).get(x) == 0);
			
			Taxi taxi = new Taxi(x ,y, imgTaxi);
			
			drawList.add(taxi);
			agentList.add(taxi);
		}
		
		for (int i = 0; i < numberOfPassengers; ++i) {
			int x, y;
			
			do {
				x = Random.uniform.nextIntFromTo(0, space.getSizeX() - 1);
				y = Random.uniform.nextIntFromTo(0, space.getSizeY() - 1);
			} while (map.getMapTransposed().get(y).get(x) == 0);
			
			Passenger passenger = new Passenger(x ,y, imgPassenger);
			
			drawList.add(passenger);
			agentList.add(passenger);
		}

		for (Drawable o : drawList) {
			space.putObjectAt(o.getX(), o.getY(), o);
		}
	}

	private void buildDisplay() {
		Object2DDisplay agentDisplay = new Object2DDisplay(space);
		agentDisplay.setObjectList(drawList);

		dsurf.addDisplayableProbeable(agentDisplay, "Agents");
		addSimEventListener(dsurf);
		dsurf.display();
		getSchedule().scheduleActionBeginning(1, this, "step");
	}

	public void step() {
		dsurf.updateDisplay();
	}

	private void buildSchedule() {
		for (MapAgent agent : agentList) {
			scheduleAgent(agent);
		}
	}

	public int getNumberOfTaxis() {
		return numberOfTaxis;
	}

	public void setNumberOfTaxis(int numberOfTaxis) {
		this.numberOfTaxis = numberOfTaxis;
	}

	public int getNumberOfPassengers() {
		return numberOfPassengers;
	}

	public void setNumberOfPassengers(int numberOfPassengers) {
		this.numberOfPassengers = numberOfPassengers;
	}
	
}
