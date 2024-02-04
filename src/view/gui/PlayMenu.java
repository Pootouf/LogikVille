package view.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import main.LogikVille;
import model.House;
import model.Level;
import model.constraints.Constraint;
import model.entity.Entity;
import view.element.GraphicCard;
import view.element.ClassicButton;
import view.element.ConstraintLabel;
import view.element.EntityLabel;
import view.element.GraphicHouse;
import view.element.WrapLayout;

@SuppressWarnings("serial")
public class PlayMenu extends AbstractMenu {
	
	private static final int SPRITE_SIZE = 50;
	private static final int CONSTRAINT_SIZE = 35;
	
	private static final int HOUSE_SIZEX = 200;
	private static final int HOUSE_SIZEY = 240;

	//ATTRIBUTS
	
	private int numberOfEntity;
	private boolean hasAnimals;
	private List<Constraint> constraintList;
	private List<ImageIcon> charactersPictures;
	private List<ImageIcon> animalsPictures;
	
	private JLabel background;
	
	private ClassicButton returnButton;
	private ClassicButton resetButton;
	private ClassicButton validateButton;
	private ClassicButton helpButton;
	private EntityLabel[] characterSprites;
	private EntityLabel[] animalSprites;
	private JPanel characterPanel;
	private JPanel animalPanel;
	private GraphicCard card;
	private List<ConstraintLabel> constraintLabels;
	
	private GraphicHouse[] houses;
	
	//CONSTRUCTEURS
	
	public PlayMenu(LogikVille lv) {
		super(lv);
	}
	
	//COMMANDES
	
	protected void initMenu() {
		this.removeAll();
		createView();
		placeComponents();
		createController();
	}
	
	protected void setHasAnimals(boolean b) {
		hasAnimals = b;
	}
	
	protected void setNumberOfEntity(int number) {
		numberOfEntity = number;
	}
	
	protected void setConstraint(List<Constraint> constraintList) {
		this.constraintList = new ArrayList<Constraint>(constraintList);
	}
	
	protected void setCharactersPictures(List<ImageIcon> cPictures) {
		charactersPictures = new ArrayList<ImageIcon>(cPictures);
	}
	
	protected void setAnimalsPictures(List<ImageIcon> pictures) {
		animalsPictures = new ArrayList<ImageIcon>(pictures);
	}
	
	//OUTILS
	
