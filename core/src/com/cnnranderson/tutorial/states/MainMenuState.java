package com.cnnranderson.tutorial.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.cnnranderson.tutorial.managers.GameStateManager;

public class MainMenuState extends GameState {

    public MainMenuState(GameStateManager gsm) {
        super(gsm);
    }

    @Override
    public void update(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) gsm.setState(GameStateManager.State.SPLASH);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(.25f, .25f, .25f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void dispose() {

    }
}
