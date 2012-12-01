package info.angrynerds.yamg;

import java.awt.*;
import java.util.*;

import info.angrynerds.yamg.robot.Element;
import info.angrynerds.yamg.robot.ElementType;
import info.angrynerds.yamg.ui.*;
import info.angrynerds.yamg.utils.*;

public class Yamg {
	public static final String VERSION = "Alpha Version 1.2";
	
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
		load.initialize(19); i();
		model = new GameModel(this); i();
		view = new GameView(this, model); i();
		initializeHoles(); i();
		initializeElements(); i();
		initializeRocks(); i();
		view.setVisible(true);
	}
	
	public void i() {load.i();}
	
	public Rectangle getFrameBounds() {
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		return Helper.getCenteredBounds(screen.width - 100, screen.height - 100);
	}
	
	private void initializeHoles() {
		for(int i = 0; i < 500; i++) {
			model.addHole(getRandomLocationOnGrid(GameModel.GROUND_LEVEL + 25));
		}
	}
	
	private void initializeElements() {
		int number = ElementType.values().length * 10 + (GameModel.UNIT * 10);
		for(ElementType type:ElementType.values()) {
			for(int i = 0; i < number; i++) {
				model.addElement(new Element(type,
						getRandomLocationOnGrid(GameModel.GROUND_LEVEL + 25)));
			}
			number -= 10;
		}
	}
	
	private void initializeRocks() {
		for(int i = 0; i < 250; i++) {
			Point point = getRandomLocationOnGrid(525);
			if(model.getElements().contains(new Rectangle(point.x,
					point.y, GameModel.UNIT, GameModel.UNIT))) {
				i++;
				continue;
			} else {
				model.addRock(point);
			}
		}
		int UNIT4 = GameModel.UNIT * 4;	// Should be 100 if UNIT is 25
		model.addRock(new Point(UNIT4 * 3, UNIT4 * 2));	// Below the Shop
		model.addRock(new Point(UNIT4 * 3 + GameModel.UNIT, UNIT4 * 2));
		model.addRock(new Point(UNIT4 * 3 + GameModel.UNIT * 2, UNIT4 * 2));
	}
	
	private Point getRandomLocationOnGrid(int offset) {
		Random random = new Random();
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize(); i();
		int xLimit = screen.width - 100;
		int yLimit = GameModel.BOTTOM;
		int x = random.nextInt((int) xLimit/GameModel.UNIT) * GameModel.UNIT;
		int y = (random.nextInt((int) yLimit/GameModel.UNIT) * GameModel.UNIT) + offset;
		return new Point(x, y);
	}

	public GameModel getModel() {
		return model;
	}

	public GameView getView() {
		return view;
	}
}