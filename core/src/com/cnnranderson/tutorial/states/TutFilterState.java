package com.cnnranderson.tutorial.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.cnnranderson.tutorial.managers.GameStateManager;
import com.cnnranderson.tutorial.utils.CameraStyles;
import com.cnnranderson.tutorial.utils.Constants;
import com.cnnranderson.tutorial.utils.b2d.BodyBuilder;

import static com.cnnranderson.tutorial.utils.Constants.PPM;

public class TutFilterState extends GameState {

    private World world;
    private Box2DDebugRenderer b2dr;
    private Body body;

    public TutFilterState(GameStateManager gsm) {
        super(gsm);
        this.world = new World(new Vector2(0, -9.8f), false);
        this.b2dr = new Box2DDebugRenderer();

        body = BodyBuilder.createBox(world, 0, 150, 40, 40, false, true,
                Constants.BIT_PLAYER, (short) (Constants.BIT_WALL), (short) 1);

        BodyBuilder.createBox(world, 0, -20, 400, 40, true, true,
                Constants.BIT_SENSOR, (short) (Constants.BIT_SENSOR), (short) 1);

        BodyBuilder.createBox(world, 0, -90, 400, 40, true, true,
                Constants.BIT_WALL, (short) (Constants.BIT_PLAYER), (short) 0);
    }

    @Override
    public void update(float delta) {
        world.step(1 / 60f, 6, 2);
        cameraUpdate();
        batch.setProjectionMatrix(camera.combined);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(.25f, .25f, .25f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        b2dr.render(world, camera.combined.cpy().scl(PPM));
    }

    @Override
    public void dispose() {
        b2dr.dispose();
        world.dispose();
    }

    private void cameraUpdate() {
        CameraStyles.lerpToTarget(camera, body.getPosition().scl(PPM));
    }
}
