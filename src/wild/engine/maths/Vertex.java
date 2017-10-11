package wild.engine.maths;

public class Vertex {

	public static final int size = 8;

	private Vec3 pos;
    private Vec2 texCoord;
    private Vec3 normal;
    
    public Vertex(Vec3 pos)
    {
            this(pos, new Vec2(0,0));
    }
    
    public Vertex(Vec3 pos, Vec2 texCoord)
    {
            this(pos, texCoord, new Vec3(0,0,0));
    }
    
    public Vertex(Vec3 pos, Vec2 texCoord, Vec3 normal)
    {
            this.pos = pos;
            this.texCoord = texCoord;
            this.normal = normal;
    }

    public Vec3 getPos()
    {
            return pos;
    }

    public void setPos(Vec3 pos)
    {
            this.pos = pos;
    }

    public Vec2 getTexCoord()
    {
            return texCoord;
    }

    public void setTexCoord(Vec2 texCoord)
    {
            this.texCoord = texCoord;
    }

    public Vec3 getNormal()
    {
            return normal;
    }

    public void setNormal(Vec3 normal)
    {
            this.normal = normal;
    }

}
