package com.keikebreau.proton_cruiser;

import java.util.Random;

public class Spawn {
	protected final Controller controller;
	protected static final Random r = new Random();
	
	private int tick;
	private static final int BASE_TICKS_PER_ELECTRON = 25;
	private static final int BASE_TICKS_PER_DUST = BASE_TICKS_PER_ELECTRON * 3;
	private int ticksPerElectron;
	private int ticksPerDust;

	public Spawn(Controller controller, GameScreen gameScreen) {
		this.controller = controller;
		tick = 0;
		ticksPerElectron = BASE_TICKS_PER_ELECTRON;
		ticksPerDust = BASE_TICKS_PER_DUST;
		spawnPlayer();
	}
	
	public void tick() {
		++tick;
		
		// Update spawn rates based on frame speed
		float frameSpeed = controller.getPlayer().getFrameSpeed();
		ticksPerElectron = (int) (BASE_TICKS_PER_ELECTRON - 20 * frameSpeed);
		ticksPerDust = ticksPerElectron * 3;

		if (tick % ticksPerElectron == 0) {
			spawnElectron();
		}
		if (tick % ticksPerDust == 0) {
			spawnDust();
		}
	}
	
	public void spawnPlayer() {
		Player p = new Player((float) GameScreen.WIDTH /2 - Player.SIZE, (float) GameScreen.HEIGHT /2 - Player.SIZE, ID.PLAYER, controller);
		controller.requestAdd(p);
	}

	public void spawnElectron() {
		controller.requestAdd(new ElectronEnemy(GameScreen.WIDTH - Enemy.SIZE, r.nextInt(GameScreen.HEIGHT), controller));
	}
	
	public void spawnDust() {
		controller.requestAdd(new DustEnemy(GameScreen.WIDTH - Enemy.SIZE, r.nextInt(GameScreen.HEIGHT), controller));
	}

}
