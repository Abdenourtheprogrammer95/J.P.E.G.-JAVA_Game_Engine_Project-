package com.mygdx.game;

import coding_project.JPEG.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

// Tiled imports
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;

public class GameScreen implements Screen {
    // Map
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;
    private Collision collision;

    // LibGDX core
    private final SpriteBatch batch;
    private OrthographicCamera camera;
    private Viewport viewport;

    public static final float WORLD_WIDTH  = 32f;
    public static final float WORLD_HEIGHT = 18f; // 16:9
    private int worldHeight;
    private int worldWidth;

    // Player
    private PlayerActor playerActor;

    // Enemies
    private final Array<Entity> enemies = new Array<>();
    private final List<EnemyActor> enemyActors = new ArrayList<>();

    // Levels
    private int currentLevelIndex = 0;
    private int enemiesKilled = 0;
    private Level currentLevel;
    private final List<Level> levels = new ArrayList<>();
    private boolean allLevelsCompleted = false;

    // Current wave state
    private final List<Wave> waves = new ArrayList<>();

    private TextureAtlas defaultAtlas;
    private TextureAtlas creeperAtlas;
    private TextureAtlas endermanAtlas;
    // private TextureAtlas phantomAtlas;
    private TextureAtlas skeletonAtlas;
    private TextureAtlas spiderAtlas;
    private TextureAtlas zombieAtlas;

    private EnumMap<EnemyType, TextureAtlas> enemyAtlases;

    private final List<Renderable> renderQueue = new ArrayList<>();
    private final List<Tree> trees = new ArrayList<>();

    public GameScreen() {
        batch = new SpriteBatch();
        initLevels();
    }

    private void initLevels() {
        levels.add(new Level("Map_s/DayMap_prairie_1st-attempt.tmx", 5));
        levels.add(new Level("Map_s/DayMap_snowy-prairie.tmx", 8));
        levels.add(new Level("Map_s/collision_ready--DayMap_prairie_1st-attempt.tmx", 5));
    }

    private void loadTreesFromMap() {
        MapLayer objectLayer = map.getLayers().get("Objects");
        if (objectLayer == null) {
            System.out.println("[ERROR] Objects layer not found!");
            return;
        }

        System.out.println("---- OBJECT LAYER DUMP ----");
        for (MapObject obj : objectLayer.getObjects()) {

            System.out.println(
                "Object class=" + obj.getClass().getSimpleName() +
                    ", name=" + obj.getName() +
                    ", type=" + obj.getProperties().get("type")
            );
        }
        System.out.println("--------------------------");

        var layer = map.getLayers().get("Objects");
        if (layer == null) {
            System.out.println("[DEBUG] Objects layer not found!");
            return;
        }

        int tileSize = map.getProperties().get("tilewidth", Integer.class);

        for (MapObject obj : layer.getObjects()) {
            // Safely get type, fall back to name if type is null
            String type = obj.getProperties().get("type", String.class);
            if (type == null) {
                type = obj.getName(); // If type is null, check the name
                System.out.println("[DEBUG] Type is null, fallback to name: " + type);
            }

            // Only proceed if it's a tree (adjust for possible casing issues)
            if (!"tree".equalsIgnoreCase(type)) continue;

            Float rootHeightProp = obj.getProperties().get("rootHeight", Float.class);
            float rootHeight = (rootHeightProp != null) ? rootHeightProp : 0f;

            Boolean flippedProp = obj.getProperties().get("flipped", Boolean.class);
            boolean flipped = (flippedProp != null) ? flippedProp : false;

            if (obj instanceof TiledMapTileMapObject) {
                TiledMapTileMapObject tileObj = (TiledMapTileMapObject) obj;
                TextureRegion region = tileObj.getTile().getTextureRegion();

                // Correctly scale coordinates by tile size with floating-point division
                float x = tileObj.getX() / (float) tileSize;
                float y = tileObj.getY() / (float) tileSize;
                float width  = region.getRegionWidth()  / (float) tileSize;
                float height = region.getRegionHeight() / (float) tileSize;
                float rotation = tileObj.getRotation();

                // Add tree to the list
                trees.add(new Tree(x, y, width, height, rootHeight, region, flipped, rotation));

                // Add debug to confirm the texture region is not null
                System.out.println("[TREE DEBUG] Tile tree loaded at " + x + "," + y);
            } else {
                System.err.println(
                    "[TREE DEBUG] Tree object is not a tile object: " +
                        obj.getClass().getSimpleName()
                );
            }
        }

        System.out.println("[DEBUG] Total trees loaded: " + trees.size());
    }

