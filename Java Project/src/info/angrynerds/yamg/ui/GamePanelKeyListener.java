package info.angrynerds.yamg.ui;

import info.angrynerds.yamg.*;
import info.angrynerds.yamg.robot.Element;
import info.angrynerds.yamg.utils.*;

import java.awt.*;
import java.awt.event.*;

public class GamePanelKeyListener implements KeyListener {
	private GameModel model;
	private GameView view;
	
	public GamePanelKeyListener(Yamg y, GameModel gm, GameView gv) {
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
		allowedToMove &= !model.isLocked();
		if(allowedToMove) {
			model.getRobot().move(GameModel.getUnit(), direction);
			if(view.getAutoscroll() && view.canScroll(scrollDirection))
				view.getPanel().scroll(scrollDirection, GameModel.getUnit());
			Element el = model.removeElement(model.getRobotRect());
			if(el != null) {
				view.addFlyUp("+ $" + el.getType().getPrice());
				model.doRefresh();
			}
			model.FIRST_STEP = true;
		}
		if(model.getRobotLocation().y >= GameModel.GROUND_LEVEL) {	// We should add a hole.
			model.addHole(model.getRobotLocation());
			DebugConsole.getInstance().println("Added hole: " + model.getRobot().getLocation());
		}
		if(model.getShop().getBounds().intersects(model.getRobotRect())) {
			view.getFlyups().setPermanentFlyUp("Press S to open Shop");
		} else if(model.getPortal().getBounds().intersects(model.getRobotRect())) {
			view.getFlyups().setPermanentFlyUp("Press S to open Real Estate");
		} else view.getFlyups().setPermanentFlyUp("");
		model.doRefresh();
	}

	/**
	 * Handle all the other (non-motion) key events.
	 * @param keyCode The keycode
	 */
	private void handleOtherKeyEvents(int keyCode) {
		if(keyCode == KeyEvent.VK_S) {
			if(model.getShop().getBounds().contains(model.getRobotLocation())) {
				model.getShop().setVisible(true);
				view.refreshView();
			} else if(model.getPortal().getBounds().contains(model.getRobotLocation())) {
				model.getPortal().setVisible(true);
				view.refreshView();
			}
		} else if(keyCode == KeyEvent.VK_J) { // Scroll down (debugging purposes)
			view.getPanel().scroll(Direction.DOWN, GameModel.getUnit());
		} else if(keyCode == KeyEvent.VK_M) { // Scroll up (debugging purposes)
			view.getPanel().scroll(Direction.UP, GameModel.getUnit());
		} else if(keyCode == KeyEvent.VK_R) {
			if(model.getRobot().getReserves() >= 1) {
				if(model.getRobot().getFuelTank().isFull()) {
					view.addFlyUp("Tank is already full!");
				} else {
					model.getRobot().useReserve();
					model.getRobot().getFuelTank().setFuelLevel(
							model.getRobot().getFuelTank().getFuelCapacity());
					view.addFlyUp("Used reserve fuel tank (" + model.getRobot().getReserves() + " left)");
				}
			} else {
				view.addFlyUp("You don't have any reserves!");
			}
		} else if(keyCode == KeyEvent.VK_D) {
			if(model.getRobotLocation().y <= GameModel.GROUND_LEVEL) {
				view.addFlyUp("Can't use dynamite aboveground!");
			} else if(model.getRobot().getDynamite() <= 0) {
				view.addFlyUp("You don't have any dynamite!");
			} else {
				if(!model.isInfiniteDynamite()) model.getRobot().useDynamite();
				for(Point point:Helper.getBlastRadius(model.getRobotLocation(),
						GameModel.getUnit(), model.getRobot().getDynamiteTier())) {
					model.addHole(point);
					if(model.getRocks().contains(new Rectangle(point.x,
							point.y, GameModel.getUnit(), GameModel.getUnit()))) {
						model.getRocks().remove(new Rectangle(point.x,
								point.y, GameModel.getUnit(), GameModel.getUnit()));
					}
				}
				if(model.isInfiniteDynamite()) {
					view.addFlyUp("Used infinite dynamite");
				} else {
					view.addFlyUp("Used dynamite (" + model.getRobot().getDynamite() + " left)");
				}
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