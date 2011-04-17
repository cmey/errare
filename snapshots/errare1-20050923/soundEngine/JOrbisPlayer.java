package soundEngine;


import java.applet.AppletContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.util.Hashtable;
import java.util.Vector;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import com.jcraft.jogg.Packet;
import com.jcraft.jogg.Page;
import com.jcraft.jogg.StreamState;
import com.jcraft.jogg.SyncState;
import com.jcraft.jorbis.Block;
import com.jcraft.jorbis.Comment;
import com.jcraft.jorbis.DspState;
import com.jcraft.jorbis.Info;

/**
 * 
 * @author Troubleshooting
 * 
 * This class handles sound playing
 */
public class JOrbisPlayer implements Runnable{
	
	
	private Vector playlist=new Vector();
	private String soundtrack="";
	private Thread player=null;
	private InputStream bitStream=null;
	
	private int udp_port=-1;
	private String udp_baddress=null;
	
	private static AppletContext acontext=null;
	
	private static final int BUFSIZE=4096*2;
	private static int convsize=BUFSIZE*2;
	private static byte[] convbuffer=new byte[convsize]; 
	
	private int RETRY=3;
	private int retry=RETRY;
	
	private String playlistfile="playlist";
	
	private boolean icestats=false;
	
	private SyncState oy;
	private StreamState os;
	private Page og;
	private Packet op;
	private Info vi;
	private Comment vc;
	private DspState vd;
	private Block vb;
	
	private byte[] buffer=null;
	private int bytes=0;
	
	private int format;
	private int rate=0;
	private int channels=0;
	private int left_vol_scale=100;
	private int right_vol_scale=100;
	private SourceDataLine outputLine=null;
	private String current_source=null;
	
	private int frameSizeInBytes;
	private int bufferLengthInBytes;
	
	/**
	 * Changes the channel
	 * @param c the new channel
	 */
	public void setChannel(int c){
		this.channels=c;
	}
	
	/**
	 * Changes the left volume
	 * @param v the new volume
	 */
	public void setLeftVolScale(int v){
		this.left_vol_scale=v;
	}
	
	/**
	 * Changes the right volume
	 * @param v the new volume
	 */
	public void setRightVolScale(int v){
		this.right_vol_scale=v;
	}
	
	/**
	 * Changes the player's path
	 * @param path
	 */
	public void addPath(String path){
		playlist.add(path);
	}
	
	/**
	 * Initializes the player
	 *
	 */
	private void init_jorbis(){
		oy=new SyncState();
		os=new StreamState();
		og=new Page();
		op=new Packet();
		
		vi=new Info();
		vc=new Comment();
		vd=new DspState();
		vb=new Block(vd);
		
		buffer=null;
		bytes=0;
		
		oy.init();
	}
	
	/**
	 * Checks and switches on the sound device
	 * @param channels
	 * @param rate
	 * @return the outputline
	 */
	private SourceDataLine getOutputLine(int channels, int rate){
		if(outputLine!=null || this.rate!=rate || this.channels!=channels){
			if(outputLine!=null){
				outputLine.drain();
				outputLine.stop();
				outputLine.close();
			}
			init_audio(channels, rate);
			outputLine.start();
		}
		return outputLine;
	}
	
	/**
	 * Checks the path and the output device
	 * @param channels
	 * @param rate
	 */
	private void init_audio(int channels, int rate){
		try {
			
			AudioFormat audioFormat = 
				new AudioFormat((float)rate, 
						16,
						channels,
						true,  // PCM_Signed
						false  // littleEndian
				);
			DataLine.Info info = 
				new DataLine.Info(SourceDataLine.class,
						audioFormat, 
						AudioSystem.NOT_SPECIFIED);
			if (!AudioSystem.isLineSupported(info)) {
				
				return;
			}
			
			try{
				outputLine = (SourceDataLine) AudioSystem.getLine(info);
				
				outputLine.open(audioFormat);
			} 
			catch (LineUnavailableException ex) { 
				System.out.println("Unable to open the sourceDataLine: " + ex);
				return;
			} 
			catch (IllegalArgumentException ex) { 
				System.out.println("Illegal Argument: " + ex);
				return;
			}
			
			frameSizeInBytes = audioFormat.getFrameSize();
			int bufferLengthInFrames = outputLine.getBufferSize()/frameSizeInBytes/2;
			bufferLengthInBytes = bufferLengthInFrames * frameSizeInBytes;
			
			this.rate=rate;
			this.channels=channels;
		}
		catch(Exception ee){
			System.out.println(ee);
		}
	}
	
	
	/**
	 * Creates a new thread that plays a stream
	 */
	public void run() {
		Thread me = Thread.currentThread();
		
		while(true){
			bitStream=selectSource(soundtrack);
			if(bitStream!=null){
				if(udp_port!=-1){
					play_udp_stream(me);
				}
				else{
					play_stream(me);
				}
			}
			if(player!=me){
				break;
			}
			bitStream=null;
		}
		player=null; 
	}
	
