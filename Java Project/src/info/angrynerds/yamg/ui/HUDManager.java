package info.angrynerds.yamg.ui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.*;

import info.angrynerds.yamg.*;
import info.angrynerds.yamg.robot.FuelTank;

public class HUDManager implements PropertyChangeListener {
	private GameModel model;
	
	private JLabel version = new JLabel(Yamg.VERSION);
	private JLabel money;
	private JLabel fuelTankInfo;
	private JProgressBar fuelTankBar;
	private JLabel reserveInfo;
	private JLabel dynamiteInfo;
	
	public HUDManager(GameModel model) {
		this.model = model;
		money = new JLabel(String.format("$%,d", this.model.getBankAccount().getMoney()));
		fuelTankInfo = new JLabel("Loading fuel tank info...");
		fuelTankBar = new JProgressBar();
		reserveInfo = new JLabel("Loading reserve info...");
		dynamiteInfo = new JLabel("Derp.");
		update();
	}
	
	public JPanel getPanel() {
		JPanel panel = new JPanel();
		panel.add(version);
		panel.add(new JLabel(" | "));
		panel.add(money);
		panel.add(new JLabel(" | "));
		panel.add(fuelTankInfo);
		panel.add(new JLabel(" | "));
		panel.add(fuelTankBar);
		panel.add(new JLabel(" | "));
		panel.add(reserveInfo);
		panel.add(new JLabel(" | "));
		panel.add(dynamiteInfo);
		return panel;
	}

	public void propertyChange(PropertyChangeEvent evt) {
		if(evt.getPropertyName().equals("money")) {
			update();
		}
	}
	
	public void update() {
		money.setText(String.format("$%,d", model.getBankAccount().getMoney()));
		FuelTank tank = model.getRobot().getFuelTank();
		if(tank.isInfinite()) {
			fuelTankInfo.setText("Fuel Tank: Tier DANIEL, Fuel Level: INFINITE");
			fuelTankBar.setValue(100);
			reserveInfo.setText("Fuel tank reserves: CHUCK NORRIS");
		} else {
			fuelTankInfo.setText("Fuel Tank: Tier " + tank.getTier() +
					", Fuel Level: " + tank.getFuelLevel() + "/" + tank.getFuelCapacity());
			fuelTankBar.setValue(tank.getFuelPercent());
			reserveInfo.setText("Fuel tank reserve" + (model.getRobot().getReserves() == 1 
					? "" : "s") + ": " + model.getRobot().getReserves());
		}
		if(model.isInfiniteDynamite()) {
			dynamiteInfo.setText("Dynamite: INFINITE!");
		} else {
			dynamiteInfo.setText("Dynamite: " + model.getRobot().getDynamite());
		}
	}
}