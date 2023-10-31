package com.keikebreau.proton_cruiser;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.AudioDevice;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Game extends ApplicationAdapter {
	AudioDevice audioDevice;
	final Map<String, Sound> soundMap = new HashMap<>();
	final Map<String, Music> musicMap = new HashMap<>();
	private OrthographicCamera camera;
	private PolygonSpriteBatch batch;
	private ShapeRenderer shape;
	private BitmapFont font;
	private Spawn spawner;

	static final int WIDTH = 1280;
	static final int HEIGHT = 800;

	/** Height of the health bar. */
	static final int HUD_HEIGHT = Game.HEIGHT / 15;
	/** X coordinate of the upper-left corner of the health bar. */
	static final int HUD_X = Game.WIDTH * 3 / 128;
	/** Y coordinate of the upper-left corner of the health bar. */
	static final int HUD_Y = Game.HEIGHT / 32;

	/** Handler for all game objects. */
	private Controller controller;

	/** Current level. */
	private static int level = 1;

	public static int getLevel() {
		return level;
	}

	@Override
	public void create () {
		controller = new Controller();
		spawner = new Spawn(controller, this);
		batch = new PolygonSpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, WIDTH, HEIGHT);
		musicMap.put("music", Gdx.audio.newMusic(Gdx.files.internal("music.ogg")));
		musicMap.put("menu", Gdx.audio.newMusic(Gdx.files.internal("menu.ogg")));
		soundMap.put("bigBounce", Gdx.audio.newSound(Gdx.files.internal("bigBounce.ogg")));
		soundMap.put("bounce", Gdx.audio.newSound(Gdx.files.internal("bounce.ogg")));
		soundMap.put("click", Gdx.audio.newSound(Gdx.files.internal("click.ogg")));
		soundMap.put("laser", Gdx.audio.newSound(Gdx.files.internal("laser.ogg")));
		musicMap.get("music").setLooping(true);
		musicMap.get("music").play();
		shape = new ShapeRenderer();
		try {
			audioDevice = Gdx.audio.newAudioDevice(44100, false);
		} catch (GdxRuntimeException e) {
			System.err.println("Could not initialize audio device! Closing application");
			dispose();
			System.exit(1);
		}
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("multivac-ghost.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = 24;
		font = generator.generateFont(parameter); // font size 12 pixels
		generator.dispose(); // don't forget to dispose to avoid memory leaks!

		level = 1;
		/** Current score. */
		float score = 0;
	}

	@Override
	public void render () {
		controller.tick();
		spawner.tick();
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
		batch.setProjectionMatrix(camera.combined);
		String scoreStr = "Speed: " + controller.getPlayer().getFrameSpeed();
		String levelStr = "Level: " + Game.getLevel();
		batch.begin();
		font.draw(batch, scoreStr, HUD_X, HUD_Y + HUD_HEIGHT + 20);
		// Draw current level.
		font.draw(batch, levelStr, HUD_X, HUD_Y + HUD_HEIGHT + font.getLineHeight() + 20);
		batch.end();
		batch.flush();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		audioDevice.dispose();
	}
}
