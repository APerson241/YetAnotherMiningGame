package info.angrynerds.yamg.utils;

import info.angrynerds.yamg.Yamg;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.DateFormat;
import java.util.Date;

import javax.swing.*;

/**
 * Handles everything related to the debugging console.
 */
public class DebugConsole {
	private static DebugConsole instance = new DebugConsole();
	private JFrame frame;
	private JTextArea area;
	private DateFormat dateFormat;
	private final String saveMenuItemText = "Save As Log File...";
	private final String hideMenuItemText = "Hide Window";
	
	private DebugConsole() {
		dateFormat = DateFormat.getTimeInstance(DateFormat.MEDIUM);
	}
	
	public static DebugConsole getInstance() {
		return instance;
	}
	
	public void println(String text) {
		if(frame == null) buildGUI();
		area.append("\n[" + dateFormat.format(new Date()) + "] " + text);
	}
	
	private void buildGUI() {
		frame = new JFrame("YAMG Debug Console");
		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		area = new JTextArea();
		area.setEditable(false);
		area.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		area.setText("#Version: " + Yamg.VERSION + "\n#Date: " + (new Date()).toString());
		mainPanel.add(new JScrollPane(area));
		
		JMenuBar menuBar = new JMenuBar();
		JMenu actionsMenu = new JMenu("Actions");
		JMenuItem saveMenuItem = new JMenuItem(saveMenuItemText);
		saveMenuItem.addActionListener(new MenuListener());
		actionsMenu.add(saveMenuItem);
		JMenuItem hideMenuItem = new JMenuItem(hideMenuItemText);
		hideMenuItem.addActionListener(new MenuListener());
		actionsMenu.add(hideMenuItem);
		menuBar.add(actionsMenu);
		frame.setJMenuBar(menuBar);
		
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
	
	/**
	 * A listener class to handle the two menu items.
	 * @author John
	 *
	 */
	public class MenuListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			// Making a lot of assumptions about what could possibly result in an event
			String command = ((JMenuItem) arg0.getSource()).getText();
			if(command.equals(hideMenuItemText)) {
				toggleVisible();
			} else if(command.equals(saveMenuItemText)) {
				JFileChooser chooser = new JFileChooser();
				int returnVal = chooser.showSaveDialog(frame);
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					File logFile = chooser.getSelectedFile();
					PrintWriter printWriter = null;
					try {
						printWriter = new PrintWriter(new FileWriter(logFile));
						printWriter.write(area.getText()); // lazy, I know
						printWriter.flush();
						printWriter.close();
					} catch(IOException ex) {
						println("[DebugConsole/MenuListener] ERROR in writing log file:");
						println("[DebugConsole/MenuListener] " + ex.getMessage());
						ex.printStackTrace();
					} finally {
						if(printWriter != null) {
							printWriter.close();
						}
					}
					println("[DebugConsole/MenuListener] Wrote log to \"" + logFile.getName() + "\".");
				} else {
					println("[DebugConsole/MenuListener] User cancelled file selection.");
				}
			}
		}
		
	}
}