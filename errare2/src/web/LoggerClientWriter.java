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

import logger.Logger;

/**
 *
 * @author christophe
 */
public class LoggerClientWriter implements Runnable{

    private String toWrite;
    
    public LoggerClientWriter(String msg){
        this.toWrite = msg;
    }
    
    public void run() {
        LoggerClient instance = LoggerClient.getInstance();
        if(!instance.valid)
            return;
        
        try {
            instance.writer.writeChars(toWrite);
            instance.writer.flush();
        } catch (Exception e) {
            Logger.printExceptionERROR(e, false);
        }
    }

}
