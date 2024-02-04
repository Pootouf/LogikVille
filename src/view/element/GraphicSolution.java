package view.element;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;

import javax.swing.JComponent;

@SuppressWarnings("serial")
public class GraphicSolution extends JComponent {

	
	public GraphicSolution(int numberOfLines, int numberOfColumn) {
		this.setLayout(new GridLayout(numberOfColumn, numberOfLines));
		this.setOpaque(false);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g.setColor(new Color(173, 76, 0));
		g2.setStroke(new BasicStroke(8));
		g2.drawRoundRect(1, 1, this.getWidth() - 1, this.getHeight() - 1, 100, 100);
	}
	
}
