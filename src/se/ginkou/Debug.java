package se.ginkou;

public class Debug {
	private static final boolean DEBUGGING = true;
	
	public static void out(String output) {
		if (DEBUGGING) {
			System.out.println(output);
		}
	}
	
	public static void err(String error) {
		if (DEBUGGING) {
			System.err.println(error);
		}
	}
}
