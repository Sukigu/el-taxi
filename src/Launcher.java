import java.util.ArrayList;

import elements.MapAgent;
import elements.StreetElement;
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
	private DisplaySurface dsurf;
	private Object2DGrid space;
	private StreetElement map;

	private int numberOfTaxis;

	public Launcher() {
		super();
		numberOfTaxis = 5;
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
		space = new Object2DGrid(100, 100);
		map = new StreetElement("map.txt");
		

		for (int i = 0; i < numberOfTaxis; ++i) {
			int x, y;
			
			do {
				x = Random.uniform.nextIntFromTo(0, space.getSizeX() - 1);
				y = Random.uniform.nextIntFromTo(0, space.getSizeY() - 1);
			} while (space.getObjectAt(x, y) != null);
			
			Taxi taxi = new Taxi(x ,y);
			
			drawList.add(taxi);
			drawList.add(map);
			agentList.add(taxi);
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
}
