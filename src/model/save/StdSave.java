package model.save;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.House;
import model.StdHouse;
import model.constraints.Constraint;
import model.constraints.IsInHouse;
import model.constraints.IsNotInHouse;
import model.constraints.IsNeighbourWith;
import model.constraints.IsNotNeighbourWith;
import model.constraints.ToTheLeftOf;
import model.constraints.IsWith;
import model.constraints.IsNotWith;
import model.entity.Animal;
import model.entity.AnimalManager;
import model.entity.Character;
import model.entity.CharacterManager;
import model.entity.Entity;
import util.Contract;

public class StdSave implements Save {
	
	//ATTRIBUTS
	private int numberOfHouses;
	private List<Constraint> constraints;
	private boolean hasAnimals;
	private int numberOfLevelPersonalized;
	private CharacterManager cm;
	private AnimalManager am;
	private House[] houses;
	
	private static String entityTheme;
	
	//CONSTRUCTEUR
	public StdSave(String entityTheme) {
		Contract.checkCondition(entityTheme != null && !entityTheme.equals(""), "Not a valid string path for file entity theme");
		StdSave.entityTheme = entityTheme;
	}
	
	//REQUETES
	@Override
	public synchronized int getNumberOfHouses() {
		return numberOfHouses;
	}

	@Override
	public synchronized List<Constraint> getConstraints() {
		List<Constraint> result = new ArrayList<Constraint>();
		for (Constraint c : constraints) {
			result.add(c);
		}
		return result;
	}

	@Override
	public synchronized boolean hasAnimals() {
		return hasAnimals;
	}

	@Override
	public boolean isLoaded() {
		return getNumberOfHouses() != 0 && getConstraints() != null;
	}

	@Override
	public synchronized int getNumberOfLevelPersonalized() {
		return numberOfLevelPersonalized;
	}
	
	public synchronized final CharacterManager getCharacterManager() {
		return cm;
	}
	
	public synchronized final AnimalManager getAnimalManager() {
		return am;
	}
	
	public synchronized final House[] getHouses() {
		return houses;
	}

	//COMMANDES
	@Override
	public synchronized void loadlevelClassic(String line) throws IOException {
		Pattern pattern = Pattern.compile("LEVEL ([0-9]+) : HOUSES (\\d) , PETS \\? (0|1) (.*)");
		Matcher matcher = pattern.matcher(line);
		if(matcher.matches()) {
			numberOfHouses = Integer.parseInt(matcher.group(2));
			cm = new CharacterManager(entityTheme, getNumberOfHouses(), getNumberOfHouses());
			hasAnimals = matcher.group(3).equals("0") ? false : true;
			int animalNumber = hasAnimals() ? getNumberOfHouses() : 0;
			am = new AnimalManager(entityTheme, animalNumber, getNumberOfHouses());
			int patternLength = ("LEVEL " + matcher.group(1) + " : HOUSES " 
					+ getNumberOfHouses() + " , PETS ? " + matcher.group(3)).length() + 1;
			line = line.substring(patternLength);
			patternConstraints(line);
		} else {
			System.out.println("Error occured");
		}
	}

	@Override
	public synchronized void loadLevelPersonalized(String line) throws IOException {
		Pattern pattern = Pattern.compile("LEVEL ([0-9]+) : HOUSES (\\d) , PETS \\? (0|1) (.*)");
		Matcher matcher = pattern.matcher(line);
		if(matcher.matches()) {
			numberOfHouses = Integer.parseInt(matcher.group(2));
			cm = new CharacterManager(entityTheme, getNumberOfHouses(), getNumberOfHouses());
			hasAnimals = matcher.group(3).equals("0") ? false : true;
			int animalNumber = hasAnimals() ? getNumberOfHouses() : 0;
			am = new AnimalManager(entityTheme, animalNumber, getNumberOfHouses());
			int patternLength = ("LEVEL " + matcher.group(1) + " : " 
				 + "HOUSES "  + getNumberOfHouses() + " , PETS ? " + matcher.group(3)).length() + 1;
			line = line.substring(patternLength);
			patternConstraints(line);
		} else {
			System.out.println("Error occured");
		}

	}

	@Override
	public synchronized void closeLevel() {
		Contract.checkCondition(isLoaded(), "Cannot close any level, already no level");
		constraints = null;
		hasAnimals = false;
		entityTheme = null;
		houses = null;
	}

	
	
	// OUTILS
	
	/**
	 * Gère l'expression régulière des contraintes avec une seule entité et une maison en particulier.
	 * @return
	 * 		l'expression régulière des Contraintes 1 & 2
	 */
	private String regExpConstraintUnitary() {
		return "(\\{CONSTRAINT: (\\d) , HOUSE: (\\d); ENTITY: (A|C), (\\d) \\})*";
	}
	
	/**
	 * Gère l'expression régulière des contraintes avec deux entités.
	 * @return
	 * 		l'expression régulière des Contraintes 3, 4, 5, 6, 7
	 */
	private String regExpConstraintBinary() {
		return "(\\{CONSTRAINT: (\\d) , ENTITY1: (A|C), (\\d) ; ENTITY2: (A|C), (\\d) \\})*";
	}
	
