package com.cnnranderson.tutorial;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.cnnranderson.tutorial.managers.GameStateManager;
import com.cnnranderson.tutorial.utils.TiledObjectUtil;

import static com.cnnranderson.tutorial.utils.Constants.PPM;

public class Application extends ApplicationAdapter {

    // DEBUG
    public static boolean DEBUG = false;

    // Game Information
    public static final String TITLE = "Tutorial";
    public static final int V_WIDTH = 720;
    public static final int V_HEIGHT = 480;
    public static final float SCALE = 2.0f;

    private OrthographicCamera camera;
    private SpriteBatch batch;

    private GameStateManager gsm;

    private OrthogonalTiledMapRenderer tmr;
    private TiledMap map;

    private Box2DDebugRenderer b2dr;
    private World world;
    private Body player;

    private Texture tex;

    @Override
    public void create() {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        batch = new SpriteBatch();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, w / SCALE, h / SCALE);

        gsm = new GameStateManager(this);

        /*
        world = new World(new Vector2(0f, -9.8f), false);
        b2dr = new Box2DDebugRenderer();

        player = createBox(140, 140, 32, 32, false, false);

        tex = new Texture("Images/cat.png");

        map = new TmxMapLoader().load("Maps/test_map.tmx");
        tmr = new OrthogonalTiledMapRenderer(map);

        TiledObjectUtil.parseTiledObjectLayer(world, map.getLayers().get("collision-layer").getObjects());
        */
    }

    @Override
    public void render() {
        gsm.update(Gdx.graphics.getDeltaTime());
        gsm.render();

        /*
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(Gdx.graphics.getDeltaTime());

        batch.begin();
        batch.draw(tex, player.getPosition().x * PPM - (tex.getWidth() / 2), player.getPosition().y * PPM - (tex.getHeight() / 2));
        batch.end();

        tmr.render();

        b2dr.render(world, camera.combined.scl(PPM));
        */

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) Gdx.app.exit();
    }

    @Override
    public void resize(int width, int height) {
        gsm.resize((int) (width / SCALE), (int) (height / SCALE));
    }

    @Override
    public void dispose() {
        gsm.dispose();
        batch.dispose();
    }

    public void update(float delta) {
        /*
        world.step(1 / 60f, 6, 2);

        cameraUpdate(delta);
        tmr.setView(camera);
        batch.setProjectionMatrix(camera.combined);
        */
    }

    public void cameraUpdate(float delta) {
        Vector3 position = camera.position;
        position.x = camera.position.x + (player.getPosition().x * PPM - camera.position.x) * .1f;
        position.y = camera.position.y + (player.getPosition().y * PPM - camera.position.y) * .1f;
        camera.position.set(position);

        camera.update();
    }

    public Body createBox(int x, int y, int width, int height, boolean isStatic, boolean rest) {
        Body pBody;
        BodyDef def = new BodyDef();

        if(isStatic)
            def.type = BodyDef.BodyType.StaticBody;
        else
            def.type = BodyDef.BodyType.DynamicBody;

        def.position.set(x / PPM, y / PPM);
        def.fixedRotation = true;
        pBody = world.createBody(def);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2 / PPM, height / 2 / PPM);

        if(rest) {
            pBody.createFixture(shape, 1.0f).setRestitution(1f);
        } else {
            pBody.createFixture(shape, 1.0f);
        }
        shape.dispose();
        return pBody;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }
    public SpriteBatch getBatch() {
        return batch;
    }
}
