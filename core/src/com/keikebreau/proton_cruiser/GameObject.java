package com.keikebreau.proton_cruiser;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

public abstract class GameObject {
	protected Rectangle bounds;
	protected ID id;
	protected float velX, velY;
	protected float accelX, accelY;
	protected float charge;
	protected Color color;
	protected Controller controller;
	protected Random r;
	
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
	
	public void setX(float x) {
		bounds.setX(x);
	}
	
	public void setY(float y) {
		bounds.setY(y);
	}
	
	public float getX() {
		return bounds.getX();
	}
	
	public float getY() {
		return bounds.getY();
	}
	
	public float getCenterX() {
		Vector2 center = new Vector2();
		bounds.getCenter(center);
		return center.x;
	}
	
	public float getCenterY() {
		Vector2 center = new Vector2();
		bounds.getCenter(center);
		return center.y;
	}
	
	public void setId(ID id) {
		this.id = id;
	}
	
	public ID getId() {
		return id;
	}
	
	public void addAccelX(float accelX) {
		this.accelX += accelX;
	}
	
	public void addAccelY(float accelY) {
		this.accelY += accelY;
	}
	
	public float getVelX() {
		return velX;
	}
	
	public float getVelY() {
		return velY;
	}
	
	public float getAccelX() {
		return accelX;
	}
	
	public float getAccelY() {
		return accelY;
	}
	
	public float getWidth() {
		return bounds.getWidth();
	}
	
	public float getHeight() {
		return bounds.getHeight();
	}
	
	public Color getColor() {
		return color;
	}
	
	public float getCharge() {
		return charge;
	}
}
