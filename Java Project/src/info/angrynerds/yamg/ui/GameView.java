package info.angrynerds.yamg.ui;

import info.angrynerds.yamg.*;
import info.angrynerds.yamg.utils.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Date;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class GameView {
	private Yamg yamg;
	private AboutView aboutView;
	
	private JFrame frame;
	private JLabel statusBar;
	
	private GamePanel panel;
	private GameModel model;
	private HUDManager hud;
	private FlyUpsManager flyups;
	private OptionsManager options;
	
	private Runnable refreshJob;
	private boolean isRefreshing = false;
	private float fps = 123;

	private boolean autoscroll = true;
	
	public GameView(Yamg yam, GameModel model) {
		yamg = yam;
		this.model = model;
	}
	
	public void i() {
		yamg.i();
	}
	
	private void buildGUI() {
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize(); i();
		frame = new JFrame("Yet Another Mining Game"); i();
		panel = new GamePanel(new Dimension((screen.width - 100), screen.height - 100),
				model, flyups); i();
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
			JMenuItem cheatMenuItem = new JMenuItem("Enter a cheat code...");
				cheatMenuItem.addActionListener(new MenuBarListener());
			fileMenu.add(cheatMenuItem);
			fileMenu.addSeparator();
			JMenuItem exitMenuItem = new JMenuItem("Quit");
				exitMenuItem.addActionListener(new MenuBarListener());
			fileMenu.add(exitMenuItem);
		menuBar.add(fileMenu);
		JMenu helpMenu = new JMenu("Help");
			JMenuItem aboutMenuItem = new JMenuItem("About YAMG...");
				aboutMenuItem.addActionListener(new MenuBarListener());
			helpMenu.add(aboutMenuItem);
		menuBar.add(helpMenu);
		frame.setJMenuBar(menuBar);
		frame.getContentPane().add(BorderLayout.SOUTH, statusBar); i();
		frame.getContentPane().add(BorderLayout.CENTER, panel); i();
		frame.getContentPane().add(BorderLayout.NORTH, hud.getPanel()); i();
		frame.addKeyListener(new MyKeyListener(yamg, yamg.getModel(), this)); i();
		frame.setResizable(false); i();
		frame.setBounds(Helper.getCenteredBounds(
				screen.width - 100,
				screen.height - 100)); i();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); i();
		refreshView();
		setRefreshing(true);
	}
	
	public FlyUpsManager getFlyups() {
		return flyups;
	}

	public void setVisible(boolean visible) {
		if(frame == null) {
			hud = new HUDManager(model);
			flyups = new FlyUpsManager();
			options = new OptionsManager(model);
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
		if(options.isVerbose()) System.out.println("[GameView] Refreshing view...");
		panel.repaint();
		hud.update();
		statusBar.setText(getStatusBarText());
		model.getShop().update();
		model.getPortal().update();
	}
	
	public String getStatusBarText() {
		String gravityInfo = (options.isGravityDebugStatus())
				?" | Gravity Info: abvG " + (model.getRobotLocation().y < 200) +
				", abvH " + (model.getHoles().contains(new Rectangle(model.getRobotLocation().x,
						model.getRobotLocation().y + 25, 25, 25))):"";
		
		return "Status: " + (model.LOCKED?"Locked":"Ready") + " | Robot position = (" +
			model.getRobotLocation().x + ", " + model.getRobotLocation().y +
			") | Scroll position = " + panel.getScroll() +
			(options.isAutoscrollOnStatus()?(" | Autoscroll " + (autoscroll ? "ON"
			: "OFF")):"") + (options.isGravityOnStatus()?(" | Gravity " +
			(model.isGravity() ? "ON" : "OFF")):"") + gravityInfo +
			(options.isFPSStatus()?(isRefreshing?(" | " + fps + " FPS"):" | -- FPS"):"");
	}
	
	public int getGravityDelay() {
		return options.getGravityDelay();
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
		}
		return false;
	}

	public OptionsManager getOptions() {
		return options;
	}
	
	private class MenuBarListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			if(arg0.getSource() instanceof JMenuItem) {
				JMenuItem item = (JMenuItem) arg0.getSource();
				if(item.getText().equals("Preferences...")) {
					if(options == null) {
						options = new OptionsManager(model);
					}
					options.setVisible(true);
				} else if(item.getText().equals("Quit")) {
					System.exit(0);
				} else if(item.getText().equals("About YAMG...")) {
					aboutView.setVisible(true);
				} else if(item.getText().equals("Enter a cheat code...")) {
					String result = JOptionPane.showInputDialog(frame,
							"Enter a cheat code!", "Oh boy...", 1);
					if(result != null) {
						result = result.trim().toLowerCase();
						options.addCheat(result);
					}
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
						Yamg controller = model.getController();
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
					Yamg controller = model.getController();
					controller.runApplication();
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
			if(options.isPrintMouseCoords()) {
				System.out.println("Mouse was clicked: " + arg0.getPoint());
			}
			if(model.LOCKED) {
				model.LOCKED = false;
				model.getShop().hideDialog();
				model.getPortal().setDialogVisible(false);
				refreshView();
			}
		}
		public void mouseReleased(MouseEvent arg0) {}
	}
	
	private class RefreshJob implements Runnable {
		private Date lastRefresh = new Date();
		public void run() {
			while(isRefreshing) {
				if(!options.isRefresh()) {
					System.out.println("BREAK!");
					isRefreshing = false;
					refreshView();
				}
				fps = (new Date()).getTime() - lastRefresh.getTime();
				refreshView();
				lastRefresh = new Date();
				try {
					Thread.sleep(options.getRefreshDelay());
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