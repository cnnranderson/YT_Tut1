package com.cnnranderson.tutorial.map;


import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJoint;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJointDef;
import com.badlogic.gdx.utils.Array;
import com.cnnranderson.tutorial.entities.Player;
import com.cnnranderson.tutorial.utils.b2d.BodyBuilder;

import static com.cnnranderson.tutorial.utils.Constants.PPM;

public class DungeonRoom {

    private World world;

    private int x, y;
    private Vector2 center;
    private Array<Body> structure;
    private Array<FloorSwitch> switches;
    private Array<Vector2> focalPoints;
    private Array<Player> enemies;

    public DungeonRoom(World world, Vector2 roomCenter, int x, int y) {
        this.world = world;
        this.center = roomCenter;
        this.structure = new Array<Body>();
        this.switches = new Array<FloorSwitch>();
        this.focalPoints = new Array<Vector2>();
        this.x = x;
        this.y = y;

        initRoomStructure();
    }

    private void initRoomStructure() {
        // Bottom Wall
        structure.add(right = BodyBuilder.createBox(world, center.x + 199, center.y - 228, 323, 24, true, true));
        structure.add(left = BodyBuilder.createBox(world, center.x - 199, center.y - 228, 323, 24, true, true));

        // Top Wall
        structure.add(BodyBuilder.createBox(world, center.x + 199, center.y + 228, 323, 24, true, true));
        structure.add(BodyBuilder.createBox(world, center.x - 199, center.y + 228, 323, 24, true, true));

        // Right wall
        structure.add(BodyBuilder.createBox(world, center.x + 348, center.y + 127, 24, 178, true, true));
        structure.add(BodyBuilder.createBox(world, center.x + 348, center.y - 127, 24, 178, true, true));

        // Left wall
        structure.add(BodyBuilder.createBox(world, center.x - 348, center.y + 127, 24, 178, true, true));
        structure.add(BodyBuilder.createBox(world, center.x - 348, center.y - 127, 24, 178, true, true));
    }

    private Body left, right;
    public void createTestLock() {
        Array<PrismaticJoint> joints = new Array<PrismaticJoint>();
        Body leftSpike, rightSpike;
        BodyDef bDef = new BodyDef();
        bDef.type = BodyDef.BodyType.DynamicBody;
        bDef.position.set(center.x / PPM, (center.y - 215) / PPM);
        bDef.fixedRotation = true;

        leftSpike = world.createBody(bDef);
        rightSpike = world.createBody(bDef);

        float[] vertsL = new float[]{
                0,0,
                38/PPM,0,
                12/PPM,23/PPM,
                0,23/PPM
        };
        float[] vertsR = new float[]{
                0,0,
                -38/PPM,0,
                -12/PPM,23/PPM,
                0,23/PPM
        };

        PolygonShape shape = new PolygonShape();
        shape.set(vertsL);
        leftSpike.createFixture(shape, 1.0f);
        shape.set(vertsR);
        rightSpike.createFixture(shape, 1.0f);
        shape.dispose();

        PrismaticJointDef pDef = new PrismaticJointDef();
        pDef.enableMotor = true;
        pDef.motorSpeed = 10;
        pDef.maxMotorForce = 500;
        pDef.bodyA = left;
        pDef.bodyB = leftSpike;
        pDef.enableLimit = true;
        pDef.lowerTranslation = 130 / PPM;
        pDef.upperTranslation = 172 / PPM;
        pDef.localAnchorB.set(12 / PPM, 11 / PPM);
        pDef.collideConnected = false;
        joints.add((PrismaticJoint) world.createJoint(pDef));

        pDef.bodyA = right;
        pDef.bodyB = rightSpike;
        pDef.collideConnected = false;
        pDef.enableMotor = true;
        pDef.motorSpeed = -10;
        pDef.maxMotorForce = 500;
        pDef.upperTranslation = -130 / PPM;
        pDef.lowerTranslation = -172 / PPM;
        pDef.localAnchorB.set(-12 / PPM, 11 / PPM);
        joints.add((PrismaticJoint) world.createJoint(pDef));

        int i = MathUtils.random(1);
        switches.add(new FloorSwitch(world, center.x + MathUtils.random(280f) - 140f, center.y + MathUtils.random(280f) - 140f, joints, i == 1));
        focalPoints.add(switches.get(switches.size - 1).getSensor().getPosition().cpy().scl(PPM));
    }

    public void render(Batch batch) {
        for(FloorSwitch fs : switches) {
            fs.draw(batch);
        }
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public Array<Vector2> getFocalPoints() {
        return focalPoints;
    }

    // TODO: Add Enemy
    // TODO: Hide Room? Necessary for dungeon size?
}
