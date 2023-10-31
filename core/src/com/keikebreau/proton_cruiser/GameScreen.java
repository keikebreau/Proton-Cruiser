package com.keikebreau.proton_cruiser;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.AudioDevice;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class GameScreen implements Screen {
	final ProtonCruiser game;
	AudioDevice audioDevice;
	final Map<String, Sound> soundMap = new HashMap<>();
	final Map<String, Music> musicMap = new HashMap<>();
	private OrthographicCamera camera;
	private ShapeRenderer shape;
	private Spawn spawner;

	static final int WIDTH = 1280;
	static final int HEIGHT = 800;

	/** Height of the health bar. */
	static final int HUD_HEIGHT = GameScreen.HEIGHT / 15;
	/** X coordinate of the upper-left corner of the health bar. */
	static final int HUD_X = GameScreen.WIDTH * 3 / 128;
	/** Y coordinate of the upper-left corner of the health bar. */
	static final int HUD_Y = GameScreen.HEIGHT / 32;

	/** Handler for all game objects. */
	private final Controller controller;

	/** Current level. */
	int level = 1;
	int score = 0;

	public int getLevel() {
		return level;
	}

	public GameScreen(final ProtonCruiser game)
	{
		this.game = game;

		// Load music and sound effects.
		musicMap.put("music", Gdx.audio.newMusic(Gdx.files.internal("music.ogg")));
		musicMap.put("menu", Gdx.audio.newMusic(Gdx.files.internal("menu.ogg")));
		soundMap.put("bigBounce", Gdx.audio.newSound(Gdx.files.internal("bigBounce.ogg")));
		soundMap.put("bounce", Gdx.audio.newSound(Gdx.files.internal("bounce.ogg")));
		soundMap.put("click", Gdx.audio.newSound(Gdx.files.internal("click.ogg")));
		soundMap.put("laser", Gdx.audio.newSound(Gdx.files.internal("laser.ogg")));
		musicMap.get("music").setLooping(true);

		try {
			audioDevice = Gdx.audio.newAudioDevice(44100, false);
		} catch (GdxRuntimeException e) {
			System.err.println("Could not initialize audio device! Closing application");
			dispose();
			System.exit(1);
		}

		// create the camera and the SpriteBatch
		camera = new OrthographicCamera();
		camera.setToOrtho(false, WIDTH, HEIGHT);

		controller = new Controller(game, this);
		spawner = new Spawn(controller, this);

		musicMap.get("music").play();
		shape = new ShapeRenderer();

		level = 1;
		score = 0;
	}

	@Override
	public void show() {

	}

	@Override
	public void render(float delta) {
		controller.tick();
		spawner.tick();
		score += (int)(1000.0f * controller.getPlayer().getFrameSpeed());
		ScreenUtils.clear(0.05f, 0.05f, 0.05f, 1);
		camera.update();


		for (Iterator<GameObject> iter = controller.getObjectIterator(); iter.hasNext(); ) {
			GameObject obj = iter.next();
			shape.setProjectionMatrix(camera.combined);
			shape.begin(ShapeType.Filled);
			shape.setColor(obj.color);
			shape.circle(obj.getX(), obj.getY(), obj.getWidth(), (int)obj.getHeight());
			shape.end();
		}

		// Render the heads-up display
		// Draw current score.
		game.batch.setProjectionMatrix(camera.combined);
		String scoreStr = "Speed: " + controller.getPlayer().getFrameSpeed();
		String levelStr = "Level: " + level;
		game.batch.begin();
		game.font.draw(game.batch, scoreStr, HUD_X, HUD_Y + HUD_HEIGHT + 20);
		// Draw current level.
		game.font.draw(game.batch, levelStr, HUD_X, HUD_Y + HUD_HEIGHT + game.font.getLineHeight() + 20);
		game.batch.end();
		game.batch.flush();
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose () {
		soundMap.values().forEach(Sound::dispose);
		musicMap.values().forEach(Music::dispose);
		audioDevice.dispose();
	}
}
