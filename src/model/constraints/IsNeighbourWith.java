package model.constraints;

import model.House;
import model.entity.Entity;
import util.Contract;

/**
 * Troisième type de contrainte pouvant être rencontré dans les cartes.
 * Impose que l'entité E et F soient voisines.
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

public class IsNeighbourWith<E extends Entity, F extends Entity> extends Constraint {
	
	// ATTRIBUTS
	
	private Entity firstEntity;
	
	private Entity secondEntity;
	
	// CONSTRUCTEUR
	
	public IsNeighbourWith(House[] maisons, Entity e, Entity f) {
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
		return firstEntity.getHouse().getIndex() == secondEntity.getHouse().getIndex() + 1 
				|| firstEntity.getHouse().getIndex() == secondEntity.getHouse().getIndex() - 1;
	}

	@Override
	public int getId() {
		return 3;
	}
}
