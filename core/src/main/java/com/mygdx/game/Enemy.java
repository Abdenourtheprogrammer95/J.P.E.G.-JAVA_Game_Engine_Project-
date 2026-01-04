package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Enemy {

    private Texture texture;
    private float x, y;
    private float speed = 100f;
    private float width, height;
    private int health =50;
    private float aggroRange = 200f;
    private boolean active = false;
    private boolean aggro;

    public Enemy(float x, float y, boolean aggro) {
        this.x = x;
        this.y = y;
        this.aggro= aggro;
        texture = new Texture("Image_s/Sprite_s/Enemy.png");
        width = texture.getWidth();
        height = texture.getHeight();
    }

    public void update(float delta, PlayerActor player) {
        if (!aggro) return; // enemy static

        float dx = player.getX() - x, dy = player.getY() - y, distance = (float) Math.sqrt(dx*dx + dy*dy);

        if (distance < aggroRange) {
            active = true;
        }

        if (active) {
            x += (dx / distance) * speed * delta;
            y += (dy / distance) * speed * delta;
        }
    }

    public void dispose() {
        texture.dispose();
    }
    public float getX() { return x; }

    public float getY() { return y; }

    public void setPosition(float X,float Y){
        this.x=X;
        this.y=Y;
    }
    public Rectangle getBounds() {
        return new Rectangle(x, y, texture.getWidth(), texture.getHeight());
    }


    public void takeDamage(int dmg) {
        health -= dmg;
    }

    public boolean isDead() {
        return health <= 0;
    }

    public boolean shouldBeRemoved() {
        return y < -texture.getHeight();
    }

    public int getWidth() {
        return texture.getWidth();
    }

    public int getHeight() {
        return texture.getHeight();
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, x, y);
    }

}
