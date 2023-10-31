package com.keikebreau.proton_cruiser;

import com.badlogic.gdx.graphics.Color;

public class ElectronEnemy extends Enemy {
	private static final float CHARGE = 0.0001f;
	private static final float BASE_VEL = -10.0f;
	public ElectronEnemy(int x, int y, Controller controller) {
		super(x, y, CHARGE, Color.YELLOW, ID.ELECTRON, controller);
		velX = BASE_VEL * controller.getPlayer().getFrameSpeed();
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
