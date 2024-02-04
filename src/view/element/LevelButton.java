package view.element;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import javax.swing.Icon;
import javax.swing.JLabel;


@SuppressWarnings("serial")
public class LevelButton extends ClassicButton {	
	

	public static final Color COLOR_TRANSPARENCY;
	static {
		Color tmp = new Color(255, 128, 0);
		COLOR_TRANSPARENCY = new Color(tmp.getRed(), tmp.getGreen(), tmp.getBlue(), 200);
	}
	
	private JLabel icon;
	
	
	public LevelButton(Icon scaledImage) {
		super("", COLOR_TRANSPARENCY, null);
		icon = new JLabel(scaledImage);
		this.setLayout(new FlowLayout());
		this.add(icon);
	}
	
	//REQUETES
	
	@Override
	public Icon getIcon() {
		return icon.getIcon();
	}
	
	//COMMANDE
	
	@Override
	public void setIcon(Icon i) {
		icon.setIcon(i);
	}

	@Override
	protected void paintComponent(Graphics g) {
		if(getColor() != null) {
			g.setColor(getColor());
			g.fillRoundRect(0, 0, this.getWidth() - 1, this.getHeight() - 1, 30, 30);
		}
		g.setColor(Color.black);
		g.drawString(this.getText(), getCenterXForText(this.getText(), g), getCenterYForBox() + this.getIcon().getIconHeight() + this.getIcon().getIconHeight() / 15);
	}
	
	
	
	protected int getCenterXForBox() {
		return this.getWidth()/2 - this.getIcon().getIconWidth()/2;
	}
	
	protected int getCenterYForBox() {
		return this.getHeight()/2 - this.getIcon().getIconHeight()/2;
	}
	
}
