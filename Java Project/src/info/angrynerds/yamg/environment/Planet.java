package info.angrynerds.yamg.environment;

import info.angrynerds.yamg.engine.GameModel;
import info.angrynerds.yamg.engine.Launcher;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
	
	public static Dimension SIZE = null;
	
	private boolean containsRobot = false;
	private boolean scanned;
	private boolean colonized;

	private GameModel model;
	
	/**
	 * The standard width for all planets, based on the width of the user's screen.
	 */
	private static int width = -1;
	
	/**
	 * The constructor for a Planet.
	 * @param model A reference to the main GameModel.
	 * @param name The name of the world.
	 * @param homePlanet Whether or not this planet is the home planet of the robot.
	 * @param width The width of the planet, in pixels.
	 * @param height The height of the planet, in pixels.
	 */
	public Planet(GameModel model, String name, boolean homePlanet, int width, int height) {
		this.model = model;
		this.name = name;
		this.homePlanet = homePlanet;
		scanned = homePlanet;
		colonized = homePlanet;
		SIZE = new Dimension(width, height);
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

	public int getBOTTOM() {
		return SIZE.height;
	}
	
	public void addHole(Point point) {
		holes.add(new Rectangle(point.x, point.y, GameModel.getUnit(), GameModel.getUnit()));
	}
	
	public void addElement(Element element) {
		elements.add(element);
	}
	
	public void addRock(Point point) {
		rocks.add(new Rectangle(point.x, point.y, GameModel.getUnit(), GameModel.getUnit()));
	}
	
	public Element removeElement(Rectangle bounds) {
		Element deleted = null;
		for(Element element:elements) {
			if(new Rectangle(element.getLocation(), GameModel.getSquareUnit()).intersects(bounds)) {
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
		int number = ElementType.values().length * 10 + (GameModel.getUnit() * 10);
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
					point.y, GameModel.getUnit(), GameModel.getUnit()))) {
				i++;
				continue;
			} else {
				addRock(point);
			}
		}
		int UNIT4 = GameModel.getUnit() * 4;	// Should be 100 if UNIT is 25
		addRock(new Point(UNIT4 * 3, UNIT4 * 2));	// Below the Shop
		addRock(new Point(UNIT4 * 3 + GameModel.getUnit(), UNIT4 * 2));
		addRock(new Point(UNIT4 * 3 + GameModel.getUnit() * 2, UNIT4 * 2));
	}
	
	private Point getRandomLocationOnGrid(int offset) {
		Random random = new Random();
		int xLimit = SIZE.width;
		int yLimit = SIZE.height;
		int x = random.nextInt((int) xLimit/GameModel.getUnit()) * GameModel.getUnit();
		int y = (random.nextInt((int) yLimit/GameModel.getUnit()) * GameModel.getUnit()) + offset;
		return new Point(x, y);
	}
	
	public String toString() {
		return name + (scanned?(colonized?" [colonized]":" [scanned]"):"");
	}
	
	public static int getStandardWidth() {
		if (width == -1) {
			int x = Launcher.getFrameBounds().width;
			x -= (x % GameModel.getUnit());
			width = x;
		}
		return width;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (colonized ? 1231 : 1237);
		result = prime * result + (containsRobot ? 1231 : 1237);
		result = prime * result
				+ ((elements == null) ? 0 : elements.hashCode());
		result = prime * result + ((holes == null) ? 0 : holes.hashCode());
		result = prime * result + (homePlanet ? 1231 : 1237);
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((rocks == null) ? 0 : rocks.hashCode());
		result = prime * result + (scanned ? 1231 : 1237);
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Planet other = (Planet) obj;
		if (colonized != other.colonized)
			return false;
		if (containsRobot != other.containsRobot)
			return false;
		if (elements == null) {
			if (other.elements != null)
				return false;
		} else if (!elements.equals(other.elements))
			return false;
		if (holes == null) {
			if (other.holes != null)
				return false;
		} else if (!holes.equals(other.holes))
			return false;
		if (homePlanet != other.homePlanet)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (rocks == null) {
			if (other.rocks != null)
				return false;
		} else if (!rocks.equals(other.rocks))
			return false;
		if (scanned != other.scanned)
			return false;
		return true;
	}
}