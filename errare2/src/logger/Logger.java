/* Errare Humanum Est Project
 * Copyright (C) 2007 Manuel A. Fernandez Montecelo <mafm@users.sourceforge.net>
 * Copyright (C) 2008 Christophe Meyer <christophe.meyer@esial.net> added networking
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
import javax.swing.JOptionPane;
import web.LoggerClient;

/**
 * Logging class for the project, with Singleton pattern.
 * 
 * @author mafm@users.sourceforge.net
 * 
 * 
 * Update : automatic discovery of the file name, line number, class name and method name
 * 			of the caller. No more need to pass a String (the ClassName) when 
 *			calling a print.
 * Added networking centralization for logs.    
 *     
 * @author Christophe Meyer
 *
 */
public class Logger {

    public static void initialize() {
        int ret = JOptionPane.showConfirmDialog(null, "Errare can send it's logging informations over the network to the developer\nto help him correct problems. Do you accept to help ?","Logging over network",JOptionPane.YES_NO_OPTION);
                if(ret==JOptionPane.YES_OPTION){
                    Logger.setNetworked(true);
                }else{
                    Logger.setNetworked(false);
                }

    }

    public static void setNetworked(boolean b) {
        Logger.instance().networked = b;
        if (!b) {
            Logger.printWARNING("User choosed not to allow networked logging.");
        } else {
            Logger.printINFO("User accepted networked logging.");
        }
    }
    private boolean networked;

    /** Encodes the class priority levels and names */
    private static class Level {

        /** Only for developers */
        public final static int DEBUG = 1;
        /** Useful information, like "logged into the server succesfully" */
        public final static int INFO = 2;
        /** It's not certain whether it's an error or not, or lead to it */
        public final static int WARNING = 3;
        /** Plain error, still managable */
        public final static int ERROR = 4;
        /** Error that will force the program to stop now or soon */
        public final static int FATAL = 5;

        /** Translate the given encoded level to an string */
        public static String translateToString(int level) {
            switch (level) {
                case DEBUG:
                    return "   DEBUG";
                case INFO:
                    return "   INFO";
                case WARNING:
                    return "(x)WARNING";
                case ERROR:
                    return "(x)ERROR";
                case FATAL:
                    return "(x)FATAL";
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
    /** How much to go up in the stack trace */
    int stackTraceUpper = System.getProperty("os.name").startsWith("Mac") ? 5 : 4;
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
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        // initialize file writer
        try {
            fileWriter = new FileWriter(new File(fileName));
        } catch (Exception e) {
            fileWriter = null;
            System.err.println("Logger: Can't write log to file '" + fileName + "', using console");
        }
    }

    /**
     * Print a DEBUG message
     * 
     * @param message
     *            The message itself.
     */
    public static void printDEBUG(String message) {
        instance().print(Level.DEBUG, message);
    }

    /**
     * Print a INFO message
     * 
     * @param message
     *            The message itself.
     */
    public static void printINFO(String message) {
        instance().print(Level.INFO, message);
    }

    /**
     * Print a WARNING message
     * 
     * @param message
     *            The message itself.
     */
    public static void printWARNING(String message) {
        instance().print(Level.WARNING, message);
    }

    /**
     * Print a ERROR message
     * 
     * @param message
     *            The message itself.
     */
    public static void printERROR(String message) {
        instance().print(Level.ERROR, message);
    }

    /**
     * Print a FATAL message
     * 
     * @param message
     *            The message itself.
     */
    public static void printFATAL(String message) {
        instance().print(Level.FATAL, message);
    }

    public static void printExceptionERROR(Exception e, boolean canBeNetworked) {
        PrintWriter pw = new PrintWriter(instance().fileWriter);
        e.printStackTrace(pw);
        pw.flush();
        if (instance().networked && canBeNetworked) {
            PrintWriter networkPrintWriter = LoggerClient.getPrintWriter();
            if (networkPrintWriter != null) {
                e.printStackTrace(networkPrintWriter);
                networkPrintWriter.flush();
            }
        }
        e.printStackTrace();
    }

    public static void printExceptionERROR(Exception e) {
        printExceptionERROR(e, true);
    }

    public static void printErrorERROR(Error er, boolean canBeNetworked) {
        PrintWriter pw = new PrintWriter(instance().fileWriter);
        er.printStackTrace(pw);
        pw.flush();
        if (instance().networked && canBeNetworked) {
            PrintWriter networkPrintWriter = LoggerClient.getPrintWriter();
            if (networkPrintWriter != null) {
                er.printStackTrace(networkPrintWriter);
                networkPrintWriter.flush();
            }
        }
        er.printStackTrace();
    }

    public static void printErrorERROR(Error er) {
        printErrorERROR(er, true);
    }

    /**
     * Print a message from the given class name with the given level. It's the
     * backend for all the other methods who have the level encoded in their
     * names.
     * 
     * @param level
     *            The type (priority, severity) of message.
     * @param message
     *            The message itself.
     */
    private synchronized void print(int level, String message) {
        // stack infos
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();
        if (level >= levelFilter && stack.length >= stackTraceUpper) {
            // date
            String dateFormatted = dateFormatter.format(Calendar.getInstance().getTime());
            // level
            String levelFormatted = Level.translateToString(level);
            // more infos
            StackTraceElement stacky = stack[stackTraceUpper - 1];
            // full message that will be printed
            String fullMessage = levelFormatted + " :: " + dateFormatted + " :: " +
                    stacky.getFileName() + " line " + stacky.getLineNumber() + " :: " +
                    stacky.getClassName() + " class :: " + stacky.getMethodName() + "() :: " + message + "\n";

            System.err.print(fullMessage);

            try {
                fileWriter.write(fullMessage);
                fileWriter.flush();
                if (instance().networked) {
                    LoggerClient.send(fullMessage);
                }
            } catch (Exception e) {
                fileWriter = null;
                printWARNING("Writing to log file failed, reason: " + e.getMessage() +
                        ", using console from now on");
                print(level, message);
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
