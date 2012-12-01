package info.angrynerds.yamg.robot;

import info.angrynerds.yamg.GameModel;
import info.angrynerds.yamg.utils.Helper;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class Portal {
	private Rectangle bounds;
	
	private JFrame frame;
	private GameModel model;
	private JLabel statusBar;
	
	private int planetsUnlocked = 1;
	
	public Portal(GameModel m) {
		model = m;
		int UNIT = GameModel.UNIT;
		bounds = new Rectangle(UNIT * 24, UNIT * 5, UNIT * 3, UNIT * 3);
	}

	public void setBounds(Rectangle bounds) {
		this.bounds = bounds;
	}

	public Rectangle getBounds() {
		return bounds;
	}
	
	public void paint(Graphics g) {
		int yOffset = model.getView().getPanel().getScroll();
		g.setColor(new Color(146, 107, 61));	// A nice brown
		g.fillRect(bounds.x, bounds.y + yOffset, bounds.width, bounds.height);
		g.setColor(Color.BLACK);
		Font original = g.getFont();
		g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
		g.drawString("REAL", bounds.x + ((int)(GameModel.UNIT / 10)),
				bounds.y + (GameModel.UNIT) + yOffset);
		g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
		g.drawString("ESTATE", bounds.x + ((int) (GameModel.UNIT / 10)),
				bounds.y + (GameModel.UNIT) + yOffset + 20);
		g.setFont(original);
	}
	
	private void buildGUI() {
		frame = new JFrame("Mr. Shop");
		JPanel mainPanel = new JPanel();
		mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		statusBar = new JLabel("Loading...");
		frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
		frame.getContentPane().add(BorderLayout.SOUTH, statusBar);
		frame.addKeyListener(new PortalListener());
		frame.addWindowListener(new PortalListener());
		frame.setBounds(Helper.getCenteredBounds(600, 500));
	}
	
	public void setDialogVisible(boolean visible) {
		if(frame == null) buildGUI();
		model.doRefresh();
		frame.setVisible(visible);
	}
	
	public void update() {
		if(frame == null) buildGUI();
		statusBar.setText("Money: " + Helper.formatMoney(model.getBankAccount().getMoney()) +
				" | Planets unlocked: " + planetsUnlocked);
	}
	
	public class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			
		}
	}
	
	private class PortalListener implements KeyListener, WindowListener {
		public void keyPressed(KeyEvent arg0) {
			if(arg0.getKeyCode() == KeyEvent.VK_ESCAPE) {
				frame.setVisible(false);
			}
		}

		public void keyReleased(KeyEvent arg0) {}
		public void keyTyped(KeyEvent arg0) {}

		public void windowActivated(WindowEvent arg0) {}
		public void windowClosed(WindowEvent arg0) {}
		public void windowClosing(WindowEvent arg0) {
			model.LOCKED = false;
			model.getView().refreshView();
		}
		public void windowDeactivated(WindowEvent arg0) {}
		public void windowDeiconified(WindowEvent arg0) {}
		public void windowIconified(WindowEvent arg0) {}
		public void windowOpened(WindowEvent arg0) {}
	}
}