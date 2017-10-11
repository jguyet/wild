package wild.world;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

import wild.engine.components.shape.Block;
import wild.engine.components.shape.obj.Model;
import wild.engine.maths.Buffers;
import wild.engine.maths.Vec2;
import wild.engine.maths.Vec3;

public class ChunkFile {
	
	public static final String PATH = "/Users/jguyet/goinfre/world";
		
	private static Vec3 parseVertex(String line) {
        String[] xyz = line.split(" ");
        float x = Float.valueOf(xyz[1]);
        float y = Float.valueOf(xyz[2]);
        float z = Float.valueOf(xyz[3]);
        return new Vec3(x, y, z);
    }
	
	private static int parseInt(String line) {
        String[] xyz = line.split(" ");
        int size = Integer.valueOf(xyz[1]);
        return size;
    }
	
	private static FloatBuffer parseBufferrealSize(String line) {
        String[] xyz = line.split(" ");
        int size = Integer.valueOf(xyz[1]);
        FloatBuffer buffer = Buffers.createFloatBuffer(size);
        return buffer;
    }

	public static Chunk loadModel(World world, int x, int z) throws IOException {
		
		File f =  new File(PATH + "/" + x + "_" + z);
		File fdata =  new File(PATH + "/data_" + x + "_" + z);
		
		if (f.exists() == false || fdata.exists() == false)
			return null;
		Chunk chunk = new Chunk(world, x, 0, z);
		chunk.buffer = new Block[Chunk.SIZE][Chunk.SIZE][Chunk.SIZE];
		
        BufferedReader reader = new BufferedReader(new FileReader(PATH + "/" + x + "_" + z));
        String line;
        while ((line = reader.readLine()) != null) {
            String prefix = line.split(" ")[0];
            if (prefix.equals("#")) {
                continue;
            } else if (prefix.equals("b")) {
            	Vec3 b = parseVertex(line);
            	chunk.buffer[(int)b.x][(int)b.y][(int)b.z] = Block.GRASS_BLOCK;
            	
            }else if (prefix.equals("bufferSize")) {
            	int size = parseInt(line);
            	chunk.openGLbuffer.setBufferSize(size);
            	
            }else if (prefix.equals("numberOfObjects")) {
            	int size = parseInt(line);
            	chunk.openGLbuffer.setNumberOfObjects(size);
            	
            }else {
                throw new RuntimeException("OBJ file contains line which cannot be parsed correctly: " + line);
            }
        }
        reader.close();
        
        FileInputStream datainput =  new FileInputStream(PATH + "/data_" + x + "_" + z);
        
        byte[] buf = new byte[chunk.openGLbuffer.getBufferSize()];
        datainput.read(buf, 0, chunk.openGLbuffer.getBufferSize());
        ByteBuffer openglbuffer = BufferUtils.createByteBuffer(chunk.openGLbuffer.getBufferSize());
        openglbuffer.put(buf);
        chunk.openGLbuffer.setBuffer(openglbuffer);
        
        datainput.close();
        return chunk;
    }
	
	public static void writeModel(Chunk c) throws IOException {
		File f =  new File(PATH + "/" + c.x + "_" + c.z);
		File fdata =  new File(PATH + "/data_" + c.x + "_" + c.z);
		
		if (f.exists() == true && fdata.exists() == true)
			return;
		BufferedWriter writerinfos = new BufferedWriter(new FileWriter(PATH + "/" + c.x + "_" + c.z));
		
		writerinfos.write("bufferSize " + c.openGLbuffer.getBufferSize() + "\n");
		writerinfos.write("numberOfObjects " + c.openGLbuffer.getNumberOfObjects() + "\n");
		
		for (int x = 0; x < Chunk.SIZE; x++) {
			for (int y = 0; y < Chunk.SIZE; y++) {
				for (int z = 0; z < Chunk.SIZE; z++) {
					
					if (c.buffer[x][y][z] == null)
						continue ;
					writerinfos.write("b");
					writerinfos.write(" " + x);
					writerinfos.write(" " + y);
					writerinfos.write(" " + z);
					writerinfos.write("\n");
				}
			}
		}
		writerinfos.flush();
		writerinfos.close();
		
		
		c.openGLbuffer.getBuffer().rewind();
		byte[] buf = new byte[c.openGLbuffer.getBufferSize()];
		c.openGLbuffer.getBuffer().get(buf);
		c.openGLbuffer.getBuffer().position(c.openGLbuffer.getBufferSize());
		
		FileOutputStream fos = new FileOutputStream(PATH + "/data_" + c.x + "_" + c.z);
		fos.write(buf, 0, c.openGLbuffer.getBufferSize());
		fos.flush();
		fos.close();
    }
}
