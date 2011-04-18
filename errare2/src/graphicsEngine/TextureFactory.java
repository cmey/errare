package graphicsEngine;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Vector;

import javax.media.opengl.GL;
import logger.Logger;

public class TextureFactory {

    transient static protected Hashtable<byte[], Texture> texturecollector = new Hashtable<byte[], Texture>();
    transient static protected LinkedList<Texture> tofreeList = new LinkedList<Texture>();
    transient static protected MessageDigest md;

    /** Memory and speed optimisation: if the texture has already
     *  been loaded in the past (based on a hashcode signature of the file), it will use that texture again
     *  (avoids loading the same file multiple times).
     **/
    public static Texture loadTexture(String filename) throws IOException {
        String ext = filename.substring(filename.length() - 4, filename.length());
        if (ext.equalsIgnoreCase(".tga")) {
            ext = ".png";
            filename = filename.substring(0, filename.length() - 4) + ext;
        }
        if (ext.equalsIgnoreCase(".jpg") || ext.equalsIgnoreCase(".png") || ext.equalsIgnoreCase(".bmp")) {
            
            // get access to file
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(filename);
            
            if (null == is) {
                throw new IOException("File not found: " + filename);
            }
            Vector<Byte> file = new Vector<Byte>();
            int v = 0;
            while ((v = is.read()) != -1) {
                file.add((byte) v);
            }
            is.close();
            try {
                md = MessageDigest.getInstance("MD5");

                md.reset();
                byte[] f = new byte[file.size()];
                for (int i = 0; i < file.size(); i++) {
                    f[i] = file.get(i);
                }
                md.update(f);
                byte[] digest = md.digest();
                // already loaded ?
                Texture ret = texturecollector.get(digest);
                if (ret != null) {
                    if (GraphicsEngine.DEBUG) {
                        Logger.printINFO("(" + filename + ") Texture optimisation stage avoided one Texture load!");
                    }
                    return ret;
                } else {
                    Texture newtexture;
                    try {
                        newtexture = new Texture(filename);
                        texturecollector.put(digest, newtexture);
                        return newtexture;
                    } catch (IOException e) {
                        Logger.printERROR("IO/Error reading texture " + filename + " : " + e);
                        Logger.printExceptionERROR(e);
                        System.exit(1); //TODO: etre moins violent et desactiver le mode texture pour les objets en question (pas facile a faire)
                    }
                // not .jpg or .png or .bmp
                }
            } catch (NoSuchAlgorithmException e) {
                Logger.printExceptionERROR(e);
                System.exit(0); // TODO: que faire de mieux ? peut-on se permettre une desactivation du partage des textures ?
            }
        } else {
            Logger.printERROR("(" + filename + ") Unsupported file extension");
        }
        return null;
    }

    public static Texture createRenderTexture(int size) {
        Texture res = null;
        try {
            res = new Texture(size);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return res;
    }

    /**
     * The next time we use the given texture it will be loaded from disk
     */
    public static void free(Texture tex) {
        tofreeList.add(tex);
        texturecollector.remove(tex);
    }

    /**
     * This openGL-tick will free all textures that are in waiting queue to be freed
     * @param gl
     */
    public static void freeTick(GL gl) {
        for (Texture t : tofreeList) {
            t.free(gl);
            tofreeList.removeFirst();
        }
    }
}
