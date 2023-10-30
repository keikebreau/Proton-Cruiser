package com.keikebreau.proton_cruiser;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;

/** All enemies have these basic fields and methods in common. */
public abstract class Enemy extends GameObject {
	public static final int SIZE = Game.WIDTH / 80;

	/** Construct an Enemy, possibly with charge */
	public Enemy(float x, float y, float charge, Color color, ID id, Controller controller) {
		super(x, y, SIZE, SIZE, charge, color, id, controller);
	}
	
	/** Construct an Enemy without charge */
	public Enemy(float x, float y, Color color, ID id, Controller controller) {
		super(x, y, SIZE, SIZE, 0.0f, color, id, controller);
	}

	@Override
	public void tick() {
		velX += accelX;
		velY += accelY;
		bounds.x += velX;
		bounds.y += velY;
	}
	
	@Override
	public Rectangle getBounds() {
		return bounds;
	}
}