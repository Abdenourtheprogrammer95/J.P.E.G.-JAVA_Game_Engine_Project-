package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Tree implements Renderable {

    private final float x, y;          // bottom-left in world units
    private final float width, height; // sprite size in world units
    private final float rootHeight;    // trunk height
    private final TextureRegion sprite;
    private final boolean flipped;
    private final float rotation;

    public Tree(float x, float y, float width, float height, float rootHeight,
        TextureRegion sprite, boolean flipped, float rotation) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.rootHeight = rootHeight;
        this.sprite = sprite;
        this.flipped = flipped;
        this.rotation = rotation;
    }

    @Override
    public float getBaseY() {
        // Fallen tree → lies on the ground
        if (rotation != 0f) {
            return y + height*0.5f;
        }

        // Standing tree → trunk root
        return y + rootHeight;
    }

    @Override
    public float getOcclusionMinY() {
        if (rotation != 0f) {
            return y + height * 0.25f;   // bottom of trunk band
        }
        return getBaseY();
    }

    @Override
    public float getOcclusionMaxY() {
        if (rotation != 0f) {
            return y + height * 0.75f;   // top of trunk band
        }
        return getBaseY();
    }

    @Override
    public void render(SpriteBatch batch) {
        System.out.println("[TREE DEBUG] x=" + x + " y=" + y + " width=" + width + " height=" + height +
            " rotation=" + rotation + " flipped=" + flipped);

        System.out.println("[TREE DEBUG] BaseY (before adjustment): " + getBaseY());

        float drawX = x;
        float drawY = y;

        float originX = width / 2f;
        float originY = height / 2f;

        // This case compensates for Tiled rotation (otherwise my fallen tree would have drift upwards)
        if (rotation != 0f) {
            drawY -= (height - width) / 0.50f;
            drawX -= (width - height) / 2f;
        }

        System.out.println("[TREE DEBUG] Adjusted drawY: " + drawY);

        batch.draw(
            sprite,
            drawX,
            drawY,
            originX,
            originY,
            width,
            height,
            flipped ? -1f : 1f,
            1f,
            -rotation
        );

        System.out.println("[TREE DEBUG] Final position: x=" + drawX + " y=" + drawY);
    }


    public float getHeight() {
        return height;
    }

    public float getY() {
        return y;
    }

    public float getRotation() {
        return rotation;
    }

    public boolean getFlipped() {
        return flipped;
    }
}
