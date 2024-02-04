package model;

import java.util.List;

import model.constraints.*;
import model.entity.Animal;
import model.entity.AnimalManager;
import model.entity.Character;
import model.entity.CharacterManager;
import model.entity.Entity;
import util.Contract;

import java.util.ArrayList;

public class Solver {
	
	// ATTRIBUTS
	
	private boolean solutionFound;
	private List<List<House>> solutions;
	
	// CONSTRUCTEUR
	
	public Solver() {
		solutions = new ArrayList<List<House>>();
	}
	
	// REQUETES
	
	/**
	 * Renvoie la liste des solutions trouvées.
	 */
	public List<List<House>> getSolutions() {
		return solutions;
	}
	
	/**
	 * Renvoie si au moins une solution a été trouvée.
	 */
	public synchronized boolean isSolutionFound() {
		return solutionFound;
	}
	
	/**
	 * Renvoie la solution d'indice i.
	 * 
	 * @pre
	 * 	<pre> 0 <= i < solutions.size() </pre>
	 */
	public synchronized List<House> getSolution(int i) {
		Contract.checkCondition(0 <= i && i < solutions.size(), "wrong index");
		return solutions.get(i);
	}
	
	/**
	 * Renvoie le nombre de solutions trouvées.
	 */
	public synchronized int numberOfSolutions() {
		return solutions.size();
	}
	
	// COMMANDES
	
	/**
	 * A partir d'une liste de contraintes, d'un AnimalManager 
	 *  et d'un CharacterManager renvoie la liste de toutes les solutions possibles.
	 * 
	 * @param c
	 * 
	 * 
	 * @post 
	 * 	<pre>
	 * 		isSolutionFound() => getSolutions().size() > 0
	 * 							forall s in getSolutions() :
	 * 								s respecte chacune des contraintes de c
	 * 
	 * 		!isSolutionFound() => getSolutions().size() == 0
	 * </pre>
	 */
	public synchronized void findSolutionsFor(List<Constraint> constraintList, CharacterManager cm, AnimalManager am) {
		
		// Reinitialisation des attributs

		solutionFound = false;
		solutions = new ArrayList<List<House>>();
				
		
		// Séparation des contraintes unitaires en fonction de leurs types.
		List<IsInHouse<Entity>>constraint1List = getConstraint1(constraintList);
		constraintList.removeAll(constraint1List);
		

		allSolutions(constraintList, constraint1List, cm, am);
				
			
			
		House[] house = new StdHouse[cm.getEntityList().size()];
		// On enlève toutes les solutions qui ne respesctent pas les contraintes.
		
		List<List<House>> tmp = new ArrayList<List<House>>(solutions);
		
		for (int i = 0; i < tmp.size(); i++) {
			List<House> s = tmp.get(i);
			
			for (int j = 0; j < cm.getEntityList().size(); ++j) {
				house[j] = s.get(j);
				s.get(j).getCharacter().setHouse(house[j]);
				Animal a = s.get(j).getAnimal();
				if (a != null) {
					a.setHouse(house[j]);
				}
			}
			
			for(Constraint constraint : constraintList) {
				constraint.setHouses(house);
				if(!constraint.isVerified()) {
					solutions.remove(s);
					break;
				}
			}
				
		}
		solutionFound = (solutions.size() > 0);
		
		for(Entity c : cm.getEntityList()) {
			c.setHouse(null);
		}
		for(Entity c : am.getEntityList()) {
			c.setHouse(null);
		}
	}
	

	// OUTILS

	/**
	 * Depuis une liste de contraintes, renvoie celles de type 1.
	 */
	@SuppressWarnings("unchecked")
	private List<IsInHouse<Entity>> getConstraint1(List<Constraint> constraintList) {
		ArrayList<IsInHouse<Entity>> result = new ArrayList<IsInHouse<Entity>>();
		for(Constraint c : constraintList) {
			if(c.getId() == 1) {
				result.add((IsInHouse<Entity>) c);
			}
		} 
		return result;
	}

