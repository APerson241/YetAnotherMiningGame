package info.angrynerds.yamg.ui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import info.angrynerds.yamg.engine.*;
import info.angrynerds.yamg.environment.Element;
import info.angrynerds.yamg.environment.ElementType;
import info.angrynerds.yamg.robot.Robot;
import info.angrynerds.yamg.utils.*;

import javax.swing.*;

public class WelcomeView {
	private JFrame frame;
	private Launcher yamg;
	private JPanel centerPanel;
	private ImagePanel image;
	
	private int status = 1; // 1 = animation, 2 = "click to continue", 3 = tutorial
	private StringBuilder stringShownYAMG = new StringBuilder();
	private StringBuilder stringShownYAMG2 = new StringBuilder();
	public int tutorialFrame;
	
	public WelcomeView(Launcher yamg) {
		this.yamg = yamg;
	}
	
	public void showWindow() {
		frame = new JFrame("Welcome to YAMG!");
		centerPanel = new JPanel();
			centerPanel.setLayout(new BorderLayout());
			image = new ImagePanel();
			centerPanel.add(BorderLayout.CENTER, image);
		frame.getContentPane().add(centerPanel);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(Helper.getCenteredBounds(890, 550));
		frame.setVisible(true);
		try {
			animateWelcome();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void animateWelcome() throws InterruptedException {
		Thread.sleep(1000);
		for(char c:"YAMG".toCharArray()) {
			stringShownYAMG.append(c);
			image.repaint();
			Thread.sleep(300);
		}
		for(String str:new String[] {"Yet ", "Another ", "Mining ", "Game"}) {
			stringShownYAMG2.append(str);
			image.repaint();
			Thread.sleep(300);
		}
		Thread.sleep(300);
		if(status != 3) status = 2; // If the user clicked to skip it, don't go back to status 2.
		image.repaint();
	}
	
	/*
	public void animateTutorial() throws InterruptedException {
		for(; tutorialFrame < 4; tutorialFrame++) {
			image.repaint();
			Thread.sleep(250);
		}
		Thread.sleep(750);
		tutorialFrame++; image.repaint();
		for(; tutorialFrame < 8; tutorialFrame++) {
			image.repaint();
			Thread.sleep(250);
		}
		Thread.sleep(750);
		tutorialFrame++; image.repaint();
		Thread.sleep(250);
		tutorialFrame++; image.repaint();
		Thread.sleep(750);
		tutorialFrame++; image.repaint();
		for(; tutorialFrame < 15; tutorialFrame++) {
			image.repaint();
			Thread.sleep(250);
		}
		Thread.sleep(750);
		tutorialFrame++; image.repaint();
		Thread.sleep(2000);
	}*/
	
	public void hideWindow() {
		frame.setVisible(false);
	}
	
	@SuppressWarnings("serial")
	private class ImagePanel extends JPanel implements MouseListener {
		public ImagePanel() {
			this.addMouseListener(this);
		}
		
		private void paintBackgroundAndStuff(Graphics g) {
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
			if(status <= 2) {
				g.setColor(Color.WHITE);
				g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 200));
				g.drawString(stringShownYAMG.toString(), 190, 200);
				g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 48));
				g.drawString(stringShownYAMG2.toString(), 110, 300);
			}
		}
		
		public void paintComponent(Graphics g) {
			switch(status) {
			case 1:
				paintBackgroundAndStuff(g);
				break;
			case 2:
				paintBackgroundAndStuff(g);
				drawContinueArrow(g, image.getWidth() - 200, image.getHeight() - 110);
				break;
			case 3:
				paintBackgroundAndStuff(g);
				drawOldTutorial(g, this.getSize());
				drawContinueArrow(g, image.getWidth() - 200, image.getHeight() - 200);
				break;
			}
			g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
			g.setColor((status == 3)?Color.BLACK:Color.YELLOW);
			g.drawString(Configurables.GAME_VERSION, 5, image.getHeight() - 10);
		}

		private final Dimension CONTINUE_ARROW_SIZE = new Dimension(150, 50);
		private final int ARROW_BODY_MARGIN = 10;
		private final int ARROW_HEAD_WIDTH = 30;
		/**
		 * Draws an arrow that says "Continue".
		 * Order of drawing:
		 * <pre>
		 *       6
		 *       |\
		 *       | \
		 * 1-----7  \
		 * |         \
		 * |          5
		 * |         /
		 * 2-----3  /
		 *       | /
		 *       |/
		 *       4
		 * </pre>
		 * @param g The Graphics object
		 * @param x The x-coordinate of the bounding box of the arrow
		 * @param y The y-coordinate of the bounding box of the arrow
		 * @param darkBackground Whether the arrow is to be drawn against a dark background
		 */
		private void drawContinueArrow(Graphics g, int x, int y) {
			boolean cont = status == 2; // true if we're drawing the "Continue" button and
			                            // false if we're drawing the "Start Game" button
			Polygon arrow = new Polygon(
					new int[] {x, x, x + CONTINUE_ARROW_SIZE.width - ARROW_HEAD_WIDTH,
							x + CONTINUE_ARROW_SIZE.width - ARROW_HEAD_WIDTH, x +
							CONTINUE_ARROW_SIZE.width,x + CONTINUE_ARROW_SIZE.width -
							ARROW_HEAD_WIDTH, x + CONTINUE_ARROW_SIZE.width - ARROW_HEAD_WIDTH},
					new int[] {y + ARROW_BODY_MARGIN, y + CONTINUE_ARROW_SIZE.height -
							ARROW_BODY_MARGIN, y + CONTINUE_ARROW_SIZE.height -
							ARROW_BODY_MARGIN, y + CONTINUE_ARROW_SIZE.height, y +
							CONTINUE_ARROW_SIZE.height / 2, y, y + ARROW_BODY_MARGIN},
					7);
			g.setColor(Color.GREEN);
			g.fillPolygon(arrow);
			g.setColor(cont?Color.WHITE:Color.BLACK);
			g.drawPolygon(arrow);
			g.setFont(new Font(Font.MONOSPACED, Font.BOLD, cont?24:20));
			g.drawString(cont?"Continue":"Start Game", x + (cont?10:5), y + 30);
		}

		public void mouseClicked(MouseEvent arg0) {}
		public void mouseEntered(MouseEvent arg0) {}
		public void mouseExited(MouseEvent arg0) {}
		public void mousePressed(MouseEvent arg0) {
			if(status == 2) {
				status = 3;
				image.repaint();
				/*(new Thread(new Runnable() {
					public void run() {
						try {
							animateTutorial();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						startButton.setEnabled(true);
						exitButton.setEnabled(true);
					}
				})).start();*/
			} else if(status == 3) {
				yamg.runApplication(); // yep, that's it
			}
		}
		public void mouseReleased(MouseEvent arg0) { }
	}
	
	/**
	 * The old, static version of the tutorial.
	 * 99% certain this will end up being hosted at
	 * <a href="thedailywtf.com">thedailywtf.com</a> someday.
	 */
	private void drawOldTutorial(Graphics g, Dimension size) {
		int yCoord = 50;
		g.setColor(Color.getHSBColor(172, 0, 75));
		g.fillRect(0, 0, size.width, size.height);
		g.setColor(Color.BLACK);
		Font original = g.getFont();
		g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
		String welcomeMessage = "Welcome to Yet Another Mining Game!";
		g.drawString(welcomeMessage, (image.getWidth() - g.getFontMetrics().stringWidth(
				welcomeMessage)) / 2, 25);
		g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
		g.drawString("Instructions", 23, yCoord);
		g.setFont(original);
		yCoord += 25;
		g.drawString("You are controlling a robot, represented by a friendly green " +
				"square:", 23, yCoord);
		yCoord += 10;
		Robot.paint(g, new Point(23, yCoord), GameModel.getSquareUnit(), false);
		g.setColor(Color.BLACK);
		yCoord += 45;
		yCoord = drawLinesOfText(g, 23, yCoord, new String[] {
				"The goal of the game is to gradually upgrade your robot. To upgrade,",
				"you need money, which you get from collecting elements. Elements are",
				"those little colored circles on the ground:"});
		ArrayList<Element> toDraw = new ArrayList<Element>();
		int xCoord = 23;
		for(ElementType type: ElementType.values()) {
			toDraw.add(new Element(type, new Point(xCoord, yCoord)));
			xCoord += 35;
		}
		for(Element elem: toDraw) {			// Draw sample elements
			g.setColor(elem.getColor());
			g.fillOval(elem.getLocation().x, elem.getLocation().y, 25, 25);
			g.setColor(Color.BLACK);
			g.drawString("$" + elem.getType().getPrice(), elem.getLocation().x,
					elem.getLocation().y + 45);
		}
		yCoord += 75;
		yCoord = drawLinesOfText(g, 23, yCoord, new String[] {
				"When you drive over each element, you will recieve the amount of money",
				"shown under each element and the element will disappear.", "",
				"Good luck!"});
	}
	
	/*
	char[] startSentence = "You are a robot.".toCharArray();
	/**
	 * Draw a frame of the tutorial animation.
	 * @param g
	 
	private void drawTutorial(Graphics g, int frame, Dimension size) {
		if (frame <= 16) {
			StringBuilder toDraw = new StringBuilder();
			for(int i = 0; i < frame; i++) {
				toDraw.append(startSentence[i]);
			}
			g.setColor(Color.WHITE);
			g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 48));
			g.drawString(toDraw.toString(), (size.width -
					g.getFontMetrics().stringWidth(toDraw.toString()))/2,
					(size.height - g.getFontMetrics().getHeight())/2);
		}
	}
	*/
	
	/**
	 * Draws the specified lines of text at the specified position, incrementing each line
	 * by 15.
	 * @param g The Graphics object.
	 * @param xCoord The x coordinate for all of the strings.
	 * @param yCoord The starting y coordinate.
	 * @param strings The strings to draw.
	 * @return The y coordinate after all of the strings have been drawn.
	 */
	public int drawLinesOfText(Graphics g, int xCoord, int yCoord, String...strings) {
		for(String str:strings) {
			g.drawString(str, xCoord, yCoord);
			yCoord += 15;
		}
		return yCoord + 15;
	}
	
	/**
	 * Overload of drawText(Graphics, int, int, String...)
	 * @param g The Graphics object.
	 * @param xCoord The x coordinate for all of the strings.
	 * @param yCoord The starting y coordinate.
	 * @param increment The amount the y coordinate increments after each line of text.
	 * @param strings The strings to draw.
	 * @return The y coordinate after all of the strings have been drawn.
	 */
	public int drawLinesOfText(Graphics g, int xCoord, int yCoord, int increment, String...strings) {
		for(String str:strings) {
			g.drawString(str, xCoord, yCoord);
			yCoord += increment;
		}
		return yCoord + increment;
	}
}