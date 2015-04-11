package com.cnnranderson.tutorial.states;

import box2dLight.DirectionalLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.cnnranderson.tutorial.managers.GameStateManager;
import com.cnnranderson.tutorial.utils.CameraStyles;
import com.cnnranderson.tutorial.utils.Constants;

import static com.cnnranderson.tutorial.utils.Constants.PPM;


public class LaserState extends GameState {

    private World world;
    private RayHandler rays;
    private Box2DDebugRenderer b2dr;
    private Body body;

    public LaserState(GameStateManager gsm) {
        super(gsm);
        this.world = new World(new Vector2(0, -9.8f), false);
        this.b2dr = new Box2DDebugRenderer();
        this.rays = new RayHandler(world);
        //this.rays.setAmbientLight(.7f);
        body = createBox(0, 150, 40, 40, false, true, Constants.BIT_PLAYER, Constants.BIT_WALL, (short) 0x0000);
        createBox(0, -20, 400, 40, true, true, Constants.BIT_WALL,(short) (Constants.BIT_PLAYER | Constants.BIT_WALL), (short) 0x0000);

        DirectionalLight dl = new DirectionalLight(rays, 100, Color.GREEN, 0);
    }

    @Override
    public void update(float delta) {
        world.step(1 / 60f, 6, 2);
        cameraUpdate();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(.25f, .25f, .25f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        b2dr.render(world, camera.combined.cpy().scl(PPM));
        rays.updateAndRender();
    }

    @Override
    public void dispose() {
        b2dr.dispose();
        world.dispose();
    }

    private void cameraUpdate() {
        CameraStyles.lerpToTarget(camera, body.getPosition());
    }

    public Body createBox(float x, float y, float w, float h, boolean isStatic, boolean canRotate, short cBits, short mBits, short gIndex) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.fixedRotation = canRotate;
        bodyDef.position.set(x / PPM, y / PPM);
        if(isStatic) {
            bodyDef.type = BodyDef.BodyType.StaticBody;
        } else {
            bodyDef.type = BodyDef.BodyType.DynamicBody;
        }

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(w / PPM / 2, h / PPM / 2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;
        fixtureDef.filter.categoryBits = cBits;
        fixtureDef.filter.maskBits = mBits;
        fixtureDef.filter.groupIndex = gIndex;

        return world.createBody(bodyDef).createFixture(fixtureDef).getBody();
    }
}
