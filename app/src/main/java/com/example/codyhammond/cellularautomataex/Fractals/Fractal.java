package com.example.codyhammond.cellularautomataex.Fractals;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.SurfaceHolder;

import com.example.codyhammond.cellularautomataex.PaintRunnable;

/**
 * Created by codyhammond on 10/14/16.
 */

 abstract public class Fractal implements PaintRunnable {

    private SurfaceHolder surfaceHolder;
    protected Canvas BuffCanvas;
    protected Bitmap buffCanvasBitmap;
    protected Paint paint;
    private Point screenDimen;
    public Fractal(Point point)
    {
        paint=new Paint();
    }



}