    private void loadLevel(int index) {
        disposeMap();

        enemies.clear();
        enemyActors.clear();
        enemiesKilled = 0;
        currentLevel = levels.get(index);

        // Load the map
        map = new TmxMapLoader().load(currentLevel.mapPath);

        int tileSize = map.getProperties().get("tilewidth", Integer.class);
        mapRenderer = new OrthogonalTiledMapRenderer(map, 1f/tileSize);

        // --- collision layers ---
        List<TiledMapTileLayer> collisionLayers = new ArrayList<>();
        for (String layerName : new String[]{"Collision__01", "Collision__02"}) {
            var layerObj = map.getLayers().get(layerName);
            if (layerObj instanceof TiledMapTileLayer) {
                collisionLayers.add((TiledMapTileLayer) layerObj);
            }
        }
        collision = collisionLayers.isEmpty() ? null : new Collision(collisionLayers);

        // v USER-ADDED DEBUGGING v
        System.out.println("[DEBUG] Collision layers found: " + collisionLayers.size());
        for (TiledMapTileLayer layer : collisionLayers) {
            System.out.println("[DEBUG] Collision layer name: " + layer.getName());
        }
        // ^ USER-ADDED DEBUGGING ^

        // --- world size in tiles ---
        worldWidth = map.getProperties().get("width", Integer.class) * tileSize;
        worldHeight = map.getProperties().get("height", Integer.class) * tileSize;

        // v USER-ADDED DEBUGGING v
        System.out.println("User-Added Debugging... Loading map: " + currentLevel.mapPath);
        System.out.println("Collision layers found: " + collisionLayers.size());
        // ^ USER-ADDED DEBUGGING ^

        // --- spawn player & enemies ---
        spawnPlayer();
        boolean spawnedFromMap = spawnEnemiesFromMap();
        if (!spawnedFromMap) {
            spawnRandomEnemies(20); // fallback
        }

        loadTreesFromMap();

        System.out.println("[DEBUG] Loaded map with tile size: " + map.getProperties().get("tilewidth", Integer.class));

        // v USER-ADDED DEBUGGING v
        System.out.println("[TILE INFO] worldWidth=" + worldWidth + " worldHeight=" + worldHeight);
        System.out.println("[TILE INFO] tileSize=" + map.getProperties().get("tilewidth", Integer.class));
        // ^ USER-ADDED DEBUGGING ^
    }

    private boolean isSpawnable(float x, float y) {
        if (collision == null) return true;
        return !collision.isBlocked(x, y, 1f, 1f);
    }

