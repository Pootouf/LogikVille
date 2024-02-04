package model.constraints;

import model.House;
import model.entity.Animal;
import model.entity.Character;
import util.Contract;

/**
 * Sixième type de contrainte pouvant être rencontré dans les cartes.
 * Impose que le personnage C soit dans la même maison que l'animal A.
 * 
 * @inv <pre>
 * 		getCharacter() != null && getAnimal() != null
 * </pre>
 * 
 * @cons <pre>
 * 		$ARGS$
 * 			Maison maison[]
 * 			Character c
 * 			Animal a
 *		$PRE$
 *			maisons.length > 0
 * 			c != null && a != null
 *		$POST$
 *			getCharacter() == c
 *			getAnimal() == a
 *			getHouses() == maisons
 * 
 * </pre>
 */

public class IsWith<C extends Character, A extends Animal> extends Constraint {
	
	// ATTRIBUTS
	
	private Character character;
	
	private Animal animal;
	
	// CONSTRUCTEUR
	
	public IsWith(House[] maisons, Character c, Animal f) {
		super(maisons);
		Contract.checkCondition(c != null && f != null, "One entity is null");
		
		character = c;
		animal = f;
	}
	
	// REQUETES
	
	/**
	 * Renvoie le personnage.
	 */
	public Character getCharacter() {
		return character;
	}
	

	/**
	 * Renvoie l'animal.
	 */
	public Animal getAnimal() {
		return animal;
	}
	
	@Override
	public boolean isVerified() {
		return getCharacter().getHouse().getIndex() == getAnimal().getHouse().getIndex();
	}
	
	@Override
	public int getId() {
		return 6;
	}
}
