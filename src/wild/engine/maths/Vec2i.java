package wild.engine.maths;

public class Vec2i {
	public static final Vec2i UP = new Vec2i(0, 1);
	public static final Vec2i RIGHT = new Vec2i(1, 0);
	
	public int x, y;

	public Vec2i() {
		this(0, 0);
	}
	
	public Vec2i(int v) {
		this(v, v);
	}
	
	public Vec2i(Vec2i v) {
		this(v.x, v.y);
	}
	
	public Vec2i(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Vec2i cross(Vec2i r) {
		return new Vec2i(r.x, -r.y);
	}
	
	public int max() {
		return Math.max(x, y);
	}
	
	public int min() {
		return Math.min(x, y);
	}

	public Vec2i negate() {
		x = -x;
		y = -y;

		return this;
	}

	public Vec2i add(Vec2i vec) {
		x += vec.x;
		y += vec.y;

		return this;
	}

	public Vec2i sub(Vec2i vec) {
		x -= vec.x;
		y -= vec.y;

		return this;
	}

	public Vec2i mul(Vec2i vec) {
		x *= vec.x;
		y *= vec.y;

		return this;
	}

	public Vec2i div(Vec2i vec) {
		x /= vec.x;
		y /= vec.y;
		
		return this;
	}
	
	public Vec2i add(int x, int y) {
		this.x += x;
		this.y += y;

		return this;
	}

	public Vec2i sub(int x, int y) {
		this.x -= x;
		this.y -= y;

		return this;
	}

	public Vec2i mul(int x, int y) {
		this.x *= x;
		this.y *= y;

		return this;
	}

	public Vec2i div(int x, int y) {
		this.x /= x;
		this.y /= y;
		
		return this;
	}
	
	public Vec2i add(int v) {
		x += v;
		y += v;

		return this;
	}

	public Vec2i sub(int v) {
		x -= v;
		y -= v;

		return this;
	}

	public Vec2i mul(int v) {
		x *= v;
		y *= v;

		return this;
	}

	public Vec2i div(int v) {
		x /= v;
		y /= v;

		return this;
	}

	public Vec2i copy() {
		return new Vec2i(x, y);
	}

	public String toString() {
		return x + " " + y;
	}
	
	public boolean equals(Vec2i v) {
		return x == v.x && y == v.y;
	}
	
	public void print() {
		System.out.print(this);
	}
	
	public void println() {
		System.out.println(this);
	}
}
