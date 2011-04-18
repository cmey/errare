package mapEditor;
import java.io.File;

import javax.swing.JFileChooser;

@SuppressWarnings("serial")
public class OuvrirFichier extends JFileChooser{
	
	public OuvrirFichier(Principale caller){
		
		// Note: source for ExampleFileFilter can be found in FileChooserDemo,
	    // under the demo/jfc directory in the JDK.
	    MyFileFilter filter = new MyFileFilter();
	    filter.addExtension("md2");
	    filter.setDescription("MD2 Models");
	    this.setCurrentDirectory(new File("data/"));
	    this.setFileFilter(filter);
	    int returnVal = this.showOpenDialog(caller);
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
	//    	BaseGrid model = new BaseGrid("data/"+this.getSelectedFile().getName());
	//		Main.graphicsEngine.setHeightMap(model.getGrid(), (int) model.getSize());  	
	    }
	}

}
