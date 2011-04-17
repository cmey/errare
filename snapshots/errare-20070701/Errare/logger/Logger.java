/* Errare Humanum Est Project
 * Copyright (C) 2007 Manuel A. Fernandez Montecelo <mafm@users.sourceforge.net>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA
 */

package logger;

import java.io.*;
import java.util.*;
import java.text.*;

/**
 * Logging class for the project, with Singleton pattern.
 * 
 * @author mafm@users.sourceforge.net
 */
public class Logger {

	/** Encodes the class priority levels and names */
	private static class Level {
		/** Only for developers */
		public final static int DEBUG = 1;

		/** Useful information, like "logged into the server succesfully" */
		public final static int INFO = 2;

		/** It's not certain whether it's an error or not, or lead to it */
		public final static int WARNING = 3;

		/** Plain error */
		public final static int ERROR = 4;

		/** Error that will force the program to stop now or soon */
		public final static int FATAL = 5;

		/** Translate the given encoded level to an string */
		public static String translateToString(int level) {
			switch (level) {
			case DEBUG:
				return "DEBUG";
			case INFO:
				return "INFO";
			case WARNING:
				return "WARNING";
			case ERROR:
				return "ERROR";
			case FATAL:
				return "FATAL";
			default:
				return "INVALID LEVEL";
			}
		}
	}

	/** Singleton instance */
	private static Logger INSTANCE;

	/** File name of the log */
	String fileName;

	/** To write the output to a file */
	FileWriter fileWriter;

	/** Date format for the log message */
	DateFormat dateFormatter;

	/** Only print messages with given level, or superior */
	private int levelFilter = Level.DEBUG;

	/** Access to the singleton instance */
	public static Logger instance() {
		if (INSTANCE == null) {
			INSTANCE = new Logger();
		}
		return INSTANCE;
	}

	/** Default constructor */
	private Logger() {
		// level
		setLevelFilter(Level.DEBUG);

		/** TODO: Should be done taking the name from a configuration file */
		fileName = "errare.log";

		// date format
		dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale
				.getDefault());

		// initialize file writer
		try {
			fileWriter = new FileWriter(new File(fileName));
		} catch (Exception e) {
			fileWriter = null;
			System.err.println("Logger: Can't write log to file '" + fileName
					+ "', using console");
		}
	}

	/**
	 * Print a DEBUG message
	 * 
	 * @param className
	 *            Class (module) name that calls this method.
	 * @param message
	 *            The message itself.
	 */
	public static void printDEBUG(String className, String message) {
		instance().print(className, Level.DEBUG, message);
	}

	/**
	 * Print a INFO message
	 * 
	 * @param className
	 *            Class (module) name that calls this method.
	 * @param message
	 *            The message itself.
	 */
	public static void printINFO(String className, String message) {
		instance().print(className, Level.INFO, message);
	}

	/**
	 * Print a WARNING message
	 * 
	 * @param className
	 *            Class (module) name that calls this method.
	 * @param message
	 *            The message itself.
	 */
	public static void printWARNING(String className, String message) {
		instance().print(className, Level.WARNING, message);
	}

	/**
	 * Print a ERROR message
	 * 
	 * @param className
	 *            Class (module) name that calls this method.
	 * @param message
	 *            The message itself.
	 */
	public static void printERROR(String className, String message) {
		instance().print(className, Level.ERROR, message);
	}

	/**
	 * Print a FATAL message
	 * 
	 * @param className
	 *            Class (module) name that calls this method.
	 * @param message
	 *            The message itself.
	 */
	public static void printFATAL(String className, String message) {
		instance().print(className, Level.FATAL, message);
	}

	/**
	 * Print a message from the given class name with the given level. It's the
	 * backend for all the other methods who have the level encoded in their
	 * names.
	 * 
	 * @param className
	 *            Class (module) name that calls this method.
	 * @param level
	 *            The type (priority, severity) of message.
	 * @param message
	 *            The message itself.
	 */
	private synchronized void print(String className, int level, String message) {
		if (level >= levelFilter) {
			// date
			String dateFormatted = dateFormatter.format(Calendar.getInstance()
					.getTime());
			// level
			String levelFormatted = Level.translateToString(level);
			// full message that will be printed
			String fullMessage = dateFormatted + " :: " + levelFormatted
					+ " :: " + className + " :: " + message;

			// writing to the file or the console if we can't access the file
			if (fileWriter == null) {
				System.err.println(fullMessage);
			} else {
				try {
					fileWriter.write(fullMessage + "\n");
					fileWriter.flush();
				} catch (Exception e) {
					fileWriter = null;
					printWARNING("Logger",
							"Writing to log file failed, using console from now on");
					print(className, level, message);
				}
			}
		}
	}

	/**
	 * Set a new level filter (to really log the messages with the given or
	 * superior level). Private so no modules can fiddle with it, we should set
	 * it up only during initialization, based on config file values or so.
	 * 
	 * @param level
	 *            New level to filter out messages.
	 */
	private void setLevelFilter(int level) {
		levelFilter = level;
	}
}
