package wild.engine.maths;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

public class Buffers {

	public static FloatBuffer createFloatBuffer(int size) {
		return BufferUtils.createFloatBuffer(size);
	}
	
	public static ByteBuffer createByteBuffer(int size) {
		return BufferUtils.createByteBuffer(size);
	}
	
	private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(4 * 4);
	public static FloatBuffer toMatrixBuffer(Mat4 v) {
		matrixBuffer.clear();
		for (int x = 0; x < 4; x++) {
			for (int y = 0; y < 4; y++) {
				matrixBuffer.put(v.matrix[x][y]);
			}	
		}
		matrixBuffer.flip();
		
		return matrixBuffer;
	}
}