	/**
	 * Plays a stream
	 * @param me
	 */
	private void play_stream(Thread me) {
		init_jorbis();
		
		retry=RETRY;
		
		loop:
			while(true){
				int eos=0;
				
				int index=oy.buffer(BUFSIZE);
				buffer=oy.data;
				try{ bytes=bitStream.read(buffer, index, BUFSIZE); }
				catch(Exception e){
					System.err.println(e);
					return;
				}
				oy.wrote(bytes);
				
				if(oy.pageout(og)!=1){
					if(bytes<BUFSIZE)break;
					System.err.println("Input does not appear to be an Ogg bitstream.");
					return;
				}
				
				os.init(og.serialno());
				os.reset();
				
				vi.init();
				vc.init();
				
				if(os.pagein(og)<0){ 
					// error; stream version mismatch perhaps
					System.err.println("Error reading first page of Ogg bitstream data.");
					return;
				}
				
				retry=RETRY;
				
				if(os.packetout(op)!=1){ 
					// no page? must not be vorbis
					System.err.println("Error reading initial header packet.");
					break;
					
				}
				
				if(vi.synthesis_headerin(vc, op)<0){ 
					// error case; not a vorbis header
					System.err.println("This Ogg bitstream does not contain Vorbis audio data.");
					return;
				}
				
				int i=0;
				
				while(i<2){
					while(i<2){
						int result=oy.pageout(og);
						if(result==0) break; // Need more data
						if(result==1){
							os.pagein(og);
							while(i<2){
								result=os.packetout(op);
								if(result==0)break;
								if(result==-1){
									System.err.println("Corrupt secondary header.  Exiting.");
									
									break loop;
								}
								vi.synthesis_headerin(vc, op);
								i++;
							}
						}
					}
					
					index=oy.buffer(BUFSIZE);
					buffer=oy.data; 
					try{ bytes=bitStream.read(buffer, index, BUFSIZE); }
					catch(Exception e){
						System.err.println(e);
						return;
					}
					if(bytes==0 && i<2){
						System.err.println("End of file before finding all Vorbis headers!");
						return;
					}
					oy.wrote(bytes);
				}
				
				{
					byte[][] ptr=vc.user_comments;
					StringBuffer sb=null;
					if(acontext!=null) sb=new StringBuffer();
					
					for(int j=0; j<ptr.length;j++){
						if(ptr[j]==null) break;
						System.err.println("Comment: "+new String(ptr[j], 0, ptr[j].length-1));
						if(sb!=null)sb.append(" "+new String(ptr[j], 0, ptr[j].length-1));
					} 
					System.err.println("Bitstream is "+vi.channels+" channel, "+vi.rate+"Hz");
					System.err.println("Encoded by: "+new String(vc.vendor, 0, vc.vendor.length-1)+"\n");
					if(sb!=null)acontext.showStatus(sb.toString());
				}
				
				convsize=BUFSIZE/vi.channels;
				
				vd.synthesis_init(vi);
				vb.init(vd);
				
				double[][][] _pcm=new double[1][][];
				float[][][] _pcmf=new float[1][][];
				int[] _index=new int[vi.channels];
				
				getOutputLine(vi.channels, vi.rate);
				
				while(eos==0){
					while(eos==0){
						
						if(player!=me){
							
							try{
								
								bitStream.close();
							}
							catch(Exception ee){}
							return;
						}
						
						int result=oy.pageout(og);
						if(result==0)break; // need more data
						if(result==-1){ // missing or corrupt data at this page position
							
						}
						else{
							os.pagein(og);
							while(true){
								result=os.packetout(op);
								if(result==0)break; // need more data
								if(result==-1){ // missing or corrupt data at this page position
									// no reason to complain; already complained above
								}
								else{
									// we have a packet.  Decode it
									int samples;
									if(vb.synthesis(op)==0){ // test for success!
										vd.synthesis_blockin(vb);
									}
									while((samples=vd.synthesis_pcmout(_pcmf, _index))>0){
										double[][] pcm=_pcm[0];
										float[][] pcmf=_pcmf[0];
										boolean clipflag=false;
										int bout=(samples<convsize?samples:convsize);
										
										// convert doubles to 16 bit signed ints (host order) and
										// interleave
										for(i=0;i<vi.channels;i++){
											int ptr=i*2;
											//int ptr=i;
											int mono=_index[i];
											for(int j=0;j<bout;j++){
												int val=(int)(pcmf[i][mono+j]*32767.);
												if(val>32767){
													val=32767;
													clipflag=true;
												}
												if(val<-32768){
													val=-32768;
													clipflag=true;
												}
												if(val<0) val=val|0x8000;
												convbuffer[ptr]=(byte)(val);
												convbuffer[ptr+1]=(byte)(val>>>8);
												ptr+=2*(vi.channels);
											}
										}
										outputLine.write(convbuffer, 0, 2*vi.channels*bout);
										vd.synthesis_read(bout);
									}	  
								}
							}
							if(og.eos()!=0)eos=1;
						}
					}
					
					if(eos==0){
						index=oy.buffer(BUFSIZE);
						buffer=oy.data;
						try{ bytes=bitStream.read(buffer,index,BUFSIZE); }
						catch(Exception e){
							System.err.println(e);
							return;
						}
						if(bytes==-1){
							break;
						}
						oy.wrote(bytes);
						if(bytes==0)eos=1;
					}
				}
				
				
				
				os.clear();
				vb.clear();
				vd.clear();
				vi.clear();
			}
		
		oy.clear();
		
		
		
		try {
			if(bitStream!=null)bitStream.close();
		} 
		catch(Exception e) { }
	}
	
