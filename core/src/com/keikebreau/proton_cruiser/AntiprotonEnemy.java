package com.keikebreau.proton_cruiser;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;

public class AntiprotonEnemy extends GameObject {
	public static final int WIDTH = Game.WIDTH / 160;
	public static final int HEIGHT = WIDTH;
	private static final Color COLOR = Color.RED;
	private static float CHARGE = 0.001f;
	private float baseVelX;

	public AntiprotonEnemy(int x, int y, Controller controller) {
		super(x, y, WIDTH, HEIGHT, CHARGE, COLOR, ID.ANTIPROTON, controller);
		// Bullets always move down and move to the side randomly.
		velX = r.nextInt(10) - 5;
		baseVelX = velX;
		velY = 5;
		// Play laser sound at half volume with a randomized pitch close to the
		// original.
		//AudioPlayer.getSound("laser").play(1.1f - r.nextFloat() / 5, 0.15f);
	}

	@Override
	public void tick() {
		velX += accelX;
		velY += accelY;
		bounds.x += velX;
		bounds.y += velY;
		Player p = controller.getPlayer();
		float speedLimit = baseVelX * p.getFrameSpeed();
		if (p.isSlowingDown()) {
			velX = Math.max(velX, speedLimit);
		} else if (p.isSpeedingUp()) {
			velX = Math.min(velX, speedLimit);
		}
	}
	
	@Override
	public Rectangle getBounds() {
		return bounds;
	}
}
