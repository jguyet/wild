package wild.engine.light;

import wild.engine.maths.Vec3;

public class BaseLight {

	private Vec3 color;
	private float intensity;
	
	public BaseLight(Vec3 color, float intensity){
		this.color = color;
		this.intensity = intensity;
	}

	public Vec3 getColor() {
		return color;
	}

	public void setColor(Vec3 color) {
		this.color = color;
	}

	public float getIntensity() {
		return intensity;
	}

	public void setIntensity(float intensity) {
		this.intensity = intensity;
	}
	
}
