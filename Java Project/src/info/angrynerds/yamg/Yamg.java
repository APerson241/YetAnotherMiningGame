package info.angrynerds.yamg;

import java.awt.*;

import info.angrynerds.yamg.ui.*;
import info.angrynerds.yamg.utils.*;

public class Yamg {
	public static final String VERSION = "Alpha Version 1.2.3";
	
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
//		System.out.println("End of go()");
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
	
	public Rectangle getFrameBounds() {
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		return Helper.getCenteredBounds(screen.width - 100, screen.height - 100);
	}

	public GameModel getModel() {
		return model;
	}

	public GameView getView() {
		return view;
	}
}