package wild.world;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

import java.nio.FloatBuffer;

import wild.engine.maths.Vec3;

public abstract class Entity {

	public Vec3 pos;
	
	protected int bufferfSize = 0;
	protected FloatBuffer bufferf;
	
	private int vbo;
	
	public Entity() {
		
	}
	
	protected void createBuffer() {
		vbo = glGenBuffers();
		
		glBindBuffer(GL_ARRAY_BUFFER, vbo);//bind le buffer
		
		glBufferData(GL_ARRAY_BUFFER, bufferf, GL_STATIC_DRAW);//add static buffer
	}
	
	protected void render() {
		
		glEnableVertexAttribArray(0);//position
		glEnableVertexAttribArray(1);//color
		
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 7 * 4, 0);
		glVertexAttribPointer(1, 4, GL_FLOAT, false, 7 * 4, 12);
		
		glDrawArrays(GL_QUADS, 0, bufferfSize);
		
		glDisableVertexAttribArray(0);//position
		glDisableVertexAttribArray(1);//color
	}
}
