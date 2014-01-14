package info.angrynerds.yamg.utils;

import info.angrynerds.yamg.ui.OptionsManager;

import java.awt.*;

public abstract class Helper {
	/**
	 * Takes the specified dimension and returns bounds for that dimension that center
	 * the dimension on the screen.
	 * @param dimension The size of the component.
	 * @return The centered bounds for that component.
	 */
	public static Rectangle getCenteredBounds(Dimension dimension) {
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		return getCenteredBounds(dimension, screen);
	}
	
	/**
	 * Takes the smaller dimension and returns bounds for that dimension centered on the larger
	 * dimension
	 * @param smaller The size of the smaller dimension
	 * @param larger The dimension that the smaller dimension should be centered on
	 * @return The centered bounds for the smaller dimension
	 */
	public static Rectangle getCenteredBounds(Dimension smaller, Dimension larger) {
		int x = (larger.width - smaller.width)/2;
		int y = (larger.height - smaller.height)/2;
		Rectangle result = new Rectangle(x, y, smaller.width, smaller.height);
		return result;
	}

	public static Rectangle getCenteredBounds(int width, int height) {
		return getCenteredBounds(new Dimension(width, height));
	}
	
	public static String formatMoney(int money) {
		return String.format("$%,d", money);
	}
	
	public static String formatComma(int number) {
		return String.format("%,d", number);
	}
	
	public static Point[] getBlastRadius(Point point, int blockSize, int radius) {
		if(radius == 0) {
			return new Point[] {new Point(point.x, point.y + blockSize)};
		} else if(radius == 1) {
			return new Point[] {new Point(point.x, point.y - blockSize),
					new Point(point.x - blockSize, point.y),
					new Point(point.x + blockSize, point.y),
					new Point(point.x, point.y + blockSize)};
		} else if(radius == 2) {
			Point[] result = new Point[9];
			int i = 0;
			for(int x = point.x - blockSize; x <= point.x + blockSize; x += blockSize) {
				for(int y = point.y - blockSize; y <= point.y + blockSize; y += blockSize) {
					result[i++] = new Point(x, y);
				}
			}
			return result;
		} else if(radius > 2){
			/*
			 * 2 -> 9 = 3^2, 3 -> 25 = 5^2
			 */
			int i = 0;
			Point[] result = new Point[(radius * 2 + 1) * (radius * 2 + 1)]; // Trust me, it works.
			for(int x = (point.x - (blockSize * radius));
					x <= point.x + (blockSize * radius); x += blockSize) {
				for(int y = (point.y - (blockSize * radius));
					y <= point.y + (blockSize * radius); y += blockSize) {
					result[i++] = new Point(x, y);
				}
			}
			if(OptionsManager.getInstance().isVerbose()) System.out.println("[Helper - getBlastRadius()] i was " + i);
			return result;
		} else {
			return null;
		}
	}
}