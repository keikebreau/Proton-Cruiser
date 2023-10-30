package com.keikebreau.proton_cruiser;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import java.util.Random;

public class BackgroundStar extends GameObject {
	/** Parallax scrolling will be used for background stars.
	 * The front layer has the brightest, largest and fastest moving stars, and
	 * stars are increasingly dark, small, and slow as the layers progress. */
	private enum Layer {
		FRONT (new Color(Color.toIntBits(180, 180, 180, 255)), Game.WIDTH / 120, -50),
		MID (new Color(Color.toIntBits(140, 140, 140, 255)), Game.WIDTH / 170, -30),
		BACK (new Color(Color.toIntBits(50, 50, 50, 255)), Game.WIDTH / 240, -10);
		
		final Color color;
		final int size;
		final int velX;
		
		private Layer(Color color, int size, int velX) {
			this.color = color;
			this.size = size;
			this.velX = velX;
		}
		
		public Color getColor() {
			return color;
		}
		
		public int getSize() {
			return size;
		}
		
		public int getVelX() {
			return velX;
		}
	}
	
	private final Random r;
	private final Layer layer;

	public BackgroundStar(int x, int y, ID id, Controller controller) {
		// Width, height, and color will come from the layer.
		super(x, y, 0, 0, null, id, controller);
		r = new Random();
		Layer[] layers = Layer.values();
		int layerIndex = r.nextInt(layers.length);
		layer = layers[layerIndex];
		bounds.width = layer.getSize();
		bounds.height = layer.getSize();
		color = layer.getColor();
		velX = layer.getVelX();
	}

	@Override
	public void tick() {
		Player p = controller.getPlayer();
		if (p != null) {
			velX = layer.getVelX() * p.getFrameSpeed();
		} else {
			velX = layer.getVelX() * .01f;
		}
		bounds.x += velX;
		//width = layer.getSize();
		//height = layer.getSize();
		// Recycle off-screen background stars.
		if (bounds.x <= 0) {
			bounds.x = Game.WIDTH + bounds.width;
			bounds.y = r.nextFloat() * Game.HEIGHT;
		}
	}
	
	@Override
	public Rectangle getBounds() {
		return null;
	}
}
