package info.angrynerds.yamg.robot;

import info.angrynerds.yamg.*;
import info.angrynerds.yamg.ui.GamePanel;
import info.angrynerds.yamg.utils.*;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * This class should handle all aspects of switching between planets, holding planets, and other
 * related tasks.
 * @author Daniel Glus (APerson241)
 */
public class Portal {
	private Rectangle bounds; // In the game world, the bounds of the box that says "Real Estate"
	private Rectangle exitButton, scanButton, colonizeButton, travelButton, galaxyPane;
	private GameModel model;
	private List<Planet> planets;
	private Map<Planet, Rectangle> hitboxes;
	private Planet selectedPlanet;
	private boolean visible;
	private Rectangle window; // The bounds of the window that appears on the screen
	private int xOff, yOff; // Offsets for painting the PLANETS, not anything else
	private int planetNameIndex = 0; // How far we are through the list of planets
	private int scanPrice = Configurables.BASE_SCAN_PRICE;

	final int HALO = 5; // Width, in pixels, of halo
	final int PLANET_SIZE = 50; // In galaxy pane, width and height of planets
	String[] PLANET_NAMES = new String[] {"Amel", "Antar", "Avalon", "Belzagor", "Cyteen", "Ytterby"};
	Color buttonEnabledColor = Color.getHSBColor(176, 196, 222);
	Color buttonDisabledColor = Color.GRAY;
	
	public Portal(GameModel m) {
		model = m;
		int UNIT = GameModel.getUnit();
		bounds = new Rectangle(UNIT * 24, UNIT * 5, UNIT * 3, UNIT * 3);
		planets = new ArrayList<Planet>();
		hitboxes = new HashMap<Planet, Rectangle>();
		visible = false;
	}
	
	private void initialize() {
		window = Helper.getCenteredBounds(new Dimension(GamePanel.windowDimension.width
				- Configurables.PORTAL_MARGIN * 2, GamePanel.windowDimension.height -
				Configurables.PORTAL_MARGIN * 2), GamePanel.windowDimension);
		window.setLocation(window.x, window.y - 50); // Shift black window a little bit up so it's centered
		xOff = window.width / 2 + window.x;
		yOff = window.height / 2 + window.y;
		galaxyPane = new Rectangle(window.x + Configurables.GALAXY_MARGIN, window.y +
				Configurables.GALAXY_MARGIN, window.width -
				Configurables.GALAXY_MARGIN * 2, window.height - Configurables.GALAXY_MARGIN * 2);
		exitButton = new Rectangle(window.x + window.width - 55, window.y + 5, 50, 20);
		scanButton = new Rectangle(window.x + (window.width - 80) / 2, window.y + window.height - 35, 90, 20);
		colonizeButton = new Rectangle(window.x + (window.width - 80) / 2 + 150, window.y + window.height - 35, 130, 20);
		travelButton = new Rectangle(window.x + (window.width - 80) / 2 - 150, window.y + window.height - 35, 120, 20);
	}

	public void setBounds(Rectangle bounds) {
		this.bounds = bounds;
	}

	public Rectangle getBounds() {
		return bounds;
	}
	
	/**
	 * Paints the square that appears in the game world
	 */
	public void paintOutside(Graphics g) {
		int yOffset = model.getView().getPanel().getScroll();
		g.setColor(new Color(146, 107, 61));	// A nice brown
		g.fillRect(bounds.x, bounds.y + yOffset, bounds.width, bounds.height);
		g.setColor(Color.BLACK);
		Font original = g.getFont();
		g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
		g.drawString("REAL", bounds.x + ((int)(GameModel.getUnit() / 10)),
				bounds.y + (GameModel.getUnit()) + yOffset);
		g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
		g.drawString("ESTATE", bounds.x + ((int) (GameModel.getUnit() / 10)),
				bounds.y + (GameModel.getUnit()) + yOffset + 20);
		g.setFont(original);
	}
	
	/**
	 * @param x_o The x-coord of the planet with respect to the "galaxy".
	 * @param y_o The y-coord of the planet with respect to the "galaxy".
	 */
	private void paintPlanet(Graphics g, Planet planet, int x_o, int y_o) {
		int x = x_o + xOff - PLANET_SIZE / 2, y = y_o + yOff - PLANET_SIZE / 2;
		if(planet.equals(selectedPlanet)) {
			g.setColor(Color.WHITE);
			g.fillOval(x - HALO, y - HALO, PLANET_SIZE + HALO * 2, PLANET_SIZE + HALO * 2);
		}
		g.setColor(planet.containsRobot()?Color.CYAN:(planet.isHomePlanet()?
				Color.GREEN:(planet.isColonized()?Color.BLUE:Color.DARK_GRAY)));
		g.fillOval(x, y, PLANET_SIZE, PLANET_SIZE);
		if(planet.containsRobot() || planet.equals(selectedPlanet)) {
			int textWidth = g.getFontMetrics().stringWidth(planet.getName());
			g.setColor(Color.WHITE);
			g.drawString(planet.getName(), x + PLANET_SIZE / 2 - textWidth / 2,
					y_o + PLANET_SIZE + yOff + 5);
		}
		if(!hitboxes.containsKey(planet))
			hitboxes.put(planet, new Rectangle(x, y, PLANET_SIZE, PLANET_SIZE));
	}
	
