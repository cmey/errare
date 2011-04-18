package web;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.swing.JOptionPane;
import org.jdesktop.jdic.desktop.Desktop;
import org.jdesktop.jdic.desktop.DesktopException;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;


/**
 * Cette classe permet de faire une mise � jour de votre application
 * @author Wichtounet
 *
 */
public class Updater {
	//Chemin vers le lanceur
	private String lanceurPath = "Chemin vers lanceur";
	
	//Chemin vers le fichier xml 
	private String xmlPath = "Lien vers fichier xml";
	
	//Version actuelle
	private String version = "XXX";
	
	//Document xml
	private Document xmlDocument = null;
	
	//Variable contenant le nom du r�p�rtoire courant
	private String currentFolder = System.getProperty("user.dir");
	
	/**
	 * Cette m�thode permet de mettre � jour votre programme, elle va chercher 
	 * sur internet la derni�re version disponible et effectue la mise � jour 
	 * selon le consentement de l'utilisateur
	 *
	 */
	public void update(){
		ArrayList<String> versions = getVersions();
		
		//Si la version est nulle
		if(versions.size() == 0){
			JOptionPane.showMessageDialog(null,"Impossible de se connecter au service, v�rifiez votre " +
				"connection internet");
		}else{
			//Si la derni�re version n'est pas la m�me que l'actuelle
			if(!versions.get(versions.size() - 1).equals(version)){

				String versionChoisie = (String)JOptionPane.showInputDialog(null,"Choississez la version � installer","Versions disponibles",JOptionPane.QUESTION_MESSAGE,
						null,versions.toArray(),versions.get(versions.size() - 1));
				
				//S'il veut la t�l�charger
				if(versionChoisie != ""){					
					Element racine = xmlDocument.getRootElement();
										
					//On liste toutes les versions
					List listVersions = racine.getChildren("version");
					Iterator iteratorVersions = listVersions.iterator();
					
					//On parcourt toutes les versions
					while(iteratorVersions.hasNext()){
						Element version = (Element)iteratorVersions.next();
						
						Element elementNom = version.getChild("nom");
						
						//Si c'est la bonne version, on t�l�charge tous ses fichiers
						if(elementNom.getText().equals((String)versionChoisie)){
							Element elementFiles = version.getChild("files");
							
							//On liste tous les fichiers d'une version
							List listFiles = elementFiles.getChildren("file");
							Iterator iteratorFiles = listFiles.iterator();
							
							//On parcours chaque fichier de la version
							while(iteratorFiles.hasNext()){
								Element file = (Element)iteratorFiles.next();
								
								//On t�l�charge le fichier
								downloadFile(file.getChildText("url"),currentFolder + 
										File.separator + file.getChildText("destination"));
							}
							
							break;
						}
					}
					
					JOptionPane.showMessageDialog(null,"La nouvelle version a �t� t�l�charg�e, "  + 
						"le programme va �tre relanc�");
					
					File lanceur = new File(lanceurPath);
					
					try {
						//On lance le lanceur
						Desktop.open(lanceur);
						
						//On quitte le programme				
						System.exit(0);
					} catch (DesktopException e) {
						JOptionPane.showMessageDialog(null,"Impossible de relancer le programme");
					}
				}
			}
			else{
				JOptionPane.showMessageDialog(null,"Pas de nouvelles version disponible pour le moment");
			}
		}
	}

	/**
	 * Cette m�thode va chercher sur internet les versions disponibles pour l'application
	 * @return les versions disponibles
	 */
	private ArrayList<String> getVersions(){
		ArrayList<String> versions = new ArrayList<String>();
		
		try {
			URL xmlUrl = new URL(xmlPath);
			
			//On ouvre une connections ur la page
			URLConnection urlConnection = xmlUrl.openConnection();
			urlConnection.setUseCaches(false);
			
			//On se connecte sur cette page
			urlConnection.connect();
			
			//On r�cup�re le fichier XML sous forme de flux
			InputStream stream = urlConnection.getInputStream();
						
			SAXBuilder sxb = new SAXBuilder();
						
			//On cr�e le document xml avec son flux
			try {xmlDocument = sxb.build(stream);
			} catch (JDOMException e) {e.printStackTrace();
			} catch (IOException e) {e.printStackTrace();}
			
			//On r�cup�re la racine
			Element racine = xmlDocument.getRootElement();
			
			//On liste toutes les versions
			List listVersions = racine.getChildren("version");
			Iterator iteratorVersions = listVersions.iterator();
			
			//On parcourt toutes les versions
			while(iteratorVersions.hasNext()){
				Element version = (Element)iteratorVersions.next();
				
				Element elementNom = version.getChild("nom");
				
				versions.add(elementNom.getText());
			}
			
			//On trie la liste
			Collections.sort(versions);
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return versions;
	}
	
	/**
	 * Cette m�thode t�l�charge une fichier sur internet et le stocke en local
	 * @param filePath, chemin du fichier � t�l�charger
	 * @param destination, chemin du fichier en local
	 */
	private void downloadFile(String filePath, String destination) { 
		URLConnection connection = null;
		InputStream is = null;
		FileOutputStream destinationFile = null;
		
		try { 
			//On cr�e l'URL
	        URL url = new URL(filePath);

			//On cr�e une connection vers cet URL
			connection = url.openConnection( );
	        
			//On r�cup�re la taille du fichier
			int length = connection.getContentLength();

			//Si le fichier est inexistant, on lance une exception
			if(length == -1){
				throw new IOException("Fichier vide");
	       	}

			//On r�cup�re le stream du fichier
			is = new BufferedInputStream(connection.getInputStream());

			//On pr�pare le tableau de bits pour les donn�es du fichier
			byte[] data = new byte[length];

			//On d�clare les variables pour se retrouver dans la lecture du fichier
			int currentBit = 0;
			int deplacement = 0;
			
			//Tant que l'on n'est pas � la fin du fichier, on r�cup�re des donn�es
			while(deplacement < length){
				currentBit = is.read(data, deplacement, data.length-deplacement);	
				if(currentBit == -1)break;	
				deplacement += currentBit;
			}

			//Si on est pas arriv� � la fin du fichier, on lance une exception
			if(deplacement != length){
				throw new IOException("Le fichier n'a pas �t� lu en entier (seulement " 
					+ deplacement + " sur " + length + ")");
			}		
		
			//On cr�e un stream sortant vers la destination
			destinationFile = new FileOutputStream(destination); 

			//On �crit les donn�es du fichier dans ce stream
			destinationFile.write(data);

			//On vide le tampon et on ferme le stream
			destinationFile.flush();

	      } catch (MalformedURLException e) { 
	    	  System.err.println("Probl�me avec l'URL : " + filePath); 
	      } catch (IOException e) { 
	        e.printStackTrace();
	      } finally{
	    	  try {
	    		  is.close();
				  destinationFile.close();
	    	  } catch (IOException e) {
	    		  e.printStackTrace();
	    	  }
	      }
	}
}