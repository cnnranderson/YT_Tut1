package com.cnnranderson.tutorial.managers;

import com.cnnranderson.tutorial.Application;
import com.cnnranderson.tutorial.states.GameState;
import com.cnnranderson.tutorial.states.SplashState;

import java.util.Stack;

public class GameStateManager {

    // Application Reference
    private final Application app;

    private Stack<GameState> states;

    public enum State {
        SPLASH,
        MAINMENU
    }

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
            case MAINMENU: return null;
        }
        return null;
    }
}
