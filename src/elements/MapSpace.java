package elements;

import java.util.ArrayList;

import agents.Agent;

public class MapSpace {
	private final Element staticElement; // Road, Building, GasStation, TaxiStop
	private ArrayList<Element> topElements;
	
	public MapSpace(Element baseElement) {
		this.staticElement = baseElement;
		topElements = new ArrayList<Element>();
	}
	
	public Element getStaticElement() {
		return staticElement;
	}
	
	public ArrayList<Element> getTopElements() {
		return topElements;
	}
	
	public void addTopElement(Element element) {
		topElements.add(element);
	}
	
	/* Possível azo a erros: implementar equals() para todos os elementos */
	public void removeTopElement(Element element) {
		topElements.remove(element);
	}
	
	public Element searchAgent(Agent agent) {
		for (Element e : topElements) {
			if (e instanceof TaxiElement) {
				TaxiElement taxi = (TaxiElement) e;
				if (taxi.getAgent() == agent) return e;
			}
			else if (e instanceof PassengerElement) {
				PassengerElement passenger = (PassengerElement) e;
				if (passenger.getAgent() == agent) return e;
			}
			else continue;
		}
		
		return null;
	}
}
