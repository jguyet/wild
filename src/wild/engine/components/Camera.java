package wild.engine.components;

import static org.lwjgl.opengl.GL11.GL_FILL;
import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11.GL_LINE;
import static org.lwjgl.opengl.GL11.glLineWidth;
import static org.lwjgl.opengl.GL11.glPolygonMode;

import org.lwjgl.input.Mouse;

import wild.Editor;
import wild.editor.ECRaycast;
import wild.engine.components.shape.Block;
import wild.engine.lib.lwjgl.Lwjgl;
import wild.engine.maths.Mathf;
import wild.engine.maths.Quat;
import wild.engine.maths.Vec2;
import wild.engine.maths.Vec3;
import wild.utils.Color;

public class Camera {

	public static final Vec3 yAxis = new Vec3(0, 1, 0);
	public static final float GRAVITY = 0.05f;
	public static float sensibility = 0.1f;
	public static float speed = 0.01f;
	
	
	private Quat rotation = new Quat();
	//private Vec3 rotation;
	private Vec3 pos;
	private Vec3 dir = new Vec3();
	public ECRaycast ray = new ECRaycast(5f, 0.004f);
	public boolean grounded = false;
	public boolean gravity = true;
	
	public float gravityFactor = 0;
	
	public Camera() {
		this(new Vec3(0, 10, 0), new Quat(0, 0, 0, 1));
	}

	public Camera(Vec3 pos, Quat rotation) {
		this.pos = pos;
		this.rotation = rotation;
		
		if (mouseLocked)
			Input.setCursor(false);
	}

	boolean mouseLocked = true;
	
    Vec2 centerPosition = new Vec2(Lwjgl.getWindowWidth()/2, Lwjgl.getWindowHeight()/2);
    
    float xa = 0, ya = 0, za = 0;
    
	public void input(){
		
		dir.x = 0;
		dir.y = 0;
		dir.z = 0;
		
        if(mouseLocked && Mouse.isGrabbed())
        {
                Vec2 deltaPos = Input.getMousePosition().sub(centerPosition);
                
                boolean rotX = deltaPos.x != 0;
                boolean rotY = deltaPos.y != 0;
                
                if(rotX)
                	rotation.x -= Mouse.getDY() * sensibility;
                
                if(rotY)
                	rotation.y += Mouse.getDX() * sensibility;
                if(rotY || rotX)
                        Input.setMousePosition(new Vec2(Lwjgl.getWindowWidth()/2, Lwjgl.getWindowHeight()/2));
        }
		
        //LOCK 90deg
        /*if (rotation.x > 90)
        	rotation.x = 90;
        if (rotation.x < -90)
        	rotation.x = -90;*/
        
        
		if(Input.getKey(Input.KEY_W) || Input.getKey(Input.KEY_UP)) {
			dir.z = -speed;
		}
		if(Input.getKey(Input.KEY_S) || Input.getKey(Input.KEY_DOWN)) {
			dir.z = speed;
		}
		if(Input.getKey(Input.KEY_A) || Input.getKey(Input.KEY_LEFT)) {
			dir.x = -speed;
		}
		if(Input.getKey(Input.KEY_D) || Input.getKey(Input.KEY_RIGHT)) {
			dir.x = speed;
		}
		if (Input.getKey(Input.KEY_SPACE) && grounded) {
			dir.y = 0.25f;
		}
		if (Input.getKeyDownDbl(Input.KEY_SPACE)) {
			System.out.println("DOUBLECLIK");
		}
		
		if (Input.getKeyDown(Input.KEY_LSHIFT)) {
			mouseLocked = !mouseLocked;
			Input.setCursor(mouseLocked);
		}
		
		xa += dir.x * Mathf.cos(Mathf.toRadians(rotation.y)) - dir.z * Mathf.sin(Mathf.toRadians(rotation.y));
		ya += dir.y;
		za += dir.z * Mathf.cos(Mathf.toRadians(rotation.y)) + dir.x * Mathf.sin(Mathf.toRadians(rotation.y));
		
		
		move(xa, ya, za);
		
		xa *= 0.9f;
		ya *= 0.9f;
		za *= 0.9f;
		System.out.println(pos.toString());
	}
	
	public void move(float x, float y, float z) {
		
		if (gravity) {
			gravityFactor += Camera.GRAVITY * 0.01f;
			if (grounded)
				gravityFactor = 0;
			y -= gravityFactor;
		}
		
		
		if (Editor.worldeditor.collide(x, 0, 0, new Vec3(pos.x, pos.y, pos.z)) == false)
			pos.x += x;
		if (Editor.worldeditor.collide(0, y, 0, new Vec3(pos.x, pos.y, pos.z)) == false) {
			pos.y += y;
			grounded = false;
		} else
			grounded = true;
		if (Editor.worldeditor.collide(0, 0, z, new Vec3(pos.x, pos.y, pos.z)) == false)
			pos.z += z;
	}
	
	public Vec3 getPos() {
		return pos;
	}

	public void setPos(Vec3 pos) {
		this.pos = pos;
	}
	
	public Vec3 getDir() {
		return dir;
	}
	
	public void setDir(Vec3 dir) {
		this.dir = dir;
	}

