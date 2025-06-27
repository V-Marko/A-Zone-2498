package com.example.a_zone2498;

public class Bullet {
    private int x, y;
    private int speed = 30;

    public Bullet(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void update() {
        x += speed;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isOffScreen(int screenWidth) {
        return x > screenWidth;
    }
}
