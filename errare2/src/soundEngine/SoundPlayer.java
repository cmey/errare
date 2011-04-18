package soundEngine;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import net.java.games.joal.AL;
import net.java.games.joal.ALException;
import net.java.games.joal.ALFactory;

public abstract class SoundPlayer {

	protected String path;
	
	protected AL al;
	

	// temporary buffer
	protected ByteBuffer dataBuffer = ByteBuffer.allocateDirect(4096 * 8);

	// front and back buffers
	protected IntBuffer buffers = createIntBuffer(2);

	// audio source
	protected IntBuffer source = createIntBuffer(1);

	
	public SoundPlayer(String path,AL al) {
		this.al = al;
		this.path = path;
	}

	/**
	 * Creates an integer buffer to hold specified ints - strictly a utility
	 * method
	 * 
	 * @param size
	 *            how many int to contain
	 * @return created IntBuffer
	 */
	protected static IntBuffer createIntBuffer(int size) {
		ByteBuffer temp = ByteBuffer.allocateDirect(4 * size);
		temp.order(ByteOrder.nativeOrder());
		return temp.asIntBuffer();
	}
	
	public abstract void playSound();
	
	public abstract void release();
	

	public void setPosition(float x, float y, float z) {
		al.alSource3f(source.get(0), AL.AL_POSITION, x, y, z);
		check();
	}

	protected void check() {

		int error = al.alGetError();
		String err = "";
		if (error != AL.AL_NO_ERROR) {
			try {
				for (Field f : Class.forName("net.java.games.joal.AL")
						.getFields()) {
					if (f.getType().getName().compareTo("int") == 0) {
						if (f.getInt(f) == error)
							err = f.getName();
					}
				}
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			throw new ALException("OpenAL error raised: " + err);
		}
	}


	public void setOrientation(float xat, float yat, float zat, float xup,
			float yup, float zup) {
		float[] o = { xat, yat, zat, xup, yup, zup };
		FloatBuffer ori = FloatBuffer.wrap(o);
		al.alListenerfv(AL.AL_ORIENTATION, ori);
		check();
	}

	public void setDirection(float x, float y, float z) {
		al.alSource3f(source.get(0), AL.AL_DIRECTION, x, y, z);

		check();
	}

	public void setGain(float volume) {
		al.alSourcef(source.get(0), AL.AL_GAIN, volume);
		check();
	}

	public void setResonance(){
		//IntBuffer tmp = createIntBuffer(1);
		//al.alSourcei(source.get(0),AL.AL_AUTOWAH_RESONANCE,)
	}
	
	public void set(){
		//AL.AL_ECHO
	}

	public float getGain(){
		float vol = 0;
		al.alBufferf(source.get(0),AL.AL_GAIN,vol);
		return vol;
	}
	
	
	/*public static SoundPlayer getPlayer(String path, AL al) throws MalformedURLException, IOException {
		if(path.endsWith(".wav")) return new WavPlayer(path,al);
		else if(path.endsWith(".ogg")) return new OggPlayer(path,al);
		return null;
	}*/
	
}
