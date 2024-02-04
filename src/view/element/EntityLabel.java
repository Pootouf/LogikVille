package view.element;

import javax.swing.Icon;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class EntityLabel extends JLabel {

	private int index;
	
	public EntityLabel(Icon image, int index) {
		super(image);
		this.index = index;
	}
	
	public int getIndex() {
		return index;
	}
	
}
