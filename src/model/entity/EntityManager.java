package model.entity;

import java.util.ArrayList;
import java.util.List;

import util.Contract;

/**
 * Classe gérant les entités à charger afin de donner une liste d'entités disponibles.
 * 
 * @inv <pre>
 * 		
 *</pre>
 */
public abstract class EntityManager {
	
	// ATTRIBUTS
	
	private List<Entity> entityList;
	
	// REQUETES
	
	/**
	 * Renvoie la liste des noms des entités disponibles.
	 */
	public List<String> getEntityNameList() {
		List<String>result = new ArrayList<String>();
		for(Entity e : entityList) {
			result.add(e.getRole());
		}
		return result;
	}
	/**
	 * Renvoie la liste des entités disponibles
	 */
	public List<Entity> getEntityList() {
		return entityList;
	}
	
	/**
	 * Renvoie l'entité à l'indice demandé dans la liste.
	 * @param
	 * 		int i
	 * @pre
	 * 		getEntityList.size() > i >= 0
	 */
	public Entity getEntity(int i) {
		Contract.checkCondition(i >= 0 && i < getEntityList().size(), i + " is Not a valid index in the entity list");
		return getEntityList().get(i);
	}
	
	// COMMANDE
	
	/**
	 * Modifie la liste d'entités disponibles.
	 * 
	 * @pre : <pre>
	 * 		list != null ==>
	 * 			forall e in list :
	 * 				e != null
	 * </pre>
	 * @post : <pre>
	 * 		list != null ==>
	 * 			getEntityList() == list
	 * 		list == null ==>
	 * 			getEntityList().isEmpty()
	 * </pre>
	 */
	protected void setEntityList(List<Entity> list) {
		if(list != null) {
			for(Entity e : list) {
				Contract.checkCondition(e != null, "List contains null entity");
			}
			entityList = new ArrayList<Entity>(list);
		} else {
			entityList = new ArrayList<Entity>();
		}
	}
}