	/**
	 * Depuis une liste de Contraintes1 et une liste de Contraintes2,
	 * construit toutes les solutions possibles pour la Card c.
	 * @param constraint1List
	 * @param constraint2List
	 * 
	 */
	private void allSolutions(List<Constraint> list, List<IsInHouse<Entity>> constraint1List,
			CharacterManager cm, AnimalManager am) {
		
		List<Character> characters = new ArrayList<Character>();
		for (Entity e : cm.getEntityList()) {
			characters.add((Character) e);
		}
		
		List<Animal> animals = new ArrayList<Animal>();
		for (Entity e : am.getEntityList()) {
			animals.add((Animal) e);
		}

		List<House> solutionPossible = new ArrayList<House>();
		for (int i = 0; i < characters.size(); ++i) {
			solutionPossible.add(new StdHouse(i, characters.size()));
		}
		
		
		
		for (IsInHouse<Entity> c1 : constraint1List) {
			Entity e = c1.getEntity();
			int index = c1.getIndice();
			House h = solutionPossible.get(index);
			if(e.getEntityType().equals("A")) {
				if (h.getAnimal() != null && h.getAnimal() != e|| !animals.contains(e) && e.getHouse() != h) {
					solutions = new ArrayList<List<House>>();
					return;
				}
				h.setAnimal((Animal) e);
				e.setHouse(h);
				animals.remove(e);
			} else {
				if (h.getCharacter() != null && h.getCharacter() != e || !characters.contains(e) && e.getHouse() != h) {
					solutions = new ArrayList<List<House>>();
					return;
				}
				h.setCharacter((Character) e);	
				e.setHouse(h);
				characters.remove(e);
			}
		}
		
		for(Entity c : cm.getEntityList()) {
            c.setHouse(null);
        }
        for(Entity c : am.getEntityList()) {
            c.setHouse(null);
        }
		
		List<List<Character>> temp = allEntitiesCombinations(solutionPossible, characters);
		List<List<Animal>> temp2 = allEntitiesCombinations(solutionPossible, animals);
		List<List<House>> result = allCombinations(solutionPossible, temp, temp2);
		
		solutions = result;	
	}

		
	private List<House> cloneList(List<House> h) {
		List<House> clone = new ArrayList<House>(h.size());
		for(House house : h) {
			clone.add(house.clone());
		}
		return clone;
	}

	private List<List<House>> allCombinations(List<House> house, List<List<Character>> cList, List<List<Animal>> aList) {
		List<List<House>> result = new ArrayList<List<House>>();

		if(aList.get(0).size() == 0) {
			for (List<Character> c : cList) {
				List<House> tmp = cloneList(house);
				int k = 0;
				for (int j = 0; j < tmp.size(); ++j) {
					House h = tmp.get(j);
					if (h.getCharacter() == null) {
						h.setCharacter(c.get(k));
						++k;
					} 
				}
				result.add(tmp);			
			}
		} else {
		
			for (List<Character> c : cList) {
				for(List<Animal> a : aList) {
					List<House> tmp = cloneList(house);
					int k = 0;
					int i = 0;
					for (int j = 0; j < tmp.size(); ++j) {
						House h = tmp.get(j);
						if (h.getCharacter() == null) {
							h.setCharacter(c.get(i));
							++i;
						}
						if(h.getAnimal() == null) {
							h.setAnimal(a.get(k));
							++k;
						}
					}
					result.add(tmp);
				}
			}
			
		}
		return result;
	}
	
	
	
	
	private <E> List<List<E>> allEntitiesCombinations(List<House> houses, List<E> list) {
		if (list.isEmpty()) {
			List<List<E>> result = new ArrayList<List<E>>();
			result.add(list);
			return result;
		}
		E entity = list.remove(0);
		List<List<E>> result = new ArrayList<List<E>>();
		List<List<E>> permut = allEntitiesCombinations(houses, list);
		for (List<E> c : permut) {
		    for (int index = 0; index <= c.size(); index++) {
			     List<E> temp = new ArrayList<>(c);
			     temp.add(index, entity);
			     result.add(temp);
		    }
		}
		return result;
	}
}		
		

		
		
		
