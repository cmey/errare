/**
 * 
 */
package networkEngine2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * @author linxsam
 *
 */
public class testClient {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws UnknownHostException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {
		// TODO Auto-generated method stub
		System.out.println("Connecting linxsan.hd.free.fr");
		Socket soc = new Socket("127.0.0.1",Server.listenPort);
		Client c = new Client(soc);
		c.setUdp1(12010);
		c.setUdp1(12020);
		c.setIDI("");
		c.start();
		System.out.print("Input command, type exit to quit \ncommand> ");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String cmd = br.readLine();
		while(true) {
			c.sendPing();
			Thread.sleep(100);
		}
	}

}
