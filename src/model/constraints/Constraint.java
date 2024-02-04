package model.constraints;
import model.House;
import util.Contract;
/**
 * Classe mère des différents types de contraintes.
 * Gère les différents types de contraintes
 * et permet de savoir si une contrainte est vérifiée.
 * 
 * @inv <pre>
 * 		getHouses().length == numberOfHouses()
 * 		getHouses().length > 0
 * </pre>
 * @cons <pre>
 * 		$ARGS$ maisons
 * 		$PRE$
 * 			maisons.length > 0
 * 		$POST$
 * 			getHouses() == maisons
 * </pre>
 */
public abstract class Constraint {
	private House[] houses;
	
	Constraint(House[] maisons) {
		Contract.checkCondition(maisons.length > 0, "Not a valid list of houses");
		houses = maisons;
	}
	
	//REQUETES
	
	/*
	 * Renvoie la liste des maisons.
	 */
	public House[] getHouses() {
		return houses;
	}
	
	/*
	 * Renvoie la maison d'indice passé en paramètre.
	 * @param :
	 * 		int i
	 * @pre :
	 * 		i >= 0 && i <= getNumberOfHouses() -1
	 */
	public House getHouse(int i) {
		Contract.checkCondition(i >= 0 && i <= getNumberOfHouses() - 1, "Invalid index");
		return houses[i];
	}
	/*
	 * Indique le nombre de maisons.
	 */
	public int getNumberOfHouses() {
		return houses.length;
	}

	/**
	 * Vérifie si une contrainte est remplie.
	 */
	public abstract boolean isVerified();
	
	/**
	 * Renvoie le numéro de contrainte.
	 */
	public abstract int getId();
	
	// COMMANDES
	
	/**
	 * Remplace le tableau de maisons de la contrainte.
	 * 
	 * @pre 
	 * 	<pre>houses != null </pre>
	 * 
	 * @post
	 * 	<pre>getHouses() == houses </pre>
	 * 
	 */
	public void setHouses(House[] h) {
		Contract.checkCondition(h != null, "array not valid");
		houses = h;
	} 
}	
