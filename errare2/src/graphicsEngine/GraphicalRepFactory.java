package graphicsEngine;

import geom.Sphere;
import java.io.IOException;
import java.util.Hashtable;
import java.util.LinkedList;

import loader3DS.ModelLoader;
import logger.Logger;

public class GraphicalRepFactory {

	transient static protected final Hashtable<String, GraphicalRep> collector = new Hashtable<String, GraphicalRep>();
	transient static protected LinkedList<GraphicalRep> tofreeList = new LinkedList<GraphicalRep>();
	
	
	/** Memory and speed optimisation: if the model has already
	 *  been loaded in the past, it will use that model again
	 * (avoids loading the same file multiple times).
	 **/
	public static GraphicalRep load(String path) throws IOException{
		if(path==null){
			throw new IOException("The path to the model file is null.");
		}
		// already loaded ?
		GraphicalRep ret = collector.get(path);
		if(ret != null){
			if(GraphicsEngine.DEBUG) Logger.printINFO("("+path+") GraphicalRep optimisation stage avoided one GraphicalRep load!");
			return ret.collectorClone();
		}else{
			String ext = path.substring(path.length()-4,path.length());
			GraphicalRep newrep;
			try{
				if(ext.equals(".3ds")){
					ModelLoader loader = new ModelLoader(path);
					loader.loadObject();
					newrep = loader.getModel3D();
					collector.put(path, newrep);
					return newrep;
				}
				else if(ext.equals(".md2")){
					newrep = MD2Factory.loadMD2(path);
					collector.put(path, newrep);
					return newrep;
				}
				else{ // not .3ds nor .md2
					Logger.printERROR("("+path+") Unsupported file extension");
					Logger.printERROR("PANIC !");
					System.exit(1); //TODO: etre moins violent et ne pas afficher les objets en question (pas facile a faire)
				}
			}catch(IOException e){
				Logger.printERROR("IO/Error reading model "+path+" : "+e);
				System.exit(1); //TODO: etre moins violent et ne pas afficher les objets en question (pas facile a faire)
			}
			
		return null;
		}
		
	}
	
}
