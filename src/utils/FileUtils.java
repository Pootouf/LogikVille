package utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

public class FileUtils {
	public static File getImageWithChooser() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		JFileChooser jfc = new JFileChooser();
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		int response = jfc.showOpenDialog(null);
		
		if(response == JFileChooser.APPROVE_OPTION) {
			File image = jfc.getSelectedFile();
			String mimetype = null;
			try {
				mimetype = Files.probeContentType(image.toPath());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if (mimetype == null || !mimetype.split("/")[0].equals("image")) {
				JOptionPane.showMessageDialog(null, "Impossible de charger le fichier, ce n'est pas une image !", "Erreur de chargement !", JOptionPane.ERROR_MESSAGE);
			    return null;
			}
			return image;
		}
		return null;
	}
}
