package main;
import java.util.ArrayList;

import agents.PassengerAgent;
import agents.TaxiAgent;
import agents.TaxiAgent1;
import agents.TaxiAgent2;
import agents.TaxiAgent3;
import elements.Map;
import elements.PassengerElement;
import elements.TaxiElement;
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
	private ContainerController agentContainer;
	private ArrayList<Drawable> drawList;
	private DisplaySurface dsurf;
	private Object2DGrid space;
	private Map elementMap;
	
	private int numTaxisBehavior1, numTaxisBehavior2, numTaxisBehavior3, numPassengers;
	
	public Launcher() {
		super();
		numTaxisBehavior1 = 2;
		numTaxisBehavior2 = 2;
		numTaxisBehavior3 = 0;
		numPassengers = 5;
		
		drawList = new ArrayList<Drawable>();
		elementMap = new Map("res/map.txt", drawList);
		space = new Object2DGrid(elementMap.getDimX(), elementMap.getDimY());
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
		agentContainer = rt.createMainContainer(p1);
		
		elementMap.setAgentContainer(agentContainer);
		placeElements();
	}

	private void placeElements() {
		for (int x = 0; x < elementMap.getDimX(); ++x) {
			for (int y = 0; y < elementMap.getDimY(); ++y) {
				drawList.add(elementMap.getSpaceAt(x, y).getStaticElement());
			}
		}
		
		int numTaxis = numTaxisBehavior1 + numTaxisBehavior2 + numTaxisBehavior3;

		for (int i = 0; i < numTaxis; ++i) {
			int x, y;
			
			do {
				x = Random.uniform.nextIntFromTo(0, space.getSizeX() - 1);
				y = Random.uniform.nextIntFromTo(0, space.getSizeY() - 1);
			} while (!elementMap.getSpaceAt(x, y).getStaticElement().canHaveElementOnTop());
			
			TaxiAgent taxi = null;
			
			if (numTaxisBehavior1 > 0) {
				taxi = new TaxiAgent1(x, y, elementMap);
				--numTaxisBehavior1;
			}
			else if (numTaxisBehavior2 > 0) {
				taxi = new TaxiAgent2(x, y, elementMap);
				--numTaxisBehavior2;
			}
			else if (numTaxisBehavior3 > 0) {
				taxi = new TaxiAgent3(x, y, elementMap);
				--numTaxisBehavior3;
			}
			
			TaxiElement newElement = new TaxiElement(taxi);
			elementMap.getSpaceAt(x, y).addTopElement(newElement);
			drawList.add(newElement);
			
			try {
				agentContainer.acceptNewAgent("taxi" + i, taxi).start();
			} catch (StaleProxyException e) {
				e.printStackTrace();
			}
		}
		
		for (int i = 0; i < numPassengers; ++i) {
			int x, y;
			
			do {
				x = Random.uniform.nextIntFromTo(0, space.getSizeX() - 1);
				y = Random.uniform.nextIntFromTo(0, space.getSizeY() - 1);
			} while (!elementMap.getSpaceAt(x, y).getStaticElement().canHaveElementOnTop());
			
			PassengerAgent passenger = new PassengerAgent(x, y, elementMap);
			
			PassengerElement newElement = new PassengerElement(passenger);
			elementMap.getSpaceAt(x, y).addTopElement(newElement);
			drawList.add(newElement);
			
			try {
				agentContainer.acceptNewAgent("passenger" + PassengerAgent.getLastId(), passenger).start();
			} catch (StaleProxyException e) {
				e.printStackTrace();
			}
		}

		for (Drawable o : drawList) {
			space.putObjectAt(o.getX(), o.getY(), o);
		}
	}

	/* Getters and setters for parameters */
	public int getNumTaxisBehavior1() {
		return numTaxisBehavior1;
	}

	public void setNumTaxisBehavior1(int numTaxisBehavior1) {
		this.numTaxisBehavior1 = numTaxisBehavior1;
	}

	public int getNumTaxisBehavior2() {
		return numTaxisBehavior2;
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
