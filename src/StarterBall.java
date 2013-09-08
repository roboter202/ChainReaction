import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;


public class StarterBall {
	
	private Level level;
	private int xPos;
	private int yPos;
	
	
	public StarterBall(Level lev) {
		level = lev;
		xPos = lev.getWidth() / 2;
		yPos = lev.getHeight() / 2;
	}
	
	public void update() {
		
		if (Mouse.isInsideWindow()) {
			xPos = Mouse.getX();
			yPos = level.getHeight() - Mouse.getY();
		}
	}
	
	public void render() {
		//Render
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc (GL11.GL_ONE, GL11.GL_ONE);
		GL11.glColor3f(0.588F, 0.588F, 0.588F);
		level.drawMovingBall(xPos, yPos, level.getStarterBallSize());
		GL11.glDisable(GL11.GL_BLEND);
	}

	public void place() {
		new Ball(level, xPos, yPos, 0);
	}
	

}
