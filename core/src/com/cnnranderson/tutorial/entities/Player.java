package com.cnnranderson.tutorial.entities;

import box2dLight.Light;
import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.cnnranderson.tutorial.utils.Constants;

import static com.cnnranderson.tutorial.utils.Constants.PPM;

public class Player {

    private TextureAtlas atlas;
    private float hp, mp;
    private float SPEED = 425;
    private TextureRegion current;

    private Body body;
    private PointLight light;

    // Animations
    private float animState, attackAnim;
    private TextureRegion frame1, frame2, frame3, frame4;
    private Animation wLeft, wUp, wDown;
    private Animation aLeft, aUp, aDown, aReturn;
    private Animation harp;
    private boolean dirFlip;
    private boolean playingHarp;
    private boolean attackComplete;

    public Player(World world, RayHandler rays) {
        this.body = createCircle(world, 0, 0, 16);
        this.body.setLinearDamping(20f);
        this.body.setAngularDamping(1.3f);

        //light = new PointLight(rays, 200, new Color(1f, 1f, 1f, .9f), 7, 0, 0);
        //light.setSoftnessLength(0f);
        //light.attachToBody(body);

        Filter f = new Filter();
        f.groupIndex = -1;
        f.categoryBits = Constants.BIT_PLAYER;
        f.maskBits = Constants.BIT_WALL | Constants.BIT_PLAYER;
        Light.setContactFilter(f);

        playingHarp = false;
        attackComplete = true;
        dirFlip = false;
        hp = 100;
        mp = 100;
        initAnimations();
    }

    private void initAnimations() {
        animState = 0;

        atlas = new TextureAtlas(Gdx.files.internal("img/link.txt"));
        frame1 = atlas.findRegion("down1");
        frame2 = atlas.findRegion("down2");
        wDown = new Animation(.175f, frame1, frame2);
        wDown.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);

        frame1 = atlas.findRegion("left1");
        frame2 = atlas.findRegion("left2");
        wLeft = new Animation(.175f, frame1, frame2);
        wLeft.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);

        frame1 = atlas.findRegion("up1");
        frame2 = atlas.findRegion("up2");
        wUp = new Animation(.175f, frame1, frame2);
        wUp.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);

        frame1 = atlas.findRegion("song1");
        frame2 = atlas.findRegion("song2");
        frame3 = atlas.findRegion("song3");
        frame4 = atlas.findRegion("song4");
        harp = new Animation(.25f, frame1, frame2, frame3, frame4);
        harp.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);

        frame1 = atlas.findRegion("attack_down1");
        frame2 = atlas.findRegion("attack_down2");
        frame3 = atlas.findRegion("attack_down3");
        aDown = new Animation(.045f, frame1, frame2, frame3);
        aDown.setPlayMode(Animation.PlayMode.NORMAL);

        frame1 = atlas.findRegion("attack_up1");
        frame2 = atlas.findRegion("attack_up2");
        frame3 = atlas.findRegion("attack_up3");
        aUp = new Animation(.045f, frame1, frame2, frame3);
        aUp.setPlayMode(Animation.PlayMode.NORMAL);

        frame1 = atlas.findRegion("attack_left1");
        frame2 = atlas.findRegion("attack_left2");
        frame3 = atlas.findRegion("attack_left3");
        aLeft = new Animation(.045f, frame1, frame2, frame3);
        aLeft.setPlayMode(Animation.PlayMode.NORMAL);

        aReturn = wDown;
        current = wDown.getKeyFrame(animState);
    }

    public void controller(float delta) {
        // Player Controller
        float x = 0, y = 0;

        if(Gdx.input.isKeyJustPressed((Input.Keys.SPACE))) {
            attackComplete = false;
            attackAnim = 0;
        }

        boolean heldAttack = false;
        if(Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            heldAttack = true;
        }

        if(Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isTouched()) {
            x -= 1;
            current = wLeft.getKeyFrame(animState, true);

            if(!heldAttack) {
                aReturn = wLeft;
                dirFlip = false;
                if(current.isFlipX())
                    current.flip(true, false);
            }
        }
        if(Gdx.input.isKeyPressed((Input.Keys.RIGHT))) {
            x += 1;
            current = wLeft.getKeyFrame(animState, true);

            if(!heldAttack) {
                aReturn = wLeft;
                dirFlip = true;
                if(!current.isFlipX())
                    current.flip(true, false);
            }
        }
        if(Gdx.input.isKeyPressed((Input.Keys.UP))) {
            y += 1;
            current = wUp.getKeyFrame(animState, true);
            if(!heldAttack)
                aReturn = wUp;
        }
        if(Gdx.input.isKeyPressed((Input.Keys.DOWN))) {
            y -= 1;
            current = wDown.getKeyFrame(animState, true);
            if(!heldAttack)
                aReturn = wDown;
        }

        if(!attackComplete) {
            x /= 3;
            y /= 3;
            if(aReturn == wDown)
                current = aDown.getKeyFrame(attackAnim, false);
            else if(aReturn == wUp)
                current = aUp.getKeyFrame(attackAnim, false);
            else if(aReturn == wLeft) {
                if(!dirFlip) {
                    current = aLeft.getKeyFrame(attackAnim, false);
                    if(current.isFlipX()) {
                        current.flip(true, false);
                    }
                } else {
                    current = aLeft.getKeyFrame(attackAnim, false);
                    if(!current.isFlipX()) {
                        current.flip(true, false);
                    }
                }
            }
            attackAnim += delta;
            if(aDown.isAnimationFinished(attackAnim) && !heldAttack) {
                attackComplete = true;
                current = aReturn.getKeyFrame(animState);
            }
        }

        // Dampening check
        if(x != 0) {
            body.setLinearVelocity(x * SPEED * delta, body.getLinearVelocity().y);
        }
        if(y != 0) {
            body.setLinearVelocity(body.getLinearVelocity().x, y * SPEED * delta);
        }

        if(y != 0 || x != 0) {
            playingHarp = false;
            animState += delta;
        } else if(Gdx.input.isKeyJustPressed((Input.Keys.C)) || playingHarp) {
            playingHarp = true;
            animState += delta;
            current = harp.getKeyFrame(animState, true);
        }
    }

    public void render(Batch batch) {
        float w = current.getRegionWidth();
        float h = current.getRegionHeight();
        batch.begin();
        batch.draw(current, body.getPosition().x * Constants.PPM - w, body.getPosition().y * Constants.PPM - h,
                0, 0, w, h, 2, 2, 0);
        batch.end();
    }

    public float getHP() {
        return hp;
    }
    public float getMP() {
        return mp;
    }
    public Vector2 getPosition() {
        return body.getPosition();
    }

    public void dispose() {
        atlas.dispose();
    }

    private Body createCircle(World world, float x, float y, float radius) {
        Body pBody;
        BodyDef def = new BodyDef();

        def.type = BodyDef.BodyType.DynamicBody;

        def.position.set(x / PPM, y / PPM);
        def.fixedRotation = true;
        pBody = world.createBody(def);

        CircleShape shape = new CircleShape();
        shape.setRadius(radius / PPM);

        FixtureDef fd = new FixtureDef();
        fd.shape = shape;
        fd.density = 1.0f;
        fd.filter.categoryBits = Constants.BIT_PLAYER;
        fd.filter.maskBits = Constants.BIT_WALL | Constants.BIT_SENSOR;
        fd.filter.groupIndex = 0;
        pBody.createFixture(fd).setUserData(this);
        shape.dispose();
        return pBody;
    }
}
