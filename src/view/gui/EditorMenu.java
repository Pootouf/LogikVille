package view.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.plaf.metal.MetalCheckBoxIcon;

import main.LogikVille;
import model.Editor;
import model.House;
import model.LogikVilleModel;
import model.constraints.Constraint;
import model.constraints.IsInHouse;
import model.constraints.IsNotInHouse;
import model.constraints.IsNeighbourWith;
import model.constraints.IsNotNeighbourWith;
import model.constraints.ToTheLeftOf;
import model.constraints.IsWith;
import model.constraints.IsNotWith;
import model.entity.Animal;
import model.entity.Character;
import model.entity.Entity;
import utils.FileUtils;
import utils.GBC;
import view.element.GraphicCard;
import view.element.GraphicSolution;
import view.element.ClassicButton;
import view.element.ConstraintLabel;
import view.element.RoundPanel;

@SuppressWarnings("serial")
public class EditorMenu extends AbstractMenu {
	
	public static final int MAX_NAME_LENGTH = 10;
	public static final int NUMBER_OF_CONSTRAINT = 7;
	public static final int NUMBER_OF_UNITARY_CONSTRAINT = 2;
	public static final int NUMBER_OF_BINARY_CONSTRAINT = 5;
	public static final int MAXIMUM_NUMBER_OF_SOLUTIONS = 10;
	
	public static final int SOLUTION_SIZE = 50;
	
	//ATTRIBUTS
	
	private JLabel background;
	
	private JPanel initPanel;
	private JPanel buildPanel;
	private GraphicCard card;
	private Box cardBox;
	
	private ClassicButton validateButton;
	private ClassicButton returnButton;
	private JComboBox<Integer> houseList;
	private JCheckBox animalBox;
	private JTextField nameField;
	private ClassicButton imageButton;
	private ClassicButton nextButton;
	
	private JComboBox<ConstraintEnum> constraintList;
	private JPanel constraintBuilderPanel;
	
	private JComboBox<String> leftListConstraint;
	private JComboBox<String> rightListConstraint;
	
	private ClassicButton addButton;
	private ClassicButton cancelButton;
	private ClassicButton redoButton;
	private JLabel solutionLabel;
	private Box solutionList;
	
	private List<ConstraintLabel> constraintLabelList;
	private List<ConstraintLabel> constraintRemovedLabelList;
	
	private GBC constraintRightComboBox;
	
	private int numberOfEntity;
	private boolean hasAnimals;
	private String[] namesCharacter;
	private String[] namesAnimal;
	private String[] namesHouse;
	private String[] namesEntity;
	
	
	//CONSTRUCTEURS

	public EditorMenu(LogikVille lv) {
		super(lv);
		createView();
		placeComponents();
		createController();
	}
	
	
	//OUTILS
	
