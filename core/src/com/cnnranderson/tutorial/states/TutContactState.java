package com.cnnranderson.tutorial.states;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.cnnranderson.tutorial.managers.GameStateManager;

/**
 * Created by conner on 4/7/15.
 */
public class TutContactState extends GameState {

    private World world;
    private Box2DDebugRenderer b2dr;
    private Body target;

    public TutContactState(GameStateManager gsm) {
        super(gsm);

    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void render() {

    }

    @Override
    public void dispose() {

    }
}
