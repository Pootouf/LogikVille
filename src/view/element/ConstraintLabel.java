package view.element;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import util.Contract;

@SuppressWarnings("serial")
public class ConstraintLabel extends JComponent {
	
	public static final String SPRITE_LOC = "res/ressources_personalized/sprites/constraint/";
	
	public static final int NUMBER_OF_CONSTRAINT = 7;
	
	public static final Color COLOR = new Color(255, 128, 0);
	
	//ATTRIBUTS
	
	private int numberOfEntity;
	private int constraintId;
	private int houseSelect;
	private int size;
	
	
	//CONSTRUCTEURS
	
	public ConstraintLabel(int numberOfEntity, int constraintId, int size) {
		this(numberOfEntity, validateCondition(constraintId), size, -1);
	}
	
	public ConstraintLabel(int numberOfEntity, int constraintId, int size, int houseSelect) {
		Contract.checkCondition(constraintId <= NUMBER_OF_CONSTRAINT, "Contrainte invalide");
		this.setLayout(new BorderLayout());
		this.numberOfEntity = numberOfEntity;
		this.constraintId = constraintId;
		this.houseSelect = houseSelect;
		this.size = size;
		if(constraintId == 1 || constraintId == 2) {
			createConstraintUnitary();
		} else {
			JPanel p = new JPanel(new GridLayout(1, 1));
			ImageIcon imageIcon = new ImageIcon(SPRITE_LOC + "constraint" + constraintId +".png");
			ImageIcon scaledImage = new ImageIcon(imageIcon.getImage().getScaledInstance(-1, (int) (size / 1.2), Image.SCALE_SMOOTH));
			
			p.add(new JLabel(scaledImage) {
				@Override
				public Dimension getPreferredSize() {
					return new Dimension(size, size);
				}
				
			});
			p.setOpaque(false);
			add(p, BorderLayout.CENTER);
		}
	}
	
	//REQUETES
	
	@Override
	public Dimension getMaximumSize() {
		return new Dimension(Integer.MAX_VALUE, getPreferredSize().height);
	}
	
	
	//COMMANDES
	
	public void addEntityLeft(EntityLabel entity) {
		Contract.checkCondition(entity != null, "Entity null");
		add(entity, BorderLayout.WEST);
	}
	
	public void addEntityRight(EntityLabel entity) {
		Contract.checkCondition(entity != null, "Entity null");
		add(entity, BorderLayout.EAST);
	}
	
	//OUTILS
	
	private void createConstraintUnitary() {
		JPanel p = new JPanel(new GridLayout(1, numberOfEntity));
		{ //--
			p.setOpaque(false);
			for(int i = 0; i < numberOfEntity; i++) {
				JLabel jb = null;
				if(i == houseSelect) {
					ImageIcon imageIcon = new ImageIcon(SPRITE_LOC + "constraint" + constraintId +".png");
					ImageIcon scaledImage = new ImageIcon(imageIcon.getImage().getScaledInstance(-1, (int) (size / 1.2), Image.SCALE_SMOOTH));
					jb = new JLabel(scaledImage);
				} else {
					ImageIcon imageIcon = new ImageIcon(SPRITE_LOC + "constraint_default.png");
					ImageIcon scaledImage = new ImageIcon(imageIcon.getImage().getScaledInstance(-1, (int) (size / 1.2), Image.SCALE_SMOOTH));
					jb = new JLabel(scaledImage);
				}
				p.add(jb);
			}
		} //--
		add(p, BorderLayout.CENTER);
	}
	
	private static int validateCondition(int constraintId) {
		Contract.checkCondition(constraintId != 1 && constraintId != 2, "Invalid id");
		return constraintId;
	}
}
