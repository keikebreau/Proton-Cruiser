package com.keikebreau.proton_cruiser;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setWindowedMode(Game.WIDTH, Game.HEIGHT);
		config.useVsync(true);
		config.setTitle("Proton Cruiser");
		new Lwjgl3Application(new Game(), config);
	}
}