	private void createView() {
		ImageIcon imageIcon = new ImageIcon(SPRITE_LOC + "background-1.png");
		Icon scaledImage = new ImageIcon(imageIcon.getImage().getScaledInstance(getApp().getScreenPixelX(), getApp().getScreenPixelY(), Image.SCALE_SMOOTH));
		background = new JLabel(scaledImage);
		background.setLayout(new GridLayout(1, 2));
		background.setPreferredSize(new Dimension(getApp().getScreenPixelX(), getApp().getScreenPixelY()));
		
		initPanel = new RoundPanel(COLOR_TRANSPARENCY, new GridBagLayout());
		buildPanel = new JPanel(new GridBagLayout());
		buildPanel.setOpaque(false);
		
		nameField = new JTextField();
		nameField.setFont(FONT.deriveFont(Font.BOLD, getFontSize(20)));
		nameField.setColumns(MAX_NAME_LENGTH);
		
		imageButton = new ClassicButton("Choisir l'image du niveau");
		imageButton.setFont(FONT.deriveFont(Font.BOLD, getFontSize(20)));
		
		card = new GraphicCard("Carte", getResizeX(250), getResizeY(500));
		card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
		card.setFont(FONT.deriveFont(Font.BOLD, getFontSize(20)));
		card.setBorder(BorderFactory.createEmptyBorder(getResizeY(40), getResizeX(10), getResizeY(40), getResizeX(10)));
		
		cardBox = Box.createVerticalBox();
		cardBox.setOpaque(false);
		
		validateButton = new ClassicButton("Créer le niveau");
		validateButton.setPreferredSize(new Dimension(getResizeX(200), getResizeY(40)));
		validateButton.setFont(FONT.deriveFont(Font.BOLD, getFontSize(20)));
		validateButton.setEnabled(false);
		
		returnButton = new ClassicButton("Retour");
		returnButton.setPreferredSize(new Dimension(getResizeX(200), getResizeY(40)));
		returnButton.setFont(FONT.deriveFont(Font.BOLD, getFontSize(20)));
		
		Integer[] houseChoices = {3, 4, 5};
		houseList = new JComboBox<Integer>(houseChoices);
		houseList.setFont(FONT.deriveFont(Font.BOLD, getFontSize(20)));
		
		animalBox = new JCheckBox("Souhaitez-vous jouer avec des compagnons : ");
		animalBox.setHorizontalTextPosition(SwingConstants.LEFT);
		animalBox.setOpaque(false);
		animalBox.setFont(FONT.deriveFont(Font.BOLD, getFontSize(20)));
		animalBox.setIcon(new MetalCheckBoxIcon () {
		    protected int getControlSize() { return getResizeX(20); }
		});
		
		nextButton = new ClassicButton("Suivant");
		nextButton.setFont(FONT.deriveFont(Font.BOLD, getFontSize(20)));
		nextButton.setPreferredSize(new Dimension(getResizeX(200), getResizeY(40)));
		
		String[] constraintsTab = new String[NUMBER_OF_CONSTRAINT];
		for(int i = 1; i <= NUMBER_OF_CONSTRAINT; i++) {
			constraintsTab[i - 1] = new String("Contrainte " + i);
		}
		constraintList = new JComboBox<ConstraintEnum>();
		constraintList.setFont(FONT.deriveFont(Font.BOLD, getFontSize(20)));
		
		
		leftListConstraint = new JComboBox<String>();
		leftListConstraint.setFont(FONT.deriveFont(Font.BOLD, getFontSize(20)));
		rightListConstraint = new JComboBox<String>();
		rightListConstraint.setFont(FONT.deriveFont(Font.BOLD, getFontSize(20)));
		

		
		addButton = new ClassicButton("Ajouter");
		addButton.setFont(FONT.deriveFont(Font.BOLD, getFontSize(20)));
		cancelButton = new ClassicButton("Annuler");
		cancelButton.setFont(FONT.deriveFont(Font.BOLD, getFontSize(20)));
		cancelButton.setEnabled(false);
		
		redoButton  = new ClassicButton("Refaire");
		redoButton.setFont(FONT.deriveFont(Font.BOLD, getFontSize(20)));
		redoButton.setEnabled(false);
		
		solutionLabel = new JLabel("0 Solutions");
		solutionLabel.setFont(FONT.deriveFont(Font.BOLD, getFontSize(20)));
		solutionList = Box.createVerticalBox();
		solutionList.setOpaque(false);
		
		constraintBuilderPanel = new JPanel(new GridBagLayout());
		constraintBuilderPanel.setOpaque(false);
		
		constraintRightComboBox = new GBC(2, 0);
		
		constraintLabelList = new ArrayList<ConstraintLabel>();
		constraintRemovedLabelList = new ArrayList<ConstraintLabel>();
	}

