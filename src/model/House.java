package model;
import model.entity.Animal;
import model.entity.Character;
/**
 * Gestion des entités présentes dans la maison.
 * 
 * @inv <pre>
 * 		0 <= getCharacter() <= 1
 * 		0 <= getAnimal() <= 1
 * 		isHouseEmpty() ==> getAnimal() == null && getCharacter() == null
 * 		0 <= getIndex() <= Level.MAX_HOUSES
 * </pre>
 * @cons <pre>
 * 		$ARGS$ int index
 * 		$PRE$ index >= 0  && index < numberOfHouses
 * 		$POST$
 * 			getIndex() == index
 * </pre>
 */
public interface House {
	
	//REQUETES
	
	/**
	 * Le personnage présent dans la maison.
	 */
	Character getCharacter();
	
	/**
	 * L'animal présent dans la maison.
	 */
	Animal getAnimal();
	
	/**
	 * Indique si la maison est vide.
	 */
	boolean isHouseEmpty();
	
	/**
	 * Renvoie l'indice représentant la maison sur le plateau.
	 */
	int getIndex();
	
	//COMMANDES
	/**
	 * Ajoute un personnage à la maison.
	 * setCharacter(null) permet de retirer tout personnage de la maison.
	 * @param <pre>
	 * 		Personnage p
	 * </pre>
	 * @post <pre>
	 * 		p == null ==> getCharacter() == null
	 * 		p != null ==> getCharacter() == p
	 * </pre>
	 */
	void setCharacter(Character p);
	
	/**
	 * Ajoute un animal à la maison.
	 * setAnimal(null) permet de retirer tout animal de la maison.
	 * @param <pre>
	 * 		Animal a
	 * </pre>
	 * @post <pre>
	 * 		a == null ==> getAnimal() == null
	 * 		a != null ==> getAnimal() == a
	 * </pre>
	 */
	void setAnimal(Animal a);

	House clone();
}
