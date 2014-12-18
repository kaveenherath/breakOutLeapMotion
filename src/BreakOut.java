/**
 * Kaveen Herath 
 * Breakout
 */

import acm.graphics.*;

import java.awt.event.MouseEvent;
import acm.program.*;
import acm.util.*;
import java.awt.*;
import java.awt.event.*;
import com.leapmotion.leap.*;

class GTitle extends GLabel {

	private int SIZE;

	public GTitle(String name, double X, double Y) {
		super(name, X, Y);
	}

	public void setSize(int number) {
		Font font1 = new Font("SansSerif", Font.PLAIN, number);
		setFont(font1);
	}

}

public class BreakOut extends GraphicsProgram {

	public static final int APPLICATION_WIDTH = 1200;
	public static final int APPLICATION_HEIGHT = 600;
	private static final int HEIGHT = APPLICATION_HEIGHT;
	private static final int PADDLE_WIDTH = 150;
	private static final int PADDLE_HEIGHT = 10;
	private static final int BRICK_SEP = 8;
	private static final int BRICK_WIDTH = 100;
	private static final int BRICK_HEIGHT = 10;
	private static final int BALL_RADIUS = 15;
	private GRect rect = new GRect(0, 600, PADDLE_WIDTH, PADDLE_HEIGHT);
	private GOval ball = new GOval(300, 300, BALL_RADIUS, BALL_RADIUS);
	private GLabel title = new GLabel("BREAKOUT", 550, 20);
	private GLabel time = new GLabel("You Have 60 sec, press P to pause", 550,
			400);
	private GLabel click = new GLabel("Click to Start", 550, 500);
	private GLabel color = new GLabel(
			"Orange- 5, Cyan- 2, Green- 3, Pink-1, Red-4", 550, 430);
	private RandomGenerator rgen = RandomGenerator.getInstance();
	private double vx = rgen.nextDouble(-5.0, 8.0);
	private double vy = rgen.nextDouble(1.0, 4.0);
	private static int score = 0;
	private Font font = new Font("SansSerif", Font.PLAIN, 30);
	private Font font1 = new Font("SansSerif", Font.PLAIN, 10);
	private GLabel Score_label = new GLabel("Your score is " + score, 550, 400);
	private static int pause = 0;
	public static int SIZE = 125;
	public static String NAME = "BreakOut";
	public double X = 300;
	public double Y = 600;
	public GTitle TITLE = new GTitle(NAME, X, Y);
	public CustomListener L = new CustomListener();
	public Controller c = new Controller();

	public void run() {
		c.addListener(L);
		addMouseListeners();
		addKeyListeners();

		titlePage();
		// pause(2000);
		for (int x = 0; x < 3; x++) {
			setSize(APPLICATION_WIDTH, APPLICATION_HEIGHT);
			startBreakOut();
			time.setFont(font);
			time.setColor(Color.YELLOW);
			add(time);
			color.setFont(font1);
			color.setColor(Color.YELLOW);
			add(color);
			click.setColor(Color.YELLOW);
			add(click);
			click.setFont(font);
			pause(2000);
			// waitForClick();
			remove(time);
			remove(color);
			// waitForClick();
			remove(click);
			pause(2000);
			// System.out.println("new game");
			moveBall();

			// System.out.println("game ended");

			removeAll();

		}

		GLabel end = new GLabel("Game Over", 550, 200);
		end.setFont(font);
		end.setColor(Color.WHITE);
		add(end);
		c.removeListener(L);

	}

	public void titlePage() {
		TITLE.setColor(Color.YELLOW);
		add(TITLE);
		TITLE.setSize(SIZE);
		// waitForClick();

		while (X > 0 && Y > 0) {
			TITLE.setSize(SIZE--);
			X = TITLE.getX() + 2.5;
			Y = Y - 5;
			TITLE.setLocation(X, Y);
			pause(15);
		}
	}