	/**
	 * Checks the file is playable
	 * @param me
	 */
	private void play_udp_stream(Thread me) {
		init_jorbis();
		
		boolean firstloop=true;
		
		try{
			loop: 
				while(true){      
					int eos=0;
					int index=oy.buffer(BUFSIZE);
					buffer=oy.data;
					try{ bytes=bitStream.read(buffer, index, BUFSIZE); }
					catch(Exception e){
						System.err.println(e);
						return;
					}
					
					oy.wrote(bytes);
					if(oy.pageout(og)!=1){
						
						System.err.println("Input does not appear to be an Ogg bitstream.");
						return;
					}
					
					os.init(og.serialno());
					os.reset();
					
					vi.init();
					vc.init();
					if(os.pagein(og)<0){ 
						// error; stream version mismatch perhaps
						System.err.println("Error reading first page of Ogg bitstream data.");
						return;
					}
					
					if(os.packetout(op)!=1){ 
						// no page? must not be vorbis
						System.err.println("Error reading initial header packet.");
//						break;
						return;
					}
					
					if(vi.synthesis_headerin(vc, op)<0){ 
						// error case; not a vorbis header
						System.err.println("This Ogg bitstream does not contain Vorbis audio data.");
						return;
					}
					
					int i=0;
					while(i<2){
						while(i<2){
							int result=oy.pageout(og);
							if(result==0) break; // Need more data
							if(result==1){
								os.pagein(og);
								while(i<2){
									result=os.packetout(op);
									if(result==0)break;
									if(result==-1){
										System.err.println("Corrupt secondary header.  Exiting.");
										
										break loop;
									}
									vi.synthesis_headerin(vc, op);
									i++;
								}
							}
						}
						
						if(i==2)break;
						
						index=oy.buffer(BUFSIZE);
						buffer=oy.data; 
						try{ bytes=bitStream.read(buffer, index, BUFSIZE); }
						catch(Exception e){
							System.err.println(e);
							return;
						}
						if(bytes==0 && i<2){
							System.err.println("End of file before finding all Vorbis headers!");
							return;
						}
						oy.wrote(bytes);
					}
					break;
				}
		}
		catch(Exception e){
		}
		
		try{ bitStream.close(); }catch(Exception e){}
		
		UDPIO io=null;
		try{
			io=new UDPIO(udp_port);
		}
		catch(Exception e){ return; }
		
		bitStream=io;
		play_stream(me);
		
		
	}
	
	/**
	 * Stops playing the current file and closes the stream
	 *
	 */
	public void stop(){
		if(player==null){
			try{
				outputLine.drain();
				outputLine.stop();
				outputLine.close();
				if(bitStream!=null)bitStream.close();
			}
			catch(Exception e){}
		}
		player=null;
	}
	
	
	
