package wild.editor;

import static org.lwjgl.opengl.GL11.GL_FILL;
import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11.GL_LINE;
import static org.lwjgl.opengl.GL11.glLineWidth;
import static org.lwjgl.opengl.GL11.glPolygonMode;

import java.util.ArrayList;
import java.util.List;

import wild.engine.components.shape.Block;
import wild.engine.lib.lwjgl.Lwjgl;
import wild.engine.maths.Vec3;
import wild.utils.Color;

public class ECRaycast {
	
	private float		dist;
	private float		precision;
	private Vec3		position;
	private Vec3		direction;
	private RaycastHit		hit;
	private Vec3 		hitPoint;
	private ArrayList<Vec3> points = new ArrayList<Vec3>();

	public ECRaycast(float dist, float precision)
	{
		this.dist = dist;
		this.precision = precision;
		this.position = new Vec3();
		this.direction = new Vec3();
		for (int i = 0; i < 10 * 16; i++) {
			points.add(new Vec3());
		}
	}

	public void update(WorldEditor core)
	{
		hit = null;
		hitPoint = null;
		for (float i = 0; i < dist; i += precision)
		{
			Vec3 point = position.copy().add(direction.copy().mul(i));
			
			//System.out.println(point.toString());
			//System.out.println(direction.toString());
			Block block = core.getBlock((int)point.x, (int)point.y, (int)point.z);
			
			if (block != null)
			{
				hit = new RaycastHit(block, block.pos.copy());
				hitPoint = point;
				return;
			}
		}
	}
	
	public static Vec3 move(Vec3 pos, Vec3 dir, float amt) {
		pos = pos.copy().add(dir.mul(amt));
		return (pos);
	}

	public Vec3 getPosition()
	{
		return position;
	}

	public void setPosition(Vec3 position)
	{
		this.position = position;
	}

	public Vec3 getDirection()
	{
		return direction;
	}

	public void setDirection(Vec3 direction)
	{
		this.direction = direction;
	}

	public RaycastHit getHit()
	{
		return hit;
	}
	
	public Vec3 getExactHitPoint()
	{
		return hitPoint;
	}
	
	public class RaycastHit
	{
		private Block		block;
		private Vec3	blockPosition;

		public RaycastHit(Block block, Vec3 blockPosition)
		{
			this.block = block;
			this.blockPosition = blockPosition;
		}
		
		public Block getBlock()
		{
			return block;
		}
		
		public Vec3 getBlockPosition()
		{
			return blockPosition;
		}
	}
}
