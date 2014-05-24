package info.angrynerds.yamg.ui;

import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import info.angrynerds.yamg.GameModel;
import info.angrynerds.yamg.utils.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Along with options, also takes care of cheat codes.
 */
public class OptionsManager {
	private JFrame frame;
	private JCheckBox gravity;
	private JSlider gravityDelay;
	private JLabel gravityLabel;
	// GENERAL
	private JCheckBox fpsStatus;
	private JCheckBox autoScroll;
	private JCheckBox refresh;
	private JLabel refreshLabel;
	private JSlider refreshSlider;
	private JSlider unitSlider;
	private JLabel unitLabel;
	// DEBUGGING
	private JCheckBox gravityDebugStatus;
	private JCheckBox autoscrollStatus;
	private JCheckBox gravityOnStatus;
	private JCheckBox bossStatus;
	private JCheckBox scrollStatus;
	private JCheckBox notifyViewRefresh;
	
	private GameModel model;
	
	private static OptionsManager instance = new OptionsManager();
	private List<PropertyChangeListener> listeners;
	
	private OptionsManager() {}
	
	public static OptionsManager getInstance() {
		return instance;
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		listeners.add(listener);
	}
	
	public void notifyPropertyChangeListeners() {
		PropertyChangeEvent event = new PropertyChangeEvent(this, null, false, false);
		for(PropertyChangeListener listener:listeners) {
			listener.propertyChange(event);
		}
	}
	
	// A helper method.
	private JPanel buildTopPanel() {
		JPanel top = new JPanel();
		top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
		// --------- GRAVITY PANEL ---------
		JPanel gravityPanel = new JPanel();
		gravityPanel.setBorder(BorderFactory.createTitledBorder("Gravity"));
		gravity = new JCheckBox("Enable Gravity");
			gravity.addActionListener(new ButtonListener());
			gravity.setSelected(false);
		gravityPanel.add(gravity);
		gravityLabel = new JLabel("");
		gravityPanel.add(gravityLabel);
		gravityDelay = new JSlider();
			gravityDelay.addChangeListener(new GravityListener());
			gravityDelay.setMinimum(50);
			gravityDelay.setMaximum(2500);
			gravityDelay.setMajorTickSpacing(200);
			gravityDelay.setMinorTickSpacing(100);
			gravityDelay.setValue(1000);
			gravityDelay.setSnapToTicks(true);
			gravityDelay.setPaintTicks(true);
			gravityDelay.setEnabled(false);
		gravityPanel.add(gravityDelay);
		top.add(gravityPanel);
		// --------- UNIT SIZE PANEL ---------
		JPanel unitPanel = new JPanel();
		unitPanel.setBorder(BorderFactory.createTitledBorder("Unit Size"));
		unitLabel = new JLabel("Unit size: 25 (default 25)");
		unitPanel.add(unitLabel);
		unitSlider = new JSlider();
			unitSlider.setMinimum(5);
			unitSlider.setMaximum(200);
			unitSlider.setMajorTickSpacing(50);
			unitSlider.setMinorTickSpacing(5);
			unitSlider.setValue(25);
			unitSlider.setPaintTicks(true);
			unitSlider.setSnapToTicks(true);
			unitSlider.addChangeListener(new UnitListener());
			unitSlider.addMouseListener(new UnitListener());
		unitPanel.add(unitSlider);
		JButton unitResetButton = new JButton("Reset");
		unitResetButton.addActionListener(new UnitListener());
		unitPanel.add(unitResetButton);
		top.add(unitPanel);
		return top;
	}
	
	private void buildGUI() {
		frame = new JFrame("Preferences");
		JPanel mainPanel = new JPanel();
		mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		mainPanel.setLayout(new BorderLayout());
		JPanel center = new JPanel(new GridLayout(1, 2));
		JPanel generalOptions = new JPanel();
			generalOptions.setBorder(BorderFactory.createTitledBorder("General"));
			generalOptions.setLayout(new BoxLayout(generalOptions, BoxLayout.Y_AXIS));
			fpsStatus = new JCheckBox("Show FPS on status bar");
			fpsStatus.setSelected(true);
			generalOptions.add(fpsStatus);
			autoScroll = new JCheckBox("Enable Autoscroll");
			autoScroll.addActionListener(new ButtonListener());
			autoScroll.setSelected(true);
			generalOptions.add(autoScroll);
			refresh = new JCheckBox("Refresh Screen");
			refresh.addActionListener(new ButtonListener());
			refresh.setSelected(true);
			generalOptions.add(refresh);
			refreshLabel = new JLabel("?");
			generalOptions.add(refreshLabel);
			refreshSlider = new JSlider();
				refreshSlider.addChangeListener(new RefreshListener());
				refreshSlider.setMinimum(250);
				refreshSlider.setMaximum(1000);
				refreshSlider.setMajorTickSpacing(100);
				refreshSlider.setMinorTickSpacing(25);
				refreshSlider.setValue(1000);
				refreshSlider.setSnapToTicks(true);
				refreshSlider.setPaintTicks(true);
			generalOptions.add(refreshSlider);
		center.add(generalOptions);
		JPanel debugOptions = new JPanel();
			debugOptions.setBorder(BorderFactory.createTitledBorder("Debugging"));
			debugOptions.setLayout(new BoxLayout(debugOptions, BoxLayout.Y_AXIS));
			gravityDebugStatus = new JCheckBox("Gravity debug info on status bar");
			gravityDebugStatus.addActionListener(new ButtonListener());
			debugOptions.add(gravityDebugStatus);
			autoscrollStatus = new JCheckBox("Autoscroll on/off on status bar");
			autoscrollStatus.addActionListener(new ButtonListener());
			debugOptions.add(autoscrollStatus);
			gravityOnStatus = new JCheckBox("Gravity on/off on status bar");
			gravityOnStatus.addActionListener(new ButtonListener());
			debugOptions.add(gravityOnStatus);
			scrollStatus = new JCheckBox("Scroll position on/off on status bar");
			scrollStatus.addActionListener(new ButtonListener());
			debugOptions.add(scrollStatus);
			notifyViewRefresh = new JCheckBox("View refresh notifications on/off");
			notifyViewRefresh.addActionListener(new ButtonListener());
			debugOptions.add(notifyViewRefresh);
		center.add(debugOptions);
		mainPanel.add(buildTopPanel(), BorderLayout.NORTH);
		mainPanel.add(center, BorderLayout.CENTER);
		frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
		frame.setBounds(Helper.getCenteredBounds(550, 350));
	}
	
