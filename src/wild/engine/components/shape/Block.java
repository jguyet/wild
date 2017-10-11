package wild.engine.components.shape;

import wild.engine.components.Tex;
import wild.engine.lib.lwjgl.OpenGLBuffer;
import wild.engine.maths.Vec3;
import wild.utils.Color;
import wild.utils.RGB;

public class Block {

	public static final Block GRASS_BLOCK = new Block(1f, new Color(50,205,50, 1));
	public static final Block EARTH_BLOCK = new Block(1f, new Color(111,105,93, 1));
	public static final Block WOOD_BLOCK = new Block(1f, new Color(164,133,73, 1));
	public static final Block TREE_BLOCK = new Block(1f, new Color(49,121,61, 1));
	public static final Block TRANSPARENT_BLOCK = new Block(1f, new Color(0,0,0,0));
	
	public float	scale;
	public Color 	color = new Color(0,0,0,1);
	public Tex		texture;
	public Vec3	pos;
	
	public boolean	select = false;
	
	public Block(float scale)
	{
		this.scale = scale;
	}
	
	public Block(float scale, Color color)
	{
		this.scale = scale;
		this.color = color;
	}
	
	public Block(float scale, Color color, Tex texture)
	{
		this.scale = scale;
		this.color = color;
		this.texture = texture;
	}
	
	public Block(Vec3 pos, float scale)
	{
		this.pos = pos;
		this.scale = scale;
	}
	
	public Block(Vec3 pos, float scale, Color color)
	{
		this.pos = pos;
		this.scale = scale;
		this.color = color;
	}
	
	public Block(Vec3 pos, float scale, Color color, Tex texture)
	{
		this.pos = pos;
		this.scale = scale;
		this.color = color;
		this.texture = texture;
	}
	
	public float[] getBlock(Vec3 pos) {
		
		float x = pos.x;
		float y = pos.y;
		float z = pos.z;
		
		float[] data = new float[] {
				//front
				x, y,z,								color.r * 0.9f, color.g * 0.9f, color.b * 0.9f, color.a,
				x+scale, y,z,						color.r * 0.9f, color.g * 0.9f, color.b * 0.9f, color.a,
				x+scale, y+scale,z,					color.r * 0.9f, color.g * 0.9f, color.b * 0.9f, color.a,
				x, y+scale,z,						color.r * 0.9f, color.g * 0.9f, color.b * 0.9f, color.a,
				//back
				x+scale, y,z+scale,					color.r * 0.9f, color.g * 0.9f, color.b * 0.9f, color.a,
				x, y,z+scale, 						color.r * 0.9f, color.g * 0.9f, color.b * 0.9f, color.a,
				x, y+scale,z+scale, 				color.r * 0.9f, color.g * 0.9f, color.b * 0.9f, color.a,
				x+scale, y+scale,z+scale, 			color.r * 0.9f, color.g * 0.9f, color.b * 0.9f, color.a,
				// right side
				x+scale, y,z, 						color.r * 0.8f, color.g * 0.8f, color.b * 0.8f, color.a,
				x+scale, y,z+scale, 				color.r * 0.8f, color.g * 0.8f, color.b * 0.8f, color.a,
				x+scale, y+scale,z+scale, 			color.r * 0.8f, color.g * 0.8f, color.b * 0.8f, color.a,
				x+scale, y+scale,z, 				color.r * 0.8f, color.g * 0.8f, color.b * 0.8f, color.a,
				//left side
				x, y,z, 							color.r * 0.8f, color.g, color.b, color.a,
				x, y+scale,z, 						color.r * 0.8f, color.g, color.b, color.a,
				x, y+scale,z+scale, 				color.r * 0.8f, color.g, color.b, color.a,
				x, y,z+scale, 						color.r * 0.8f, color.g, color.b, color.a,
				//bottom
				x, y, z, 							color.r * 0.7f, color.g * 0.7f, color.b * 0.7f, color.a,
				x, y, z + scale, 					color.r * 0.7f, color.g * 0.7f, color.b * 0.7f, color.a,
				x + scale, y, z + scale, 			color.r * 0.7f, color.g * 0.7f, color.b * 0.7f, color.a,
				x + scale, y, z, 					color.r * 0.7f, color.g * 0.7f, color.b * 0.7f, color.a,
				//top
				x, y + scale, z, 					color.r * 1f, color.g * 1f, color.b * 1f, color.a,
				x, y + scale, z + scale, 			color.r * 1f, color.g * 1f, color.b * 1f, color.a,
				x + scale, y + scale, z + scale, 	color.r * 1f, color.g * 1f, color.b * 1f, color.a,
				x + scale, y + scale, z, 			color.r * 1f, color.g * 1f, color.b * 1f, color.a
		};
		return (data);
	}
	
