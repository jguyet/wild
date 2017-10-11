package wild.world;

import static org.lwjgl.opengl.GL11.*;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Vector4f;

import wild.engine.components.Tex;
import wild.engine.components.shape.Block;
import wild.engine.lib.lwjgl.Lwjgl;
import wild.engine.lib.lwjgl.OpenGLBuffer;
import wild.engine.lib.lwjgl.TexturedSquare;
import wild.engine.lib.lwjgl.shaders.Shader;
import wild.engine.maths.Mat4;
import wild.engine.maths.Vec2;
import wild.engine.maths.Vec3;
import wild.utils.Color;
import wild.utils.RGB;
import wild.world.noise.NoiseGeneration;

public class Chunk {

	public static final int SIZE = 10;
	public int x, y, z;
	public Block[][][] buffer;
	
	public int randomHeight;
	
	public OpenGLBuffer openGLbuffer = new OpenGLBuffer();
	
	public World world;
	private NoiseGeneration noise;
	
	public boolean created = false;
	public boolean initialized = false;
	
	public Chunk(World world, int x, int y, int z, NoiseGeneration noise, int randomHeight)
	{
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
		this.noise = noise;
		this.randomHeight = randomHeight;
		intialize();
		textureChunk();
	}
	
	public Chunk(World world, int x, int y, int z)
	{
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
		this.noise = null;
		this.randomHeight = 0;
	}
	
	public void destruct() {
		initialized = false;
		this.openGLbuffer.destruct();
	}
	
	private void intialize()
	{	
		buffer = new Block[SIZE][SIZE][SIZE];
		for (int x = 0; x < SIZE; x++) {
			for (int y = 0; y < SIZE; y++) {
				for (int z = 0; z < SIZE; z++) {
					
					int xx = this.x * SIZE + x;
					int yy = this.y * SIZE + y;
					int zz = this.z * SIZE + z;
					
					if (noise.getExactNoise(xx, zz) > yy - randomHeight) {
						
						Block s = Block.GRASS_BLOCK;
						buffer[x][y][z] = s;
					}
				}
			}
		}
	}
	
	public void textureChunk()
	{
		for (int x = 0; x < SIZE; x++) {
			for (int y = 0; y < SIZE; y++) {
				for (int z = 0; z < SIZE; z++) {
					
					if (buffer[x][y][z] == null)
						continue ;
					
					boolean up = getBlock(x, y + 1, z) != null;
					
					if (up) {
						buffer[x][y][z] = Block.EARTH_BLOCK;
					} else {
						buffer[x][y][z] = Block.GRASS_BLOCK;
					}
				}
			}
		}
	}
	
	public void clearChunk()
	{
		for (int x = 0; x < SIZE; x++) {
			for (int y = 0; y < SIZE; y++) {
				for (int z = 0; z < SIZE; z++) {
					
					int xx = this.x * SIZE + x;
					int yy = this.y * SIZE + y;
					int zz = this.z * SIZE + z;
					
					if (buffer[x][y][z] == null)
						continue ;
					
					boolean up = getBlock(x, y + 1, z) == null;
					boolean down = getBlock(x, y - 1, z) == null;
					boolean left = world.getBlock(xx - 1, yy, zz) == null;
					boolean right = world.getBlock(xx + 1, yy, zz) == null;
					boolean front = world.getBlock(xx, yy, zz - 1) == null;
					boolean back = world.getBlock(xx, yy, zz + 1) == null;
					
					if (!up && !down && !left && !right && !front && !back) {
						continue ;
					}
					
					Block b = buffer[x][y][z];
					Vec3 v = new Vec3 (xx, yy, zz);
					
					if (up) {
						/*int rand = Utils.random_int(1, 1000);
						if (rand < 3) {
							new Tree(new Vector3f(xx, yy, zz));
							System.out.println("NEW TREE");
						}*/
						System.out.println("AAAAA");
						b.gettop(openGLbuffer, v);
					}
					if (down) {
						//b.getbottom(openGLbuffer, v);
					}
					if (left) {
						//b.getleft(openGLbuffer, v);
					}
					if (right) {
						//b.getright(openGLbuffer, v);
					}
					if (front) {
						//b.getfront(openGLbuffer, v);
					}
					if (back) {
						//b.getback(openGLbuffer, v);
					}
				}
			}
		}
	}
	
	public Block getBlock(int x, int y, int z) {
		if (x < 0 || x >= SIZE || y < 0 || y >= SIZE || z < 0 || z >= SIZE)
			return null;
		if (buffer[x][y][z] != null)
			return (buffer[x][y][z]);
		return null;
	}
	
	public static final TexturedSquare GRASS_BLOCK = new TexturedSquare() {
		@Override
		public void top(Tex texture) {
			texture.setPosition(2);
			texture.positionRot(3);
			texture.rotate_vertical();
		}

		@Override
		public void left(Tex texture) {
			texture.setPosition(5);
			texture.positionRot(1);
		}

		@Override
		public void front(Tex texture) {
			texture.setPosition(8);
			texture.positionRot(2);
		}

		@Override
		public void right(Tex texture) {
			texture.setPosition(7);
			texture.positionRot(2);
		}

		@Override
		public void back(Tex texture) {
			texture.setPosition(6);
			texture.positionRot(2);
		}

		@Override
		public void bottom(Tex texture) {
			texture.setPosition(10);
		}
	};
	
	public static final TexturedSquare EARTH_BLOCK = new TexturedSquare() {
		@Override
		public void top(Tex texture) {
			texture.setPosition(10);
			texture.positionRot(3);
			texture.rotate_vertical();
		}

		@Override
		public void left(Tex texture) {
			texture.setPosition(10);
			texture.positionRot(1);
		}

		@Override
		public void front(Tex texture) {
			texture.setPosition(10);
			texture.positionRot(2);
		}

		@Override
		public void right(Tex texture) {
			texture.setPosition(10);
			texture.positionRot(2);
		}

		@Override
		public void back(Tex texture) {
			texture.setPosition(10);
			texture.positionRot(2);
		}

		@Override
		public void bottom(Tex texture) {
			texture.setPosition(10);
		}
	};
	
	public void render() {
		
		if (initialized == false)
			return ;
		
		int var_block_color = Shader.MAIN.getUniform("block_color");
		
		glEnable(GL_TEXTURE_2D);
		
		Tex.test.bind();
		
		Shader.MAIN.bind();
		
		Shader.MAIN.setColor4f(var_block_color, RGB.AQUA);

		this.openGLbuffer.render();
		
		Tex.test.unbind();
		
		glDisable(GL_TEXTURE_2D);
		
		Shader.MAIN.unbind();
		
		/*int xx = this.x * SIZE;
		int yy = this.y * SIZE;
		int zz = this.z * SIZE;
		
		Lwjgl.addLinedSquare(new Block(new Vector3f(xx + (SIZE / 2) - 0.5f, yy + (SIZE / 2) - 0.5f, zz + (SIZE / 2) - 0.5f), SIZE, Color.BLACK));*/
	}
}