    /* Spawn enemies from Tiled map "Objects" layer.
       @return true if at least 1 enemy spawned from map, false otherwise */
    private boolean spawnEnemiesFromMap() {
        var layer = map.getLayers().get("Objects");
        if (layer == null) return false;

        boolean spawned = false;

        for (MapObject obj : layer.getObjects()) {
            if (!"enemy_spawn".equals(obj.getName())) continue;

            /* if (!"enemy_spawn".equalsIgnoreCase(
                obj.getProperties().get("type", String.class))) {
                continue;
             */

            // v USER-ADDED DEBUGGING v
            System.out.println("[DEBUG] Enemy spawn object found at x=" +
                obj.getProperties().get("x") + ", y=" + obj.getProperties().get("y") +
                ", type=" + obj.getProperties().get("type"));
            // ^ USER-ADDED DEBUGGING ^

            MapProperties props = obj.getProperties();

            int tileSize = map.getProperties().get("tilewidth", Integer.class);
            float x = props.get("x", Float.class) / tileSize;
            float y = props.get("y", Float.class) / tileSize;

            if (!isSpawnable(x, y)) {
                System.out.println("[DEBUG] Enemy spawn blocked at " + x + "," + y);
                continue;
            }

            String type = props.get("type", String.class);
            if (type == null) continue;

            try {
                Class<? extends Entity> clazz = resolveEnemyClass(type);
                Entity enemy = clazz.getDeclaredConstructor().newInstance();
                enemy.setXpos(x);
                enemy.setYpos(y);

                enemies.add(enemy);

                if (!(enemy instanceof Monster)) {
                    throw new IllegalStateException("Spawned enemy is not a Monster");
                }

                Monster monster = (Monster) enemy;
                TextureAtlas atlas = enemyAtlases.get(monster.getEnemyType());
                if (atlas==null) {
                    atlas = defaultAtlas;
                }
                enemyActors.add(new EnemyActor(enemy, atlas));

                spawned = true;

            } catch (ReflectiveOperationException e) {
                System.err.println("Failed to spawn enemy of type: " + type);
            }

            System.out.println("[DEBUG] Enemy spawn object found at x=" +
                obj.getProperties().get("x") + ", y=" + obj.getProperties().get("y") +
                ", type=" + obj.getProperties().get("type"));

        }
        return spawned;
    }

    /* Spawn enemies randomly in a top-right square (fallback for testing) */
    private void spawnRandomEnemies(int numEnemies) {
        for (int i = 0; i < numEnemies; i++) {
            Zombie enemy = new Zombie();

            float x, y;
            do {
                float enemySize = 1f;

                x = (float) Math.random() * (worldWidth  - enemySize);
                y = (float) Math.random() * (worldHeight - enemySize);

            } while (!isSpawnable(x, y));

            enemy.setXpos(x);
            enemy.setYpos(y);

            // v TEMPORARY — FOR TESTING v
            System.out.println("Spawned " + enemy.getClass().getSimpleName() +
                " at (" + enemy.getXpos() + ", " + enemy.getYpos() + ")");
            // ^ TEMPORARY — FOR TESTING ^

            enemies.add(enemy);

            if (enemy instanceof Monster) {
                Monster monster = enemy;
                TextureAtlas atlas = getAtlasesForMonster(monster);
                enemyActors.add(new EnemyActor(enemy, atlas));
            } else {
                // non-monster entity: fallback to default texture/atlas
                enemyActors.add(new EnemyActor(enemy, defaultAtlas));
            }
        }
    }

    private void initWaves() {
        waves.clear();
        waves.add(new Wave(50, true, Creeper.class));
        waves.add(new Wave(40, false, Enderman.class));
        waves.add(new Wave(40, true, Skeleton.class));
        waves.add(new Wave(20, true, Spider.class));
        waves.add(new Wave(100, true, Zombie.class));
    }

    private TextureAtlas getAtlasesForMonster(Monster monster) {
        EnemyType type = monster.getEnemyType();
        TextureAtlas atlas = enemyAtlases.get(type);

        if (atlas == null) {
            return defaultAtlas;
        }

        return atlas;
    }

