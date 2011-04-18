/* Errare Humanum Est Project
 * Copyright (C) 2008 Christophe Meyer <christophe.meyer@esial.net>
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
package web;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.*;
import logger.Logger;

public class LoggerClient {

    DataOutputStream writer;
    OutputStream out;
    boolean valid;
    private static LoggerClient INSTANCE = new LoggerClient();
    Socket sok;

    private LoggerClient() {
        /** CONNECTING */
        try {
            System.err.println("Trying to join log server...");
            InetAddress ineta = InetAddress.getByName("cyberchrist.hd.free.fr");
            sok = new Socket(ineta, 13668);
            out = sok.getOutputStream();
            writer = new DataOutputStream(out);
            writer.writeByte(127);
            valid = true;
        } catch (Exception e) {
            Logger.printExceptionERROR(e,false);
            valid = false;
        }
        System.err.println("LoggerClient initialized");
    }

    public static LoggerClient getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new LoggerClient();
        }
        return INSTANCE;
    }
    
    public static PrintWriter getPrintWriter(){
        LoggerClient instance = getInstance();
        if(!instance.valid)
            return null;
        else
            return new PrintWriter(instance.out);
    }

    public static void send(String msg) {
        if(!getInstance().valid)
            return;
        LoggerClientWriter lcw = new LoggerClientWriter(msg);
        Thread thread = new Thread(lcw);
        thread.start();
    }
    
    public void quit(){
        try {
            sok.close();
        } catch (IOException ex) {
            Logger.printExceptionERROR(ex,false);
        }
    }
}
