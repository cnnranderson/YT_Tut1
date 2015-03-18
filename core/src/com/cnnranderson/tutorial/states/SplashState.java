package com.cnnranderson.tutorial.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.MathUtils;
import com.cnnranderson.tutorial.managers.GameStateManager;

public class SplashState extends GameState {

    float r = MathUtils.random(0f, 1f);
    float g = MathUtils.random(0f, 1f);

    public SplashState(GameStateManager gsm) {
        super(gsm);
    }

    @Override
    public void update(float delta) {
        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            gsm.endCurrentState();
        } else if(Gdx.input.isKeyJustPressed(Input.Keys.N)) {
            gsm.setState(GameStateManager.State.SPLASH);
        }
    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(r, g, 1, .5f);

        batch.setProjectionMatrix(camera.combined);
    }

    @Override
    public void dispose() {

    }
}
