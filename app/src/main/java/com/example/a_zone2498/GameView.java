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
    private Bitmap body, head, wheel, head1, head2, shoot1, shoot2;
    private Player player;
    private Handler handler = new Handler();
    private Runnable runnable;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Масштабирование ресурсов
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

        // Для пуль (размеры вычислены "в лоб", можно менять)
        int[] originalShoot1Size = {1709/4, 490/4};
        int[] originalShoot2Size = {577/4, 353/4};

        body = loadAndScaleBitmap(context, R.drawable.body, originalBodySize[0], originalBodySize[1]);
        head = loadAndScaleBitmap(context, R.drawable.head, originalHeadSize[0], originalHeadSize[1]);
        wheel = loadAndScaleBitmap(context, R.drawable.wheel, originalWheelSize[0], originalWheelSize[1]);
        head1 = loadAndScaleBitmap(context, R.drawable.heand, originalHead1Size[0], originalHead1Size[1]);
        head2 = loadAndScaleBitmap(context, R.drawable.heand_2, originalHead2Size[0], originalHead2Size[1]);
        shoot1 = loadAndScaleBitmap(context, R.drawable.shoot_1, originalShoot1Size[0], originalShoot1Size[1]);
        shoot2 = loadAndScaleBitmap(context, R.drawable.shoot_2, originalShoot2Size[0], originalShoot2Size[1]);

        player = new Player(300, 700);

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

        Matrix matrix = new Matrix();
        matrix.postTranslate(-wheel.getWidth() / 2f, -wheel.getHeight() / 2f);
        matrix.postRotate(player.getWheelRotation());
        matrix.postTranslate(player.getX() + body.getWidth() / 2f - 10, player.getY() - 10);
        canvas.drawBitmap(wheel, matrix, null);

        canvas.drawBitmap(head1, player.getX() + 20,
                player.getY() - body.getHeight() + 170 - head1.getHeight() - 10, null);
        canvas.drawBitmap(head2, player.getX() + body.getWidth() - 25,
                player.getY() - body.getHeight() + 150 - head2.getHeight() - 20, null);

        canvas.drawBitmap(body, player.getX(), player.getY() - body.getHeight(), null);

        canvas.drawBitmap(head,
                player.getX() + body.getWidth() / 2f - head.getWidth() / 2f,
                player.getY() - body.getHeight() - head.getHeight() + 10,
                null);

        canvas.drawBitmap(shoot1,
                player.getX() + 10,
                player.getY() - body.getHeight() + 150,
                null);
        canvas.drawBitmap(shoot2,
                player.getX() + body.getWidth() - shoot2.getWidth() + 100,
                player.getY() - body.getHeight() + 90,
                null);

        for (Bullet bullet : player.getBullets()) {
            canvas.drawBitmap(head1, bullet.getX(), bullet.getY(), null);
        }
    }

    public void moveLeft(boolean start) {
        player.moveLeft(start);
    }

    public void moveRight(boolean start) {
        player.moveRight(start);
    }

    public void jump() {
        player.jump();
    }

    public void shoot() {
        player.shoot();
    }
}