    private void spawnWave(int waveIndex) {
        enemies.clear();
        enemyActors.clear();
        enemiesKilled = 0;

        Wave wave = waves.get(waveIndex);

        for (int i = 0; i < wave.getNumEnemies(); i++) {

            try {
                Entity enemy = wave.getEnemyType()
                    .getDeclaredConstructor()
                    .newInstance();

                float x, y;
                do {
                    x = (float) Math.random() * (worldWidth - 1);
                    y = (float) Math.random() * (worldHeight - 1);
                } while (!isSpawnable(x, y));

                enemy.setXpos(x);
                enemy.setYpos(y);

                enemies.add(enemy);

                if (enemy instanceof Monster) {
                    Monster monster = (Monster) enemy;
                    TextureAtlas atlas = getAtlasesForMonster(monster);
                    enemyActors.add(new EnemyActor(enemy, atlas));

                    // TEMP DEBUG
                    System.out.println("[DEBUG] Spawned enemyActor for " + enemy.getClass().getSimpleName() +
                        " at x=" + enemy.getXpos() + ", y=" + enemy.getYpos());

                } else {
                    // non-monster entity: fallback to default texture/atlas
                    enemyActors.add(new EnemyActor(enemy, defaultAtlas));

                    // TEMP DEBUG
                    System.out.println("[DEBUG] Spawned enemyActor for " + enemy.getClass().getSimpleName() +
                        " at x=" + enemy.getXpos() + ", y=" + enemy.getYpos());
                }

            } catch (ReflectiveOperationException e) {
                throw new RuntimeException(
                    "Failed to spawn enemy of type "
                        + wave.getEnemyType().getSimpleName(),
                    e
                );
            }
        }
    }

    private void spawnPlayer() {
        float[] spawn = getPlayerSpawn();

        // v USER-ADDED DEBUGGING v
        System.out.println("[DEBUG] Player spawn coordinates: x=" + spawn[0] + ", y=" + spawn[1]);
        if (collision != null) {
            System.out.println("[DEBUG] Is player spawn blocked? " +
                collision.isBlocked(spawn[0], spawn[1], 1f, 1f));
        }
        // ^ USER-ADDED DEBUGGING ^

        // visuals and rendering
        playerActor = new PlayerActor(spawn[0], spawn[1], worldHeight, worldWidth, collision);
    }

    private Class<? extends Entity> resolveEnemyClass(String type) {
        switch (type) {
            case "Creeper":  return Creeper.class;
            case "Enderman":  return Enderman.class;
            case "Skeleton":   return Skeleton.class;
            case "Spider":   return Spider.class;
            case "Zombie":   return Zombie.class;
            default:
                throw new IllegalArgumentException("Unknown enemy type: " + type);
        }
    }

    private void disposeMap() {
        if (mapRenderer != null) mapRenderer.dispose();
        if (map != null) map.dispose();
    }

    private void update(float delta) {
        GameScreenDebug.frame++;

        if (playerActor == null || playerActor.getLogic().isDead()) {
            return;
        }

        if (allLevelsCompleted) return; // stop updating after last level

        // updates player visuals
        playerActor.update(delta);

        for (int i = enemies.size-1; i >= 0; i--) {
            Entity enemy = enemies.get(i);

            if (enemy instanceof Monster) {
                // triggers the most up-to-date state (idle, walking, chasing, ...)

                Monster m = (Monster) enemy;
                LivingEntity playerTarget = playerActor.getLogic();
                // if (playerTarget == null) continue;

                enemy.update(delta);

                // Update AI (decides state ONLY)
                m.updateAI(playerTarget, enemy);

                // Execute state behavior (issues movement OR attack OR stun)
                enemy.getCurrentState().behave(enemy, playerTarget, delta);

                // v USER-ADDED DEBUGGING v
                System.out.println(
                    "[COLLISION MOVE] frame=" + GameScreenDebug.frame +
                        " enemy=" + enemy.getClass().getSimpleName() +
                        " dx=" + (enemy.getMoveDX()) +
                        " dy=" + (enemy.getMoveDY())
                );
                // ^ USER-ADDED DEBUGGING ^

                // Apply movement ONCE if requested
                if (enemy.canMove() && collision != null) {
                    collision.move(enemy, delta);
                }
            }

            // condition for enemy removal
            if (enemy.isMarkedForRemoval()) {
                enemies.removeIndex(i);
                enemyActors.remove(i);
                enemiesKilled++;
                continue;
            }

            // visuals for each "monster"
            enemyActors.get(i).update(delta, playerActor.getX());

            // v USER-ADDED DEBUGGING v
            System.out.printf(
                "[DEBUG] frame=%d, enemy=%s, logicState=%s, attacking=%b, attackTimer=%.2f, animState=%s, stateTime=%.2f%n",
                GameScreenDebug.frame,
                enemy.getName(),
                enemy.getCurrentState(),
                enemy.isAttacking(),
                enemy.getAttackTimer(),
                enemyActors.get(i).getCurrentState(),
                enemyActors.get(i).getStateTime()
            );
            // ^ USER-ADDED DEBUGGING ^
        }

        // checks level completion
        if (!allLevelsCompleted && enemiesKilled >= currentLevel.enemiesToClear) {
            goToNextLevel();
        }
    }

