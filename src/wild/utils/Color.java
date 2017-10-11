package wild.utils;

public class Color {

	public static final Color BLACK = new Color(0, 0, 0);
	public static final Color WHITE = new Color(255, 255, 255);
	public static final Color RED = new Color(255, 0, 0);
	public static final Color BLUE = new Color(0, 0, 255);
	
	private static final float MAX_BYTE = 255;
	
	public float r;
	public float g;
	public float b;
	public float a;
	
	public Color(float r, float g, float b) {
		this.r = r / MAX_BYTE;
		this.g = g / MAX_BYTE;
		this.b = b / MAX_BYTE;
		this.a = 1;
	}
	
	public Color(float r, float g, float b, float a) {
		this.r = r / MAX_BYTE;
		this.g = g / MAX_BYTE;
		this.b = b / MAX_BYTE;
		this.a = a;
	}
	
}
