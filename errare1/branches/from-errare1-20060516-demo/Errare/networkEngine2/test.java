package networkEngine2;

import java.io.IOException;
import java.net.UnknownHostException;

public class test {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws UnknownHostException 
	 * @throws ClassNotFoundException 
	 */
	public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException {
		// TODO Auto-generated method stub
		Server s = new Server();
		s.start();

	}

}
