package info.angrynerds.yamg;

import info.angrynerds.yamg.robot.Element;

import java.awt.*;
import java.io.Serializable;
import java.util.List;

/**
 * A planet is a game world.
 * @author Daniel Glus (APerson241)
 */
@SuppressWarnings("serial")
public class Planet implements Serializable {
	private String name;
	private boolean homePlanet;
	
	private List<Rectangle> holes;
	private List<Element> elements;
	private List<Rectangle> rocks;
	
	private static int BOTTOM = 0;
	
	private boolean containsRobot = false;

	private GameModel model;
	
	/**
	 * The constructor for a Planet.
	 * @param bottom The y-coordinate indicating the bottom of the map.
	 */
	public Planet(GameModel model, String name, boolean homePlanet, int bottom) {
		this.model = model;
		this.name = name;
		this.homePlanet = homePlanet;
		BOTTOM = bottom;
	}
	
	public List<Rectangle> getHoles() {
		return holes;
	}
	
	public List<Element> getElements() {
		return elements;
	}
	
	public List<Rectangle> getRocks() {
		return rocks;
	}

	public String getName() {
		return name;
	}

	public boolean isHomePlanet() {
		return homePlanet;
	}
	
	/**
	 * Returns if the planet should have a portal on its surface.
	 */
	public boolean hasPortal() {
		return !homePlanet;
	}

	public static int getBOTTOM() {
		return BOTTOM;
	}
	
	public void addElement(Element element) {
		elements.add(element);
	}
	
	public void addRock(Point point) {
		rocks.add(new Rectangle(point.x, point.y, GameModel.UNIT, GameModel.UNIT));
	}
	
	public Element removeElement(Point location) {
		Element deleted = null;
		for(Element element:elements) {
			if(element.getLocation().equals(location)) {
				deleted = element;
				break;
			}
		}
		if(deleted != null) {
			model.getBankAccount().deposit(deleted.getType().getPrice());
			elements.remove(deleted);
		}
		return deleted;
	}
	
	public boolean containsRobot() {
		return containsRobot;
	}
}