    private void goToNextLevel() {
        currentLevelIndex++;

        if (currentLevelIndex >= levels.size()) {
            System.out.println("All levels completed!");
            allLevelsCompleted = true;  // <-- prevent further updates
            return;
        }

        loadLevel(currentLevelIndex);
    }

    @Override
    public void show() {
        enemyAtlases = new EnumMap<>(EnemyType.class);

        enemyAtlases.put(EnemyType.CREEPER,
            new TextureAtlas("Image_s/Sprite_s/Creeper/CAtlas/Creeper.atlas"));
        enemyAtlases.put(EnemyType.ENDERMAN,
            new TextureAtlas("Image_s/Sprite_s/Enderman/EAtlas/Enderman.atlas"));
        //enemyAtlases.put(EnemyType.PHANTOM,
            //new TextureAtlas("Image_s/Sprite_s/Phantom/PAtlas/Phantom.atlas"));
        enemyAtlases.put(EnemyType.SKELETON,
            new TextureAtlas("Image_s/Sprite_s/Skeleton/SAtlas/Skeleton.atlas"));
        enemyAtlases.put(EnemyType.SPIDER,
            new TextureAtlas("Image_s/Sprite_s/Spider/SAtlas/Spider.atlas"));
        enemyAtlases.put(EnemyType.ZOMBIE,
            new TextureAtlas("Image_s/Sprite_s/Zombie/ZAtlas/Zombie.atlas"));

        // v TEMPORARY — FOR TESTING v
        for (EnemyType type : EnemyType.values()) {
            TextureAtlas atlas = enemyAtlases.get(type);
            System.out.println(type + " atlas loaded? " + (atlas != null));
            if (atlas != null) {
                System.out.println("Regions: " + atlas.getRegions().size);
            }
        }

        defaultAtlas = new TextureAtlas("Image_s/Sprite_s/Zombie/ZAtlas/Zombie.atlas");

        zombieAtlas = enemyAtlases.get(EnemyType.ZOMBIE);
        System.out.println("Zombie regions:");
        for (TextureAtlas.AtlasRegion r : zombieAtlas.getRegions()) {
            System.out.println(" - " + r.name);
        }
        // ^ TEMPORARY — FOR TESTING ^

        loadLevel(0); // 1st level loading (map)
        initWaves();
        spawnWave(0);

        // v TEMPORARY — FOR TESTING v
        System.out.println("Available layers:");
        map.getLayers().forEach(layer ->
            System.out.println(" - " + layer.getName())
        );
        // ^ TEMPORARY — FOR TESTING ^

        camera = new OrthographicCamera();
        viewport = new ExtendViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        viewport.apply(true);

        camera.position.set(WORLD_WIDTH / 2f, WORLD_HEIGHT / 2f, 0);
        camera.update();
    }

