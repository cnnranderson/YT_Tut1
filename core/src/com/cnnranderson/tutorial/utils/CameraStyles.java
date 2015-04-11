package com.cnnranderson.tutorial.utils;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class CameraStyles {

    public static void lockOnTarget(Camera camera, Vector2 target) {
        Vector3 position = camera.position;
        position.x = target.x;
        position.y = target.y;
        camera.position.set(position);
        camera.update();
    }

    public static void lerpToTarget(Camera camera, Vector2 target) {
        // a + (b - a) * lerp factor
        Vector3 position = camera.position;
        position.x = camera.position.x + (target.x - camera.position.x) * .1f;
        position.y = camera.position.y + (target.y - camera.position.y) * .1f;
        camera.position.set(position);
        camera.update();
    }

    public static void lockAverageBetweenTargets(Camera camera, Vector2 targetA, Vector2 targetB) {
        Vector3 position = camera.position;
        position.x = (targetA.x + targetB.x) / 2;
        position.y = (targetA.y + targetB.y) / 2;
        camera.position.set(position);
        camera.update();
    }

    public static void lerpAverageBetweenTargets(Camera camera, Vector2 targetA, Vector2 targetB) {
        float avgX = (targetA.x + targetB.x) / 2;
        float avgY = (targetA.y + targetB.y) / 2;

        Vector3 position = camera.position;
        position.x = camera.position.x + (avgX - camera.position.x) * .1f;
        position.y = camera.position.y + (avgY - camera.position.y) * .1f;
        camera.position.set(position);
        camera.update();
    }

    public static boolean searchFocalPoints(OrthographicCamera camera, Array<Vector2> focalPoints, Vector2 target, float threshold) {
        for(Vector2 point : focalPoints) {
            if(target.dst(point) < threshold) {
                float newZoom = (target.dst(point) / threshold) + .2f;
                camera.zoom = camera.zoom + ((newZoom > 1? 1 : newZoom) - camera.zoom) * .1f;
                CameraStyles.lerpToTarget(camera, point);
                return true;
            }
        }
        return false;
    }

    public static void shake(Camera camera, Vector2 displacement, float strength) {
        Vector3 position = camera.position;
        position.x += displacement.x * strength;
        position.y += displacement.y * strength;
        camera.position.set(position);
        camera.update();
    }
}
