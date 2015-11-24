import java.util.ArrayList;

import agents.Agent;
import uchicago.src.sim.engine.SimpleModel;
import uchicago.src.sim.gui.DisplaySurface;
import uchicago.src.sim.gui.Object2DDisplay;
import uchicago.src.sim.space.Object2DGrid;

public class Model extends SimpleModel {
	ArrayList<Agent> agentList;
	DisplaySurface dsurf;
	Object2DGrid space;
	
	public Model() {
		name = "El Taxi";
	}
	
	@Override
	public void setup() {
		super.setup();
		autoStep = true;
		shuffle = true;
		
		dsurf = new DisplaySurface(this, name);
		registerDisplaySurface(name, dsurf);
	}
	
	@Override
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
	
	@Override
	protected void preStep() {
	}
	
	@Override
	protected void postStep() {
	}
}
