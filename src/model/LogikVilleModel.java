package model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;

import model.save.SaveManager;
import util.Contract;

public class LogikVilleModel {
	//CONSTANTES
	public static final String CLASSIC_THEME_NAME = "classic";
	public static final String CLASSIC_THEME_ENTITY  = "res/ressources_personalized/themes/" + CLASSIC_THEME_NAME + "/entity.txt";
	public static final String PROP_LOAD_CLASSIC = "loadClassic";
	public static final String PROP_LOAD_PERSONALIZED = "loadPersonalized";
	public static final String PROP_DELETE = "delete";
	public static final String PROP_DELETE_ERROR = "deleteError";
	public final static Object lock = new Object();
	
	//ATTRIBUTS	
	private final SaveManager classicSave;
	private SaveManager personalizedSave;
	private String entityTheme;
	
	private Level currentLevel;
	private Editor editableLevel;
	
	private PropertyChangeSupport pcs;
	
	//CONSTRUCTEUR
	public LogikVilleModel() {
		classicSave = new SaveManager(false);
		personalizedSave = new SaveManager(true);
		this.entityTheme = CLASSIC_THEME_ENTITY;
		createEditableLevel();
		setEntityTheme(entityTheme);
		pcs = new PropertyChangeSupport(this);
	}
	
	//REQUETES
	public String getEntityTheme() {
		return entityTheme;
	}
	
	public Level getCurrentLevel() {
		return currentLevel;
	}
	
	public Editor getEditableLevel() {
		return editableLevel;
	}
	
	public PropertyChangeListener[] getPropertyChangeListeners(String p) {
        Contract.checkCondition(p != null);

        return pcs.getPropertyChangeListeners(p);
    }
	
	public PropertyChangeListener[] getPropertyChangeListeners() {
		return pcs.getPropertyChangeListeners();
	}
	
	public int getClassicLevelNumber() {
		return classicSave.getNumberOfLevels();
	}
	
	public int getPersonalizedLevelNumber() {
		return personalizedSave.getNumberOfLevels();
	}
	
	public String getImagePathPersonalized(int level) {
		return personalizedSave.getLevelId().get(level).getElement1();
	}
	
	public String getNamePersonalized(int level) {
		return personalizedSave.getLevelId().get(level).getElement2();
	}
	
	public int getLevelNumberFromName(String name) {
		return personalizedSave.getLevelNumber(name);
	}
	
	//COMMANDES
	
	public void addPropertyChangeListener(String pName, PropertyChangeListener lnr) {
	    Contract.checkCondition(pName != null && lnr != null);

	    pcs.addPropertyChangeListener(pName, lnr);
	}
	
	public void removePropertyChangeListener(String pName, PropertyChangeListener lnr) {
	    Contract.checkCondition(pName != null && lnr != null);

	    pcs.removePropertyChangeListener(pName, lnr);
	}
	
	public void removePropertyChangeListener(PropertyChangeListener pcl) {
		pcs.removePropertyChangeListener(pcl);
	}
	
	public synchronized void reloadPersonalizedManager() {
		personalizedSave = new SaveManager(true);
		personalizedSave.setEntityTheme(entityTheme);
	}
	/**
	 * Change le thème des entités selon le paramètre donné.
	 * @param entityTheme
	 * @pre entityTheme != null
	 */
	public void setEntityTheme(String entityTheme) {
		Contract.checkCondition(entityTheme != null, "Not a valid entity theme path");
		this.entityTheme = entityTheme;
		classicSave.setEntityTheme(entityTheme);
		personalizedSave.setEntityTheme(entityTheme);
		editableLevel.setEntityTheme(entityTheme);
	}
	
	/**
	 * Ouvre le niveau classique désigné par le numéro passé en paramètre.
	 * @param int levelNumber
	 * @pre levelNumber > 0 && levelNumber <= Level.MAX_LEVEL_NUMBER
	 */
	public void openLevelClassic(int levelNumber) {
		Contract.checkCondition(1 <= levelNumber && levelNumber <= Level.MAX_LEVEL_NUMBER, "Cannot open a level that doesn't exist");
		new Thread(new Runnable() {

			@Override
			public void run() {
				currentLevel = new Level(levelNumber, classicSave);
				pcs.firePropertyChange(PROP_LOAD_CLASSIC, null, getCurrentLevel());
			}
			
		}).start();
	}
	
	/**
	 * Ouvre le niveau personnalisé désigné par le numéro passé en paramètre.
	 * @param int levelNumber
	 * @pre levelNumber > 0 && levelNumber <= Level.MAX_LEVEL_NUMBER
	 */
	public void openLevelPersonalized(int levelNumber) {
		Contract.checkCondition(1 <= levelNumber && levelNumber <= Level.MAX_LEVEL_NUMBER, "Cannot open a level that doesn't exist");
		new Thread(new Runnable() {
			@Override
			public void run() {
				currentLevel = new Level(levelNumber, personalizedSave);
				pcs.firePropertyChange(PROP_LOAD_PERSONALIZED, null, getCurrentLevel());
			}
		}).start();
	}
	
	/**
	 * Supprimer un niveau personnalisé désigné par son noméro passé en paramètre.
	 * @pre
	 * 		levelNumber > 0 && levelNumber <= Level.MAX_LEVEl_NUMBER
	 */
	public void deleteLevelPersonalized(int levelNumber) {
		Contract.checkCondition(1 <= levelNumber && levelNumber <= Level.MAX_LEVEL_NUMBER, "Cannot delete a level that doesn't exist");
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					personalizedSave.deleteLevel(levelNumber);
				} catch (IOException e) {
					pcs.firePropertyChange(PROP_DELETE_ERROR, null, false);
					return;
				}
				pcs.firePropertyChange(PROP_DELETE, null, true);
			}
		}).start();
	}
	
	/**
	 * Crée un niveau éditable
	 * @pre
	 * 		getEntityTheme() != null
	 * @post
	 * 		getEditableLevel() != null
	 */
	public void createEditableLevel() {
		editableLevel = new Editor(getEntityTheme());
	}
	
	/**
	 * Ferme le niveau courant.
	 */
	public void closeLevel() {
		currentLevel = null;
	}

	
}
