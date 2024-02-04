package model.save;

import java.io.IOException;
import java.util.List;

import model.House;
import model.constraints.Constraint;
import model.entity.AnimalManager;
import model.entity.CharacterManager;

/**
 * Gère les expressions régulières de niveaux.
 * Charge les données en fonction du niveau demandé.
 * 
 * @inv
 * 		Level.MIN_HOUSES <= getHouses() <= Level.MAX_HOUSES
 * 		isLoaded() ==> (0 < getConstraints() && getConstraints() != null)
 * 		isLoaded() <==> getNumberOfHouses() != 0 && getConstraints() != null
 * @cons
 * 		$ARGS$ -
 * 		$POST$ -
 */
public interface Save {
	//REQUÊTES
	
	/**
	 * Renvoie le nombre de maisons du niveau chargé.
	 * @pre
	 * 		isLoaded() == true
	 */
	int getNumberOfHouses();
	
	/**
	 * Renvoie la liste des contraintes du niveau chargé.
	 * @pre
	 * 		isLoaded() == true
	 */
	List<Constraint> getConstraints();
	
	/**
	 * Indique si le niveau comporte des animaux.
	 * @pre
	 * 		isLoaded() == true
	 */
	boolean hasAnimals();
	
	/**
	 * Indique si un niveau a été chargé.
	 */
	boolean isLoaded();
	
	/**
	 * Donne le numéro du dernier niveau enregistré dans le fichier des niveaux personnalisés
	 */
	int getNumberOfLevelPersonalized();
	
	/**
	 * Renvoie le character manager associé au niveau.
	 */
	public CharacterManager getCharacterManager();
	
	/**
	 * Renvoie le animal manager associé au niveau.
	 */
	public AnimalManager getAnimalManager();

	/**
	 * Renvoie le tableau de maisons.
	 */
	public House[] getHouses();
	
	//COMMANDES
	/**
	 * Charge les données du niveau souhaité dans le fichier des niveaux classiques.
	 * @param
	 * 		int levelNumber
	 * @pre
	 * 		0 < levelNumber <= Level.MAX_LEVEL_NUMBER
	 * @post
	 * 		isLoaded() == true
	 */
	void loadlevelClassic(String line) throws IOException;
	
	/**
	 * Charge les données du niveau souhaité dans le fichier des niveaux personnalisés.
	 * @param
	 * 		int levelNumber
	 * @pre
	 * 		0 < levelNumber <= getNumberOfLevelPersonalized()
	 * @post
	 * 		isLoaded() == true
	 */
	void loadLevelPersonalized(String line)throws IOException;
	
	/**
	 * Ferme un niveau.
	 * @pre
	 * 		isLoaded() == true
	 * @post
	 * 		isLoaded() == false
	 */
	void closeLevel();
}
