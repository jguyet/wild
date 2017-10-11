package wild.engine.lib.lwjgl;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

import wild.engine.lib.lwjgl.shaders.Shader;
import wild.utils.RGB;

public class OpenGLBuffer {
	
	public class OpenGLBufferException extends Exception {
		public OpenGLBufferException(String exception) {
			super(exception);
		}
	}
	
	public static final int V_INTEGER = 0;
	public static final int V_FLOAT = 1;
	public static final int V_BYTE = 2;
	public static final int V_SHORT = 0;
	
	private ByteBuffer buffer;
	private int bufferSize;
	private int numberOfObjects;
	private boolean	initialized = false;
	private int vbo;//vertex buffer object
	//private int vbc;//vertex buffer coord
	
	public OpenGLBuffer() {
		buffer = BufferUtils.createByteBuffer(10000000);
	}
	
	public ByteBuffer getBuffer() {
		return buffer;
	}

	public void setBuffer(ByteBuffer buffer) {
		this.buffer = buffer;
	}

	public int getBufferSize() {
		return bufferSize;
	}

	public void setBufferSize(int bufferSize) {
		this.bufferSize = bufferSize;
	}

	public int getNumberOfObjects() {
		return numberOfObjects;
	}

	public void setNumberOfObjects(int numberOfObjects) {
		this.numberOfObjects = numberOfObjects;
	}
	
	public void add(float x, float y, float z, float r, float g, float b, float a) {
		
		buffer.putInt((int)x);
		buffer.putInt((int)y);
		buffer.putInt((int)z);
		this.numberOfObjects++;
		this.bufferSize += (4 * 3);
	}
	
	private void createBuffer() {
		
		System.out.println("BUFFER ARRAY POS :" + this.buffer.position());
		System.out.println("BUFFER ARRAY SIZE :" + this.buffer.remaining());
		
		if (this.buffer.capacity() > this.bufferSize) {
			this.buffer.rewind();
			byte[] copy = new byte[this.bufferSize];
			
			this.buffer.get(copy, 0, this.bufferSize);
			this.buffer.position(this.bufferSize);
			this.buffer = BufferUtils.createByteBuffer(this.bufferSize);
			this.buffer.put(copy);
		}
		buffer.flip();
		
		//if(glIsBuffer(vbo) == true)
	    //    glDeleteBuffers(vbo);
		//generation d'un id d'espace memoire vbo (sur la carte graphique)
		vbo = glGenBuffers();
		
		//verouillage du buffer 
		glBindBuffer(GL_ARRAY_BUFFER, vbo);//bind le buffer
		
		/*
		 * GL_STATIC_DRAW : pour les données très peu mises à jour
		 * GL_DYNAMIC_DRAW : pour les données mises à jour fréquemment (plusieurs fois par seconde mais pas à chaque frame)
		 * GL_STREAM_DRAW : pour les données mises à jour tout le temps (A chaque frame cette fois-ci)
		 */
		glBufferData(GL_ARRAY_BUFFER, this.bufferSize, GL_STATIC_DRAW);//set le size de memoire vbo
		
		glBufferSubData(GL_ARRAY_BUFFER, 0, this.buffer);//add le buffer de position
		
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		
		// ##############################################  coord
		
		/*vbc = glGenBuffers();
		
		//verouillage du buffer 
		glBindBuffer(GL_ARRAY_BUFFER, vbc);//bind le buffer
		
		glBufferData(GL_ARRAY_BUFFER,  (((4 * 8) * 6) * this.bufferSize), GL_STATIC_DRAW);
		
		FloatBuffer coord = BufferUtils.createFloatBuffer(((4 * 8) * this.bufferSize));
		
		for (int i = 0; i < this.bufferSize; i++) {
		
			coord.put(new float[] {
					0, 0,
					1, 0,
					1, 1,
					0, 1
			});
		}
		
		coord.flip();
		
		glBufferSubData(GL_ARRAY_BUFFER, 0, coord);
		
		glBindBuffer(GL_ARRAY_BUFFER, 0);*/
		
		
	}
	
	public void destruct() {
		if(glIsBuffer(vbo) == true)
	        glDeleteBuffers(vbo);
		glDeleteBuffers(vbo);
	}
	
	public void render() {
		
		if (this.initialized == false) {
			System.out.println("BUFFER SIZE :" + this.bufferSize);
			System.out.println("BUFFER OBJECT SIZE :" + this.numberOfObjects);
			createBuffer();
			this.initialized = true;
			this.buffer = null;
		}
		
		//glEnableClientState(GL_VERTEX_ARRAY);
		//glEnableClientState(GL_TEXTURE_COORD_ARRAY);
		
		// #####################################################
		//        SET L'ARRAY DES POSITION DES BLOCK          //
		glBindBuffer(GL_ARRAY_BUFFER, this.vbo);
		glEnableVertexAttribArray(0);
		int saut = (4 * 3);
		glVertexAttribPointer(0, 3, GL_INT, false, saut, 0);
		glBindAttribLocation(Shader.MAIN.program, 0, "block_position");
		
		/*// #####################################################
		//        SET L'ARRAY DES COORDONEE DE TEXTURE        //
		glBindBuffer(GL_ARRAY_BUFFER, this.vbc);
		glEnableVertexAttribArray(1);
		glVertexAttribPointer(1, 2, GL_FLOAT, false, 8, 0);
		glBindAttribLocation(Shader.MAIN.program, 1, "texCoord");*/
		
		// #####################################################
		glDrawArrays(GL_QUADS, 0, numberOfObjects); //      DRAW
		// #####################################################
		
		//   DISABLES ATTRIBS  #################################
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		// #####################################################
		
		//  UNLOCK BUFFERS #####################################
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		// #####################################################
		
		//glDisableClientState(GL_TEXTURE_COORD_ARRAY);
		//glDisableClientState(GL_VERTEX_ARRAY);
		
	}
}