	private void placeComponents() {
		{ //--
			JPanel p = new JPanel(new BorderLayout());
			{ //--
				p.setBorder(BorderFactory.createEmptyBorder(getResizeY(70), getResizeX(70), getResizeY(70), getResizeX(150)));
				p.setOpaque(false);
				JPanel q = new JPanel();
				{ //--
					{ //--
						JScrollPane jsp = new JScrollPane();
						{ //--
							jsp.setViewportView(cardBox);
							jsp.setOpaque(false);
							jsp.getViewport().setOpaque(false);
							jsp.setBorder(BorderFactory.createEmptyBorder());
						} //--
						card.add(jsp);
						card.add(Box.createVerticalGlue());
					} //--
					q.add(card);
					q.setOpaque(false);
				} //--
				p.add(q, BorderLayout.CENTER);
				q = new JPanel();
				{ //--
					q.setOpaque(false);
					q.add(validateButton);
					q.add(returnButton);
				} //--
				p.add(q, BorderLayout.SOUTH);
			} //--
			background.add(p);
			JPanel r = new JPanel(new GridLayout(1, 1));
			{ //--
				r.setBorder(BorderFactory.createEmptyBorder(getResizeY(100), getResizeX(80), getResizeY(100), getResizeX(80)));
				r.setOpaque(false);
				{ //--
					JPanel q = new JPanel();
					{ //--
						q.setOpaque(false);
						JLabel jb = new JLabel("Saisir le nom : ");
						jb.setFont(FONT.deriveFont(Font.BOLD, getFontSize(20)));
						q.add(jb);
						q.add(nameField);
					} //--
					initPanel.add(q, new GBC(0, 1).weight(1, 1).fill(GBC.HORIZONTAL));
					q = new JPanel();
					{ //--
						q.setOpaque(false);
						q.add(imageButton);
					} //--
					initPanel.add(q, new GBC(0, 2).weight(0, 1).fill(GBC.HORIZONTAL));
					q = new JPanel();
					{ //--
						q.setOpaque(false);
						JLabel jb = new JLabel("Choix du nombre de maisons : ");
						jb.setFont(FONT.deriveFont(Font.BOLD, getFontSize(20)));
						q.add(jb);
						q.add(houseList);
					} //--
					initPanel.add(q, new GBC(0, 3).weight(1, 1).fill(GBC.HORIZONTAL));
					q = new JPanel();
					{ //--
						q.setOpaque(false);
						q.add(animalBox);
					} //--
					initPanel.add(q, new GBC(0, 4).weight(0, 1).fill(GBC.HORIZONTAL));
					q = new JPanel();
					{ //--
						q.setOpaque(false);
						q.add(nextButton);
					} //--
					initPanel.add(q, new GBC(0, 5).weight(0, 1).fill(GBC.HORIZONTAL));
					initPanel.add(new JLabel(""), new GBC(1, 0).weight(0, 2));
					initPanel.add(new JLabel(""), new GBC(1, 2).weight(0, 2));
					initPanel.add(new JLabel(""), new GBC(1, 4).weight(0, 2));
					initPanel.add(new JLabel(""), new GBC(1, 6).weight(0, 2));
				} //--
				r.add(initPanel);
			} //--
			background.add(r);
		} //--
		this.add(background);
		
		{ //--
			JPanel p = new JPanel();
			{ //--
				p.setOpaque(false);
				p.add(constraintList);
			} //--
			buildPanel.add(p, new GBC(0, 0).weight(1, 1).fill(GBC.BOTH));
			{ //--
				constraintBuilderPanel.add(leftListConstraint, new GBC(0, 0).weight(1, 2));
				constraintBuilderPanel.add(rightListConstraint, constraintRightComboBox);
				constraintBuilderPanel.add(addButton, new GBC(1, 2).weight(2, 0).fill(GBC.HORIZONTAL));
				constraintBuilderPanel.add(cancelButton, new GBC(2, 2).weight(1, 2).fill(GBC.HORIZONTAL));
				constraintBuilderPanel.add(redoButton, new GBC(0, 2).fill(GBC.HORIZONTAL));
				constraintBuilderPanel.add(new JLabel(""), new GBC(3, 1).weight(0, 0));
			} //--
			buildPanel.add(constraintBuilderPanel, new GBC(0, 1).weight(0, 2).fill(GBC.BOTH));
			p = new RoundPanel(COLOR_TRANSPARENCY, new BorderLayout()) {
				@Override
				public Dimension getPreferredSize() {
					return getMaximumSize();
				}
			};
			{ //--
				p.setOpaque(false);
				JPanel q = new JPanel(new BorderLayout());
				{ //--
					q.setOpaque(false);
					JPanel r = new JPanel();
					{ //--
						r.setOpaque(false);
						JLabel jb = new JLabel("Solutions");
						jb.setFont(FONT.deriveFont(Font.BOLD, getFontSize(30)));
						r.add(jb);
					} //--
					q.add(r, BorderLayout.CENTER);
					q.add(solutionLabel, BorderLayout.EAST);
				} //--
				p.add(q, BorderLayout.NORTH);
				JScrollPane jsp = new JScrollPane();
				{ //--
					jsp.setOpaque(false);
					jsp.getViewport().setOpaque(false);
					jsp.setBorder(BorderFactory.createEmptyBorder());
					jsp.setViewportView(solutionList);
				}//--
				p.add(jsp, BorderLayout.CENTER);
				p.setBorder(BorderFactory.createEmptyBorder(getResizeY(5), getResizeX(20), getResizeY(20), getResizeX(20)));
			} //--
			buildPanel.add(p, new GBC(0, 2).weight(0, 3).fill(GBC.BOTH));
		} //--
	}



