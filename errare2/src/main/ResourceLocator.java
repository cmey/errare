package main;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;

/**
 *
 * @author christophe
 */
public class ResourceLocator {

    static public InputStream getRessourceAsStream(String fn){
/*        if(!fn.startsWith("/"))
            fn = "/"+fn;*/
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(fn);
    }
    
    static public URL getRessource(String fn) {
/*        if(!fn.startsWith("/"))
            fn = "/"+fn;*/
        return Thread.currentThread().getContextClassLoader().getResource(fn);
    }
    
    @Deprecated
    static public File getFile(String fn) {        
        try{
            InputStream is = getRessourceAsStream(fn);
            DataInputStream dis = new DataInputStream(is);
            
            File tempFile = File.createTempFile("errare",".log");
            FileOutputStream dos = new FileOutputStream(tempFile);
            int buf=0;
            while((buf=dis.read())!=-1){
                dos.write(buf);
            }
            dis.close();
            dos.close();
            return tempFile;
        }catch(Exception e){
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }
}
