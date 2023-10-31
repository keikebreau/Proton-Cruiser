package com.keikebreau.proton_cruiser;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class ProtonCruiser extends Game {
    public SpriteBatch batch;
    public BitmapFont font;
    public AssetManager manager;

    public void create() {
        manager = new AssetManager();
        batch = new SpriteBatch();
        // Load a font
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("multivac-ghost.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 24;
        font = generator.generateFont(parameter); // font size 12 pixels
        generator.dispose(); // don't forget to dispose to avoid memory leaks!
        this.setScreen(new MainMenuScreen(this));
    }

    public void render() {
        super.render();
    }

    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}
