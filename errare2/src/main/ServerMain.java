package main;

import java.io.IOException;

import logger.Logger;

import gameEngine.GameEngineServer;
import persistenceEngine.PersistenceClient;
import persistenceEngine.PersistenceServer;

/**
 * The Main entry for the server side program.
 * @author Christophe
 *
 */
public class ServerMain {
    
	private PersistenceServer persistenceServer;
	private PersistenceClient persistenceClient;
    private GameEngineServer gameEngineServer;
    private boolean continue_main;
    
    public ServerMain() throws IOException {
        Logger.printDEBUG("Starting");
    	continue_main = true;
    	
    	// for the moment, the persistence server is embeded.
    	persistenceServer = new PersistenceServer();
    	
    	persistenceClient = new PersistenceClient();
        
    	gameEngineServer = new GameEngineServer(this);
    }
    
    private void run(){
    	while(continue_main){
    		//serverNetworkLayer is in a thread appart
    		gameEngineServer.run();
    	}
    }
    
    public GameEngineServer getGameEngineServer(){
    	return this.gameEngineServer;
    }
    
    public static void main(String args[]) throws Exception{
    	new ServerMain();
    }

    /**
	 * Shut down properly.
	 */
	public void quit() {
		if(gameEngineServer!=null) gameEngineServer.quit();
		//if(serverNetworkLayer!=null) serverNetworkLayer.quit();
		System.exit(0);
	}
}
