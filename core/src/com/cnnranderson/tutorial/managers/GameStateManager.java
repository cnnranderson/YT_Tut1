package com.cnnranderson.tutorial.managers;

import com.cnnranderson.tutorial.Application;
import com.cnnranderson.tutorial.states.*;

import java.util.Stack;

public class GameStateManager {

    // Application Reference
    private final Application app;

    private Stack<GameState> states;

    public enum State {
        SPLASH,
        PLAY,
        DUNGEON,
        FILTER,
        LIGHTS,
        JOINTS,
        CONTACT,
        LASER
    }

    public GameStateManager(final Application app) {
        this.app = app;
        this.states = new Stack<GameState>();
        this.setState(State.CONTACT);
    }

    public Application application() {
        return app;
    }

    public void update(float delta) {
        states.peek().update(delta);
    }

    public void render() {
        states.peek().render();
    }

    public void dispose() {
        for(GameState gs : states) {
            gs.dispose();
        }
        states.clear();
    }

    public void resize(int w, int h) {
        states.peek().resize(w, h);
    }

    public void setState(State state) {
        if(states.size() >= 1) {
            states.pop().dispose();
        }
        states.push(getState(state));
    }

    private GameState getState(State state) {
        switch(state) {
            case SPLASH: return new SplashState(this);
            case PLAY: return new SplashState(this);
            case FILTER: return new TutFilterState(this);
            case DUNGEON: return new DungeonState(this);
            case LASER: return new LaserState(this);
            case JOINTS: return new TutJointState(this);
            case LIGHTS: return new TutLightsState(this);

            case CONTACT: return new TutContactState(this);
        }
        return null;
    }
}
