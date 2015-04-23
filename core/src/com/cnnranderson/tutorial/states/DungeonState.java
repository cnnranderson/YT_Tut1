package com.cnnranderson.tutorial.states;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.utils.Array;
import com.cnnranderson.tutorial.map.FloorSwitch;
import com.cnnranderson.tutorial.entities.Player;
import com.cnnranderson.tutorial.handlers.WorldContactListener;
import com.cnnranderson.tutorial.managers.GameStateManager;
import com.cnnranderson.tutorial.map.DungeonRoom;
import com.cnnranderson.tutorial.utils.CameraStyles;
import com.cnnranderson.tutorial.utils.Constants;
import com.cnnranderson.tutorial.utils.b2d.BodyBuilder;

import static com.cnnranderson.tutorial.utils.Constants.PPM;
import static com.cnnranderson.tutorial.utils.b2d.BodyBuilder.*;

public class DungeonState extends GameState {

    private Player player;

    // Hud
    private BitmapFont font;
    private OrthographicCamera hud;
    private String cameraType = "Lock to target - Room Center";

    // Camera tracker stuff
    private DungeonRoom[][] rooms;
    private Vector2 pos;
    private int cameraVal = 0;

    // Player stuff
    private Vector2 target;

    // b2d/lock stuff
    private RayHandler rays;
    private Box2DDebugRenderer b2dr;
    private World world;
    private Array<FloorSwitch> switches;
    private Array<Body> torches;

    public DungeonState(GameStateManager gsm) {
        super(gsm);

        // HUD init
        font = new BitmapFont();
        hud = new OrthographicCamera();
        hud.setToOrtho(false, 720, 480);

        // b2d world init
        world = new World(new Vector2(0f, 0f), false);
        world.setContactListener(new WorldContactListener());
        rays = new RayHandler(world);
        rays.setAmbientLight(.4f);
        
        b2dr = new Box2DDebugRenderer();

        // Camera init stuff
        target = new Vector2(0, 0);
        rooms = new DungeonRoom[16][16];
        switches = new Array<FloorSwitch>();
        torches = new Array<Body>();

        // Room init
        pos = new Vector2(MathUtils.random(12) + 2, MathUtils.random(12) + 2);
        initRoom(0);

        // Player init
        player = new Player(world, rays);
    }

