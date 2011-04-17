/*Errare is a free and crossplatform MMORPG project.
Copyright (C) 2006  Martin DELEMOTTE

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.*/

package databaseEngine;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

public class Test {

	public static void main(String[] args) throws ParserConfigurationException, TransformerFactoryConfigurationError, TransformerException {
		DatabaseEngine t = new DatabaseEngine();
		t.set("sample.errare.is.the.best.game", 1);
		System.out.println("errare is the best game: "+t.getInt("sample.errare.is.the.best.game"));
		
		t.set("sample.errare.is.the.best.game", "test");
		System.out.println("errare is the best game: "+t.get("sample.errare.is.the.best.game"));
		
		t.set("sample.errare.is.the.best.game", true);
		System.out.println("errare is the best game: "+t.getBoolean("sample.errare.is.the.best.game"));
		
	}

}
