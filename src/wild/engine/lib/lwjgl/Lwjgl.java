package wild.engine.lib.lwjgl;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import wild.engine.components.Camera;
import wild.engine.components.Tex;
import wild.engine.components.shape.Block;
import wild.engine.components.shape.obj.Model;
import wild.engine.maths.Vec3;

public class Lwjgl {
	
	private static final int GLUT_3_2_CORE_PROFILE = 2048;//for macos
	
	private static final String WINDOWS_PATH = "lwjgl_lib" + File.separator + "windows" + File.separator;
	private static final String LINUX_PATH = "lwjgl_lib" + File.separator + "linux" + File.separator;
	private static final String MACOS_PATH = "lwjgl_lib" + File.separator + "macosx" + File.separator;

	public static void loadLibraryProperty()
	{
		String osName = System.getProperty("os.name").toLowerCase();
		boolean windows = false;
		boolean macos = false;
		
		if (osName.contains("win"))
			windows = true;
		if (osName.contains("Mac OS X"))//Mac OS X
			macos = true;
		File f = null;
		if (windows == true)
			f = new File(WINDOWS_PATH);
		if (macos == true)
			f = new File(LINUX_PATH);
		else
			f = new File(MACOS_PATH);
		
		System.setProperty("org.lwjgl.librarypath", f.getAbsolutePath());
	}
	
	public static String getVersion() {
		return org.lwjgl.Sys.getVersion();
	}
	
	public static String getOpenGlVersion() {
		return (glGetString(GL_VERSION));
	}
	
	// ########################################################### WINDOW
	
