package model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

import model.entity.Animal;
import model.entity.AnimalManager;
import model.entity.Character;
import model.entity.CharacterManager;
import model.entity.Entity;
import model.save.SaveManager;
import util.Contract;
import utils.Couple;

/**
 * Classe gérant le plateau de jeu correspondant à un niveau.
 * Permet d'assigner les entités aux maisons et de charger le niveau demandé.
 * 
 *@inv
 *		MIN_HOUSES <= houses.size() <= MAX_HOUSES
 *		card != null
 *@cons
 *		$ARGS$ int levelNumber, boolean isPersonalizedLvl
 *		$PRE$
 *			MAX_LEVEL_NUMBER => levelNumber > 0
 *		$POST$
 *			
 */
public class Level {
	//CONSTANTES
	public static final int MAX_HOUSES = 5;
	public static final int MIN_HOUSES = 3;
	public static final int MAX_LEVEL_NUMBER = 84;
	public static final String PROP_ENTITYHOUSE = "entityHouse";
	
	//ATTRIBUTS
	private final List<House> houses;
	private final SaveManager sm;
	private final Inventory inventory;
	private final Solver solver;
	
	private PropertyChangeSupport pcs;
	
	//CONSTRUCTEUR
	public Level(int levelNumber, SaveManager sm) {
		Contract.checkCondition(levelNumber > 0 && levelNumber <= MAX_LEVEL_NUMBER, "Not a valid loadable level");
		Contract.checkCondition(sm != null && sm.getEntityTheme() != null, "Not a valid save manager");
		houses = new ArrayList<House>();
		initializeHouses(MAX_HOUSES);
		this.sm = sm;
		this.sm.loadlevel(levelNumber);
		inventory = new Inventory(sm.getSave().getCharacterManager().getEntityList(), 
				sm.getSave().getAnimalManager().getEntityList());
		solver = new Solver();
		solver.findSolutionsFor(getCard().getConstraints(), getCharacterManager(), getAnimalManager());
		pcs = new PropertyChangeSupport(this);
	}
	
	//REQUETES
	public Card getCard() {
		return sm.getCard();
	}
	
	public AnimalManager getAnimalManager() {
		return sm.getSave().getAnimalManager();
	}
	
	public CharacterManager getCharacterManager() {
		return sm.getSave().getCharacterManager();
	}
	
	public House getHouse(int i) {
		Contract.checkCondition(i >= 0 && i < MAX_HOUSES, "index not valid");
		return houses.get(i);
	}
	
	public List<House> getHouses() {
		return houses;
	}
	
	public synchronized boolean isLevelWon() {
		List<House> solution = solver.getSolution(0);
		for(int i = 0; i < solution.size(); i++) {
			House solutionHouse = solution.get(i);
			House levelHouse = houses.get(i);
			if(solutionHouse.getAnimal() != levelHouse.getAnimal()
					|| solutionHouse.getCharacter() != levelHouse.getCharacter()) {
				return false;
			}	
		}
		return true;
	}
	
	/**
	 * Indique si la maison passée en paramètre est pleine.
	 * Regarde si le niveau comporte des animaux ou non afin de savoir si elle est pleine.
	 * @param h
	 * @pre
	 * 		h != null
	 */
	public synchronized boolean isHouseFull(House h) {
		Contract.checkCondition(h != null, "Cannot access to a house if null");
		return h.getCharacter() != null && h.getAnimal() != null
				|| h.getCharacter() != null && !sm.getSave().hasAnimals();
	}
	
	/**
	 * Indique si la maison est vide.
	 * @param h
	 * @return
	 */
	public synchronized boolean isHouseEmpty(House h) {
		Contract.checkCondition(h != null, "Cannot access to a house if null");
		return h.isHouseEmpty();
	}
	
