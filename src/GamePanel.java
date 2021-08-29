import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;


public class GamePanel extends JPanel implements ActionListener{
	
	static final int SCREEN_WIDTH=600,SCREEN_HEIGHT=600;	//Width and Height of the game Screen
	static final int UNIT_SIZE=25;		
	static final int GAME_UNITS=(SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
	static final int DELAY=100;			//Delay for controlling the game speed, for increasing game speed decrease the delay
	final int x[]=new int[GAME_UNITS];	//Array for storing the X-coordinates of the body parts of the Snake
	final int y[]=new int[GAME_UNITS];	//Array for storing the Y-coordinates of the body parts of the Snake
	int bodyLength=3;					//Length of the Snake
	int applesEaten=0;					//Number of Apples Eaten by the Snake
	int appleX,appleY;					//Coordinates of the Apple 
	char direction='R';					//Snake's current direction of movement
	boolean running=false;				//It represents whether game is Over or In progress
	boolean paused=false;				//It represents whether game is paused
	Timer timer;						
	Random random;
	
	
	GamePanel(){
		random=new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));	//Setting the size of the Game Screen
		this.setBackground(Color.black);									//Setting the background color of the game screen;
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		StartGame();
	}
	
	
	public void StartGame() {
		newApple();
		running=true;
		timer=new Timer(DELAY,this);
		timer.start();
		
	}
	
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}
	
	
	public void draw(Graphics g) {
		
		if(running) {
			
			//To draw the Apple on the Screen
			g.setColor(Color.red);
			g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
			
			//To draw the Snake on the Screen
			for(int i=0;i<bodyLength;i++) {
				
				//To draw the head of the Snake
				if(i==0){
					g.setColor(Color.green);
					g.fillOval(x[i],y[i],UNIT_SIZE,UNIT_SIZE);
					g.setColor(Color.black);
					g.fillOval(x[i]+8, y[i]+10, 6, 6);
					g.fillOval(x[i]+18, y[i]+10, 6, 6);
				}
				
				//To draw the body of the Snake
				else {
					g.setColor(Color.green);
					g.fillOval(x[i],y[i],UNIT_SIZE,UNIT_SIZE);
				}	
			}
			
			if(paused) {
				g.setColor(Color.red);
				g.setFont(new Font("TimesRoman",Font.PLAIN,50));
				FontMetrics metrics=getFontMetrics(g.getFont());
				g.drawString("GAME PAUSED", (SCREEN_WIDTH-metrics.stringWidth("GAME PAUSED"))/2, SCREEN_HEIGHT/2);
			}
			
			//To display the current Score on the screen
			g.setColor(Color.red);
			g.setFont(new Font("Dialog",Font.PLAIN,18));
			FontMetrics metrics=getFontMetrics(g.getFont());
			g.drawString("Score : "+applesEaten, (SCREEN_WIDTH-metrics.stringWidth("Score : "+applesEaten))/2, g.getFont().getSize());
			
		}
		else {
			gameOver(g);
		}
		
	}
	
	
	public void newApple(){
		//New position of the Apple on the screen
		appleX=random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
		appleY=random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
	}
	
	
	public void move(){
		
		for(int i=bodyLength;i>0;i--) {
			x[i]=x[i-1];
			y[i]=y[i-1];
		}
		
		switch(direction) {
		
		case 'U' :
			y[0]=y[0]-UNIT_SIZE;
			break;
			
		case 'D' :
			y[0]=y[0]+UNIT_SIZE;
			break;
			
		case 'R' :
			x[0]=x[0]+UNIT_SIZE;
			break;
			
		case 'L' :
			x[0]=x[0]-UNIT_SIZE;
			break;
		}
		
	}
	
	
	public void checkApple(){
		//To check if the Apple is eaten by the Snake at this position 
		if((x[0]==appleX)&&(y[0]==appleY)) {
			bodyLength++;
			applesEaten++;
			newApple();
		}
	}
	
	
	public void checkCollisions(){
		
		//checks the collision of Head of the Snake with its Body
		for(int i=1;i<bodyLength;i++) {
			if((x[0]==x[i]) && (y[0]==y[i]))
				running=false;
		}
		//checks the collision of Head of the Snake with the Boundaries
		if((x[0]<0)||(y[0]<0)||(x[0]>=SCREEN_WIDTH)||(y[0]>=SCREEN_HEIGHT)) {
			running=false;
		}
		if(!running) {
			timer.stop();
		}
		
	}
	
	
	public void gameOver(Graphics g){
		//To display GAME OVER on the Screen when game is over
		g.setColor(Color.red);
		g.setFont(new Font("TimesRoman",Font.PLAIN,75));
		FontMetrics metrics=getFontMetrics(g.getFont());
		g.drawString("GAME OVER", (SCREEN_WIDTH-metrics.stringWidth("GAME OVER"))/2, SCREEN_HEIGHT/2);
		
		//To display the final Score on the Screen when game is over
		g.setColor(Color.green);
		g.setFont(new Font("Helvetica",Font.PLAIN,36));
		FontMetrics metrics1=getFontMetrics(g.getFont());
		g.drawString("Score : "+applesEaten, (SCREEN_WIDTH-metrics1.stringWidth("Score : "+applesEaten))/2, 2*SCREEN_HEIGHT/3);
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if((running) &&(!paused)) {
			move();
			checkApple();
			checkCollisions();
		}
		repaint();
	}

	
	public class MyKeyAdapter extends KeyAdapter{

		@Override
		public void keyPressed(KeyEvent e) {
			
			switch(e.getKeyCode()) {
			
			//if UP Arrow key if pressed
			case KeyEvent.VK_UP:
				if((direction!='D')&&(!paused))
					direction='U';
				break;
			
			//if DOWN Arrow key if pressed
			case KeyEvent.VK_DOWN:
				if((direction!='U')&&(!paused))
					direction='D';
				break;
				
			//if LEFT Arrow key if pressed	
			case KeyEvent.VK_LEFT:
				if((direction!='R')&&(!paused))
					direction='L';
				break;
				
			//if RIGHT Arrow key if pressed		
			case KeyEvent.VK_RIGHT:
				if((direction!='L')&&(!paused))
					direction='R';
				break;
			
			//If ENTER key is pressed,then game will be paused or resumed
			case KeyEvent.VK_ENTER:
				if(!paused) {
					paused=true;
				}
				else {
					paused=false;
				}
				break;
			}
		}
		
	}
	
}
