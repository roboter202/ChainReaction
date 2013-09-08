
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;





public class Level {
	
	private final int ballSize = 9;
	private final int starterBallSize = 30;
	private int xSize;
	private int ySize;
	private int numBalls;
	private int ballWinCount;
	private int totalBallsExpanded = -1;	
	private final int expandedLifeLength = 3000;
	private long score = 0;
	private StarterBall starterBall;
	private boolean starterBallPlaced = false;
	private ArrayList<Ball> movingBalls = new ArrayList<Ball>();
	private ArrayList<Ball> expandedBalls = new ArrayList<Ball>();
	//private long[] effectDisabledUntil;
	//private boolean[] effectDisabled;
	private Object[][] effectDisabled = {{false, 0L}, {false, 0L}};
	private BallDrawer ballMoving;
	private int levelId;

	

	
	public Level(int x, int y) {
		//Create The DL
		xSize = x;
		ySize = y;
		ballMoving = new BallDrawer();
		ballMoving.init();
	}

	public void createLevel(int levelNum) {
		//Reset Level
		movingBalls.clear();
		expandedBalls.clear();
		totalBallsExpanded = -1;
		score = 0;
		starterBallPlaced = false;
		levelId = levelNum;
		//Calculate Balls and Create them
		numBalls = 5*levelNum;
		if (numBalls > 75) {
			//numBalls = 75;
		}
		
		ballWinCount = (int) Math.ceil( (Math.sin((float)numBalls / (float)55)* (float)(numBalls -1)) );
		if (ballWinCount > numBalls) ballWinCount = numBalls;
		starterBall = new StarterBall(this);	
		
		int evilCnt = 0;
		int goodCnt = 0;
		if (levelNum > 10) {
			Random rnd = new Random();		
			evilCnt = rnd.nextInt((int)((numBalls + (levelNum * 0.2)) / 15));
			goodCnt = rnd.nextInt((int)((numBalls + (levelNum * 0.2)) / 15));
		}
		for (int i = 0; i < numBalls - evilCnt - goodCnt; i++) {
			new Ball(this, -1, -1, 0);
		}
		
		for (int i = 0; i < evilCnt; i++) {
			new Ball(this, -1, -1, 1);
		}
		
		for (int i = 0; i < goodCnt; i++) {
			new Ball(this, -1, -1, 2);
		}
	}

	public boolean isDone() {
		//Titel says it all
		return (ballWinCount <= totalBallsExpanded);
	}


	public boolean allBallsDead() {		
		//No balls left? -> Game unsolvable?
		return (expandedBalls.size() == 0);
	}


	public long getPoints() {
		//Serve Score
		return score;
	}

	public void updateAndRender(int delta) {
		//Update and Render starter Ball
		if (!starterBallPlaced) {
			starterBall.update();
			starterBall.render();
		}

		//Draw Balls		
		for(int i = 0; i <  movingBalls.size(); i++) {
			movingBalls.get(i).maintain(delta);
			
		}
			    
	}

	public int getWidth() {
		return xSize;
	}


	public int getHeight() {
		return ySize;
	}
	
	public boolean isStarterBallPlaced() {
		//Has the level started
		return starterBallPlaced;
		
	}

	public void placeStarterBall() {
		starterBallPlaced = true;
		starterBall.place();
	}
	
	public double getBallSpeed() {
		Random rnd = new Random();
		return rnd.nextDouble()*90 + 65;
	}
	
	public int getBallSize() {
		return ballSize;
	}
	
	public void drawMovingBall(double d1, double d2,double d3) {
		ballMoving.render(d1, d2, d3);
	}
	
	public int getBallsExpanded() {
		return totalBallsExpanded;
	}

	public void addExpanded(Ball ball) {
		totalBallsExpanded++;
		expandedBalls.add(ball);
	}
	
	public void addBall(Ball b) {
		movingBalls.add(b);
	}
	
	public void addScore(int i) {
		score += i;
	}


	public double getExpandedBallSize() {
		Random rnd = new Random();
		return (double) (rnd.nextInt(35) + 29) / ((levelId-1) * 0.02 + 1.1) + 6;
		
	}

	public int getMaxAge() {
		return expandedLifeLength;
	}



	public boolean testCollision(Ball ball) {
		boolean notHit = true;
		
		//Test collison with expanded Balls
		for(int i = 0; i <  expandedBalls.size(); i++) {
			Ball b = expandedBalls.get(i);

			if(notHit &&  b.isExpanded() && Math.sqrt(Math.pow(b.getXPos() - ball.getXPos(), 2) + Math.pow(b.getYPos() - ball.getYPos(), 2)) <= ball.getRadius() + b.getRadius()) {
				ball.setChainLevel(b.getChainLevel()+1);
				ball.startExpansion();
				notHit = false;
			}
			
		}
		
		//Colliding Ball to Ball
		/*for(int i = 0; i <  movingBalls.size(); i++) {
			Ball b = movingBalls.get(i);

			if(b != ball && Math.sqrt(Math.pow(b.getXPos() - ball.getXPos(), 2) + Math.pow(b.getYPos() - ball.getYPos(), 2)) <= ball.getRadius() + b.getRadius()) {
				ball.setDownSpeed(-ball.getDownSpeed());
				ball.setRightSpeed(-ball.getRightSpeed());
				
				b.setDownSpeed(-b.getDownSpeed());
				b.setRightSpeed(-b.getRightSpeed());
				ball.move(10, false);
			}
			
		}*/
		return notHit;
	}


	public void die(Ball ball) {
		//Kill me!
		expandedBalls.remove(ball);
		movingBalls.remove(ball);		
	}


	public int getBallsToExpand() {
		return ballWinCount;
	}


	public int getStarterBallSize() {
		return starterBallSize;
	}

	public boolean isEffectDisable(int i) {
		if (System.currentTimeMillis() >= (long) effectDisabled[i][1]) {
			effectDisabled[i][0] = false;
		}
		return (boolean)effectDisabled[i][0];
	}

	public void disableEffect(int i, int j) {
		effectDisabled[i][0] = true;
		effectDisabled[i][1] = System.currentTimeMillis() + j;
	}
	

}
