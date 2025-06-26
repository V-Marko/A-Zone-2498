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
    private Bitmap body, head, wheel;
    private Player player;
    private Handler handler = new Handler();
    private Runnable runnable;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Bitmap originalBody = BitmapFactory.decodeResource(getResources(), R.drawable.body);
        Bitmap originalHead = BitmapFactory.decodeResource(getResources(), R.drawable.head);
        Bitmap originalWheel = BitmapFactory.decodeResource(getResources(), R.drawable.koleso);

        body = loadAndScaleBitmap(context, R.drawable.body, originalBody.getWidth() / 15, originalBody.getHeight() / 15);
        head = loadAndScaleBitmap(context, R.drawable.head, originalHead.getWidth() / 15, originalHead.getHeight() / 15);
        wheel = loadAndScaleBitmap(context, R.drawable.koleso, originalWheel.getWidth() / 15, originalWheel.getHeight() / 15);

        player = new Player(300, 700);

        runnable = new Runnable() {
            @Override
            public void run() {
                player.update();
                invalidate();
                handler.postDelayed(this, 30);
            }
        };
        handler.post(runnable);
    }

    private Bitmap loadAndScaleBitmap(Context context, int resourceId, int desiredWidth, int desiredHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getResources(), resourceId, options);
        int scaleFactor = Math.max(1, Math.min(options.outWidth / desiredWidth, options.outHeight / desiredHeight));
        options.inJustDecodeBounds = false;
        options.inSampleSize = scaleFactor;
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);
        return Bitmap.createScaledBitmap(bitmap, desiredWidth, desiredHeight, true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Matrix matrix = new Matrix();
        matrix.postTranslate(-wheel.getWidth() / 2f, -wheel.getHeight() / 2f);
        matrix.postRotate(player.getWheelRotation());
        matrix.postTranslate(player.getX() + body.getWidth() / 2 - 10, player.getY() - 10);
        canvas.drawBitmap(wheel, matrix, null);

        canvas.drawBitmap(body, player.getX(), player.getY() - body.getHeight(), null);
        canvas.drawBitmap(head, (body.getHeight() / 4) + player.getX(), player.getY() - body.getHeight() + 10 - head.getHeight(), null);
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
}
