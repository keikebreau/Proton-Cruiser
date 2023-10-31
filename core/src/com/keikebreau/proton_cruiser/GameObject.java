package com.keikebreau.proton_cruiser;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public abstract class GameObject {
	protected final Rectangle bounds;
	protected final ID id;
	protected float velX, velY;
	protected float charge;
	protected Color color;
	protected final Controller controller;
	protected final Random r;
	
	/** Construct a GameObject, possibly with charge. */
	public GameObject(float x, float y, int width, int height, float charge, Color color, ID id, Controller controller) {
		bounds = new Rectangle(x, y, width, height);
		this.charge = charge;
		this.id = id;
		this.controller = controller;
		this.color = color;
		r = new Random();
	}
	
	/** Construct a GameObject without charge. */
	public GameObject(float x, float y, int width, int height, Color color, ID id, Controller controller) {
		this(x, y, width, height, 0.0f, color, id, controller);
	}
	
	public abstract void tick();
	public abstract Rectangle getBounds();

    public float getX() {
		return bounds.getX();
	}
	
	public float getY() {
		return bounds.getY();
	}

    public ID getId() {
		return id;
	}
	
	public float getVelX() {
		return velX;
	}
	
	public float getVelY() {
		return velY;
	}
	
	public float getWidth() {
		return bounds.getWidth();
	}
	
	public float getHeight() {
		return bounds.getHeight();
	}

    public float getCharge() {
		return charge;
	}
}
