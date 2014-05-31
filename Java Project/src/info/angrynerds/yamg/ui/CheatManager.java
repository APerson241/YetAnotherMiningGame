package info.angrynerds.yamg.ui;

import info.angrynerds.yamg.utils.Helper;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles all the cheat codes.
 */
public class CheatManager {
	private Logger log = Logger.getGlobal();
	
	private boolean infiniteFuel;
	private boolean infiniteDynamite;
	private final Dimension WINDOW_SIZE = new Dimension(300, 200);
	private final int WINDOW_MARGIN = 5;
	private Rectangle window; // The bounds of the window which appears onscreen
	private boolean visible;
	private GameButton exitButton;
	private Rectangle moneyButton;
	private int moneyToAdd; // The money which should get added to the bank account
	private boolean constantsInitialized;
	
	private static CheatManager instance = new CheatManager();
	
	private List<PropertyChangeListener> listeners;
	
	private CheatManager() {
		listeners = new ArrayList<PropertyChangeListener>();
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		listeners.add(listener);
	}
	
	public void notifyPropertyChangeListeners(String property, int oldVal, int newVal) {
		PropertyChangeEvent event = new PropertyChangeEvent(this, property, oldVal, newVal);
		for(PropertyChangeListener listener:listeners) {
			listener.propertyChange(event);
		}
	}
	
	public static CheatManager getInstance() { return instance; }
	
	private void initialize() {
		window = Helper.getCenteredBounds(WINDOW_SIZE, GamePanel.windowDimension);
		exitButton = new GameButton(new Rectangle(window.x + window.width - 55, window.y + 5, 50,
				20), "Exit");
		
		moneyButton = new Rectangle(window.x + 5, window.y + 50, 100, 40);
		constantsInitialized = true;
	}
	
	public void paint(Graphics g, int yOffset) {
		if(!visible) return;
		if(!constantsInitialized) initialize();
		g.setColor(Color.BLACK);
		g.fillRect(window.x - WINDOW_MARGIN, window.y - WINDOW_MARGIN, window.width +
				WINDOW_MARGIN * 2, window.height + WINDOW_MARGIN * 2);
		g.setColor(Color.WHITE);
		g.fillRect(window.x, window.y, window.width, window.height);
		exitButton.paint(g);
		g.setColor(Color.GREEN);
		g.fillRect(moneyButton.x, moneyButton.y, moneyButton.width, moneyButton.height);
		g.setColor(Color.BLACK);
		g.drawString("Add $50K", moneyButton.x + 5, moneyButton.y + moneyButton.height / 2 + 10);
	}
	
	public void paint(Graphics g) { paint(g, 0); }

	/**
	 * @return whether it's visible
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * @param visible the visible to set
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	/**
	 * @return the infiniteFuel
	 */
	public boolean isInfiniteFuel() {
		return infiniteFuel;
	}

	/**
	 * @param infiniteFuel the infiniteFuel to set
	 */
	public void setInfiniteFuel(boolean infiniteFuel) {
		this.infiniteFuel = infiniteFuel;
	}

	/**
	 * @return the infiniteDynamite
	 */
	public boolean isInfiniteDynamite() {
		return infiniteDynamite;
	}

	/**
	 * @param infiniteDynamite the infiniteDynamite to set
	 */
	public void setInfiniteDynamite(boolean infiniteDynamite) {
		this.infiniteDynamite = infiniteDynamite;
	}

	/**
	 * Handle a mouse click.
	 * @param point The mouse click location.
	 */
	public void mouseClick(Point point) {
		if(exitButton.getBounds().contains(point) || !window.contains(point)) {
			setVisible(false); return;
		} else if(moneyButton.contains(point)) {
			log.logp(Level.FINE, getClass().getSimpleName(), "mouseClick()", "Setting money to add...");
			setMoneyToAdd(50000);
			log.logp(Level.FINE, getClass().getSimpleName(), "mouseClick()", "Set money to add.");
		}
	}

	/**
	 * @return the moneyToAdd
	 */
	public int getMoneyToAdd() {
		return moneyToAdd;
	}

	/**
	 * @param moneyToAdd the moneyToAdd to set
	 */
	public void setMoneyToAdd(int moneyToAdd) {
		int old = this.moneyToAdd;
		this.moneyToAdd = moneyToAdd;
		notifyPropertyChangeListeners("moneyToAdd", old, this.moneyToAdd);
	}

	public void clearMoneyToAdd() {
		moneyToAdd = 0;
	}
}