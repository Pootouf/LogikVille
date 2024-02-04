package view.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import main.LogikVille;
import model.Card;
import model.Level;
import model.entity.Entity;
import util.Contract;
import view.element.ClassicButton;
import view.element.LevelButton;

@SuppressWarnings("serial")
public abstract class AbstractLevelMenu extends AbstractMenu {
	
	//CONSTANTES
	
	public static int NUMBER_OF_LEVEL = 8;
	public static int BUTTON_SIZE = 175;
	
	//ATTRIBUTS
	
	private JLabel background;
	
	private ClassicButton goLeft;
	private ClassicButton goRight;
	private ClassicButton buttonState;
	private ClassicButton returnButton;
	
	private JTextField research;
	
	private LevelButton[] levelButtons;
	
	private String title;
	private String buttonName;
	
	private JPopupMenu[] popupMenus; 
	private JMenuItem[] openMenus;
	private JMenuItem[] deleteMenus;
	
	private int pageLevel;
	private boolean isPersonnalized;
	
	
	//CONSTRUCTEUR
	
	public AbstractLevelMenu(LogikVille lv, boolean isPersonnalized, String buttonName) {
		super(lv);
		this.title = (isPersonnalized ? "Niveaux Personnalisés" : "Niveaux Classiques");
		this.isPersonnalized = isPersonnalized;
		this.buttonName = buttonName;
		createView();
		placeComponents();
		createController();
	}
	
	//REQUETES
	protected JButton getButtonState() {
		return buttonState;
	}
	
	protected boolean isPersonnalized() {
		return isPersonnalized;
	}
	
	protected JButton getLevelButton(int i) {
		Contract.checkCondition(0 <= i && i < NUMBER_OF_LEVEL , "Index de bouton invalide");
		return levelButtons[i];
	}
	
	protected int getPage() {
		return pageLevel;
	}
	
	protected JTextField getResearchField() {
		return research;
	}
	
	protected JPopupMenu[] getPopupMenus() {
		return popupMenus;
	}
	
	protected JMenuItem[] getOpenMenus() {
		return openMenus;
	}
	
	protected JMenuItem[] getDeleteMenus() {
		return deleteMenus;
	}
	
	//COMMANDES
	
	protected abstract LevelButton[] createLevelButtons();
	
	protected abstract void setLevelButtons(List<ImageIcon> sprites, List<String> names);
	
	public void setPageLevel(int i) {
		pageLevel = i;
	}
	
	public void refresh() { 
		int numberLevel;
		if(!isPersonnalized) {
			numberLevel = getApp().getModel().getClassicLevelNumber();
		} else {
			numberLevel = getApp().getModel().getPersonalizedLevelNumber();
		}
		pageLevel = (int) (pageLevel % ((Math.ceil((double)numberLevel / NUMBER_OF_LEVEL))));
		List<ImageIcon> sprites = new ArrayList<ImageIcon>();
		List<String> names = new ArrayList<String>();
		this.setLevelButtons(sprites, names);
		for(int i = 0; i < NUMBER_OF_LEVEL; i++) {
			if(i + pageLevel * NUMBER_OF_LEVEL + 1 > numberLevel) {
				levelButtons[i].setVisible(false);
			} else {
				levelButtons[i].setText(names.get(i));
				levelButtons[i].setIcon(sprites.get(i));
				levelButtons[i].setVisible(true);
			}
		}
	}
	
	//OUTILS

