package com.example.codyhammond.cellularautomataex.Fractals;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.view.SurfaceHolder;

import com.example.codyhammond.cellularautomataex.PaintRunnable;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by codyhammond on 10/14/16.
 */

 abstract public class FractalandChaos implements PaintRunnable {

    protected SurfaceHolder surfaceHolder;
    protected Canvas BuffCanvas;
    protected Bitmap buffCanvasBitmap;
    protected Paint paint;
    protected Point screenDimen;
    protected CyclicBarrier drawBarrier;
    protected CyclicBarrier repeatBarrier;
    protected ExecutorService executorService;
    protected Runnable actionRunnable;
    protected final int workerThreadSize;
    protected Runnable restartRunnable;
    protected Runnable[]workerRunnables;
    protected Timer restartTimer;

    public FractalandChaos(Point point, SurfaceHolder surfaceHolder,int workerThreadSize)
    {
        paint=new Paint();
        paint.setColor(Color.CYAN);
        this.surfaceHolder=surfaceHolder;
        this.workerThreadSize=workerThreadSize;

        screenDimen=point;
        BuffCanvas=new Canvas();
        buffCanvasBitmap=Bitmap.createBitmap(point.x,point.y,Bitmap.Config.ARGB_8888);
        BuffCanvas.setBitmap(buffCanvasBitmap);
        paintBackground();
        restartTimer=new Timer();



    }

    protected void paintBackground()
    {
        Paint backpaint=new Paint();
        backpaint.setColor(Color.BLACK);
        Rect background=new Rect(0,0,screenDimen.x,screenDimen.y);
        BuffCanvas.drawRect(background,backpaint);
        updateSurface();

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

    protected void initActionRunnable()
    {
        actionRunnable=new Runnable() {
            @Override
            public void run() {
                updateSurface();
                drawBarrier.reset();
            }
        };
    }

    protected abstract void restart();

    protected void initCyclicBarriers()
    {
        drawBarrier=new CyclicBarrier(workerThreadSize,actionRunnable);
        repeatBarrier=new CyclicBarrier(workerThreadSize,restartRunnable);
    }

    protected void initRepeatRunnable()
    {
        restartRunnable=new Runnable() {
            @Override
            public synchronized void run() {

                Log.i("restartRunnable","restartAction");

                repeatBarrier.reset();
                restart();
                restartTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        paintBackground();
                        startWorkers();
                    }
                },5000L);
            }
        };
    }


    protected  void drawBarrierAwait()
    {
        try {
            drawBarrier.await();

        }
        catch (InterruptedException ie){
            String msg= ie.getMessage() == null ? "null" : ie.getLocalizedMessage();
         //   Log.e("drawBarrier",msg);
        }
        catch (BrokenBarrierException ie)
        {
            String msg= ie.getMessage() == null ? "null" : ie.getLocalizedMessage();
         //   Log.e("Broken",msg);
        }
    }
    protected void repeatBarrierAwait()
    {
        try {
            repeatBarrier.await();
        }
        catch (InterruptedException | BrokenBarrierException be){
            String msg= be.getMessage() == null ? "null" : be.getMessage();
         //   Log.e("repeatBarrier",msg);
        }
    }


    protected void startWorkers()
    {
        for(Runnable r : workerRunnables)
        {
            executorService.execute(r);
        }

       // executorService.execute(workerRunnables[0]);
    }

}
