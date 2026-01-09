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
    private final EnumMap<EnemyState, Animation<TextureRegion>> animations;
    private Direction facing = Direction.RIGHT;
    private float stateTime = 0f;
    private Animation<TextureRegion> currentAnimation;

    private static final float PIXELS_PER_TILE = 16f;

    @Override
    public float getBaseY() {
        return (float) enemyLogic.getYpos(); // bottom of hitbox
    }

    public EnemyState getCurrentState() { return currentState; }
    public float getStateTime() { return stateTime; }

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

            // Try to find regions with numbered suffixes (e.g., zombie__walk--01, zombie__walk--02, etc.)
            int i = 1;
            while (true) {
                // Generate the name with the current suffix
                String regionName = prefix + "--" + String.format("%02d", i);

                // Look for the region by name
                TextureAtlas.AtlasRegion region = atlas.findRegion(regionName);
                if (region == null) {
                    break; // No more matching regions, break out of the loop
                }

                // Add the found region to the list
                regions.add(region);

                i++; // Increment the suffix number
            }

            // Fallback: single-frame animation (idle, etc.)
            if (regions.isEmpty()) {
                TextureAtlas.AtlasRegion single = atlas.findRegion(prefix);
                if (single != null) {
                    regions.add(single);
                }
            }

            Animation.PlayMode mode =
                (state == EnemyState.ATTACK ||
                    state == EnemyState.HURT  ||
                    state == EnemyState.DEAD)
                    ? Animation.PlayMode.NORMAL
                    : Animation.PlayMode.LOOP;

            animations.put(
                state,
                new Animation<>(0.1f, regions, mode)
            );
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

        System.out.println(
            "[DEBUG] Initial enemyLogic state = " + enemyLogic.getCurrentState()
        );
    }

    public void update(float delta, float playerX) {
        stateTime += delta;

        float enemyCenterX = enemyLogic.getXpos() + enemyLogic.getCollisionWidth() / 2f;

        facing = (playerX > enemyCenterX)
            ? Direction.RIGHT
            : Direction.LEFT;

        EnemyState logicState = enemyLogic.getCurrentState();
        if (logicState != currentState) {
            setAnimationForState(logicState);
            currentState = logicState;
            stateTime = 0f; // reset animation on state change
        }

        /*
        // Checks for non-looping animation finished
        if (currentAnimation != null &&
            currentAnimation.getPlayMode() == Animation.PlayMode.NORMAL &&
            currentAnimation.isAnimationFinished(stateTime)) {

            if (currentState == EnemyState.DEAD) {
                // Tell game world to remove this enemy
                enemyLogic.markForRemoval();
            }
        }
         */

        // v USER-ADDED DEBUGGING v
        if ((int)(stateTime * 10) % 10 == 0) {
            System.out.println(
                "[ACTOR] " + enemyLogic.getName() +
                    " logicState=" + enemyLogic.getCurrentState() +
                    " animState=" + currentState +
                    " pos=(" + enemyLogic.getXpos() + "," + enemyLogic.getYpos() + ")"
            );
        }
        // ^ USER-ADDED DEBUGGING ^
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
        float x = enemyLogic.getXpos() + colW / 2f - spriteW / 2f;
        float y = enemyLogic.getYpos() + colH / 2f - spriteH / 2f;

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
