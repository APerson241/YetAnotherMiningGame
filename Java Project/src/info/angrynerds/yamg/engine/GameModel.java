package info.angrynerds.yamg.engine;

import java.awt.*;
import java.beans.*;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

import info.angrynerds.yamg.environment.Element;
import info.angrynerds.yamg.environment.GravityJob;
import info.angrynerds.yamg.environment.Planet;
import info.angrynerds.yamg.robot.*;
import info.angrynerds.yamg.robot.Robot;
import info.angrynerds.yamg.ui.*;
import info.angrynerds.yamg.utils.*;

/**
 * Hold ALL the data!
 */
@SuppressWarnings("serial")
public class GameModel implements PropertyChangeListener, Serializable {
	private Logger log = Logger.getGlobal();
	
	private Planet currentPlanet;
	
	private Robot robot;
	private transient Launcher yamg;
	private BankAccount bank;
	private transient Shop shop;
	private transient Portal portal;
	
	private static transient Thread gravityThread = null;
	
	// MILESTONES
	/**
	 * Whether or not the user has taken a step yet.
	 */
	public transient boolean FIRST_STEP = false;
	/**
	 * Whether or not the user has purchased the portal yet.
	 */
	public boolean HAS_PORTAL = false;
	
	public transient boolean GRAVITY = true;
	/**
	 * Whether or not the user has infinite dynamite.
	 */
	private transient boolean infiniteDynamite;
	
	/**
	 * The side of one square.
	 */
	private static transient int UNIT = Configurables.DEFAULT_UNIT;
	public static int GROUND_LEVEL = UNIT * 8;
	
	public GameModel(Launcher y) {
		currentPlanet = new Planet(this, "Home", true, Launcher.getFrameBounds().width, Configurables.BOTTOM);
		portal = new Portal(this);
		portal.addPlanet(currentPlanet);
		robot = new Robot();
		bank = new BankAccount(Configurables.STARTING_MONEY);
		shop = new Shop(this);
		yamg = y;
		robot.addPropertyChangeListener(this);
		CheatManager.getInstance().addPropertyChangeListener(this);
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
	
	public Rectangle getRobotRect() {
		return robot.getRect();
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
	 * the fuel level of the robot.  Consulted by the {@link info.angrynerds.yamg.ui.GamePanelKeyListener
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
			break;
		case DOWN:
			result &= robot.getLocation().y < currentPlanet.getBOTTOM();
			break;
		case LEFT:
			result &= robot.getLocation().x > 0;
			break;
		case RIGHT:
			result &= robot.getLocation().x < Launcher.getFrameBounds().width - UNIT;
			break;
		}
		result &= !isRockNextToRobot(direction);
		result &= robot.getFuelTank().getFuelLevel() > 0;
		return result;
	}
	
	public Element removeElement(Rectangle bounds) {
		return currentPlanet.removeElement(bounds);
	}

	public GameView getView() {
		return yamg.getView();
	}

	public void propertyChange(PropertyChangeEvent evt) {
		log.entering(getClass().getSimpleName(), "propertyChange(PropertyChangeEvent)");
		if(CheatManager.getInstance().getMoneyToAdd() > 0) {
			// If the CheatManager indicates that it wants us to deposit money, then do so...
			bank.deposit(CheatManager.getInstance().getMoneyToAdd());
			// ... then set the amount of money it wants us to deposit to 0
			CheatManager.getInstance().clearMoneyToAdd();
		}
	}

	public Launcher getController() {
		return yamg;
	}

	public boolean isRockNextToRobot() {
		for(Direction direction:Direction.values()) {
			if(isRockNextToRobot(direction)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isRockNextToRobot(Direction direction) {
		// If direction is LEFT, negative UNIT; else if direction is RIGHT, UNIT
		int xOffset = (direction.equals(Direction.LEFT))?(-UNIT)
				:((direction.equals(Direction.RIGHT))?UNIT:0);
		// If direction is UP, negative UNIT; else if direction is DOWN, UNIT
		int yOffset = (direction.equals(Direction.UP))?(-UNIT)
				:((direction.equals(Direction.DOWN))?UNIT:0);
		return currentPlanet.getRocks().contains(new Rectangle(robot.getLocation().x + xOffset,
				robot.getLocation().y + yOffset, UNIT, UNIT));
	}
	
	/**
	 * Whether or not the model is locked.
	 * @return Whether or not the model is locked.
	 */
	public boolean isLocked() {
		return shop.isVisible() || portal.isVisible() || CheatManager.getInstance().isVisible();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (HAS_PORTAL ? 1231 : 1237);
		result = prime * result + ((bank == null) ? 0 : bank.hashCode());
		result = prime * result
				+ ((currentPlanet == null) ? 0 : currentPlanet.hashCode());
		result = prime * result + ((robot == null) ? 0 : robot.hashCode());
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
		GameModel other = (GameModel) obj;
		if (HAS_PORTAL != other.HAS_PORTAL)
			return false;
		if (bank == null) {
			if (other.bank != null)
				return false;
		} else if (!bank.equals(other.bank))
			return false;
		if (currentPlanet == null) {
			if (other.currentPlanet != null)
				return false;
		} else if (!currentPlanet.equals(other.currentPlanet))
			return false;
		if (robot == null) {
			if (other.robot != null)
				return false;
		} else if (!robot.equals(other.robot))
			return false;
		return true;
	}

	/**
	 * @return the unit
	 */
	public static int getUnit() {
		return UNIT;
	}
	
	/**
	 * @return A Dimension where both sides are UNIT.
	 */
	public static Dimension getSquareUnit() {
		return new Dimension(UNIT, UNIT);
	}
}