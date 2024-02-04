package model;

import java.util.ArrayList;
import java.util.List;

import model.constraints.Constraint;
import util.Contract;

/**
 * Classe gérant la liste des contraintes comprise dans une carte.
 * @inv
 * 		getConstraints() != null
 * 		getConstraints().size() > 0
 * 		forall c in getConstraints():
 * 			c != null
 * 
 * @cons
 * 		$ARGS$ List<Constraint> list
 * 		$PRE$
 * 			list != null
 * 			forall c in list
 * 				c != null
 * 		$POST$
 * 			getConstraints() == list
 */
public class Card {
	private final List<Constraint> constraints;
	private final int numberOfCharacters;
	private final boolean hasAnimals;
	
	public Card(List<Constraint> list, int nbChar, boolean animals) {
		Contract.checkCondition(list != null, "Not a valid list of constraints");
		for(Constraint c : list) {
			Contract.checkCondition(c != null, "Not a valid constraint");
		}
		constraints = new ArrayList<Constraint>(list);
		numberOfCharacters = nbChar;
		hasAnimals = animals;
	}
	
	// REQUETES
	
	/**
	 * Renvoie la liste des contraintes de la carte
	 */
	public List<Constraint>getConstraints() {
		return new ArrayList<Constraint>(constraints);
	}
	
	/**
	 * Renvoie le nombre de personnages de la carte.
	 */
	public int getNumberOfCharacters() {
		return numberOfCharacters;
	}
	
	/**
	 * Renvoie si la carte utilise des animaux.
	 */
	public boolean hasAnimals() {
		return hasAnimals;
	}
	/**
	 * Renvoie la contrainte d'indice i de la carte.
	 * @arg
	 * 		int i
	 * @pre
	 * 		0 <= i <= getConstraints().size()
	 */
	public Constraint getConstraint(int i) {
		Contract.checkCondition(i >= 0 && getConstraints().size() >= i, "Not a valid index");
		return constraints.get(i);
	}
}
