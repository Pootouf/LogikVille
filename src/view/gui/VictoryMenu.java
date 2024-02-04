package view.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import main.LogikVille;
import view.element.ClassicButton;
import view.element.RoundPanel;
import view.element.WrapLayout;


@SuppressWarnings("serial")
public class VictoryMenu extends AbstractMenu {
	
	//ATTRIBUTS
	
	private JLabel victoryImage;
	private JLabel background;
	
	private ClassicButton continueButton;
	
	
	//CONSTRUCTEUR
	
	public VictoryMenu(LogikVille lv) {
		super(lv);
		createView();
		placeComponents();
		createController();
	}
	
	
	
	
	
	//OUTILS

	private void createView() {
		this.setBackground(Color.GRAY);
		
		ImageIcon imageIcon = new ImageIcon(SPRITE_LOC + "background-2.png");
		Icon scaledImage = new ImageIcon(imageIcon.getImage().getScaledInstance(getApp().getScreenPixelX(), getApp().getScreenPixelY(), Image.SCALE_SMOOTH));
		background = new JLabel(scaledImage);
		background.setLayout(new BorderLayout());
		background.setPreferredSize(new Dimension(getApp().getScreenPixelX(), getApp().getScreenPixelY()));
		
		imageIcon = new ImageIcon(SPRITE_LOC + "title.png");
		scaledImage = new ImageIcon(imageIcon.getImage().getScaledInstance(getApp().getScreenPixelX() / 2, -1 , Image.SCALE_SMOOTH));
		victoryImage = new JLabel(scaledImage);
		
		continueButton = new ClassicButton("Continuer");
		continueButton.setFont(FONT.deriveFont(Font.BOLD, getFontSize(40)));
	}
	
	private void placeComponents() {
		{ //--
			Box b = Box.createVerticalBox();
			{ //--
				b.setBorder(BorderFactory.createEmptyBorder(0, getResizeX(300), 0, getResizeX(300)));
				b.add(Box.createVerticalGlue());
				JPanel p = new JPanel();
				{ //--
					p.setOpaque(false);
					p.add(victoryImage);
				} //--
				b.add(p);
				b.add(Box.createVerticalGlue());
				p = new RoundPanel(COLOR_TRANSPARENCY, new WrapLayout()) {
					@Override
					public Dimension getMaximumSize() {
						return getPreferredSize();
					}
				};
				{ //--
					p.setOpaque(false);
					p.setBorder(BorderFactory.createEmptyBorder(getResizeY(30), getResizeX(30), getResizeY(30), getResizeX(30)));
					Box c = Box.createVerticalBox();
					{ //--
						c.add(Box.createVerticalGlue());
						JLabel jb = new JLabel("Bravo, vous avez gagné !");
						jb.setFont(FONT.deriveFont(Font.BOLD, getFontSize(40)));;
						c.add(jb);
						c.add(Box.createVerticalGlue());
					} //--
					p.add(c);
				} //--
				b.add(p);
				b.add(Box.createVerticalGlue());
				p = new JPanel();
				{ //--
					p.setOpaque(false);
					p.add(continueButton);
				} //--
				b.add(p);
				b.add(Box.createVerticalGlue());
			} //--
			background.add(b, BorderLayout.CENTER);
		} //--
		this.add(background);
	}
	
	private void createController() {
		addGameStateListener(continueButton, LogikVille.LEVEL_STATE);
	}
	
}
