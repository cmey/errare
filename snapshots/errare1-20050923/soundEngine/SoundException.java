package soundEngine;



/**
 *
 * @author  Troubleshooting
 */
public class SoundException extends java.lang.RuntimeException {
    
    /**
     * Creates a new instance of <code>SoundException</code> without detail message.
     */
    public SoundException() {
    }
    
    
    /**
     * Constructs an instance of <code>SoundException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public SoundException(String msg) {
        super(msg);
    }
    public SoundException(Throwable cause) {
        super(cause);
    }
}
