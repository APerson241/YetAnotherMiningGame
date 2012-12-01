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
		location = new Rectangle(50, 175, GameModel.UNIT, GameModel.UNIT);
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
}