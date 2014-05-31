package info.angrynerds.yamg.utils;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.DateFormat;
import java.util.Date;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import javax.swing.*;

/**
 * Handles everything related to the debugging console/logger.
 */
public class DebugConsole extends Handler {
	private Logger log = Logger.getGlobal();
	
	private JFrame frame;
	private JTextArea area;
	private DateFormat dateFormat = DateFormat.getTimeInstance(DateFormat.MEDIUM);
	private static final String SAVE_MENU_ITEM_TEXT = "Save As Log File...";
	private static final String HIDE_MENU_ITEM_TEXT = "Hide Window";
	
	public void printlnWithTimestamp(String text) {
		println("[" + dateFormat.format(new Date()) + "] " + text);
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
		area.setText("#Version: " + Configurables.GAME_VERSION + "\n#Date: " + (new Date()).toString());
		mainPanel.add(new JScrollPane(area));
		
		JMenuBar menuBar = new JMenuBar();
		JMenu actionsMenu = new JMenu("Actions");
		JMenuItem saveMenuItem = new JMenuItem(SAVE_MENU_ITEM_TEXT);
		saveMenuItem.addActionListener(new MenuListener());
		actionsMenu.add(saveMenuItem);
		JMenuItem hideMenuItem = new JMenuItem(HIDE_MENU_ITEM_TEXT);
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
		log.exiting(getClass().getSimpleName(), "setVisible(boolean)");
	}
	
	public void toggleVisible() {
		if(frame == null) buildGUI();
		frame.setVisible(!frame.isVisible());
	}
	
	public boolean isVisible() {
		if(frame == null) buildGUI();
		return frame.isVisible();
	}
	
	/**
	 * A listener class to handle the two menu items.
	 */
	class MenuListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			// Making a lot of assumptions about what could possibly result in an event
			String command = ((JMenuItem) arg0.getSource()).getText();
			if(command.equals(HIDE_MENU_ITEM_TEXT)) {
				toggleVisible();
			} else if(command.equals(SAVE_MENU_ITEM_TEXT)) {
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
						log.throwing(getClass().getSimpleName(), "actionPerformed(ActionEvent)", ex);
						ex.printStackTrace();
					} finally {
						if(printWriter != null) {
							printWriter.close();
						}
					}
					log.logp(Level.INFO, getClass().getSimpleName(), "actionPerformed(ActionEvent)",
							"Wrote log to \"" + logFile.getName() + "\".");
				} else {
					log.logp(Level.WARNING, getClass().getSimpleName(), "actionPerformed(ActionEvent)",
							"User canceled file selection.");
				}
			}
		}
	}

	@Override
	public void close() throws SecurityException {
		setVisible(false);
	}

	@Override
	public void flush() {}

	@Override
	public void publish(LogRecord record) {
		println("[" + dateFormat.format(new Date()) + " - " + record.getLevel().toString() +
				" - " + record.getSourceClassName() + "/" + record.getSourceMethodName() + "] " +
				record.getMessage());
	}
}