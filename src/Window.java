

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;


public class Window {

	private int width, height;
	private String title;
	private int  fps;
	private long startTime;
	private long lastFrame;
	
	public Window(int w, int h, String s) {
		//Create Window
		title = s;
		width = w;
		height = h;

		
		try {
		    Display.setDisplayMode(new DisplayMode(width, height));
		    Display.setTitle(s);
		    //Display.setVSyncEnabled(true);
		    Display.create();
		} catch (LWJGLException e) {
		    e.printStackTrace();
		    System.exit(0);
		}
		
    	System.out.println("OpenGL version is: ".concat(GL11.glGetString(GL11.GL_VERSION)));
    	
		//Make the Cursor invisible
		Cursor emptyCursor;
		try {
			emptyCursor = new Cursor(1, 1, 0, 0, 1, BufferUtils.createIntBuffer(1), null);
			Mouse.setNativeCursor(emptyCursor);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Init OpenGL
		GL11.glShadeModel(GL11.GL_SMOOTH);      
		GL11.glEnable(GL11.GL_CULL_FACE);
     
 
		GL11.glClearColor(1F, 1F, 1F, 1F);                                                   
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, w, h, 0, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);

		//Initialize the Game
		Game.InitGL();
		// Main while loop
		while (!Display.isCloseRequested() && !Game.isQuitted() ) {
			int delta = getDelta();
			//Handel Input (Keyboard, Mouse)
			Game.input(delta);
			//Clear Graphic Buffers
		    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);	
		    //
		    Game.updateAndRender(delta);

		    //Update Window / Flush 
		    Display.update();
		    //Calculate FPS
		    fps++;
		    if (System.currentTimeMillis() - startTime > 1000 || startTime == 0) {
		    	Display.setTitle(title.concat(" [FPS: ".concat(String.valueOf(fps).concat("]"))));
		    	startTime = System.currentTimeMillis();
		    	fps = 0;
		    }
		    int i = GL11.glGetError();
		    if (i != GL11.GL_NO_ERROR) {
		    	System.out.println(GLU.gluErrorString(i));
		    }
		}
		//Destor the Window
		Display.destroy();
	}
	
	private int getDelta() {
		//Needed for Timebased events
		long time = System.currentTimeMillis();
		int delta = (int) (time - lastFrame);
		lastFrame = time;
		if (delta < 0) {delta = 0;}
		return delta;
	}

	public Window() {
		//Alternavie Constructor
		this(800, 600, "Window");
	}

}
