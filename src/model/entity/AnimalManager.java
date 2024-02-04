package model.entity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import util.Contract;

/**
 * Classe gérant le chargement des sauvegardes des animaux.
 * Indique les personnages disponibles pour un nombre de personnages donné
 * 
 * @inv <pre>
 * 		
 * </pre>
 * 
 * @cons <pre>
 * 		$ARGS$ String file
 * 			   int numberOfChar
 * 			   int numberOfHouses
 * 		$PRE$
 * 			  file != null && !file.equals("")
 * 			  numberOfEntity == numberOfHouses || numberOfEntity == 0
 * 		$POST$
 * 			  getEntityList() ==> liste des numberOfEntity animaux disponibles
 * 									trouvés dans le file
 * 
 * </pre>
 *
 */
public class AnimalManager extends EntityManager{

	public AnimalManager(String file, int numberOfEntity, int nbHouses) {
		Contract.checkCondition(file != null && !file.equals(""), "Not a valid string for a file");
		Contract.checkCondition(numberOfEntity == nbHouses  || numberOfEntity == 0, "Not a valid number of animals");
		setEntityList(readSave(file, numberOfEntity));
	}
		
	// OUTILS
	
	/**
	 * Lit le fichier de sauvegarde passé en argument et renvoie la liste des numberOfEntity
	 * animaux disponibles.
	 * @param file
	 * @param numberOfEntity
	 * @post
	 * 		list.length == numberOfEntity
	 * 		forall e in list:
	 * 			e est de type Animal
	 */
	private List<Entity> readSave(String file, int numberOfEntity) {
		List<Entity> list = new ArrayList<Entity>();
		try {
			File f = new File(file);
			if(f.exists() && f.canRead()) {
				FileReader fr = new FileReader(f);
				BufferedReader br = new BufferedReader(fr);
				String line;
				while((line = br.readLine()) != null && numberOfEntity != 0) {
					Animal a = animalPattern(line);
					if (a != null) {
						list.add(animalPattern(line));
						--numberOfEntity;
					}
				}
				br.close();
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * A partir d'une expression régulière identifiant le rôle et l'image d'un
	 * animal sur une chaîne de caractères donné, renvoie le personnage.
	 * @param line
	 * @post
	 * 		a == null ==>
	 * 			line ne correspond pas à un animal
	 * 		a != null ==>
	 * 			line correspond à une chaîne de caractères décrivant un animal
	 */
	private Animal animalPattern(String line) {
		Pattern pattern = Pattern.compile("ANIMAL (\\d): (.+), (.+)\\.([A-Za-z]+)");
		Matcher matcher = pattern.matcher(line);
		if(matcher.matches()) {
			int index = Integer.parseInt(matcher.group(1));
			String role = matcher.group(2);
			String picture = matcher.group(3);
			String extension = matcher.group(4);
			Animal a = new Animal(role, picture + "." + extension, index);
			return a;
		}
		return null;
	}

}
