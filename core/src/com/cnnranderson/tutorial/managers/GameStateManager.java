package com.cnnranderson.tutorial.managers;


import com.cnnranderson.tutorial.Application;
import com.cnnranderson.tutorial.states.GameState;
import com.cnnranderson.tutorial.states.SplashState;

import java.util.Stack;

public class GameStateManager {

    // Application Reference
    private final Application app;

    private Stack<GameState> states;

    // Possible Game States
    public enum State {
        SPLASH
    }

    /**
     * Game State Manager
     * Manages all game states in stack ordinance
     *
     * @param app - Main Application reference
     */
    public GameStateManager(final Application app) {
        this.app = app;
        this.states = new Stack<GameState>();
        this.setState(State.SPLASH);
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

    public void resize(int w, int h) {
        states.peek().resize(w, h);
    }

    public void dispose() {
        for(GameState gs : states) {
            gs.dispose();
        }
        states.clear();
    }

    public void setState(State state) {
        states.push(getState(state));
        if(states.size() > 5) {
            states.remove(0).dispose();
        }
    }

    public void endCurrentState() {
        if(states.size() > 1) {
            states.pop().dispose();
        }
    }

    private GameState getState(State state) {
        return new SplashState(this);
    }
}
