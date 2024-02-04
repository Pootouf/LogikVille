package model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.constraints.Constraint;
import model.entity.AnimalManager;
import model.entity.CharacterManager;
import model.entity.Entity;
import model.save.SaveManager;
import util.Contract;

/**
 * Classe s'occupant de l'édition d'un niveau et plus particulièrement d'une carte.
 * 
 * @inv
 * 		constraintsChosen != null
 * 		isValidable ==>
 * 			Level.MIN_HOUSES <= numberOfHouses <= Level.MAX_HOUSES
 * 			&& constraintsChosen.size() > 0
 * 			&& solver.numberOfSolutions() == 1
 * 
 * @cons
 * 		$ARGS$ String entityTheme
 * 		$PRE$ entityTheme != null
 * 		$POST$
 * 			constraintsChosen != null
 *
 */
public class Editor {
	//CONSTANTE
	public static final String PROP_SOLVE = "solve";
	//ATTRIBUTS
	private final List<Constraint> constraintsChosen;
	private House[] houses;
	private boolean hasAnimals;
	private Solver solver;
	private String image;
	private String name;
	
	private String entityTheme;
	private AnimalManager animalsAvailable;
	private CharacterManager charactersAvailable;
	
	private final List<Constraint> constraintsUndone;
	
	private PropertyChangeSupport pcs;
	
	//CONSTRUCTEUR
	public Editor(String entityTheme) {
		Contract.checkCondition(entityTheme != null, "Not a valid entity theme file");
		this.entityTheme = entityTheme;
		constraintsChosen = new ArrayList<Constraint>();
		constraintsUndone = new ArrayList<Constraint>();
		solver = new Solver();
		pcs = new PropertyChangeSupport(this);
	}
	
	//REQUETES
	
	/**
	 * Renvoie la string représentant l'image choisie pour le niveau.
	 */
	public String getImage() {
		return image;
	}
	
	/**
	 * Renvoie la string représentant le nom donné par l'utilisateur au niveau.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Renvoie le personnage à l'indice donné parmi les personnages disponibles.
	 */
	public Entity getCharacter(int index) {
		Contract.checkCondition(index >= 0 && index < charactersAvailable.getEntityList().size(), "Not a valid index for entity");
		return charactersAvailable.getEntity(index);
	}
	
	/**
	 * Renvoie l'animal à l'indice donné parmi les personnages disponibles.
	 */
	public Entity getAnimal(int index) {
		Contract.checkCondition(index >= 0 && index < animalsAvailable.getEntityList().size(), "Not a valid index for entity");
		return animalsAvailable.getEntity(index);
	}
	
	/**
	 * Indique si le niveau en cours de création est validable: si toutes ses données
	 * sont initialisées et qu'il ne possède qu'une solution.
	 */
	public boolean isValidable() {
		return houses.length >= Level.MIN_HOUSES && houses.length <= Level.MAX_HOUSES
				&& constraintsChosen.size() > 0
				&& solver.numberOfSolutions() == 1;
	}
	
	/**
	 * Renvoie la liste des conrtaintes choisies.
	 */
	public List<Constraint> getConstraintsChosen() {
		return new ArrayList<Constraint>(constraintsChosen);
	}
	
	/**
	 * Renvoie la liste des contraintes enlevées.
	 */
	public List<Constraint> getConstraintsUndone() {
		return new ArrayList<Constraint>(constraintsUndone);
	}
	
	/**
	 * Renvoie la liste des solutions trouvées par le solveur pour la liste des contraintes
	 * choisies.
	 */
	public List<List<House>> getSolutions() {
		return solver.getSolutions();
	}
	
	
	/**
	 * Renvoie la liste des noms des animaux disponibles pour le niveau édité.
	 * @pre
	 * 		animalsAvailable != null
	 */
	public List<String> getAnimalsNameList() {
		Contract.checkCondition(animalsAvailable != null, "Cannot create a list from an animal manager which is null");
		return animalsAvailable.getEntityNameList();
	}
	
	/**
	 * Renvoie la liste des noms des characters disponibles pour le niveau édité.
	 * @pre
	 * 		charactersAvailable != null
	 */
	public List<String> getCharactersNameList() {
		Contract.checkCondition(charactersAvailable != null, "Cannot create a list from a character manager which is null");
		return charactersAvailable.getEntityNameList();
	}
	
	/**
	 * Renvoie la liste des maisons. 
	 */
	public House[] getHouses() {
		return houses;
	}

	
	public PropertyChangeListener[] getPropertyChangeListeners(String p) {
        Contract.checkCondition(p != null);

        return pcs.getPropertyChangeListeners(p);
    }
	//COMMANDES
	
