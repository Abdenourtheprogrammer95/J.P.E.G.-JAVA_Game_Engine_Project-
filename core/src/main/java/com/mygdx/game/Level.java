package com.mygdx.game;

public class Level {
    public final String mapPath;
    public final int enemiesToClear;

    public Level(String mapPath, int enemiesToClear) {
        this.mapPath = mapPath;
        this.enemiesToClear = enemiesToClear;
    }
}
