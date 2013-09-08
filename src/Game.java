import java.awt.Font;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;



public final class Game {
	
	public static final int width = 800;
	public static final int height = 600 ;
	private static double scrBackColor = 0.2;
	private static int colorFade = 0;
	//private static Window window;
	
	private static int levelCnt = 0;
	private static Level curLevel;
	private static long scoreTotal = 0;
	private static DLGrid grid = new DLGrid();

	private static UnicodeFont font;
	private static UnicodeFont font2;
	
	private static boolean speedMode = false;
	private static boolean quit = false;
	
	
		
	
	public static void newGame() {
		 new Window(width, height, "Chain Reaction");
	}

	public static void updateAndRender(int delta) {
		if (speedMode) {
			delta = delta * 7;
		}
		//Is Level Done or did the User fail? -> Create new Level
		if (curLevel.allBallsDead() && curLevel.isStarterBallPlaced()) {
			if (curLevel.isDone()) {
				scoreTotal += curLevel.getPoints();
				levelCnt++;
				curLevel.createLevel( levelCnt +1);
			} else {
				curLevel.createLevel( levelCnt +1);	
			}
		}
		
		//Calculate Background Color Fade
		if (curLevel.isDone()) {
			if (colorFade == 1 && scrBackColor < 0.258) {
				scrBackColor = scrBackColor + (double)(delta)/1000 * 0.4;
				if (scrBackColor >= 0.258) {
					scrBackColor = 0.258;
					colorFade = 0;
				}
			} else if (colorFade != 1) {
				colorFade = 1;
			}
		} else {
			if (colorFade == -1 && scrBackColor > 0.2) {
				scrBackColor = scrBackColor - (double)(delta)/1000 * 0.4;
				if (scrBackColor <= 0.2) {
					scrBackColor = 0.2;
					colorFade = 0;
				}
			} else if (colorFade != -1) {
				colorFade = -1;
			}
		}
		//Draw Background Color
		GL11.glColor3d(scrBackColor, scrBackColor, scrBackColor);
		
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex3d(0, 0, -1);
			GL11.glVertex3d(0, Game.height, -1);
			GL11.glVertex3d(Game.width,Game.height, -1);
			GL11.glVertex3d(Game.width, 0, -1);
		GL11.glEnd();
		
		
		//Draw Grid
		GL11.glColor3f(0.17F, 0.17F, 0.17F);
		grid.render(delta);
		//Draw Level
		curLevel.updateAndRender(delta);
		
		//Draw Text
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			String s;
			s = String.valueOf(scoreTotal).concat(" total points");
			font.drawString(0, height - font.getHeight(s), s);
			
			s = "(c) 2012 by Christian P. J. Windeck";
			font.drawString((width - font.getWidth(s)) / 2, height - font.getHeight(s), s);
			
			s = String.valueOf(curLevel.getPoints()).concat(" level points");
			font.drawString(width - font.getWidth(s),  height - font.getHeight(s), s);
			
			if (speedMode) {
				s = "Speed Mode: On";
				font.drawString(width - font.getWidth(s),  0, s);
			}
			
			if (curLevel.getBallsExpanded() >= 0) {
				s = String.valueOf(curLevel.getBallsExpanded()).concat(" of ").concat(String.valueOf(curLevel.getBallsToExpand())).concat(" Balls expanded");
				font.drawString(0, 0, s);
			}
			s = "Level ".concat(String.valueOf(levelCnt+1));
			font.drawString(0, font.getHeight(s), s);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
	}


	public static void input(int delta) {
		//Was there a Mouse event? -> Place StarterBall
		if (Mouse.next()) {
			if (Mouse.getEventButton() == 0 && Mouse.getEventButtonState() && !curLevel.isStarterBallPlaced()) {
				curLevel.placeStarterBall();
			}
		}
		if (Keyboard.next()) {
			if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE && Keyboard.getEventKeyState()) {
				quit = true;
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_CAPITAL) && Keyboard.isKeyDown(Keyboard.KEY_C) && Keyboard.isKeyDown(Keyboard.KEY_Q) ) {
				speedMode = !speedMode;
			}
		}
	}

	@SuppressWarnings("unchecked")
	public static void InitGL() {
		//Create Grid
		//Create Fonts
		//Create Initial Level
		grid.createGrid(30);
		Font awtFont = new Font("Helvatica", Font.PLAIN, 12);//proxima-nova
		font = new UnicodeFont(awtFont);
		font.addAsciiGlyphs();
		font.addGlyphs(400, 600);
		font.getEffects().add(new ColorEffect(java.awt.Color.WHITE));
		try {
			font.loadGlyphs();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		awtFont = new Font("Helvatica", Font.PLAIN, 15);//proxima-nova
		font2 = new UnicodeFont(awtFont);
		font2.addAsciiGlyphs();
		font2.addGlyphs(400, 600);
		font2.getEffects().add(new ColorEffect(java.awt.Color.WHITE));
		try {
			font2.loadGlyphs();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		curLevel = new Level(width, height);
		curLevel.createLevel( levelCnt +1);
	}

	public static UnicodeFont getFont() {
		//Give Font 1
		return font;
	}
	
	public static UnicodeFont getFont2() {
		//Give Font 2
		return font2;
	}

	public static boolean isQuitted() {
		return quit;
	}
	
}
