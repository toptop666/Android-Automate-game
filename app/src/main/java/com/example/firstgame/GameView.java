package com.example.firstgame;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private MainThread thread;

    public GameView(Context context) {
        super(context);

        getHolder().addCallback(this);

        thread = new MainThread(getHolder(), this);
        setFocusable(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        boolean retry = true;
        while(retry) {
            try {
                thread.setRunning(false);
                thread.join();
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    public void update() {

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if(canvas != null) {
            canvas.drawColor(Color.WHITE);
            Paint paint = new Paint();

            RegularExpression start = new RegularExpression('0', Types.Elementary);
            RegularExpression finish = new RegularExpression('1', Types.Elementary);
            RegularExpression fin = new RegularExpression(start, finish, Types.Chaining);
            RegularExpression rer = new RegularExpression(fin, finish, Types.Chaining);

            Automate gen = new Automate(rer);
            gen.drawAutomate(paint, canvas, BitmapFactory.decodeResource(getResources(), R.drawable.arrowhead));

        }

    }
}
