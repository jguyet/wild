package wild.editor;

import java.awt.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import wild.engine.components.Tex;
import wild.engine.components.shape.Block;
import wild.engine.lib.lwjgl.Lwjgl;
import wild.engine.lib.lwjgl.TexturedSquare;
import wild.engine.maths.Vec3;
import wild.utils.Color;

public class WorldEditor {

	public static final int SIZE = 20;
	
	public boolean initialized = false;
	
	private Map<BlockKey, Block>	blocks = new HashMap<BlockKey, Block>();
	private Map<BlockKey, Block>	ground = new HashMap<BlockKey, Block>();
	
	public WorldEditor()
	{
		intializeGround();
		initialized = true;
	}
	
	public void destruct() {
		initialized = false;
	}
	
	private void intializeGround()
	{
		int xx = -(SIZE / 2);
		int zz = -(SIZE / 2);
		int xxmax = SIZE / 2;
		int zzmax = SIZE / 2;
		
		for (int x = xx; x < xxmax; x++) {
			for (int z = zz; z < zzmax; z++) {
				
				Block s = new Block(new Vec3(x, 0, z), 1, new Color(111,105,93, 1));
				ground.put(new BlockKey(x, 0, z), s);
			}
		}
		
		for (int x = xx; x < xxmax; x++) {
			for (int z = zz; z < zzmax; z++) {
				for (int y = 1; y < 2; y++) {
					addBlock(x, y, z);
				}
			}
		}
	}
	
	public Block getBlock(int x, int y, int z) {
		if (blocks.containsKey(new BlockKey(x, y, z)))
			return blocks.get(new BlockKey(x, y, z));
		return ground.get(new BlockKey(x, y, z));
	}
	
	public boolean collide(float x, float y, float z, Vec3 pos) {
		float dist = 0.50f;
		
		int x0 = (int)(pos.x + x - dist);
		int x1 = (int)(pos.x + x + dist);
		
		int y0 = (int)(pos.y + y - dist);
		int y1 = (int)(pos.y + y + dist);
		
		int z0 = (int)(pos.z + z - dist);
		int z1 = (int)(pos.z + z + dist);
		
		if (getBlock(x0, y1, z1) != null) return true;
		
		if (getBlock(x0, y0, z0) != null) return true;
		if (getBlock(x1, y0, z0) != null) return true;
		if (getBlock(x1, y1, z0) != null) return true;
		if (getBlock(x0, y1, z0) != null) return true;
		
		if (getBlock(x0, y0, z1) != null) return true;
		if (getBlock(x1, y0, z1) != null) return true;
		if (getBlock(x1, y1, z1) != null) return true;
		if (getBlock(x0, y1, z1) != null) return true;
		
		return false;
	}
	
	public void addBlock(int x, int y, int z) {
		
		int R = (int)(Math.random()*256);
		int G = (int)(Math.random()*256);
		int B= (int)(Math.random()*256);
		
		Block s = new Block(new Vec3(x, y, z), 1, new Color(R,G,B, 1));
		blocks.put(new BlockKey(x, y, z), s);
	}
	
	public void removeBlock(int x, int y, int z) {
		if (blocks.containsKey(new BlockKey(x, y, z)))
			blocks.remove(new BlockKey(x, y, z));
	}
	
	public void render() {
		
		if (initialized == false)
			return ;
		try {
		Iterator<Block> it = blocks.values().iterator();
		while (it.hasNext()) {
			Block b = it.next();
			Lwjgl.addSquare(b);
		}
		} catch (ConcurrentModificationException e) {
			System.out.println("HAHHAHAHAHAH");
		}
		
		for (Block b : ground.values()) {
			Lwjgl.addLinedSquareFace(b);
		}
		
		//Lwjgl.addLinedSquare(new Block(new Vector3f(0, SIZE / 2, 0), SIZE, Color.BLACK));
	}
	
	private class BlockKey {

	    private final int x;
	    private final int y;
	    private final int z;

	    public BlockKey(int x, int y, int z) {
	        this.x = x;
	        this.y = y;
	        this.z = z;
	    }

	    @Override
	    public boolean equals(Object o) {
	        if (this == o) return true;
	        if (!(o instanceof BlockKey)) return false;
	        BlockKey key = (BlockKey) o;
	        return x == key.x && y == key.y && z == key.z;
	    }

	    @Override
	    public int hashCode() {
	        int result = x;
	        result = 31 * result + y;
	        result = 31 * result + z;
	        return result;
	    }

	}
}
