package com.example.firstgame;

import static com.example.firstgame.MainThread.canvas;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

public class CharacterSprite {

    private Bitmap image;
    private int x,y;

    private int xVelocity = 10;
    private int yVelocity = 5;
    private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

    public CharacterSprite(Bitmap bmp) {
        image = bmp;
        x = 100;
        y = 100;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, x, y, null);
    }

    public void RotateBitmap(float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        this.image = Bitmap.createBitmap(this.image, 0, 0, this.image.getWidth(), this.image.getHeight(), matrix, true);
    }

    public void ScaleBitmap(float scaleRatioX, float scaleRatioY) {
        Matrix matrix = new Matrix();
        matrix.postScale(scaleRatioX, scaleRatioY);
        this.image = Bitmap.createBitmap(this.image, 0, 0, this.image.getWidth(), this.image.getHeight(), matrix, true);
    }


    public void update() {
        y += yVelocity;
        x += xVelocity;
        if (y==1700) {
            yVelocity = 0;
        }
        if(x==900) {
            xVelocity = 0;
        }
    }
}
