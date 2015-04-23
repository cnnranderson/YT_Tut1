package com.cnnranderson.tutorial.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.cnnranderson.tutorial.entities.TutorialBox;
import com.cnnranderson.tutorial.handlers.MyContactListener;
import com.cnnranderson.tutorial.managers.GameStateManager;
import com.cnnranderson.tutorial.utils.CameraStyles;

import static com.cnnranderson.tutorial.utils.Constants.PPM;

public class TutContactState extends GameState {

    private World world;
    private Box2DDebugRenderer b2dr;
    private TutorialBox player, obj1, obj2;

    public TutContactState(GameStateManager gsm) {
        super(gsm);

        this.world = new World(new Vector2(0, 0), false);
        this.world.setContactListener(new MyContactListener());

        this.b2dr = new Box2DDebugRenderer();

        player = new TutorialBox(world, "PLAYER", 0, 0);
        obj1 = new TutorialBox(world, "OBJ1", 75, 75);
        obj2 = new TutorialBox(world, "OBJ2", -75, 75);
    }

    @Override
    public void update(float delta) {
        world.step(1 / 60f, 6, 2);

        inputUpdate(delta);
        cameraUpdate();
        batch.setProjectionMatrix(camera.combined);
    }

    @Override
    public void render() {
        Gdx.gl20.glClearColor(.25f, .25f, .25f, 1f);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        b2dr.render(world, camera.combined.cpy().scl(PPM));
    }

    @Override
    public void dispose() {
        b2dr.dispose();
        world.dispose();
    }

    private void cameraUpdate() {
        CameraStyles.lerpToTarget(camera, new Vector2(0, 0));
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
            player.body.setLinearVelocity(x * 350 * delta, player.body.getLinearVelocity().y);
        }
        if(y != 0) {
            player.body.setLinearVelocity(player.body.getLinearVelocity().x, y * 350 * delta);
        }
    }
}
