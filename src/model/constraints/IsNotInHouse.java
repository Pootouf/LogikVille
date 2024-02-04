package model.constraints;

import model.House;
import model.entity.Entity;
import util.Contract;

/**
 * Deuxième type de contrainte pouvant être rencontré dans les cartes.
 * Impose la maison dans laquelle une entité n'habite pas.
 * @inv <pre>
 * 		getNumberOfHouses() > indice >= 0
 * 		getEntity() != null
 * </pre>
 * @cons <pre>
 * 		$ARGS$
 * 			Maison[] maisons
 * 			Entity e
 * 			int indice
 * 		$PRE$
 * 			maisons.length > 0
 * 			e != null
 * 			getNumberOfHouses() > indice >= 0
 * 		$POST$
 * 			getEntity() == e
 * 			getIndice() == indice
 * 			getHouses() == maisons
 */
public class IsNotInHouse<E extends Entity> extends Constraint {
	private Entity entity;
	private int indice;
	
	//CONSTRUCTEUR
	public IsNotInHouse(House[] maisons, Entity e, int indice) {
		super(maisons);
		Contract.checkCondition(e != null, "Not a valid entity");
		Contract.checkCondition(getNumberOfHouses() > indice && indice >= 0, "Invalid integer for indice");
		entity = e;
		this.indice = indice;
	}
	
	//REQUETES
	
	/**
	 * Renvoie l'entité.
	 */
	public Entity getEntity() {
		return entity;
	}
	
	/**
	 * Renvoie l'indice de la maison.
	 */
	public int getIndice() {
		return indice;
	}
	
	@Override
	public boolean isVerified() {
		return getEntity().getHouse().getIndex() != getIndice();
	}
	
	@Override
	public int getId() {
		return 2;
	}
}
