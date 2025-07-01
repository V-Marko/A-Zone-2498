package com.example.a_zone2498;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

public class GameView extends View {
    private Bitmap body, head, wheel, head1, head2, shoot1, shoot2, bullet;
    private Player player;
    private Handler handler = new Handler();
    private Runnable runnable;

    int scale = 15;

    Bitmap originalBody = BitmapFactory.decodeResource(getResources(), R.drawable.body);
    int[] originalBodySize = {originalBody.getWidth() / scale, originalBody.getHeight() / scale};

    Bitmap originalHead = BitmapFactory.decodeResource(getResources(), R.drawable.head);
    int[] originalHeadSize = {originalHead.getWidth() / scale, originalHead.getHeight() / scale};

    Bitmap originalWheel = BitmapFactory.decodeResource(getResources(), R.drawable.wheel);
    int[] originalWheelSize = {originalWheel.getWidth() / scale, originalWheel.getHeight() / scale};

    Bitmap originalHead1 = BitmapFactory.decodeResource(getResources(), R.drawable.heand);
    int[] originalHead1Size = {originalHead1.getWidth() / scale, originalHead1.getHeight() / scale};

    Bitmap originalHead2 = BitmapFactory.decodeResource(getResources(), R.drawable.heand_2);
    int[] originalHead2Size = {originalHead2.getWidth() / scale, originalHead2.getHeight() / scale};

    int[] originalShoot1Size = {1709/4, 490/4};
    int[] originalShoot2Size = {577/4, 353/4};

    Bitmap originalBullet = BitmapFactory.decodeResource(getResources(), R.drawable.bullet);
    int[] originalBulletSize = {originalBullet.getWidth() / scale, originalBullet.getHeight() / scale};

    private PlayerController playerController;

