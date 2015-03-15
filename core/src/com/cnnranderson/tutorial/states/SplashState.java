package com.cnnranderson.tutorial.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.cnnranderson.tutorial.managers.GameStateManager;

public class SplashState extends GameState {

    Texture tex;

    public SplashState(GameStateManager gsm) {
        super(gsm);
        tex = new Texture("Images/cat.png");
    }

    @Override
    public void handleInput() {
        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            gsm.setState(GameStateManager.State.LOADING);
        }
    }

    @Override
    public void update(float delta) {
        handleInput();
    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(1, 1, 1, .5f);

        batch.setProjectionMatrix(camera.combined);

        this.batch.begin();
        this.batch.draw(tex, 20, 20);
        this.batch.end();
    }

    @Override
    public void resize(int w, int h) {
        camera.setToOrtho(false, w / 2, h / 2);
    }

    @Override
    public void dispose() {
        tex.dispose();
    }
}
