package elements;

import java.util.ArrayList;

public class MapSpace {
	private final Element staticElement; // Road, Building, GasStation, TaxiStop
	private ArrayList<Element> topElements;
	
	public MapSpace(Element baseElement) {
		this.staticElement = baseElement;
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
}
