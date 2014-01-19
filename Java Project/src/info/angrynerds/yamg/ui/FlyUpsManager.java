package info.angrynerds.yamg.ui;

import java.awt.*;
import java.util.List;
import java.util.*;

public class FlyUpsManager {
	private ArrayList<FlyUp> flyups;
	
	private String permanentFlyUp;
	
	public FlyUpsManager() {
		flyups = new ArrayList<FlyUp>();
		setPermanentFlyUp("");
	}
	
	public void addFlyUp(String message) {
		flyups.add(new FlyUp(message));
		while(flyups.size() > 5) {
			flyups.remove(0);
		}
		repositionFlyUps();
	}
	
	public void repositionFlyUps() {
//		removeDeadFlyUps();
		int basePosition = 15 + (!permanentFlyUp.equals("") ? 0 : 15);
		for(FlyUp fly:flyups) {
			fly.setDistanceAboveRobot(basePosition);
			basePosition += 15;
		}
	}
	
	public void removeDeadFlyUps() {
		List<FlyUp> toDelete = new ArrayList<FlyUp>();
		for(FlyUp fly:flyups) {
			if(fly.timeSinceCreation() >= 3300) toDelete.add(fly);
		}
		for(FlyUp fly:toDelete) {
			flyups.remove(fly);
		}
	}
	
	public ArrayList<FlyUp> getList() {
		return flyups;
	}
	
	public void paint(Graphics g, int yOffset) {
		removeDeadFlyUps();
		g.setColor(Color.BLACK);
		int bottomEdge = 15 * flyups.size();
		bottomEdge += (permanentFlyUp.equals(""))?0:15;
		float rgb = (bottomEdge <= (200 + yOffset)) ? 0.0F : 1.0F;
		g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
		g.drawString(permanentFlyUp, 2, 15);
		int yCoord = 15;
		if(!permanentFlyUp.equals("")) yCoord += 20;
		for(FlyUp data:flyups) {
			float alpha = 1.0F;
			if((data.timeSinceCreation() >= 2000) && (data.timeSinceCreation() < 3000)) {
				alpha = 1.0F - ((data.timeSinceCreation() - 2000.0F)/1000.0F);
			}
//			System.out.println("-\t- rgb: " + rgb + ", alpha: " + alpha);
			g.setColor(new Color(rgb, rgb, rgb, alpha));
			g.drawString(data.getMessage(), 2, yCoord);
			yCoord += 20;
		}
	}

	public void setPermanentFlyUp(String permanentFlyUp) {
		this.permanentFlyUp = permanentFlyUp;
	}

	public String getPermanentFlyUp() {
		return permanentFlyUp;
	}
	
	class FlyUp {
		private String message;
		private int distance;
		private long timeStartedUp;
		
		public FlyUp(String message) {
			this.message = message;
			this.distance = 15;
			this.timeStartedUp = (new Date()).getTime();
		}
		
		/**
		 * Tells if the flyup should cease to be displayed.
		 * @return Whether the current time is 2 seconds after the time the flyup was created.
		 */
		public boolean isDead() {
			return (this.timeStartedUp + 2000) >= (new Date()).getTime();
		}
		
		/**
		 * @return Time since creation, in milliseconds.
		 */
		public long timeSinceCreation() {
			return (new Date()).getTime() - timeStartedUp;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public int getDistanceAboveRobot() {
			return distance;
		}

		public void setDistanceAboveRobot(int location) {
			this.distance = location;
		}
	}
}