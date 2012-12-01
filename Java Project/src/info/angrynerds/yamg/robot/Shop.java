package info.angrynerds.yamg.robot;

import info.angrynerds.yamg.GameModel;
import info.angrynerds.yamg.utils.Helper;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class Shop {
	private Rectangle bounds;
	
	private JFrame frame;
	private GameModel model;
	private JLabel statusBar;
	private ShopComponents s;
	
	public Shop(GameModel m) {
		model = m;
		int UNIT = GameModel.UNIT;
		bounds = new Rectangle(UNIT * 12, UNIT * 5, UNIT * 3, UNIT * 3);
		s = new ShopComponents();
	}

	public void setBounds(Rectangle bounds) {
		this.bounds = bounds;
	}

	public Rectangle getBounds() {
		return bounds;
	}
	
	public void paint(Graphics g) {
		int yOffset = model.getView().getPanel().getScroll();
		g.setColor(Color.MAGENTA);
		g.fillRect(bounds.x, bounds.y + yOffset, bounds.width, bounds.height);
		g.setColor(Color.BLACK);
		Font original = g.getFont();
		g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
		g.drawString("SHOP", bounds.x + ((int)(GameModel.UNIT / 5)),
				bounds.y + (GameModel.UNIT * 2) + yOffset);
		g.setFont(original);
	}

	public void showDialog() {
		if(frame == null) {
			buildGUI();
		}
		model.doRefresh();
		frame.setVisible(true);
	}

	/**
	 * Builds the GUI.
	 */
	private void buildGUI() {
		frame = new JFrame("Mr. Shop");
		JPanel mainPanel = new JPanel();
		mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		JPanel fuelPanel = new JPanel();
			fuelPanel.setBorder(BorderFactory.createTitledBorder("Fuel"));
			fuelPanel.setLayout(new BoxLayout(fuelPanel, BoxLayout.Y_AXIS));
			s.fuelBar = new JProgressBar();
			fuelPanel.add(s.fuelBar);
			s.fuelLabel = new JLabel("Loading fuel tank info...");
			fuelPanel.add(s.fuelLabel);
			fuelPanel.add(new JLabel("Exchange rate: $1 = 1 fuel"));
			JPanel fillUp = new JPanel();
				fillUp.add(new JLabel("Amount of fuel:"));
				String[] choices = new String[] {"$1", "$5", "$10", "$20", "Fill 'er up"};
				s.fuelBox = new JComboBox(choices);
					s.fuelBox.addActionListener(new ButtonListener());
					s.fuelBox.setSelectedIndex(4);
				fillUp.add(s.fuelBox);
				s.fuelButton = new JButton("Add Fuel");
					s.fuelButton.addActionListener(new ButtonListener());
				fillUp.add(s.fuelButton);
			fuelPanel.add(fillUp);
		mainPanel.add(fuelPanel);
		JPanel reservePanel = new JPanel();
			reservePanel.setBorder(BorderFactory.createTitledBorder("Fuel Reserve Tank"));
			reservePanel.setLayout(new GridLayout(2, 1));
			s.reserveLabel = new JLabel("Loading...");
			reservePanel.add(s.reserveLabel);
			JPanel reserveButtonPanel = new JPanel(new GridLayout(1, 2));
			((GridLayout) reserveButtonPanel.getLayout()).setHgap(5);
			s.reserveButton = new JButton("Buy Another Reserve ($100)");
				s.reserveButton.addActionListener(new ButtonListener());
			reserveButtonPanel.add(s.reserveButton);
			s.reserveSellButton = new JButton("Sell A Reserve ($90)");
				s.reserveSellButton.addActionListener(new ButtonListener());
			reserveButtonPanel.add(s.reserveSellButton);
			reservePanel.add(reserveButtonPanel);
		mainPanel.add(reservePanel);
		JPanel dynamitePanel = new JPanel();
			dynamitePanel.setBorder(BorderFactory.createTitledBorder("Dynamite"));
			dynamitePanel.setLayout(new GridLayout(2, 1));
			s.dynamiteLabel = new JLabel("Loading...");
			dynamitePanel.add(s.dynamiteLabel);
			JPanel dynamiteButtonPanel = new JPanel(new GridLayout(1, 2));
			((GridLayout) dynamiteButtonPanel.getLayout()).setHgap(5);
			s.dynamiteButton = new JButton("Buy Some Dynamite ($150)");
				s.dynamiteButton.addActionListener(new ButtonListener());
			dynamiteButtonPanel.add(s.dynamiteButton);
			s.dynamiteSellButton = new JButton("Sell Some Dynamite ($140)");
				s.dynamiteSellButton.addActionListener(new ButtonListener());
			dynamiteButtonPanel.add(s.dynamiteSellButton);
			dynamitePanel.add(dynamiteButtonPanel);
		mainPanel.add(dynamitePanel);
		JPanel upgradePanel = new JPanel();
			upgradePanel.setBorder(BorderFactory.createTitledBorder("Upgrades"));
			GridLayout gl = new GridLayout(2, 3);
			gl.setHgap(5);
			gl.setVgap(5);
			upgradePanel.setLayout(gl);
			s.upgradeFuelLabel = new JLabel("Loading...");
			upgradePanel.add(new JLabel("Fuel Tank"));
			upgradePanel.add(s.upgradeFuelLabel);
			s.upgradeFuelButton = new JButton("Loading...");
				s.upgradeFuelButton.addActionListener(new ButtonListener());
			upgradePanel.add(s.upgradeFuelButton);
			upgradePanel.add(new JLabel("Dynamite"));
			s.upgradeDynamiteLabel = new JLabel("Loading...");
			upgradePanel.add(s.upgradeDynamiteLabel);
			s.upgradeDynamiteButton = new JButton("Loading...");
			s.upgradeDynamiteButton.addActionListener(new ButtonListener());
			upgradePanel.add(s.upgradeDynamiteButton);
		mainPanel.add(upgradePanel);
		JPanel unlockPanel = new JPanel(new GridLayout(2, 1));
			unlockPanel.setBorder(BorderFactory.createTitledBorder("Unlocks"));
			s.unlockPortalLabel = new JLabel("Loading...");
			unlockPanel.add(s.unlockPortalLabel);
			s.unlockPortalButton = new JButton("Unlock Portal ($30,000)");
			s.unlockPortalButton.addActionListener(new ButtonListener());
			unlockPanel.add(s.unlockPortalButton);
		mainPanel.add(unlockPanel);
		statusBar = new JLabel("Loading...");
		frame.addKeyListener(new ShopListener());
		frame.addWindowListener(new ShopListener());
		frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
		frame.getContentPane().add(BorderLayout.SOUTH, statusBar);
		frame.setBounds(Helper.getCenteredBounds(600, 550));
	}
	
	public void update() {
		if((frame != null) && (model != null)) {
			FuelTank tank = model.getRobot().getFuelTank();
			assert (tank != null) : "The tank was null!";	// Sanity check
			// FUEL
			if(tank.isInfinite()) {
				s.fuelBar.setValue(100);
				s.fuelLabel.setText("Fuel Tank: Tier OMG, Fuel Level: INFINITE");
				s.fuelButton.setText("Add Fuel ($0)");
				s.fuelButton.setEnabled(false);
				s.fuelBox.setEnabled(false);
			} else {
				s.fuelBar.setValue(tank.getFuelPercent());
				s.fuelLabel.setText("Tier " + tank.getTier() +
						", Fuel Level: " + tank.getFuelLevel() + "/" +
						tank.getFuelCapacity() + ".");
				s.fuelButton.setText("Add Fuel ($" + Helper.formatComma(
						getSelectedFuelAmount()) + ")");
				s.fuelButton.setEnabled((model.getBankAccount().getMoney() 
								>= getSelectedFuelAmount()) 
						&& ((getSelectedFuelAmount() + tank.getFuelLevel()) 
								<= tank.getFuelCapacity())
						&& (!tank.isFull()));
				s.fuelBox.setEnabled((model.getBankAccount().getMoney() > 0) && !tank.isFull());
			}
			// RESERVE
			s.reserveLabel.setText(getReserveLabelText());
			s.reserveButton.setEnabled((model.getBankAccount().getMoney() 
					>= 100) && !tank.isInfinite());
			s.reserveSellButton.setEnabled((model.getRobot().getReserves() > 0) 
					|| model.isInfiniteDynamite());
			// DYNAMITE
			s.dynamiteLabel.setText(getDynamiteLabelText());
			s.dynamiteButton.setEnabled(model.getBankAccount().getMoney() >= 150);
			s.dynamiteSellButton.setEnabled(model.getRobot().getDynamite() > 0);
			// UPGRADE
			if(tank.isInfinite()) {
				s.upgradeFuelLabel.setText("Tier OMG, Capacity: INFINITE units.");
				s.upgradeFuelButton.setText("Upgrade to Tier IMPOSSIBURU ($INFINITY)");
				s.upgradeFuelButton.setEnabled(false);
			} else if(tank.getTier() >= 20) {
				s.upgradeFuelLabel.setText("Tier 20, Capacity: " + tank.getFuelCapacity() + " units.");
				s.upgradeFuelButton.setText("Fully Upgraded!");
				s.upgradeFuelButton.setEnabled(false);
			} else {
				s.upgradeFuelLabel.setText("Tier " + tank.getTier() +
						", Capacity: " + tank.getFuelCapacity() + " units.");
				s.upgradeFuelButton.setText("Upgrade Tank (" +
						Helper.formatMoney(tank.getMoneyNeededForNextTier()) + ")");
				s.upgradeFuelButton.setEnabled(model.getBankAccount().getMoney() >
					tank.getMoneyNeededForNextTier());
			}
			int dt = model.getRobot().getDynamiteTier();
			s.upgradeDynamiteLabel.setText(String.format("Tier %,d, Blast radius: %,d", dt, dt));
			if(model.getRobot().getDynamiteTier() >= 2) {
				s.upgradeDynamiteButton.setText("Fully Upgraded!");
				s.upgradeDynamiteButton.setEnabled(false);
			} else {
				int dNeeded = 10 + (((int)Math.pow(dt + 1, 2)) * 20);
				s.upgradeDynamiteButton.setText(String.format("Upgrade Dynamite ($%,d)", dNeeded));
				s.upgradeDynamiteButton.setEnabled(model.getBankAccount().getMoney() >= dNeeded);
			}
			// UNLOCK
			s.unlockPortalLabel.setText(model.HAS_PORTAL?"You've already unlocked the portal!"
					:"The Portal.  Unlocks a world of possibilities.");
			s.unlockPortalButton.setEnabled(!model.HAS_PORTAL 
					&& (model.getBankAccount().getMoney() >= 30000));
			statusBar.setText("Money: " + Helper.formatMoney(model.getBankAccount().getMoney()));
		}
	}
	
	private String getReserveLabelText() {
		if(model.getRobot().getFuelTank().isInfinite()) {
			return "Reserves not needed: INFINITE FUEL.";
		} else {
			if(model.getRobot().getReserves() == 0) {
				return "You don't have any reserves!";
			} else if(model.getRobot().getReserves() == 1) {
				return "You have 1 fuel reserve.";
			} else {
				return "You have " +
				Helper.formatComma(model.getRobot().getReserves()) + " fuel reserves.";
			}
		}
	}
	
	private String getDynamiteLabelText() {
		if(model.isInfiniteDynamite()) {
			return "You have INFINITE packs of dynamite!";
		} else if(model.getRobot().getDynamite() == 0) {
			return "You don't have any packs of dynamite!";
		} else if(model.getRobot().getDynamite() == 1) {
			return "You have 1 pack of dynamite.";
		} else {
			return "You have " +
				Helper.formatComma(model.getRobot().getDynamite()) + " packs of dynamite.";
		}
	}
	
	private int getSelectedFuelAmount() {
		if(s.fuelBox == null) return 500;
		int index = s.fuelBox.getSelectedIndex();
		FuelTank tank = model.getRobot().getFuelTank();
		switch(index) {
		case 0:
			return 1;
		case 1:
			return 5;
		case 2:
			return 10;
		case 3:
			return 20;
		case 4:
			int amount = tank.getFuelCapacity() - tank.getFuelLevel();
			return amount;
		}
		return 0;
	}
	
	private class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			if(arg0.getSource() instanceof JButton) {
				JButton button = (JButton) arg0.getSource();
				FuelTank tank = model.getRobot().getFuelTank();
				if(button.getText().startsWith("Add Fuel")) {
					model.getBankAccount().withdraw(getSelectedFuelAmount());
					model.getRobot().refuel(getSelectedFuelAmount());
				} else if(button.getText().startsWith("Upgrade Tank")) {
					model.getBankAccount().withdraw(tank.getMoneyNeededForNextTier());
					tank.upgrade();
				} else if(button.getText().startsWith("Upgrade Dynamite")) {
					model.getBankAccount().withdraw(10 +
							(((int)Math.pow(model.getRobot().getDynamiteTier() + 1, 2)) * 5));
					model.getRobot().upgradeDynamite();
				} else if(button.getText().equals("Buy Another Reserve ($100)")) {
					model.getBankAccount().withdraw(100);
					model.getRobot().addReserve();
				} else if(button.getText().equals("Sell A Reserve ($90)")) {
					model.getBankAccount().deposit(90);
					model.getRobot().sellReserve();
				} else if(button.getText().equals("Buy Some Dynamite ($150)")) {
					model.getBankAccount().withdraw(150);
					model.getRobot().addDynamite();
				} else if(button.getText().equals("Sell Some Dynamite ($140)")) {
					model.getBankAccount().deposit(140);
					if(!model.isInfiniteDynamite()) {
						model.getRobot().sellDynamite();
					}
				} else if(button.getText().equals(s.unlockPortalButton.getText())) {
					model.getBankAccount().withdraw(30000);
					model.HAS_PORTAL = true;
				}
				model.doRefresh();
			} else if(arg0.getSource() instanceof JComboBox) {
				if(frame.isVisible()) model.doRefresh();
			}
		}
	}
	
	private class ShopListener implements KeyListener, WindowListener {
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

	public void hideDialog() {
		if(frame == null) buildGUI();
		frame.setVisible(false);
	}
}