    @Override
    public void update(float delta) {
        world.step(1 / 60f, 6, 2);

        player.controller(delta);

        // Change camera track type
        if(Gdx.input.isTouched() || Gdx.input.isKeyJustPressed(Input.Keys.Y)) {
            createLamp(player.getPosition().scl(32));
            if (camera.zoom < 5) {
                camera.zoom += .1f;
            } else if(camera.zoom > 5) {
                camera.zoom -= .1f;
            }
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.A)) {
            cameraVal += 1;
            if(cameraVal > 3) {
                cameraVal = 0;
            }
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.B)) {
            addTorch(player.getPosition());
        }
        // Room Transition Check
        // Go Right
        if(player.getPosition().x > (target.x + 360) / PPM) {
            target.x += 720;
            pos.x += 1;
            if(rooms[(int)pos.x][(int)pos.y] == null) {
                initRoom(1);
            }
        }
        // Go Left
        if(player.getPosition().x < (target.x - 360) / PPM) {
            target.x -= 720;
            pos.x -= 1;
            if(rooms[(int)pos.x][(int)pos.y] == null) {
                initRoom(3);
            }
        }
        // Go Up
        if(player.getPosition().y > (target.y + 240) / PPM) {
            target.y += 480;
            pos.y += 1;
            if(rooms[(int)pos.x][(int)pos.y] == null) {
                initRoom(0);
            }
        }
        // Go Down
        if(player.getPosition().y < (target.y - 240) / PPM) {
            target.y -= 480;
            pos.y -= 1;
            if(rooms[(int)pos.x][(int)pos.y] == null) {
                initRoom(2);
            }
        }

        if(currentTorch != null) {
            currentTorch.setColor(.8f + r, g, .2f, .7f);
            currentTorch.update();
        }
        if(up) {
            r += .03;
            g = MathUtils.random(.7f) - .1f;
            if(r > .9f)
                up = !up;
        } else {
            r -= .03;
            if(r < .4f)
                up = !up;
        }
        cameraUpdate();
        batch.setProjectionMatrix(camera.combined);
        rays.setCombinedMatrix(camera.combined.cpy().scl(PPM));
    }

    private boolean up = true;
    private float r = 0.0f, g = 0.0f;

    @Override
    public void render() {
        Gdx.gl.glClearColor(.25f, .25f, .25f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        b2dr.render(world, camera.combined.cpy().scl(PPM));

        rooms[(int) pos.x][(int) pos.y].render(batch);
        rays.updateAndRender();
        player.render(batch);

        batch.setProjectionMatrix(hud.combined);
        batch.begin();
        font.draw(batch, cameraType, 100, 150);
        font.draw(batch, "Room: " + (pos.x + 1) + " " + (pos.y + 1), 100, 130);
        batch.end();
    }

    @Override
    public void dispose() {
        b2dr.dispose();
        world.dispose();
        player.dispose();
    }

    private void cameraUpdate() {
        camera.zoom = 1;
        switch(cameraVal) {
            case 0:
                CameraStyles.lockOnTarget(camera, target);
                cameraType = "Lock On - Room Center";
                break;
            case 1:
                CameraStyles.lerpToTarget(camera, target);
                cameraType = "Lerp - Room Center";
                break;
            case 2:
                CameraStyles.lerpAverageBetweenTargets(camera, target, player.getPosition().scl(PPM));
                cameraType = "Lock Average - Player/Room";
                break;
            case 3:
                boolean found = CameraStyles.searchFocalPoints(camera, rooms[(int) pos.x][(int) pos.y].getFocalPoints(), player.getPosition().scl(PPM), 180);
                if(!found) {
                    CameraStyles.lerpToTarget(camera, target);
                }
                cameraType = "Focal Search - Player/Center of Room";
                break;
        }
    }

    private void initRoom(int enter) {
        rooms[(int) pos.x][(int) pos.y] = new DungeonRoom(world, target, (int) pos.x, (int) pos.y);
        placeLock(enter);
    }

    private void placeLock(int enter) {
        /* Safety blocks */
        boolean l = false, r = false, u = false, d = false;
        if(pos.x + 1 > rooms.length - 1) {
            r = true;
            createBox(world, target.x + 335, target.y, 50, 76, true, true);
        }
        if (pos.x - 1 < 0) {
            l = true;
            createBox(world, target.x - 335, target.y, 50, 76, true, true);
        }

        if(pos.y + 1 > rooms[0].length - 1) {
            u = true;
            createBox(world, target.x, target.y + 215, 76, 50, true, true);
        }
        if (pos.y - 1 < 0) {
            d = true;
            createBox(world, target.x, target.y - 215, 76, 50, true, true);
        }

        /* Random Blocks */
        int percent = 30;
        if(enter != 0) {
            int random = MathUtils.random(100);
            if(random < 70) {
                rooms[(int)pos.x][(int)pos.y].createTestLock();
            }
        }
        if(enter != 1 && !l) {
            int random = MathUtils.random(100);
            if(random < percent) {
                createBox(world, target.x - 335, target.y, 50, 76, true, true);
            }
        }
        if(enter != 2 && !u) {
            int random = MathUtils.random(100);
            if(random < percent) {
                createBox(world, target.x, target.y + 215, 76, 50, true, true);
            }
        }
        if(enter != 3 && !r) {
            int random = MathUtils.random(100);
            if(random < percent) {
                createBox(world, target.x + 335, target.y, 50, 76, true, true);
            }
        }
    }

    private PointLight currentTorch;

    private void addTorch(Vector2 position) {
        Body b = BodyBuilder.createBox(world, position.x * PPM, position.y * PPM, 10, 10, false, false);
        b.setLinearDamping(2f);
        b.getFixtureList().get(0).setRestitution(.4f);
        Filter f = new Filter();
        f.categoryBits = Constants.BIT_SENSOR;
        f.maskBits = Constants.BIT_PLAYER | Constants.BIT_WALL;
        b.getFixtureList().get(0).setFilterData(f);
        currentTorch = new PointLight(rays, 20, new Color(.8f, .1f, .2f, .7f), 4, 0, 0);
        currentTorch.setSoftnessLength(0f);
        currentTorch.attachToBody(b);
    }
    private void addTorch(Body b) {
        b.setLinearDamping(2f);
        b.getFixtureList().get(0).setRestitution(.4f);
        Filter f = new Filter();
        f.categoryBits = Constants.BIT_SENSOR;
        f.maskBits = Constants.BIT_PLAYER | Constants.BIT_WALL;
        b.getFixtureList().get(0).setFilterData(f);
        currentTorch = new PointLight(rays, 20, new Color(.8f, .1f, .2f, .7f), 4, 0, 0);
        currentTorch.setSoftnessLength(0f);
        currentTorch.attachToBody(b);
    }
    private void createLamp(Vector2 position) {
        Body lamp = BodyBuilder.createCircle(world, position.x, position.y, 3, false, false,
                Constants.BIT_WALL, Constants.BIT_PLAYER, (short) 0);

        Body clamp = BodyBuilder.createBox(world, position.x, (position.y - 32), 3, 3, true, true,
                Constants.BIT_SENSOR, Constants.BIT_SENSOR, (short) 0);

        Body clamp2 = BodyBuilder.createBox(world, position.x, (position.y + 64), 3, 3, true, true,
                Constants.BIT_SENSOR, Constants.BIT_SENSOR, (short) 0);

        DistanceJointDef jDef = new DistanceJointDef();
        jDef.bodyA = clamp2;
        jDef.bodyB = lamp;
        jDef.collideConnected = false;
        jDef.length = 64f / PPM;
        world.createJoint(jDef);

        jDef.bodyA = clamp;
        jDef.dampingRatio = .85f;
        jDef.length = 3f / PPM;
        jDef.frequencyHz = 1.40f;
        world.createJoint(jDef);

        addTorch(lamp);
    }
}
