package info.angrynerds.yamg.utils;

import java.awt.*;

import javax.swing.*;

/**
 * Handles everything related to the debugging console.
 */
public class DebugConsole {
	private static DebugConsole instance = new DebugConsole();
	private JFrame frame;
	private JTextArea area;
	
	private DebugConsole() { }
	
	public static DebugConsole getInstance() {
		return instance;
	}
	
	public void println(String text) {
		if(frame == null) buildGUI();
		area.append("\n" + text);
	}
	
	private void buildGUI() {
		frame = new JFrame("YAMG Debug Console");
		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		area = new JTextArea();
		area.setEditable(false);
		area.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		area.setText("~~~~~ YAMG Debug Console ~~~~~");
		mainPanel.add(area);
		frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
		frame.setBounds(Helper.getCenteredBounds(600, 500));
	}
	
	public void setVisible(boolean visible) {
		if(frame == null) buildGUI();
		frame.setVisible(visible);
	}
	
	public void toggleVisible() {
		if(frame == null) buildGUI();
		frame.setVisible(!frame.isVisible());
		println("[DebugConsole/toggleVisible()] Visibility is " + isVisible());
	}
	
	public boolean isVisible() {
		if(frame == null) buildGUI();
		return frame.isVisible();
	}
}