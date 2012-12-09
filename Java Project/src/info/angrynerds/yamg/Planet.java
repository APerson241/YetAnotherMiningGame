package info.angrynerds.yamg;

import info.angrynerds.yamg.robot.*;

import java.awt.*;
import java.io.Serializable;
import java.util.*;
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
	private boolean scanned;
	private boolean colonized;

	private GameModel model;
	
	/**
	 * The constructor for a Planet.
	 * @param model A reference to the main GameModel.
	 * @param name The name of the world.
	 * @param homePlanet Whether or not this planet is the home planet of the robot.
	 * @param bottom The y-coordinate indicating the bottom of the map.
	 */
	public Planet(GameModel model, String name, boolean homePlanet, int bottom) {
		this.model = model;
		this.name = name;
		this.homePlanet = homePlanet;
		scanned = homePlanet;
		colonized = homePlanet;
		BOTTOM = bottom;
		holes = new ArrayList<Rectangle>();
		elements = new ArrayList<Element>();
		rocks = new ArrayList<Rectangle>();
		buildWorld();
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

	public boolean isContainsRobot() {
		return containsRobot;
	}

	public boolean isScanned() {
		return scanned;
	}
	
	public void setScanned(boolean b) {
		scanned = b;
	}

	public boolean isColonized() {
		return colonized;
	}
	
	public void setColonized(boolean b) {
		colonized = b;
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
	
	public void addHole(Point point) {
		holes.add(new Rectangle(point.x, point.y, GameModel.UNIT, GameModel.UNIT));
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
	
	private void buildWorld() {
		initializeHoles();
		initializeElements();
		initializeRocks();
	}
	
	private void initializeHoles() {
		for(int i = 0; i < 500; i++) {
			addHole(getRandomLocationOnGrid(GameModel.GROUND_LEVEL + 25));
		}
	}
	
	private void initializeElements() {
		int number = ElementType.values().length * 10 + (GameModel.UNIT * 10);
		for(ElementType type:ElementType.values()) {
			for(int i = 0; i < number; i++) {
				addElement(new Element(type,
						getRandomLocationOnGrid(GameModel.GROUND_LEVEL + 25)));
			}
			number -= 10;
		}
	}
	
	private void initializeRocks() {
		for(int i = 0; i < 250; i++) {
			Point point = getRandomLocationOnGrid(525);
			if(getElements().contains(new Rectangle(point.x,
					point.y, GameModel.UNIT, GameModel.UNIT))) {
				i++;
				continue;
			} else {
				addRock(point);
			}
		}
		int UNIT4 = GameModel.UNIT * 4;	// Should be 100 if UNIT is 25
		addRock(new Point(UNIT4 * 3, UNIT4 * 2));	// Below the Shop
		addRock(new Point(UNIT4 * 3 + GameModel.UNIT, UNIT4 * 2));
		addRock(new Point(UNIT4 * 3 + GameModel.UNIT * 2, UNIT4 * 2));
	}
	
	private Point getRandomLocationOnGrid(int offset) {
		Random random = new Random();
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int xLimit = screen.width - 100;
		int yLimit = BOTTOM;
		int x = random.nextInt((int) xLimit/GameModel.UNIT) * GameModel.UNIT;
		int y = (random.nextInt((int) yLimit/GameModel.UNIT) * GameModel.UNIT) + offset;
		return new Point(x, y);
	}
	
	public String toString() {
		return name + (scanned?(colonized?" [colonized]":" [scanned]"):"");
	}
}