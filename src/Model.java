import agents.Agent;
import uchicago.src.sim.engine.SimpleModel;

public class Model extends SimpleModel {
	public Model() {
		name = "My Hello World Model";
	}
	
	protected void preStep() {
	}
	
	protected void postStep() {
	}
	
	public void setup() {
		super.setup();
		autoStep = true;
		shuffle = true;
	}
	
	public void buildModel() {
		agentList.add(new Agent("teste"));
	}
}
