package wild.engine.lib.lwjgl;

import wild.engine.components.Tex;

public interface TexturedSquare {

	public void top(Tex texture);
	
	public void left(Tex texture);
	
	public void front(Tex texture);
	
	public void right(Tex texture);
	
	public void back(Tex texture);
	
	public void bottom(Tex texture);
	
}
