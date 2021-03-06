package info.angrynerds.yamg.ui;

import info.angrynerds.yamg.engine.*;
import info.angrynerds.yamg.utils.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class GameView {
	private Logger log = Logger.getGlobal();
	
	private Launcher yamg;
	private AboutView aboutView;
	
	private JFrame frame;
	private JLabel statusBar;
	
	private GamePanel panel;
	private GameModel model;
	private FlyUpsManager flyups;
	
	private Runnable refreshJob;
	private boolean isRefreshing = false;
	private float fps = 123;

	private boolean autoscroll = true;
	
	public GameView(Launcher yam, GameModel model) {
		yamg = yam;
		this.model = model;
	}
	
	private void buildGUI() {
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		frame = new JFrame("Yet Another Mining Game");
		panel = new GamePanel(new Dimension((screen.width - 100), screen.height - 100),
				model, flyups);
		panel.addMouseListener(new PanelClickListener());
		statusBar = new JLabel(getStatusBarText());
		aboutView = new AboutView();
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
			JMenuItem newMenuItem = new JMenuItem("New Game");
				newMenuItem.addActionListener(new MenuBarListener());
			fileMenu.add(newMenuItem);
			JMenuItem openMenuItem = new JMenuItem("Open Game...");
				openMenuItem.addActionListener(new MenuBarListener());
			fileMenu.add(openMenuItem);
			JMenuItem saveMenuItem = new JMenuItem("Save Game As...");
				saveMenuItem.addActionListener(new MenuBarListener());
			fileMenu.add(saveMenuItem);
			fileMenu.addSeparator();
			JMenuItem optionsMenuItem = new JMenuItem("Preferences...");
				optionsMenuItem.addActionListener(new MenuBarListener());
			fileMenu.add(optionsMenuItem);
			fileMenu.addSeparator();
			JMenuItem exitMenuItem = new JMenuItem("Quit");
				exitMenuItem.addActionListener(new MenuBarListener());
			fileMenu.add(exitMenuItem);
		menuBar.add(fileMenu);
		JMenu advancedMenu = new JMenu("Advanced");
			JMenuItem cheatMenuItem = new JMenuItem("Cheat Code Manager...");
				cheatMenuItem.addActionListener(new MenuBarListener());
			advancedMenu.add(cheatMenuItem);
			JMenuItem debugMenuItem = new JMenuItem("Toggle debug window");
				debugMenuItem.addActionListener(new MenuBarListener());
			advancedMenu.add(debugMenuItem);
		menuBar.add(advancedMenu);
		JMenu helpMenu = new JMenu("Help");
			JMenuItem aboutMenuItem = new JMenuItem("About YAMG...");
				aboutMenuItem.addActionListener(new MenuBarListener());
			helpMenu.add(aboutMenuItem);
		menuBar.add(helpMenu);
		frame.setJMenuBar(menuBar);
		frame.getContentPane().add(BorderLayout.SOUTH, statusBar);
		frame.getContentPane().add(BorderLayout.CENTER, panel);
		frame.addKeyListener(new GamePanelKeyListener(yamg, yamg.getModel(), this));
		frame.setResizable(false);
		frame.setBounds(Helper.getCenteredBounds(
				screen.width - 100,
				screen.height - 100));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		refreshView();
		setRefreshing(true);
	}
	
	public FlyUpsManager getFlyups() {
		return flyups;
	}

	public void setVisible(boolean visible) {
		if(frame == null) {
			flyups = new FlyUpsManager();
			buildGUI();
		}
		frame.setVisible(visible);
	}
	
	public void setRefreshing(boolean refreshing) {
		if(refreshJob == null) {
			refreshJob = new RefreshJob();
		}
		boolean prev = isRefreshing;
		isRefreshing = refreshing;
		if(refreshing && !prev) {
			Thread t = new Thread(refreshJob);
			t.start();
		}
	}
	
	public void refreshView() {
		if(OptionsManager.getInstance().isNotifyViewRefresh()) {
			log.logp(Level.FINEST, getClass().getSimpleName(), "refreshView()", "Refreshing...");
		}
		panel.repaint();
		statusBar.setText(getStatusBarText());
		model.getShop().update();
	}
	
	public String getStatusBarText() {
		String gravityInfo = (OptionsManager.getInstance().isGravityDebugStatus())
				?" | Gravity Info: abvG " + (model.getRobotLocation().y < 200) +
				", abvH " + (model.getHoles().contains(new Rectangle(model.getRobotLocation().x,
						model.getRobotLocation().y + 25, 25, 25))):"";
		
		return "Status: " + (model.isLocked()?"Locked":"Ready") + " | Robot position = (" +
			model.getRobotLocation().x + ", " + model.getRobotLocation().y +
			")" + (OptionsManager.getInstance().isScrollPositionOnStatus()?(" | Scroll position = " + panel.getScroll()):"") +
			(OptionsManager.getInstance().isAutoscrollOnStatus()?(" | Autoscroll " + (autoscroll ? "ON"
			: "OFF")):"") + (OptionsManager.getInstance().isGravityOnStatus()?(" | Gravity " +
			(model.isGravity() ? "ON" : "OFF")):"") + gravityInfo +
			(OptionsManager.getInstance().isFPSStatus()?(isRefreshing?(" | " + fps + " FPS"):" | -- FPS"):"");
	}
	
	public int getGravityDelay() {
		return OptionsManager.getInstance().getGravityDelay();
	}
	
	public void addFlyUp(String message) {
		flyups.addFlyUp(message);
	}

	public GamePanel getPanel() {
		return panel;
	}
	
	public boolean getAutoscroll() {
		return autoscroll;
	}

	public void setAutoscroll(boolean selected) {
		autoscroll = selected;
	}

	public boolean canScroll(Direction scrollDirection) {
		Point robot = model.getRobotLocation();
		int topBorder = Math.abs(panel.getScroll());
		int bottomBorder = Math.abs(panel.getScroll()) + panel.getHeight();
		switch(scrollDirection) {
		case DOWN:
			return robot.y <= (topBorder + 50);
		case UP:
			return robot.y >= (bottomBorder - 50);
		default:
			// It's only ever supposed to be scrolling up or down
			break;
		}
		return false;
	}
	
	private class MenuBarListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			if(arg0.getSource() instanceof JMenuItem) {
				JMenuItem item = (JMenuItem) arg0.getSource();
				if(item.getText().equals("Preferences...")) {
					OptionsManager.getInstance().setVisible(true);
				} else if(item.getText().equals("Quit")) {
					System.exit(0);
				} else if(item.getText().equals("About YAMG...")) {
					aboutView.setVisible(true);
				} else if(item.getText().equals("Cheat Code Manager...")) {
					CheatManager.getInstance().setVisible(true);
				} else if(item.getText().equals("Save Game As...")) {
					JFileChooser fileChooser = new JFileChooser();
					fileChooser.addChoosableFileFilter(new FileNameExtensionFilter(
							"YAMG save files", "yamg"));
					fileChooser.showSaveDialog(frame);
					File file = fileChooser.getSelectedFile();
					try {
						FileOutputStream fs = new FileOutputStream(file);
						ObjectOutputStream os = new ObjectOutputStream(fs);
						os.writeObject(model);
						os.close();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else if(item.getText().equals("Open Game...")) {
					JFileChooser fileChooser = new JFileChooser();
					fileChooser.addChoosableFileFilter(new FileNameExtensionFilter(
							"YAMG save files", "yamg"));
					fileChooser.showOpenDialog(frame);
					File file = fileChooser.getSelectedFile();
					try {
						FileInputStream fs = new FileInputStream(file);
						ObjectInputStream is = new ObjectInputStream(fs);
						model.getView().setVisible(false);
						Launcher controller = model.getController();
						controller.runApplication();
						model = (GameModel) is.readObject();
						is.close();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				} else if(item.getText().equals("New Game")) {
					model.getView().setVisible(false);
					Launcher controller = model.getController();
					controller.runApplication();
				} else if(item.getText().equals("Toggle debug window")) {
					((DebugConsole)log.getHandlers()[0]).toggleVisible();
				}
			}
			model.doRefresh();
		}
	}
	
	private class PanelClickListener implements MouseListener {
		public void mouseClicked(MouseEvent arg0) {}
		public void mouseEntered(MouseEvent arg0) {}
		public void mouseExited(MouseEvent arg0) {}
		public void mousePressed(MouseEvent arg0) {
			log.logp(Level.FINER, getClass().getSimpleName(), "mousePressed(MouseEvent)",
					arg0.getPoint().toString());
			if(model.getPortal().isVisible()) {
				model.getPortal().mouseClick(arg0.getPoint());
			} else if(CheatManager.getInstance().isVisible()) {
				CheatManager.getInstance().mouseClick(arg0.getPoint());
			} else if(model.getShop().isVisible()) {
				model.getShop().setVisible(false);
			}
			refreshView();
		}
		public void mouseReleased(MouseEvent arg0) {}
	}
	
	private class RefreshJob implements Runnable {
		private Date lastRefresh = new Date();
		public void run() {
			while(isRefreshing) {
				if(!OptionsManager.getInstance().isRefresh()) {
					isRefreshing = false;
					refreshView();
				}
				long timePassed = (new Date()).getTime() - lastRefresh.getTime();
				fps = (timePassed==0)?0:(1000/timePassed);
				refreshView();
				lastRefresh = new Date();
				try {
					Thread.sleep(OptionsManager.getInstance().getRefreshDelay());
				} catch(InterruptedException ex) {
					ex.printStackTrace();
				}
			}
		}
	}

	public Component getFrame() {
		return frame;
	}
}