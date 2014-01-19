package info.angrynerds.yamg.utils;

import java.awt.*;

/**
 * Where we put all the "magic numbers".
 * @author Daniel Glus
 * @version 0.5
 */
public abstract class Configurables {
	/**
	 * The amount of money you start with.
	 */
	public static final int STARTING_MONEY = 100;
	/**
	 * The side of one game square.
	 */
	public static final int DEFAULT_UNIT = 25;
	/**
	 * The standard y-coordinate of the bottom of the map.
	 */
	public static final int BOTTOM = 5000;
	/**
	 * The distance from the edge of the window to the edge of the game window, in pixels.
	 */
	public static final int SCREEN_MARGIN = 100;
	/**
	 * The distance below ground level that a rock has to be for the dynamite help message 
	 * to be displayed next to it, in pixels.
	 */
	public static final int ROCK_DISTANCE = 50;
	/**
	 * The distance from the edge of the Portal window to the edge of the game window, in pixels.
	 */
	public static final int PORTAL_MARGIN = 100;
	/**
	 * The margin for the "galaxy pane" in the Portal window
	 */
	public static final int GALAXY_MARGIN = 50;
	/**
	 * The starting price for scanning for a planet.
	 */
	public static final int BASE_SCAN_PRICE = 50;
	/**
	 * The price for colonizing a planet.
	 */
	public static final int COLONIZE_PRICE = 2500;
	/**
	 * The price for traveling to another planet.
	 */
	public static final int TRAVEL_PRICE = 50000;
	/**
	 * The max dynamite level.
	 */
	public static final int MAX_DYNAMITE_LEVEL = 5;
	
	/**
	 * @return How big the rectangle announcing that the game is over should be.
	 */
	public static Dimension GAME_OVER_SCREEN() { return new Dimension(800, 200); }
}