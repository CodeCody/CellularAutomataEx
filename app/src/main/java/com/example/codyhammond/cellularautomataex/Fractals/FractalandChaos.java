package com.example.codyhammond.cellularautomataex.Fractals;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.SurfaceHolder;

import com.example.codyhammond.cellularautomataex.PaintRunnable;

/**
 * Created by codyhammond on 10/14/16.
 */

 abstract public class FractalandChaos implements PaintRunnable {

    protected SurfaceHolder surfaceHolder;
    protected Canvas BuffCanvas;
    protected Bitmap buffCanvasBitmap;
    protected Paint paint;
    protected Point screenDimen;

    public FractalandChaos(Point point, SurfaceHolder surfaceHolder)
    {
        paint=new Paint();
        this.surfaceHolder=surfaceHolder;

        screenDimen=point;
        BuffCanvas=new Canvas();
        buffCanvasBitmap=Bitmap.createBitmap(point.x,point.y,Bitmap.Config.ARGB_8888);
        BuffCanvas.setBitmap(buffCanvasBitmap);
        paintBackground(point);

    }

    private void paintBackground(Point point)
    {
        Rect background=new Rect(0,0,point.x,point.y);
        BuffCanvas.drawRect(background,paint);
        paint.setColor(Color.CYAN);
    }

    void updateSurface()
    {
        try {
            if (!Thread.currentThread().isInterrupted()) {
                Canvas canvas;
                canvas = surfaceHolder.lockCanvas();
                canvas.drawBitmap(buffCanvasBitmap, 0, 0, null);
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
        catch (NullPointerException e) {}
    }

}
