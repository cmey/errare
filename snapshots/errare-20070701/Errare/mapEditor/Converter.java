package mapEditor;

public class Converter {

	
	public static long uint2long(int in){
		return (long)(in & 0xFFFFFFFFL);
	}
	
	public static void main(String args[]){
		
		int t = -10;
		System.out.println(uint2long(t));
	}
}