	/**
	 * A partir d'une ligne, évalue les contraintes et intialise la liste des contraintes.
	 * @param line
	 * @pre
	 * 		line != null && line != ""
	 * @post
	 * 		getConstraints() != null
	 */
	private void patternConstraints(String line) {
		Contract.checkCondition(line != null && !line.equals(""), "Not a valid line for regexp");
		Pattern pattern;
		Matcher matcher;
		List<Constraint> result = new ArrayList<Constraint>();
		while(!line.equals("")) {
			pattern = Pattern.compile(regExpConstraintBinary());
			matcher = pattern.matcher(line);
			if(matcher.matches()) {
				int numberConstraint = Integer.parseInt(matcher.group(2));
				result.add(initializeConstraintBinary(numberConstraint, matcher.group(3), 
						Integer.parseInt(matcher.group(4)) - 1, 
						matcher.group(5), Integer.parseInt(matcher.group(6)) - 1));
				line = line.replace(matcher.group(1), "");
			} else {
				pattern = Pattern.compile(regExpConstraintUnitary() + regExpConstraintBinary());
				matcher = pattern.matcher(line);
				if(matcher.matches()) {
					int numberConstraint = Integer.parseInt(matcher.group(2));
					result.add(initlializeConstraintUnitary(numberConstraint, Integer.parseInt(matcher.group(3)) - 1,matcher.group(4), 
							Integer.parseInt(matcher.group(5))- 1));
					line = line.replace(matcher.group(1), "");

				} else {
					System.out.println("error occured");
				}
			}

		}
		constraints = new ArrayList<Constraint>(result);
	}
	
	/**
	 * Initialise la contrainte unitaire correspondante avec les informations passées en paramètres.
	 * @param
	 * 		int idConstraint, int indexHouse, String entityType, int indexEntity
	 */
	private Constraint initlializeConstraintUnitary(int idConstraint, int indexHouse, String entityType, int indexEntity) {
		//Test des préconditions
		Contract.checkCondition(idConstraint == 1 || idConstraint == 2, "Not a unitary constraint");
		Contract.checkCondition(indexHouse < getNumberOfHouses(), "Not a valid index for house");
		Contract.checkCondition(entityType.equals("C") || entityType.equals("A"), "Not a valid entity type");
		Contract.checkCondition(indexEntity <= getNumberOfHouses(), "Not a valid index of entity");
				
		//Création de l'entité
		Entity e;
		if(entityType.equals("C")) {
			e = cm.getEntity(indexEntity);
		} else {
			e = am.getEntity(indexEntity);
		}
		
		// Instanciation de la contrainte
		Constraint c;
		houses = new House[getNumberOfHouses()];
		for(int i = 0; i < getNumberOfHouses(); i++) {
			houses[i] = new StdHouse(i, getNumberOfHouses());
		}
		switch(idConstraint) {
		case 1:
			c = new IsInHouse<Entity>(houses, e, indexHouse);
			break;
		case 2:
			c = new IsNotInHouse<Entity>(houses, e, indexHouse);
			break;
		default:
			System.out.println("Not a possible case");
			c = null;
		}
		return c;
	}
	/**
	 * Initialise la contrainte binaire correspondante avec les informations passées en paramètres.
	 * @param
	 * 		int idConstraint, String entityType1, int index1, String entityType2, int index2
	 */
	private Constraint initializeConstraintBinary(int idConstraint, String entityType1, int index1, String entityType2, int index2) {
		// Test des préconditions
		Contract.checkCondition(idConstraint != 1 && idConstraint != 2, "Not a binary constraint");
		Contract.checkCondition(index2 <= getNumberOfHouses(), "Not a valid index for entity 2");
		Contract.checkCondition(entityType1.equals("C") || entityType1.equals("A"), "Not a valid entity type");
		Contract.checkCondition(entityType2.equals("C") || entityType2.equals("A"), "Not a valid entity type");
		Contract.checkCondition(index1 <= getNumberOfHouses(), "Not a valid index of entity 1");
		
		// Création des entités
		Entity e;
		Entity f;
		if(entityType1.equals("C")) {
			e = cm.getEntity(index1);
			if(entityType2.equals("C")) {
				f = cm.getEntity(index2);
			} else {
				f = am.getEntity(index2);
			}
		} else {
			e = am.getEntity(index1);
			if(entityType2.equals("C")) {
				f = cm.getEntity(index2);
			} else {
				f = am.getEntity(index2);
			}
		}
		
		// Instanciation de la contrainte
		Constraint c;
		houses = new House[getNumberOfHouses()];
		for(int i = 0; i < getNumberOfHouses(); i++) {
			houses[i] = new StdHouse(i, getNumberOfHouses());
		}
		switch(idConstraint) {
		case 3:
			c = new IsNeighbourWith<Entity, Entity>(houses, e, f);
			break;
		case 4: 
			c = new IsNotNeighbourWith<Entity, Entity>(houses, e, f);
			break;
		case 5:
			c = new ToTheLeftOf<Entity, Entity>(houses, e, f);
			break;
		case 6:
			c = new IsWith<Character, Animal>(houses, (Character)e, (Animal)f);
			break;
		case 7:
			c = new IsNotWith<Character, Animal>(houses, (Character)e, (Animal)f);
			break;
			
		default:
			System.out.println("Not a valid constraint");
			c = null;
		}
		return c;
	}
}