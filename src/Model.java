import java.util.ArrayList;

import elements.MapAgent;
import elements.Taxi;
import uchicago.src.sim.engine.BasicAction;
import uchicago.src.sim.engine.Schedule;
import uchicago.src.sim.engine.SimModelImpl;
import uchicago.src.sim.gui.DisplaySurface;
import uchicago.src.sim.gui.Object2DDisplay;
import uchicago.src.sim.space.Object2DGrid;

public class Model extends SimModelImpl {
	private ArrayList<MapAgent> agentList;
	private DisplaySurface dsurf;
	private Object2DGrid space;
	private Schedule schedule;
	
	private int numberOfTaxis;
	
	public Model() {
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
		schedule = new Schedule(1);
		dsurf = new DisplaySurface(this, getName());
		registerDisplaySurface(getName(), dsurf);
	}
	
	@Override
	public Schedule getSchedule() {
		return schedule;
	}
	
	@Override
	public void begin() {
		buildModel();
		buildDisplay();
		buildSchedule();
	}
	
	public void buildModel() {
		agentList = new ArrayList<MapAgent>();
		space = new Object2DGrid(100, 100);

		Taxi osGar = new Taxi(20 ,20);

		space.putObjectAt(osGar.getX(), osGar.getY(), osGar);
		agentList.add(osGar);
	}
	
	private void buildDisplay() {
		Object2DDisplay agentDisplay = new Object2DDisplay(space);
		agentDisplay.setObjectList(agentList);
		
		dsurf.addDisplayableProbeable(agentDisplay, "Agents");
		addSimEventListener(dsurf);
		dsurf.display();
		schedule.scheduleActionBeginning(1, this, "step");
	}

	public void step() {
		dsurf.updateDisplay();
	}
	
	private void buildSchedule() {
		/*class MyAction extends BasicAction {
			public void execute() {
				dsurf.updateDisplay();
			}
		}
		
		MyAction action = new MyAction();
		
		schedule.scheduleActionAtInterval(.5, action);*/
	}
	
	public int getNumberOfTaxis() {
		return numberOfTaxis;
	}
	
	public void setNumberOfTaxis(int numberOfTaxis) {
		this.numberOfTaxis = numberOfTaxis;
	}
}
