package com.cnnranderson.tutorial.utils;

public class Accumulator {

    enum Type {
        PINGPONG,
        LOOP,
        ONCE
    }

    private float start, end, wait, endWait, speed;
    private float initWait, initEndWait;
    private boolean direction, paused, looped, waiting, finish;
    private Type type;

    public Accumulator(float s, float e, float w, float sp, Type type) {
        start = s;
        end = e;

        wait = w;
        initWait = wait;

        endWait = 0;
        initEndWait = endWait;

        speed = sp;

        paused = false;
        direction = true;
        waiting = false;
        finish = false;

        this.type = type;
    }

    public Accumulator() {
        this(0, 1, 0, 1, Type.PINGPONG);
    }

    public void update(float delta) {
        if(!paused && (type == Type.LOOP || !looped)) {
            waiting = false;
            if(direction) {
                // Count up
                if (start < end) {
                    start += delta * speed;
                    if (start > end) {
                        start = end;
                    }
                } else if (wait > 0) {
                    wait -= delta;
                    waiting = true;
                } else {
                    direction = false;
                    finish = true;
                    wait = initWait;
                }
            } else if(type != Type.ONCE) {
                // Count down
                if (start > 0) {
                    start -= delta * speed;
                    if (start < 0) {
                        start = 0;
                    }
                } else if(endWait > 0) {
                    endWait -= delta;
                    waiting = true;
                } else {
                    direction = true;
                    looped = true;
                    endWait = initEndWait;
                }
            }
        }
    }

    /* Pause methods */
    public void pause() {
        paused = true;
    }

    public void unpause() {
        paused = false;
    }

    /* Builder Methods */
    public Accumulator start(float start) {
        this.start = start;
        return this;
    }
    public Accumulator end(float end) {
        this.end = end;
        return this;
    }
    public Accumulator wait(float wait) {
        this.wait = wait;
        this.initWait = wait;
        return this;
    }
    public Accumulator endwait(float wait) {
        this.endWait = wait;
        this.initEndWait = wait;
        return this;
    }
    public Accumulator speed(float speed) {
        if(speed < 1f) {
            this.speed = 1f;
        } else {
            this.speed = speed;
        }
        return this;
    }

    /* Value Methods */
    public float getValue() {
        return start;
    }

    public float getValueAsPercent() {
        return start / end;
    }

    /* State Methods */
    public boolean hasLooped() {
        return looped;
    }

    public boolean isFinished() {
        return finish;
    }

    public boolean isWaiting() {
        return waiting;
    }

    public boolean countingUp() {
        return direction;
    }
}
