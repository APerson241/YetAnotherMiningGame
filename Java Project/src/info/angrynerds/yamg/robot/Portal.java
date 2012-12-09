package info.angrynerds.yamg.robot;

import info.angrynerds.yamg.*;
import info.angrynerds.yamg.utils.Helper;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.event.*;

/**
 * This class should handle all aspects of switching between planets, holding planets, and other
 * related tasks.
 * @author Daniel Glus (APerson241)
 */
public class Portal {
	private Rectangle bounds;
	
	private JFrame frame;
	private GameModel model;
	private JLabel statusBar;
	private JList planetsList;
	
	private List<Planet> planets;
	
	private JButton scanButton;
	private JButton colonizeButton;
	
	public Portal(GameModel m) {
		model = m;
		int UNIT = GameModel.UNIT;
		bounds = new Rectangle(UNIT * 24, UNIT * 5, UNIT * 3, UNIT * 3);
		planets = new ArrayList<Planet>();
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
		frame = new JFrame("Mr. Real Estate");
		JPanel mainPanel = new JPanel();
		mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		mainPanel.setLayout(new BorderLayout());
		planetsList = new JList(getPlanets().toArray());
			planetsList.addListSelectionListener(new UserListener());
		mainPanel.add(planetsList);
		JPanel right = new JPanel();
			right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
			scanButton = new JButton("Scan for Planets ($50)");
			scanButton.setName("scanButton");
			scanButton.addActionListener(new UserListener());
			right.add(scanButton);
			right.add(Box.createVerticalStrut(5));
			colonizeButton = new JButton("Colonize Planet ($500)");
			colonizeButton.setName("colonizeButton");
			colonizeButton.addActionListener(new UserListener());
			right.add(colonizeButton);
		statusBar = new JLabel("Loading...");
		frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
		frame.getContentPane().add(BorderLayout.EAST, right);
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
		scanButton.setEnabled(model.getBankAccount().getMoney() >= 50);
		colonizeButton.setEnabled((!planetsList.isSelectionEmpty() 
				&& !getSelectedPlanet().isColonized()) && model.getBankAccount().getMoney() >= 500);
		planetsList.setListData(planets.toArray());
		statusBar.setText("Money: " + Helper.formatMoney(model.getBankAccount().getMoney()) +
				" | Planets unlocked: " + getPlanets().size());
	}
	
	public Planet getSelectedPlanet() {
		return (Planet) planetsList.getSelectedValue();
	}
	
	String[] planetNames = new String[] {"Amel", "Antar", "Avalon", "Belzagor", "Cyteen"};
	/**
	 * This method generates a new planet.
	 * @return A brand-new planet.
	 */
	private Planet generateNewPlanet() {
		String name = planetNames[(int) Math.floor(Math.random() * (planetNames.length - 1))];
		int bottom = 5000;
		if(Math.random() <= 0.5) bottom = 3000;
		else if(Math.random() <= 0.5) bottom = 7000;
		return new Planet(model, name, false, bottom);
	}
	
	public class UserListener implements ActionListener, ListSelectionListener {
		public void actionPerformed(ActionEvent arg0) {
			if(arg0.getSource() instanceof JButton) {
				JButton button = (JButton)arg0.getSource();
				if(button.getName().equals("colonizeButton")) {
					getSelectedPlanet().setColonized(true);
					model.getBankAccount().withdraw(500);
				} else if(button.getName().equals("scanButton")) {
					Planet newPlanet = generateNewPlanet();
					newPlanet.setScanned(true);
					planets.add(newPlanet);
					model.getBankAccount().withdraw(50);
				}
			}
			model.doRefresh();
		}

		public void valueChanged(ListSelectionEvent arg0) {
			
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

	public void addPlanet(Planet currentPlanet) {
		planets.add(currentPlanet);
	}
	
	public List<Planet> getPlanets() {
		return planets;
	}
}