import java.util.Random;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.UnicodeFont;


public class Ball {

  private double xPos;
  private double yPos;
  private double colorRed, colorGreen, colorBlue;
  private double downSpeed, rightSpeed;
  private Level level;
  private double radius;
  private double size;
  private boolean expanded = false;
  private boolean disabled = false;
  private int chainLevel = 0;
  private double expanding = 0;
  private int age = 0;
  private int score;
  private double expandedSize;
  private int effect;
  private int effectTime = 0;
  private int effectState;
  
  
  public Ball(Level lev, int x, int y, int e) {
	//Create Ball
    level = lev;
    radius = size =lev.getBallSize();
    effect = e;
    if (x == -1) {
    	//Make me Random
      Random rnd = new Random();
      xPos = rnd.nextInt(lev.getWidth() - 2 * lev.getBallSize()) + lev.getBallSize();
      yPos = rnd.nextInt(lev.getHeight() - 2 * lev.getBallSize()) + lev.getBallSize();
      int direction = rnd.nextInt(360);
      downSpeed = Math.cos(Math.toRadians(direction)) * lev.getBallSpeed();
      rightSpeed = Math.sin(Math.toRadians(direction)) * lev.getBallSpeed();
      
      if (effect == 1) {
    	effectState = 0;
      } else if (effect == 2) {
    	 effectState = 0;
      }else {
    	colorRed = rnd.nextDouble();
    	colorGreen = rnd.nextDouble();
     	colorBlue =  rnd.nextDouble();
      }
      
    } else {
    	//This must be the starterBall
      xPos = x;
      yPos = y;
      colorRed = colorGreen = colorBlue = 0.588;
      this.startExpansion();
    }
    level.addBall(this);
  }
    

  public void startExpansion() {
	expandedSize = level.getExpandedBallSize() + size;
    expanded = true;
    level.addExpanded(this);
    if (effect == 1 && !level.isEffectDisable(0)) {
    	Random rnd = new Random();
    	score = - (rnd.nextInt(2000) + 3000); 
    	colorRed = 1;
    	colorGreen = 0;
    	colorBlue = 0;
    } else if (effect == 1 && level.isEffectDisable(0)) {
    	score = (int) (100 * Math.pow(chainLevel, 3));
    	colorRed = 1;
    	colorGreen = 0.27;
    	colorBlue = 0;
    } else if (effect == 2) {
    	score = (int) (150 * Math.pow(chainLevel, 3));
    	level.disableEffect(0, 3000);
    } else {    
    	score = (int) (100 * Math.pow(chainLevel, 3));
    }
    level.addScore(score);
    downSpeed = rightSpeed = 0;
    expanding = 1;
  }

  public void maintain(int delta) {
	if (effect == 1  && !level.isEffectDisable(0) && !expanded) {
		effectTime += delta;
		if (effectTime >= 200) {
			effectTime = 0;
			effectState = (effectState +1) % 2;
			switch (effectState) {
			case 0: colorRed = 1; colorGreen = 0; colorBlue = 0; break;
			case 1: colorRed = 1; colorGreen = 1; colorBlue = 1; break;
			}
		}
	} else if (effect == 1  && level.isEffectDisable(0) && !expanded) {
		effectTime += delta;
		if (effectTime >= 200) {
			effectTime = 0;
			effectState = (effectState +1) % 2;
			switch (effectState) {
			case 0: colorRed = 1; colorGreen = 0.27; colorBlue = 0; break;
			case 1: colorRed = 1; colorGreen = 1; colorBlue = 1; break;
			}
		}
	} else if (effect == 1 && score > 0 && expanded) {
		effectTime += delta;
		if (effectTime >= 200) {
			effectTime = 0;
			effectState = (effectState +1) % 2;
			switch (effectState) {
			case 0: colorRed = 1; colorGreen = 0.27; colorBlue = 0; break;
			case 1: colorRed = 1; colorGreen = 1; colorBlue = 1; break;
			}
		}
	} else if (effect == 1 && score < 0 && expanded) {
		effectTime += delta;
		if (effectTime >= 200) {
			effectTime = 0;
			effectState = (effectState +1) % 2;
			switch (effectState) {
			case 0: colorRed = 1; colorGreen = 0; colorBlue = 0; break;
			case 1: colorRed = 1; colorGreen = 1; colorBlue = 1; break;
			}
		}
	} else if (effect == 2) {
		effectTime += delta;
		if (effectTime >= 200) {
			effectTime = 0;
			effectState = (effectState +1) % 2;
			switch (effectState) {
			case 0: colorRed = 0.27; colorGreen = 0; colorBlue = 1; break;
			case 1: colorRed = 1; colorGreen = 1; colorBlue = 1; break;
			}
		}
	}
	
    if(this.expanding == 1) {
      expand(delta);
    } else if(this.expanding == -1) {
      shrink(delta);
    } else if(this.expanded == true) {
      age = age + delta;
      if (age > level.getMaxAge()) {
        expanding = -1;
      }
      render();
    } else if(this.disabled == false) {
      move(delta);
    }
  }

