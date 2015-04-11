package com.cnnranderson.tutorial.handlers;

import com.badlogic.gdx.physics.box2d.*;
import com.cnnranderson.tutorial.map.FloorSwitch;
import com.cnnranderson.tutorial.entities.Player;

public class WorldContactListener implements ContactListener {

    public WorldContactListener() {
        super();
    }

    // Called when two fixtures start to collide
    @Override
    public void beginContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        if(fa == null || fb == null) return;
        if(fa.getUserData() == null || fb.getUserData() == null) return;

        if(isSwitchContact(fa, fb)) {
            if(fa.getUserData() instanceof FloorSwitch) {
                ((FloorSwitch) fa.getUserData()).activate();
            } else {
                ((FloorSwitch) fb.getUserData()).activate();
            }
        }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        if(fa == null || fb == null) return;
        if(fa.getUserData() == null || fb.getUserData() == null) return;

        if(isSwitchContact(fa, fb)) {
            if(fa.getUserData() instanceof FloorSwitch) {
                ((FloorSwitch) fa.getUserData()).deactivate();
            } else {
                ((FloorSwitch) fb.getUserData()).deactivate();
            }
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }

    private boolean isSwitchContact(Fixture a, Fixture b) {
        if(a.getUserData() instanceof FloorSwitch || b.getUserData() instanceof FloorSwitch) {
            if(b.getUserData() instanceof Player || a.getUserData() instanceof Player) {
                return true;
            }
        }
        return false;
    }
}
