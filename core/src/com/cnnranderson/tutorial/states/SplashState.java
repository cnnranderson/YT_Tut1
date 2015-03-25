package com.cnnranderson.tutorial.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.cnnranderson.tutorial.managers.GameStateManager;
import com.cnnranderson.tutorial.utils.Accumulator;

public class SplashState extends GameState {

    float f = 10f;
    Texture tex;
    Accumulator acc;

    public SplashState(GameStateManager gsm) {
        super(gsm);
        tex = new Texture("img/splash.png");
        acc = new Accumulator();

        acc.start(0f).end(1f).wait(0.5f).speed(3f).endwait(.5f);
        batch.setProjectionMatrix(camera.combined);
    }

    @Override
    public void update(float delta) {
        acc.update(delta);

        if(!acc.isWaiting() && acc.countingUp()) {
            acc.speed((1-acc.getValueAsPercent()) * f);
        } else if(!acc.isWaiting()) {
            acc.speed(acc.getValueAsPercent() * f);
        }

        if(acc.hasLooped()) {
            gsm.setState(GameStateManager.State.MAINMENU);
        }
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.setColor(1f, 1f, 1f, acc.getValueAsPercent());
        batch.draw(tex, Gdx.graphics.getWidth() / 4 - tex.getWidth() / 2, Gdx.graphics.getHeight() / 4 - tex.getWidth() - 60 + acc.getValueAsPercent() * 90);
        batch.end();
    }

    @Override
    public void dispose() {
        tex.dispose();
    }
}
