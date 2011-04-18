package graphicsEngine;

import geom.Point;
import java.io.IOException;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import logger.Logger;
import wiiremotej.*;
import wiiremotej.event.WRAccelerationEvent;
import wiiremotej.event.WRButtonEvent;
import wiiremotej.event.WRCombinedEvent;
import wiiremotej.event.WRExtensionEvent;
import wiiremotej.event.WRIREvent;
import wiiremotej.event.WRStatusEvent;
import wiiremotej.event.WiiRemoteDiscoveredEvent;
import wiiremotej.event.WiiRemoteDiscoveryListener;
import wiiremotej.event.WiiRemoteListener;

/**
 * @author christophe
 * @date 24 avr. 2008
 */
public class WiiSword extends Graphics implements WiiRemoteListener{
    
    private Point A;
    private Point B;
    private boolean WiiRemotePresent;
    
    public WiiSword () {
        WiiRemotePresent = false;
        A = new Point(0,0,0);
        B = new Point(50,50,50);
        try {
            WiiRemoteJ.setConsoleLoggingAll();
            final WiiSword ws = this;
            WiiRemoteDiscoveryListener listener = new WiiRemoteDiscoveryListener() {

                public void wiiRemoteDiscovered(WiiRemoteDiscoveredEvent evt) {
                    evt.getWiiRemote().addWiiRemoteListener(ws);
                }

                public void findFinished(int numberFound) {
                    System.out.println("Found " + numberFound + " remotes!");
                }
            };

            WiiRemote remote = WiiRemoteJ.findRemote();
            if(remote!=null){
                remote.addWiiRemoteListener(this);
                remote.setAccelerometerEnabled(true);
                WiiRemotePresent = true;
            }else{
                Logger.printWARNING("No WiiRemote found!\n");
            }
        } catch (IOException ex) {
            Logger.printExceptionERROR(ex);
        } catch (IllegalStateException ex) {
            Logger.printExceptionERROR(ex);
        } catch (InterruptedException ex) {
            Logger.printExceptionERROR(ex);
        } catch (Exception ex){
            Logger.printExceptionERROR(ex);
        } catch (Error er){
            Logger.printErrorERROR(er);
        }
    }
    
    @Override
    protected void draw(GLAutoDrawable gld) {
        if(!WiiRemotePresent)
            return;
        GL gl = gld.getGL();
        gl.glPushAttrib(GL.GL_ALL_ATTRIB_BITS);
        gl.glDisable(GL.GL_TEXTURE_2D);
        gl.glLineWidth(4);
        gl.glBegin(GL.GL_LINE);
        gl.glColor4f(1,0,0,1);
        gl.glVertex3f(A.x, A.y, A.z);
        gl.glColor4f(1,1,1,1);
        gl.glVertex3f(B.x, B.y, B.z);
        gl.glEnd();
        gl.glPopAttrib();
    }

    @Override
    protected void drawGeometryOnly(GLAutoDrawable gld) {
        
    }

    @Override
    protected void drawByTriangles(GLAutoDrawable gld) {
        
    }

    public void buttonInputReceived(WRButtonEvent arg0) {
        
    }

    public void statusReported(WRStatusEvent evt) {
        System.out.println("Battery level: " + (double)evt.getBatteryLevel()/2+ "%");
        System.out.println("Continuous: " + evt.isContinuousEnabled());
        System.out.println("Remote continuous: " + evt.getSource().isContinuousEnabled());
    }

    private static final int NB_COMPOSANTES = 2;
    private static final int BUFFER_SIZE = 3;
    
    // accelerations
    double[][] a = new double[BUFFER_SIZE][NB_COMPOSANTES];
    // means of accelerations
    double[][] amoy = new double[BUFFER_SIZE-1][NB_COMPOSANTES];
    // speeds
    double[][] v = new double[BUFFER_SIZE-1][NB_COMPOSANTES];
    // means of speeds
    double[] vmoy = new double[NB_COMPOSANTES];
    // position
    double[] pos = new double[3];

    static float max = 100;
    
