package wild.engine.light;

import wild.engine.maths.Vec3;

public class SpotLight {

	private PointLight pointLight;
	private Vec3 direction;
	private float cutoff;

	public SpotLight(PointLight pointLight, Vec3 direction, float cutoff) {
		this.pointLight = pointLight;
		this.direction = direction.normalize();
		this.cutoff = cutoff;
	}

	public PointLight getPointLight() {
		return pointLight;
	}

	public void setPointLight(PointLight pointLight) {
		this.pointLight = pointLight;
	}

	public Vec3 getDirection() {
		return direction;
	}

	public void setDirection(Vec3 direction) {
		this.direction = direction;
	}

	public float getCutoff() {
		return cutoff;
	}

	public void setCutoff(float cutoff) {
		this.cutoff = cutoff;
	}

}
