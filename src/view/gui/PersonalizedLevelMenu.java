package view.gui;

import java.awt.Dimension;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import main.LogikVille;
import model.LogikVilleModel;
import view.element.LevelButton;

@SuppressWarnings("serial")
public class PersonalizedLevelMenu extends AbstractLevelMenu {
	
	public PersonalizedLevelMenu(LogikVille lv) {
		super(lv, true, "Niveaux Classiques");
	}
	
	//OUTILS
	
	@Override
	protected void createController() {
		super.createController();
		addGameStateListener(this.getButtonState(), LogikVille.LEVEL_STATE);
		for(int i = 0; i < NUMBER_OF_LEVEL; i++) {
			final int j = i + 1;
			ActionListener al = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					LogikVilleModel model = getApp().getModel();
					model.openLevelPersonalized(j + getPage() * NUMBER_OF_LEVEL);
				}
			};
			getLevelButton(i).addActionListener(al);
		     
			
			getOpenMenus()[i].addActionListener(al);
			getDeleteMenus()[i].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					LogikVilleModel model = getApp().getModel();
					int response = showStylizedConfirmDialogue("Voulez-vous supprimer ce niveau ?");
					if(response != JOptionPane.OK_OPTION) {
						return;
					}
					model.deleteLevelPersonalized(j + getPage() * NUMBER_OF_LEVEL);
				}
			});
			
		}
		
		getApp().getModel().addPropertyChangeListener(LogikVilleModel.PROP_DELETE, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						refresh();
						PersonalizedLevelMenu.this.revalidate();
						showStylizedMessageDialogue("Niveau supprimé avec succès !", "Réussite !");
					}
				});
			}
		});
		
		getApp().getModel().addPropertyChangeListener(LogikVilleModel.PROP_DELETE_ERROR, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						showStylizedErrorDialogue("Echec de la suppression du niveau", "Echec");
					}
				});
			}
		});
		
		final JTextField research = getResearchField();
		
		research.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String text = research.getText();
				
				int value = getApp().getModel().getLevelNumberFromName(text);
				
				if (value == -1) {
					try {
						value = Integer.parseInt(text);
					} catch(NumberFormatException exc) {
						showStylizedErrorDialogue( "Niveau non trouvé !", "Erreur de saisie !");
						research.setText("");
						return;
					};
				}
				
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
		
		getApp().getModel().addPropertyChangeListener(LogikVilleModel.PROP_LOAD_PERSONALIZED, getPCLLoad());
	}

	@Override
	protected LevelButton[] createLevelButtons() {
		LevelButton[] levelButtons = new LevelButton[NUMBER_OF_LEVEL];
		int numberLevel = getApp().getModel().getPersonalizedLevelNumber();
		for(int i = 0; i < NUMBER_OF_LEVEL; i++) {
			if(i + getPage() * NUMBER_OF_LEVEL + 1 > numberLevel) {
				ImageIcon imageIcon = new ImageIcon(SPRITE_LOC + "nothing.png");
				ImageIcon scaledImage = new ImageIcon(imageIcon.getImage().getScaledInstance(getResizeX(BUTTON_SIZE), -1, Image.SCALE_SMOOTH));
				levelButtons[i] = new LevelButton(scaledImage);
				levelButtons[i].setPreferredSize(new Dimension(getResizeX(200), getResizeY(200)));
				levelButtons[i].setFont(FONT.deriveFont(Font.BOLD, getFontSize(10)));
				levelButtons[i].setText("Niveau " + (getPage() * NUMBER_OF_LEVEL + i + 1));
				continue;
			}
			String path = getApp().getModel().getImagePathPersonalized((getPage() * NUMBER_OF_LEVEL + i + 1));
			ImageIcon imageIcon = new ImageIcon(path == null ? SPRITE_LOC + "nothing.png" : path);
			ImageIcon scaledImage = new ImageIcon(imageIcon.getImage().getScaledInstance(getResizeX(BUTTON_SIZE), getResizeX(BUTTON_SIZE), Image.SCALE_DEFAULT));
			levelButtons[i] = new LevelButton(scaledImage);
			levelButtons[i].setPreferredSize(new Dimension(getResizeX(200), getResizeY(200)));
			levelButtons[i].setFont(FONT.deriveFont(Font.BOLD, getFontSize(10)));
			String name = getApp().getModel().getNamePersonalized((getPage() * NUMBER_OF_LEVEL + i + 1));
			levelButtons[i].setText(name == null ? "Niveau " + (getPage() * NUMBER_OF_LEVEL + i + 1) : name);
		}
		return levelButtons;
	}

	@Override
	protected void setLevelButtons(List<ImageIcon> sprites, List<String> names) {
		int numberLevel = getApp().getModel().getPersonalizedLevelNumber();
		for(int i = 0; i < NUMBER_OF_LEVEL; i++) {
			if(i + getPage() * NUMBER_OF_LEVEL + 1 > numberLevel) {
				return;
			}
			String path = getApp().getModel().getImagePathPersonalized((getPage() * NUMBER_OF_LEVEL + i + 1));
			ImageIcon imageIcon = new ImageIcon(path == null ? SPRITE_LOC + "nothing.png" : path);
			ImageIcon scaledImage = new ImageIcon(imageIcon.getImage().getScaledInstance(getResizeX(BUTTON_SIZE), getResizeX(BUTTON_SIZE), Image.SCALE_DEFAULT));
			sprites.add(scaledImage);
			String name = getApp().getModel().getNamePersonalized((getPage() * NUMBER_OF_LEVEL + i + 1));
			names.add(name == null ? "Niveau " + (getPage() * NUMBER_OF_LEVEL + i + 1) : name);
		}
	}
	
}
