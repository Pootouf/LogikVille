package view.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import main.LogikVille;
import model.Level;
import model.LogikVilleModel;
import model.save.ThemeSaver;
import utils.FileUtils;
import utils.GBC;
import view.element.ClassicButton;
import view.element.RoundPanel;


@SuppressWarnings("serial")
public class CreatorThemeMenu extends AbstractMenu {
	
	public static final int MAX_NAME_LENGTH = 10;
	public static final int MAX_ENTITY = Level.MAX_HOUSES;
	public static final int SPRITE_SIZE = 80;
	public static final String THEME_LOC = "res/ressources_personalized/themes/";
	
	//ATTRIBUTS
	
	private JLabel background;
	
	private JLabel[] characterSprites;
	private JLabel[] animalSprites;
	
	private JTextField[] characterNames;
	private JTextField[] animalNames;
	
	private ClassicButton[] characterChoosers;
	private ClassicButton[] animalChoosers;
	
	private JComboBox<String> loadTheme;
	private ClassicButton confirmLoadButton;
	
	private JTextField nameTheme;
	
	private ClassicButton returnButton;
	private ClassicButton validateButton;
	private ClassicButton resetButton;
	private ClassicButton deleteButton;
	
	private boolean isLoaded;
	private List<String> characterDefaultNames;
	private List<String> animalDefaultNames;
	private List<ImageIcon> characterDefaultSprites;
	private List<ImageIcon> animalDefaultSprites;
	
	private String themeLoaded;
	
	private List<String> imageCharacterAbsolutePaths;
	private List<String> imageAnimalAbsolutePaths;
	
	private ThemeSaver themeSaver;
	
	
	//CONSTRUCTEUR
	
	public CreatorThemeMenu(LogikVille lv) {
		super(lv);
		createModel();
	}
	
	//REQUETES
	
	public ThemeSaver getModel() {
		return themeSaver;
	}
	
	//COMMANDES
	
	public void setCharacterDefaultNames(List<String> characterDN) {
		characterDefaultNames = new ArrayList<String>(characterDN);
	}
	
	public void setAnimalDefaultNames(List<String> animalDN) {
		animalDefaultNames = new ArrayList<String>(animalDN);
	}
	
	public void setCharacterDefaultSprites(List<ImageIcon> characterDS) {
		characterDefaultSprites = new ArrayList<ImageIcon>(characterDS);
	}
	
	public void setAnimalDefaultSprites(List<ImageIcon> animalDS) {
		animalDefaultSprites = new ArrayList<ImageIcon>(animalDS);
	}
	
	public void setCharacterImagePath(List<String> characterIP) {
		imageCharacterAbsolutePaths = new ArrayList<String>(characterIP);
	}
	
	public void setAnimalImagePath(List<String> animalIP) {
		imageAnimalAbsolutePaths = new ArrayList<String>(animalIP);
	}
	
	public void initMenu() {
		createView();
		placeComponents();
		createController();
		this.revalidate();
	}
	
	public void setInfoMenu(String nameTheme) {
		getModel().loadTheme(THEME_LOC + nameTheme + "/entity.txt", CreatorThemeMenu.MAX_ENTITY);
		setCharacterDefaultNames(getModel().getCharacterNameList());
		setAnimalDefaultNames(getModel().getAnimalNameList());
		setCharacterDefaultSprites(getModel().getListImageCharacter());
		setAnimalDefaultSprites(getModel().getListImageAnimal());
		setCharacterImagePath(getModel().getListImageCharacterPath());
		setAnimalImagePath(getModel().getListImageAnimalPath());
		initMenu();
	}

	//OUTILS
	
	private void createModel() {
		themeSaver = new ThemeSaver();
	}

