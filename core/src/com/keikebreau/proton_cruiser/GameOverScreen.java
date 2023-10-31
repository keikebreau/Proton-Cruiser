package com.keikebreau.proton_cruiser;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameOverScreen implements Screen {
    final ProtonCruiser game;
    final int score;
    OrthographicCamera camera;

    public GameOverScreen(final ProtonCruiser game, int score) {
        this.game = game;
        this.score = score;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, GameScreen.WIDTH, GameScreen.HEIGHT);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.05f, 0.05f, 0.05f, 1);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.font.draw(game.batch, "Game Over! Score: " + score, GameScreen.WIDTH / 2.0f, GameScreen.HEIGHT / 3.0f);
        game.font.draw(game.batch, "Press any key to continue", GameScreen.WIDTH / 2.0f, GameScreen.HEIGHT * 2.0f / 3.0f);
        game.batch.end();

        if (Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)) {
            game.setScreen(new GameScreen(game));
            dispose();
        }
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
    public void dispose() {

    }
}
