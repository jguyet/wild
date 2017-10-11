package wild.world;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import wild.engine.components.shape.Block;
import wild.engine.maths.Vec3;
import wild.world.env.Tree;
import wild.world.noise.NoiseGeneration;

public class World {
	
	private Map<ChunkKey, Chunk> hashChunk = new HashMap<ChunkKey, Chunk>();
	private NoiseGeneration terrainNoise;

	public World()
	{
		terrainNoise = new NoiseGeneration(new Random().nextInt());
		//terrainNoise.calcFinalNoise();
		//loadByPos(new Vector3f(0, 0, 0));
	}
	
	public void loadByPos(Vec3 pos) {
		ArrayList<Chunk> lst = new ArrayList<Chunk>();
		
		//int removefar = 21 * Chunk.SIZE;
		int farload = 3 * Chunk.SIZE;
		
		Map<ChunkKey, Chunk> tmp = new HashMap<ChunkKey, Chunk>();
		
		for (int x = (int)pos.x - farload; x < pos.x + farload; x += Chunk.SIZE) {
			for (int z = (int)pos.z - farload; z < pos.z + farload; z += Chunk.SIZE) {
				
				int xc = x / Chunk.SIZE;
				int zc = z / Chunk.SIZE;
				
				Chunk c = hashChunk.get(new ChunkKey(xc, zc));
				
				if (c != null) {
					tmp.put(new ChunkKey(xc, zc), c);
					continue ;
				}
				
				c = genChunk(x, z);
				
				if (c != null) {
					lst.add(c);
					c.render();
					tmp.put(new ChunkKey(xc, zc), c);
				}
			}
		}
		
		for (Chunk c : hashChunk.values()) {
			
			if (tmp.containsKey(new ChunkKey(c.x, c.z)) == false)
					c.destruct();
		}
		
		hashChunk.clear();
		hashChunk = tmp;
	}
	
	public Chunk genChunk(int x, int z) {
		int xc = x / Chunk.SIZE;
		int zc = z / Chunk.SIZE;
		
		if (hashChunk.containsKey(new ChunkKey(xc, zc)))
			return null;
		
		//terrainNoise.getNoise(xc, zc);
		Chunk n = null;
		
		/*try {
		n = ChunkFile.loadModel(this, xc, zc);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (n != null) {
			System.out.println("Loaded" + xc + " " + zc);
			hashChunk.put(new ChunkKey(xc, zc), n);
		}*/
		
		if (n == null) {
			System.out.println("LOAD " + xc + " " + zc);
			n = new Chunk(this, xc, 0, zc, terrainNoise, 10);
			hashChunk.put(new ChunkKey(xc, zc), n);
			n.clearChunk();
			/*try {
				ChunkFile.writeModel(n);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
		}
		n.initialized = true;
		return (n);
	}
	
	public void render()
	{
		Map<ChunkKey, Chunk> tmp = new HashMap<ChunkKey, Chunk>(hashChunk);
		for (Chunk c : tmp.values()) {
			c.render();
		}
		for (Tree t : Tree.trees) {
			t.render();
		}
	}
	
	public Block getBlock(int x, int y, int z) {
		int xc = x / Chunk.SIZE;
		int zc = z / Chunk.SIZE;
		
		Chunk chunk = hashChunk.get(new ChunkKey(xc, zc));
		
		if (chunk == null)
			return null;
		
		int xb = x % Chunk.SIZE;
		int yb = y;
		int zb = z % Chunk.SIZE;
		
		return (chunk.getBlock(xb, yb, zb));
	}
	
	private class ChunkKey {

	    private final int x;
	    private final int y;

	    public ChunkKey(int x, int y) {
	        this.x = x;
	        this.y = y;
	    }

	    @Override
	    public boolean equals(Object o) {
	        if (this == o) return true;
	        if (!(o instanceof ChunkKey)) return false;
	        ChunkKey key = (ChunkKey) o;
	        return x == key.x && y == key.y;
	    }

	    @Override
	    public int hashCode() {
	        int result = x;
	        result = 31 * result + y;
	        return result;
	    }

	}
}
