package info.angrynerds.yamg.ui;

import info.angrynerds.yamg.*;
import info.angrynerds.yamg.robot.Element;
import info.angrynerds.yamg.utils.Direction;
import info.angrynerds.yamg.utils.Helper;

import java.awt.*;
import java.awt.image.BufferedImage;

import javax.swing.*;

@SuppressWarnings("serial")
public class GamePanel extends JPanel {
	
	/**
	 * How big the client window is
	 */
	private Dimension windowDimension;
	private GameModel model;
	private FlyUpsManager flyups;
	
	/**
	 * Used for scrolling
	 */
	private int yOffset = 0;
	
	public static final Color DEFAULT_GROUND = Color.DARK_GRAY;
	
	public GamePanel(Dimension d, GameModel model, FlyUpsManager flyups) {
		windowDimension = d;
		this.model = model;
		this.flyups = flyups;
	}
	
	private void drawHUD(Graphics g) {
		g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
		g.setColor((yOffset <= -GameModel.GROUND_LEVEL)?Color.GREEN:Color.BLACK);
		Font font = g.getFont();
		BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		FontMetrics fm = img.getGraphics().getFontMetrics(font);
		String money = String.format("$%,d", model.getBankAccount().getMoney());
		g.drawString(money, getWidth() - fm.stringWidth(money), 15);
	}
	
	private boolean isYCoordInsideWindow(int yCoord) {
		return (yCoord >= Math.abs(yOffset)) 
		&& (yCoord <= (windowDimension.height + Math.abs(yOffset)));
	}
	
	/**
	 * This is THE paint method.  It paints the entire game window.  Very, very important.
	 * @param g The graphics thingy.
	 */
	public void paintComponent(Graphics g) {
		g.setColor(Color.CYAN);
		g.fillRect(0, yOffset, windowDimension.width, GameModel.GROUND_LEVEL);
		g.setColor(DEFAULT_GROUND);
		g.fillRect(0, yOffset + GameModel.GROUND_LEVEL, windowDimension.width, GameModel.BOTTOM);
		model.getShop().paint(g);
		if(model.HAS_PORTAL) model.getPortal().paint(g);
		g.setColor(Color.LIGHT_GRAY);
		for(Rectangle rect:model.getHoles()) {
			if(isYCoordInsideWindow(rect.y)) {
				g.fillRect(rect.x, rect.y + yOffset, GameModel.UNIT, GameModel.UNIT);
			}
//			System.out.println("Filled a hole.");
		}
		g.setColor(Color.BLACK);
		for(Rectangle rect:model.getRocks()) {
			if(isYCoordInsideWindow(rect.y)) {
				g.fillRect(rect.x, rect.y + yOffset, GameModel.UNIT, GameModel.UNIT);
			}
		}
		for(Element elem:model.getElements()) {
			if(isYCoordInsideWindow(elem.getLocation().y)) {
				g.setColor(elem.getColor());
				g.fillOval(elem.getLocation().x, elem.getLocation().y + yOffset,
					GameModel.UNIT, GameModel.UNIT);
			}
		}
		g.setColor(model.getRobot().isDead()?Color.RED:Color.GREEN);
		g.fillRoundRect(model.getRobotLocation().x, model.getRobotLocation().y + yOffset,
				GameModel.UNIT, GameModel.UNIT,
				9, 9);
		flyups.paint(g, yOffset);
		if(model.getRobot().isDead() && model.getRobot().getReserves() <= 0) {
			g.setColor(new Color(255, 255, 255, 200));
			Rectangle r = Helper.getCenteredBounds(new Dimension(800, 200), getSize());
			g.fillRect(r.x, r.y, r.width, r.height);
			g.setColor(new Color(0, 0, 255, 200));
			int calcY = (this.getHeight() + 50)/2;	// Centered on the vertical axis
			int yOff = 50;	// How far the other two lines are offset from the center line
			g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 72));
			g.drawString("LOL YOU ARE DEAD", (this.getWidth() - 700)/2, calcY - yOff);
			g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 48));
			g.drawString("Now you have to start over again", (this.getWidth() - 745)/2, calcY);
			g.setColor(Color.BLACK);
			g.drawString("Press ENTER to restart! =D", (this.getWidth() - 615)/2, calcY + yOff);
		} else if(model.LOCKED) {
			g.setColor(new Color(0, 0, 0, 0.5F));
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
		}
		if(!model.FIRST_STEP) {
			int x = model.getRobotLocation().x;
			int y = model.getRobotLocation().y;
			g.setColor(new Color(1, 1, 1, 0.75F));
			g.fillRect(x - GameModel.UNIT - 5, y - GameModel.UNIT - 30, 190, 50);
			g.setColor(Color.BLACK);
			g.drawRect(x - GameModel.UNIT - 5, y - GameModel.UNIT - 30, 190, 50);
			g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
			g.drawString("Use the arrow keys", x - GameModel.UNIT, y - GameModel.UNIT - 10);
			g.drawString("to move the robot!", x - GameModel.UNIT, y - GameModel.UNIT + 10);
		}
		drawHUD(g);
	}

	public void scroll(Direction up, int i) {
		switch(up) {
		case UP:
			if(yOffset >= -4600) yOffset -= i; 
			break;
		case DOWN:
			if(yOffset < 0) yOffset += i;
			break;
		}
	}

	public int getScroll() {
		return yOffset;
	}
}