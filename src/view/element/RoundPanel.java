package view.element;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.LayoutManager;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class RoundPanel extends JPanel {

	private final Color color;
	
	public RoundPanel(Color color, LayoutManager layout) {
		super(layout);
		this.color = color;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		g.setColor(color);
		g.fillRoundRect(0, 0, this.getWidth() - 1, this.getHeight() - 1, 30, 30);
	}
	
}
