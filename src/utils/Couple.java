package utils;

public class Couple <E, F>{
	
	//ATTRIBUTS
	private E element1;
	private F element2;
	
	//CONSTRUCTEUR
	public Couple(E elt1, F elt2) {
		element1 = elt1;
		element2 = elt2;
	}
	
	//REQUETES
	public E getElement1() {
		return element1;
	}
	
	public F getElement2() {
		return element2;
	}
}
