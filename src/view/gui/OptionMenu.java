package view.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.plaf.metal.MetalCheckBoxIcon;

import main.LogikVille;
import model.LogikVilleModel;
import view.element.ClassicButton;
import view.element.RoundPanel;

@SuppressWarnings("serial")
public class OptionMenu extends AbstractMenu {
	
	public static final Dimension DEFAULT_RESOLUTION = resolutionBuilder()[0];

	//ATTRIBUTS
	
	private JLabel background;
	
	private JCheckBox fullscreenBox;
	private JComboBox<Dimension> resolutionBox;
	private JComboBox<String> themeBox;
	
	private ClassicButton returnButton;
	private ClassicButton validateButton;
	private ClassicButton themeBuilderButton;
	
	
	//CONSTRUCTEUR
	
	public OptionMenu(LogikVille lv) {
		super(lv);
		createView();
		placeComponents();
		createController();
		refresh(this);
	}

	//COMMANDES
	
	public void resetThemeList() {
		ComboBoxModel<String> model = new DefaultComboBoxModel<String>(themeList());
		themeBox.setModel(model);
	}
	
	public void setSelectedTheme(String s) {
		themeBox.getModel().setSelectedItem(s);
	}
	
	public void setFullScreenBox(boolean b) {
		fullscreenBox.setSelected(b);
	}
	
	public void setSelectedDimension(Dimension d) {
		resolutionBox.getModel().setSelectedItem(d);
	}
	
	
	
	//OUTILS
	
	protected void refresh(OptionMenu optionMenu) {
		optionMenu.setFullScreenBox(getApp().isFullScreenActivated());
		optionMenu.setSelectedTheme(getApp().getSelectedTheme());
		optionMenu.setSelectedDimension(getApp().getSelectedResolution());
	}

	private void createView() {
		this.setBackground(Color.GRAY);
		
		ImageIcon imageIcon = new ImageIcon(SPRITE_LOC + "background-2.png");
		Icon scaledImage = new ImageIcon(imageIcon.getImage().getScaledInstance(getApp().getScreenPixelX(), getApp().getScreenPixelY(), Image.SCALE_SMOOTH));
		background = new JLabel(scaledImage);
		background.setLayout(new BoxLayout(background, BoxLayout.Y_AXIS));
		background.setBorder(BorderFactory.createEmptyBorder(getResizeY(30), getResizeX(150), getResizeY(30), getResizeX(150)));
		background.setPreferredSize(new Dimension(getApp().getScreenPixelX(), getApp().getScreenPixelY()));
		
		
		returnButton = new ClassicButton("Retour");
		returnButton.setFont(FONT.deriveFont(Font.BOLD, getFontSize(25)));
		returnButton.setPreferredSize(new Dimension(getResizeX(300), returnButton.getPreferredSize().height));
		validateButton = new ClassicButton("Appliquer");
		validateButton.setFont(FONT.deriveFont(Font.BOLD, getFontSize(25)));
		themeBuilderButton = new ClassicButton("Créateur de thèmes");
		themeBuilderButton.setFont(FONT.deriveFont(Font.BOLD, getFontSize(25)));
		themeBuilderButton.setPreferredSize(new Dimension(getResizeX(300), themeBuilderButton.getPreferredSize().height));
		
		fullscreenBox = new JCheckBox();
		fullscreenBox.setOpaque(false);
		fullscreenBox.setIcon(new MetalCheckBoxIcon () {
		    protected int getControlSize() { return getResizeX(20); }
		});
		
		resolutionBox = new JComboBox<Dimension>(resolutionBuilder());
		resolutionBox.setFont(FONT.deriveFont(Font.BOLD, getFontSize(30)));
		
		themeBox = new JComboBox<String>(themeList());
		themeBox.setFont(FONT.deriveFont(Font.BOLD, getFontSize(30)));
	}
	
