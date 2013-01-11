package info.angrynerds.yamg.ui;

import info.angrynerds.yamg.*;
import info.angrynerds.yamg.robot.Element;
import info.angrynerds.yamg.utils.*;

import java.awt.*;
import java.awt.event.*;

public class MyKeyListener implements KeyListener {
	private GameModel model;
	private GameView view;
	
	public MyKeyListener(Yamg y, GameModel gm, GameView gv) {
		model = gm;
		view = gv;
	}

	/**
	 * The {@link info.angrynerds.yamg.GameModel GameModel} makes the final decision on whether
	 * or not the robot is able to move after this method tests whether or not the view is
	 * locked.
	 */
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		Direction direction = Direction.DOWN;
		Direction scrollDirection = Direction.LEFT;
		boolean allowedToMove = true;
		// Handle the robot moving key events
		if((keyCode == KeyEvent.VK_UP) && model.isSpaceAboveRobot()) {
			direction = Direction.UP;
			scrollDirection = Direction.DOWN;
		} else if(keyCode == KeyEvent.VK_DOWN) {
			direction = Direction.DOWN;
			scrollDirection = Direction.UP;
		} else if(keyCode == KeyEvent.VK_LEFT) {
			direction = Direction.LEFT;
		} else if(keyCode == KeyEvent.VK_RIGHT) {
			direction = Direction.RIGHT;
		} else {
			allowedToMove = false;
		}
		// Handle all the other key events here
		handleOtherKeyEvents(keyCode);
		allowedToMove &= model.canRobotMove(direction);
		allowedToMove &= !model.LOCKED;
		if(allowedToMove) {
			model.getRobot().move(GameModel.UNIT, direction);
			if(view.getPanel().justVisitedRock() && !model.isRockNextToRobot()) {
				model.HAS_VISITED_ROCK = true;
				view.getPanel().setJustVisitedRock(false);
			}
			if(view.getAutoscroll() && view.canScroll(scrollDirection))
				view.getPanel().scroll(scrollDirection, GameModel.UNIT);
			Element el = model.removeElement(model.getRobotLocation());
			if(el != null) {
				view.addFlyUp("+ $" + el.getType().getPrice());
				model.doRefresh();
			}
			model.FIRST_STEP = true;
		}
		if(model.getRobotLocation().y >= GameModel.GROUND_LEVEL) {	// We should add a hole.
			model.addHole(model.getRobotLocation());
			if(view.getOptions().isVerbose()) 
				System.out.println("Added hole: " + model.getRobot().getLocation());
		}
		if(model.getShop().getBounds().contains(model.getRobotLocation())) {
			view.getFlyups().setPermanentFlyUp("Press S to open Shop");
		} else if(model.getPortal().getBounds().contains(model.getRobotLocation())) {
			view.getFlyups().setPermanentFlyUp("Press S to open Real Estate");
		} else view.getFlyups().setPermanentFlyUp("");
//		System.out.println("Moved robot.");
		model.doRefresh();
	}

	/**
	 * Handle all the other (non-motion) key events.
	 * @param keyCode The keycode
	 */
	private void handleOtherKeyEvents(int keyCode) {
		if(keyCode == KeyEvent.VK_S) {
			if(model.getShop().getBounds().contains(model.getRobotLocation())) {
				model.getShop().showDialog();
				model.LOCKED = true;
				view.refreshView();
			} else if(model.getPortal().getBounds().contains(model.getRobotLocation())) {
				model.getPortal().setDialogVisible(true);
				model.LOCKED = true;
				view.refreshView();
			}
		} else if(keyCode == KeyEvent.VK_J) {
			view.getPanel().scroll(Direction.DOWN, GameModel.UNIT);
		} else if(keyCode == KeyEvent.VK_M) {
			view.getPanel().scroll(Direction.UP, GameModel.UNIT);
		} else if((keyCode == KeyEvent.VK_R) && (model.getRobot().getReserves() >= 1)) {
			if(model.getRobot().getFuelTank().isFull()) {
				view.addFlyUp("Tank is already full!");
			} else {
				model.getRobot().useReserve();
				model.getRobot().getFuelTank().setFuelLevel(
						model.getRobot().getFuelTank().getFuelCapacity());
				view.addFlyUp("Used reserve fuel tank");
			}
		} else if(keyCode == KeyEvent.VK_D) {
			if(model.getRobotLocation().y <= GameModel.GROUND_LEVEL) {
				view.addFlyUp("Can't use dynamite aboveground!");
			} else if(model.getRobot().getDynamite() <= 0) {
				view.addFlyUp("You don't have any dynamite!");
			} else {
				if(!model.isInfiniteDynamite()) model.getRobot().useDynamite();
				for(Point point:Helper.getBlastRadius(model.getRobotLocation(),
						GameModel.UNIT, model.getRobot().getDynamiteTier())) {
					model.addHole(point);
					if(model.getRocks().contains(new Rectangle(point.x,
							point.y, GameModel.UNIT, GameModel.UNIT))) {
						model.getRocks().remove(new Rectangle(point.x,
								point.y, GameModel.UNIT, GameModel.UNIT));
					}
				}
				view.addFlyUp("Used dynamite");
			}
		} else if((keyCode == KeyEvent.VK_ENTER) && (model.getRobot().isDead())) {
			view.setVisible(false);
			Yamg controller = model.getController();
			controller.runApplication();
		}
	}

	public void keyReleased(KeyEvent e) {

	}

	public void keyTyped(KeyEvent e) {

	}
}