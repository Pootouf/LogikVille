package main;


import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import model.LogikVilleModel;
import util.Contract;
import view.gui.AbstractMenu;
import view.gui.CreatorThemeMenu;
import view.gui.EditorMenu;
import view.gui.LevelMenu;
import view.gui.OptionMenu;
import view.gui.PersonalizedLevelMenu;
import view.gui.PlayMenu;
import view.gui.TitleMenu;
import view.gui.VictoryMenu;
import javax.swing.ImageIcon;
public class LogikVille {
	
	//CONSTANTES DE CLASSE
	
	//SYSTEM SETTING
	public static int FPS = 200;
	
	//STATE_SETTING
	public static final int NUMBER_OF_STATE = 8;
	public static final int TITLE_STATE = 0;
	public static final int LEVEL_STATE = 1;
	public static final int USERLEVEL_STATE = 2;
	public static final int PLAY_STATE = 3;
	public static final int EDITOR_STATE = 4;
	public static final int VICTORY_STATE = 5;
	public static final int OPTION_STATE = 6;
	public static final int THEME_STATE = 7;
		
	
	//ATTRIBUTS
	
	//SCREEN_SETTING
	//Taille de l'écran en PIXEL
	private int screenPixelX;
	private int screenPixelY;
	
	//AFFICHAGE
	private JFrame mainFrame;
	
	private Map<Integer, AbstractMenu> menuMap;
	
	
	//SYSTEM
	@SuppressWarnings("unused")
	private int fps = FPS;
	private int gameState;
	private boolean fullscreen = true;
	private String selectedTheme = LogikVilleModel.CLASSIC_THEME_NAME;
	private Dimension selectedResolution = OptionMenu.DEFAULT_RESOLUTION;
	
	//MODEL
	private LogikVilleModel model;
	
	
	
	//CONSTRUCTEURS
	public LogikVille() {
		gameState = TITLE_STATE;
		createModel();
		createView();
		placeComponents();
		createController();
	}
	
	
	
	
	
	//REQUETES
	
	public LogikVilleModel getModel() {
		return model;
	}
	
	public JFrame getFrame() {
		return mainFrame;
	}
	
	public int getGameState() {
		return gameState;
	}
	
	public int getScreenPixelX() {
		return screenPixelX;
	}
	
	public int getScreenPixelY() {
		return screenPixelY;
	}
	
	public AbstractMenu getMenu(int gameState) {
		return menuMap.get(gameState);
	}
	
	public String getSelectedTheme() {
		return selectedTheme;
	}
	
	public boolean isFullScreenActivated() {
		return fullscreen;
	}
	
	public Dimension getSelectedResolution() {
		return selectedResolution;
	}
	
	//COMMANDES
	
	public void setSelectedTheme(String s) {
		selectedTheme = s;
	}
	
	public void setSelectedResolution(Dimension d) {
		selectedResolution = d;
	}
	
	public void display() {
		mainFrame.pack();
		mainFrame.setLocationRelativeTo(null);
		refresh();
		mainFrame.setVisible(true);
	}
	
	public void setGamestate(int state) {
		Contract.checkCondition(0 <= state && state < NUMBER_OF_STATE, "Invalid State");
		gameState = state;
		mainFrame.setContentPane(menuMap.get(state));
		mainFrame.pack();
	}
	
	public void setFps(int f) {
		fps = f;
	}
	
	public void setFullscreen(boolean b) {
		fullscreen = b;
	}
	
	public void setFrameSize(Dimension d) {
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		screenPixelX = d.width;
		screenPixelY = d.height;
		if(screenPixelX == -1) {
			screenPixelX = screen.width;
		}
		if(screenPixelY == -1) {
			screenPixelY = screen.height;
		}
	}
	
	public void resetFrame() {
		createMenu();
		setGamestate(gameState);
		display();
	}
	
	
	//OUTILS
	private void createModel() {
		model = new LogikVilleModel();
	}
	
	private void createView( ) {
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		if(screenPixelX == 0) {
			screenPixelX = screen.width;
		}
		if(screenPixelY == 0) {
			screenPixelY = screen.height;
		}
		createMenu();
	}
	
	private void createMenu() {
		if(mainFrame != null) {
			mainFrame.dispose();
		}
		mainFrame = new JFrame();
		if(fullscreen) {
			mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
			mainFrame.setUndecorated(true);
		}
		mainFrame.pack();
		mainFrame.setPreferredSize(new Dimension(screenPixelX, screenPixelY + mainFrame.getInsets().top));
		mainFrame.setResizable(false);
		mainFrame.setTitle("LogikVille");
		mainFrame.setIconImage(new ImageIcon("res/ressources_personalized/sprites/gui/nothing.png").getImage());
		
		AbstractMenu titleMenu = new TitleMenu(this);
		AbstractMenu levelMenu = new LevelMenu(this);
		AbstractMenu personalizedLevelMenu = new PersonalizedLevelMenu(this);
		AbstractMenu playMenu = new PlayMenu(this);
		AbstractMenu editorMenu = new EditorMenu(this);
		AbstractMenu victoryMenu = new VictoryMenu(this);
		AbstractMenu optionMenu = new OptionMenu(this);
		AbstractMenu themeMenu = new CreatorThemeMenu(this);
		
		menuMap = new HashMap<Integer, AbstractMenu>();
		menuMap.put(TITLE_STATE, titleMenu);
		menuMap.put(LEVEL_STATE, levelMenu);
		menuMap.put(USERLEVEL_STATE, personalizedLevelMenu);
		menuMap.put(PLAY_STATE, playMenu);
		menuMap.put(EDITOR_STATE, editorMenu);
		menuMap.put(VICTORY_STATE, victoryMenu);
		menuMap.put(OPTION_STATE, optionMenu);
		menuMap.put(THEME_STATE, themeMenu);
	}
	
	private void placeComponents() {
		mainFrame.setContentPane(menuMap.get(gameState));
	}
	
	
	
	private void createController() {
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setResizable(false);
	}
	
	
	private void refresh() {
		mainFrame.repaint();
	}
	
	
	
	
	
	
	//POINT D'ENTREE
  	public static void main(String[] args) {
  		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				LogikVille game = new LogikVille();
		  		game.display();
		  		new Thread(new Runnable() {
		  			public void run() {
		  				double drawInterval = 1000000000/FPS;
		  				double delta = 0;
		  				long lastTime = System.nanoTime();
		  				long currentTime;
		  				int drawCount = 0;
		  				int timer = 0;
		  				while(this != null) {
		  					currentTime = System.nanoTime();
		  					delta += (currentTime - lastTime)/drawInterval;
		  					timer += (currentTime - lastTime);
		  					lastTime = currentTime;
		  					if(delta >= 1) {
		  						game.refresh();
		  						delta--;
		  						drawCount++;
		  					}
		  					if(timer > 1000000000) {
		  						game.setFps(drawCount);
		  						timer = 0;
		  						drawCount = 0;
		  					}
		  				}
		  			}
		  		}).start();
			}
  		});
  	}
  	
  	public static void quit() {
  		System.exit(0);
  	}
}