	private void createView() {
		this.setBackground(Color.GRAY);
		
		ImageIcon imageIcon = new ImageIcon(SPRITE_LOC + "background-2.png");
		Icon scaledImage = new ImageIcon(imageIcon.getImage().getScaledInstance(getApp().getScreenPixelX(), getApp().getScreenPixelY(), Image.SCALE_SMOOTH));
		background = new JLabel(scaledImage);
		background.setLayout(new BoxLayout(background, BoxLayout.Y_AXIS));
		background.setPreferredSize(new Dimension(getApp().getScreenPixelX(), getApp().getScreenPixelY()));
		
		
		returnButton = new ClassicButton("Retour");
		returnButton.setFont(FONT.deriveFont(Font.BOLD, getFontSize(25)));
		returnButton.setPreferredSize(new Dimension(getResizeX(300), returnButton.getPreferredSize().height));
		
		resetButton = new ClassicButton("Réinitialiser");
		resetButton.setFont(FONT.deriveFont(Font.BOLD, getFontSize(25)));
		resetButton.setPreferredSize(new Dimension(getResizeX(300), resetButton.getPreferredSize().height));
		
		validateButton = new ClassicButton("Créer le thème");
		validateButton.setFont(FONT.deriveFont(Font.BOLD, getFontSize(25)));
		
		characterSprites = new JLabel[MAX_ENTITY];
		animalSprites = new JLabel[MAX_ENTITY];
		characterNames = new JTextField[MAX_ENTITY];
		animalNames = new JTextField[MAX_ENTITY];
		for(int i = 0; i < MAX_ENTITY; i++) {
			characterSprites[i] = new JLabel(new ImageIcon(characterDefaultSprites.get(i).getImage().getScaledInstance(getResizeX(SPRITE_SIZE), getResizeX(SPRITE_SIZE), 0)));
			animalSprites[i] = new JLabel(new ImageIcon(animalDefaultSprites.get(i).getImage().getScaledInstance(getResizeX(SPRITE_SIZE), getResizeX(SPRITE_SIZE), 0)));
			characterNames[i] = new JTextField(characterDefaultNames.get(i));
			characterNames[i].setColumns(6);
			characterNames[i].setFont(FONT.deriveFont(Font.BOLD, getFontSize(15)));
			animalNames[i] = new JTextField(animalDefaultNames.get(i));
			animalNames[i].setColumns(6);
			animalNames[i].setFont(FONT.deriveFont(Font.BOLD, getFontSize(15)));
		}
		
		characterChoosers = new ClassicButton[MAX_ENTITY];
		animalChoosers = new ClassicButton[MAX_ENTITY];
		for(int i = 0; i < MAX_ENTITY; i++) {
			characterChoosers[i] = new ClassicButton("Image");
			characterChoosers[i].setFont(FONT.deriveFont(Font.BOLD, getFontSize(15)));
			animalChoosers[i] = new ClassicButton("Image");
			animalChoosers[i].setFont(FONT.deriveFont(Font.BOLD, getFontSize(15)));
		}
		
		loadTheme = new JComboBox<String>(getThemesNames());
		loadTheme.setFont(FONT.deriveFont(Font.BOLD, getFontSize(20)));
		
		confirmLoadButton = new ClassicButton("Charger le thème");
		confirmLoadButton.setFont(FONT.deriveFont(Font.BOLD, getFontSize(20)));
		deleteButton = new ClassicButton("Effacer le thème");
		deleteButton.setFont(FONT.deriveFont(Font.BOLD, getFontSize(20)));
		
		nameTheme = new JTextField();
		nameTheme.setFont(FONT.deriveFont(Font.BOLD, getFontSize(20)));
		nameTheme.setColumns(MAX_NAME_LENGTH);
	}
	
	private String[] getThemesNames() {
		File f = new File("res/ressources_personalized/themes/");
		String[] temp = f.list();
		List<String> list = new ArrayList<String>();
		for(int i = 0; i < temp.length; i++) {
			if(!temp[i].equals("classic")) {
				list.add(temp[i]);
			}
		}
		String[] result = new String[list.size()];
		for(int i = 0; i < list.size(); i++) {
			result[i] = list.get(i);
		}
		return result;
	}
	