	/**
	 * Plays the current file
	 *
	 */
	protected void play_sound(){
		if(player!=null) return;
		player=new Thread(this);
		player.start();
	}
	
	/**
	 * Plays a specified file
	 * @param s
	 */
	protected void play_sound(String s){
		if(player!=null) {
			stop_sound();
			return;
		}
		player=new Thread(this);
		soundtrack=s;
		player.start();
	}
	
	/**
	 * Stops the player
	 *
	 */
	protected void stop_sound(){
		if(player==null) return;
		player=null;
		
	}
	
	/**
	 * Selects the palying mode (playlist, single file...)
	 * @param soundtrack
	 * @return
	 */
	private InputStream selectSource(String soundtrack){
		if(soundtrack.endsWith(".pls")){
			soundtrack=fetch_pls(soundtrack);
			if(soundtrack==null) return null;
			
		}
		else if(soundtrack.endsWith(".m3u")){
			soundtrack=fetch_m3u(soundtrack);
			if(soundtrack==null)return null;
			
		}
		
		if(!soundtrack.endsWith(".ogg")){
			return null;  
		}
		
		InputStream is=null;
		URLConnection urlc=null;
		try{
			URL  url=new File(soundtrack).toURL();
			urlc=url.openConnection();
			is=urlc.getInputStream();
			current_source=url.getProtocol()+"://"+url.getHost()+":"+url.getPort()+url.getFile();
		}
		catch(Exception ee){
			System.err.println(ee); 	    
		}
		
		if(is==null){
			try{
				is=new FileInputStream(System.getProperty("user.dir")+
						System.getProperty("file.separator")+soundtrack);
				current_source=null;
			}catch(Exception ee){ System.err.println(ee);}
		}
		
		if(is==null) return null;
		
		System.out.println("Select: "+soundtrack);
		
		{
			boolean find=false;
			
			
		}
		
		int i=0;
		String s=null;
		String t=null;
		udp_port=-1;
		udp_baddress=null;
		while(urlc!=null && true){
			s=urlc.getHeaderField(i);
			t=urlc.getHeaderFieldKey(i);
			if(s==null)break;
			i++;
			if(t!=null && t.equals("udp-port")){
				try{ udp_port=Integer.parseInt(s); }
				catch(Exception ee){System.err.println(ee);}
			}
			else if(t!=null && t.equals("udp-broadcast-address")){
				udp_baddress=s;
			}
		}
		return is;
	}
	
	/**
	 * Fetches the paths from a playlist
	 * @param pls
	 * @return
	 */
	private String fetch_pls(String pls){
		InputStream pstream=null;
		if(pls.startsWith("http://")){
			try{
				URL url=new URL(pls);
				URLConnection urlc=url.openConnection();
				pstream=urlc.getInputStream();
			}
			catch(Exception ee){
				System.err.println(ee); 	    
				return null;
			}
		}
		
		String line=null;
		while(true){
			try{line=readline(pstream);}catch(Exception e){}
			if(line==null)break;
			if(line.startsWith("File1=")){
				byte[] foo=line.getBytes();
				int i=6;
				for(;i<foo.length; i++){
					if(foo[i]==0x0d)break;
				}
				return line.substring(6, i);
			}
		}
		return null;
	}
	
	/**
	 * Fetches the paths from a m3u playlist
	 * @param pls
	 * @return
	 */
	private String fetch_m3u(String m3u){
		InputStream pstream=null;
		if(m3u.startsWith("http://")){
			try{
				URL url=new URL(m3u);
				URLConnection urlc=url.openConnection();
				pstream=urlc.getInputStream();
			}
			catch(Exception ee){
				System.err.println(ee); 	    
				return null;
			}
		}
		
		String line=null;
		while(true){
			try{line=readline(pstream);}catch(Exception e){}
			if(line==null)break;
			return line;
		}
		return null;
	}
	
	/**
	 * Creates connections to sound files of a playlist
	 *
	 */
	private void loadPlaylist(){
		
		if(playlistfile==null){
			return;
		}
		
		try{
			InputStream is=null;
			try{
				URL url=new URL(playlistfile);
				URLConnection urlc=url.openConnection();
				is=urlc.getInputStream();
			}
			catch(Exception ee){ }
			
			
			if(is==null) return;
			
			while(true){
				String line=readline(is);
				if(line==null) break;
				byte[] foo=line.getBytes();
				for(int i=0;i<foo.length; i++){
					if(foo[i]==0x0d){
						line=new String(foo, 0, i);
						break;
					}
				}
				playlist.addElement(line);
			}
		}
		catch(Exception e){
			System.out.println(e);
		}
	}
	
