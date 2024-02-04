package model.save;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import model.Card;
import model.Level;
import model.constraints.Constraint;
import model.constraints.IsInHouse;
import model.constraints.IsNotInHouse;
import model.constraints.IsNeighbourWith;
import model.constraints.IsNotNeighbourWith;
import model.constraints.ToTheLeftOf;
import model.constraints.IsWith;
import model.constraints.IsNotWith;
import model.entity.Entity;
import util.Contract;
import utils.Couple;

/**
 * Classe s'occupant de charger le fichier demandé.
 * Enregistre les informations des niveaux dans une Map sous forme d'expression régulière.
 * 
 * @inv
 * 		fileString != null
 * 		levelInfo != null
 * 		0 <= levelInfo.size() < Level.MAX_LEVEL_NUMBER
 * 		numberOfLevel <==> levelInfo.size()
 * 
 * @cons
 * 		$ARGS$ boolean isPersonalized
 * 		$PRE$ -
 * 		$POST$
 * 			levelInfo() != null
 * 			fileString != null
 *
 */
public class SaveManager {
	// CONSTANTES
	public static final String FILE_CLASSIC = "/ressources_classic/save.xml";
	public static final String FILE_PERSONALIZED = "res/ressources_personalized/save.xml";
	
	//ATTRIBUTS
	
	
	private final String fileString;
	
	private int numberOfLevel;
	private final Map<Integer, String> levelInfo;
	private Card card;
	private Save save;
	private final Map<Integer, Couple<String, String>> levelId;
	private String entityTheme;
	
	//CONSTRUCTEUR
	
	public SaveManager(boolean isPersonalized) {
		if(!isPersonalized) {
			fileString = FILE_CLASSIC;
		} else {
			fileString = FILE_PERSONALIZED;
		}
		levelInfo = new HashMap<Integer, String>();
		levelId = new HashMap<Integer, Couple<String, String>>();
		loadLevelFile();
	}
	
	//REQUETE
	/**
	 * Renvoie la carte associée au dernier niveau chargé s'il existe.
	 * Renvoie null sinon.
	 */
	public synchronized Card getCard() {
		return card;
	}
	/**
	 * Renvoie le thème associé aux entités.
	 */
	public synchronized String getEntityTheme() {
		return entityTheme;
	}
	
	/**
	 * Renvoie la save chargée pour un niveau
	 */
	public synchronized Save getSave() {
		return save;
	}
	
	public synchronized int getNumberOfLevels() {
		return levelInfo.size();
	}
	
	public synchronized Map<Integer, Couple<String, String>> getLevelId() {
		return levelId;
	}
	
	/**
	 * Renvoie le numéro de niveau associé à son nom.
	 * @param name
	 * @return
	 */
	public synchronized int getLevelNumber(String name) {
		Contract.checkCondition(name != null, "Not a valid name to search in level id map");
		for(Entry<Integer, Couple<String, String>> couple : levelId.entrySet()) {
			String text = couple.getValue().getElement2();
			if(text != null && text.toLowerCase().equals(name.toLowerCase())) {
				return couple.getKey();
			}
		}
		return -1;
	}
	
	//COMMANDES
	
	/**
	 * Attribue un nouveau thème concernant les entités.
	 * @param newEntity
	 * @pre newEntity != null
	 * @post getEntityTheme() == newEntity
	 */
	public synchronized void setEntityTheme(String newEntity) {
		Contract.checkCondition(newEntity != null, "Not a valid entity theme");
		entityTheme = newEntity;
	}
	
