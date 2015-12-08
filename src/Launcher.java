import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import elements.Building;
import elements.Gas;
import elements.Map;
import elements.MapElement;
import elements.Passenger;
import elements.Road;
import elements.Stop;
import elements.Taxi;
import elements.Taxi1;
import elements.Taxi2;
import elements.Taxi3;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.StaleProxyException;
import sajas.core.Runtime;
import sajas.sim.repast3.Repast3Launcher;
import sajas.wrapper.ContainerController;
import uchicago.src.sim.gui.DisplaySurface;
import uchicago.src.sim.gui.Drawable;
import uchicago.src.sim.gui.Object2DDisplay;
import uchicago.src.sim.space.Object2DGrid;
import uchicago.src.sim.util.Random;

public class Launcher extends Repast3Launcher {
	private ContainerController container;
	private ArrayList<Drawable> drawList;
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
	
	private int numTaxisBehavior1, numTaxisBehavior2, numTaxisBehavior3, numPassengers;

	public Launcher() {
		super();
		numTaxisBehavior1 = 2;
		numTaxisBehavior2 = 2;
		numTaxisBehavior3 = 2;
		numPassengers = 5;
		
		drawList = new ArrayList<Drawable>();
		elementsList = new ArrayList<MapElement>();
		map = new Map("res/map.txt");
		space = new Object2DGrid(map.getMapLines(), map.getMapCols());
		try {
			imgTaxi = ImageIO.read(new File("res/img/taxi.png"));
			imgPassenger = ImageIO.read(new File("res/img/passenger.png"));
			imgRoad = ImageIO.read(new File("res/img/road.jpg"));
			imgGas = ImageIO.read(new File("res/img/gas.png"));
			imgStop = ImageIO.read(new File("res/img/StopTaxi.jpg"));
			imgBuilding = ImageIO.read(new File("res/img/building.png"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public String getName() {
		return "Taxi Simulation";
	}

	@Override
	public String[] getInitParam() {
		return new String[] {"numTaxisBehavior1", "numTaxisBehavior2", "numTaxisBehavior3", "numPassengers"};
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
		buildDisplay();
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

	@Override
	protected void launchJADE() {
		Runtime rt = Runtime.instance();
		Profile p1 = new ProfileImpl();
		container = rt.createMainContainer(p1);
		
		placeElements();
	}

	public void placeElements() {
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
		
		int numTaxis = numTaxisBehavior1 + numTaxisBehavior2 + numTaxisBehavior3;

		for (int i = 0; i < numTaxis; ++i) {
			int x, y;
			
			do {
				x = Random.uniform.nextIntFromTo(0, space.getSizeX() - 1);
				y = Random.uniform.nextIntFromTo(0, space.getSizeY() - 1);
			} while (map.getMapTransposed().get(y).get(x) == 0);
			
			Taxi taxi = null;
			
			if (numTaxisBehavior1 > 0) {
				taxi = new Taxi1(x, y, imgTaxi);
				--numTaxisBehavior1;
			}
			else if (numTaxisBehavior2 > 0) {
				taxi = new Taxi2(x, y, imgTaxi);
				--numTaxisBehavior2;
			}
			else if (numTaxisBehavior3 > 0) {
				taxi = new Taxi3(x, y, imgTaxi);
				--numTaxisBehavior3;
			}
			
			drawList.add(taxi);
			try {
				container.acceptNewAgent("taxi" + i, taxi).start();
			} catch (StaleProxyException e) {
				e.printStackTrace();
			}
		}
		
		for (int i = 0; i < numPassengers; ++i) {
			int x, y;
			
			do {
				x = Random.uniform.nextIntFromTo(0, space.getSizeX() - 1);
				y = Random.uniform.nextIntFromTo(0, space.getSizeY() - 1);
			} while (map.getMapTransposed().get(y).get(x) == 0);
			
			Passenger passenger = new Passenger(x, y, imgPassenger);
			
			drawList.add(passenger);
		}

		for (Drawable o : drawList) {
			space.putObjectAt(o.getX(), o.getY(), o);
		}
	}

	/* Gets and sets for parameters */
	public int getNumTaxisBehavior1() {
		return numTaxisBehavior1;
	}

	public void setNumTaxisBehavior1(int numTaxisBehavior1) {
		this.numTaxisBehavior1 = numTaxisBehavior1;
	}

	public int getNumTaxisBehavior2() {
		return numTaxisBehavior1;
	}

	public void setNumTaxisBehavior2(int numTaxisBehavior2) {
		this.numTaxisBehavior2 = numTaxisBehavior2;
	}

	public int getNumTaxisBehavior3() {
		return numTaxisBehavior3;
	}

	public void setNumTaxisBehavior3(int numTaxisBehavior3) {
		this.numTaxisBehavior3 = numTaxisBehavior3;
	}

	public int getNumPassengers() {
		return numPassengers;
	}

	public void setNumPassengers(int numPassengers) {
		this.numPassengers = numPassengers;
	}
	
}
