package com.keikebreau.proton_cruiser;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;

import static com.badlogic.gdx.math.MathUtils.clamp;

public class Player extends GameObject {
	
	/** Radius of the player's circle. */
	public static int SIZE = Game.WIDTH / 40;
	
	/** Color of the player's box. */
	private static final Color COLOR = Color.WHITE;
	
	/** Player's speed. */
	private float frameSpeed;
	
	/** Light speed acceleration/deceleration per tick (when activated) */
	private static final float FRAME_SPEED_INC = 0.0001f;
	//private static final float FRAME_SPEED_INC = 0.01f;
	
	/** Light speed acceleration/deceleration per tick (actual) */
	private float frameAccelFactor;
	
	/** Player's minimum speed. */
	public static final float MIN_FRAME_SPEED = 0.01f;
	
	/** Player's top speed. */
	public static final float MAX_FRAME_SPEED = 0.999f;
	
	/** Player's maximum possible HP. */
	public static int MAX_HP = 100;
	
	/** The player's remaining health points. */
	private int hp;
	
	public Player(float x, float y, ID id, Controller controller) {
		super(x, y, SIZE, SIZE, COLOR, id, controller);
		hp = MAX_HP;
		frameSpeed = MIN_FRAME_SPEED;
		frameAccelFactor = 0.0f;
	}

	@Override
	public void tick() {
		bounds.x += velX;
		bounds.y += velY;
		frameSpeed = clamp(frameSpeed + frameAccelFactor, MIN_FRAME_SPEED, MAX_FRAME_SPEED);
		hp = clamp(hp + 1, 0, MAX_HP);
		bounds.x = clamp(bounds.x, 0, Game.WIDTH - SIZE);
		bounds.y = clamp(bounds.y, 0, Game.HEIGHT - SIZE);
		controller.handlePlayerInteractions();
	}
	
	@Override
	public Rectangle getBounds() {
		return bounds;
	}
	
	public int getHP() {
		return hp;
	}
	
	public void loseHP(int removedHP) {
		hp = clamp(hp - removedHP, 0, Player.MAX_HP);
	}
	
	public float getFrameSpeed() {
		return frameSpeed;
	}

	public void speedUp() {
		frameAccelFactor = FRAME_SPEED_INC;
	}
	
	public boolean isSpeedingUp() {
		return frameAccelFactor == FRAME_SPEED_INC;
	}
	
	public void slowDown() {
		frameAccelFactor = -FRAME_SPEED_INC;
	}
	
	public boolean isSlowingDown() {
		return frameAccelFactor == -FRAME_SPEED_INC;
	}
	
	public void cruise() {
		frameAccelFactor = 0.0f;
	}
	
	public boolean isCruising() {
		return frameAccelFactor == 0.0f;
	}

	public void loseFrameSpeed() {
		frameSpeed = clamp(frameSpeed - FRAME_SPEED_INC * 2, MIN_FRAME_SPEED, MAX_FRAME_SPEED);
	}
}
