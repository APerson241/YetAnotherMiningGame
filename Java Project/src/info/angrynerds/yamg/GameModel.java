package info.angrynerds.yamg;

import java.awt.*;
import java.beans.*;
import java.io.Serializable;
import java.util.List;

import info.angrynerds.yamg.robot.*;
import info.angrynerds.yamg.robot.Robot;
import info.angrynerds.yamg.ui.*;
import info.angrynerds.yamg.utils.*;

/**
 * Hold ALL the data!
 */
@SuppressWarnings("serial")
public class GameModel implements PropertyChangeListener, Serializable {
	private Planet currentPlanet;
	
	private Robot robot;
	private transient Yamg yamg;
	private BankAccount bank;
	private transient Shop shop;
	private transient Portal portal;
	
	private static transient Thread gravityThread = null;
	
	/**
	 * Whether or not the user has taken a step yet.
	 */
	public transient boolean FIRST_STEP = false;
	public transient boolean GRAVITY = true;
	/**
	 * Whether or not the user has purchased the portal yet.
	 */
	public boolean HAS_PORTAL = false;
	public transient boolean LOCKED = false;
	/**
	 * Whether or not the user has infinite dynamite.
	 */
	private transient boolean infiniteDynamite;
	
	/**
	 * The side of one square.
	 */
	public static transient int UNIT = 25;	// Should be 25.
	public static int GROUND_LEVEL = UNIT * 8;
	/**
	 * The y-coordinate of the bottom of the map.
	 */
	public static final int BOTTOM = 5000;
	
	public GameModel(Yamg y) {
		currentPlanet = new Planet(this, "main", true, BOTTOM);
		portal = new Portal(this);
		portal.addPlanet(currentPlanet);
		robot = new Robot();
		bank = new BankAccount(100);
		shop = new Shop(this);
		yamg = y;
		robot.addPropertyChangeListener(this);
	}
	
	public void addHole(Point point) {
		currentPlanet.addHole(point);
	}
	
	public static void setUnit(int newUnit) {
		UNIT = newUnit;
		GROUND_LEVEL = UNIT * 8;
	}
	
	public void addElement(Element element) {
		currentPlanet.addElement(element);
	}
	
	public void addRock(Point point) {
		currentPlanet.addRock(point);
	}
	
	public List<Rectangle> getHoles() {
		return currentPlanet.getHoles();
	}
	
	public List<Element> getElements() {
		return currentPlanet.getElements();
	}
	
	public List<Rectangle> getRocks() {
		return currentPlanet.getRocks();
	}
	
	public Point getRobotLocation() {
		return robot.getLocation();
	}

	public Robot getRobot() {
		return robot;
	}
	
	public BankAccount getBankAccount() {
		return bank;
	}
	
	public void infiniteDynamite(boolean b) {
		infiniteDynamite = b;
		if(b) {
			robot.addDynamite();
		} else {
			robot.useDynamite();
		}
	}
	
	public boolean isInfiniteDynamite() {
		return infiniteDynamite;
	}
	
	public Shop getShop() {
		return shop;
	}
	
	public Portal getPortal() {
		return portal;
	}
	
	public boolean isGravity() {
		return GRAVITY;
	}
	
	public void setGravity(boolean gravity) {
		GRAVITY = gravity;
		if(gravity) {
			if(gravityThread == null) {
				Runnable gravityJob = new GravityJob(this);
				gravityThread = new Thread(gravityJob);
				gravityThread.start();
			}
		} else {
			gravityThread = null;
		}
	}
	
	public boolean isSpaceAboveRobot() {
		if(robot.getLocation().y <= 0) return false;
		if(robot.getLocation().y <= GROUND_LEVEL) return true;
		for(Rectangle hole:currentPlanet.getHoles()) {
			if(((hole.y + hole.height) == robot.getLocation().y) &&
					(robot.getLocation().x == hole.x)) {
				return true;
			}
		}
		return false;
	}

	public void doRefresh() {
		yamg.getView().refreshView();
	}

	/**
	 * Determines if the robot can move in the given direction based on the robot position and
	 * the fuel level of the robot.  Consulted by the {@link info.angrynerds.yamg.ui.MyKeyListener
	 * MyKeyListener} before the MyKeyListener moves the robot after the user presses an arrow
	 * key.
	 * @param direction The direction the robot wants to move
	 * @return Whether or not the robot can move
	 */
	public boolean canRobotMove(Direction direction) {
		boolean result = true;
		/* Each case checks for 2 things: if the robot will move outside of the bounds
		 * and if there is a rock in the robot's path.
		 */
		switch(direction) {
		case UP:
			result &= robot.getLocation().y > 0;
			result &= !currentPlanet.getRocks().contains(new Rectangle(robot.getLocation().x,
					robot.getLocation().y - UNIT, UNIT, UNIT));
			break;
		case DOWN:
			result &= robot.getLocation().y < 5175;	// 5175 = the bottom of the map
			result &= !currentPlanet.getRocks().contains(new Rectangle(robot.getLocation().x,
					robot.getLocation().y + UNIT, UNIT, UNIT));
			break;
		case LEFT:
			result &= robot.getLocation().x > 0;
			result &= !currentPlanet.getRocks().contains(new Rectangle(robot.getLocation().x - UNIT,
					robot.getLocation().y, UNIT, UNIT));
			break;
		case RIGHT:
			result &= robot.getLocation().x < yamg.getFrameBounds().width - UNIT;
			result &= !currentPlanet.getRocks().contains(new Rectangle(robot.getLocation().x + UNIT,
					robot.getLocation().y, UNIT, UNIT));
			break;
		}
		result &= robot.getFuelTank().getFuelLevel() > 0;
		return result;
	}
	
	public Element removeElement(Point location) {
		return currentPlanet.removeElement(location);
	}

	public GameView getView() {
		return yamg.getView();
	}

	public void propertyChange(PropertyChangeEvent evt) {
		
	}

	public Yamg getController() {
		return yamg;
	}
}