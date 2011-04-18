package md5Loader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

public class MD5Loader {

	public static void main(String[] args) throws MalformedURLException, IOException {
		ModelReader mr = new ModelReader("md5Loader/box_skin.md5mesh");
		//ModelReader mr = new ModelReader("md5Loader/Bob_with_lamp_clean.md5mesh");
		//ModelReader mr = new ModelReader("md5Loader/man_noanim.md5mesh");
		mr.readModel();
	}
	
}