    @Override
    public void render(float delta) { // called 60x/s
        /* // v USER-ADDED DEBUGGING v
        System.out.println("[RENDER DEBUG] renderQueue size=" + renderQueue.size());
        for (Renderable r : renderQueue) {
            System.out.println(" - " + r.getClass().getSimpleName() + " baseY=" + r.getBaseY());
        }
        // ^ USER-ADDED DEBUGGING ^ */

        update(delta);

        // dead-zone's size
        float deadZoneWidth = 8f;
        float deadZoneHeight = 5f;

        // an intelligent mechanism that allows the camera to be positioned correctly and follow the player
        float leftLimit = camera.position.x - deadZoneWidth / 2;
        float rightLimit = camera.position.x + deadZoneWidth / 2;
        float bottomLimit = camera.position.y - deadZoneHeight / 2;
        float topLimit = camera.position.y + deadZoneHeight / 2;

        // if the player goes past the right edge
        if (playerActor.getX() > rightLimit) {
            camera.position.x = playerActor.getX() - deadZoneWidth / 2;
        }

        // if the player goes past the left edge
        if (playerActor.getX() < leftLimit) {
            camera.position.x = playerActor.getX() + deadZoneWidth / 2;
        }

        // if the player goes above the top
        if (playerActor.getY() > topLimit) {
            camera.position.y = playerActor.getY() - deadZoneHeight / 2;
        }

        // if the player goes below the bottom
        if (playerActor.getY() < bottomLimit) {
            camera.position.y = playerActor.getY() + deadZoneHeight / 2;
        }

        /* then limits the camera to the edges of the map
           OVERSCROLL (edit to change the visible margin) */
        float overscroll = 2f;

        // strict map boundaries
        float halfW = camera.viewportWidth / 2;
        float halfH = camera.viewportHeight / 2;

        // boundaries with overscroll
        float minX = halfW - overscroll;
        float maxX = worldWidth - halfW + overscroll;

        float minY = halfH - overscroll;
        float maxY = worldHeight - halfH + overscroll;

        // applies limits
        if (camera.position.x < minX) camera.position.x = minX;
        if (camera.position.x > maxX) camera.position.x = maxX;

        if (camera.position.y < minY) camera.position.y = minY;
        if (camera.position.y > maxY) camera.position.y = maxY;

        camera.update();

        Gdx.gl.glClearColor(0.4f, 0.6f, 1f, 1f); // sky blue

        // clears the screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // shows map
        mapRenderer.setView(camera);
        mapRenderer.render();

        /* For coordinate-based smart rendering: the player passes in front of the tree when lower than the trunk root
           and walks behind it when his altitude is higher than the root */
        renderQueue.clear();
        renderQueue.addAll(trees);

        // System.out.println("[DEBUG] Total trees loaded: " + trees.size());
        // System.out.println("[RENDER DEBUG] renderQueue size=" + renderQueue.size());

        renderQueue.add(playerActor);
        renderQueue.addAll(enemyActors);

        // comparing the altitude for drawing order
        renderQueue.sort(Comparator.comparing(Renderable::getBaseY).reversed());

        // displays player + enemies
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        for (Renderable r : renderQueue) {
            r.render(batch);
        }

        batch.end();
    }

    // ===== GET PLAYER SPAWN FROM TMX =====
    private float[] getPlayerSpawn() {
        var objectLayer = map.getLayers().get("Objects");
        if (objectLayer == null) return new float[]{2, 2};

        var spawn = objectLayer.getObjects().get("player_spawn");
        if (spawn == null) return new float[]{2, 2};

        int tileSize = map.getProperties().get("tilewidth", Integer.class);

        float x = spawn.getProperties().get("x", Float.class) / tileSize;
        float y = spawn.getProperties().get("y", Float.class) / tileSize;

        return new float[]{x, y};
    }

    @Override
    public void dispose() {
        if (map != null) map.dispose();
        if (mapRenderer != null) mapRenderer.dispose();
        if (batch != null) batch.dispose();
        if (playerActor != null) playerActor.dispose();

        // EnemyActor holds no disposable resources
        enemies.clear();
        enemyActors.clear();

        // Dispose texture atlases
        for (TextureAtlas atlas : enemyAtlases.values()) {
            atlas.dispose();
        }
    }

    public int getHeight() {
        return worldHeight;
    }

    public int getWidth() {
        return worldWidth;
    }

    @Override
    public void resize(int width, int height) {
        if (viewport != null) {
            viewport.update(width, height, true);
        }
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }
}
