import uchicago.src.sim.engine.SimInit;

public class Main {
	private static final boolean BATCH_MODE = true;

	public static void main(String[] args) {
		boolean runMode = !BATCH_MODE; // BATCH_MODE or !BATCH_MODE

		// create a simulation
		SimInit init = new SimInit();

		// create a model
		//MyHelloWorldModel model = new MyHelloWorldModel();
		Model myModel = new Model();

		// load model into simulation:
		init.loadModel(myModel, null, runMode);
	}
}
