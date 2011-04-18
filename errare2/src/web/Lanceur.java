package web;

import java.io.File;
import javax.swing.JOptionPane;
import org.jdesktop.jdic.desktop.Desktop;
import org.jdesktop.jdic.desktop.DesktopException;

/**
 * Cette classe est un lanceur pour votre application, elle va rechercher si une 
 * nouvelle version a �t� t�l�charg�e et le cas �ch�ant, va remplacer l'actuelle 
 * par la nouvelle.
 * @author Wichtounet
 *
 */
public class Lanceur {
	//Variables contenant les noms des fichiers � charger
	private static final String pathCurrent = File.separator + "Actual.jar";
	private static final String pathNew = File.separator + "New.jar";
	private static final String pathOld = File.separator + "Old.jar";
	
	//Variable contenant le nom du r�p�rtoire courant
	private static final String currentFolder = System.getProperty("user.dir");
	
	/**
	 * Classe main du lanceur, elle s'occuppe de tout
	 * @param args
	 */
	public static void main(String[] args) {
		File current = new File(currentFolder + pathCurrent);
		File newVersion = new File(currentFolder + pathNew);
		File old = new File(currentFolder + pathOld);
		
		//Si une nouvelle version a �t� t�l�charg�e
		if(newVersion.exists()){
			//On renomme la version actuelle (donc la vielle)
			current.renameTo(old);
			
			//On renomme la nouvelle avec le nom de l'ancienne
			newVersion.renameTo(current);
			
			//On supprimme l'ancienne
			old.delete();
			
			try {
				//On lance le nouveau fichier .jar
				Desktop.open(current);
			} catch (DesktopException e) {
				e.printStackTrace();
			}
		//S'il n'y a qu'une version courante et pas de nouvelles
		}else if(current.exists()){
			try {
				//On lance le jar actuel
				Desktop.open(current);
			} catch (DesktopException e) {
				e.printStackTrace();
			}
		//Si aucun fichier n'existe
		}else{
			//On avertit d'un probl�me
			JOptionPane.showMessageDialog(null,"Aucun fichier jar � lancer...");
		}
	}
}