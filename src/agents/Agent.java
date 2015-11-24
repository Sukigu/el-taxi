package agents;

import models.MapElement;

public class Agent extends MapElement {
	protected String name;
	
	public Agent(String name) {
		super(0, 0);
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}
