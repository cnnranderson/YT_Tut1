package com.cnnranderson.tutorial;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.cnnranderson.tutorial.managers.GameStateManager;

public class Application extends ApplicationAdapter {

    // DEBUG
    public static boolean DEBUG = false;

    // Game Information
    public static final String TITLE = "Tutorial";
    public static final int V_WIDTH = 720;
    public static final int V_HEIGHT = 480;
    public static final float SCALE = 1f;

    public static Engine ashley;
    public static AssetManager assets;

    private OrthographicCamera camera;
    private SpriteBatch batch;

    private GameStateManager gsm;

    @Override
    public void create() {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        ashley = new Engine();

        assets = new AssetManager();
        assets.load("img/switch.png", Texture.class);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, w / SCALE, h / SCALE);

        batch = new SpriteBatch();
        batch.setProjectionMatrix(camera.combined);

        gsm = new GameStateManager(this);
    }

    @Override
    public void render() {
        if(assets.update()) {
            gsm.update(Gdx.graphics.getDeltaTime());
            gsm.render();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) gsm.setState(GameStateManager.State.DUNGEON);
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) gsm.setState(GameStateManager.State.FILTER);
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) gsm.setState(GameStateManager.State.LASER);
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)) gsm.setState(GameStateManager.State.LIGHTS);

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) Gdx.app.exit();
    }

    @Override
    public void resize(int width, int height) {
        gsm.resize((int) (720 / SCALE), (int) (480 / SCALE));
    }

    @Override
    public void dispose() {
        gsm.dispose();
        batch.dispose();
    }

    public OrthographicCamera getCamera() {
        return camera;
    }
    public SpriteBatch getBatch() {
        return batch;
    }
}
