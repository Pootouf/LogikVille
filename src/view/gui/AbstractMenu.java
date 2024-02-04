package view.gui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;

import main.LogikVille;
import model.constraints.Constraint;
import model.constraints.IsInHouse;
import model.constraints.IsNotInHouse;
import model.constraints.IsNeighbourWith;
import model.constraints.IsNotNeighbourWith;
import model.constraints.ToTheLeftOf;
import model.constraints.IsWith;
import model.constraints.IsNotWith;
import model.entity.Entity;
import view.element.ConstraintLabel;
import view.element.EntityLabel;

@SuppressWarnings("serial")
public class AbstractMenu extends JPanel{
	
	public static final Color COLOR_TRANSPARENCY;
	static {
		Color tmp = new Color(242, 192, 134);
		COLOR_TRANSPARENCY = new Color(tmp.getRed(), tmp.getGreen(), tmp.getBlue(), 200);
	}
	
	//SPRITE SETTINGS
	public static final String SPRITE_LOC = "res/ressources_personalized/sprites/gui/";
	public static final Font FONT = new Font("Helvetica", 0, 0);
	
		
	//ATTRIBUTS
	
	private LogikVille app;
	
	
	//CONSTRUCTEURS
	public AbstractMenu(LogikVille lv) {
		this.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		app = lv;
	}
	
	
	
	
	//REQUETES
	
	protected LogikVille getApp() {
		return app;
	}
	
	
	//OUTILS
	
	protected int getResizeX(float f) {
		return (int)((app.getScreenPixelX() * f) / 1280f);
	}
	
	protected int getResizeY(float f) {
		return (int)((app.getScreenPixelY() * f) / 720f);
	}
	
	protected int getFontSize(float f) {
		return (app.getScreenPixelX() < app.getScreenPixelY() ? getResizeX(f) : getResizeY(f));
	}
	
	protected void addGameStateListener(JButton button, int gameState) {
		addGameStateListener(button, gameState, null);
	}
	
