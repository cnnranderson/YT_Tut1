package com.cnnranderson.tutorial.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.cnnranderson.tutorial.managers.GameStateManager;

public class LoadingState extends GameState {

    public LoadingState(GameStateManager gsm) {
        super(gsm);

    }

    @Override
    public void handleInput() {
        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            gsm.setState(GameStateManager.State.SPLASH);
        }
    }

    @Override
    public void update(float delta) {
        handleInput();
    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(1, 0, 1, 1);
    }

    @Override
    public void resize(int w, int h) {

    }

    @Override
    public void dispose() {

    }
}
