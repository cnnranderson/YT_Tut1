package com.cnnranderson.tutorial.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJoint;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJointDef;
import com.cnnranderson.tutorial.managers.GameStateManager;
import com.cnnranderson.tutorial.utils.CameraStyles;
import com.cnnranderson.tutorial.utils.b2d.BodyBuilder;

import static com.cnnranderson.tutorial.utils.Constants.PPM;

public class TutJointState extends GameState {

    private World world;
    private Box2DDebugRenderer b2dr;
    private Body target;

    public TutJointState(GameStateManager gsm) {
        super(gsm);
        this.world = new World(new Vector2(0, 0), false);
        this.b2dr = new Box2DDebugRenderer();

        target = BodyBuilder.createBox(world, 0, 0, 30, 30, false, true);
        target.setLinearDamping(20f);
        buildPrismaticJoints();
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
        CameraStyles.lerpToTarget(camera, target.getPosition().scl(PPM));
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
            target.setLinearVelocity(x * 350 * delta, target.getLinearVelocity().y);
        }
        if(y != 0) {
            target.setLinearVelocity(target.getLinearVelocity().x, y * 350 * delta);
        }
    }


    /* TUTORIAL AREA */

    private void buildPrismaticJoints() {
        Body bodyA = BodyBuilder.createBox(world, 0, 0, 70, 30, false, true);
        Body bodyB = BodyBuilder.createBox(world, 0, 0, 30, 30, false, true);
        bodyA.setLinearDamping(1.75f);
        bodyB.setLinearDamping(1.75f);

        PrismaticJointDef pDef = new PrismaticJointDef();
        pDef.bodyA = bodyA;
        pDef.bodyB = bodyB;
        pDef.collideConnected = false;

        pDef.enableLimit = true;
        pDef.upperTranslation = 110 / PPM;
        pDef.lowerTranslation = -110 / PPM;
        world.createJoint(pDef);
    }

    /* END TUTORIAL AREA */
}
