package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PlayerActor implements Renderable {

    private Texture texture;
    private float x, y;
    private float speed = 20f;
    private float mapWidth, mapHeight;
    private Collision collision;

    public PlayerActor(float x, float y, float mapHeight, float mapWidth, Collision collision) {
        this.x = x;
        this.y = y;
        this.collision=collision;
        this.mapHeight=mapHeight;
        this.mapWidth=mapWidth;
        texture = new Texture("Image_s/Sprite_s/player.png");
    }

    public float getX(){
        return x;
    }

    public float getY(){
        return y;
    }

    @Override
    public float getBaseY() {
        return getY(); // feet position
    }

    public void setPosition(float X,float Y){
        this.x=X;
        this.y=Y;
    }

    public void update(float delta) {

        float nextX = x;
        float nextY = y;

        // movement
        if (Gdx.input.isKeyPressed(Input.Keys.W))
            nextY += speed * delta;

        if (Gdx.input.isKeyPressed(Input.Keys.S))
            nextY -= speed * delta;

        if (Gdx.input.isKeyPressed(Input.Keys.A))
            nextX -= speed * delta;

        if (Gdx.input.isKeyPressed(Input.Keys.D))
            nextX += speed * delta;

        // if (Gdx.input.isKeyPressed(Input.Keys.SPACE))
            // attack logic

        // --- TILED COLLISION ---
        // => only apply the move if the area is not blocked
        if (collision == null || !collision.isBlocked(nextX, nextY, 1f, 1f)) {
            x = nextX;
            y = nextY;
        }

        // --- MAP LIMITS ---
        float playerWidth = 1f;
        float playerHeight = 1f;

        // prevents the entity from leaving the map
        if (x < 0) x = 0;
        if (y < 0) y = 0;

        if (x + playerWidth > mapWidth)
            x = mapWidth - playerWidth;

        if (y + playerHeight > mapHeight)
            y = mapHeight - playerHeight;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, x, y, 1f, 1f);
    }
    public void dispose() {
        texture.dispose();
    }
}
