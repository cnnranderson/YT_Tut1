package com.cnnranderson.tutorial.handlers;

import com.badlogic.gdx.physics.box2d.*;
import com.cnnranderson.tutorial.entities.Player;
import com.cnnranderson.tutorial.entities.TutorialBox;

public class MyContactListener implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        if(fa == null || fb == null) return;
        if(fa.getUserData() == null || fb.getUserData() == null) return;

        if(isTutorialContact(fa, fb)) {
            TutorialBox tba = (TutorialBox) fa.getUserData();
            TutorialBox tbb = (TutorialBox) fb.getUserData();

            tbb.body.applyForceToCenter(-3000, 0, false);
        }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        if(fa == null || fb == null) return;
        if(fa.getUserData() == null || fb.getUserData() == null) return;

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }

    private boolean isTutorialContact(Fixture a, Fixture b) {
        return (a.getUserData() instanceof TutorialBox && b.getUserData() instanceof TutorialBox);
    }
}
