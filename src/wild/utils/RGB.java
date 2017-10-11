package wild.utils;

public class RGB {

	private static final int BIT_MASK = 0xff;

    /**
     * White, or (0xFF,0xFF,0xFF) in (R,G,B)
     */
    public static final RGB WHITE = fromRGB(0xFFFFFF);

    /**
     * Silver, or (0xC0,0xC0,0xC0) in (R,G,B)
     */
    public static final RGB SILVER = fromRGB(0xC0C0C0);

    /**
     * Gray, or (0x80,0x80,0x80) in (R,G,B)
     */
    public static final RGB GRAY = fromRGB(0x808080);

    /**
     * Black, or (0x00,0x00,0x00) in (R,G,B)
     */
    public static final RGB BLACK = fromRGB(0x000000);

    /**
     * Red, or (0xFF,0x00,0x00) in (R,G,B)
     */
    public static final RGB RED = fromRGB(0xFF0000);

    /**
     * Maroon, or (0x80,0x00,0x00) in (R,G,B)
     */
    public static final RGB MAROON = fromRGB(0x800000);

    /**
     * Yellow, or (0xFF,0xFF,0x00) in (R,G,B)
     */
    public static final RGB YELLOW = fromRGB(0xFFFF00);

    /**
     * Olive, or (0x80,0x80,0x00) in (R,G,B)
     */
    public static final RGB OLIVE = fromRGB(0x808000);

    /**
     * Lime, or (0x00,0xFF,0x00) in (R,G,B)
     */
    public static final RGB LIME = fromRGB(0x00FF00);

    /**
     * Green, or (0x00,0x80,0x00) in (R,G,B)
     */
    public static final RGB GREEN = fromRGB(0x008000);

    /**
     * Aqua, or (0x00,0xFF,0xFF) in (R,G,B)
     */
    public static final RGB AQUA = fromRGB(0x00FFFF);

    /**
     * Teal, or (0x00,0x80,0x80) in (R,G,B)
     */
    public static final RGB TEAL = fromRGB(0x008080);

    /**
     * Blue, or (0x00,0x00,0xFF) in (R,G,B)
     */
    public static final RGB BLUE = fromRGB(0x0000FF);

    /**
     * Navy, or (0x00,0x00,0x80) in (R,G,B)
     */
    public static final RGB NAVY = fromRGB(0x000080);

    /**
     * Fuchsia, or (0xFF,0x00,0xFF) in (R,G,B)
     */
    public static final RGB FUCHSIA = fromRGB(0xFF00FF);

    /**
     * Purple, or (0x80,0x00,0x80) in (R,G,B)
     */
    public static final RGB PURPLE = fromRGB(0x800080);

    /**
     * Orange, or (0xFF,0xA5,0x00) in (R,G,B)
     */
    public static final RGB ORANGE = fromRGB(0xFFA500);

    public int r;
    public int g;
    public int b;
    public int a;

    /**
     * Creates a new Color object from a red, green, and blue
     *
     * @param red integer from 0-255
     * @param green integer from 0-255
     * @param blue integer from 0-255
     * @return a new Color object for the red, green, blue
     * @throws IllegalArgumentException if any value is strictly >255 or <0
     */
    public static RGB fromRGB(int red, int green, int blue) throws IllegalArgumentException {
        return new RGB(red, green, blue);
    }

    /**
     * Creates a new Color object from a blue, green, and red
     *
     * @param blue integer from 0-255
     * @param green integer from 0-255
     * @param red integer from 0-255
     * @return a new Color object for the red, green, blue
     * @throws IllegalArgumentException if any value is strictly >255 or <0
     */
    public static RGB fromBGR(int blue, int green, int red) throws IllegalArgumentException {
        return new RGB(red, green, blue);
    }

    /**
     * Creates a new color object from an integer that contains the red,
     * green, and blue bytes in the lowest order 24 bits.
     *
     * @param rgb the integer storing the red, green, and blue values
     * @return a new color object for specified values
     * @throws IllegalArgumentException if any data is in the highest order 8
     *     bits
     */
    public static RGB fromRGB(int rgb) throws IllegalArgumentException {
        return fromRGB(rgb >> 16 & BIT_MASK, rgb >> 8 & BIT_MASK, rgb >> 0 & BIT_MASK);
    }

    /**
     * Creates a new color object from an integer that contains the blue,
     * green, and red bytes in the lowest order 24 bits.
     *
     * @param bgr the integer storing the blue, green, and red values
     * @return a new color object for specified values
     * @throws IllegalArgumentException if any data is in the highest order 8
     *     bits
     */
    public static RGB fromBGR(int bgr) throws IllegalArgumentException {
        return fromBGR(bgr >> 16 & BIT_MASK, bgr >> 8 & BIT_MASK, bgr >> 0 & BIT_MASK);
    }

    public RGB(int red, int green, int blue) {
        this.r = red;
        this.g = green;
        this.b = blue;
        this.a = 1;
    }
    
    public RGB(int red, int green, int blue, int a) {
        this.r = red;
        this.g = green;
        this.b = blue;
        this.a = a;
    }

    /**
     *
     * @return An integer representation of this color, as 0xRRGGBB
     */
    public int asRGB() {
        return r << 16 | g << 8 | b << 0;
    }

    /**
     *
     * @return An integer representation of this color, as 0xBBGGRR
     */
    public int asBGR() {
        return b << 16 | g << 8 | r << 0;
    }


    /**
     * Creates a new color with its RGB components changed as if it was dyed
     * with the colors passed in, replicating vanilla workbench dyeing
     *
     * @param colors The colors to dye with
     * @return A new color with the changed rgb components
     */
    // TODO: Javadoc what this method does, not what it mimics. API != Implementation
    public RGB mixColors(RGB... colors) {

        int totalRed = r;
        int totalGreen = g;
        int totalBlue = b;
        int totalMax = Math.max(Math.max(totalRed, totalGreen), totalBlue);
        for (RGB color : colors) {
            totalRed += color.r;
            totalGreen += color.g;
            totalBlue += color.b;
            totalMax += Math.max(Math.max(color.r, color.g), color.b);
        }

        float averageRed = totalRed / (colors.length + 1);
        float averageGreen = totalGreen / (colors.length + 1);
        float averageBlue = totalBlue / (colors.length + 1);
        float averageMax = totalMax / (colors.length + 1);

        float maximumOfAverages = Math.max(Math.max(averageRed, averageGreen), averageBlue);
        float gainFactor = averageMax / maximumOfAverages;

        return RGB.fromRGB((int) (averageRed * gainFactor), (int) (averageGreen * gainFactor), (int) (averageBlue * gainFactor));
    }

    @Override
    public String toString() {
        return "Color:[rgb0x" + Integer.toHexString(r).toUpperCase() + Integer.toHexString(g).toUpperCase() + Integer.toHexString(b).toUpperCase() + "]";
    }
}
