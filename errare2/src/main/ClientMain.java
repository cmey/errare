/*Errare is a free and crossplatform MMORPG project.
Copyright (C) 2006  Christophe Meyer

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.*/
package main;

import freeworldLoader.FreeWorld3DLoader;
import gameEngine.GameEngineClient;
import genericEngine.Engine;
import graphicsEngine.Extensions;
import graphicsEngine.GraphicsEngine;
import guiEngine.GuiEngine;

import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagLayout;
import java.util.Random;

import javax.swing.JFrame;

import logger.Logger;
import physicsEngine.PhysicsEngine;
import soundEngine.SoundEngine;
import userInputEngine.UserInputController;
import xmlEngine.XmlEngine;

/**
 * The Errare client application.
 * Everything is rendered in a JFrame.
 * @author christophe
 */
public class ClientMain extends JFrame implements Engine {

    public static final long PERIOD = (long) ((1.0 / 60.0) * 1E9);
    private JFrame jf;
    private Random random;
    private SoundEngine soundEngine;
    private PhysicsEngine physicsEngine;
    private GraphicsEngine graphicsEngine;
    private GuiEngine guiEngine;
    private XmlEngine xmlEngine;
    private UserInputController userInputController;
    private GameEngineClient gameEngineClient;
    static String login, password;


    /**
     * Called from main.
     */
    public static void start() throws Exception {
        ClientMain cm = new ClientMain();
    }

    /**
     * Shut down properly.
     */
    public void quit() {
        if (guiEngine != null) {
            guiEngine.quit();
        }
        if (graphicsEngine != null) {
            graphicsEngine.quit();
        }
        if (physicsEngine != null) {
            physicsEngine.quit();
        }
        if (soundEngine != null) {
            soundEngine.quit();
        }

        setVisible(false);
        dispose();
        System.exit(0);
    }

    
    public ClientMain() throws Exception {
        super("Errare");
        this.jf = this;

        setupJFrame();

        try {
            System.setSecurityManager(null);

            Logger.initialize();

            Extensions.initialize();

            String os = System.getProperty("os.name");
            String user = System.getProperty("user.name");
            Logger.printINFO("OS name : " + os + "  USER name : " + user);

            xmlEngine = new XmlEngine();

            userInputController = new UserInputController("test", xmlEngine);
            userInputController.register(this, "exit");

            soundEngine = new SoundEngine();
            guiEngine = new GuiEngine(this);
            graphicsEngine = new GraphicsEngine(this);
            physicsEngine = new PhysicsEngine(this);
            gameEngineClient = new GameEngineClient(this);

            soundEngine.play("data/sounds/music/Ville.ogg", false);

            FreeWorld3DLoader loader = new FreeWorld3DLoader(physicsEngine, graphicsEngine);
            loader.loadWorld("freeworld/world.xml");

            jf.getContentPane().add(getGraphicsEngine().getGLComponent());
//            jf.validate();
//            jf.requestFocus();

            setVisible(true);
            
            run();

        } catch (Exception e) {
            Logger.printExceptionERROR(e);
        } catch (Error er) {
            Logger.printErrorERROR(er);
        }
    }

    /**
     * Nicely center the window on the screen.
     */
    private void setupJFrame() {
        Dimension d = getToolkit().getScreenSize();

        int width = (int) d.getWidth()/2;
        int x = (int) (d.getWidth() / 2) - (int) (width / 2);

        int height = (int) d.getHeight()/2;
        int y = (int) (d.getHeight() / 2) - (int) (height / 2);

        setBounds(x, y, width, height);
        setResizable(false);
        getContentPane().setLayout(new GridBagLayout());
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        //setUndecorated(true);
    }

    private void setFullScreenMode(JFrame jf) {
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] devices = env.getScreenDevices();
        GraphicsDevice device = devices[0];

        device.setFullScreenWindow(jf);
    }

    public void run() {

        long begin, end;
        long sleeptime = 0;
        long runtime = 0;

        begin = 0;
        while (true) {
            if (sleeptime > 0) {
                try {
                    Thread.sleep(sleeptime / 1000000, (int) (sleeptime % 1000000));
                } catch (InterruptedException ex) {
                }
            }

            begin = System.nanoTime();

            graphicsEngine.run();
            guiEngine.run();
            physicsEngine.run();

            end = System.nanoTime();
            runtime = end - begin;
            sleeptime = PERIOD - runtime;

            sleeptime = 0;
        }

    }


    public static void main(final String[] args) throws Exception {
        //LoginScreen ls = new LoginScreen();
        // ls will call start()

        if(args.length>0) {
            if("net".equalsIgnoreCase(args[0]))
                ServerMain.main(args);
        }else{
            start();
        }
    }


    // ------- LISTENERS ----------
    

    public boolean invokeKeyEvent(String action) {
        if (action.equalsIgnoreCase("exit")) {
            this.quit(); // shut down properly
        }
        return false;
    }

    public boolean invokeMouseEvent(String action, int x, int y) {
        // not used
        return false;
    }


    // ------- GETTERS -----------


    public PhysicsEngine getPhysicsEngine() {
        return physicsEngine;
    }

    public JFrame getJFrame() {
        return jf;
    }

    public Random getRandom() {
        return random;
    }

    public GraphicsEngine getGraphicsEngine() {
        return graphicsEngine;
    }

    public GuiEngine getGuiEngine() {
        return guiEngine;
    }

    public SoundEngine getSoundEngine() {
        return soundEngine;
    }

    public XmlEngine getXmlEngine() {
        return xmlEngine;
    }

    public UserInputController getUserInputController() {
        return userInputController;
    }

    public GameEngineClient getGameEngineClient() {
        return gameEngineClient;
    }

    public String requestLogin() {
        return this.login;
    }

    public String requestPass() {
        return this.password;
    }
}
