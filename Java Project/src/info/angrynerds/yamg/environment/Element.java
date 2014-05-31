package info.angrynerds.yamg.environment;

import info.angrynerds.yamg.engine.GameModel;

import java.awt.*;
import java.io.*;

@SuppressWarnings("serial")
public class Element implements Serializable {
	private ElementType type;
	private Point location;
	
	public Element(ElementType type, Point location) {
		setType(type);
		this.setLocation(location);
	}

	public void setType(ElementType type) {
		this.type = type;
	}

	public ElementType getType() {
		return type;
	}

	public void setLocation(Point location) {
		this.location = location;
	}

	public Point getLocation() {
		return location;
	}
	
	public Color getColor() {
		switch(type) {
		case COPPER:
			return new Color(219, 63, 45);
		case ALUMINUM:
			return new Color(134, 134, 134);
		case SILVER:
			return new Color(180, 180, 180);
		case GOLD:
			return Color.YELLOW;
		case BISMUTH:
			return new Color(242, 242, 242);
		case PLATINUM:
			return new Color(219, 237, 219);
		case EINSTEINIUM:
			return new Color(138, 90, 170);
		}
		return Color.BLUE;
	}
	
	public void paint(Graphics g, int yOff) {
		g.setColor(getColor());
		g.fillOval(getLocation().x, getLocation().y + yOff,
				GameModel.getUnit(), GameModel.getUnit());
	}
}