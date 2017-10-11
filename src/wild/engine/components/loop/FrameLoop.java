package wild.engine.components.loop;

import wild.utils.Time;

public class FrameLoop {

	public long		graphRate = (long)((double) 1000000000L) / 10000;
	public double	physicRate = (long)((double) 1000000000L) / 120;
	public boolean	running = false;
	
	private MotorGraphics motor;
	private GraphicLoop graphloop;
	private PhysicLoop physicloop;
	private InputLoop inputloop;
	
	private static final Object locker = new Object();
	
	public FrameLoop(MotorGraphics motor)
	{
		this.motor = motor;
	}
	
	public void start() {

		if (running)
			return;
		running = true;
		physicloop = new PhysicLoop();
		graphloop = new GraphicLoop();
		inputloop = new InputLoop();
		
		physicloop.start();
		inputloop.start();
		
		graphloop.run();
	}

	public void stop() {
		if (!running)
			return;

		running = false;
	}
	
	private void call(int id) {
		//synchronized (locker) {
			switch (id) {
			case 1:
				motor.graphicRenderingLoop();
				break ;
			case 2:
				motor.graphicPhysicalLoop();
				break ;
			case 3:
				motor.inputLoop();
				break ;
			}
		//}
	}
	
	public class GraphicLoop implements Runnable {

		public GraphicLoop() {
		
		}
		
		@Override
		public void run() {

			long frames = 0;
			long frameCounter = 0;

			long lastTime = Time.getTime();

			while (running) {
				
				long startTime = Time.getTime();
				long passedTime = startTime - lastTime;
				
				if (passedTime > graphRate)
				{
					lastTime = Time.getTime();
					Time.setDelta(passedTime);
					Time.setFrame(frames);
					call(1);
					frames++;
					frameCounter++;
				}
				if (frameCounter >= 1000000000L) {
					frames = 0;
					frameCounter = 0;
				}
			}
		}
	}
	
	public class PhysicLoop implements Runnable {

		private Thread _t;
		
		public PhysicLoop() {
			this._t = new Thread(this);
		}
		
		public void start() {
			this._t.start();
		}
		
		@Override
		public void run() {

			long lastTime = Time.getTime();
			while (running) {
				
				long startTime = Time.getTime();
				long passedTime = startTime - lastTime;
				
				if (passedTime > physicRate)
				{
					lastTime = Time.getTime();
					call(2);
				}
			}
		}
	}
	
	public class InputLoop implements Runnable {

		private Thread _t;
		
		public InputLoop() {
			this._t = new Thread(this);
		}
		
		public void start() {
			this._t.start();
		}
		
		@Override
		public void run() {

			long lastTime = Time.getTime();
			while (running) {
				
				long startTime = Time.getTime();
				long passedTime = startTime - lastTime;
				
				if (passedTime > physicRate)
				{
					lastTime = Time.getTime();
					call(3);
				}
			}
		}
	}
}
