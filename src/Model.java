import java.util.ArrayList;

import agents.Agent;
import uchicago.src.sim.engine.BasicAction;
import uchicago.src.sim.engine.Schedule;
import uchicago.src.sim.engine.SimModelImpl;
import uchicago.src.sim.gui.DisplaySurface;
import uchicago.src.sim.gui.Object2DDisplay;
import uchicago.src.sim.space.Object2DGrid;

public class Model extends SimModelImpl {
	private ArrayList<Agent> agentList;
	private DisplaySurface dsurf;
	private Object2DGrid space;
	private Schedule schedule;
	
	private int numberOfTaxis;
	
	public Model() {
		super();
		numberOfTaxis = 5;
	}
	
	public String getName() {
		return "Taxi Simulation";
	}
	
	@Override
	public String[] getInitParam() {
		return new String[] {"numberOfTaxis"};
	}
	
	@Override
	public void setup() {
		dsurf = new DisplaySurface(this, getName());
		registerDisplaySurface(getName(), dsurf);
	}
	
	public Schedule getSchedule() {
		return schedule;
	}
	
	public void begin() {
		buildModel();
		buildDisplay();
		buildSchedule();
	}
	
	public void buildModel() {
		agentList = new ArrayList<Agent>();
		space = new Object2DGrid(500, 500);

		Agent osGar = new Agent("Osmani García");

		agentList.add(osGar);
		space.putObjectAt(200, 200, osGar);
	}
	
	private void buildDisplay() {
		Object2DDisplay agentDisplay = new Object2DDisplay(space);
		agentDisplay.setObjectList(agentList);
		
		dsurf.addDisplayableProbeable(agentDisplay, "Agents");
		addSimEventListener(dsurf);
		dsurf.display();
	}
	
	private void buildSchedule() {
		class MyAction extends BasicAction {
			public void execute() {
				System.out.println("olá");
			}
		}
		
		MyAction action = new MyAction();
		
		schedule.scheduleActionAtEnd(action);
	}
	
	public int getNumberOfTaxis() {
		return numberOfTaxis;
	}
	
	public void setNumberOfTaxis(int numberOfTaxis) {
		this.numberOfTaxis = numberOfTaxis;
	}
}
