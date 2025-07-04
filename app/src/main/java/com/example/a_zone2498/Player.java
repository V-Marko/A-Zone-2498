package com.example.a_zone2498;

import java.util.ArrayList;

public class Player {
    private int x, y;
    public float speed = 15;
    public float jumpSpeed = 20;
    public float jumpLimit = 550;
    private float jumpHeight = 250;
    private boolean facingRight = true;
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
    private final float RECOIL_ROTATION_MAX = 15;
    private float currentRecoilOffsetShoot1 = 0;
    private float currentRecoilOffsetShoot2 = 0;
    private float currentRecoilRotation = 0;
    private int breatheTimer = 0;
    private final int BREATHE_PERIOD = 30;
    private final float BREATHE_AMPLITUDE = 10;

    public Player(int x, int groundY) {
        this.x = x;
        this.groundY = groundY;
        this.y = groundY;
    }

    public void moveLeft(boolean start) {
        movingLeft = start;
        if (start) {
            facingRight = false;
        }
    }

    public void moveRight(boolean start) {
        movingRight = start;
        if (start) {
            facingRight = true;
        }
    }

    public void jump() {
        if (!isJumping) {
            isJumping = true;
            jumpY = 0;
        }
    }

    public void shoot(int bullet1OffsetXRight, int bullet1OffsetXLeft, int bullet1OffsetY, int bullet2OffsetXRight, int bullet2OffsetXLeft, int bullet2OffsetY) {
        // Adjust bullet spawn positions based on direction
        int bullet1OffsetX = facingRight ? bullet1OffsetXRight : bullet1OffsetXLeft;
        int bullet2OffsetX = facingRight ? bullet2OffsetXRight : bullet2OffsetXLeft;
        bullets.add(new Bullet(x + bullet1OffsetX, y - bullet1OffsetY, facingRight));
        bullets.add(new Bullet(x + bullet2OffsetX, y - bullet2OffsetY, facingRight));
        isRecoiling = true;
        recoilTimer = 0;
    }

    public void update() {
        // Rotate the wheel based on movement
        if (movingLeft || movingRight) {
            wheelRotation += speed; // Adjust the speed of rotation as needed
        }

        if (movingLeft) {
            x -= speed;
        }
        if (movingRight) {
            x += speed;
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
                currentRecoilRotation = RECOIL_ROTATION_MAX * (progress * 2);
            } else {
                currentRecoilOffsetShoot1 = RECOIL_OFFSET_MAX_SHOOT1 * (1 - (progress - 0.5f) * 2);
                currentRecoilOffsetShoot2 = RECOIL_OFFSET_MAX_SHOOT2 * (1 - (progress - 0.5f) * 2);
                currentRecoilRotation = RECOIL_ROTATION_MAX * (1 - (progress - 0.5f) * 2);
            }
            if (recoilTimer >= RECOIL_DURATION) {
                isRecoiling = false;
                currentRecoilOffsetShoot1 = 0;
                currentRecoilOffsetShoot2 = 0;
                currentRecoilRotation = 0;
            }
        }
        if (!movingLeft && !movingRight && !isJumping && !isRecoiling) {
            breatheTimer = (breatheTimer + 1) % BREATHE_PERIOD;
        } else {
            breatheTimer = 0;
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

    public float getRecoilRotation() {
        return currentRecoilRotation;
    }

    public float getBreatheOffset() {
        if (!movingLeft && !movingRight && !isJumping && !isRecoiling) {
            return (float) (BREATHE_AMPLITUDE * Math.sin(2 * Math.PI * breatheTimer / BREATHE_PERIOD));
        }
        return 0;
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

    public boolean isFacingRight() {
        return facingRight;
    }

    public void setFacingRight(boolean facingRight) {
        this.facingRight = facingRight;
    }
}