package wild.engine.components;

import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_REPEAT;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.IntBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;

import wild.engine.maths.Vec2;

public class Tex {
	
	public static final Tex 	BLANK = new Tex();
	
	public static Tex			skybox = new Tex("default_skybox.png", 4, 3, GL_NEAREST);
	public static Tex			test = new Tex("herbe.png", GL_NEAREST);
	public static Tex			herbe = new Tex("block.png", 4, 3, GL_NEAREST);
	
	private int					id;
	private int 				width,
								height;
	private boolean				loaded = false;
	public float				columnH = 1f,
								columnW = 1f;
	public int					posid = 1;
	public Vec2[]			posPoints = {
									new Vec2(0,0),
									new Vec2(1,0),
									new Vec2(1,1),
									new Vec2(0,1)
								};
	public int			rotate = 0;
	
	public Tex() {
		//blank
	}
	
	public Tex(String path, int filter) {
		load(path, filter);
	}
	
	public Tex(String path, float columnW, float columnH, int filter) {
		this.columnH = columnH;
		this.columnW = columnW;
		load(path, filter);
	}
	
	private void load(String path, int filter) {
		int[] pixels = null;
		try {
			BufferedImage image = ImageIO.read(new File("./res/textures/" + path));
			width = image.getWidth();
			height = image.getHeight();
			pixels = new int[width * height];
			image.getRGB(0, 0, width, height, pixels, 0, width);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		int[] data = new int[pixels.length];
		for (int i = 0; i < pixels.length; i++) {
			int a = (pixels[i] & 0xff000000) >> 24;
			int r = (pixels[i] & 0xff0000) >> 16;
			int g = (pixels[i] & 0xff00) >> 8;
			int b = (pixels[i] & 0xff);

			data[i] = a << 24 | b << 16 | g << 8 | r;
		}
		
		int id = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, id);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, filter);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, filter);


		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
		
		this.id = id;
		this.loaded = true;
	}
	
	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void bind() {
		if (this.loaded)
			glBindTexture(GL_TEXTURE_2D, id);
	}
	
	public void unbind() {
		if (this.loaded)
			glBindTexture(GL_TEXTURE_2D, 0);
	}
	
	public void setPosition(int posId)
	{
		this.posid = posId;
		
		int tx = 0;
		int ty = 0;
		
		if (posId < columnW) {
			tx = posId;
			ty = 1;
		}
		else {
			tx = (this.posid % ((int)columnW));
			ty = ((this.posid / ((int)columnH)));
			
			if (tx == 0)
				tx = (int)columnW;
			if (ty == 1)
				ty = 2;
		}
		
		posPoints[0].x = (float)((float)tx / columnW) - ((float)1 / columnW);
		posPoints[0].y = (float)((float)ty / columnH) - ((float)1 / columnH);
		
		posPoints[1].x = (float)posPoints[0].x + (float)((float)1 / columnW);
		posPoints[1].y = (float)posPoints[0].y;
		
		posPoints[2].x = (float)posPoints[1].x;
		posPoints[2].y = (float)posPoints[1].y + ((float)1 / columnH);
		
		posPoints[3].x = (float)posPoints[0].x;
		posPoints[3].y = (float)posPoints[1].y + ((float)1 / columnH);
	}
	
	public void positionRot(int rotation)
	{	
		if (rotation == 0) {
			setPosition(this.posid);
			this.rotate = 0;
			return ;
		}
		if (rotation == 1) {
			setPosition(this.posid);
			this.rotate = 1;
			rotate_z();
			//rotate_horizontal();
		}
		else if (rotation == 2) {
			setPosition(this.posid);
			//rotate_vertical();
			rotate_z();
			rotate_z();
		}
		else if (rotation == 3) {
			//rotate_horizontal();
			//rotate_vertical();
			rotate_z();
			rotate_z();
			rotate_z();
		}
	}
	
	private void rotate_z()
	{
		Vec2[]	newpoints = {
				posPoints[3],
				posPoints[0],
				posPoints[1],
				posPoints[2]
		};
		
		this.posPoints = newpoints;
	}
	
	public void rotate_horizontal()
	{
		
		Vec2[]	newpoints = {
				posPoints[1],
				posPoints[0],
				posPoints[3],
				posPoints[2]
		};
		this.posPoints = newpoints;
	}
	
	public void rotate_vertical()
	{
		Vec2[]	newpoints = {
				posPoints[3],
				posPoints[2],
				posPoints[1],
				posPoints[0]
		};
		this.posPoints = newpoints;
	}
	
}
