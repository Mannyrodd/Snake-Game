import java.awt.*;
import java.awt.event.*;
import java.util.Random;

import javax.swing.*;

public class GamePanel extends JPanel implements ActionListener {

	static final int SCREEN_WIDTH = 600;
	static final int SCREEN_HEIGHT = 600;

//	How big the objects in this game are going to be.
	static final int UNIT_SIZE = 25;

//	How many objects I can fit on the screen.
	static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;

//	Delay for timer.
	static final int DELAY = 75;

//	Holds all of the coordinate of the body parts for the snake.
	final int x[] = new int[GAME_UNITS];
	final int y[] = new int[GAME_UNITS];

//	Amount of body parts for the snake.
	int bodyParts = 6;

	int applesEaten;
	int appleX;
	int appleY;

//	Snake begins in the right direction.
	char direction = 'R';

	boolean running = false;
	
	Timer timer;

	Random random;

	GamePanel(){
		
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startGame();
		
	}

	public void startGame() {
		newApple();
		running = true;
		
//		Dictates how fast the game is running. this is from ActionListener interface.
		timer = new Timer(DELAY, this);
		timer.start();

	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);

	}

	public void draw(Graphics g) {

		if(running) {
			
//			Remove Grid lines.
			
			/*
			for(int i = 0; i < SCREEN_HEIGHT/UNIT_SIZE; i++) {
//			Draw grid to visualize where components are going to be better.
			g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
			g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
			}
			*/
			
			g.setColor(Color.red);
			g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
		
			for(int i = 0; i < bodyParts; i++) {
//				Draw the head of the snake.
				if(i == 0) {
					g.setColor(Color.green);
					g.fillRect(x[i], y[i], UNIT_SIZE,UNIT_SIZE);
				}
			
//				Draw the body of the snake.
				else {
					g.setColor(new Color(45, 180, 0));
					g.fillRect(x[i], y[i], UNIT_SIZE,UNIT_SIZE);
				}
			}
			
			g.setColor(Color.red);
			g.setFont(new Font("Ink Free", Font.BOLD, 75));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten))/2, g.getFont().getSize());
		}
		
		else {
			gameOver(g);
		}
		
		
	}
	
	public void newApple() {
		
		appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
		appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
	}

	public void move() {
		
		for(int i = bodyParts; i > 0; i--) {
//			Shifting all the coordinates by 1.
			x[i] = x[i-1];
			y[i] = y[i-1];
		}
		
		switch(direction) {
		
		case 'U':
			y[0] = y[0] - UNIT_SIZE;
			break;
		
		case 'D':
			y[0] = y[0] + UNIT_SIZE;
			break;
			
		case 'L':
			x[0] = x[0] - UNIT_SIZE;
			break;
			
		case 'R':
			x[0] = x[0] + UNIT_SIZE;
			break;
			
		}

	}

	public void checkApple() {
//		Every time a snake eats an apple, it grows.
		if((x[0] == appleX) && (y[0] == appleY)) {
			bodyParts++;
			applesEaten++;
			newApple();
		}

	}

	public void checkCollisions() {
		
//		Checks if head collides with body.
		for(int i = bodyParts; i > 0; i--) {
			if((x[0] == x[i]) && (y[0] == y[i])) {
				running = false;
			}
		}
		
//		Checks if head touches left corner.
		if(x[0] < 0) {
			running = false;
		}
		
//		Checks if head touches right corner.
		if(x[0] > SCREEN_WIDTH) {
			running = false;
		}
		
//		Checks if head touches top border.
		if(y[0] < 0) {
			running = false;
		}
		
//		Checks if head touches bottom border.
		if(y[0] > SCREEN_HEIGHT) {
			running = false;
		}
		
		if(!running) {
			timer.stop();
		}

	}

	public void gameOver(Graphics g) {

//		Score
		g.setColor(Color.red);
		g.setFont(new Font("Ink Free", Font.BOLD, 75));
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score: " + applesEaten))/2, g.getFont().getSize());
		
//		Game Over text
		g.setColor(Color.red);
		g.setFont(new Font("Ink Free", Font.BOLD, 75));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(running) {
			move();
			
//			Check if run into apple.
			checkApple();
			
//			Check if run into borders on the screen.
			checkCollisions();
		}
		
		repaint();

	}

	public class MyKeyAdapter extends KeyAdapter {
		
//		Controls for the snake.
		@Override
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				if(direction != 'R') {
					direction = 'L';
				}
				
				break;
			}
			
			switch(e.getKeyCode()) {
			case KeyEvent.VK_RIGHT:
				if(direction != 'L') {
					direction = 'R';
				}
				
				break;
			}
			
			switch(e.getKeyCode()) {
			case KeyEvent.VK_UP:
				if(direction != 'D') {
					direction = 'U';
				}
				
				break;
			}
			
			switch(e.getKeyCode()) {
			case KeyEvent.VK_DOWN:
				if(direction != 'U') {
					direction = 'D';
				}
				
				break;
			}
			

		}
	}

}
