package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface Renderable {
    float getBaseY();              // depth anchor
    void render(SpriteBatch batch);

    default float getOcclusionMinY() {
        return getBaseY();
    }

    default float getOcclusionMaxY() {
        return getBaseY();
    }
}
