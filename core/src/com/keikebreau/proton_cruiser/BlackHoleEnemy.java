package com.keikebreau.proton_cruiser;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;

public class BlackHoleEnemy extends GameObject {
	public static final int WIDTH = Game.WIDTH * 2;
	public static final int HEIGHT = WIDTH;
	private static final Color COLOR = Color.BLACK;
	
	/** Timer for different boss events. */
	private int timer = 0;

	public BlackHoleEnemy(int x, int y, Controller controller) {
		super(x, y, WIDTH, HEIGHT, COLOR, ID.BLACK_HOLE, controller);
		velX = 0;
		velY = 2;
	}

	@Override
	public void tick() {
		velY += accelY;
		bounds.x += velX;
		bounds.y += velY;
		++timer;
		// Move down for 100 ticks.
		/** Number of ticks before stopping initial vertical movement. */
		int MOVE_IN = 60;
		if (timer == MOVE_IN) {
			velY = 0;
		}
		float frameSpeed = controller.getPlayer().getFrameSpeed();
		if (frameSpeed < 0.9f) {
			accelY = -0.1f;
		}
		
		// 10% chance per tick to spawn a bullet.
		int spawn = r.nextInt(10);
		if (spawn == 0) {
			// Spawn a bullet from the center of the black hole.
			controller.requestAdd(new AntiprotonEnemy((int)(bounds.x + WIDTH / 2), (int)(bounds.y + HEIGHT / 2), controller));
		}
	}
	
	@Override
	public Rectangle getBounds() {
		return new Rectangle(bounds.x, bounds.y, WIDTH, HEIGHT);
	}
}