	/**
	 * Creates a stream to a file and reads its lines
	 * @param is
	 * @return
	 */
	private String readline(InputStream is) {
		StringBuffer rtn=new StringBuffer();
		int temp;
		do {
			try {temp=is.read();}
			catch(Exception e){return(null);}
			if(temp==-1){ return(null);}
			if(temp!=0 && temp!='\n')rtn.append((char)temp);
		}while(temp!='\n');                                                        
		return(rtn.toString());
	}
	
	/**
	 * Default builder
	 *
	 */
	public JOrbisPlayer(){
	}
	
	
	/**
	 * 
	 * @author chacal
	 * 
	 * A class that manages UDP connections
	 */
	private class UDPIO extends InputStream{
		InetAddress address;
		DatagramSocket socket = null;
		DatagramPacket sndpacket;
		DatagramPacket recpacket;
		byte[] buf = new byte[1024];
		//String host;
		int port;
		byte[] inbuffer=new byte[2048];
		byte[] outbuffer=new byte[1024];
		int instart=0, inend=0, outindex=0;
		
		UDPIO(int port){
			
			this.port=port;
			try{
				socket = new DatagramSocket(port);
			}
			catch(Exception e){System.err.println(e);}
			recpacket = new DatagramPacket(buf, 1024);
			
		}
		
		void setTimeout(int i){
			try{socket.setSoTimeout(i);}
			catch(Exception e){System.out.println(e);}
		}
		
		int getByte() throws java.io.IOException{
			if((inend-instart)<1){ read(1); }
			return inbuffer[instart++]&0xff;
		}
		int getByte(byte[] array) throws java.io.IOException{
			return getByte(array, 0, array.length);
		}
		int getByte(byte[] array, int begin, int length) throws java.io.IOException{
			int i=0;
			int foo=begin;
			while(true){
				if((i=(inend-instart))<length){
					if(i!=0){
						System.arraycopy(inbuffer, instart, array, begin, i);
						begin+=i;
						length-=i;
						instart+=i;
					}
					read(length);
					continue;
				}
				System.arraycopy(inbuffer, instart, array, begin, length);
				instart+=length;
				break;
			}
			return begin+length-foo;
		}
		int getShort() throws java.io.IOException{
			if((inend-instart)<2){ read(2); }
			int s=0;
			s=inbuffer[instart++]&0xff;
			s = ((s<<8)&0xffff) | (inbuffer[instart++]&0xff);
			return s;
		}
		int getInt() throws java.io.IOException{
			if((inend-instart)<4){ read(4); }
			int i=0;
			i=inbuffer[instart++]&0xff;
			i = ((i<<8)&0xffff) | (inbuffer[instart++]&0xff);
			i = ((i<<8)&0xffffff) | (inbuffer[instart++]&0xff);
			i = (i<<8) | (inbuffer[instart++]&0xff);
			return i;
		}
		private void getPad(int n) throws java.io.IOException{
			int i;
			while (n > 0){
				if((i=inend-instart)<n){
					n-=i;
					instart+=i;
					read(n);
					continue;
				}
				instart+=n;
				break;
			}
		}
		
		private void read(int n) throws java.io.IOException{
			if (n>inbuffer.length){
				n=inbuffer.length;
			}
			instart=inend=0;
			int i;
			while(true){
				recpacket.setData(buf, 0, 1024);
				socket.receive(recpacket);
				
				i=recpacket.getLength();
				System.arraycopy(recpacket.getData(), 0, inbuffer, inend, i);
				if(i==-1){
					throw new java.io.IOException();
				}
				inend+=i;
				break;
			}
		}
		public void close() throws java.io.IOException{
			socket.close();
		}
		public int read() throws java.io.IOException{ return 0; }
		public int read(byte[] array, int begin, int length) throws java.io.IOException{
			return getByte(array, begin, length);
		}
	}
	
	
	public static void main(String[] arg){
		
		JOrbisPlayer player=new JOrbisPlayer();
		JOrbisPlayer player2=new JOrbisPlayer();
		
		
		if(arg.length>0){
			for(int i=0; i<arg.length; i++){	
				player.playlist.addElement(arg[i]);
			}
		}
		
		
		player.play_sound("soundEngine/Musique.ogg");
		
		player2.setChannel(16);
		player2.play_sound("soundEngine/Musique2.ogg");
		
	}
}
