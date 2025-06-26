package com.example.a_zone2498;

public class Player {
    private int x, y;
    public float speed = 15;
    public float jumpSpeed = 20;
    public float jumpLimit = 550;
    private float jumpHeight = 250;

    private int wheelRotation = 0;
    private boolean movingLeft = false;
    private boolean movingRight = false;
    private boolean isJumping = false;
    private int jumpY = 0;
    private int groundY;

    public Player(int x, int groundY) {
        this.x = x;
        this.groundY = groundY;
        this.y = groundY;
    }

    public void moveLeft(boolean start) {
        movingLeft = start;
    }

    public void moveRight(boolean start) {
        movingRight = start;
    }

    public void jump() {
        if (!isJumping) {
            isJumping = true;
            jumpY = 0;
        }
    }

    public void update() {
        if (movingLeft) {
            x -= speed;
            wheelRotation -= 15;
        }
        if (movingRight) {
            x += speed;
            wheelRotation += 15;
        }
        if (isJumping) {
            jumpY += jumpSpeed;
            y = groundY - (int)(jumpHeight * Math.sin(Math.PI * jumpY / jumpLimit));
            if (jumpY >= jumpLimit) {
                isJumping = false;
                y = groundY;
                jumpY = 0;
            }
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWheelRotation() {
        return wheelRotation;
    }
}