	public void moveBall() {

		long start = System.currentTimeMillis();
		long end = start + 60 * 1000;
		vx = rgen.nextDouble(-5.0, 8.0);
		rgen.nextDouble(1.0, 4.0);
		boolean TRUE = true;
		while (TRUE) {
			while (pause == 0) {
				remove(Score_label);

				double bx = ball.getX();
				double by = ball.getY();
				if (bx < 0 || bx > APPLICATION_WIDTH)
					vx = -vx;
				if (by < 0) {
					vy = -vy;

				}

				if (getCollidingObject() == rect) {
					vy = -vy;

				}

				if (getCollidingObject() instanceof GRect
						&& getCollidingObject() != rect) {
					// "Orange- 5, Cyan- 2, Green- 3, Pink-1, Red-4"
					GObject block = getCollidingObject();
					if (block.getColor().equals(Color.orange))
						score += 5;
					if (block.getColor().equals(Color.cyan))
						score += 2;
					if (block.getColor().equals(Color.green))
						score += 3;
					if (block.getColor().equals(Color.pink))
						score += 1;
					if (block.getColor().equals(Color.red))
						score += 4;

					remove(getCollidingObject());
					vy = -vy;

				}
				ball.move(vx, vy);
				Score_label = new GLabel("Your score is " + score, 550, 400);
				Score_label.setFont(font);
				Score_label.setColor(Color.WHITE);
				add(Score_label);
				if (ball.getY() + 2 > rect.getY()) {
					TRUE = false;
					break;
				}
				pause(12);
			}

		}
	}

	private GObject getCollidingObject() {
		GObject collider;
		collider = getElementAt(ball.getX(), ball.getY());
		if (collider != null)
			return collider;
		else {
			collider = getElementAt(ball.getX() + 2 * BALL_RADIUS, ball.getY());
			if (collider != null)
				return collider;
			else {
				collider = getElementAt(ball.getX(), ball.getY() + 2
						* BALL_RADIUS);
				if (collider != null)
					return collider;
				else {
					collider = getElementAt(ball.getX() + 2 * BALL_RADIUS,
							ball.getY() + 2 * BALL_RADIUS);
					if (collider != null)
						return collider;
				}

			}
		}
		return null;

	}

	public void mouseMoved(MouseEvent mouse) {
		if (mouse.getX() > 0 && mouse.getX() < 1300)

			rect.setLocation(mouse.getX(), HEIGHT - 20);

	}

	public void keyPressed(KeyEvent key) {

		if (key.getKeyCode() == KeyEvent.VK_P) {
			if (pause == 0)
				pause = 1;
			else
				pause = 0;
		}

	}

	private void startBreakOut() {
		title.setColor(Color.GREEN);
		add(title);
		makePaddle();
		makeBricks();
		setBackground(Color.BLACK);

		makeBall();
	}

	private void makeBall() {
		ball.setFilled(true);
		ball.setColor(Color.WHITE);
		ball.setFillColor(Color.WHITE);
		add(ball, 300, 300);
	}

	private void makePaddle() {

		rect.setFilled(true);
		rect.setColor(Color.blue);
		rect.setFillColor(Color.blue);
		add(rect);
	}

	private void makeBricks() {
		int x = 50;
		int y = 280;

		for (int index = 0; index < 15; index++) {
			for (int index1 = 0; index1 < 10; index1++) {
				GRect one = new GRect(x, y, BRICK_WIDTH, BRICK_HEIGHT);
				int random = (int) (Math.random() * 5);
				one.setFilled(true);
				switch (random) {
				case 0:
					one.setFillColor(Color.cyan);
					one.setColor(Color.cyan);
					break;
				case 1:
					one.setFillColor(Color.green);
					one.setColor(Color.green);
					break;
				case 2:
					one.setFillColor(Color.pink);
					one.setColor(Color.pink);
					break;
				case 3:
					one.setFillColor(Color.red);
					one.setColor(Color.red);
					break;
				case 4:
					one.setFillColor(Color.orange);
					one.setColor(Color.orange);
					break;
				default:
					one.setFillColor(Color.cyan);
					one.setColor(Color.cyan);
					break;
				}

				add(one);
				x = x + BRICK_SEP + BRICK_WIDTH;
			}
			x = 50;
			y = y - BRICK_SEP - BRICK_HEIGHT;
		}
	}

}
