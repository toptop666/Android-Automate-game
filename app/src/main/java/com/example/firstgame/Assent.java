package com.example.firstgame;

import static com.example.firstgame.MainThread.canvas;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;

public class Assent {

    private Bitmap image;
    private int x,y;

    public void setPoint(Point point) {
        this.x = point.x;
        this.y = point.y;
    }

    public Assent(Bitmap bmp) {
        image = bmp;
    }

    public void draw(Canvas canvas, Paint paint) {
        canvas.drawBitmap(image, x, y, paint);
    }

    public void RotateAssent(float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        this.image = Bitmap.createBitmap(this.image, x, y, this.image.getWidth(), this.image.getHeight(), matrix, true);
    }

    public void ScaleAssent(float scaleRatioX, float scaleRatioY) {
        Matrix matrix = new Matrix();
        matrix.postScale(scaleRatioX, scaleRatioY);
        this.image = Bitmap.createBitmap(this.image, x, y, this.image.getWidth(), this.image.getHeight(), matrix, true);
    }



}
