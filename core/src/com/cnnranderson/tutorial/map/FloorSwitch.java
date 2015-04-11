package com.cnnranderson.tutorial.map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJoint;
import com.badlogic.gdx.utils.Array;
import com.cnnranderson.tutorial.Application;
import com.cnnranderson.tutorial.utils.Constants;

import static com.cnnranderson.tutorial.utils.Constants.PPM;

public class FloorSwitch {

    private World world;
    private Body sensor;
    private Texture tex;
    private TextureRegion on, off;
    private boolean activated, flips;
    private Array<PrismaticJoint> control;

    public FloorSwitch(World world, float x, float y, Array<PrismaticJoint> control, boolean flips) {
        this.world = world;
        this.control = control;
        this.activated = false;
        this.flips = flips;

        // Setup textures
        tex = Application.assets.get("img/switch.png", Texture.class);
        on = new TextureRegion(tex);
        off = new TextureRegion(tex);

        on.setRegion(0, 0, 16, 16);
        off.setRegion(16, 0, 16, 16);

        initSwitch(x, y);
    }

    private void initSwitch(float x, float y) {
        BodyDef bDef = new BodyDef();
        bDef.position.set(x / PPM, y / PPM);

        sensor = world.createBody(bDef);

        CircleShape shape = new CircleShape();
        shape.setRadius(9 / PPM);

        FixtureDef fd = new FixtureDef();
        fd.shape = shape;
        fd.isSensor = true;
        fd.filter.categoryBits = Constants.BIT_SENSOR;
        fd.filter.maskBits = Constants.BIT_PLAYER;
        sensor.createFixture(fd).setUserData(this);
        shape.dispose();
    }

    public void draw(Batch batch) {
        batch.begin();
        if(on != null && off != null) {
            batch.draw(!activated? on : off,
                    sensor.getPosition().x * PPM - 16, sensor.getPosition().y * PPM - 16,
                    0, 0, 16, 16, 2, 2, 0);
        }
        batch.end();
    }

    public Body getSensor() {
        return sensor;
    }

    public void activate() {
        if(!activated) {
            activated = true;
            for (PrismaticJoint j : control) {
                j.setMotorSpeed(-j.getMotorSpeed());
            }
        }
    }
    public void deactivate() {
        if(flips && activated) {
            activated = false;
            for (PrismaticJoint j : control) {
                j.setMotorSpeed(-j.getMotorSpeed());
            }
        }
    }
}
