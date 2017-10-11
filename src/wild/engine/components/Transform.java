package wild.engine.components;

import wild.engine.lib.lwjgl.Lwjgl;
import wild.engine.maths.Mat4;
import wild.engine.maths.Vec3;

public class Transform {
	
	private static Camera camera;
	
	private static float zNear;//vision de depart
	private static float zFar;//vision max en profondeur
	private static float width;
	private static float height;
	private static float fov;//angle de vision
	
	
	
	private Vec3 translation;
	private Vec3 rotation;
	private Vec3 scale;

	public Transform() {
		translation = new Vec3(0, 0, 0);
		rotation = new Vec3(0, 0, 0);
		scale = new Vec3(1, 1, 1);
	}

	/*public Mat4 getTransformation() {
		Mat4 translationMatrix = new Mat4().initTranslation(
				translation.x, translation.y, translation.z);
		Mat4 rotationMatrix = new Mat4().initRotation(rotation.x,
				rotation.y, rotation.z);
		Mat4 scaleMatrix = new Mat4().initScale(scale.x,
				scale.y, scale.z);

		return translationMatrix.mul(rotationMatrix.mul(scaleMatrix));
	}*/

	public void setProjectedTransformation() {
		
		// ######################################################## SANS LIB
		//Matrix4f transformationMatrix = getTransformation();
		//Matrix4f projectionMatrix = new Matrix4f().initProjection(fov, width, height, zNear, zFar);
		//Matrix4f cameraRotation = new Matrix4f().initCamera(camera.getRotation(), camera.getUp());
		//Matrix4f cameraTranslation = new Matrix4f().initTranslation(-camera.getPos().x, -camera.getPos().y,-camera.getPos().z);
		//return projectionMatrix.mul(cameraRotation.mul(cameraTranslation.mul(transformationMatrix)));
		
		// ######################################################## AVEC LIB
		Lwjgl.setProjectionPerspective(fov, width, height, zNear, zFar);
		Lwjgl.setCameraTransformation(camera);
	}

	public Vec3 getTranslation() {
		return translation;
	}
	
	public static void setProjection(float fov, float width, float height, float zNear, float zFar){
		Transform.fov = fov;
		Transform.width = width;
		Transform.height = height;
		Transform.zNear = zNear;
		Transform.zFar = zFar;
	}

	public void setTranslation(Vec3 translation) {
		this.translation = translation;
	}

	public Vec3 getRotation() {
		return rotation;
	}

	public void setRotation(Vec3 rotation) {
		this.rotation = rotation;
	}

	public void setRotation(float x, float y, float z) {
		this.rotation = new Vec3(x, y, z);
	}

	public void setTranslation(float x, float y, float z) {
		this.translation = new Vec3(x, y, z);
	}

	public Vec3 getScale() {
		return scale;
	}

	public void setScale(Vec3 scale) {
		this.scale = scale;
	}

	public void setScale(float x, float y, float z) {
		this.scale = new Vec3(x, y, z);
	}

	public static Camera getCamera() {
		return camera;
	}

	public static void setCamera(Camera camera) {
		Transform.camera = camera;
	}

}