    private float body_Y = 70;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);

        body = loadAndScaleBitmap(context, R.drawable.body, originalBodySize[0], originalBodySize[1]);
        head = loadAndScaleBitmap(context, R.drawable.head, originalHeadSize[0], originalHeadSize[1]);
        wheel = loadAndScaleBitmap(context, R.drawable.wheel, originalWheelSize[0], originalWheelSize[1]);
        head1 = loadAndScaleBitmap(context, R.drawable.heand, originalHead1Size[0], originalHead1Size[1]);
        head2 = loadAndScaleBitmap(context, R.drawable.heand_2, originalHead2Size[0], originalHead2Size[1]);
        shoot1 = loadAndScaleBitmap(context, R.drawable.shoot_1, originalShoot1Size[0], originalShoot1Size[1]);
        shoot2 = loadAndScaleBitmap(context, R.drawable.shoot_2, originalShoot2Size[0], originalShoot2Size[1]);
        bullet = loadAndScaleBitmap(context, R.drawable.bullet, originalBulletSize[0], originalBulletSize[1]);

        player = new Player(300, 700);
        playerController = new PlayerController(player);

        runnable = new Runnable() {
            @Override
            public void run() {
                player.update();
                player.updateBullets(getWidth());
                invalidate();
                handler.postDelayed(this, 30);
            }
        };
        handler.post(runnable);
    }

    private Bitmap loadAndScaleBitmap(Context context, int resourceId, int desiredWidth, int desiredHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), resourceId, options);
        int scaleFactor = Math.max(1, Math.min(options.outWidth / desiredWidth, options.outHeight / desiredHeight));
        options.inJustDecodeBounds = false;
        options.inSampleSize = scaleFactor;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resourceId, options);
        return Bitmap.createScaledBitmap(bitmap, desiredWidth, desiredHeight, true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float breatheOffset = player.getBreatheOffset();
        boolean facingRight = player.isFacingRight();
        int playerX = player.getX();
        int playerY = player.getY();

        // Wheel
        float wheelX = facingRight ? (playerX + body.getWidth() / 2f - wheel.getWidth() / 2f-10) : (playerX + body.getWidth() / 2f - wheel.getWidth() / 2f - 140);
        float wheelY = playerY - wheel.getHeight() / 2f+70;

        Matrix wheelMatrix = new Matrix();
        wheelMatrix.postTranslate(wheelX, wheelY);
        wheelMatrix.postRotate(player.getWheelRotation(), wheelX + wheel.getWidth() / 2f, wheelY + wheel.getHeight() / 2f);

        canvas.drawBitmap(wheel, wheelMatrix, null);

        // Head1
        Matrix head1Matrix = new Matrix();
        float head1X = facingRight ? playerX + 20 : playerX + body.getWidth() - head1.getWidth() - 190;
        float head1Y = playerY - body.getHeight() + 170 - head1.getHeight() - 10 + breatheOffset + body_Y + 10;
        if (!facingRight) {
            head1Matrix.postScale(-1f, 1f, head1.getWidth() / 2f, head1.getHeight() / 2f);
            head1Matrix.postTranslate(head1X + head1.getWidth(), head1Y);
        } else {
            head1Matrix.postTranslate(head1X, head1Y);
        }
        if (player.isRecoiling()) head1Y -= player.getRecoilOffsetShoot2();
        canvas.drawBitmap(head1, head1Matrix, null);

        // Head2
        Matrix head2Matrix = new Matrix();
        float head2X = facingRight ? playerX + body.getWidth() - 25 : playerX - 165;
        float head2Y = playerY - body.getHeight() + 150 - head2.getHeight() - 20 + breatheOffset + body_Y + 10;
        if (!facingRight) {
            head2Matrix.postScale(-1f, 1f, head2.getWidth() / 2f, head2.getHeight() / 2f);
            head2Matrix.postTranslate(head2X + head2.getWidth(), head2Y);
        } else {
            head2Matrix.postTranslate(head2X, head2Y);
        }
        if (player.isRecoiling()) head2Y -= player.getRecoilOffsetShoot1();
        canvas.drawBitmap(head2, head2Matrix, null);

        // Body
        Matrix bodyMatrix = new Matrix();
        float bodyX = facingRight ? playerX : playerX + body.getWidth();
        if (!facingRight) {
            bodyMatrix.postScale(-1f, 1f, body.getWidth() / 2f, body.getHeight() / 2f);
            bodyMatrix.postTranslate(bodyX - body.getWidth() - 150, playerY - body.getHeight() + breatheOffset + body_Y + 10);
        } else {
            bodyMatrix.postTranslate(bodyX, playerY - body.getHeight() + breatheOffset + body_Y + 10);
        }
        canvas.drawBitmap(body, bodyMatrix, null);

        // Head
        float headX = playerX + (facingRight ? body.getWidth() / 2f - head.getWidth() / 2f : -body.getWidth() / 2f + head.getWidth() / 2f + 10);
        float headY = playerY - body.getHeight() - head.getHeight() + 10 + breatheOffset + body_Y + 10;
        canvas.drawBitmap(head, headX, headY, null);

        // Shoot1
        Matrix shoot1Matrix = new Matrix();
        float shoot1X = facingRight ? playerX + 10 : playerX + body.getWidth() - 150;
        float shoot1Y = playerY - body.getHeight() + 150 + breatheOffset + body_Y + 10;
        if (player.isRecoiling()) shoot1Y -= player.getRecoilOffsetShoot1();
        if (!facingRight) {
            shoot1Matrix.postScale(-1f, 1f, shoot1.getWidth() / 2f, shoot1.getHeight() / 2f);
            shoot1Matrix.postTranslate(shoot1X - shoot1.getWidth(), shoot1Y);
        } else {
            shoot1Matrix.postTranslate(shoot1X, shoot1Y);
        }
        canvas.drawBitmap(shoot1, shoot1Matrix, null);

        // Shoot2
        Matrix shoot2Matrix = new Matrix();
        float shoot2X = facingRight ? playerX + body.getWidth() - shoot2.getWidth() + 100 : playerX - 250 + shoot2.getWidth();
        float shoot2Y = playerY - body.getHeight() + 90 + breatheOffset + body_Y + 10;
        if (player.isRecoiling()) shoot2Y -= player.getRecoilOffsetShoot2();
        if (!facingRight) {
            shoot2Matrix.postScale(-1f, 1f, shoot2.getWidth() / 2f, shoot2.getHeight() / 2f);
            shoot2Matrix.postTranslate(shoot2X - shoot2.getWidth(), shoot2Y);
        } else {
            shoot2Matrix.postTranslate(shoot2X, shoot2Y);
        }
        canvas.drawBitmap(shoot2, shoot2Matrix, null);

        // Bullets
        for (Bullet bulletArray : player.getBullets()) {
            float bulletX = bulletArray.getX();
            float bulletY = bulletArray.getY() + body_Y + 10;
            canvas.drawBitmap(bullet, bulletX, bulletY, null);
        }
    }

    public void moveLeft(boolean start) {
        playerController.moveLeft(start);
    }

    public void moveRight(boolean start) {
        playerController.moveRight(start);
    }

    public void jump() {
        playerController.jump();
    }

    public void shoot() {
        playerController.shoot(400, -330, 135, 390, -330, 200);
    }

    private class PlayerController {
        private Player player;

        public PlayerController(Player player) {
            this.player = player;
        }

        public void moveLeft(boolean start) {
            player.moveLeft(start);
            if (!start) player.setFacingRight(false);
        }

        public void moveRight(boolean start) {
            player.moveRight(start);
            if (!start) player.setFacingRight(true);
        }

        public void jump() {
            player.jump();
        }

        public void shoot(int bullet1OffsetXRight, int bullet1OffsetXLeft, int bullet1OffsetY, int bullet2OffsetXRight, int bullet2OffsetXLeft, int bullet2OffsetY) {
            player.shoot(bullet1OffsetXRight, bullet1OffsetXLeft, bullet1OffsetY, bullet2OffsetXRight, bullet2OffsetXLeft, bullet2OffsetY);
        }
    }
}