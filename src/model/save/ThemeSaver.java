package model.save;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

import model.Level;
import model.entity.AnimalManager;
import model.entity.CharacterManager;
import model.entity.Entity;
import util.Contract;

/**
 * Classe gérant les fichiers de thèmes d'entités.
 *
 */
public class ThemeSaver {
	//ATTRIBUTS
	List<ImageIcon> listImageAnimal;
	List<String> listImageAnimalPath;
	List<ImageIcon> listImageCharacter;
	List<String> listImageCharacterPath;
	List<String> characterNameList;
	List<String> animalNameList;
	
	//CONSTRUCTEUR
	
	//REQUETES
	
	public List<ImageIcon> getListImageAnimal() {
		return new ArrayList<ImageIcon>(listImageAnimal);
	}
	
	public List<String> getListImageAnimalPath() {
		return new ArrayList<String>(listImageAnimalPath);
	}
	
	public List<ImageIcon> getListImageCharacter() {
		return new ArrayList<ImageIcon>(listImageCharacter);
	}
	
	public List<String> getListImageCharacterPath() {
		return new ArrayList<String>(listImageCharacterPath);
	}
	
	public List<String> getCharacterNameList() {
		return new ArrayList<String>(characterNameList);
	}
	
	public List<String> getAnimalNameList() {
		return new ArrayList<String>(animalNameList);
	}
	
	//COMMANDES
	
	/**
	 * Charge un fichier de thème dont le chemin est passé en paramètre.
	 * @pre
	 * 		pathname != null
	 * 		entitiesNumber >= Level.MIN_HOUSES && entitiesNumber <= Level.MAX_HOUSES
	 * @post
	 * 		
	 */
	public void loadTheme(String pathname, int entitiesNumber) {
		Contract.checkCondition(pathname != null, "Pathname is null cannot load");
		Contract.checkCondition(entitiesNumber >= Level.MIN_HOUSES && entitiesNumber <= Level.MAX_HOUSES, "Wrong entity number cannot load");
		CharacterManager cm = new CharacterManager(pathname, entitiesNumber, entitiesNumber);
		AnimalManager am = new AnimalManager(pathname, entitiesNumber, entitiesNumber);
		
		characterNameList = cm.getEntityNameList();
		animalNameList = am.getEntityNameList();
		
		listImageAnimal = new ArrayList<ImageIcon>();
		listImageAnimalPath = new ArrayList<String>();
		for(Entity a : am.getEntityList()) {
			listImageAnimal.add(a.getPicture());
			listImageAnimalPath.add(a.getPicString());
		}
		
		listImageCharacter = new ArrayList<ImageIcon>();
		listImageCharacterPath = new ArrayList<String>();
		for(Entity c : cm.getEntityList()) {
			listImageCharacter.add(c.getPicture());
			listImageCharacterPath.add(c.getPicString());
		}
	}
	
	/**
	 * Supprime un dossier de thème dont le chemin est passé en paramètre.
	 * @pre
	 * 		path != null
	 * @post
	 * 		
	 */
	public void deleteTheme(String path) {
		Contract.checkCondition(path != null, "Pathname is null cannot delete");
		String animalPath = path + "animals/";
		File animal = new File(animalPath);
		String[] animals = animal.list();
		for(String s : animals) {
			File image = new File(animalPath + s);
			image.delete();
		}
		String characterPath = path + "characters/";
		File character = new File(characterPath);
		String[] characters = character.list();
		for(String s : characters) {
			File image = new File(characterPath + s);
			image.delete();
		}
		File directory = new File(path +  "/animals/");
		directory.delete();
		directory = new File(path + "/characters/");
		directory.delete();
		directory = new File(path + "/entity.txt");
		directory.delete();
		directory = new File(path);
		directory.delete();
	}
	
	/**
	 * Crée un fichier de thème en fonction des listes passées en paramètres.
	 * @throws IOException 
	 * @inv 
	 * 		animalsNames != null
	 * 		charactersNames != null
	 * 		animalsSprites != null
	 * 		charactersSprites != null
	 * 		animalsNames.size() == animalsSprites.size()
	 * 		charactersNames.size() == charactersSprites.size()
	 * 		charactersNames.size() == animalsNames.size()
	 * 		pathname != null
	 * 
	 * @post
	 * 		renvoie un fichier f contenant les informations sur le thème enregistré
	 * 		à partir des listes
	 */
	public File getEntityFile(String pathname, List<String> animalsNames, List<String> charactersNames, List<String> animalsSprites, List<String> charactersSprites) throws IOException {
		Contract.checkCondition(animalsNames != null, "List of animals names null cannot create entity file");
		Contract.checkCondition(charactersNames != null, "List of characters names null cannot create entity file");
		Contract.checkCondition(animalsSprites != null, "List of animals sprites null cannot create entity file");
		Contract.checkCondition(charactersSprites != null, "List of characters sprites null cannot create entity file");
		Contract.checkCondition(animalsNames.size() == animalsSprites.size(), "Not a valid size for animals list");
		Contract.checkCondition(charactersNames.size() == charactersSprites.size(), "Not a valid size for characters list");
		Contract.checkCondition(pathname != null, "Pathname null cannot create entity file");
		Contract.checkCondition(animalsNames.size() == charactersNames.size(), "Not a valid size for characters and animals lists");
		File file = new File(pathname);
		if(!file.createNewFile()) {
			throw new IOException();
		}
		BufferedWriter bf = new BufferedWriter(new FileWriter(file));
		for(int i = 0; i < charactersNames.size(); i++) {
			bf.append("PERSONNAGE " + (i+1) + ": " + charactersNames.get(i) +", " + ("res" + charactersSprites.get(i)) + "\n");
		}
		for(int i = 0; i < charactersNames.size(); i++) {
			bf.append("ANIMAL " + (i+1) + ": " + animalsNames.get(i) +", " + ("res" + animalsSprites.get(i)) + "\n");
		}
		bf.close();
		return file;
	}
	
	/**
	 * Supprime le fichier de chemin passé en paramètre.
	 * @throws IOException 
	 */
	public void deleteEntityFile(String pathname) throws IOException {
		Contract.checkCondition(pathname != null, "Pathname null cannot delete entity file");
		File file = new File(pathname);
		if(!file.exists()) {
			throw new IOException();
		}
		file.delete();
	}
	
	
	/**
	 * Crée les dossiers inexistants à partir d'un chemin donné.
	 * @throws IOException 
	 */
	public void createDirectories(String pathname) throws IOException {
		Contract.checkCondition(pathname != null, "Pathname is null");
		Files.createDirectories(Paths.get(pathname));
	}
	
	/**
	 * A partir d'un chemin source et d'un chemin destination, copie le fichier.
	 * @throws IOException 
	 */
	public void copyFile(String srcPath, String destPath) throws IOException {
		Contract.checkCondition(srcPath != null, "Source path is null");
		Contract.checkCondition(destPath != null, "Destination path is null");
		File from = new File(srcPath);
		File to = new File(destPath);
		Files.copy(from.toPath(), to.toPath(), StandardCopyOption.REPLACE_EXISTING);
	}
}
