package wild.engine.light;

import wild.engine.maths.Vec3;

public class DirectionalLight {

	private BaseLight base;
	private Vec3 direction;
	
	public DirectionalLight(BaseLight base, Vec3 direction){
		this.base = base;
		this.direction = direction;
	}

	public BaseLight getBase() {
		return base;
	}

	public void setBase(BaseLight base) {
		this.base = base;
	}

	public Vec3 getDirection() {
		return direction;
	}

	public void setDirection(Vec3 direction) {
		this.direction = direction.normalize();
	}
	
}
