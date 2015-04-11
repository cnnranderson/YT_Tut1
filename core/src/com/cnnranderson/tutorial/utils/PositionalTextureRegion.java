package com.cnnranderson.tutorial.utils;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;


public class PositionalTextureRegion extends TextureRegion {
    public Vector2 center = new Vector2(0, 0);
    public void setCenter(Vector2 newCenter) {
        center = newCenter;
    }
}