	private void placeComponents() {
		this.removeAll();
		{ //--
			JPanel p = new JPanel(new BorderLayout());
			{ //--
				p.setOpaque(false);
				p.setBorder(BorderFactory.createEmptyBorder(getResizeY(30), getResizeX(100), getResizeY(30), getResizeX(100)));
				JPanel q = new JPanel(new BorderLayout());
				{ //--
					q.setOpaque(false);
					JPanel r = new RoundPanel(COLOR_TRANSPARENCY, new FlowLayout());
					{ //--
						r.setBorder(BorderFactory.createEmptyBorder(0, getResizeX(20), getResizeY(20), getResizeX(20)));
						r.setOpaque(false);
						JPanel s = new JPanel(new GridBagLayout());
						{ //--
							s.setOpaque(false);
							for(int i = 0; i < MAX_ENTITY; i++) {
								s.add(characterSprites[i], new GBC(i, 0).weight(0, 3));
							}
							for(int i = 0; i < MAX_ENTITY; i++) {
								s.add(characterNames[i], new GBC(i, 1).weight(0, 0));
							}
							for(int i = 0; i < MAX_ENTITY; i++) {
								s.add(characterChoosers[i], new GBC(i, 2).weight(0, 0));
							}
							s.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 5, true), "Personnages :", TitledBorder.LEFT, TitledBorder.ABOVE_TOP, FONT.deriveFont(Font.BOLD, getFontSize(40))));
						} //--
						r.add(s);
					} //--
					q.add(r, BorderLayout.WEST);
					r = new RoundPanel(COLOR_TRANSPARENCY, new FlowLayout());
					{ //--
						r.setOpaque(false);
						r.setBorder(BorderFactory.createEmptyBorder(0, getResizeX(20), getResizeY(20), getResizeX(20)));
						JPanel s = new JPanel(new GridBagLayout());
						{ //--
							s.setOpaque(false);
							for(int i = 0; i < MAX_ENTITY; i++) {
								s.add(animalSprites[i], new GBC(i, 0).weight(0, 3));
							}
							for(int i = 0; i < MAX_ENTITY; i++) {
								s.add(animalNames[i], new GBC(i, 1).weight(0, 0));
							}
							for(int i = 0; i < MAX_ENTITY; i++) {
								s.add(animalChoosers[i], new GBC(i, 2).weight(0, 0));
							}
							s.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 5, true), "Animaux :", TitledBorder.LEFT, TitledBorder.ABOVE_TOP, FONT.deriveFont(Font.BOLD, getFontSize(40))));
						} //--
						r.add(s);
					} //--
					q.add(r, BorderLayout.EAST);
				} //--
				p.add(q, BorderLayout.NORTH);
				q = new JPanel(new GridBagLayout());
				{ //--
					q.setOpaque(false);
					JPanel r = new RoundPanel(COLOR_TRANSPARENCY, new GridBagLayout());
					{ //--
						r.setBorder(BorderFactory.createEmptyBorder(getResizeY(20), getResizeX(20), getResizeY(20), getResizeX(20)));
						r.setOpaque(false);
						JLabel jl = new JLabel("Sélectionner un thème :");
						jl.setFont(FONT.deriveFont(Font.BOLD, getFontSize(20)));
						r.add(jl, new GBC(0, 0).anchor(GBC.SOUTHEAST));
						r.add(loadTheme, new GBC(1, 0).anchor(GBC.SOUTHWEST));
						r.add(confirmLoadButton, new GBC(0, 1, 2, 1).insets(getResizeX(5)));
						r.add(deleteButton, new GBC(0, 2, 2, 1).insets(getResizeX(5)));
					} //--
					q.add(r);
				} //--
				p.add(q, BorderLayout.WEST);
				q = new JPanel(new GridBagLayout());
				{ //--
					q.setOpaque(false);
					JPanel r = new RoundPanel(COLOR_TRANSPARENCY, new GridBagLayout());
					{ //--
						r.setBorder(BorderFactory.createEmptyBorder(getResizeY(20), getResizeX(20), getResizeY(20), getResizeX(20)));
						JPanel s = new JPanel(new GridBagLayout());
						{ //--
							JLabel jl = new JLabel("Entrez le nom du thème :");
							jl.setFont(FONT.deriveFont(Font.BOLD, getFontSize(20)));
							s.add(jl, new GBC(0, 0));
							s.setOpaque(false);
							s.add(nameTheme, new GBC(1, 0));
						} //--
						r.add(s, new GBC(0, 0).anchor(GBC.LINE_START));
						JLabel jl = new JLabel("Si vide, le thème chargé sera modifié");
						jl.setFont(FONT.deriveFont(Font.BOLD, getFontSize(20)));
						r.add(jl, new GBC(0, 1).anchor(GBC.LINE_START));
					} //--
					q.add(r);
				} //--
				p.add(q, BorderLayout.EAST);
				q = new JPanel(new BorderLayout());
				{//--
					q.setOpaque(false);
					q.add(returnButton, BorderLayout.EAST);
					q.add(validateButton, BorderLayout.CENTER);
					q.add(resetButton, BorderLayout.WEST);
				}//--
				p.add(q, BorderLayout.SOUTH);
			} //--
			background.add(p);
		} //--
		this.add(background);
	}
	
	private void createController() {
		addGameStateListener(returnButton, LogikVille.OPTION_STATE);
		
		for(int i = 0; i < MAX_ENTITY; i++) {
			final int j = i;
			characterChoosers[i].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					File f = FileUtils.getImageWithChooser();
					if(f != null) {
						ImageIcon sprite = new ImageIcon(new ImageIcon(f.getAbsolutePath()).getImage().getScaledInstance(getResizeX(SPRITE_SIZE), getResizeX(SPRITE_SIZE), 0));
						characterSprites[j].setIcon(sprite);
						imageCharacterAbsolutePaths.set(j, f.getAbsolutePath());
						return;
					}
				}
			});
			animalChoosers[i].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					File f = FileUtils.getImageWithChooser();
					if(f != null) {
						ImageIcon sprite = new ImageIcon(new ImageIcon(f.getAbsolutePath()).getImage().getScaledInstance(getResizeX(SPRITE_SIZE), getResizeX(SPRITE_SIZE), 0));
						animalSprites[j].setIcon(sprite);
						imageAnimalAbsolutePaths.set(j, f.getAbsolutePath());
						return;
					}
				}
			});
		}
		validateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String theme;
				if (!isLoaded) {
					theme = nameTheme.getText();
					if(theme == null || theme.equals("") || theme.startsWith(" ")) {
						showStylizedErrorDialogue( "Essaye de rentrer un autre nom !", "Nom invalide !");
						return;
					}
					if(theme.length() > MAX_NAME_LENGTH) {
						showStylizedErrorDialogue( "Ce nom est trop long ! La taille maximum est de " + MAX_NAME_LENGTH + " caractères !", "Nom invalide !");
						return;
					}
				} else {
					theme = themeLoaded;
					String tmp = nameTheme.getText();
					if(!(tmp == null || tmp.equals("") || tmp.startsWith(" "))) {
						theme = tmp;
					}
				}
				if(theme.compareTo("classic") == 0) {
					showStylizedErrorDialogue( "Impossible de modifier le thème classique !", "Nom invalide !");
					return;
				}
				File f = new File(THEME_LOC + theme + "/entity.txt");
				if(f.exists()) {
					try {
						themeSaver.deleteEntityFile(f.getPath());
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				List<String> imageAnimalFinalPath = new ArrayList<String>();
				List<String> imageCharacterFinalPath = new ArrayList<String>();
				for(int i = 0; i < MAX_ENTITY; i++) {
					File image = new File(imageAnimalAbsolutePaths.get(i));
					imageAnimalFinalPath.add("/ressources_personalized/themes/" + theme + "/animals/" + image.getName());
					image = new File(imageCharacterAbsolutePaths.get(i));
					imageCharacterFinalPath.add("/ressources_personalized/themes/" + theme + "/characters/" + image.getName());
				}
				
				try {
					themeSaver.createDirectories(THEME_LOC + theme + "/animals/");
					themeSaver.createDirectories(THEME_LOC + theme + "/characters/");
				} catch (IOException e2) {
					showStylizedErrorDialogue( "Impossible de créer le theme : " + e2.getMessage(), "Erreur !");
					themeSaver.deleteTheme(theme);
					return;
				}
				for(int i = 0; i < MAX_ENTITY; i++) {
					try {
						themeSaver.copyFile(imageCharacterAbsolutePaths.get(i), "res" + imageCharacterFinalPath.get(i));
						themeSaver.copyFile(imageAnimalAbsolutePaths.get(i), "res" + imageAnimalFinalPath.get(i));
					} catch (IOException e1) {
						showStylizedErrorDialogue( "Impossible de créer le theme : " + e1.getMessage(), "Erreur !");
						themeSaver.deleteTheme(theme);
						return;
					}
				}
				
				List<String> animalNamesString = new ArrayList<String>();
				List<String> characterNamesString = new ArrayList<String>();
				for(int i = 0; i < MAX_ENTITY; i++) {
					animalNamesString.add(animalNames[i].getText());
					characterNamesString.add(characterNames[i].getText());
				}
				
				try {
					themeSaver.getEntityFile(f.getPath(), animalNamesString, characterNamesString, imageAnimalFinalPath, imageCharacterFinalPath);
				} catch (IOException e1) {
					showStylizedErrorDialogue( "Impossible de créer le theme : " + e1.getMessage(), "Erreur !");
					themeSaver.deleteTheme(theme);
				}
				showStylizedMessageDialogue("Thème " + theme + " créé avec succès !", "Bravo !");
				validateButton.setFocusPainted(false);
				getApp().setGamestate(LogikVille.OPTION_STATE);
				((OptionMenu)getApp().getMenu(LogikVille.OPTION_STATE)).resetThemeList();
				((OptionMenu)getApp().getMenu(LogikVille.OPTION_STATE)).refresh(((OptionMenu)getApp().getMenu(LogikVille.OPTION_STATE)));
			}
		});
		
		deleteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int result = showStylizedConfirmDialogue("Voulez-vous supprimer ce thème ?");
				if(result == JOptionPane.OK_OPTION) {
					String theme = (String) loadTheme.getSelectedItem();
					themeSaver.deleteTheme(THEME_LOC + theme + "/");
					loadTheme.setModel(new DefaultComboBoxModel<String>(getThemesNames()));
					((OptionMenu)getApp().getMenu(LogikVille.OPTION_STATE)).resetThemeList();
					getApp().getModel().setEntityTheme(THEME_LOC + "classic" + "/entity.txt");
				}
			}
		});
		
		confirmLoadButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				isLoaded = true;
				themeLoaded = (String) loadTheme.getSelectedItem();
				setInfoMenu(themeLoaded);
				
				loadTheme.setSelectedItem(themeLoaded);
				showStylizedMessageDialogue("Thème " + themeLoaded + " chargé avec succès !", "Succès !");
			}
		});
		
		resetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				isLoaded = false;
				setInfoMenu(LogikVilleModel.CLASSIC_THEME_NAME);
			}
		});
	}
	
	
	
}
