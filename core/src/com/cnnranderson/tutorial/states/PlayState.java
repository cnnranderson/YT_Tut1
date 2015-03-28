package com.cnnranderson.tutorial.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.cnnranderson.tutorial.managers.GameStateManager;
import com.cnnranderson.tutorial.utils.TiledObjectUtil;
import com.cnnranderson.tutorial.utils.b2d.JointBuilder;

import static com.cnnranderson.tutorial.utils.Constants.PPM;

public class PlayState extends GameState {

    private TiledMap map;

    private Box2DDebugRenderer b2dr;
    private World world;
    private Body body1, body2, body3;

    public PlayState(GameStateManager gsm) {
        super(gsm);

        world = new World(new Vector2(0f, -9.8f), false);
        b2dr = new Box2DDebugRenderer();

        initBodies();

        map = new TmxMapLoader().load("Maps/test_map.tmx");

        TiledObjectUtil.parseTiledObjectLayer(world, map.getLayers().get("collision-layer").getObjects());
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

        b2dr.render(world, camera.combined.scl(PPM));
    }

    @Override
    public void dispose() {
        b2dr.dispose();
        world.dispose();

        map.dispose();
    }

    private void cameraUpdate() {
        Vector3 position = camera.position;
        position.x = camera.position.x + (body1.getPosition().x * PPM - camera.position.x) * .1f;
        position.y = camera.position.y + (body1.getPosition().y * PPM - camera.position.y) * .1f;
        camera.position.set(position);

        camera.update();
    }

    /* TUTORIAL AREA */

    private void initBodies() {
        body1 = createBox(140, 140, 32, 32, false, false);
        body2 = createCircle(140, 120, 12, false);
        body3 = createCircle(140, 120, 12, false);

        // First Joint
        RevoluteJointDef rDef = new RevoluteJointDef();
        rDef.bodyA = body1;
        rDef.bodyB = body2;
        rDef.collideConnected = false;
        rDef.localAnchorA.set(-16, -32).scl(1 / PPM);
        world.createJoint(rDef);

        // Second Joint
        rDef.bodyB = body3;
        rDef.localAnchorA.set(16, -32).scl(1 / PPM);
        world.createJoint(rDef);
    }

    /* END */

    public Body createBox(int x, int y, int width, int height, boolean isStatic, boolean fixedRotation) {
        Body pBody;
        BodyDef def = new BodyDef();

        if(isStatic)
            def.type = BodyDef.BodyType.StaticBody;
        else
            def.type = BodyDef.BodyType.DynamicBody;

        def.position.set(x / PPM, y / PPM);
        def.fixedRotation = fixedRotation;
        pBody = world.createBody(def);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2 / PPM, height / 2 / PPM);

        pBody.createFixture(shape, 1.0f);
        shape.dispose();
        return pBody;
    }

    public Body createCircle(int x, int y, float radius, boolean isStatic) {
        Body pBody;
        BodyDef def = new BodyDef();

        if(isStatic) {
            def.type = BodyDef.BodyType.StaticBody;
        } else {
            def.type = BodyDef.BodyType.DynamicBody;
        }

        def.position.set(x / PPM, y / PPM);
        def.fixedRotation = false;
        pBody = world.createBody(def);

        CircleShape shape = new CircleShape();
        shape.setRadius(radius / PPM);

        pBody.createFixture(shape, 1.0f);
        shape.dispose();
        return pBody;
    }
}
