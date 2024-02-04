package view.element;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

@SuppressWarnings("serial")
/*
 * Une sorte de JPanel pouvant contenir deux composants, un en haut et un en bas
 */
public class GraphicHouse extends JPanel {
	
	private static final int MAX_CHILD = 2;
	
	private static Image sprite;
	static {
		sprite = new ImageIcon("res/ressources_personalized/sprites/gui/house.png").getImage();
	}
	
	private boolean componentNorthAlreadyInUse;
	private boolean componentSouthAlreadyInUse;
	
	private Component componentNorth;
	private Component componentSouth;
	
	private int index;
	
	//CONSTRUCTEURS
	
	public GraphicHouse(int sx, int sy, int index) {
		this.setOpaque(false);
		this.setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension(sx, sy));
		this.setMaximumSize(new Dimension(sx, sy));
		this.setMinimumSize(new Dimension(sx, sy));
		sprite = sprite.getScaledInstance(sx, sy, 0);
		this.setBorder(BorderFactory.createEmptyBorder(sy / 3, sx/30, sy/6, 0));
		this.index = index;
	}
	
	//REQUETES
	
	public boolean isValidAddition(boolean isSouth) {
		return this.getComponentCount() < MAX_CHILD && ((isSouth && !componentSouthAlreadyInUse) || (!isSouth && !componentNorthAlreadyInUse));
	}
	
	public boolean isComponentNorthAlreadyInUse() {
		return componentNorthAlreadyInUse;
	}
	
	public boolean isComponentSouthAlreadyInUse() {
		return componentSouthAlreadyInUse;
	}
	
	public int getIndex() {
		return index;
	}
	
	public Component getComponentNorth() {
		return componentNorth;
	}
	
	public Component getComponentSouth() {
		return componentSouth;
	}
	
	//COMMANDES
	
	public void setComponentSouthAlreadyInUse(boolean value) {
		componentSouthAlreadyInUse = value;
	}
	
	public void setComponentNorthAlreadyInUse(boolean value) {
		componentNorthAlreadyInUse = value;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(sprite, 0, 0, this.getWidth(), this.getHeight(), null);
	}
	
	public void addComponentNorth(Component c) {
		componentNorthAlreadyInUse = true;
		componentNorth = c;
		this.add(c, BorderLayout.NORTH);
	}
	
	public void addComponentSouth(Component c) {
		componentSouthAlreadyInUse = true;
		componentSouth = c;
		this.add(c, BorderLayout.SOUTH);
	}
}
