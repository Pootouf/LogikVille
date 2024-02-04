package view.element;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;


public class ClassicButton extends JButton {
	
	//CONSTANTES DE CLASSE
	
	private static final long serialVersionUID = 838981281659626429L;
	
	private static final Color COLOR_DEFAULT_UNPUSHED = new Color(255, 128, 0);
	
	private static final Color COLOR_DEFAULT_NOT_ENABLED = new Color(255, 180, 60);
	
	private static final Color COLOR_DEFAULT_NOT_ENABLED_TEXT = new Color(90, 89, 88);
	
	private static final Color COLOR_DEFAULT_PUSHED = new Color(255, 200, 70);
	
	
	
	//ATTRIBUTS
	
	private Color color;
	
	private final Color color_pushed;
	private final Color color_unpushed;

	
	
	//CONSTRUCTEURS
	
	public ClassicButton(String string) {
		this(string, COLOR_DEFAULT_PUSHED, COLOR_DEFAULT_UNPUSHED);
	}
	
	public ClassicButton(String string, Color color1, Color color2) {
		super(string);
		color_pushed = color1;
		color_unpushed = color2;
		initButton();
	}
	
	protected ClassicButton(Icon scaledImage) {
		this(scaledImage, COLOR_DEFAULT_PUSHED, COLOR_DEFAULT_UNPUSHED);
	}
	
	public ClassicButton(Icon scaledImage, Color color1, Color color2) {
		super(scaledImage);
		color_pushed = color1;
		color_unpushed = color2;
		initButton();
	}
	
	//REQUETES
	
	public Color getColor() {
		return color;
	}
	
	
	//COMMANDES




	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(color);
		if(!isEnabled()) {
			g.setColor(COLOR_DEFAULT_NOT_ENABLED);
		}
		g.fillRoundRect(0, 0, this.getWidth() - 1, this.getHeight() - 1, 30, 30);
		if(!isEnabled()) {
			g.setColor(COLOR_DEFAULT_NOT_ENABLED_TEXT);
		} else {
			g.setColor(Color.black);
		}
		g.drawRoundRect(0, 0, this.getWidth() - 1, this.getHeight() - 1, 30, 30);
		g.drawString(this.getText(), getCenterXForText(this.getText(), g), getCenterYForText(this.getText(), g));
	}
	
	
	
	
	
	//OUTILS
	
	private void initButton() {
		color = color_unpushed;
		this.setOpaque(false);
		this.setContentAreaFilled(false);
		this.setBorderPainted(false);
		createController();
	}
	
	private void createController() {
		
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				color = color_pushed;;
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				color = color_unpushed;
			}
		});
		
		this.addAncestorListener(new AncestorListener() {
			@Override
			public void ancestorAdded(AncestorEvent event) {
			}
			@Override
			public void ancestorRemoved(AncestorEvent event) {
				color = color_unpushed;
			}
			@Override
			public void ancestorMoved(AncestorEvent event) {
			}
		});
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
