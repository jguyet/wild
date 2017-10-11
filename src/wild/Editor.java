package wild;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.glu.GLU;

import wild.editor.ECRaycast;
import wild.editor.RayCasting;
import wild.editor.WorldEditor;
import wild.engine.components.Camera;
import wild.engine.components.Input;
import wild.engine.components.Transform;
import wild.engine.components.loop.FrameLoop;
import wild.engine.components.loop.MotorGraphics;
import wild.engine.components.shape.Block;
import wild.engine.lib.lwjgl.Lwjgl;
import wild.engine.maths.Vec3;
import wild.utils.Color;
import wild.world.World;

import static org.lwjgl.opengl.GL11.*;

public class Editor {

	static	{ // #################################################
		Lwjgl.loadLibraryProperty(); // LOAD LWJGL LIBRARY ON ENVIRONEMENT
	} // #################################################
	
	public static long		lastFPS = 0;
	public static int		fps = 0;
	
	public static long		lastPFPS = 0;
	public static int		pfps = 0;
	public static String	pfps_display = "";
	
	public static final int WIDTH = 1500;//640;
	public static final int	HEIGHT = 1000;//480;
	
	public static float		rendScale = 10.0f;
	
	public static boolean	gameRunning = false;
	
	public static Camera	camera;
	public static Transform transform;
	
	public static WorldEditor worldeditor;
	
	
	public static Vec3 selectedPosition = null;
	public static long		lastposeobject;
	
	public static void main(String ...args) {
		lastFPS = System.currentTimeMillis();
		lastPFPS = System.currentTimeMillis();
		lastposeobject = System.currentTimeMillis();
		Lwjgl.createWindow(WIDTH, HEIGHT, "Wild");
		Lwjgl.initSettings();
		Lwjgl.clearScreen();
		
		camera = new Camera();
		transform = new Transform();
		Transform.setCamera(camera);
		
		worldeditor = new WorldEditor();
		
		System.out.println("OS name " + System.getProperty("os.name"));
	    System.out.println("OS version " + System.getProperty("os.version"));
	    System.out.println("LWJGL version " + Lwjgl.getVersion());
	    System.out.println("OpenGL version " + Lwjgl.getOpenGlVersion());
	    
	    //ECRaycast rayCast = new ECRaycast(30f, 0.01f);
	    
	    FrameLoop loop = new FrameLoop(new MotorGraphics() {

	    	@Override
			public void inputLoop() {// ################################################# INPUT LOOP
				// TODO Auto-generated method stub
	    		if (Lwjgl.isCloseRequested() || Input.getKey(Input.KEY_ESCAPE)) {
					Input.setCursor(true); System.exit(0);
				}
				Input.update();
				camera.input();
			}
	    	
			@Override
			public void graphicPhysicalLoop() {// ################################################# PHYSICAL LOOP
				// TODO Auto-generated method stub
				updatePhysicsFPS();
				
				
				
			}

			@Override
			public void graphicRenderingLoop() {// ################################################# RENDERING LOOP
				// TODO Auto-generated method stub
				Lwjgl.clearScreen();
				updateFPS();
				
				// ################################################# ADDCAMERA SETTINGS
				Transform.setProjection(70f, Lwjgl.getWindowWidth(), Lwjgl.getWindowHeight(),
						0.01f, 1000);
				transform.setProjectedTransformation();
				
				if (System.currentTimeMillis() - lastposeobject > 200) {
					camera.applySelectionActions();
				}
				//Lwjgl.setModelViewMode();
				// #################################################
				
				worldeditor.render();
				
				// ################################################# Skybox
				Transform.setProjection(70f, Lwjgl.getWindowWidth(), Lwjgl.getWindowHeight(),
						0.01f, 10000);
				transform.setProjectedTransformation();
				Block sky = new Block(new Vec3(-camera.getPos().x, -camera.getPos().y, -camera.getPos().z), 5000, Color.WHITE);
				Lwjgl.addSkyBox(sky);
				// #################################################
				
				/*if (selectedPosition != null) {
					glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
					glLineWidth(5);
					Lwjgl.addSquare(new Block(new Vec3(selectedPosition.x, selectedPosition.y, selectedPosition.z), 1f, Color.WHITE));
					glLineWidth(1);
					glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
				}*/
				
				
				Lwjgl.setProjectionMode();
				GLU.gluOrtho2D(0, Lwjgl.getWindowWidth(), Lwjgl.getWindowHeight(), 0);
				
				glColor4f(0,0,0,1);
				//left
				glRectf((Lwjgl.getWindowWidth() / 2 - 3) - 3, (Lwjgl.getWindowHeight() / 2 - 3), (Lwjgl.getWindowWidth() / 2 + 3) - 3, (Lwjgl.getWindowHeight() / 2 + 3));
				//right
				glRectf((Lwjgl.getWindowWidth() / 2 - 3) + 3, (Lwjgl.getWindowHeight() / 2 - 3), (Lwjgl.getWindowWidth() / 2 + 3) + 3, (Lwjgl.getWindowHeight() / 2 + 3));
				//center
				glRectf(Lwjgl.getWindowWidth() / 2 - 3, Lwjgl.getWindowHeight() / 2 - 3, Lwjgl.getWindowWidth() / 2 + 3, Lwjgl.getWindowHeight() / 2 + 3);
				//up
				glRectf((Lwjgl.getWindowWidth() / 2 - 3), (Lwjgl.getWindowHeight() / 2 - 3) + 3, (Lwjgl.getWindowWidth() / 2 + 3), (Lwjgl.getWindowHeight() / 2 + 3) + 3);
				//down
				glRectf((Lwjgl.getWindowWidth() / 2 - 3), (Lwjgl.getWindowHeight() / 2 - 3) - 3, (Lwjgl.getWindowWidth() / 2 + 3), (Lwjgl.getWindowHeight() / 2 + 3) - 3);
				
				glColor4f(255,255,255,1);
				//center
				glRectf(Lwjgl.getWindowWidth() / 2 - 2, Lwjgl.getWindowHeight() / 2 - 2, Lwjgl.getWindowWidth() / 2 + 2, Lwjgl.getWindowHeight() / 2 + 2);
				Lwjgl.setModelViewMode();
				
				// ################################################# DISPLAY
				Lwjgl.render();
				// #################################################
			}
			
		});
		loop.start();
		Lwjgl.destroy();
	}
	
	public static void updatePhysicsFPS()
	{
		if (System.currentTimeMillis() - lastPFPS > 1000)
		{
			pfps_display = "PFPS:"+ pfps;
			pfps = 0;
			lastPFPS += 1000;
		}
		pfps++;
	}
	
	public static void updateFPS()
	{
		if (System.currentTimeMillis() - lastFPS > 1000)
		{
			Display.setTitle("FPS:"+ fps  + ", " + pfps_display);
			fps = 0;
			lastFPS += 1000;
		}
		fps++;
	}
}
