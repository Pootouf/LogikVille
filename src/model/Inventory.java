package model;

import java.util.ArrayList;
import java.util.List;

import model.entity.Animal;
import model.entity.Character;
import model.entity.Entity;
import util.Contract;

/**
 * Classe gérant l'inventaire des personnages et animaux restants en jeu.
 * @inv
 * 		charactersLeft != null
 * 		charactersUseed != null
 * 		animalsLeft != null
 * 		animalsUsed != null
 * 		charactersLeft.size() + charactersUsed.size() == characters.size()=> liste passée en constructeur
 * 		animalsLeft.size() + animalsUsed.size() == animals.size() => liste passée en constructeur
 * @cons
 * 	$ARGS$ List<Character> characters, List<Animal> animals
 * 	$PRE$ characters != null && animals != null
 * 	$POST$
 * 		getAnimalsLeft() == animals
 * 		getCharactersLeft() == characters
 *
 */
public class Inventory {
	
	//ATTRIBUTS
	private List<Character> charactersLeft;
	private List<Character> charactersUsed;
	
	private List<Animal> animalsLeft;
	private List<Animal> animalsUsed;
	
	//CONSTRUCTEUR
	public Inventory(List<Entity> characters, List<Entity> animals) {
		Contract.checkCondition(characters != null, "Characters list is null");
		Contract.checkCondition(animals != null, "Animals list is null");
		charactersLeft = new ArrayList<Character>();
		animalsLeft = new ArrayList<Animal>();
		for(Entity e : characters) {
			charactersLeft.add((Character)e);
		}
		for(Entity e : animals) {
			animalsLeft.add((Animal)e);
		}
		animalsUsed = new ArrayList<Animal>();
		charactersUsed = new ArrayList<Character>();
	}
	//REQUETES
	
	protected boolean isLeftEntity(Entity e) {
		Contract.checkCondition(e != null, "Cannot verify if entity is in a list if no entity");
		return charactersLeft.contains(e) || animalsLeft.contains(e);
	}
	
	public List<Character> getCharactersLeft() {
		return charactersLeft;
	}
	
	public List<Animal> getAnimalsLeft() {
		return animalsLeft;
	}
	
	public List<Character> getCharactersUsed() {
		return charactersUsed;
	}
	
	public List<Animal> getAnimalsUsed(){
		return animalsUsed;
	}
	
	//COMMANDES
	
	/**
	 * Ajoute une entité à l'inventaire des entités disponibles.
	 * @param E entity
	 */
	public <E extends Entity > void addEntity(E entity) {
		Contract.checkCondition(entity != null, "Cannot set, entity null or already set on a house");
		if(entity.getEntityType().equals("A")) {
			animalsUsed.remove(entity);
			animalsLeft.add((Animal)entity);
		} else {
			charactersUsed.remove(entity);
			charactersLeft.add((Character)entity);
		}
	}
	
	/**
	 * Retire une entité de l'inventaire des entités disponibles.
	 * @param E entity
	 */
	public <E extends Entity> void removeEntity(E entity) {
		Contract.checkCondition(entity != null, "Cannot remove, entity null or already set on a house");
		if(entity.getEntityType().equals("A")) {
			animalsLeft.remove(entity);
			animalsUsed.add((Animal)entity);
		} else {
			charactersLeft.remove(entity);
			charactersUsed.add((Character)entity);
		}
	}

}
