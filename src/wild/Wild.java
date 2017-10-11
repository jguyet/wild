package wild;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.lwjgl.opengl.Display;

import wild.engine.components.Camera;
import wild.engine.components.Input;
import wild.engine.components.Tex;
import wild.engine.components.Transform;
import wild.engine.components.loop.FrameLoop;
import wild.engine.components.loop.MotorGraphics;
import wild.engine.components.shape.Block;
import wild.engine.components.shape.obj.Model;
import wild.engine.components.shape.obj.OBJLoader;
import wild.engine.lib.lwjgl.Lwjgl;
import wild.engine.lib.lwjgl.shaders.Shader;
import wild.engine.maths.Vec3;
import wild.utils.Color;
import wild.utils.RGB;
import wild.world.World;

public class Wild {
	
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
	public static World		world;
	
	public static Model model = null;
	public static Model egg = null;

	public static void main(String ...args) {
		
		lastFPS = System.currentTimeMillis();
		lastPFPS = System.currentTimeMillis();
		Lwjgl.createWindow(WIDTH, HEIGHT, "Wild");
		Lwjgl.initSettings();
		Lwjgl.clearScreen();
		
		camera = new Camera();
		transform = new Transform();
		
		world = new World();
		
		System.out.println("OS name " + System.getProperty("os.name"));
	    System.out.println("OS version " + System.getProperty("os.version"));
	    System.out.println("LWJGL version " + Lwjgl.getVersion());
	    System.out.println("OpenGL version " + Lwjgl.getOpenGlVersion());
		
		Transform.setCamera(camera);
        
        //world.loadByPos(new Vector3f(-camera.getPos().x, -camera.getPos().y, -camera.getPos().z));
        
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
				//world.loadByPos(new Vector3f(-camera.getPos().x, -camera.getPos().y, -camera.getPos().z));
			}

			@Override
			public void graphicRenderingLoop() {// ################################################# RENDERING LOOP
				// TODO Auto-generated method stub
				Lwjgl.clearScreen();
				updateFPS();
				world.loadByPos(new Vec3(-camera.getPos().x, -camera.getPos().y, -camera.getPos().z));
				// ################################################# ADDCAMERA SETTINGS
				Transform.setProjection(70f, Lwjgl.getWindowWidth(), Lwjgl.getWindowHeight(),
						0.3f, 1000);
				transform.setProjectedTransformation();
				// #################################################
				
				// ################################################# ADD BUFFERED CUBES
				
				world.render();
				// #################################################
				
				// ################################################# Skybox
				Transform.setProjection(70f, Lwjgl.getWindowWidth(), Lwjgl.getWindowHeight(),
						0.3f, 10000);
				transform.setProjectedTransformation();
				Block sky = new Block(new Vec3(-camera.getPos().x, -camera.getPos().y, -camera.getPos().z), 5000, Color.WHITE);
				Lwjgl.addSkyBox(sky);
				// #################################################
				
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