  private void renderScore() {
	if (score != 0 && expanded && expanding == 0) {
		UnicodeFont f = Game.getFont2();
		String s = (score > 0) ? "+ ".concat(String.valueOf(score)) : "- ".concat(String.valueOf(Math.abs(score)));
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		f.drawString((float) xPos - f.getWidth(s)/2,(float) yPos - f.getHeight(s)/2, s);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
	}
	
  }


private void shrink(int delta) {
    radius =  radius - (70 * (double)(delta)/1000);
    if (radius < 0) {
      radius = 0;
      expanding = 0;
      level.die(this);
      return;
    }
    render(); 
  }


  private void expand(int delta) {
    radius =  radius + (100 * (double)(delta)/1000);
    if (radius > expandedSize) {
      radius = expandedSize;
      expanding = 0;
    }
    render();
  }


  public void render() {  
    GL11.glColor3d(colorRed, colorGreen, colorBlue);
    level.drawMovingBall(xPos, yPos, radius);   
    renderScore();
  }
  
  public void move(int delta, boolean test) {
    xPos = xPos + (rightSpeed * delta/1000);
    yPos = yPos + (downSpeed * delta/1000);
     
    
    
    render();
    //Collison with wall?
    if (test) {
	    boolean notHit = level.testCollision(this);
	    
	    if (notHit) {
	      if (xPos <= level.getBallSize()) {
	        rightSpeed = -rightSpeed;
	        xPos = level.getBallSize();
	      }
	      if (xPos >= level.getWidth() - level.getBallSize()) {
	        rightSpeed = -rightSpeed;
	        xPos = level.getWidth() - level.getBallSize();
	      }
	      if (yPos <= level.getBallSize()) {
	        downSpeed = -downSpeed;
	        yPos = level.getBallSize();
	      }
	      if (yPos >= level.getHeight() - level.getBallSize()) {
	        downSpeed = -downSpeed;
	        yPos = level.getHeight() - level.getBallSize();
	      }
	    }
    }
  }
  
  public void move(int delta) {
	  move(delta, true);
  }
  
  public int getChainLevel() {
    return chainLevel;
  }


  public double getRadius() {
    return radius;
  }


  public boolean isExpanded() {
    return expanded;
  }


  public double getYPos() {
    return yPos;
  }


  public double getXPos() {
    return xPos;
  }


  public void setChainLevel(int i) {
    chainLevel = i;   
  }


	public double getDownSpeed() {
		return downSpeed;
	}
	
	
	public double getRightSpeed() {
		return rightSpeed;
	}
	
	
	public void setDownSpeed(double d) {
		downSpeed = d;
	}
	
	
	public void setRightSpeed(double d) {
		rightSpeed = d;
	}

}
