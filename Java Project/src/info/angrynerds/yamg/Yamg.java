package info.angrynerds.yamg;

import java.awt.*;

import info.angrynerds.yamg.ui.*;
import info.angrynerds.yamg.utils.*;

public class Yamg {
	public static final String VERSION = "Alpha Version 1.2.5";
	
	private LoadManager load;
	private GameModel model;
	private GameView view;
	private WelcomeView welcome;
	
	public static void main(String[] args) {
		new Yamg().go();
	}
	
	public void go() {
		welcome = new WelcomeView(this);
		welcome.showWindow();
		DebugConsole.getInstance().println("[Yamg/go()] End of go()");
	}
	
	public void runApplication() {
		welcome.hideWindow();
		load = new LoadManager();
		load.initialize(13); i();
		model = new GameModel(this); i();
		view = new GameView(this, model); i();
		view.setVisible(true);
	}
	
	public void i() {load.i();}
	
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