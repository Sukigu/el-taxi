package elements;

public abstract class Agent extends MapElement {
	private String name;
	
	public Agent(String name) {
		super(0, 0);
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}
