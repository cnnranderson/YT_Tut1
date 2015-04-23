package com.cnnranderson.tutorial.entities;

import com.badlogic.gdx.physics.box2d.*;
import com.cnnranderson.tutorial.utils.Constants;

public class TutorialBox {

    public Body body;
    public String id;

    public TutorialBox(World world, String id, float x, float y) {
        this.id = id;
        createBoxBody(world, x, y);
        body.setLinearDamping(20f);
    }

    private void createBoxBody(World world, float x, float y) {
        BodyDef bdef = new BodyDef();
        bdef.fixedRotation = true;
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.position.set(x / Constants.PPM, y / Constants.PPM);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(32 / Constants.PPM / 2, 32 / Constants.PPM / 2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;

        this.body = world.createBody(bdef);
        this.body.createFixture(fixtureDef).setUserData(this);
    }

    public void hit() {
        System.out.println(id + " :I've been hit!");
    }
}