	public void setVisible(boolean visible) {
		if(frame == null) buildGUI();
		frame.setVisible(visible);
	}
	
	private class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			if(arg0.getSource() instanceof JCheckBox) {
				JCheckBox source = (JCheckBox) arg0.getSource();
				if(source.getText().equals("Enable Gravity")) {
					gravityDelay.setEnabled(source.isSelected());
					gravityLabel.setText(source.isSelected()?
							String.format("Gravity Delay: %d ms", gravityDelay.getValue()):"");
					model.setGravity(source.isSelected());
				} else if(source.getText().equals("Enable Autoscroll")) {
					model.getView().setAutoscroll(source.isSelected());
				} else if(source.getText().equals(refresh.getText())) {
					fpsStatus.setEnabled(refresh.isSelected());
					model.getView().setRefreshing(refresh.isSelected());
				}
			}
			model.getView().refreshView();
		}
	}

	public int getGravityDelay() {
		if(frame == null) buildGUI();
		return gravityDelay.getValue();
	}

	public boolean isGravityDebugStatus() {
		if(frame == null) buildGUI();
		return gravityDebugStatus.isSelected();
	}

	public boolean isGravityOnStatus() {
		if(frame == null) buildGUI();
		return gravityOnStatus.isSelected();
	}

	public boolean isAutoscrollOnStatus() {
		if(frame == null) buildGUI();
		return autoscrollStatus.isSelected();
	}
	
	public boolean isBossOnStatus() {
		if(frame == null) buildGUI();
		return bossStatus.isSelected();
	}
	
	public boolean isScrollPositionOnStatus() {
		if(frame == null) buildGUI();
		return scrollStatus.isSelected();
	}
	
	public int getRefreshDelay() {
		return refreshSlider.getValue();
	}
	
	public boolean isFPSStatus() {
		if(frame == null) buildGUI();
		return fpsStatus.isSelected();
	}

	private class GravityListener implements ChangeListener {
		public void stateChanged(ChangeEvent arg0) {
			gravityLabel.setText(gravity.isSelected()?String.format("Gravity Delay: %d ms", gravityDelay.getValue()):"");
		}
	}
	
	private class RefreshListener implements ChangeListener {
		public void stateChanged(ChangeEvent arg0) {
			refreshLabel.setText(String.format("Refresh Delay: %d ms", refreshSlider.getValue()));
		}
	}
	
	private class UnitListener implements ChangeListener, MouseListener, ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			GameModel.setUnit(25);
			unitSlider.setValue(25);
			unitLabel.setText("Unit size: 25 (default 25)");
			model.getView().refreshView();
		}
		
		public void stateChanged(ChangeEvent arg0) {
			GameModel.setUnit(unitSlider.getValue());
			unitLabel.setText("Unit size: " + GameModel.getUnit() + " (default 25)");
			model.getView().refreshView();
		}

		public void mouseClicked(MouseEvent arg0) {}
		public void mouseEntered(MouseEvent arg0) {}
		public void mouseExited(MouseEvent arg0) {}
		public void mousePressed(MouseEvent arg0) {}

		public void mouseReleased(MouseEvent arg0) {
			GameModel.setUnit(unitSlider.getValue());
			unitLabel.setText("Unit size: " + GameModel.getUnit() + " (default 25)");
			model.getView().refreshView();
		}
	}

	public boolean isNotifyViewRefresh() {
		if(frame == null) buildGUI();
		return notifyViewRefresh.isSelected();
	}
	
	public boolean isRefresh() {
		if(frame == null) buildGUI();
		return refresh.isSelected();
	}
}