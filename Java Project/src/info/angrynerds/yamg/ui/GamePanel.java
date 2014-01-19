package info.angrynerds.yamg.ui;

import info.angrynerds.yamg.*;
import info.angrynerds.yamg.robot.Element;
import info.angrynerds.yamg.robot.Robot;
import info.angrynerds.yamg.utils.Configurables;
import info.angrynerds.yamg.utils.DebugConsole;
import info.angrynerds.yamg.utils.Direction;
import info.angrynerds.yamg.utils.Helper;

import java.awt.*;

import javax.swing.*;

@SuppressWarnings("serial")
public class GamePanel extends JPanel {
	/**
	 * How big the client window is
	 */
	public static Dimension windowDimension;
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
		boolean underground = yOffset <= -GameModel.GROUND_LEVEL;
		// Version
		g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
		g.setColor(Color.WHITE);
		g.drawString(Yamg.VERSION, 5, getHeight() - 10);
		// Money
		g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
		g.setColor(new Color(0, underground?255:0, 0, 128));
		String money = String.format("$%,d", model.getBankAccount().getMoney());
		int moneyX = getWidth() - g.getFontMetrics().stringWidth(money) - 5;
		g.drawString(money, moneyX, 25);
		// Fuel
		final int fuelProgressBarWidth = 200;
		g.setColor(new Color(underground?255:0, underground?255:0, underground?255:0, 172));
		g.drawRect(moneyX - fuelProgressBarWidth - 5, 6, fuelProgressBarWidth, 20);
		g.fillRect(moneyX - fuelProgressBarWidth - 5, 6,
				model.getRobot().getFuelTank().getFuelPercent() * fuelProgressBarWidth / 100, 20);
		g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
		g.setColor(Color.WHITE);
		g.drawString("Fuel", moneyX - fuelProgressBarWidth/2 - 15, 19);
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
		Graphics2D g2D = (Graphics2D) g;
		g2D.setPaint(new GradientPaint(windowDimension.width / 2, 0, Color.LIGHT_GRAY, windowDimension.width / 2,
				GameModel.GROUND_LEVEL + yOffset, Color.CYAN));
		g2D.fillRect(0, yOffset, windowDimension.width, GameModel.GROUND_LEVEL);
		g.setColor(DEFAULT_GROUND);
		g.fillRect(0, yOffset + GameModel.GROUND_LEVEL, windowDimension.width, Configurables.BOTTOM);
		model.getShop().paint(g);
		if(model.HAS_PORTAL) model.getPortal().paintOutside(g);
		g.setColor(Color.LIGHT_GRAY);
		for(Rectangle rect:model.getHoles())
			if(isYCoordInsideWindow(rect.y))
				g.fillRect(rect.x, rect.y + yOffset, GameModel.getUnit(), GameModel.getUnit());
		Robot.paint(g, model.getRobotLocation(), GameModel.getSquareUnit(),
				model.getRobot().isDead());
		g.setColor(Color.BLACK);
		for(Rectangle rect:model.getRocks())
			if(isYCoordInsideWindow(rect.y))
				g.fillRect(rect.x, rect.y + yOffset, GameModel.getUnit(), GameModel.getUnit());
		for(Element elem:model.getElements()) {
			if(isYCoordInsideWindow(elem.getLocation().y)) {
				g.setColor(elem.getColor());
				g.fillOval(elem.getLocation().x, elem.getLocation().y + yOffset,
					GameModel.getUnit(), GameModel.getUnit());
			}
		}
		flyups.paint(g, yOffset);
		if(model.getRobot().isDead() && model.getRobot().getReserves() <= 0) {
			g.setColor(new Color(255, 255, 255, 200));
			Rectangle r = Helper.getCenteredBounds(new Dimension(800, 200), getSize());
			g.fillRect(r.x, r.y, r.width, r.height);
			g.setColor(new Color(0, 0, 255, 200));
			int calcY = (this.getHeight() + 50)/2;	// Centered on the vertical axis
			int yOff = 50;	// How far the other two lines are offset from the center line
			g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 72));
			g.drawString("GAME OVER", (this.getWidth() - 700)/2, calcY - yOff);
			g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 48));
			g.drawString("You ran out of fuel - so you died.", (this.getWidth() - 745)/2, calcY);
			g.setColor(Color.BLACK);
			g.drawString("Press ENTER to restart!", (this.getWidth() - 615)/2, calcY + yOff);
		} else if(model.isLocked()) {
			g.setColor(new Color(0, 0, 0, 128));
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
			if(model.getPortal().isVisible())
				model.getPortal().paintWindow(g);
		}
		if(!model.FIRST_STEP) {
			int x = model.getRobotLocation().x;
			int y = model.getRobotLocation().y;
			g.setColor(new Color(1, 1, 1, 0.75F));
			g.fillRect(x - GameModel.getUnit() - 5, y - GameModel.getUnit() - 30, 190, 50);
			g.setColor(Color.BLACK);
			g.drawRect(x - GameModel.getUnit() - 5, y - GameModel.getUnit() - 30, 190, 50);
			g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
			g.drawString("Use the arrow keys", x - GameModel.getUnit(), y - GameModel.getUnit() - 10);
			g.drawString("to move the robot!", x - GameModel.getUnit(), y - GameModel.getUnit() + 10);
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
		default:
			DebugConsole.getInstance().println(
					"[GameView/canScroll()] Invalid scroll direction: " + up.toString());
			break;
		}
	}

	public int getScroll() {
		return yOffset;
	}
}