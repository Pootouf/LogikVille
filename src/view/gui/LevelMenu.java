package view.gui;


import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import main.LogikVille;
import model.LogikVilleModel;
import view.element.LevelButton;

@SuppressWarnings("serial")
public class LevelMenu extends AbstractLevelMenu {
	
	public LevelMenu(LogikVille lv) {
		super(lv, false, "Niveaux Personnalisés");
	}
	
	@Override
	protected void createController() {
		super.createController();
		addGameStateListener(this.getButtonState(), LogikVille.USERLEVEL_STATE);
		JPopupMenu[] menus = getPopupMenus();
		for(int i = 0; i < NUMBER_OF_LEVEL; i++) {
			final int j = i + 1;
			ActionListener al = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					LogikVilleModel model = getApp().getModel();
					model.openLevelClassic(j + getPage() * NUMBER_OF_LEVEL);
				}
			};
			getLevelButton(i).addActionListener(al);
			getOpenMenus()[i].addActionListener(al);
			getDeleteMenus()[i].setEnabled(false);
			getLevelButton(i).addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					//événement click droit de la souris
					if (SwingUtilities.isRightMouseButton(e) && e.getClickCount() == 1){
						Point position  = SwingUtilities.convertPoint(getLevelButton(j - 1), e.getPoint(), getApp().getFrame());
						menus[j - 1].show(getApp().getFrame() , position.x, position.y);
					}        
				}       
			});
			
		}
		
		final JTextField research = getResearchField();
		
		research.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String text = research.getText();
				int value;
				try {
					value = Integer.parseInt(text);
				} catch(NumberFormatException exc) {
					showStylizedErrorDialogue( "Tu ne peux entrer ici uniquement un nombre !", "Erreur de saisie !");
					research.setText("");
					return;
				};
				if(value < 0) {
					setPageLevel(0);
					refresh();
					return;
				}
				int numberLevel;
				if(!isPersonnalized()) {
					numberLevel = getApp().getModel().getClassicLevelNumber();
				} else {
					numberLevel = getApp().getModel().getPersonalizedLevelNumber();
				}
				if(value > numberLevel) {
					setPageLevel(numberLevel / NUMBER_OF_LEVEL);
					refresh();
					return;
				}
				setPageLevel(((value - 1) / NUMBER_OF_LEVEL));
				refresh();
			}
		});
		
		
		
		getApp().getModel().addPropertyChangeListener(LogikVilleModel.PROP_LOAD_CLASSIC, getPCLLoad());
	}
	
	@Override
	protected LevelButton[] createLevelButtons() {
		LevelButton[] levelButtons = new LevelButton[NUMBER_OF_LEVEL];
		for(int i = 0; i < NUMBER_OF_LEVEL; i++) {
			ImageIcon imageIcon = new ImageIcon(SPRITE_LOC + "nothing.png");
			ImageIcon scaledImage = new ImageIcon(imageIcon.getImage().getScaledInstance(getResizeX(BUTTON_SIZE), -1, Image.SCALE_SMOOTH));
			levelButtons[i] = new LevelButton(scaledImage);
			levelButtons[i].setPreferredSize(new Dimension(getResizeX(200), getResizeY(200)));
			levelButtons[i].setFont(FONT.deriveFont(Font.BOLD, getFontSize(10)));
			levelButtons[i].setText("Niveau " + (getPage() * NUMBER_OF_LEVEL + i + 1));
		}
		return levelButtons;
	}
	
	@Override
	protected void setLevelButtons(List<ImageIcon> sprites, List<String> names) {
		int numberLevel = getApp().getModel().getClassicLevelNumber();
		for(int i = 0; i < NUMBER_OF_LEVEL; i++) {
			if(i + getPage() * NUMBER_OF_LEVEL + 1 > numberLevel) {
				return;
			}
			ImageIcon imageIcon = new ImageIcon(SPRITE_LOC + "nothing.png");
			ImageIcon scaledImage = new ImageIcon(imageIcon.getImage().getScaledInstance(getResizeX(BUTTON_SIZE), getResizeX(BUTTON_SIZE), Image.SCALE_SMOOTH));
			sprites.add(scaledImage);
			names.add("Niveau " + (getPage() * NUMBER_OF_LEVEL + i + 1));
		}
	}
}
