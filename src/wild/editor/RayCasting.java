package wild.editor;

import static org.lwjgl.opengl.GL11.GL_FILL;
import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11.GL_LINE;
import static org.lwjgl.opengl.GL11.glLineWidth;
import static org.lwjgl.opengl.GL11.glPolygonMode;

import java.util.ArrayList;

import wild.Editor;
import wild.engine.components.shape.Block;
import wild.engine.lib.lwjgl.Lwjgl;
import wild.engine.maths.Vec3;
import wild.utils.Color;

public class RayCasting {

	public static Vec3 rayCast(Vec3 position, Vec3 direction, float distance, float precision) {
		
		
		for (float i = 0; i < distance; i += precision) {
			Vec3 point = move(position, direction, -i);

			Lwjgl.addSquare(new Block(point, 0.001f, Color.WHITE));
			if (Editor.worldeditor.getBlock((int)(point.x - 0.5f), (int)(point.y - 0.5f), (int)(point.z - 0.5f)) != null)
				return (point);
		}
		return null;
	}
	
	public static Vec3 move(Vec3 pos, Vec3 dir, float amt) {
		pos = pos.copy().add(dir.mul(amt));
		return (pos);
	}
}
