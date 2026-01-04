package com.mygdx.game;

import coding_project.JPEG.Direction;
import coding_project.JPEG.EnemyState;
import coding_project.JPEG.Entity;

import coding_project.JPEG.Monster;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import java.util.EnumMap;

public class EnemyActor implements Renderable {
    private final Entity enemyLogic; // e.g., Zombie, Spider

    // enemy rendering
    private EnemyState currentState;
    private EnumMap<EnemyState, Animation<TextureRegion>> animations;
    private Direction facing = Direction.RIGHT;
    private float stateTime = 0f;
    private Animation<TextureRegion> currentAnimation;

    private static final float PIXELS_PER_TILE = 16f;

    @Override
    public float getBaseY() {
        return (float) enemyLogic.getYpos(); // bottom of hitbox
    }

    public EnemyActor(Entity enemyLogic, TextureAtlas atlas) {
        this.enemyLogic = enemyLogic;
        // Wrap the texture in a single-frame TextureRegion, then create a one-frame Animation
        animations = new EnumMap<>(EnemyState.class);

        // TEMP DEBUG: verify animation regions
        System.out.println("[DEBUG] Animation check for " + enemyLogic.getClass().getSimpleName());

        for (EnemyState state : EnemyState.values()) {
            // String prefix = prefixForState(state);
            Monster m = (Monster) enemyLogic;
            String prefix = m.getAnimationPrefix(state);

            Array<TextureAtlas.AtlasRegion> regions = atlas.findRegions(prefix);

            // TEMP DEBUG: verify animation regions
            int count = (regions == null ? 0 : regions.size);
            System.out.println("  State " + state + " -> prefix='" + prefix + "' -> regions found: " + count);

            if (regions != null && regions.size > 0) {
                animations.put(
                    state,
                    new Animation<>(0.1f, regions, Animation.PlayMode.LOOP)
                );
            }
        }

        // v ——TEMPORARY FOR TESTING—— v
        if (animations.isEmpty()) {
            throw new IllegalStateException(
                "No animations loaded for " +
                    enemyLogic.getClass().getSimpleName()
            );
        }

        System.out.println("EnemyActor created for " +
            enemyLogic.getClass().getSimpleName());
        System.out.println("Animations loaded: " + animations.keySet());
        // ^ ——TEMPORARY FOR TESTING—— ^

        currentState = enemyLogic.getCurrentState();
        currentAnimation = animations.get(currentState);

        // v ——TEMPORARY FOR TESTING—— v
        System.out.println("Initial state: " + currentState);
        // ^ ——TEMPORARY FOR TESTING—— ^
    }

    public void update(float delta, float playerX) {
        stateTime += delta;

        // UNSURE ABOUT THE FOLLOWING BLOCK
        float enemyCenterX = (float)
            enemyLogic.getXpos() + enemyLogic.getCollisionWidth() / 2f;

        facing = (playerX > enemyCenterX)
            ? Direction.RIGHT
            : Direction.LEFT;

        EnemyState logicState = enemyLogic.getCurrentState();
        if (logicState != currentState) {
            setAnimationForState(logicState);
            currentState = logicState;
            stateTime = 0f; // reset animation on state change
        }
    }

    private void setAnimationForState(EnemyState state) {
        currentAnimation = animations.get(state);
    }

    public void render(SpriteBatch batch) {

        // ^ ——TEMPORARY FOR TESTING—— ^
        if (currentState == null)
            System.out.println("WARNING: currentState is null");
        // ^ ——TEMPORARY FOR TESTING—— ^

        if (currentAnimation == null) return;

        TextureRegion frame = currentAnimation.getKeyFrame(stateTime);

        float spriteW = frame.getRegionWidth()  / PIXELS_PER_TILE;
        float spriteH = frame.getRegionHeight() / PIXELS_PER_TILE;

// collision box
        float colW = enemyLogic.getCollisionWidth();
        float colH = enemyLogic.getCollisionHeight();

// align sprite to collision box center
        float x = (float) enemyLogic.getXpos() + colW / 2f - spriteW / 2f;
        float y = (float) enemyLogic.getYpos() + colH / 2f - spriteH / 2f;

        float originX = spriteW / 2f;
        float originY = spriteH / 2f;

        float scaleX = (facing == Direction.LEFT) ? -1f : 1f;

        batch.draw(
            frame,
            x, y,
            originX, originY,
            spriteW, spriteH,
            scaleX, 1f,
            0f
        );
    }
}
