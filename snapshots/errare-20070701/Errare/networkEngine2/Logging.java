package networkEngine2;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

/**
 * Logging job class
 * @author linxsam
 */
public class Logging {
	
	public final static int DEBUG = 0, INFO = 1, CRITIC = 2, FATAL = 3;
	
	private int level;
    private String className;
	private File file;
	private FileWriter fw;
	
	/**
	 * instanciates a new console logger
	 * @param lvl the log level to process
	 */
	public Logging(String classname, int lvl) {
        className = classname;
		level = lvl;
		file = null;
	}
	
	/**
	 * change the log processing level
	 * @param lvl the new procession level
	 */
	public void setlevel(int lvl) {
		level = lvl;
	}
	
	/**
	 * instanciates a new file logger
	 * @param lvl the log level to process
	 * @param filename the file the logs will be written to
	 * @throws IOException went something happens during file openning
	 */
	public Logging(int lvl, String filename) throws IOException {
		level = lvl;
		file = new File(filename);
		fw = new FileWriter(file);
	}
	
	/**
	 * writes a new entry to the log
	 * @param lvl this logs priority level
	 * @param text the text to write to the log
	 */
	public void print(int lvl, String text) {
		if(lvl>= level) {
			String le;
			switch(lvl) {
			case 0:
				le = "[DEBUG]";
				break;
			case 1:
				le = "[INFO]";
				break;
			case 2:
				le = "[CRITIC]";
				break;
			case 3:
				le = "[FATAL]";
				break;
			default:
				le = "[UNSET]";
			}
			int jour = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
			int mois = Calendar.getInstance().get(Calendar.MONTH);
			int heure = Calendar.getInstance().get(Calendar.HOUR);
			int min = Calendar.getInstance().get(Calendar.MINUTE);
			int sec = Calendar.getInstance().get(Calendar.SECOND);
			String log = le+" : "+jour+"/"+mois+" "+heure+":"+min+"."+sec+"::"+className+" - "+text;
			if(file == null) toConsole(log);
			else toFile(log);
		}
	}
	
	/**
	 * writes debug priority log
	 * @param text the log to write
	 */
	public void printDEBUG(String text) {
		print(DEBUG,text);
	}
	
	/**
	 * writes info priority log
	 * @param text the log to write
	 */
	public void printINFO(String text) {
		print(INFO,text);
	}
	
	/**
	 * writes critic priority log
	 * @param text the log to write
	 */
	public void printCRITIC(String text) {
		print(CRITIC,text);
	}
	
	/**
	 * writes fatal priority log
	 * @param text the log to write
	 */
	public void printFATAL(String text) {
		print(FATAL,text);
	}
	
	private void toConsole(String text) {
		System.out.println(text);
	}
	
	private void toFile(String text) {
		try {
			fw.write(text+"\n");
			fw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