	public Vec3 getForward() {
		Vec3 r = new Vec3(0, 0, 0);
		Vec3 rot = new Vec3(rotation.x, rotation.y, rotation.z);
		
		float cosy = (float) Math.cos(Math.toRadians(rot.y + 90));
		float sinY = (float) Math.sin(Math.toRadians(rot.y + 90));
		float cosP = (float) Math.cos(Math.toRadians(rot.x));
		float sinP = (float) Math.sin(Math.toRadians(rot.x));
		
		r.x = cosy * cosP;
		r.y = sinP;
		r.z = sinY * cosP;
		
		return r.normalize();
	}
	
	public Vec3 getForward2() {
		Vec3 r = new Vec3(0, 0, 0);
		Vec3 rot = new Vec3(rotation.x, rotation.y, rotation.z);
		
		float cosy = (float) Math.cos(Math.toRadians(rot.y - 90));
		float sinY = (float) Math.sin(Math.toRadians(rot.y - 90));
		float cosP = (float) Math.cos(Math.toRadians(-rot.x));
		float sinP = (float) Math.sin(Math.toRadians(-rot.x));
		
		r.x = cosy * cosP;
		r.y = sinP;
		r.z = sinY * cosP;
		
		return r.normalize();
	}
	
	public Vec3 getRight() {
		Vec3 r = new Vec3(0, 0, 0);
		Vec3 rot = new Vec3(rotation.x, rotation.y, rotation.z);
		
		float cosy = (float) Math.cos(Math.toRadians(rot.y));
		float sinY = (float) Math.sin(Math.toRadians(rot.y));
		
		r.x = cosy;
		r.z = sinY;
		
		return r.normalize();
	}
	
	public Quat getRotation() {
		return (this.rotation);
	}
	
	public void applySelectionActions()
	{
		ray.setPosition(getEyePosition().add(0,0.80f,0));
		ray.setDirection(getForward2());
		ray.update(Editor.worldeditor);
		
		if (ray.getHit() != null)
		{
			if (ray.getHit().getBlock() != null)
			{
				Vec3 blockPosition = ray.getHit().getBlockPosition();
				Vec3  hitPoint = ray.getExactHitPoint();
				if (Input.getMouse(0))
					removeBlock(blockPosition);
				else if (Input.getMouse(1))
					placeBlock(hitPoint,blockPosition);
				else
					setPotentialAction(hitPoint,blockPosition);
				Editor.selectedPosition = hitPoint;
			}
		}
	}
	
	private void removeBlock(Vec3 block)
	{
		Editor.lastposeobject = System.currentTimeMillis();
		Editor.worldeditor.removeBlock((int)block.x, (int)block.y, (int)block.z);
		//net.send(new BlockActionPacket(core.getGame().getPlayer().getID(), 0, block.x, block.y, block.z, 0), Protocol.TCP);
	}
	
	private void placeBlock(Vec3 point, Vec3 blockposition)
	{
		Editor.lastposeobject = System.currentTimeMillis();
		//Vector3f playerPos = Ubercube.getInstance().getGameCore().getGame().getPlayer().getRigidBody().getBody().getCollider().getPosition();
		//Vector3f playerSize = ((AABoxCollider) Ubercube.getInstance().getGameCore().getGame().getPlayer().getRigidBody().getBody().getCollider()).getSize();

		int x = (int) point.x;
		int y = (int) point.y;
		int z = (int) point.z;

		float rx = point.x;
		float ry = point.y;
		float rz = point.z;
		
		float vx = rx - x;
		float vy = ry - y;
		float vz = rz - z;
		
		System.out.println(vx + "," + vy + "," + vz);

		Vec3 check = new Vec3(vx, vy, vz).checkNormals();

		int xp = (int) check.x;
		int yp = (int) check.y;
		int zp = (int) check.z;

		Vec3 blockPos = new Vec3(x + xp, y + yp, z + zp);
		
		if (Editor.worldeditor.getBlock((int)blockPos.x, (int)blockPos.y, (int)blockPos.z) == null)
			Editor.worldeditor.addBlock((int)blockPos.x, (int)blockPos.y, (int)blockPos.z);
		/*boolean isCollisionWithPlayer = CollisionDetector.detectAABBvsAABB(playerPos, playerSize, blockPos, blockSize);
		if (!isCollisionWithPlayer)
			net.send(new BlockActionPacket(core.getGame().getPlayer().getID(), 1, x + xp, y + yp, z + zp, 0x7f555555), Protocol.TCP);*/
	}
	
	private void setPotentialAction(Vec3 point, Vec3 blockposition)
	{
		
		int x = (int) point.x;
		int y = (int) point.y;
		int z = (int) point.z;

		float rx = point.x;
		float ry = point.y;
		float rz = point.z;
		
		float vx = rx - x;
		float vy = ry - y;
		float vz = rz - z;

		Vec3 check = new Vec3(vx, vy, vz).checkNormals();

		int xp = (int) check.x;
		int yp = (int) check.y;
		int zp = (int) check.z;
		
		Vec3 bp = new Vec3(x + xp, y + yp, z + zp );
		
		glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
		glLineWidth(5);
		Lwjgl.addSquare(new Block(bp, 1f, Color.RED));
		glLineWidth(1);
		glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
		
		glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
		glLineWidth(5);
		Lwjgl.addSquare(new Block(blockposition, 1f, Color.BLUE));
		glLineWidth(1);
		glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
		
		glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
		glLineWidth(5);
		Lwjgl.addSquare(new Block(new Vec3(x, y, z), 1f, Color.BLACK));
		glLineWidth(1);
		glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);

	}
	
	public Vec3 getEyePosition()
	{
		return pos.copy().add(0f, 1.25f, 0f);
	}

}
