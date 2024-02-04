package view.element;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class GraphicCard extends JPanel {
	
	public static final Color BORDER_COLOR = Color.black;
	
	public static final Color FILL_COLOR_TOP = new Color(50, 161, 253);
	public static final Color FILL_COLOR_BOTTOM = new Color(255, 255, 255);
	
	private String text;
	private int width;
	private int height;

	public GraphicCard(String string, int width, int height) {
		text = string;
		this.width = width;
		this.height = height;
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(width, height);
	}
	
	@Override
	public Dimension getMaximumSize() {
		return getPreferredSize();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(5));
		g2.setColor(BORDER_COLOR);
		g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 100, 100);
		GradientPaint gp = new GradientPaint(0, 0, FILL_COLOR_TOP, getWidth(), getHeight(), FILL_COLOR_BOTTOM);
		g2.setPaint(gp);
		g2.fillRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 100, 100);
		g2.setColor(Color.black);
		int height = (int)g.getFontMetrics().getStringBounds(text, g2).getHeight();
		g2.drawString(text, getCenterXForText(text, g2), height + getHeight() / 200);
	}
	
	protected int getCenterXForText(String text, Graphics g) {
		int length = (int)g.getFontMetrics().getStringBounds(text, g).getWidth();
		return this.getWidth()/2 - length/2;
	}
	
	protected int getCenterYForText(String text, Graphics g) {
		int height = (int)g.getFontMetrics().getStringBounds(text, g).getHeight();
		return this.getHeight()/2 - height/2 + g.getFontMetrics().getAscent();
	}
	
}
