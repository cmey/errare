package networkEngine.messages;

import java.net.Socket;

/**
 * The superclass of the messages that clients send to the server, meaning it's a demand.
 * @author cyberchrist
 */
public abstract class TryEvent extends AbstractNetworkMessage {

	public Socket socket;
	
	public Socket getSocket(){
		return this.socket;
	}
}
