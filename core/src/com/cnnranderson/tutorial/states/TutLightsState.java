package com.cnnranderson.tutorial.states;

import box2dLight.ConeLight;
import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.cnnranderson.tutorial.managers.GameStateManager;
import com.cnnranderson.tutorial.utils.CameraStyles;
import com.cnnranderson.tutorial.utils.b2d.BodyBuilder;
import com.cnnranderson.tutorial.utils.b2d.LightBuilder;

import static com.cnnranderson.tutorial.utils.Constants.PPM;

public class TutLightsState extends GameState {

    private World world;
    private Box2DDebugRenderer b2dr;
    private Body player;

    private RayHandler rayHandler;
    private PointLight pl;
    private ConeLight cl;

    public TutLightsState(GameStateManager gsm) {
        super(gsm);
        this.world = new World(new Vector2(0, 0), false);
        this.b2dr = new Box2DDebugRenderer();

        this.player = BodyBuilder.createBox(world, 0, 0, 32, 32, false, false);
        this.player.setLinearDamping(20f);

        BodyBuilder.createBox(world, 200, 0, 40, 150, true, true);
        BodyBuilder.createBox(world, -200, 0, 40, 150, true, true);
        BodyBuilder.createBox(world, 0, 200, 150, 40, true, true);
        BodyBuilder.createBox(world, 0, -200, 150, 40, true, true);

        rayHandler = new RayHandler(world);
        rayHandler.setAmbientLight(.5f);

        cl = LightBuilder.createConeLight(rayHandler, player, Color.WHITE, 6, player.getAngle(), 30);
    }

    @Override
    public void update(float delta) {
        world.step(1 / 60f, 6, 2);
        rayHandler.update();

        inputUpdate(delta);
        cameraUpdate();
        batch.setProjectionMatrix(camera.combined);
        rayHandler.setCombinedMatrix(camera.combined.cpy().scl(PPM));
    }

    @Override
    public void render() {
        Gdx.gl20.glClearColor(.25f, .25f, .25f, 1f);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        b2dr.render(world, camera.combined.cpy().scl(PPM));
        rayHandler.render();
    }

    @Override
    public void dispose() {
        rayHandler.dispose();
        b2dr.dispose();
        world.dispose();
    }

    private void cameraUpdate() {
        CameraStyles.lerpToTarget(camera, player.getPosition().scl(PPM));
    }

    private void inputUpdate(float delta) {
        float x = 0, y = 0;
        if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
            y += 1;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            y -= 1;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            x -= 1;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            x += 1;
        }

        // Dampening check
        if(x != 0) {
            player.setLinearVelocity(x * 350 * delta, player.getLinearVelocity().y);
        }
        if(y != 0) {
            player.setLinearVelocity(player.getLinearVelocity().x, y * 350 * delta);
        }
    }

}