	public static void createWindow(int width, int height, String title) {
		Lwjgl.setTitle(title);
		try {
			Display.setDisplayMode(new DisplayMode(width, height));
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}
	
	public static void setTitle(String title) {
		Display.setTitle(title);
	}
	
	public static String getTitle() {
		return Display.getTitle();
	}
	
	public static int getWindowWidth() {
		return Display.getDisplayMode().getWidth();
	}

	public static int getWindowHeight() {
		return Display.getDisplayMode().getHeight();
	}
	
	public static boolean isCloseRequested() {
		return Display.isCloseRequested();
	}
	
	public static void clearScreen(){
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}
	
	public static void render() {
		Display.update();
	}
	
	public static void destroy() {
		Display.destroy();
		Keyboard.destroy();
		Mouse.destroy();
	}
	
	// ###########################################################
	
	// ########################################################### SETTINGS
	
	public static void initSettings() {
		
		glEnable(GLUT_3_2_CORE_PROFILE);
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		//Meilleur rendu sur les objets pres de la camera
		glEnable(GL_DEPTH_TEST);
		//retire les faces que lon ne voi pas
		//glEnable(GL_CULL_FACE);
		
		glDisable( GL_BLEND);
		glDepthFunc(GL_LEQUAL); 
		
		//init inputs
		try {
			Keyboard.create();
			Mouse.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}
	
	// ###########################################################
	
	// ########################################################### VIEW
	
	public static void setProjectionPerspective(float fov, float width, float height, float zNear, float zFar)
	{
		glEnable(GL_PROJECTION);
		glLoadIdentity();
		GLU.gluPerspective(fov, width / height, zNear, zFar);
		glEnable(GL_MODELVIEW);
		//glLoadIdentity();
	}
	
	public static void setProjectionMode() {
		glEnable(GL_PROJECTION);
		glLoadIdentity();
		//GLU.gluPerspective(0, Lwjgl.getWindowWidth(), Lwjgl.getWindowHeight(), 0);
	}
	
	public static void setModelViewMode() {
		glEnable(GL_MODELVIEW);
		//glLoadIdentity();
	}
	
	public static void setCameraTransformation(Camera camera)
	{	
		glPushAttrib(GL_TRANSFORM_BIT);
			// ########################################## ROTATIONS
			glRotatef(camera.getRotation().x, 1, 0, 0);
			glRotatef(camera.getRotation().y, 0, 1, 0);
			glRotatef(camera.getRotation().z, 0, 0, 1);
			// ########################################## POSITION
			glTranslatef(-camera.getEyePosition().x, -camera.getEyePosition().y, -camera.getEyePosition().z);
		glPopMatrix();
	}
	
	// ###########################################################
	
	// ########################################################### SHAPE
	
	public static void addSkyBox(Block s)
	{
		float x = s.pos.x - (float)(s.scale / 2);
		float y = s.pos.y - (float)(s.scale / 2);
		float z = s.pos.z - (float)(s.scale / 2);
		
		glEnable(GL_TEXTURE_2D);
		
		Tex.skybox.bind();
		
		if (s.color != null)
			glColor3f(s.color.r, s.color.g, s.color.b);
		
		glBegin( GL_QUADS );
		
		Tex.skybox.setPosition(8);
		Tex.skybox.positionRot(2);
		//front
		glTexCoord2f(Tex.skybox.posPoints[0].x,Tex.skybox.posPoints[0].y); glVertex3f( x, y,z );
		glTexCoord2f(Tex.skybox.posPoints[1].x,Tex.skybox.posPoints[1].y); glVertex3f( x+s.scale, y,z );
		glTexCoord2f(Tex.skybox.posPoints[2].x,Tex.skybox.posPoints[2].y); glVertex3f( x+s.scale, y+s.scale,z);
		glTexCoord2f(Tex.skybox.posPoints[3].x,Tex.skybox.posPoints[3].y); glVertex3f( x, y+s.scale,z );

		Tex.skybox.setPosition(6);
		Tex.skybox.positionRot(2);
		//back
		glTexCoord2f(Tex.skybox.posPoints[0].x,Tex.skybox.posPoints[0].y); glVertex3f( x+s.scale, y,z+s.scale );
		glTexCoord2f(Tex.skybox.posPoints[1].x,Tex.skybox.posPoints[1].y); glVertex3f( x, y,z+s.scale );
		glTexCoord2f(Tex.skybox.posPoints[2].x,Tex.skybox.posPoints[2].y); glVertex3f( x, y+s.scale,z+s.scale);
		glTexCoord2f(Tex.skybox.posPoints[3].x,Tex.skybox.posPoints[3].y); glVertex3f( x+s.scale, y+s.scale,z+s.scale );

		Tex.skybox.setPosition(7);
		Tex.skybox.positionRot(2);
		// right side
		glTexCoord2f(Tex.skybox.posPoints[0].x,Tex.skybox.posPoints[0].y); glVertex3f( x+s.scale, y,z );
		glTexCoord2f(Tex.skybox.posPoints[1].x,Tex.skybox.posPoints[1].y); glVertex3f( x+s.scale, y,z+s.scale );
		glTexCoord2f(Tex.skybox.posPoints[2].x,Tex.skybox.posPoints[2].y); glVertex3f( x+s.scale, y+s.scale,z+s.scale);
		glTexCoord2f(Tex.skybox.posPoints[3].x,Tex.skybox.posPoints[3].y); glVertex3f( x+s.scale, y+s.scale,z );
		
		Tex.skybox.setPosition(5);
		Tex.skybox.positionRot(1);
		//left side
		glTexCoord2f(Tex.skybox.posPoints[0].x,Tex.skybox.posPoints[0].y); glVertex3f( x, y,z );
		glTexCoord2f(Tex.skybox.posPoints[1].x,Tex.skybox.posPoints[1].y); glVertex3f( x, y+s.scale,z );
		glTexCoord2f(Tex.skybox.posPoints[2].x,Tex.skybox.posPoints[2].y); glVertex3f( x, y+s.scale,z+s.scale);
		glTexCoord2f(Tex.skybox.posPoints[3].x,Tex.skybox.posPoints[3].y); glVertex3f( x, y,z+s.scale );
		
		Tex.skybox.setPosition(10);
		//bottom
		glTexCoord2f(Tex.skybox.posPoints[0].x,Tex.skybox.posPoints[0].y); glVertex3f(x, y, z);
		glTexCoord2f(Tex.skybox.posPoints[1].x,Tex.skybox.posPoints[1].y); glVertex3f(x, y, z + s.scale);
		glTexCoord2f(Tex.skybox.posPoints[2].x,Tex.skybox.posPoints[2].y); glVertex3f(x + s.scale, y, z + s.scale);
		glTexCoord2f(Tex.skybox.posPoints[3].x,Tex.skybox.posPoints[3].y); glVertex3f(x + s.scale, y, z);
		
		Tex.skybox.setPosition(2);
		Tex.skybox.positionRot(3);
		Tex.skybox.rotate_vertical();
		//top
		glTexCoord2f(Tex.skybox.posPoints[0].x,Tex.skybox.posPoints[0].y); glVertex3f(x, y + s.scale, z);
		glTexCoord2f(Tex.skybox.posPoints[1].x,Tex.skybox.posPoints[1].y); glVertex3f(x, y + s.scale, z + s.scale);
		glTexCoord2f(Tex.skybox.posPoints[2].x,Tex.skybox.posPoints[2].y); glVertex3f(x + s.scale, y + s.scale, z + s.scale);
		glTexCoord2f(Tex.skybox.posPoints[3].x,Tex.skybox.posPoints[3].y); glVertex3f(x + s.scale, y + s.scale, z);
		
		glEnd();
		
		if (s.color != null)
			glColor3f(255, 255, 255);
		
		Tex.skybox.unbind();
		glDisable(GL_TEXTURE_2D);
	}
	
	public static void addSquare(Block s)
	{
		float x = s.pos.x - (s.scale / 2);
		float y = s.pos.y - (s.scale / 2);
		float z = s.pos.z - (s.scale / 2);
		
		glBegin( GL_QUADS );
		
		if (s.color != null)
			glColor3f(s.color.r * 0.8f, s.color.g * 0.8f, s.color.b * 0.8f);
		//front
		glVertex3f( x, y,z );
		glVertex3f( x+s.scale, y,z );
		glVertex3f( x+s.scale, y+s.scale,z);
		glVertex3f( x, y+s.scale,z );
		//back
		glVertex3f( x+s.scale, y,z+s.scale );
		glVertex3f( x, y,z+s.scale );
		glVertex3f( x, y+s.scale,z+s.scale);
		glVertex3f( x+s.scale, y+s.scale,z+s.scale );
		// right side
		glVertex3f( x+s.scale, y,z );
		glVertex3f( x+s.scale, y,z+s.scale );
		glVertex3f( x+s.scale, y+s.scale,z+s.scale);
		glVertex3f( x+s.scale, y+s.scale,z );
		//left side
		glVertex3f( x, y,z );
		glVertex3f( x, y+s.scale,z );
		glVertex3f( x, y+s.scale,z+s.scale);
		glVertex3f( x, y,z+s.scale );
		if (s.color != null)
			glColor3f(s.color.r, s.color.g, s.color.b);
		//bottom
		glVertex3f(x, y, z);
		glVertex3f(x, y, z + s.scale);
		glVertex3f(x + s.scale, y, z + s.scale);
		glVertex3f(x + s.scale, y, z);
		//top
		glVertex3f(x, y + s.scale, z);
		glVertex3f(x, y + s.scale, z + s.scale);
		glVertex3f(x + s.scale, y + s.scale, z + s.scale);
		glVertex3f(x + s.scale, y + s.scale, z);
		
		glEnd();
		
		if (s.color != null)
			glColor3f(255, 255, 255);
	}
	
	public static void addTexturedSquare(Block s, TexturedSquare faces)
	{
		float x = s.pos.x - (s.scale / 2);
		float y = s.pos.y - (s.scale / 2);
		float z = s.pos.z - (s.scale / 2);
		
		glEnable(GL_TEXTURE_2D);
		s.texture.bind();
		if (s.color != null)
			glColor3f(s.color.r, s.color.g, s.color.b);
		glBegin( GL_QUADS );
		
		faces.front(s.texture);
		//front
		glTexCoord2f(s.texture.posPoints[0].x,s.texture.posPoints[0].y); glVertex3f( x, y,z );
		glTexCoord2f(s.texture.posPoints[1].x,s.texture.posPoints[1].y); glVertex3f( x+s.scale, y,z );
		glTexCoord2f(s.texture.posPoints[2].x,s.texture.posPoints[2].y); glVertex3f( x+s.scale, y+s.scale,z);
		glTexCoord2f(s.texture.posPoints[3].x,s.texture.posPoints[3].y); glVertex3f( x, y+s.scale,z );
		faces.back(s.texture);
		//back
		glTexCoord2f(s.texture.posPoints[0].x,s.texture.posPoints[0].y); glVertex3f( x+s.scale, y,z+s.scale );
		glTexCoord2f(s.texture.posPoints[1].x,s.texture.posPoints[1].y); glVertex3f( x, y,z+s.scale );
		glTexCoord2f(s.texture.posPoints[2].x,s.texture.posPoints[2].y); glVertex3f( x, y+s.scale,z+s.scale);
		glTexCoord2f(s.texture.posPoints[3].x,s.texture.posPoints[3].y); glVertex3f( x+s.scale, y+s.scale,z+s.scale );
		faces.right(s.texture);
		// right side
		glTexCoord2f(s.texture.posPoints[0].x,s.texture.posPoints[0].y); glVertex3f( x+s.scale, y,z );
		glTexCoord2f(s.texture.posPoints[1].x,s.texture.posPoints[1].y); glVertex3f( x+s.scale, y,z+s.scale );
		glTexCoord2f(s.texture.posPoints[2].x,s.texture.posPoints[2].y); glVertex3f( x+s.scale, y+s.scale,z+s.scale);
		glTexCoord2f(s.texture.posPoints[3].x,s.texture.posPoints[3].y); glVertex3f( x+s.scale, y+s.scale,z );
		faces.left(s.texture);
		//left side
		glTexCoord2f(s.texture.posPoints[0].x,s.texture.posPoints[0].y); glVertex3f( x, y,z );
		glTexCoord2f(s.texture.posPoints[1].x,s.texture.posPoints[1].y); glVertex3f( x, y+s.scale,z );
		glTexCoord2f(s.texture.posPoints[2].x,s.texture.posPoints[2].y); glVertex3f( x, y+s.scale,z+s.scale);
		glTexCoord2f(s.texture.posPoints[3].x,s.texture.posPoints[3].y); glVertex3f( x, y,z+s.scale );
		faces.bottom(s.texture);
		//bottom
		glTexCoord2f(s.texture.posPoints[0].x,s.texture.posPoints[0].y); glVertex3f(x, y, z);
		glTexCoord2f(s.texture.posPoints[1].x,s.texture.posPoints[1].y); glVertex3f(x, y, z + s.scale);
		glTexCoord2f(s.texture.posPoints[2].x,s.texture.posPoints[2].y); glVertex3f(x + s.scale, y, z + s.scale);
		glTexCoord2f(s.texture.posPoints[3].x,s.texture.posPoints[3].y); glVertex3f(x + s.scale, y, z);
		faces.top(s.texture);
		//top
		glTexCoord2f(s.texture.posPoints[0].x,s.texture.posPoints[0].y); glVertex3f(x, y + s.scale, z);
		glTexCoord2f(s.texture.posPoints[1].x,s.texture.posPoints[1].y); glVertex3f(x, y + s.scale, z + s.scale);
		glTexCoord2f(s.texture.posPoints[2].x,s.texture.posPoints[2].y); glVertex3f(x + s.scale, y + s.scale, z + s.scale);
		glTexCoord2f(s.texture.posPoints[3].x,s.texture.posPoints[3].y); glVertex3f(x + s.scale, y + s.scale, z);
		
		glEnd();
		
		if (s.color != null)
			glColor3f(255, 255, 255);
		s.texture.unbind();
		glDisable(GL_TEXTURE_2D);
	}
	
	public static void addLinedSquare(Block s)
	{
		float x = s.pos.x - (s.scale / 2);
		float y = s.pos.y - (s.scale / 2);
		float z = s.pos.z - (s.scale / 2);
		
		if (s.color != null)
			glColor3f(s.color.r, s.color.g, s.color.b);
		glBegin( GL_LINES );
		//front
		glVertex3f( x, y,z );
		glVertex3f( x, y + s.scale,z );
		glVertex3f( x +s.scale, y,z );
		glVertex3f( x +s.scale, y + s.scale,z );
		//back
		glVertex3f( x, y,z+s.scale );
		glVertex3f( x, y + s.scale,z+s.scale );
		glVertex3f( x +s.scale, y,z+s.scale );
		glVertex3f( x +s.scale, y + s.scale,z+s.scale );
		//top
		glVertex3f(x, y + s.scale, z);
		glVertex3f(x, y + s.scale, z + s.scale);
		glVertex3f(x + s.scale, y + s.scale, z + s.scale);
		glVertex3f(x + s.scale, y + s.scale, z);
		glVertex3f( x+s.scale, y+s.scale,z);
		glVertex3f( x, y+s.scale,z );
		glVertex3f( x+s.scale, y+s.scale,z + s.scale);
		glVertex3f( x, y+s.scale,z  + s.scale);
		//bottom
		glVertex3f(x, y, z);
		glVertex3f(x, y, z + s.scale);
		glVertex3f(x + s.scale, y, z + s.scale);
		glVertex3f(x + s.scale, y, z);
		glVertex3f( x+s.scale, y,z);
		glVertex3f( x, y,z );
		glVertex3f( x+s.scale, y,z + s.scale);
		glVertex3f( x, y,z  + s.scale);
		
		glEnd();
		if (s.color != null)
			glColor3f(255, 255, 255);
	}
	
	public static void addLinedSquareFace(Block s)
	{
		float x = s.pos.x - (s.scale / 2);
		float y = s.pos.y - (s.scale / 2);
		float z = s.pos.z - (s.scale / 2);
		
		if (s.color != null)
			glColor3f(s.color.r, s.color.g, s.color.b);
		glBegin( GL_LINES );
		//top
		glVertex3f(x, y + s.scale, z);
		glVertex3f(x, y + s.scale, z + s.scale);
		glVertex3f(x + s.scale, y + s.scale, z + s.scale);
		glVertex3f(x + s.scale, y + s.scale, z);
		glVertex3f( x+s.scale, y+s.scale,z);
		glVertex3f( x, y+s.scale,z );
		glVertex3f( x+s.scale, y+s.scale,z + s.scale);
		glVertex3f( x, y+s.scale,z  + s.scale);
		
		glEnd();
		if (s.color != null)
			glColor3f(255, 255, 255);
	}
	
	public static void addLine(Vec3 a, Vec3 b)
	{
		
		glColor3f(0, 0, 0);
		glBegin( GL_LINES );
		//top
		glVertex3f(a.x, a.y, a.z);
		glVertex3f(b.x, b.y, b.z);
		
		glEnd();
		glColor3f(255, 255, 255);
	}
	
	public static void addObj(Model model, Vec3 pos)
	{
		glBegin(GL_POINTS);
        for (Model.Face face : model.getFaces()) {
            Vec3 n1 = model.getNormals().get(face.getNormalIndices()[0] - 1);
            glNormal3f(n1.x + pos.x, n1.y + pos.y, n1.z + pos.z);
            Vec3 v1 = model.getVertices().get(face.getVertexIndices()[0] - 1);
            glVertex3f(v1.x + pos.x, v1.y + pos.y, v1.z + pos.z);
            Vec3 n2 = model.getNormals().get(face.getNormalIndices()[1] - 1);
            glNormal3f(n2.x + pos.x, n2.y + pos.y, n2.z + pos.z);
            Vec3 v2 = model.getVertices().get(face.getVertexIndices()[1] - 1);
            glVertex3f(v2.x + pos.x, v2.y + pos.y, v2.z + pos.z);
            Vec3 n3 = model.getNormals().get(face.getNormalIndices()[2] - 1);
            glNormal3f(n3.x + pos.x, n3.y + pos.y, n3.z + pos.z);
            Vec3 v3 = model.getVertices().get(face.getVertexIndices()[2] - 1);
            glVertex3f(v3.x + pos.x, v3.y + pos.y, v3.z + pos.z);
        }
        glEnd();
	}
	
	public static void addFlo()
	{
		FloatBuffer fogColor = (FloatBuffer) BufferUtils.createFloatBuffer(4);
		
		fogColor.put(new float[] {
				1, 1, 1, 0.5f
		});
		
		fogColor.flip();
		glEnable(GL_FOG);
		glFogi(GL_FOG_MODE, GL_EXP);
		glFogf(GL_FOG_DENSITY, 0.001f);
		glFog(GL_FOG_COLOR, fogColor);
	}
}