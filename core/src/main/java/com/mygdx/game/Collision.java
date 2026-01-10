package com.mygdx.game;

import coding_project.JPEG.Entity;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import java.util.List;

public class Collision {

    private final List<TiledMapTileLayer> collisionLayers;
    private final float tileWidth;
    private final float tileHeight;
    private TiledMap map;

    public Collision(List<TiledMapTileLayer> layers) {
        this.collisionLayers = layers;

        // Assume all layers share the same tile size (normal in Tiled)
        this.tileWidth  = layers.get(0).getTileWidth();
        this.tileHeight = layers.get(0).getTileHeight();
    }

    private boolean isCellBlocked(TiledMapTileLayer layer, int x, int y) {
        if (x < 0 || y < 0 ||
            x >= layer.getWidth() ||
            y >= layer.getHeight()) {
            return true; // outside map = blocked
        }

        TiledMapTileLayer.Cell cell = layer.getCell(x, y);
        if (cell == null) return false;

        var tile = cell.getTile();
        if (tile == null) return false;

        MapProperties props = tile.getProperties();

        return props.containsKey("collidable") &&
            props.get("collidable", Boolean.class);
    }

    public boolean isBlocked(float x, float y, float width, float height) {

        int tileX1 = (int) x;
        int tileY1 = (int) y;
        int tileX2 = (int) (x + width  - 0.001f);
        int tileY2 = (int) (y + height - 0.001f);


        for (TiledMapTileLayer layer : collisionLayers) {
            if (isCellBlocked(layer, tileX1, tileY1) ||
                isCellBlocked(layer, tileX2, tileY1) ||
                isCellBlocked(layer, tileX1, tileY2) ||
                isCellBlocked(layer, tileX2, tileY2)) {
                return true;
            }
        }
        return false;
    }

    public void move(Entity e, float delta) {
        float dx = e.getMoveDX() * e.getMoveSpeed() * delta, dy = e.getMoveDY() * e.getMoveSpeed() * delta,
            newX = e.getXpos() + dx, newY = e.getYpos() + dy,
            w = e.getCollisionWidth(), h = e.getCollisionHeight();

        // X movement
        if (!isBlocked(newX, e.getYpos(), w, h)) {
            e.setXpos(newX);
        }

        // Y movement
        if (!isBlocked(e.getXpos(), newY, w, h)) {
            e.setYpos(newY);
        }

        //e.clearMoveRequest();
    }
}
