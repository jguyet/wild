package wild.utils;

public class Time {
	
	private static long 	delta;
	private static long		frame;
	
	public static long getTime(){
		return System.nanoTime();
	}
	
	public static long getDelta(){
		return delta;
	}
	
	public static void setDelta(long delta){
		Time.delta = delta;
	}

	public static long getFrame() {
		return frame;
	}

	public static void setFrame(long frame) {
		Time.frame = frame;
	}
	
}
