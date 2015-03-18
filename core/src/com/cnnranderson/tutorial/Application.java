package com.cnnranderson.tutorial;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.cnnranderson.tutorial.managers.GameStateManager;

public class Application extends ApplicationAdapter {

    // DEBUG
    public static boolean DEBUG = false;

    // Game Configuration
    public static final String TITLE = "Tutorial Project";
    public static final double VERSION = 0.1;
    public static final int V_WIDTH = 720;
    public static final int V_HEIGHT = 480;
    public static final float SCALE = 2.0f;

    private static GameStateManager gsm;
    private OrthographicCamera camera;
    private SpriteBatch batch;

    @Override
    public void create() {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        batch = new SpriteBatch();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, w / SCALE, h / SCALE);

        gsm = new GameStateManager(this);
    }

    @Override
    public void render() {
        gsm.update(Gdx.graphics.getDeltaTime());
        gsm.render();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) Gdx.app.exit();
    }

    @Override
    public void resize(int width, int height) {
        gsm.resize((int) (width / SCALE), (int) (height/ SCALE));
    }

    @Override
    public void dispose() {
        batch.dispose();
        gsm.dispose();
    }

    public SpriteBatch getSpriteBatch() {
        return batch;
    }
    public OrthographicCamera getCamera() {
        return camera;
    }
}