	protected void createView() {
		this.setBackground(Color.GRAY);
		
		ImageIcon imageIcon = new ImageIcon(SPRITE_LOC + "background-1.png");
		Icon scaledImage = new ImageIcon(imageIcon.getImage().getScaledInstance(getApp().getScreenPixelX(), getApp().getScreenPixelY(), Image.SCALE_SMOOTH));
		background = new JLabel(scaledImage);
		background.setLayout(new BorderLayout());
		background.setPreferredSize(new Dimension(getApp().getScreenPixelX(), getApp().getScreenPixelY()));
		
		levelButtons = createLevelButtons();
		
		goLeft = new ClassicButton("←");
		goLeft.setFont(FONT.deriveFont(Font.BOLD, getFontSize(50)));
		goRight = new ClassicButton("→");
		goRight.setFont(FONT.deriveFont(Font.BOLD, getFontSize(50)));
		buttonState = new ClassicButton(buttonName);
		buttonState.setFont(FONT.deriveFont(Font.BOLD, getFontSize(20)));
		returnButton = new ClassicButton("Retour");
		returnButton.setFont(FONT.deriveFont(Font.BOLD, getFontSize(20)));
		returnButton.setPreferredSize(new Dimension(getResizeX(300), returnButton.getSize().height));
		
		research = new JTextField();
		research.setColumns(12);	
		research.setFont(FONT.deriveFont(Font.BOLD, getFontSize(20)));
		
		popupMenus = new JPopupMenu[NUMBER_OF_LEVEL];
		
		for(int i = 0; i < NUMBER_OF_LEVEL; i++) {
			popupMenus[i] = new JPopupMenu("Menu");
		}
		
		openMenus = new JMenuItem[NUMBER_OF_LEVEL];
		deleteMenus = new JMenuItem[NUMBER_OF_LEVEL];
		for(int i = 0; i < NUMBER_OF_LEVEL; i++) {
			openMenus[i] = new JMenuItem("Ouvrir");
			openMenus[i].setFont(FONT.deriveFont(Font.BOLD, getFontSize(15)));
			deleteMenus[i] = new JMenuItem("Supprimer");
			deleteMenus[i].setFont(FONT.deriveFont(Font.BOLD, getFontSize(15)));
		}
		
		refresh();
	}
	
	
	protected void placeComponents() {
		{ //--
			JPanel p = new JPanel(new BorderLayout());
			{ //--
				JLabel jb = new JLabel(title);
				jb.setFont(FONT.deriveFont(Font.BOLD, getFontSize(60)));
				p.add(jb, BorderLayout.WEST);
				JPanel q = new JPanel(new GridLayout(2, 1));
				{ //--
					jb = new JLabel("Rechercher :");
					jb.setFont(FONT.deriveFont(Font.BOLD, getFontSize(30)));
					q.add(jb);
					q.add(research);
					q.setOpaque(false);
					q.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, getResizeX(150)));
				} //--
				p.add(q, BorderLayout.EAST);
				p.setOpaque(false);
				p.setBorder(BorderFactory.createEmptyBorder(getResizeY(30), getResizeX(50), getResizeY(30), 0));
			} //--
			background.add(p, BorderLayout.NORTH);
			background.add(goLeft, BorderLayout.WEST);
			background.add(goRight, BorderLayout.EAST);
			p = new JPanel(new BorderLayout());
			{ //--
				p.add(buttonState, BorderLayout.CENTER);
				p.add(returnButton, BorderLayout.EAST);
				p.setOpaque(false);
				p.setBorder(BorderFactory.createEmptyBorder(getResizeY(30), getResizeX(50), getResizeY(30), getResizeX(50)));
			} //--
			background.add(p, BorderLayout.SOUTH);
			p = new JPanel(new GridLayout(2, NUMBER_OF_LEVEL / 2));
			{ //--
				for(LevelButton jb : levelButtons) {
					JPanel q = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
					{ //--
						q.add(jb);
					} //--
					q.setOpaque(false);
					p.add(q);
				}
				p.setOpaque(false);
			} //--
			background.add(p, BorderLayout.CENTER);
		} //--
		this.add(background);
		
		for(int i = 0; i < NUMBER_OF_LEVEL; i++) {
			JPanel p = new JPanel();
			{
				JLabel menu = new JLabel("Menu");
				menu.setFont(FONT.deriveFont(Font.BOLD, getFontSize(15)));
				p.add(menu);
			}
			getPopupMenus()[i].add(p);
			getPopupMenus()[i].addSeparator();
			getPopupMenus()[i].add(openMenus[i]); 
			getPopupMenus()[i].add(deleteMenus[i]); 
		}
	}
	
	protected void createController() {
		addGameStateListener(returnButton, LogikVille.TITLE_STATE);
		goRight.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				pageLevel += 1;
				refresh();
			}
			
		});
		goLeft.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				pageLevel -= 1;
				if(pageLevel == -1) {
					int numberLevel;
					if(!isPersonnalized) {
						numberLevel = getApp().getModel().getClassicLevelNumber();
					} else {
						numberLevel = getApp().getModel().getPersonalizedLevelNumber();
					}
					pageLevel = (numberLevel / NUMBER_OF_LEVEL);
				}
				refresh();
			}
			
		});
		
		for(int i = 0; i < NUMBER_OF_LEVEL; i++) {
			final int j = i + 1;
			getLevelButton(i).addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					//événement click droit de la souris
					if (SwingUtilities.isRightMouseButton(e) && e.getClickCount() == 1){
						Point position  = SwingUtilities.convertPoint(getLevelButton(j - 1), e.getPoint(), getApp().getFrame());
						popupMenus[j - 1].show(getApp().getFrame() , position.x, position.y);
					}        
				}       
			});
			
		}
		
		
	}
	
	protected PropertyChangeListener getPCLLoad() {
		return new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						Level level = getApp().getModel().getCurrentLevel();
						Card card = level.getCard();
						List<ImageIcon> cPictures = new ArrayList<ImageIcon>();
						for(Entity e : level.getCharacterManager().getEntityList()) {
							cPictures.add(e.getPicture());
						}
						List<ImageIcon> aPictures = new ArrayList<ImageIcon>();
						for(Entity e : level.getAnimalManager().getEntityList()) {
							aPictures.add(e.getPicture());
						}
						PlayMenu menu = (PlayMenu) getApp().getMenu(LogikVille.PLAY_STATE);
						menu.setCharactersPictures(cPictures);
						menu.setAnimalsPictures(aPictures);
						menu.setConstraint(card.getConstraints());
						menu.setHasAnimals(card.hasAnimals());
						menu.setNumberOfEntity(card.getNumberOfCharacters());
						menu.initMenu();
						getApp().setGamestate(LogikVille.PLAY_STATE);
					}
				});
			}
		};
	}
}
