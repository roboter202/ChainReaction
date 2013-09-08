
import java.io.IOException;

import org.lwjgl.opengl.*;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;


public class BallDrawer {
		
	private Texture texture;
	private int DL;
	
	public void init()
	{
		try {
			texture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/ball.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		DL = GL11.glGenLists(1);
		
		GL11.glNewList(DL, GL11.GL_COMPILE);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc (GL11.GL_ONE, GL11.GL_ONE);
		
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glTexCoord2d(0, 0);
		GL11.glVertex2d(-1, -1);
		
		GL11.glTexCoord2d(0, 1);
		GL11.glVertex2d(-1, 1);
		
		GL11.glTexCoord2d(1, 1);
		GL11.glVertex2d(1,1);
		
		GL11.glTexCoord2d(1, 0);
		GL11.glVertex2d(1, -1);
		GL11.glEnd();
		
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEndList();
	}
	
	public void render(double x, double y, double radius) {
		//Render
		
		texture.bind();
		GL11.glPushMatrix();
			GL11.glTranslated(x, y, 0);
			GL11.glScaled(radius, radius, 0);
			GL11.glCallList(DL);
		GL11.glPopMatrix();
	}
		

}
