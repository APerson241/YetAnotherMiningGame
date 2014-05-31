package info.angrynerds.yamg.ui;

import java.awt.*;
import java.awt.event.*;

/**
 * Represents a single button in the {@link info.angrynerds.yamg.ui.GamePanel GamePanel}.
 * Manages hover effects and raises ActionEvents.
 */
public class GameButton implements MouseListener, MouseMotionListener {
	private String text;
	private Rectangle bounds;
	/**
	 * Whether the user is hovering over this GameButton. Set in the
	 * <code>mouseMoved(MouseEvent)</code> method and used in the
	 * <code>paint(Graphics)</code> method.
	 */
	private boolean mouseHovering;
	
	public static final Color BASE_BACKGROUND_COLOR = Color.DARK_GRAY;
	public static final Color HOVER_BACKGROUND_COLOR = Color.LIGHT_GRAY;
	public static final Color BORDER_COLOR = Color.BLACK;
	public static final Color TEXT_COLOR = Color.BLACK;
	
	public GameButton(Rectangle rectangle) {
		this(rectangle, "Button");
	}
	
	public GameButton(Rectangle rectangle, String text) {
		bounds = rectangle;
		this.text = text;
	}
	
	public void paint(Graphics g) {
		g.setColor(mouseHovering ? HOVER_BACKGROUND_COLOR : BASE_BACKGROUND_COLOR);
		g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
		g.setColor(BORDER_COLOR);
		g.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);
		g.setColor(TEXT_COLOR);
		int textX = bounds.x + ((bounds.width - g.getFontMetrics().stringWidth(text))
				/ 2); // Button's text is horizontally centered
		int textY = bounds.y + bounds.height / 2; // The text is already vertically centered
		g.drawString(text, textX, textY);
	}
	
	@Override
	public void mouseDragged(MouseEvent arg0) { }

	/* (non-Javadoc)
	 * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseMoved(MouseEvent arg0) {
		mouseHovering = bounds.contains(arg0.getPoint());
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @return the bounds
	 */
	public Rectangle getBounds() {
		return bounds;
	}

	/**
	 * @param bounds the bounds to set
	 */
	public void setBounds(Rectangle bounds) {
		this.bounds = bounds;
	}
}