	private void paintButton(Graphics g, String name, Rectangle box, Color color) {
		g.setColor(color);
		g.fillRect(box.x, box.y, box.width, box.height);
		g.setColor(Color.BLACK);
		g.drawRect(box.x, box.y, box.width, box.height);
		g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
		g.drawString(name, box.x + (box.width - g.getFontMetrics().stringWidth(name)) / 2,
				box.y + (box.height) * 3 / 4);
	}
	
	/**
	 * Paints the actual window.
	 */
	public void paintWindow(Graphics g) {
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(window.x, window.y, window.width, window.height);
		g.setColor(Color.BLACK);
		g.drawRect(window.x, window.y, window.width, window.height);
		// Title
		g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 24));
		g.drawString("Mr. Portal", window.x + (window.width -
				g.getFontMetrics().stringWidth("Mr. Portal"))/2, window.y + 25);
		//Buttons
		int money = model.getBankAccount().getMoney();
		paintButton(g, "Exit", exitButton, new Color(172, 0, 0));
		paintButton(g, "Scan ($" + scanPrice + ")", scanButton,
				(money>=scanPrice)?buttonEnabledColor:buttonDisabledColor);
		paintButton(g, "Colonize ($" + Configurables.COLONIZE_PRICE + ")", colonizeButton,
				(money>=Configurables.COLONIZE_PRICE)?buttonEnabledColor:buttonDisabledColor);
		paintButton(g, "Travel ($" + Configurables.TRAVEL_PRICE + ")", travelButton,
				(money>=Configurables.TRAVEL_PRICE)?buttonEnabledColor:buttonDisabledColor);
		// Galaxy pane
		g.fillRect(galaxyPane.x, galaxyPane.y, galaxyPane.width, galaxyPane.height);
		// For making them appear in a spiral
		int x = 0, y = 0, dx = 0, dy = -1, t = (int)Math.ceil(Math.sqrt(planets.size()));
		for(Planet planet : planets) {
			if(galaxyPane.intersects(new Rectangle((x * 100) + xOff - 75/2, (y * 100) +
					yOff - 75/2, 75, 75)))
				paintPlanet(g, planet, x * 100, y * 100);
			if((x == y) || ((x < 0) && (x == -y)) || ((x > 0) && (x == 1-y)))
	            t = dx; dx =- dy; dy = t;
	        x += dx; y += dy;
		}
	}
	
	public Rectangle getWindowBounds() {
		return window;
	}
	
	/**
	 * Sets whether or not the window is visible.
	 */
	public void setVisible(boolean visible) {
		if(window == null) {
			initialize();
		}
		this.visible = visible;
	}
	
	/**
	 * Returns whether or not the window is visible.
	 */
	public boolean isVisible() {
		return visible;
	}
	
	public void mouseClick(Point point) {
		if(exitButton.contains(point) || !getWindowBounds().contains(point)) {
			setVisible(false); return;
		} else if(scanButton.contains(point) && model.getBankAccount().getMoney() >= scanPrice) {
			Planet newPlanet = generateNewPlanet();
			newPlanet.setScanned(true);
			planets.add(newPlanet);
			model.getBankAccount().withdraw(scanPrice);
			scanPrice += Configurables.BASE_SCAN_PRICE;
		} else if(colonizeButton.contains(point) && model.getBankAccount().getMoney() >=
				Configurables.COLONIZE_PRICE && selectedPlanet != null &&
				!selectedPlanet.isColonized()) {
			selectedPlanet.setColonized(true);
			model.getBankAccount().withdraw(Configurables.COLONIZE_PRICE);
		} else if(selectedPlanet != null && galaxyPane.contains(point)) {
			selectedPlanet = null;
		}
		for(Planet planet:planets) {
			if(hitboxes.containsKey(planet) && hitboxes.get(planet).contains(point)) {
				selectedPlanet = planet;
				return;
			}
		}
	}
	
	/**
	 * This method generates a new planet.
	 * @return A brand-new planet.
	 */
	private Planet generateNewPlanet() {
		if(++planetNameIndex >= PLANET_NAMES.length) planetNameIndex = 0;
		String name = PLANET_NAMES[planetNameIndex];
		int bottom = 5000;
		if(Math.random() <= 0.5) bottom = 3000;
		else if(Math.random() <= 0.5) bottom = 7000;
		return new Planet(model, name, false, Planet.getStandardWidth(), bottom);
	}

	public void addPlanet(Planet currentPlanet) {
		planets.add(currentPlanet);
	}
	
	public List<Planet> getPlanets() {
		return planets;
	}
}