	private void placeComponents() {
		{ //--
			JPanel p = new JPanel(new BorderLayout());
			{ //--
				p.setOpaque(false);
				JPanel q = new JPanel();
				{
					q.setOpaque(false);
					JLabel jl = new JLabel("Options");
					jl.setFont(FONT.deriveFont(Font.BOLD, getFontSize(60)));
					q.add(jl);
				}
				p.add(q, BorderLayout.NORTH);
				Box b = Box.createVerticalBox();
				{ //--
					b.add(Box.createVerticalGlue());
					q = new RoundPanel(COLOR_TRANSPARENCY, new GridLayout(3, 1));
					{ //--
						q.setOpaque(false);
						JPanel r = new JPanel();
						{ //--
							r.setOpaque(false);
							JLabel jl = new JLabel("Plein écran : ");
							jl.setFont(FONT.deriveFont(Font.BOLD, getFontSize(30)));
							r.add(jl);
							r.add(fullscreenBox);
						} //--
						q.add(r);
						r = new JPanel();
						{ //--
							r.setOpaque(false);
							JLabel jl = new JLabel("Résolution : ");
							jl.setFont(FONT.deriveFont(Font.BOLD, getFontSize(30)));
							r.add(jl);
							r.add(resolutionBox);
						} //--
						q.add(r);
						r = new JPanel();
						{ //--
							r.setOpaque(false);
							JLabel jl = new JLabel("Charger un thème : ");
							jl.setFont(FONT.deriveFont(Font.BOLD, getFontSize(30)));
							r.add(jl);
							r.add(themeBox);
						} //--
						q.add(r);
					} //--
					b.add(q);
					b.add(Box.createVerticalGlue());
				} //--
				p.add(b, BorderLayout.CENTER);
				q = new JPanel(new BorderLayout());
				{ //--
					q.setOpaque(false);
					q.add(themeBuilderButton, BorderLayout.WEST);
					q.add(validateButton, BorderLayout.CENTER);
					q.add(returnButton, BorderLayout.EAST);
				} //--
				p.add(q, BorderLayout.SOUTH);
			} //--
			background.add(p);
		} //--
		this.add(background);
	}
	
	private void createController() {
		addGameStateListener(returnButton, LogikVille.TITLE_STATE);
		validateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getApp().setSelectedTheme((String) themeBox.getSelectedItem());
				getApp().setSelectedResolution((Dimension) resolutionBox.getSelectedItem());
				getApp().getModel().setEntityTheme("res/ressources_personalized/themes/" + themeBox.getSelectedItem() + "/entity.txt");
				getApp().setFrameSize((Dimension) resolutionBox.getSelectedItem());
				getApp().setFullscreen(fullscreenBox.isSelected());
				getApp().resetFrame();
				
				OptionMenu menu = (OptionMenu) getApp().getMenu(LogikVille.OPTION_STATE);
				
				refresh(menu);
			}
		});
		
		addGameStateListener(themeBuilderButton, LogikVille.THEME_STATE, new Runnable() {
			@Override
			public void run() {
				CreatorThemeMenu tm = (CreatorThemeMenu) getApp().getMenu(LogikVille.THEME_STATE);
				tm.setInfoMenu(LogikVilleModel.CLASSIC_THEME_NAME);
			}
		});
	}
	
	private String[] themeList() {
		File f = new File("res/ressources_personalized/themes/");
		return f.list();
	}

	private static Dimension[] resolutionBuilder() {
		return new Dimension[] {
				new Resolution(-1, -1) {
					@Override
					public String toString() {
						return "Automatique";
					}
				},
				new Resolution(3840, 2160),
				new Resolution(2560, 1440),
				new Resolution(1920, 1080),
				new Resolution(1366, 768),
				new Resolution(1538, 864),
				new Resolution(1440, 900),
				new Resolution(1280, 720),
				new Resolution(1600, 900),
				new Resolution(800, 600),
				new Resolution(1024, 768)
		};
	}
	
	public static class Resolution extends Dimension {
		public Resolution(int i, int j) {
			super(i, j);
		}

		@Override
		public String toString() {
			return width + "x" + height;
		}
	}
	
}
