package com.keikebreau.proton_cruiser;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;

public class MoleculeEnemy extends Enemy {
	/** Amount of [R, G, B] in the color of the particle, each from 0.0f to 1.0f. */
	private float[] rgb;
	/** Whether to increase or decrease each color value this tick.
	 * true when increasing, false when decreasing.
	 */
	private boolean[] rgbUp;
	/** Amount to increase or decrease each color value per tick. */
	private static final float COLOR_VELOCITY = 0.05f;
	
	private static float BASE_VEL = -5.0f;

	public MoleculeEnemy(int x, int y, Controller controller) {
		super(x, y, null, ID.MOLECULE, controller);
		bounds.width *= 2;
		bounds.height *= 2;
		// Initialize color values and color.
		rgb = new float[3];
		rgbUp = new boolean[3];
		for (int i = 0; i < 3; ++i) {
			rgb[i] = r.nextFloat();
			rgbUp[i] = r.nextBoolean();
		}
		color = new Color(rgb[0], rgb[1], rgb[2], 1.0f);
		velX = BASE_VEL * controller.getPlayer().getFrameSpeed();
	}

	@Override
	public void tick() {
		super.tick();
		Player p = controller.getPlayer();
		float speedLimit = BASE_VEL * p.getFrameSpeed();
		if (p.isSlowingDown()) {
			velX = Math.max(velX, speedLimit);
		} else if (p.isSpeedingUp()) {
			velX = Math.min(velX, speedLimit);
		}
		// Cycle through colors.
		for (int i = 0; i < 3; ++i) {
			// Increase or decrease values.
			if (rgbUp[i]) {
				rgb[i] = Game.clamp(rgb[i] + COLOR_VELOCITY, 0.0f, 1.0f);
			} else {
				rgb[i] = Game.clamp(rgb[i] - COLOR_VELOCITY, 0.0f, 1.0f);
			}
			// Set whether to increase values next tick.
			if (rgb[i] <= 0.0f) {
				rgbUp[i] = true;
			} else if (rgb[i] >= 1.0f) {
				rgbUp[i] = false;
			}
		}
		color = new Color(rgb[0], rgb[1], rgb[2], 1.0f);
	}
}
