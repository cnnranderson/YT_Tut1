package com.cnnranderson.tutorial.utils.b2d;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.cnnranderson.tutorial.utils.Constants;

public class JointBuilder {
    /**
     * Creates a Revolute joint between two bodies
     * @param world The world where bodies exist
     * @param bodyA A body to be joined
     * @param bodyB Another body to be joined
     * @param collide Whether the two bodies can collide still
     * @return new Joint created in the world between the two bodies
     */
    public static Joint joinWithRevolute(World world, Body bodyA, Body bodyB, boolean collide) {
        RevoluteJointDef rDef = new RevoluteJointDef();
        rDef.bodyA = bodyA;
        rDef.bodyB = bodyB;
        rDef.collideConnected = collide;
        return world.createJoint(rDef);
    }

    /**
     * Creates a Revolute joint between two bodies
     * @param world The world where bodies exist
     * @param bodyA A body to be joined
     * @param bodyB Another body to be joined
     * @param collide Whether the two bodies can collide still
     * @param anchorA Anchor location for bodyA
     * @param anchorB Anchor location for bodyB
     * @return new Joint created in the world between the two bodies
     */
    public static Joint joinWithRevolute(World world, Body bodyA, Body bodyB, boolean collide, Vector2 anchorA, Vector2 anchorB) {
        RevoluteJointDef rDef = new RevoluteJointDef();
        rDef.bodyA = bodyA;
        rDef.bodyB = bodyB;
        rDef.collideConnected = collide;

        rDef.localAnchorA.set(anchorA.scl(1 / Constants.PPM));
        rDef.localAnchorB.set(anchorB.scl(1 / Constants.PPM));
        return world.createJoint(rDef);
    }

    /**
     * Creates a Revolute joint between two bodies
     * @param world The world where bodies exist
     * @param bodyA A body to be joined
     * @param bodyB Another body to be joined
     * @param collide Whether the two bodies can collide still
     * @param speed Initial RPM
     * @param maxSpeed Maximum RPM of joint
     * @return new Joint created in the world between the two bodies
     */
    public static Joint joinWithRevoluteMotor(World world, Body bodyA, Body bodyB, boolean collide, float speed, float maxSpeed) {
        RevoluteJointDef rDef = new RevoluteJointDef();
        rDef.bodyA = bodyA;
        rDef.bodyB = bodyB;
        rDef.collideConnected = false;

        rDef.enableMotor = true;
        rDef.motorSpeed = speed;
        rDef.maxMotorTorque = maxSpeed;
        return world.createJoint(rDef);
    }



    /* Cart example
        body1 = createBox(140, 140, 32, 32, false, false);
        body2 = createCircle(140, 120, 12, false);
        body3 = createCircle(140, 120, 12, false);

        JointBuilder.RevoluteBuilder rb = new JointBuilder.RevoluteBuilder(world, body1, body2, false);
        rb.anchorA(-16, -32).anchorB(0, 0).motor(3, 40);
        rb.build();

        rb.bodyB(body3).anchorA(16, -32).motor(0, 40);
        rb.build();
     */
}
