package info.angrynerds.yamg.engine;

import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import info.angrynerds.yamg.ui.*;
import info.angrynerds.yamg.utils.*;

public class Launcher {
	
	private GameModel model;
	private GameView view;
	private WelcomeView welcome;
	
	private Logger log = Logger.getGlobal();
	
	public static void main(String[] args) {
		Launcher launcher = new Launcher();
		launcher.initializeLogger(); // Couldn't think of anywhere else to put it
		try {
			launcher.showWelcomeView();
		} catch(Exception ex) {
			JOptionPane.showConfirmDialog(null,
					"There was an error!\n" + ex.getMessage(), "Error!", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void initializeLogger() {
		log.setLevel(Level.ALL);
		log.addHandler(new DebugConsole());
	}
	
	public void showWelcomeView() {
		welcome = new WelcomeView(this);
		welcome.showWindow();
		log.exiting(getClass().getSimpleName(), "showWelcomeView()");
	}
	
	public void runApplication() {
		welcome.hideWindow();
		model = new GameModel(this);
		view = new GameView(this, model);
		view.setVisible(true);
		log.exiting(getClass().getSimpleName(), "runApplication()");
	}
	
	public static Rectangle getFrameBounds() {
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		return Helper.getCenteredBounds(screen.width - Configurables.SCREEN_MARGIN,
				screen.height - Configurables.SCREEN_MARGIN);
	}

	public GameModel getModel() {
		return model;
	}

	public GameView getView() {
		return view;
	}
}