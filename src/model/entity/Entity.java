package model.entity;

import javax.swing.ImageIcon;

import model.House;

/**
 * Interface des Entités.
 * Permet de regrouper animaux et personnages 
 * dans un même type.
 * 
 *@inv <pre>
 *	getRole() != null && !getRole.equals("")
 *</pre>
 *
 *@cons <pre>
 *	$ARGS$ String s
 *		   String pic
 *		   int index
 *
 *	$PRE$ 
 *		s != null && !s.equals("")
 *		pic != null
 *		index > 0
 *	$POST$
 *		getRole().equals(s)
 *		getPicture() == Image depuis le chemin représenté par pic
 *		getIndex() == index
 *	</pre>
 */

public interface  Entity {

	// REQUETES
	
	/**
	 * renvoie le type de l'entité sous la forme C pour Character et A pour Animal
	 */
	public String getEntityType();
	/**
	 * Renvoie l'image associée à l'entité.
	 */
	public ImageIcon getPicture();
	
	/**
	 * Renvoie la chaine de caractères de l'image associée à l'entité.
	 */
	public String getPicString();
	/**
	 * Renvoie le rôle ou nom de l'entité.
	 */
	public String getRole();
	
	/**
	 * Renvoie la maison dans laquelle se trouve l'entité.
	 */
	public House getHouse();
	
	/**
	 * Renvoie l'indice de l'entité dans son fichier afin de l'identifier.
	 */
	public int getIndex();
	
	/**
	 * Indique si une maison a été attribuée à l'entité.
	 */
	public boolean isSet();
	
	// COMMANDE
	/**
	 * Associe une maison où se trouve actuellement l'entité.
	 * @param <pre>
	 * 		House h
	 * </pre>
	 * </pre>
	 * @post <pre>
	 * 		getHouse() == h
	 * 		h == null => !isSet()
	 *   	h != null => isSet()
	 * </pre>
	 */
	public void setHouse(House h);
}
