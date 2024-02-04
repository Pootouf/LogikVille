package view.gui;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import main.LogikVille;
import utils.GBC;
import view.element.ClassicButton;


@SuppressWarnings("serial")
public class TitleMenu extends AbstractMenu {
	
	//ATTRIBUTS
	
	private JLabel title;
	private JLabel background;
	
	private ClassicButton play;
	private ClassicButton editor;
	private ClassicButton help;
	private ClassicButton option;
	private ClassicButton quit;
	
	
	//CONSTRUCTEUR
	
	public TitleMenu(LogikVille lv) {
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
		background.setLayout(new GridBagLayout());
		background.setPreferredSize(new Dimension(getApp().getScreenPixelX(), getApp().getScreenPixelY()));
		
		imageIcon = new ImageIcon(SPRITE_LOC + "title.png");
		scaledImage = new ImageIcon(imageIcon.getImage().getScaledInstance(getApp().getScreenPixelX() / 2, -1 , Image.SCALE_SMOOTH));
		title = new JLabel(scaledImage);
		
		play = new ClassicButton("Jouer");
		play.setFont(FONT.deriveFont(Font.BOLD, getFontSize(30)));
		editor = new ClassicButton("Editeur de cartes");
		editor.setFont(FONT.deriveFont(Font.BOLD, getFontSize(30)));
		help = new ClassicButton("Consulter l'aide");
		help.setFont(FONT.deriveFont(Font.BOLD, getFontSize(30)));
		option = new ClassicButton("Options");
		option.setFont(FONT.deriveFont(Font.BOLD, getFontSize(30)));
		quit = new ClassicButton("Quitter");
		quit.setFont(FONT.deriveFont(Font.BOLD, getFontSize(30)));
	}
	
	private void placeComponents() {
		JButton[] buttons = new JButton[] {play, editor, help,  option, quit};
		GBC[] constraints = new GBC[] {
			new GBC(1, 1).weight(0.2, 1).fill(GBC.HORIZONTAL),
			new GBC(1, 2).weight(0, 1).fill(GBC.HORIZONTAL),
			new GBC(1, 3).weight(0, 1).fill(GBC.HORIZONTAL),
			new GBC(1, 4).weight(0, 1).fill(GBC.HORIZONTAL),
			new GBC(1, 5).weight(0, 1).fill(GBC.HORIZONTAL),
		};
		{ //--
			background.add(title, new GBC(0, 0, 3, 1).weight(0, 5));
			for(int i = 0; i < constraints.length; i++) {
				background.add(buttons[i], constraints[i]);
			}
			background.add(new JLabel(" "), new GBC(1, 6).weight(0, 2));
			background.add(new JLabel(" "), new GBC(0, 6).weight(1, 0));
			background.add(new JLabel(" "), new GBC(2, 6).weight(1, 0));
		} //--
		this.add(background);
	}
	
	private void createController() {
		addGameStateListener(play, LogikVille.LEVEL_STATE);
		addGameStateListener(option, LogikVille.OPTION_STATE);
		addGameStateListener(editor, LogikVille.EDITOR_STATE);
		
		quit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		help.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					File htmlFile = new File("res/ressources_personalized/site/site.html");
				    Desktop.getDesktop().browse(htmlFile.toURI());
				} 
				catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
	}
	
}