	private void createView() {	
		ImageIcon imageIcon = new ImageIcon(SPRITE_LOC + "background-1.png");
		Icon scaledImage = new ImageIcon(imageIcon.getImage().getScaledInstance(getApp().getScreenPixelX(), getApp().getScreenPixelY(), Image.SCALE_SMOOTH));
		background = new JLabel(scaledImage);
		background.setLayout(new BorderLayout());
		background.setPreferredSize(new Dimension(getApp().getScreenPixelX(), getApp().getScreenPixelY()));
		
		returnButton = new ClassicButton("Retour");
		returnButton.setFont(FONT.deriveFont(Font.BOLD, getFontSize(20)));
		returnButton.setPreferredSize(new Dimension(getResizeX(300), returnButton.getPreferredSize().height));
		
		validateButton = new ClassicButton("Valider");
		validateButton.setFont(FONT.deriveFont(Font.BOLD, getFontSize(20)));
		validateButton.setPreferredSize(new Dimension(getResizeX(300), validateButton.getPreferredSize().height));
		
		resetButton = new ClassicButton("Recommencer");
		resetButton.setFont(FONT.deriveFont(Font.BOLD, getFontSize(20)));
		
		helpButton = new ClassicButton("Aide");
		helpButton.setFont(FONT.deriveFont(Font.BOLD, getFontSize(20)));
		helpButton.setPreferredSize(new Dimension(getResizeX(300), helpButton.getPreferredSize().height));
		
		characterSprites = new EntityLabel[numberOfEntity];
		animalSprites = new EntityLabel[numberOfEntity];
		houses = new GraphicHouse[numberOfEntity];
		for(int i = 0; i < numberOfEntity; i++) {
			imageIcon = charactersPictures.get(i);
			scaledImage = new ImageIcon(imageIcon.getImage().getScaledInstance(getResizeX(SPRITE_SIZE * (1 - numberOfEntity / 50f)), getResizeX(SPRITE_SIZE * (1 - numberOfEntity / 50f)), Image.SCALE_DEFAULT));
			characterSprites[i] = new EntityLabel(scaledImage, i);
			if(hasAnimals) {
				imageIcon = animalsPictures.get(i);
				scaledImage = new ImageIcon(imageIcon.getImage().getScaledInstance(getResizeX(SPRITE_SIZE * (1 - numberOfEntity / 50f)), getResizeX(SPRITE_SIZE * (1 - numberOfEntity / 50f)), Image.SCALE_DEFAULT));
				animalSprites[i] = new EntityLabel(scaledImage, i);
			}
			houses[i] = new GraphicHouse(getResizeX(HOUSE_SIZEX * (1 - numberOfEntity / 50f)), getResizeY(HOUSE_SIZEY * (1 - numberOfEntity / 50f)), i);
		}
		
		characterPanel = new JPanel(new WrapLayout());
		characterPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, getResizeX(4), true), "Personnages disponibles :", TitledBorder.LEADING, TitledBorder.ABOVE_TOP, FONT.deriveFont(Font.BOLD, getFontSize(20))));
		characterPanel.setOpaque(false);
		
		
		animalPanel = new JPanel(new WrapLayout());
		animalPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, getResizeX(4), true), "Animaux disponibles :", TitledBorder.LEADING, TitledBorder.ABOVE_TOP, FONT.deriveFont(Font.BOLD, getFontSize(20))));
		animalPanel.setOpaque(false);
		animalPanel.setVisible(hasAnimals);
		
		card = new GraphicCard("Carte", getResizeX(250), getResizeY(500));
		card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
		card.setFont(FONT.deriveFont(Font.BOLD, getFontSize(20)));
		card.setBorder(BorderFactory.createEmptyBorder(getResizeY(40), 0, getResizeY(40), 0));
		constraintLabels = new ArrayList<ConstraintLabel>();
		for(Constraint c : constraintList) {
			ConstraintLabel cl = buildConstraintLabel(c, numberOfEntity, CONSTRAINT_SIZE);
			constraintLabels.add(cl);
			cl.setBorder(BorderFactory.createEmptyBorder(getResizeY(10), getResizeX(20), getResizeY(10), getResizeX(20)));
		}
	}

	private void placeComponents() {
		{
			JPanel p = new JPanel(new GridLayout(1, 3));
			{ //--
				JPanel q = new JPanel(new BorderLayout());
				{ //--
					{ //--
						for(JLabel j : characterSprites) {
							characterPanel.add(j);
						}
						characterPanel.setPreferredSize(characterPanel.getPreferredSize());
					} //--
					q.add(characterPanel, BorderLayout.CENTER);
					q.setOpaque(false);
				} //--
				p.add(q);
				q = new JPanel(new GridBagLayout());
				{ //--
					q.setOpaque(false);
					q.add(helpButton);
				} //--
				p.add(q);
				q = new JPanel(new BorderLayout());
				{ //--
					{ //--
						if(hasAnimals) {
							for(JLabel j : animalSprites) {
								animalPanel.add(j);
							}
						}
						animalPanel.setPreferredSize(animalPanel.getPreferredSize());
					} //--
					q.add(animalPanel, BorderLayout.CENTER);
					q.setOpaque(false);
				} //--
				p.add(q);
				p.setOpaque(false);
				p.setBorder(BorderFactory.createEmptyBorder(getResizeY(10), getResizeX(10), 0, getResizeX(10)));
			} //--
			background.add(p, BorderLayout.NORTH);
			p = new JPanel(new BorderLayout());
			{ //--
				p.add(validateButton, BorderLayout.WEST);
				p.add(resetButton, BorderLayout.CENTER);
				p.add(returnButton, BorderLayout.EAST);
				p.setOpaque(false);
				p.setBorder(BorderFactory.createEmptyBorder(0, getResizeX(50), getResizeY(30), getResizeX(50)));
			} //--
			background.add(p, BorderLayout.SOUTH);
			p = new JPanel(new GridLayout(1, 1));
			{ //--
				{ //--
					JScrollPane jsp = new JScrollPane();
					{ //--
						Box b = Box.createVerticalBox();
						{ //--
							b.setOpaque(false);
							for(ConstraintLabel cl : constraintLabels) {
								b.add(cl);
							}
							b.add(Box.createVerticalGlue());
						} //--
						jsp.setViewportView(b);
						jsp.setOpaque(false);
						jsp.getViewport().setOpaque(false);
						jsp.setBorder(BorderFactory.createEmptyBorder());
					} //--
					card.add(jsp);
					card.add(Box.createVerticalGlue());
				} //--
				p.add(card);
				p.setOpaque(false);
				p.setBorder(BorderFactory.createEmptyBorder(getResizeY(25), getResizeX(10), getResizeY(25), 0));
			} //--
			background.add(p, BorderLayout.WEST);
			Box b = Box.createVerticalBox();
			{ //--
				b.add(Box.createVerticalGlue());
				//On utilise un WrapLayout pour corriger le probleme du FlowLayout : celui ci ne recalcule pas sa taille préférée à l'arrivée d'une nouvelle ligne
				JPanel q = new JPanel(new WrapLayout()) {
					@Override
					public Dimension getMaximumSize() {
						return getPreferredSize();
					}
				};
				{ //--
					q.setBorder(BorderFactory.createEmptyBorder(0, getResizeX(100), 0, getResizeX(100)));
					for(int i = 0; i < numberOfEntity; i++) {
						q.add(houses[i]);
					}
					q.setOpaque(false);
				} //--
				b.add(q);
				b.add(Box.createVerticalGlue());
				b.setOpaque(false);
			} //--
			background.add(b, BorderLayout.CENTER);
		}
		this.add(background);
	}
	
	private void createController() {
		addGameStateListener(returnButton, LogikVille.LEVEL_STATE);
		for(int i = 0; i < numberOfEntity; i++) {
			MouseHandler mh = new MouseHandler(characterPanel, false);
			characterSprites[i].addMouseListener(mh);
			characterSprites[i].addMouseMotionListener(mh);
			
			if(hasAnimals) {
				mh = new MouseHandler(animalPanel, true);
				animalSprites[i].addMouseListener(mh);
				animalSprites[i].addMouseMotionListener(mh);
			}
			
		}
		
		validateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (getApp().getModel().getCurrentLevel().isLevelWon()) {
					validateButton.setFocusPainted(false);
					getApp().setGamestate(LogikVille.VICTORY_STATE);
				} else {
					showStylizedMessageDialogue("Mauvaise Solution, essaye encore !", "Echec");
				}
			}
		});
		
		resetButton.addActionListener(new ActionListener( ) {
			@Override
			public void actionPerformed(ActionEvent e) {
				for(EntityLabel entity : characterSprites) {
					characterPanel.add(entity);
				}
				characterPanel.revalidate();
				if(hasAnimals) {
					for(EntityLabel entity : animalSprites) {
						animalPanel.add(entity);
					}
					animalPanel.revalidate();
				}
				for(GraphicHouse gh : houses) {
					gh.setComponentNorthAlreadyInUse(false);
					gh.setComponentSouthAlreadyInUse(false);
				}
				getApp().getModel().getCurrentLevel().reset();
			}
		});
		
		helpButton.addActionListener(new ActionListener() {
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
	
	
	
	private class MouseHandler extends MouseAdapter {
		
		private Point offset;
		private JPanel entityPanel;
		private GraphicHouse dragHouse;
		
		
		private boolean isAnimal;
		
		public MouseHandler(JPanel p, boolean isAnimal) {
			entityPanel = p;
			this.isAnimal = isAnimal;
		}
		
		/*
		 * Passe le composant sur lequel on a cliqué sur le layeredPane pour le déplacer
		 */
		@Override
		public void mousePressed(MouseEvent e) {
			JComponent c = (JComponent) e.getSource();
            offset = e.getPoint();
            JFrame frame = getApp().getFrame();
            Point position  = SwingUtilities.convertPoint(c.getParent(), c.getLocation(), frame);
            position.setLocation(position.x - frame.getInsets().left, position.y - frame.getInsets().top);
            Container h = c.getParent();
            manageHouse(h);
            
            if(h instanceof GraphicHouse) {
	            Level level = getApp().getModel().getCurrentLevel();
	            House house = level.getHouse(((GraphicHouse) h).getIndex());
				Entity entity = null;
				if(isAnimal) {
					entity = level.getAnimalManager().getEntity(((EntityLabel)e.getComponent()).getIndex());
				} else {
					entity = level.getCharacterManager().getEntity(((EntityLabel)e.getComponent()).getIndex());
				}
				level.removeEntity(entity, house);
				dragHouse = (GraphicHouse) h;
            }
			
            frame.getLayeredPane().add(c, JLayeredPane.DRAG_LAYER);
            c.setLocation(position);
            frame.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            frame.revalidate();
		}
		
		@Override
		public void mouseDragged(MouseEvent e) {
			JComponent c = (JComponent) e.getSource();
			c.setLocation(c.getX() + e.getX() - offset.x, c.getY() + e.getY() - offset.y);
		}
		
		@Override
		public void mouseReleased(MouseEvent e) {
			JFrame frame = getApp().getFrame();
			frame.getLayeredPane().remove(e.getComponent());
			boolean houseFound = false;
			Level level = getApp().getModel().getCurrentLevel();
			for(GraphicHouse h : houses) {
				if(isInHouse(h, e.getLocationOnScreen())) {
					if(h.isValidAddition(isAnimal)) {
						House house = level.getHouse(h.getIndex());
						Entity entity = null;
						if(isAnimal) {
							h.addComponentSouth(e.getComponent());
							entity = level.getAnimalManager().getEntity(((EntityLabel)e.getComponent()).getIndex());
						} else {
							h.addComponentNorth(e.getComponent());
							entity = level.getCharacterManager().getEntity(((EntityLabel)e.getComponent()).getIndex());
						}
						level.setEntity(entity, house);
						houseFound = true;
						
						break;
					} else {
						EntityLabel entityLabelDrag = (EntityLabel)e.getComponent();
						if (isAnimal) {
							EntityLabel entityLabelOld = (EntityLabel) h.getComponentSouth();
							Entity entityOld = level.getAnimalManager().getEntity(entityLabelOld.getIndex());
							Entity entityDrag = level.getAnimalManager().getEntity(entityLabelDrag.getIndex());
							h.remove(entityLabelOld);
							if(dragHouse != null) {
								dragHouse.addComponentSouth(entityLabelOld);
								h.addComponentSouth(entityLabelDrag);
								level.setEntity(entityDrag, level.getHouse(dragHouse.getIndex()));
								level.exchangeEntities(entityOld, level.getHouse(h.getIndex()), entityDrag, level.getHouse(dragHouse.getIndex()));
							} else {
								entityPanel.add(entityLabelOld);
								h.addComponentSouth(entityLabelDrag);
								level.removeEntity(entityOld, level.getHouse(h.getIndex()));
								level.setEntity(entityDrag, level.getHouse(h.getIndex()));
							}
						} else {
							EntityLabel entityLabelOld = (EntityLabel) h.getComponentNorth();
							Entity entityOld = level.getCharacterManager().getEntity(entityLabelOld.getIndex());
							Entity entityDrag = level.getCharacterManager().getEntity(entityLabelDrag.getIndex());
							h.remove(entityLabelOld);
							if(dragHouse != null) {
								dragHouse.addComponentNorth(entityLabelOld);
								h.addComponentNorth(entityLabelDrag);
								level.setEntity(entityDrag, level.getHouse(dragHouse.getIndex()));
								level.exchangeEntities(entityOld, level.getHouse(h.getIndex()), entityDrag, level.getHouse(dragHouse.getIndex()));
							} else {
								entityPanel.add(entityLabelOld);
								h.addComponentNorth(entityLabelDrag);
								level.removeEntity(entityOld, level.getHouse(h.getIndex()));
								level.setEntity(entityDrag, level.getHouse(h.getIndex()));
							}
						}
						houseFound = true;
						break;
					}
				}
			}
			dragHouse = null;
			if(!houseFound) {
				entityPanel.add(e.getComponent());
			}
			frame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			frame.revalidate();
		}
		
		/*
		 * Calcule si l'endroit que le point indique correspond à une maison
		 */
		private boolean isInHouse(GraphicHouse h, Point position) {
			Point corner = h.getLocationOnScreen();
			Dimension size = h.getSize();
			return  corner.x <= position.x && position.x <= corner.x + size.width && corner.y <= position.y && position.y <= corner.y + size.height;
		}
		
		/*
		 * Permet d'indiquer à un conteneur, si c'est une maison, qu'un de ses deux éléments est parti
		 */
		private void manageHouse(Container container) {
			if(container instanceof GraphicHouse) {
				GraphicHouse gh = (GraphicHouse) container;
				if(isAnimal) {
					gh.setComponentSouthAlreadyInUse(false);
				} else {
					gh.setComponentNorthAlreadyInUse(false);
				}
			}
		}
		
	}
	


}
