package wild.world.env;

import java.util.ArrayList;

import wild.engine.components.shape.Block;
import wild.engine.maths.Buffers;
import wild.engine.maths.Vec3;
import wild.world.Entity;

public class Tree extends Entity {

	public static ArrayList<Tree> trees = new ArrayList<Tree>();
	
	private int height = 10;
	private float width = 5.0f;
	
	public Tree(Vec3 pos) {
		this.pos = pos;
		
		bufferf = Buffers.createFloatBuffer(getSize());
		initialize();
		bufferf.flip();
		this.createBuffer();
		trees.add(this);
	}
	
	private int getSize() {
		
		int sizeb = 0;
		
		for (float y = this.pos.y; y < this.pos.y + height; y++) {
			sizeb += (6 * (9 * 4));
		}
		
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < height; j++) {
				for (int k = 0; k < height; k++) {
					float ii = i - width;
					float jj = j - width;
					float kk = k - width;
					float l = (float) Math.sqrt(ii * ii  + jj * jj + kk * kk);
					
					if (l < width) {
						sizeb += (6 * (9 * 4));
					}
				}
			}
		}
		return (sizeb);
	}
	
	
	private void initialize() {
		for (float y = this.pos.y; y < this.pos.y + height; y++) {
			bufferf.put(Block.WOOD_BLOCK.getBlock(new Vec3(pos.x, y, pos.z)));
			bufferfSize += (6 * 4);
		}
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < height; j++) {
				for (int k = 0; k < height; k++) {
					float ii = i - width;
					float jj = j - width;
					float kk = k - width;
					float l = (float) Math.sqrt(ii * ii  + jj * jj + kk * kk);
					
					if (l < width) {
						bufferf.put(Block.TREE_BLOCK.getBlock(new Vec3(pos.x + ii, pos.y + jj + height, pos.z + kk)));
						bufferfSize += (6 * 4);
					}
				}
			}
		}
	}
}