	public PropertyChangeListener[] getPropertyChangeListeners(String p) {
        Contract.checkCondition(p != null);

        return pcs.getPropertyChangeListeners(p);
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
	
	/**
	 * Affecte l'entité à la maison passée en paramètre.
	 * @param
	 * 		House h, E entity
	 * @pre
	 * 		h != null && !isHouseFull(h)
	 * 		entity != null && entity.getHouse() != null
	 * 		inventory.isLeftEntity(entity)
	 * @post
	 * 		entity.getHouse() == h
	 * 		h.getCharacter() == entity || h.getAnimal() == entity
	 */
	public synchronized <E extends Entity > void setEntity(E entity, House h) {
		Contract.checkCondition(h != null, "Cannot set, house null");
		Contract.checkCondition(!isHouseFull(h), "Cannot set, house full");
		Contract.checkCondition(entity != null && entity.getHouse() == null, "Cannot set, entity null or already set on a house");
		Contract.checkCondition(inventory.isLeftEntity(entity), "The entity is already used");
		if(entity.getEntityType().equals("A")){
			h.setAnimal((Animal)entity);
			
		} else {
			h.setCharacter((Character)entity);
			
		}
		House oldHouse = entity.getHouse();
		entity.setHouse(h);
		inventory.removeEntity(entity);
		pcs.firePropertyChange(PROP_ENTITYHOUSE, new Couple<Entity, House>(entity, oldHouse), new Couple<Entity, House>(entity, h));
	}
	
	/**
	 * Enlève l'entité de la maison passée en paramètre.
	 * @param entity, house
	 * @pre
	 * 		entity != null, h != null
	 * 		!inventory.isLeft(entity)
	 * @post
	 * 		!entity.isset()
	 * 		h.isHouseEmpty()
	 */
	public synchronized <E extends Entity> void removeEntity(E entity, House h) {
		Contract.checkCondition(h != null, "Cannot remove, house null");
		Contract.checkCondition(entity != null, "Cannot remove, entity null");
		Contract.checkCondition(!inventory.isLeftEntity(entity), "If entity is not used, no need to remove it");
		if(entity.getEntityType().equals("A")){
			h.setAnimal(null);
		} else {
			h.setCharacter(null);
		}
		entity.setHouse(null);
		inventory.addEntity(entity);
		pcs.firePropertyChange(PROP_ENTITYHOUSE, new Couple<Entity, House>(entity, h), new Couple<Entity, House>(entity, null));
	}
	
	/**
	 * Echange les entités de 2 maisons et les maisons de 2 entités.
	 * @param E entity1, E entity2, House h1, House h2
	 *  @pre
	 * 		entity1 != null && entity2 != null
	 * 		h1 != null && h2 != null
	 * 		!inventory.isLeftEntity(entity1) && !inventory.isLeftEntity(entity2)
	 * @post
	 * 		entity1.getHouse() == h2
	 * 		h2.getCharacter() == entity1 || h2.getAnimal() == entity1
	 * 		entity2.getHouse() == h1
	 * 		h1.getCharacter() == entity2 || h1.getAnimal() == entity2
	 */
	public synchronized <E extends Entity> void exchangeEntities(E entity1, House h1, E entity2, House h2) {
		Contract.checkCondition(h1 != null && h2 != null, "Cannot exchange, house null");
		Contract.checkCondition(entity1 != null && entity2 != null, "Cannot exchange, entity null");
		Contract.checkCondition(!inventory.isLeftEntity(entity1) 
				&& !inventory.isLeftEntity(entity2), "One or the two entities are not set");
		removeEntity(entity1, h1);
		removeEntity(entity2, h2);
		setEntity(entity1, h2);
		setEntity(entity2, h1);
	}
	
	/**
	 * Relance le niveau avec les données initiales.
	 * @pre
	 * 		sm.getSave() != null && sm.getSave().isLoaded()
	 * @post
	 * 		forall h in getHouses():
	 * 			h.getAnimal() == null
	 * 			h.getCharacter() == null
	 */
	public synchronized void reset() {
		Contract.checkCondition(sm.getSave() != null && sm.getSave().isLoaded(), "Cannot reset what's not loaded");
		for(House h : houses) {
			h.setAnimal(null);
			h.setCharacter(null);
		}
		List<Entity> list = sm.getSave().getCharacterManager().getEntityList();
		for(Entity e : list) {
			House oldHouse = e.getHouse();
			e.setHouse(null);
			inventory.addEntity(e);
			pcs.firePropertyChange(PROP_ENTITYHOUSE, new Couple<Entity, House>(e, oldHouse), new Couple<Entity, House>(e, null));
		}
		list = sm.getSave().getAnimalManager().getEntityList();
		for(Entity e : list) {
			House oldHouse = e.getHouse();
			e.setHouse(null);
			inventory.addEntity(e);
			pcs.firePropertyChange(PROP_ENTITYHOUSE, new Couple<Entity, House>(e, oldHouse), new Couple<Entity, House>(e, null));
		}
	}
	
	//OUTILS
	private void initializeHouses(int numberOfHouses) {
		Contract.checkCondition(MIN_HOUSES <= numberOfHouses && numberOfHouses <= MAX_HOUSES, 
				"Cannot initialize a correct amount of houses");
		for(int i = 0; i < numberOfHouses ; i++){
            House h = new StdHouse(i, numberOfHouses);
            houses.add(h);
		}
	}
}