	private void createController() {
		addGameStateListener(returnButton, LogikVille.TITLE_STATE, new Runnable() {
			@Override
			public void run() {
				Container r = buildPanel.getParent();
				cardBox.removeAll();
				if(r != null) {
					r.remove(buildPanel);
					r.add(initPanel);
					r.revalidate();
				}
				getApp().getModel().getEditableLevel().reset();
				solutionList.removeAll();
				getApp().getModel().getEditableLevel().setImage(null);
				getApp().getModel().getEditableLevel().setName(null);
				nameField.setText("");
			}
		});
		
		constraintList.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				calculateComboBox((ConstraintEnum) constraintList.getSelectedItem());
			}
		});
		
		nextButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String text = nameField.getText();
				if(text.startsWith(" ") || text.startsWith("\t")) {
					showStylizedErrorDialogue( "Essaye de rentrer un autre nom !", "Nom invalide !");
					return;
				}
				if(text.length() > MAX_NAME_LENGTH) {
					showStylizedErrorDialogue( "Ce nom est trop long ! La taille maximum est de " + MAX_NAME_LENGTH + " caractères !", "Nom invalide !");
					return;
				}
				numberOfEntity = (int) houseList.getSelectedItem();
				hasAnimals = animalBox.isSelected();
				JLabel infoHouse = new JLabel("Maisons : " + numberOfEntity);
				infoHouse.setFont(FONT.deriveFont(Font.BOLD, getFontSize(15)));
				JLabel infoAnimals = new JLabel("Animaux : " + (hasAnimals ? "Oui" : "Non"));
				infoAnimals.setFont(FONT.deriveFont(Font.BOLD, getFontSize(15)));
				JPanel p = new JPanel(new BorderLayout()) {
					@Override
					public Dimension getMaximumSize() {
						return new Dimension(super.getMaximumSize().width, getPreferredSize().height);
					}
				};
				{ //--
					p.setBorder(BorderFactory.createEmptyBorder(0, getResizeX(5), 0, getResizeX(5)));
					p.setOpaque(false);
					p.add(infoHouse, BorderLayout.WEST);
					p.add(infoAnimals, BorderLayout.EAST);
				} //--
				cardBox.add(p);
				
				DefaultComboBoxModel<ConstraintEnum> model = new DefaultComboBoxModel<ConstraintEnum>( ConstraintEnum.getConstraint(hasAnimals));
				constraintList.setModel(model);
				
				getApp().getModel().getEditableLevel().setName(text);
				if(text.equals("")) {
					getApp().getModel().getEditableLevel().setName(null);
				}
				
				
				LogikVilleModel modelLogikVille = getApp().getModel();
				modelLogikVille.getEditableLevel().setNumberOfHouses(numberOfEntity);
				modelLogikVille.getEditableLevel().setHasAnimals(hasAnimals);
				
				
				List<String> characterNames = modelLogikVille.getEditableLevel().getCharactersNameList();
				List<String> animalNames = modelLogikVille.getEditableLevel().getAnimalsNameList();
				setNamesMenu(characterNames, animalNames, numberOfEntity);
				
				calculateComboBox(ConstraintEnum.CONSTRAINT1);
				
				modelLogikVille.getEditableLevel().calculateSolutionsSolver();
				
				Container r = initPanel.getParent();
				r.remove(initPanel);
				r.add(buildPanel);
				r.revalidate();
				
			}
		});
		
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						Editor editor = getApp().getModel().getEditableLevel();
						int constraintNumber = constraintList.getSelectedIndex() + 1;
						int selectedIndexLeft = leftListConstraint.getSelectedIndex();
						Entity firstEntity = null;
						boolean isAnimal = false;
						if(selectedIndexLeft >= numberOfEntity) {
							selectedIndexLeft -= numberOfEntity;
							isAnimal = true;
						}
						if(isAnimal) {
							firstEntity = editor.getAnimal(selectedIndexLeft);
						} else {
							firstEntity = editor.getCharacter(selectedIndexLeft);
						}
						
						Object object;
						isAnimal = false;
						int selectedIndexRight = rightListConstraint.getSelectedIndex();
						if(selectedIndexRight >= numberOfEntity) {
							selectedIndexRight -= numberOfEntity;
							isAnimal = true;
						}
						if(constraintNumber == 6 || constraintNumber == 7) {
							isAnimal = true;
						}
						if(constraintNumber <= NUMBER_OF_UNITARY_CONSTRAINT) {
							object = selectedIndexRight;
						} else {
							if(isAnimal) {
								object = editor.getAnimal(selectedIndexRight);
							} else {
								object = editor.getCharacter(selectedIndexRight);
							}
						}
						Constraint c = createConstraint(constraintNumber, firstEntity, object);
						
						editor.addConstraintChosen(c);
						constraintRemovedLabelList.clear();
						redoButton.setEnabled(false);
						
						SwingUtilities.invokeLater(new Runnable() {
							@Override
							public void run() {
								ConstraintLabel cl = buildConstraintLabel(c, numberOfEntity, getResizeX(20));
								constraintLabelList.add(cl);
								
								cardBox.add(cl);
								card.revalidate();
								
								cancelButton.setEnabled(true);
							}
						});
					}
				}).start();
			}
			
			
			private Constraint createConstraint(int index, Entity e, Object object) {
				Constraint c = null;
				House[] houses = getApp().getModel().getEditableLevel().getHouses();
				switch(index) {
					case 1:
						c = new IsInHouse<Entity>(houses, e, (Integer)object);
						break;
					case 2:
						c = new IsNotInHouse<Entity>(houses, e, (Integer)object);
						break;
					case 3:
						c = new IsNeighbourWith<Entity, Entity>(houses, e, (Entity) object);
						break;
					case 4:
						c = new IsNotNeighbourWith<Entity, Entity>(houses, e, (Entity)object);
						break;
					case 5:
						c = new ToTheLeftOf<Entity, Entity>(houses, e, (Entity)object);
						break;
					case 6:
						c = new IsWith<Character, Animal>(houses, (Character)e, (Animal)object);
						break;
					case 7:
						c = new IsNotWith<Character, Animal>(houses, (Character)e, (Animal)object);
						break;
				}
				return c;
			}
		});
		
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getApp().getModel().getEditableLevel().undo();
				ConstraintLabel cl = constraintLabelList.get(constraintLabelList.size() - 1);
				cardBox.remove(cl);
				constraintLabelList.remove(cl);
				constraintRemovedLabelList.add(cl);
				card.revalidate();
				redoButton.setEnabled(getApp().getModel().getEditableLevel().getConstraintsUndone().size() > 0);
				cancelButton.setEnabled(getApp().getModel().getEditableLevel().getConstraintsChosen().size() > 0);
				createSolutionsList();
			}
		});
		
		redoButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getApp().getModel().getEditableLevel().redo();
				ConstraintLabel cl = constraintRemovedLabelList.get(constraintRemovedLabelList.size() - 1);
				cardBox.add(cl);
				constraintLabelList.add(cl);
				constraintRemovedLabelList.remove(cl);
				card.revalidate();
				redoButton.setEnabled(getApp().getModel().getEditableLevel().getConstraintsUndone().size() > 0);
				cancelButton.setEnabled(getApp().getModel().getEditableLevel().getConstraintsChosen().size() > 0);
				createSolutionsList();
			}
		});
		
		Editor editor = getApp().getModel().getEditableLevel();
		editor.addPropertyChangeListener(Editor.PROP_SOLVE, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				SwingUtilities.invokeLater(new Runnable() {

					@Override
					public void run() {
						createSolutionsList();
					}
				});
			}
		});
		
		validateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					getApp().getModel().getEditableLevel().createNewLevel();
					showStylizedMessageDialogue( "Niveau créé avec succés !", "Bravo !");
				} catch (IOException e1) {
					showStylizedErrorDialogue( "Impossible de créer le niveau : " + e1.getMessage(), "Erreur de sauvegarde !");
				}
				getApp().getModel().reloadPersonalizedManager();
				((AbstractLevelMenu) getApp().getMenu(LogikVille.USERLEVEL_STATE)).refresh();
				getApp().getModel().getEditableLevel().setImage(null);
				getApp().getModel().getEditableLevel().setName(null);
				nameField.setText("");
			}
		});
		
		imageButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				File image = FileUtils.getImageWithChooser();
				if(image == null) {
					return;
				}
				String saveTo = "res/ressources_personalized/level/" + image.getName();
				try {
					((CreatorThemeMenu)(getApp().getMenu(LogikVille.THEME_STATE))).getModel().copyFile(image.getAbsolutePath(), saveTo);
				} catch (IOException e1) {
					showStylizedErrorDialogue( "Impossible de charger l'image : " + e1.getMessage(), "Erreur de chargement !");
				}
				getApp().getModel().getEditableLevel().setImage(saveTo);
			}
		});
	}
	
	private void createSolutionsList() {
		Editor editor = getApp().getModel().getEditableLevel();
		List<List<House>> solutions = editor.getSolutions();
		solutionList.removeAll();
		solutionLabel.setText(solutions.size() + " Solutions");
		if(solutions.size() == 1) {
			validateButton.setEnabled(true);
		} else {
			validateButton.setEnabled(false);
		}
		if(solutions.size() <= MAXIMUM_NUMBER_OF_SOLUTIONS) {
			for(List<House> solution : solutions) {
				GraphicSolution graphicSolution = new GraphicSolution(numberOfEntity, (hasAnimals ? 2 : 1));
				for(House h : solution) {
					JLabel jb = new JLabel(new ImageIcon(h.getCharacter().getPicture().getImage().getScaledInstance(getResizeX(SOLUTION_SIZE), -1, 0)));
					graphicSolution.add(jb);
				}
				if(hasAnimals) {
					for(House h : solution) {
						JLabel jb = new JLabel(new ImageIcon(h.getAnimal().getPicture().getImage().getScaledInstance(getResizeX(SOLUTION_SIZE), -1, 0)));
						graphicSolution.add(jb);
					}
				}
				graphicSolution.setBorder(BorderFactory.createEmptyBorder(getResizeY(10), getResizeX(10), getResizeY(10), getResizeX(10)));
				solutionList.add(graphicSolution);
			}
		}
		solutionList.revalidate();
	}
	private void setNamesMenu(List<String> characterNames, List<String> animalNames, int numberOfHouses) {
		namesCharacter = new String[characterNames.size()];
		namesAnimal = new String[animalNames.size()];
		for(int i = 0; i < namesCharacter.length; i++) {
			namesCharacter[i] = characterNames.get(i);
		}
		for(int i = 0; i < namesAnimal.length; i++) {
			namesAnimal[i] = animalNames.get(i);
		}
		namesHouse = new String[numberOfHouses];
		for(int i = 1; i <= numberOfHouses; i++) {
			namesHouse[i - 1] = ("Maison " + i);
		}
		namesEntity = new String[namesCharacter.length + namesAnimal.length];
		for(int i = 0; i < namesCharacter.length; i++) {
			namesEntity[i] = namesCharacter[i];
		}
		for(int i = 0; i < namesAnimal.length; i++) {
			namesEntity[i + namesCharacter.length] = namesAnimal[i];
		}
	}
	
	private void calculateComboBox(ConstraintEnum element) {
		String[] leftTab = null;
		String[] rightTab = null;
		if(element.equals(ConstraintEnum.CONSTRAINT1) || element.equals(ConstraintEnum.CONSTRAINT2)) {
			if(hasAnimals) {
				leftTab = namesEntity;
			} else {
				leftTab = namesCharacter;
			}
			rightTab = namesHouse;
		}
		if(element.equals(ConstraintEnum.CONSTRAINT3) || element.equals(ConstraintEnum.CONSTRAINT4) || element.equals(ConstraintEnum.CONSTRAINT5)) {
			if(hasAnimals) {
				leftTab = namesEntity;
				rightTab = namesEntity;
			} else {
				leftTab = namesCharacter;
				rightTab = namesCharacter;
			}
		}
		if(element.equals(ConstraintEnum.CONSTRAINT6) || element.equals(ConstraintEnum.CONSTRAINT7)) {
			leftTab = namesCharacter;
			rightTab = namesAnimal;
		}
		DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>( leftTab );
		leftListConstraint.setModel( model );
		model = new DefaultComboBoxModel<>( rightTab );
		rightListConstraint.setModel( model );
	}
	
	
	private enum ConstraintEnum {
		CONSTRAINT1("est dans") {
			@Override
			boolean canApply(boolean hasAnimals) {
				return true;
			}
		},
		CONSTRAINT2("n'est pas dans") {
			@Override
			boolean canApply(boolean hasAnimals) {
				return true;
			}
		},
		CONSTRAINT3("est voisin de") {
			@Override
			boolean canApply(boolean hasAnimals) {
				return true;
			}
		},
		CONSTRAINT4("n'est pas voisin de") {
			@Override
			boolean canApply(boolean hasAnimals) {
				return true;
			}
		},
		CONSTRAINT5("est à gauche de") {
			@Override
			boolean canApply(boolean hasAnimals) {
				return true;
			}
		},
		CONSTRAINT6("est dans la même maison que ") {
			@Override
			boolean canApply(boolean hasAnimals) {
				return hasAnimals;
			}
		},
		CONSTRAINT7("n'est pas dans la même maison que") {
			@Override
			boolean canApply(boolean hasAnimals) {
				return hasAnimals;
			}
		};

		private String name;
		
		ConstraintEnum(String string) {
			name = string;
		}
		
		@Override
		public String toString() {
			return name;
		}
		
		abstract boolean canApply(boolean hasAnimals);
		
		public static ConstraintEnum[] getConstraint(boolean hasAnimals) {
			List<ConstraintEnum> result = new ArrayList<ConstraintEnum>();
			for(ConstraintEnum e : ConstraintEnum.values()) {
				if(e.canApply(hasAnimals)) {
					result.add(e);
				}
			}
			ConstraintEnum[] tab = new ConstraintEnum[result.size()];
			for(int i = 0; i < result.size(); i++) {
				tab[i] = result.get(i);
			}
			return tab;
		}
	}

}
