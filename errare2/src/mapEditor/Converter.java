package mapEditor;

public class Converter {

	
	public static long uint2long(int in){
		return (long)(in & 0xFFFFFFFFL);
	}
	
}
