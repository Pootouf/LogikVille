package model.constraints;

import model.House;
import model.entity.Entity;
import util.Contract;

/**
 * Cinquième type de contrainte pouvant être rencontré dans les cartes.
 * Impose que l'entité E soit toujours à gauche de l'entité F.
 * Elles ne sont cependant pas toujours voisines.
 * 
 * @inv <pre>
 * 		getFirstEntity() != null && getSecondEntity() != null
 * </pre>
 * 
 * @cons <pre>
 * 		$ARGS$
 * 			Maison maison[]
 * 			Entity e1, e2
 *		$PRE$
 *			maisons.length > 0
 * 			e1 != null && e2 != null
 *		$POST$
 *			getFirstEntity() == e1
 *			getSecondEntity() == e2
 *			getHouses() == maisons
 * 
 * </pre>
 */

public class ToTheLeftOf<E extends Entity, F extends Entity> extends Constraint {
	
	// ATTRIBUTS
	
	private Entity firstEntity;
	
	private Entity secondEntity;
	
	// CONSTRUCTEUR
	
	public ToTheLeftOf(House[] maisons, Entity e, Entity f) {
		super(maisons);
		Contract.checkCondition(e != null && f != null, "One entity is null");
		
		firstEntity = e;
		secondEntity = f;
	}
	
	// REQUETES
	
	/**
	 * Renvoie la première entité.
	 */
	public Entity getFirstEntity() {
		return firstEntity;
	}
	

	/**
	 * Renvoie la seconde entité.
	 */
	public Entity getSecondEntity() {
		return secondEntity;
	}
	
	@Override
	public boolean isVerified() {
		return firstEntity.getHouse().getIndex() < secondEntity.getHouse().getIndex();
	}
	
	@Override
	public int getId() {
		return 5;
	}
}
