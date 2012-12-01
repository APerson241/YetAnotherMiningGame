package info.angrynerds.yamg;

import info.angrynerds.yamg.utils.Direction;

import java.awt.*;

public class GravityJob implements Runnable {
	private GameModel model;
	
	public GravityJob(GameModel model) {
		this.model = model;
	}
	
	public void run() {
		while(model.GRAVITY) {
			Point location = model.getRobotLocation();
			boolean inTheAir = location.y < 175;
			boolean aboveAHole = model.getHoles().contains(new Rectangle(location.x,
					location.y + 25, 25, 25));
			if(inTheAir || aboveAHole) {
				if(model.canRobotMove(Direction.DOWN)) {
					model.getRobot().move(25, Direction.DOWN, false);
				}
				model.doRefresh();
			}
			try {
				Thread.sleep(model.getView().getGravityDelay());
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
