
import org.lwjgl.opengl.*;


public class DLGrid {
		
	private int DL;

	
	public void createGrid(int spacing)
	{
		DL = GL11.glGenLists(1);
		//Create DL		
		GL11.glNewList(DL, GL11.GL_COMPILE);
			GL11.glBegin(GL11.GL_LINES);
				for (int i = 1; i < Game.width / spacing +1; i++) {
					GL11.glVertex2f(spacing*i, 0);
					GL11.glVertex2f(spacing*i, Game.height);
				}
				for (int i = 1; i < Game.height / spacing +1; i++) {
					GL11.glVertex2f(0, spacing*i);
					GL11.glVertex2f(Game.width, spacing*i);
				}
			GL11.glEnd();
		GL11.glEndList();
	}
	
	public void render(int delta) {
		//Render
		GL11.glCallList(DL);
	}
	
	protected void finalize() throws Throwable
	{
	  GL11.glDeleteLists(DL, 1);
	  super.finalize(); 
	} 
	

}
