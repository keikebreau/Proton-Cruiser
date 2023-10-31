package com.keikebreau.proton_cruiser;

import java.util.Random;

public class Spawn {
	protected Controller controller;
	private Game game;
	protected static Random r = new Random();
	
	private int tick;
	private static int BASE_TICKS_PER_ELECTRON = 25;
	private static int BASE_TICKS_PER_DUST = BASE_TICKS_PER_ELECTRON * 3;
	private static int BASE_TICKS_PER_MOLECULE = BASE_TICKS_PER_ELECTRON * 3;
	private int ticksPerElectron;
	private int ticksPerDust;
	private int ticksPerMolecule;
	private boolean blackHoleSpawned = false;
	
	public Spawn(Controller controller, Game game) {
		this.controller = controller;
		this.game = game;
		tick = 0;
		ticksPerElectron = BASE_TICKS_PER_ELECTRON;
		ticksPerDust = BASE_TICKS_PER_DUST;
		ticksPerMolecule = BASE_TICKS_PER_MOLECULE;
		spawnPlayer();
	}
	
	public void tick() {
		++tick;
		
		// Update spawn rates based on frame speed
		float frameSpeed = controller.getPlayer().getFrameSpeed();
		ticksPerElectron = (int) (BASE_TICKS_PER_ELECTRON - 20 * frameSpeed);
		ticksPerDust = ticksPerElectron * 3;
		ticksPerMolecule = ticksPerElectron * 3;

		if (tick % ticksPerElectron == 0) {
			spawnElectron();
		}
		if (tick % ticksPerDust == 0) {
			spawnDust();
		}
	}
	
	public void spawnPlayer() {
		Player p = new Player(Game.WIDTH/2 - Player.SIZE, Game.HEIGHT/2 - Player.SIZE, ID.PLAYER, controller);
		controller.requestAdd(p);
	}
	
	public void spawnBackgroundStar(int numToSpawn) {
		for (int i = 0; i < numToSpawn; ++i) {
			controller.requestAdd(new BackgroundStar(r.nextInt(Game.WIDTH), r.nextInt(Game.HEIGHT), ID.BG_STAR, controller));
		}
	}
	
	public void spawnElectron() {
		controller.requestAdd(new ElectronEnemy(Game.WIDTH - Enemy.SIZE, r.nextInt(Game.HEIGHT), controller));
	}
	
	public void spawnDust() {
		controller.requestAdd(new DustEnemy(Game.WIDTH - Enemy.SIZE, r.nextInt(Game.HEIGHT), controller));
	}

	public void reset() {
		tick = 0;
		ticksPerElectron = BASE_TICKS_PER_ELECTRON;
		ticksPerDust = BASE_TICKS_PER_DUST;
		ticksPerMolecule = BASE_TICKS_PER_MOLECULE;
		blackHoleSpawned = false;
	}
}
