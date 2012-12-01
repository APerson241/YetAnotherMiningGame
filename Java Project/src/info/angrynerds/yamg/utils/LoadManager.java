package info.angrynerds.yamg.utils;

import java.awt.*;

import javax.swing.*;

public class LoadManager {
	private JFrame frame;
	private JProgressBar bar;
	
	private int finalProgress = 100;
	private int currentProgress = 0;
	
	public LoadManager() {
		buildGUI();
	}
	
	private void buildGUI() {
		frame = new JFrame("Loading Yamg...");
		JPanel mainPanel = new JPanel();
		mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		mainPanel.setLayout(new BorderLayout());
		bar = new JProgressBar();
		bar.setStringPainted(true);
		mainPanel.add(bar);
		frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setBounds(Helper.getCenteredBounds(500, 500));
	}
	
	public void initialize(int finalProgress) {
		this.finalProgress = finalProgress;
		frame.setVisible(true);
	}
	
	public void i() {
		currentProgress++;
		if(currentProgress < finalProgress) {
		bar.setValue((int)((currentProgress/finalProgress)*100));
		} else {
			bar.setValue(100);
			frame.setVisible(false);
		}
	}
}
