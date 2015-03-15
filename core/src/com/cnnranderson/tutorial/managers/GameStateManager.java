package com.cnnranderson.tutorial.managers;


import com.cnnranderson.tutorial.Application;
import com.cnnranderson.tutorial.states.GameState;
import com.cnnranderson.tutorial.states.LoadingState;
import com.cnnranderson.tutorial.states.SplashState;

import java.util.Stack;

public class GameStateManager extends Stack<GameState> {

    // Application Reference
    private final Application app;

    // Possible Game States
    public enum State {
        SPLASH,
        LOADING,
        MAINMENU,
        LEVELSELECT,
        PLAY,
        GAMEOVER
    }

    /**
     * Game State Manager
     * Manages all game states in stack ordinance
     *
     * @param app - Main Application reference
     */
    public GameStateManager(final Application app) {
        this.app = app;
        pushState(State.SPLASH);
    }

    public Application application() {
        return app;
    }

    public void update(float delta) {
        peek().update(delta);
    }

    public void render() {
        peek().render();
    }

    public void resize(int w, int h) {
        peek().resize(w, h);
    }

    private GameState getState(State state) {
        switch (state) {
            case SPLASH:
                return new SplashState(this);
            case LOADING:
                return new LoadingState(this);
            case MAINMENU:
                return null;
            case LEVELSELECT:
                return null;
            case PLAY:
                return null;
            case GAMEOVER:
                return null;
        }
        return null;
    }

    public void setState(State state) {
        popState();
        pushState(state);
    }

    public void pushState(State state) {
        push(getState(state));
    }

    public void popState() {
        pop().dispose();
    }
}
