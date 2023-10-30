package com.keikebreau.proton_cruiser;

import com.badlogic.gdx.graphics.Color;

public class DustEnemy extends Enemy {
	private enum Charge {
		POSITIVE (0.001f),
		NEGATIVE (-0.001f);
		
		float charge;
		
		Charge(float charge) {
			this.charge = charge;
		}
		
		float getCharge() {
			return charge;
		}
		
		Color getColor() {
			if (charge < 0) {
				return Color.BLUE;
			} else {
				return Color.ORANGE;
			}
		}
	}
	private static float BASE_VEL = -3.0f;
	public DustEnemy(int x, int y, Controller controller) {
		// Charge and color will be set later in this constructor.
		super(x, y, 0.0f, null, ID.DUST, controller);
		bounds.width *= 3;
		bounds.height *= 3;
		velX = BASE_VEL * controller.getPlayer().getFrameSpeed();
		Charge[] charges = Charge.values();
		int chargeIndex = r.nextInt(charges.length);
		charge = charges[chargeIndex].getCharge();
		color = charges[chargeIndex].getColor();
	}
	
	public void tick() {
		super.tick();
		Player p = controller.getPlayer();
		float speedLimit = BASE_VEL * p.getFrameSpeed();
		if (p.isSlowingDown()) {
			velX = Math.max(velX, speedLimit);
		} else if (p.isSpeedingUp()) {
			velX = Math.min(velX, speedLimit);
		}
	}
}
