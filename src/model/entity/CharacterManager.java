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
 * Classe gérant le chargement des sauvegardes de personnages.
 * Indique les personnages disponibles pour un nombre donné
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
 * 			  numberOfEntity == numberOfHouses
 * 		$POST$
 * 			  getEntityList() ==> liste des numberOfEntity personnages disponibles
 * 									trouvés dans le file
 * 
 * </pre>
 *
 */
public class CharacterManager extends EntityManager{

	public CharacterManager(String file, int numberOfEntity, int nbHouses) {
		Contract.checkCondition(file != null && !file.equals(""), "Not a valid string for a file");
		Contract.checkCondition(numberOfEntity == nbHouses, "Not a valid number of characters");
		setEntityList(readSave(file, numberOfEntity));
	}
	
	// OUTILS
	
	/**
	 * Lit le fichier de sauvegarde passé en argument et renvoie la liste des numberOfEntity
	 * personnages disponibles.
	 * @param file
	 * @param numberOfEntity
	 * @post
	 * 		list.length == numberOfEntity
	 * 		forall e in list:
	 * 			e est de type Character
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
					list.add(characterPattern(line));
					--numberOfEntity;
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
	 * personnage sur une chaîne de caractères donné, renvoie le personnage.
	 * @param line
	 * @post
	 * 		c == null ==>
	 * 			line ne correspond pas à un personnage
	 * 		c != null ==>
	 * 			line correspond à une chaîne de caractères décrivant un personnage
	 */
	private Character characterPattern(String line) {
		Pattern pattern = Pattern.compile("PERSONNAGE (\\d): (.+), (.+)\\.([A-Za-z]+)");
		Matcher matcher = pattern.matcher(line);
		if(matcher.matches()) {
			int index = Integer.parseInt(matcher.group(1));
			String role = matcher.group(2);
			String picture = matcher.group(3);
			String extension = matcher.group(4);
			Character c = new Character(role, picture + "." + extension, index);
			return c;
		}
		return null;
	}

}