	public void setImage(String path) {
		image = path;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setEntityTheme(String entityTheme) {
		Contract.checkCondition(entityTheme != null, "Invalid theme");
		this.entityTheme = entityTheme;
	}
	
	/**
	 * Calcule le nombre de solutions actuellement obtenables grâce au solver.
	 * @pre
	 * 		animalsAvailable != null
	 * 		charactersAvailable != null
	 */
	public void calculateSolutionsSolver() {
		Contract.checkCondition(charactersAvailable != null, "Cannot calculate from a a character manager which is null");
		Contract.checkCondition(animalsAvailable != null, "Cannot calculate from an animal manager which is null");
		List<List<House>> oldSolutions = getSolutions();
		solver.findSolutionsFor(new ArrayList<Constraint>(constraintsChosen), charactersAvailable, animalsAvailable);
		pcs.firePropertyChange(PROP_SOLVE, oldSolutions, getSolutions());
	}
	
	public void addPropertyChangeListener(String pName, PropertyChangeListener lnr) {
	    Contract.checkCondition(pName != null && lnr != null);

	    pcs.addPropertyChangeListener(pName, lnr);
	}
	
	public void removePropertyChangeListener(String pName, PropertyChangeListener lnr) {
	    Contract.checkCondition(pName != null && lnr != null);

	    pcs.removePropertyChangeListener(pName, lnr);
	}
	
	/**
	 * Attribue un nombre de maisons au nouveau niveau.
	 * @pre
	 * 		nbHouses => Level.MIN_HOUSES && nbHouses <= Level.MAX_HOUSES
	 * @post
	 * 		numberOfHouses == nbHouses
	 */
	public void setNumberOfHouses(int nbHouses) {
		Contract.checkCondition( nbHouses <= Level.MAX_HOUSES && nbHouses >= Level.MIN_HOUSES, "It's not a valid number of houses for a new level");
		houses = new House[nbHouses];
		for(int i = 0; i < nbHouses ; i++) {
			houses[i] = new StdHouse(i, nbHouses);
		};
		charactersAvailable = new CharacterManager(entityTheme, nbHouses, nbHouses);
	}
	
	/**
	 * Indique si le nouveau niveau doit comporter des animaux ou non.
	 * @post
	 * 		hasAnimals == hasLevelAnimals
	 */
	public void setHasAnimals(boolean hasLevelAnimals) {
		hasAnimals = hasLevelAnimals;
		if(hasAnimals) {
			animalsAvailable = new AnimalManager(entityTheme, houses.length, houses.length);
		} else {
			animalsAvailable = new AnimalManager(entityTheme, 0, houses.length);
		}
	}
	
	/**
	 * Ajoute une contrainte à la liste des contraintes choisies.
	 * @pre
	 * 		Constraint c != null
	 * @post
	 * 		|constraintsChosen| = |old constraintsChosen| + 1
	 */
	public void addConstraintChosen(Constraint c) {
		Contract.checkCondition(c != null, "Cannot add this constraint, there is no constraint");
		constraintsChosen.add(c);
		constraintsUndone.clear();
		calculateSolutionsSolver();
	}
	
	/**
	 * Retire une contrainte de la liste des contraintes choisies.
	 * @pre
	 * 		c != null
	 * 		constraintsChosen.constains(c)
	 * @post
	 * 		|constraintsChosen| = |old constraintsChosen| - 1
	 */
	public void removeConstraintsChosen(Constraint c) {
		Contract.checkCondition(c != null && constraintsChosen.contains(c), "Cannot remove a constraint that is inexistant");
		constraintsChosen.remove(c);
		calculateSolutionsSolver();
	}
	
	/**
	 * Une fois le nouveau niveau choisi par l'utilisateur, crée le niveau correspondant.
	 * Ajoute le niveau au fichier de sauvegarde.
	 * @throws IOException 
	 * @pre
	 * 		numberOfHouses >= MIN_HOUSES
	 * 		constraintsChosen.size() != 0
	 * @post
	 * 		Le niveau est ajouté au fichier de sauvegarde des niveaux personnalisés.
	 */
	public void createNewLevel() throws IOException {
		
		Contract.checkCondition(isValidable(), "Cannot create the level, it's not validable");
		SaveManager sm = new SaveManager(true);
		sm.addLevel(constraintsChosen, houses.length, hasAnimals, image, name);
	}
	
	/**
	 * Annule la dernière action effectuée.
	 *
	 */
	public void undo() {
		constraintsUndone.add(constraintsChosen.get(constraintsChosen.size()-1));
		removeConstraintsChosen(constraintsChosen.get(constraintsChosen.size()-1));
	}
	
	/**
	 * Refait la dernière action annulée.
	 */
	public void redo() {
		constraintsChosen.add(constraintsUndone.get(constraintsUndone.size()-1));
		constraintsUndone.remove(constraintsUndone.size()-1);
		calculateSolutionsSolver();
	}
	
	/**
	 * Recommence la création du niveau de 0.
	 */
	public void reset() {
		constraintsChosen.clear();
		constraintsUndone.clear();
		houses = null;
		hasAnimals = false;
		animalsAvailable = null;
		charactersAvailable = null;
		solver = new Solver();
	}
}