	/**
	 * Charge le niveau demandé en fonction du fichier demandé.
	 * @param levelNumber
	 * @pre 0 <levelNumber <= Level.MAX_LEVEL_NUMBER && entityTheme != null
	 * @post Toutes les informations du niveau sont chargées et les objets sont instanciés.
	 */
	public synchronized void loadlevel(int levelNumber) {
		Contract.checkCondition(0 < levelNumber && levelNumber <= Level.MAX_LEVEL_NUMBER, "Not a valid level number, cannot load");
		Contract.checkCondition(entityTheme != null, "Cannot load with no entity theme");
		if(save != null) { 
			save.closeLevel(); 
		}
		save = new StdSave(entityTheme);
		if(fileString.equals(FILE_CLASSIC)) {
			try {
				save.loadlevelClassic(levelInfo.get(levelNumber - 1));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			try {
				save.loadLevelPersonalized(levelInfo.get(levelNumber - 1));;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		card = new Card(save.getConstraints(), save.getNumberOfHouses(), save.hasAnimals());
	}
	
	/**
	 * Ajoute un niveau au fichier des niveaux personnalisés.
	 * @param constraints
	 * @param numberOfHouses
	 * @param hasAnimals
	 * @pre 
	 * 		constraints != null
	 * 		Level.MAX_HOUSES >= numberOfHouses >= Level.MIN_HOUSES
	 * 		0 < levelInfo.size() <= Level.MAX_LEVEL_NUMBER
	 * 		forall i, 0 < i constraints.size():
	 * 			constraints.get(i) != null
	 * @post
	 * 		levelInfo.get(levelInfo.size()) => Une string décrivant les informations du niveau ajouté
	 * @throws IOException
	 */
	public synchronized void addLevel(List<Constraint> constraints, int numberOfHouses, boolean hasAnimals, String image, String name) throws IOException{
		//--------------------------
		Contract.checkCondition(constraints != null, "Cannot create a level if there's no constraint");
		Contract.checkCondition(Level.MAX_HOUSES >= numberOfHouses && numberOfHouses >= Level.MIN_HOUSES, "Not enough houses");
		Contract.checkCondition(0 <= levelInfo.size()
				&& levelInfo.size() <= Level.MAX_LEVEL_NUMBER, "Not a valid level number, cannot add");
		Contract.checkCondition(constraints != null, "Not valid constraints");
		for(int i = 0; i < constraints.size(); i++) {
			Contract.checkCondition(constraints.get(i) != null, "Not valid constraints");
		}
		//--------------------------
		File f = new File(FILE_PERSONALIZED);
		if(!f.canRead() || !f.exists() || !f.canWrite()) {
			throw new IOException("Issue with file, can't read or doesn't exist");
		}
		List<String> newLevel = newLevel(constraints, numberOfHouses, hasAnimals, image, name);
		
		List<String> file = readFileList(f);
		new PrintWriter(f).close();
		FileWriter fw = new FileWriter(f, true);
		BufferedWriter bf = new BufferedWriter(fw);
		String endFile = file.get(file.size()-1);
		file.remove(file.size() -1);
		for(String s : file) {
			bf.append(s + "\n");
		}
		for(String s : newLevel) {
			bf.append(s);
		}
		bf.append(endFile);
		bf.close();
		fw.close();
	}
	
	/**
	 * Supprimer un niveau dans le fichier des niveaux personnalisés.
	 * @param levelNumber
	 * @pre 
	 * 		0 < levelNumber <= levelInfo.size()
	 * @post
	 * 		levelInfo.size() == old levelInfo.size() - 1
	 * 		File f : fichier des niveaux personnalisés
	 * 			countLines(f) == old countLines(f) - (nombre de lignes que comporte le niveau dans le fichier)
	 * @throws IOException
	 */
	public synchronized void deleteLevel(int levelNumber) throws IOException {
		Contract.checkCondition(0 < levelNumber && levelNumber <= levelInfo.size(), levelNumber + " is Not a valid levelNumber, cannot delete");
		File f = new File(FILE_PERSONALIZED);
		if(!f.canRead() || !f.exists() || !f.canWrite()) {
			throw new IOException("Issue with file, can't read or doesn't exist");
		}
		FileReader fr = new FileReader(f);
		try (BufferedReader br = new BufferedReader(fr)) {
			File newFile = new File("res/ressources_personalized/save1.xml");
			newFile.createNewFile();
			BufferedWriter bw = new BufferedWriter(new FileWriter(newFile));
			String line;
			boolean isLevel = false;
			boolean levelNumberChanged = false;
			while((line = br.readLine()) != null) {
				Matcher m = Pattern.compile("[\\t]*<level id=\"" + levelNumber + "\"[ ]*(name=\".*\")?[ ]*(image=\".*\")?>[\\n]*").matcher(line);
				if(m.matches()) {
					isLevel = true;
					levelNumberChanged = true;
					continue;
				}
				if(line.equals("\t</level>") && isLevel) {
					isLevel = false;
					continue;
				}
				if(!isLevel) {
					m = Pattern.compile("([\\t]*<level id=\")([0-9]+)(\"[ ]*(name=\\\".*\\\")?[ ]*(image=\\\".*\\\")?>)[\\n]*").matcher(line);
					if(levelNumberChanged && m.matches()) {
						int newLevel = Integer.parseInt(m.group(2)) - 1;
						bw.append(m.group(1) + newLevel + m.group(3) + "\n");
					} else {
						bw.append(line + "\n");
					}
				}
			}
			br.close();
			bw.close();
			f.delete();
			newFile.renameTo(new File(FILE_PERSONALIZED));
			levelInfo.clear();
			numberOfLevel = 0;
			loadLevelFile();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Modifie un niveau dans le fichier des niveaux personnalisés.
	 * S'il est modifié, le niveau se trouvera en fin de fichier et la méthode renverra true.
	 * Si aucune donnée ne change par rapport au niveau d'origine, rien ne sera modifié et false sera renvoyé.
	 * @param constraints
	 * @param numberOfHouses
	 * @param hasAnimals
	 * @pre 
	 * 		0 < levelNumber <= levelInfo.size()
	 * 		constraints != null
	 * 		Level.MAX_HOUSES >= numberOfHouses >= Level.MIN_HOUSES
	 * 		0 < levelInfo.size() <= Level.MAX_LEVEL_NUMBER
	 * 		forall i, 0 < i constraints.size():
	 * 			constraints.get(i) != null
	 * @post
	 * 		levelInfo.get(levelInfo.size()) => Une string décrivant les informations du niveau modifié
	 * @throws IOException
	 */
	public synchronized boolean modifyLevel(int levelNumber, List<Constraint> constraints, int numberOfHouses, boolean hasAnimals, String image, String name) throws IOException {
		Contract.checkCondition(0 < levelNumber && levelNumber <= levelInfo.size(), "Not a valid levelNumber, cannot modify");
		//--------------------------
		Save s = new StdSave(entityTheme);
		s.loadLevelPersonalized(levelInfo.get(levelNumber));
		if(s.getConstraints() == constraints || constraints == null) {
			if(s.getNumberOfHouses() == numberOfHouses || numberOfHouses == 0) {
				if(s.hasAnimals() == hasAnimals || s.hasAnimals() && !hasAnimals) {
					return false;
				} else {
					replaceLevel(levelNumber, s.getConstraints(), s.getNumberOfHouses(), hasAnimals, image, name);
				}
			} else {
				if(s.hasAnimals() == hasAnimals || s.hasAnimals() && !hasAnimals) {
					replaceLevel(levelNumber, s.getConstraints(), numberOfHouses, s.hasAnimals(), image, name);
				} else {
					replaceLevel(levelNumber, s.getConstraints(), numberOfHouses, hasAnimals, image , name);
				}
			}
		} else {
			if(s.getNumberOfHouses() == numberOfHouses || numberOfHouses == 0) {
				if(s.hasAnimals() == hasAnimals) {
					replaceLevel(levelNumber, constraints, s.getNumberOfHouses(), s.hasAnimals(), image, name);
				} else {
					replaceLevel(levelNumber, constraints, s.getNumberOfHouses(), hasAnimals, image, name);
				}
			} else {
				if(s.hasAnimals() == hasAnimals || s.hasAnimals() && !hasAnimals) {
					replaceLevel(levelNumber, constraints, numberOfHouses, s.hasAnimals(), image, name);
				} else {
					replaceLevel(levelNumber, constraints, numberOfHouses, hasAnimals, image, name);
				}
			}
		}
		//--------------------------
		return true;
	}
	
	/**
	 * Remplacer un niveau dans le fichier des niveaux personnalisés.
	 * Le niveau se trouvera en fin de fichier et la méthode renverra true.
	 * @param constraints
	 * @param numberOfHouses
	 * @param hasAnimals
	 * @pre 
	 * 		0 < levelNumber <= levelInfo.size()
	 * @post
	 * 		levelInfo.get(levelInfo.size()) => Une string décrivant les informations du niveau remplacé
	 * @throws IOException
	 */
	public void replaceLevel(int levelNumber, List<Constraint> constraints, int numberOfHouses, boolean hasAnimals, String image, String name) throws IOException {
		deleteLevel(levelNumber);
		addLevel(constraints, numberOfHouses, hasAnimals, image, name);
	}

	//OUTILS
	
	/**
	 * Lit les lignes d'un fichier f et les ajoute à une liste
	 * @param f
	 * @pre f != null && f.exists()
	 * @return List<String> file
	 */
	private synchronized List<String> readFileList(File f) {
		Contract.checkCondition(f != null && f.exists(), "There is no file or it doesn't exist, can't read file");
		List<String> file = new ArrayList<String>();
		try {
			FileReader fr = new FileReader(f);
			try (BufferedReader bf = new BufferedReader(fr)) {
				String line;
				while((line = bf.readLine()) != null) {
					file.add(line);
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return file;
		
	}
	
	/**
	 * Ajoute les nouvelles informations du niveau dans une liste de string.
	 * Les préconditions sont vérifiées par la méthode appelante.
	 * @param constraints
	 * @param numberOfHouses
	 * @param hasAnimals
	 * @post
	 * 		result != null
	 * 		result => contient sous forme de string les informations du niveau au format xml
	 * 				à ajouter au fichier
	 * @return List<String> result
	 */
	@SuppressWarnings("unchecked")
	private List<String> newLevel(List<Constraint> constraints, int numberOfHouses, boolean hasAnimals, String image, String name) {
		List<String> level = new ArrayList<String>();
		if(image == null && name == null) {
			//Ajout à la liste des informations basiques du niveau
			level.add("\n\t<level id=\"" + (numberOfLevel + 1) + "\">\n");
		} else if(image == null && name != null) {
			//Ajout à la liste des informations basiques du niveau
			level.add("\n\t<level id=\"" + (numberOfLevel + 1) + "\" name=\"" + name + "\">\n");
		} else if(image != null && name == null) {
			//Ajout à la liste des informations basiques du niveau
			level.add("\n\t<level id=\"" + (numberOfLevel + 1) + "\" image=\"" + image +  "\">\n");
		} else {
			//Ajout à la liste des informations basiques du niveau
			level.add("\n\t<level id=\"" + (numberOfLevel + 1) + "\" image=\"" + image + "\" name=\"" + name + "\">\n");
		}
		level.add("\t\t<numberofhouses>" + numberOfHouses + "</numberofhouses>\n");
		level.add("\t\t<hasanimals>" + hasAnimals + "</hasanimals>\n");
		level.add("\t\t<constraints>\n");
		//Evaluation de chaque contrainte
		List<Constraint> unitaryConstraints = getUnitaryConstraintsFromList(constraints);
		List<Constraint> binaryConstraints = getBinaryConstraintsFromList(constraints);
		//Ecriture pour les contraintes unaires
		for(Constraint c : unitaryConstraints) {
			level.add("\t\t\t<constraintUnitary>\n");
			level.add("\t\t\t<id>" + c.getId() + "</id>\n");
			level.add("\t\t\t<house>" +(c.getId() == 1 ? 
					((IsInHouse<Entity>) c).getIndice()+1 : ((IsNotInHouse<Entity>) c).getIndice()+1) + "</house>\n");
			Entity e = c.getId() == 1 ?((IsInHouse<Entity>) c).getEntity() : ((IsNotInHouse<Entity>) c).getEntity();
			level.add("\t\t\t<entity type=\"" + e.getEntityType() +"\">" + e.getIndex()+ "</entity>\n");
			level.add("\t\t\t</constraintUnitary>\n");
		}
		//Ecriture pour les contraintes binaires
		for(Constraint c : binaryConstraints) {
			level.add("\t\t<constraintBinary>\n");
			level.add("\t\t\t<id>" + c.getId() + "</id>\n");
			Entity e1 = null;
			Entity e2 = null;
			//On récupère l'entité associée au bon numéro de contrainte
			switch(c.getId()){
			case 3 :
				e1 = ((IsNeighbourWith<Entity, Entity>) c).getFirstEntity();
				e2 = ((IsNeighbourWith<Entity, Entity>) c).getSecondEntity();
				break;
			case 4 :
				e1 = ((IsNotNeighbourWith<Entity, Entity>) c).getFirstEntity();
				e2 = ((IsNotNeighbourWith<Entity, Entity>) c).getSecondEntity();
				break;
			case 5:
				e1 = ((ToTheLeftOf<Entity, Entity>) c).getFirstEntity();
				e2 = ((ToTheLeftOf<Entity, Entity>) c).getSecondEntity();
				break;
			case 6 :
				e1 = ((IsWith<?, ?>) c).getCharacter();
				e2 = ((IsWith<?, ?>) c).getAnimal();
				break;
			case 7 :
				e1 = ((IsNotWith<?, ?>) c).getCharacter();
				e2 = ((IsNotWith<?, ?>) c).getAnimal();
				break;
			}
			level.add("\t\t\t<entity1 type=\"" + e1.getEntityType() +"\">" + e1.getIndex() +"</entity1>\n");
			level.add("\t\t\t<entity2 type=\"" + e2.getEntityType() +"\">" + e2.getIndex() +"</entity2>\n");
			level.add("\t\t</constraintBinary>\n");
		}
		level.add("\t\t</constraints>\n");
		level.add("\t</level>\n");
		return level;
		
	}
	
	/**
	 * Renvoie la liste des contraintes unaires à partir d'une liste de contraintes.
	 * @pre
	 * 		constraints != null
	 */
	private List<Constraint> getUnitaryConstraintsFromList(List<Constraint> constraints) {
		Contract.checkCondition(constraints != null, "Cannot return list of constraints, no constraints");
		List<Constraint> result = new ArrayList<Constraint>();
		for(Constraint c : constraints) {
			if(c.getId() == 1 || c.getId() == 2) {
				result.add(c);
			}
		}
		return result;
	}
	
	/**
	 * Renvoie la liste des constraintes binaires à partir d'une liste de contraintes.
	 * @pre
	 * 		constraints != null
	 */
	private List<Constraint> getBinaryConstraintsFromList(List<Constraint> constraints) {
		Contract.checkCondition(constraints != null, "Cannot return list of constraints, no constraints");
		List<Constraint> result = new ArrayList<Constraint>();
		for(Constraint c : constraints) {
			if(c.getId() != 1 && c.getId() != 2) {
				result.add(c);
			}
		}
		return result;
	}
	
	/**
	 * Charge un fichier à partir de l'attribut entityString.
	 * Met dans levelInfo le couple numéro du niveau et string décrivant le niveau.
	 * Effectue une lecture à partir d'un fichier en xml.
	 */
	private void loadLevelFile() {
		try {
			 //Obtenir la configuration du sax parser
			 SAXParserFactory spfactory = SAXParserFactory.newInstance();
			 //Obtenir une instance de l'objet parser
			 SAXParser saxParser = spfactory.newSAXParser();
			 
			 /*les trois méthodes sont déclarées dans le
			 corp du DefaltHandler*/
			 DefaultHandler handler = new DefaultHandler() {
				 
				 private boolean isLevel;
				 private boolean isItNumberOfHouses;
				 private boolean isItHasAnimals;
				 private boolean isItId;
				 private boolean isItHouse;
				 private boolean isItEntity;
				 private boolean isItEntity1;
				 private boolean isItEntity2;
				 
				 private String description = "";
				 private String typeEntity;
				 /*cette méthode est invoquée à chaque fois que parser rencontre
				 une balise ouvrante '<' */
				 public void startElement(String uri, String localName, String qName,Attributes attributes) throws SAXException{

					 if(qName.equalsIgnoreCase("level")) {
						 String id = attributes.getValue("id");
						 if(Integer.parseInt(id) != numberOfLevel + 1) {
							 System.out.println("This id " + id + " is not correct");
							 isLevel = false;
						 }
						 String image = attributes.getValue("image");
						 String name = attributes.getValue("name");
						 levelId.put(Integer.parseInt(id), new Couple<String, String>(image, name));
						 isLevel = true;
						 description += "LEVEL " + id + " : ";
						 
					 }
					 
					 isItNumberOfHouses = isCorrect(qName, "numberofhouses");					 
					 isItHasAnimals = isCorrect(qName, "hasanimals");				 
					 isItId = isCorrect(qName, "id");
					 isItEntity = isCorrect(qName, "entity"); 
					 if(isItEntity) {
						 String id = attributes.getValue("type");
						 typeEntity = id;
					 }
					 isItHouse = isCorrect(qName, "house");
					 isItEntity1 = isCorrect(qName, "entity1");
					 if(isItEntity1) {
						 String id = attributes.getValue("type");
						 typeEntity = id;
					 }
					 isItEntity2 = isCorrect(qName, "entity2");
					 if(isItEntity2) {
						 String id = attributes.getValue("type");
						 typeEntity = id;
					 }
				 }
				 
				 public void endElement(String uri, String localName, String qName) throws SAXException {
					 isLevel = !isCorrect(qName, "level");
					 if(!isLevel) {
						 levelInfo.put(numberOfLevel, description);
						 description = "";
						 synchronized(this) {
							 ++numberOfLevel;
						 }
					 }
					 isItNumberOfHouses = !isCorrect(qName, "numberofhouses");					 
					 isItHasAnimals = !isCorrect(qName, "hasanimals");				 
					 isItId = !isCorrect(qName, "id");
					 isItEntity = !isCorrect(qName, "entity"); 
					 if(isItEntity) {
						 typeEntity = "";
					 }
					 isItHouse = !isCorrect(qName, "house");
					 isItEntity1 = !isCorrect(qName, "entity1");
					 if(isItEntity1) {
						 typeEntity = "";
					 }
					 isItEntity2 = !isCorrect(qName, "entity2");
					 if(isItEntity2) {
						 typeEntity = "";
					 }
				 }
				 
				 public void characters(char ch[], int start, int length) throws SAXException {
					 if (isItNumberOfHouses) {
						 description += "HOUSES " + new String(ch, start, length) + " , ";
						 isItNumberOfHouses = false;
					}
					 if (isItHasAnimals) {
						 description += "PETS ? " + (new String(ch, start, length).equals("false") ? "0" : "1") + " ";
						 isItHasAnimals = false;
					}
					 if (isItId) {
						 description += "{CONSTRAINT: " + new String(ch, start, length) + " , ";
						 isItId = false;
					}
					 if (isItHouse) {
						 description += "HOUSE: " + new String(ch, start, length) + "; ";
						 isItHouse = false;
					}
					 if (isItEntity) {
						 description += "ENTITY: " + typeEntity +", " + new String(ch, start, length) + " }";
						 isItEntity = false;
					}
					 if (isItEntity1) {
						 description += "ENTITY1: " + typeEntity +", " + new String(ch, start, length) + " ; ";
						 isItEntity1 = false;
					}
					 if (isItEntity2) {
						 description += "ENTITY2: " + typeEntity +", " + new String(ch, start, length) + " }";
						 isItEntity2 = false;
					}
				 }
				 //OUTILS
				 private boolean isCorrect(String qName, String s) {
					 if(qName.equalsIgnoreCase(s)) {
						 return true;
					 }
					 return false;
				 }
			 };
			 if(fileString.compareTo(FILE_CLASSIC) == 0) {
				 saxParser.parse(SaveManager.class.getResourceAsStream(fileString), handler, "res/ressources_personalized/");
			 } else {
				 saxParser.parse(fileString, handler);
			 }
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

}
