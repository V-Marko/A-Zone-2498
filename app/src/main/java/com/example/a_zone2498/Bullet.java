package com.example.a_zone2498;

public class Bullet {
    private int x, y;
    private int speed = 30;
    private boolean facingRight; // Added to track bullet direction

    public Bullet(int x, int y, boolean facingRight) {
        this.x = x;
        this.y = y;
        this.facingRight = facingRight;
    }

    public void update() {
        x += facingRight ? speed : -speed; // Move right or left based on direction
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isOffScreen(int screenWidth) {
        if (facingRight) {
            return x > screenWidth; // Off right edge
        } else {
            return x < 0; // Off left edge
        }
    }
}