	public void gettop(OpenGLBuffer buffer, Vec3 pos) {
		
		float x = pos.x;
		float y = pos.y;
		float z = pos.z;
		
		
		buffer.add(x, y + scale, z,						color.r, color.g, color.b, color.a);
		buffer.add(x, y + scale, z + scale,				color.r, color.g, color.b, color.a);
		buffer.add(x + scale, y + scale, z + scale,		color.r, color.g, color.b, color.a);
		buffer.add(x + scale, y + scale, z,				color.r, color.g, color.b, color.a);
	}
	
	public void getbottom(OpenGLBuffer buffer, Vec3 pos) {
		
		float x = pos.x;
		float y = pos.y;
		float z = pos.z;
		
		buffer.add(x, y, z,						color.r * 0.9f, color.g * 0.9f, color.b * 0.9f, color.a);
		buffer.add(x, y, z + scale,				color.r * 0.9f, color.g * 0.9f, color.b * 0.9f, color.a);
		buffer.add(x + scale, y, z + scale, 	color.r * 0.9f, color.g * 0.9f, color.b * 0.9f, color.a);
		buffer.add(x + scale, y, z,				color.r * 0.9f, color.g * 0.9f, color.b * 0.9f, color.a);
	}
	
	public void getfront(OpenGLBuffer buffer, Vec3 pos) {
		
		float x = pos.x;
		float y = pos.y;
		float z = pos.z;
		
		buffer.add(x, y,z,						color.r * 0.7f, color.g * 0.7f, color.b * 0.7f, color.a);
		buffer.add(x+scale, y,z,				color.r * 0.7f, color.g * 0.7f, color.b * 0.7f, color.a);
		buffer.add(x+scale, y+scale,z,			color.r * 0.7f, color.g * 0.7f, color.b * 0.7f, color.a);
		buffer.add(x, y+scale,z,				color.r * 0.7f, color.g * 0.7f, color.b * 0.7f, color.a);
	}
	
	public void getback(OpenGLBuffer buffer, Vec3 pos) {
		
		float x = pos.x;
		float y = pos.y;
		float z = pos.z;
		
		buffer.add(x+scale, y,z+scale,			color.r * 0.7f, color.g * 0.7f, color.b * 0.7f, color.a);
		buffer.add(x, y,z+scale,				color.r * 0.7f, color.g * 0.7f, color.b * 0.7f, color.a);
		buffer.add(x, y+scale,z+scale,			color.r * 0.9f, color.g * 0.9f, color.b * 0.9f, color.a);
		buffer.add(x+scale, y+scale,z+scale,	color.r * 0.9f, color.g * 0.9f, color.b * 0.9f, color.a);
	}
	
	public void getright(OpenGLBuffer buffer, Vec3 pos) {
		
		float x = pos.x;
		float y = pos.y;
		float z = pos.z;
		
		buffer.add(x+scale, y,z,				color.r * 0.7f, color.g * 0.7f, color.b * 0.7f, color.a);
		buffer.add(x+scale, y,z+scale,			color.r * 0.7f, color.g * 0.7f, color.b * 0.7f, color.a);
		buffer.add(x+scale, y+scale,z+scale, 	color.r * 0.9f, color.g * 0.9f, color.b * 0.9f, color.a);
		buffer.add(x+scale, y+scale,z, 			color.r * 0.9f, color.g * 0.9f, color.b * 0.9f, color.a);
	}
	
	public void getleft(OpenGLBuffer buffer, Vec3 pos) {
		
		float x = pos.x;
		float y = pos.y;
		float z = pos.z;
		
		buffer.add(x, y,z, 						color.r * 0.7f, color.g * 0.7f, color.b * 0.7f, color.a);
		buffer.add(x, y+scale,z, 				color.r * 0.9f, color.g * 0.9f, color.b * 0.9f, color.a);
		buffer.add(x, y+scale,z+scale, 			color.r * 0.9f, color.g * 0.9f, color.b * 0.9f, color.a);
		buffer.add(x, y,z+scale, 				color.r * 0.7f, color.g * 0.7f, color.b * 0.7f, color.a);
	}
	
}
