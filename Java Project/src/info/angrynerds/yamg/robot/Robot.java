package info.angrynerds.yamg.robot;

import info.angrynerds.yamg.GameModel;
import info.angrynerds.yamg.utils.*;

import java.awt.*;
import java.beans.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class Robot implements Serializable {
	private Rectangle location;
	private Point storedLocation;
	
	private FuelTank tank;
	private int reserves = 0;
	private int dynamite = 0;
	private int dynamiteTier = 1;
	
	private boolean dead = false;
	
	private List<PropertyChangeListener> listeners;
	
	public Robot() {
		location = new Rectangle(50, 175, GameModel.getUnit(), GameModel.getUnit());
		tank = new FuelTank();
		listeners = new ArrayList<PropertyChangeListener>();
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		listeners.add(listener);
	}
	
	public void notifyPropertyChangeListeners(PropertyChangeEvent event) {
		for(PropertyChangeListener listener:listeners) {
			listener.propertyChange(event);
		}
	}
	
	public void move(int distance, Direction direction) {
		if(!dead) {
			move(distance, direction, true);
			if(tank.isEmpty()) {
				dead = true;
				notifyPropertyChangeListeners(new PropertyChangeEvent(this, "dead", false, true));
			}
		}
	}
	
	/**
	 * Paints the thing.
	 */
	public static void paint(Graphics g, Point position, Dimension size, boolean dead) {
		int x0 = position.x, y0 = position.y;
		g.setColor(dead?Color.RED:Color.GREEN);
		int arcSize = (int) Math.floor((1/5) * 0.5 * (size.width + size.height));
		g.fillRoundRect(x0, y0, size.width, size.height, arcSize, arcSize);
		g.setColor(Color.BLACK);
		g.fillRect(x0 + size.width/10, y0 + size.height/5, 4 *
				size.width / 5, size.height / 10); // Black bar
		g.setColor(Color.BLUE);
		g.fillOval(x0 + 3 * size.width / 5, y0 + size.height/20,
				size.width / 10, size.height / 10); // Blue circle
		g.setColor(Color.BLACK);
		g.fillOval(x0 + 4 * size.width / 5, y0 + size.height/20,
				size.width / 10, size.height / 10); // Black circle
		g.drawRect(x0 + size.width / 5, y0 + 9 * size.height/20, 3 *
				size.width / 5, 5 * size.height / 12);
		g.drawRect(x0 + 2 * size.width / 5, y0 + 9 * size.height/20, size.width / 5,
				5 * size.height / 12);
		g.drawRect(x0 + size.width / 5, y0 + size.height/2 + 5 * size.height/36,
				3 * size.width / 5, 5 * size.height / 36);
	}
	
	public void useReserve() {
		if(reserves >= 1) {
			if(tank.isEmpty()) {
				dead = false;
			}
			tank.setFuelLevel(tank.getFuelCapacity());
			reserves--;
		}
	}
	
	public void addReserve() {
		reserves++;
	}
	
	public void useDynamite() {
		if(dynamite >= 1) {
			notifyPropertyChangeListeners(new PropertyChangeEvent(this, "dynamite",
					dynamite, --dynamite));
		}
	}
	
	public void addDynamite() {
		dynamite++;
	}
	
	public int getDynamiteTier() {
		return dynamiteTier;
	}
	
	public void upgradeDynamite() {
		dynamiteTier++;
	}
	
	public void storeLocation() {
		storedLocation.x = location.x;
		storedLocation.y = location.y;
	}
	
	public void goToLocation() {
		location.x = storedLocation.x;
		location.y = storedLocation.y;
	}
	
	public Point getStoredLocation() {
		return storedLocation;
	}
	
	public void move(int distance, Direction direction, boolean useFuel) {
		switch(direction) {
		case UP:
			location.y -= distance; break;
		case DOWN:
			location.y += distance; break;
		case LEFT: 
			location.x -= distance; break;
		case RIGHT:
			location.x += distance; break;
		default:
			assert false: "Bad direction: " + location;
		}
		if(useFuel) tank.useFuel(1);
	}
	
	public FuelTank getFuelTank() {
		return tank;
	}
	
	public void refuel(int fuel) {
		tank.refuel(fuel);
		if(!tank.isEmpty()) {
			setDead(false);
		}
	}

	public Point getLocation() {
		return new Point(location.x, location.y);
	}

	public Dimension getSize() {
		return new Dimension(location.width, location.height);
	}

	public Rectangle getRect() {
		return location;
	}

	public int getReserves() {
		return reserves;
	}
	
	public int getDynamite() {
		return dynamite;
	}

	public void setDead(boolean dead) {
		this.dead = dead;
	}

	public boolean isDead() {
		return dead;
	}

	public void sellReserve() {
		reserves--;
	}

	public void sellDynamite() {
		dynamite--;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (dead ? 1231 : 1237);
		result = prime * result + dynamite;
		result = prime * result + dynamiteTier;
		result = prime * result
				+ ((listeners == null) ? 0 : listeners.hashCode());
		result = prime * result
				+ ((location == null) ? 0 : location.hashCode());
		result = prime * result + reserves;
		result = prime * result
				+ ((storedLocation == null) ? 0 : storedLocation.hashCode());
		result = prime * result + ((tank == null) ? 0 : tank.hashCode());
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
		Robot other = (Robot) obj;
		if (dead != other.dead)
			return false;
		if (dynamite != other.dynamite)
			return false;
		if (dynamiteTier != other.dynamiteTier)
			return false;
		if (listeners == null) {
			if (other.listeners != null)
				return false;
		} else if (!listeners.equals(other.listeners))
			return false;
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		if (reserves != other.reserves)
			return false;
		if (storedLocation == null) {
			if (other.storedLocation != null)
				return false;
		} else if (!storedLocation.equals(other.storedLocation))
			return false;
		if (tank == null) {
			if (other.tank != null)
				return false;
		} else if (!tank.equals(other.tank))
			return false;
		return true;
	}
}