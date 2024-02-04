package model;

import model.entity.Animal;
import model.entity.Character;
import util.Contract;

public class StdHouse implements House, Cloneable {
	private Character character;
	private Animal animal;
	private final int index;
	
	public StdHouse(int ind, int numberOfHouses) {
		Contract.checkCondition(ind >= 0  && ind < numberOfHouses, "Cannot set this index of house");
		index = ind;
	}
	
	@Override
	public Character getCharacter() {
		return character;
	}

	@Override
	public Animal getAnimal() {
		return animal;
	}

	@Override
	public boolean isHouseEmpty() {
		return getCharacter() == null && getAnimal() == null;
	}

	@Override
	public void setCharacter(Character p) {
		character = p;
	}

	@Override
	public void setAnimal(Animal a) {
		animal = a;
	}

	@Override
	public int getIndex() {
		return index;
	}
	
	// COMMANDES
	
	@Override
	 public StdHouse clone() {
		StdHouse clone = null;
		try {
			 clone = (StdHouse) super.clone();
		 } catch (CloneNotSupportedException e) {
			 throw new AssertionError();
		 }
		 clone.setAnimal(animal);
		 clone.setCharacter(character);
		 return clone;
	 }

	
}