	protected void addGameStateListener(JButton button, int gameState, Runnable runnable) {
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				button.setFocusPainted(false);
				app.setGamestate(gameState);
				if(runnable != null) {
					runnable.run();
				}
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	protected ConstraintLabel buildConstraintLabel(Constraint c, int numberOfEntity, int size) {
		ConstraintLabel cl = null;
		switch(c.getId()) {
			case 1:
				ImageIcon imageIcon = ((IsInHouse<?>)c).getEntity().getPicture();
				ImageIcon scaledImage = new ImageIcon(imageIcon.getImage().getScaledInstance(getResizeX(size), getResizeX(size), Image.SCALE_DEFAULT));
				cl = new ConstraintLabel(numberOfEntity, c.getId(), getResizeY(size), ((IsInHouse<Entity>) c).getIndice());
				cl.addEntityLeft(new EntityLabel(scaledImage, ((IsInHouse<Entity>) c).getEntity().getIndex()));
				break;
			case 2:
				imageIcon = ((IsNotInHouse<?>)c).getEntity().getPicture();
				scaledImage = new ImageIcon(imageIcon.getImage().getScaledInstance(getResizeX(size), getResizeX(size), Image.SCALE_DEFAULT));
				cl = new ConstraintLabel(numberOfEntity, c.getId(), getResizeY(size), ((IsNotInHouse<Entity>) c).getIndice());
				cl.addEntityLeft(new EntityLabel(scaledImage, ((IsNotInHouse<Entity>) c).getEntity().getIndex()));
				break;
			case 3:
				imageIcon = ((IsNeighbourWith<?, ?>)c).getFirstEntity().getPicture();
				scaledImage = new ImageIcon(imageIcon.getImage().getScaledInstance(getResizeX(size), getResizeX(size), Image.SCALE_DEFAULT));
				cl = new ConstraintLabel(numberOfEntity, c.getId(), getResizeY(size));
				cl.addEntityLeft(new EntityLabel(scaledImage, ((IsNeighbourWith<Entity, Entity>) c).getFirstEntity().getIndex()));
				imageIcon = ((IsNeighbourWith<?, ?>)c).getSecondEntity().getPicture();
				scaledImage = new ImageIcon(imageIcon.getImage().getScaledInstance(getResizeX(size), getResizeX(size), Image.SCALE_DEFAULT));
				cl.addEntityRight(new EntityLabel(scaledImage, ((IsNeighbourWith<Entity, Entity>) c).getSecondEntity().getIndex()));
				break;
			case 4:
				imageIcon = ((IsNotNeighbourWith<?, ?>)c).getFirstEntity().getPicture();
				scaledImage = new ImageIcon(imageIcon.getImage().getScaledInstance(getResizeX(size), getResizeX(size), Image.SCALE_DEFAULT));
				cl = new ConstraintLabel(numberOfEntity, c.getId(), getResizeY(size));
				cl.addEntityLeft(new EntityLabel(scaledImage, ((IsNotNeighbourWith<Entity, Entity>) c).getFirstEntity().getIndex()));
				imageIcon = ((IsNotNeighbourWith<?, ?>)c).getSecondEntity().getPicture();
				scaledImage = new ImageIcon(imageIcon.getImage().getScaledInstance(getResizeX(size), getResizeX(size), Image.SCALE_DEFAULT));
				cl.addEntityRight(new EntityLabel(scaledImage, ((IsNotNeighbourWith<Entity, Entity>) c).getSecondEntity().getIndex()));
				break;
			case 5:
				imageIcon = ((ToTheLeftOf<?, ?>)c).getFirstEntity().getPicture();
				scaledImage = new ImageIcon(imageIcon.getImage().getScaledInstance(getResizeX(size), getResizeX(size), Image.SCALE_DEFAULT));
				cl = new ConstraintLabel(numberOfEntity, c.getId(), getResizeY(size));
				cl.addEntityLeft(new EntityLabel(scaledImage, ((ToTheLeftOf<Entity, Entity>) c).getFirstEntity().getIndex()));
				imageIcon = ((ToTheLeftOf<?, ?>)c).getSecondEntity().getPicture();
				scaledImage = new ImageIcon(imageIcon.getImage().getScaledInstance(getResizeX(size), getResizeX(size), Image.SCALE_DEFAULT));
				cl.addEntityRight(new EntityLabel(scaledImage, ((ToTheLeftOf<Entity, Entity>) c).getSecondEntity().getIndex()));
				break;
			case 6:
				imageIcon = ((IsWith<?, ?>)c).getCharacter().getPicture();
				scaledImage = new ImageIcon(imageIcon.getImage().getScaledInstance(getResizeX(size), getResizeX(size), Image.SCALE_DEFAULT));
				cl = new ConstraintLabel(numberOfEntity, c.getId(), getResizeY(size));
				cl.addEntityLeft(new EntityLabel(scaledImage, ((IsWith<?, ?>) c).getCharacter().getIndex()));
				imageIcon = ((IsWith<?, ?>)c).getAnimal().getPicture();
				scaledImage = new ImageIcon(imageIcon.getImage().getScaledInstance(getResizeX(size), getResizeX(size), Image.SCALE_DEFAULT));
				cl.addEntityRight(new EntityLabel(scaledImage, ((IsWith<?, ?>) c).getAnimal().getIndex()));
				break;
			case 7:
				imageIcon = ((IsNotWith<?, ?>)c).getCharacter().getPicture();
				scaledImage = new ImageIcon(imageIcon.getImage().getScaledInstance(getResizeX(size), getResizeX(size), Image.SCALE_DEFAULT));
				cl = new ConstraintLabel(numberOfEntity, c.getId(), getResizeY(size));
				cl.addEntityLeft(new EntityLabel(scaledImage, ((IsNotWith<?, ?>) c).getCharacter().getIndex()));
				imageIcon = ((IsNotWith<?, ?>)c).getAnimal().getPicture();
				scaledImage = new ImageIcon(imageIcon.getImage().getScaledInstance(getResizeX(size), getResizeX(size), Image.SCALE_DEFAULT));
				cl.addEntityRight(new EntityLabel(scaledImage, ((IsNotWith<?, ?>) c).getAnimal().getIndex()));
				break;
		}
		return cl;
	}
	
	protected int showStylizedConfirmDialogue(String message) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		int response = JOptionPane.showConfirmDialog(null, message);
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return response;
	}
	
	protected void showStylizedMessageDialogue(String message, String title) {
		showDialogue(message, title, JOptionPane.INFORMATION_MESSAGE);
	}
	
	protected void showStylizedErrorDialogue(String message, String title) {
		showDialogue(message, title, JOptionPane.ERROR_MESSAGE);
	}
	
	private void showDialogue(String message, String title, int type) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		JOptionPane.showMessageDialog(null, message, title, type);
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
