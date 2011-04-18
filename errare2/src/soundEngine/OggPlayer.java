package soundEngine;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import logger.Logger;
import net.java.games.joal.AL;
import net.java.games.joal.ALException;
import net.java.games.joal.ALFactory;
import net.java.games.joal.util.ALut;

/**
 * Plays ogg files using openal.
 * <p>
 * First open the file by calling open(OggInputStream). Then play it either by
 * using play(), or playInNewThread(long). If you use Play() you must also call
 * update() at an interval, to feed OpenAL with data.
 */
public class OggPlayer extends SoundPlayer{


	private AL al;

	// is used to unpack ogg file.
	private OggInputStream oggInputStream;

	// set to true when player is initalized.
	private boolean initalized = false;

	// a seperate thread that calls update.
	private PlayerThread playerThread = null;
	

	public OggPlayer(String path, AL al) throws MalformedURLException, IOException {
		super(path,al);
		this.al = al;
		open();
	}

	/**
	 * Opens the specified ogg file in the classpath.
	 * 
	 * @throws IOException
	 * @throws MalformedURLException
	 */
	private void open() throws MalformedURLException, IOException {
		oggInputStream = new OggInputStream(new File(path).toURI().toURL()
				.openStream());

		buffers.rewind();
		Logger.printINFO(al.toString());
		al.alGenBuffers(2, buffers);
		check();

		source.rewind();
		al.alGenSources(1, source);
		check();

		initalized = true;

		al.alSource3f(source.get(0), AL.AL_POSITION, 0, 0, 0);
		al.alSource3f(source.get(0), AL.AL_VELOCITY, 0, 0, 0);
		al.alSource3f(source.get(0), AL.AL_DIRECTION, 0, 0, 0);
		al.alSourcef(source.get(0), AL.AL_ROLLOFF_FACTOR, 0);
		al.alSourcei(source.get(0), AL.AL_SOURCE_RELATIVE, AL.AL_TRUE);
	}

	/**
	 * release the file handle
	 */
	public void release() {
		if (initalized) {
			al.alSourceStop(source.get(0));
			empty();
			al.alDeleteSources(1, source);
			check();
			al.alDeleteBuffers(2, buffers);
			check();
		}
	}

	/**
	 * Plays the Ogg stream. update() must be called regularly so that the data
	 * is copied to OpenAl
	 */
	private boolean play() {
		if (playing()) {
			return true;
		}

		for (int i = 0; i < buffers.capacity(); i++) {
			if (!stream(buffers.get(i))) {
				return false;
			}
		}

		al.alSourceQueueBuffers(source.get(0), 2, buffers);
		al.alSourcePlay(source.get(0));

		return true;
	}

	/**
	 * Plays the track in a newly crated thread.
	 * 
	 * @param updateInterval
	 *            at which interval should the thread call update, in
	 *            milliseconds.
	 */
	private boolean playInNewThread(long updateIntervalMillis) {
		if (play()) {
			playerThread = new PlayerThread(updateIntervalMillis);
			playerThread.start();
			return true;
		}

		return false;
	}

	/**
	 * check if the source is playing
	 */
	public boolean playing() {
		IntBuffer tmp = createIntBuffer(1);
		al.alGetSourcei(source.get(0), AL.AL_SOURCE_STATE, tmp);

		return tmp.get(0) == AL.AL_PLAYING;
	}

	/**
	 * Copies data from the ogg stream to openal. Must be called often.
	 * 
	 * @return true if sound is still playing, false if the end of file is
	 *         reached.
	 */
	private synchronized boolean update() throws IOException {
		boolean active = true;
		IntBuffer tmp = createIntBuffer(1);
		al.alGetSourcei(source.get(0), AL.AL_BUFFERS_PROCESSED, tmp);
		int processed = tmp.get(0);
		while (processed-- > 0) {
			IntBuffer buffer = createIntBuffer(1);
			al.alSourceUnqueueBuffers(source.get(0), 1, buffer);
			check();

			active = stream(buffer.get(0));
			buffer.rewind();

			al.alSourceQueueBuffers(source.get(0), 1, buffer);
			check();
		}

		return active;
	}

	/**
	 * reloads a buffer
	 * 
	 * @return true if success, false if read failed or end of file.
	 */
	protected boolean stream(int buffer) {
		try {
			int bytesRead = oggInputStream.read(dataBuffer, 0, dataBuffer
					.capacity());
			if (bytesRead >= 0) {
				dataBuffer.rewind();
				boolean mono = (oggInputStream.getFormat() == OggInputStream.FORMAT_MONO16);
				int format = (mono ? AL.AL_FORMAT_MONO16
						: AL.AL_FORMAT_STEREO16);
				al.alBufferData(buffer, format, dataBuffer, bytesRead,
						oggInputStream.getRate());
				check();
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * empties the queue
	 */
	protected void empty() {
		IntBuffer tmp = createIntBuffer(1);
		al.alGetSourcei(source.get(0), AL.AL_BUFFERS_QUEUED, tmp);
		int queued = tmp.get(0);
		while (queued-- > 0) {
			IntBuffer buffer = createIntBuffer(1);
			al.alSourceUnqueueBuffers(source.get(0), 1, buffer);
			check();
		}
	}



	/**
	 * The thread that updates the sound.
	 */
	private class PlayerThread extends Thread {
		// at what interval update is called.
		long interval;

		// Creates the PlayerThread
		PlayerThread(long interval) {
			this.interval = interval;
		}

		// Calls update at an interval
		public void run() {
			try {
				while (update()) {
					sleep(interval);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	
	public void playSound() {
		play();
		new Thread() {
			public void run() {
				try {
					while (update()) {
						Thread.sleep(5);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();
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
	

	public static void main(String args[]) throws IOException,
			InterruptedException {
		ALut.alutInit();
		AL al = ALFactory.getAL();
		new OggPlayer("data/sounds/music/02_fanatic-conquerer.ogg", al).playSound();
	}
}
