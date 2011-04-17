/*Errare is a free and crossplatform MMORPG project.
Copyright (C) 2006  Christophe MEYER

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.*/

package graphicsEngine;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Error {

	public static void println(String str){
		System.out.println("GraphicsEngine: "+str);
		BufferedWriter bw;
		try {
			bw = new BufferedWriter(new FileWriter("logfile.txt",true));
			bw.write("GraphicsEngine: "+str);
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("omg bad error writing log file! system fucked!");
		}
	}
}
