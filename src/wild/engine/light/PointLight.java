package wild.engine.light;

import wild.engine.maths.Vec3;

public class PointLight {

	private BaseLight baseLight;
	private Attenuation atten;
	private Vec3 position;
	private float range;
	
	public PointLight(BaseLight baseLight, Attenuation atten, Vec3 position, float range){
		this.baseLight = baseLight;
		this.atten = atten;
		this.position = position;
		this.range = range;
	}
	
	public BaseLight getBaseLight() {
		return baseLight;
	}
	public void setBaseLight(BaseLight baseLight) {
		this.baseLight = baseLight;
	}
	public Attenuation getAtten() {
		return atten;
	}
	public void setAtten(Attenuation atten) {
		this.atten = atten;
	}
	public Vec3 getPosition() {
		return position;
	}
	public void setPosition(Vec3 position) {
		this.position = position;
	}

	public float getRange() {
		return range;
	}

	public void setRange(float range) {
		this.range = range;
	}
	
}
