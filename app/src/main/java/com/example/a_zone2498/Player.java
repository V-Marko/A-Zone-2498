package com.example.a_zone2498;

import java.util.ArrayList;

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
    private ArrayList<Bullet> bullets = new ArrayList<>();
    private boolean isRecoiling = false;
    private int recoilTimer = 0;
    private final int RECOIL_DURATION = 10;
    private final int RECOIL_OFFSET_MAX_SHOOT1 = 10;
    private final int RECOIL_OFFSET_MAX_SHOOT2 = 20;
    private final int RECOIL_OFFSET_MAX_HEAD1 = 20;
    private final int RECOIL_OFFSET_MAX_HEAD2 = 20;
    private final float RECOIL_ROTATION_MAX = 10;
    private final int RECOIL_BACKWARD_MAX = 10;
    private float currentRecoilOffsetShoot1 = 0;
    private float currentRecoilOffsetShoot2 = 0;
    private float currentRecoilOffsetHead1 = 0;
    private float currentRecoilOffsetHead2 = 0;
    private float currentRecoilRotation = 0;
    private float currentRecoilBackward = 0;
    private int initialX;

    public Player(int x, int groundY) {
        this.x = x;
        this.groundY = groundY;
        this.y = groundY;
        this.initialX = x;
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

    public void shoot() {
        bullets.add(new Bullet(x + 400, y - 135));
        bullets.add(new Bullet(x + 390, y - 200));
        isRecoiling = true;
        recoilTimer = 0;
        initialX = x;
        currentRecoilBackward = RECOIL_BACKWARD_MAX;
    }

    public void update() {
        if (movingLeft) {
            x -= speed;
            wheelRotation -= 15;
            initialX = x;
        }
        if (movingRight) {
            x += speed;
            wheelRotation += 15;
            initialX = x;
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
        if (isRecoiling) {
            recoilTimer++;
            float progress = (float) recoilTimer / RECOIL_DURATION;
            if (progress < 0.5f) {
                currentRecoilOffsetShoot1 = RECOIL_OFFSET_MAX_SHOOT1 * (progress * 2);
                currentRecoilOffsetShoot2 = RECOIL_OFFSET_MAX_SHOOT2 * (progress * 2);
                currentRecoilOffsetHead1 = RECOIL_OFFSET_MAX_HEAD1 * (progress * 2);
                currentRecoilOffsetHead2 = RECOIL_OFFSET_MAX_HEAD2 * (progress * 2);
                currentRecoilRotation = RECOIL_ROTATION_MAX * (progress * 2);
            } else {
                currentRecoilOffsetShoot1 = RECOIL_OFFSET_MAX_SHOOT1 * (1 - (progress - 0.5f) * 2);
                currentRecoilOffsetShoot2 = RECOIL_OFFSET_MAX_SHOOT2 * (1 - (progress - 0.5f) * 2);
                currentRecoilOffsetHead1 = RECOIL_OFFSET_MAX_HEAD1 * (1 - (progress - 0.5f) * 2);
                currentRecoilOffsetHead2 = RECOIL_OFFSET_MAX_HEAD2 * (1 - (progress - 0.5f) * 2);
                currentRecoilRotation = RECOIL_ROTATION_MAX * (1 - (progress - 0.5f) * 2);
            }
            currentRecoilBackward = RECOIL_BACKWARD_MAX * (1 - progress);
            x = initialX - (int) currentRecoilBackward;
            if (recoilTimer >= RECOIL_DURATION) {
                isRecoiling = false;
                currentRecoilOffsetShoot1 = 0;
                currentRecoilOffsetShoot2 = 0;
                currentRecoilOffsetHead1 = 0;
                currentRecoilOffsetHead2 = 0;
                currentRecoilRotation = 0;
                currentRecoilBackward = 0;
                x = initialX;
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

    public ArrayList<Bullet> getBullets() {
        return bullets;
    }

    public boolean isRecoiling() {
        return isRecoiling;
    }

    public float getRecoilOffsetShoot1() {
        return currentRecoilOffsetShoot1;
    }

    public float getRecoilOffsetShoot2() {
        return currentRecoilOffsetShoot2;
    }

    public float getRecoilOffsetHead1() {
        return currentRecoilOffsetHead1;
    }

    public float getRecoilOffsetHead2() {
        return currentRecoilOffsetHead2;
    }

    public float getRecoilRotation() {
        return currentRecoilRotation;
    }

    public void updateBullets(int screenWidth) {
        ArrayList<Bullet> toRemove = new ArrayList<>();
        for (Bullet bullet : bullets) {
            bullet.update();
            if (bullet.isOffScreen(screenWidth)) {
                toRemove.add(bullet);
            }
        }
        bullets.removeAll(toRemove);
    }
}