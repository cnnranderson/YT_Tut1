package com.cnnranderson.tutorial.states;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.cnnranderson.tutorial.Application;
import com.cnnranderson.tutorial.managers.GameStateManager;

public abstract class GameState {
    protected GameStateManager gsm;
    protected Application app;

    protected SpriteBatch batch;
    protected OrthographicCamera camera;

    protected GameState(GameStateManager gsm) {
        this.gsm = gsm;
        app = gsm.application();
        batch = app.getSpriteBatch();
        camera = app.getCamera();
    }

    public abstract void handleInput();
    public abstract void update(float delta);
    public abstract void render();
    public abstract void resize(int w, int h);
    public abstract void dispose();
}
