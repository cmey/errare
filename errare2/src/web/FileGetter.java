package web;

/*
* FileGetter.java
*
* Created on 23 mai 2005, 13:46
*/

import java.net.URL;
import java.net.URLConnection;
import java.io.InputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
*
* @author Christophe
*/
public class FileGetter {

/** Creates a new instance of RecuperartionFile */
public FileGetter(String urlFichierDistant, String pathFichierLocal) {
//on crée l'URL pointant sur le fichier
try{
URL url = new URL(urlFichierDistant);
URLConnection urlCon = url.openConnection();
InputStream is = urlCon.getInputStream();
if(is!=null){
// On le lit completement dans un tableau de bits
byte[] data = new byte[0];
byte [] buffer = new byte [1024*2];
int taille;
while ((taille= is.read(buffer)) > 0) { // tant qu'il y a quelque chose à lire
//on alloue un nouveau tableau
byte [] data2 = new byte[data.length+taille];
// on recopie dedans les données deja lues
System.arraycopy(data,0,data2,0,data.length);
// On recopier dedans les données tout juste lues
System.arraycopy(buffer,0,data2,data.length,taille);
// ancien tableau = nouveau tableau
data = data2;
// nicolas.charlot arrange toi à mettre içi ton progressBar pour controler l'etat de chargement
}
File fichier = new File(pathFichierLocal); //mettre içi lengthnom de ton fichier
FileOutputStream fos = new FileOutputStream(fichier);
fos.write(data);
fos.flush();
fos.close();
}
}catch(java.io.IOException ex){
ex.printStackTrace();
}

}
public static void main(String[] fichier){
FileGetter test = new FileGetter("http://www.vetmed.wisc.edu/dms/fapm/personnel/tom_b/2004-lion_small.jpg","C:\\monJar.jpg");
}
}