    private void bornerPoint(Point P) {
        if(P.x > max) P.x = max;
        if(P.y > max) P.y = max;
        if(P.z > max) P.z = max;
        if(P.x < -max) P.x = -max;
        if(P.y < -max) P.y = -max;
        if(P.z < -max) P.z = -max;
    }
    
    
    // a[0] is freed for the new value
    private void shift(){
        // shift accelerations
        for(int i = a.length-1; i>0; i-- )
            for(int c = 0; c<NB_COMPOSANTES; c++)
                a[i][c] = a[i-1][c];
        // shift speeds
        for(int i = v.length-1; i>0; i-- )
            for(int c = 0; c<NB_COMPOSANTES; c++)
                v[i][c] = v[i-1][c];
    }
    
    
    double[] vitesse = new double[NB_COMPOSANTES];
    double[] position = new double[NB_COMPOSANTES];
    long lastTime = 0;
    public void accelerationInputReceived(WRAccelerationEvent evt) {
        //x = (int)(evt.getXAcceleration()/5*300)+300;
        //y = (int)(evt.getYAcceleration()/5*300)+300;
        //z = (int)(evt.getZAcceleration()/5*300)+300;
        
        double roll = evt.getRoll();
        double pitch = evt.getPitch();
        boolean s = evt.isStill();
        
        if(0==lastTime){
            lastTime = System.currentTimeMillis();
            vitesse[0] = 0;
            vitesse[1] = 0;
            position[0] = 0;
            position[1] = 0;
        }
        long thisTime = System.currentTimeMillis();
        long deltaT = thisTime - lastTime;
        lastTime = thisTime;
        // get new values of acceleration
        double[] ac = new double[3]; // x y z
        ac[0] = evt.getXAcceleration();
        ac[1] = evt.getYAcceleration();
        ac[2] = evt.getZAcceleration();
        
        /*
        // free one place in the buffers
        shift();
        
        // copy new values into buffer
        for(int i = 0; i<NB_COMPOSANTES; i++)
            a[0][i] = ac[i];
                
        // calculate means of acceleration
        for(int c = 0; c<NB_COMPOSANTES; c++)
            for(int i = 0; i<(BUFFER_SIZE-1); i++ )
                amoy[i][c] = a[i][c] - a[i+1][c];
        
        // calculate new speeds
        for(int c = 0; c<NB_COMPOSANTES; c++)
            for(int i = 0; i<(BUFFER_SIZE-1-1); i++ )
                v[i][c] = v[i+1][c] + amoy[i][c];
        
        // calculate mean of speed
        for(int c = 0; c<NB_COMPOSANTES; c++)
            vmoy[c] = v[0][c] - v[1][c];
        
        // calculate new pos
        for(int c = 0; c<NB_COMPOSANTES; c++)
            pos[c] = pos[c] + vmoy[c] * 50;
        
        A.x = (float) pos[0];
        A.z = (float) pos[1];
        
        */
        
        double timeScale = deltaT / 1000.0;
        double meshScale = 10;
        double accScale = 200;
        
        for(int i=0; i<NB_COMPOSANTES; i++){
            vitesse[i] = vitesse[i] + ac[i] * timeScale;
            position[i] = position[i] + vitesse[i] * timeScale * meshScale * accScale;
            // borne dans un cube
            if(position[i] > max) {position[i] = max; vitesse[i] = 0;}
            if(position[i] < -max) {position[i] = -max; vitesse[i] = 0;}
        }
        
        A.x = (float) position[0];
        A.z = (float) position[1];
        
        B.x = (float) (A.x + meshScale);//+ (-Math.cos(roll) * Math.sin(pitch) * meshScale));
        B.y = (float) (A.y + meshScale); //+ (Math.cos(roll) * Math.cos(pitch) * meshScale));
        B.z = (float) (A.z + meshScale); //+ (Math.sin(roll) * meshScale));

    }
    
    public void IRInputReceived(WRIREvent arg0) {
        for(IRLight light : arg0.getIRLights()){
            
        }
    }

    public void extensionInputReceived(WRExtensionEvent arg0) {
        
    }

    public void extensionConnected(WiiRemoteExtension arg0) {
        
    }

    public void extensionPartiallyInserted() {
        
    }

    public void extensionUnknown() {
        
    }

    public void extensionDisconnected(WiiRemoteExtension arg0) {
        
    }

    public void combinedInputReceived(WRCombinedEvent arg0) {
        
    }

    public void disconnected() {
        Logger.printWARNING("Remote disconnected... Please Wii again.");
        WiiRemotePresent = false;
        //System.exit(0);
    }
}
