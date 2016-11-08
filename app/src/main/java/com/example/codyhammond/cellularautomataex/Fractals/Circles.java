package com.example.codyhammond.cellularautomataex.Fractals;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.view.SurfaceHolder;

import java.util.LinkedList;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by codyhammond on 10/26/16.
 */

public class Circles extends FractalandChaos
{
    private int circleRadius=400;
    private int iterations=10;
    private int centerX=0;
    private int centerY=0;


    public Circles(Point point, SurfaceHolder surfaceHolder)
    {
        super(point,surfaceHolder,4);
        this.surfaceHolder=surfaceHolder;
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.CYAN);
        centerX=screenDimen.x/2;
        centerY=screenDimen.y/2;
        initAsyncUtil();
    }

    private void initAsyncUtil()
    {
        initWorkRunnables();
        initRepeatRunnable();
        initActionRunnable();
        initCyclicBarriers();
    }



    @Override
    public synchronized void run()
    {
        startWorkers();

        while(true)
        {
            if(Thread.currentThread().isInterrupted())
            {
                executorService.shutdownNow();
            }
        }
    }

    @Override
    public void draw()
    {
        updateSurface();
    }

    private void drawTopLeftQuadrant(int x,int y,int radius,int countDown)
    {
        if(countDown!=1 && !Thread.currentThread().isInterrupted() && y <= centerY && y >= 0  && x <= centerX) {
            BuffCanvas.drawCircle(x, y, radius, paint);

            drawBarrierAwait();

            drawTopLeftQuadrant(x,y - radius,radius/2,countDown-1);
            drawTopLeftQuadrant(x,y+radius,radius/2,countDown-1);
            drawTopLeftQuadrant(x+radius,y,radius/2,countDown-1);
            drawTopLeftQuadrant(x-radius,y,radius/2,countDown-1);
        }
    }

    private void drawTopRightQuadrant(int x,int y,int radius,int countDown)
    {
        if(countDown!=1 && !Thread.currentThread().isInterrupted() && y <= centerY && y >= 0 && x >= centerX )
        {
            BuffCanvas.drawCircle(x, y, radius, paint);

            drawBarrierAwait();

            drawTopRightQuadrant(x,y - radius,radius/2,countDown-1);
            drawTopRightQuadrant(x,y+radius,radius/2,countDown-1);
            drawTopRightQuadrant(x-radius,y,radius/2,countDown-1);
            drawTopRightQuadrant(x+radius,y,radius/2,countDown-1);
        }
    }


    private void drawBottomLeftQuadrant(int x,int y,int radius,int countDown)
    {
        if(countDown!=1 && !Thread.currentThread().isInterrupted() && y >= centerY  && x <=centerX) {
            BuffCanvas.drawCircle(x, y, radius, paint);

            drawBarrierAwait();

            drawBottomLeftQuadrant(x,y + radius,radius/2,countDown-1);
            drawBottomLeftQuadrant(x,y - radius,radius/2,countDown-1);
            drawBottomLeftQuadrant(x+radius,y,radius/2,countDown-1);
            drawBottomLeftQuadrant(x-radius,y,radius/2,countDown-1);

        }
    }
    private void drawBottomRightQuadrant(int x,int y,int radius,int countDown)
    {
        if(countDown!=1 && !Thread.currentThread().isInterrupted() && y >= centerY  && x >= centerX ) {
            BuffCanvas.drawCircle(x, y, radius, paint);

            drawBarrierAwait();

            drawBottomRightQuadrant(x,y + radius,radius/2,countDown-1);
            drawBottomRightQuadrant(x,y - radius,radius/2,countDown-1);
            drawBottomRightQuadrant(x-radius,y,radius/2,countDown-1);
            drawBottomRightQuadrant(x+radius,y,radius/2,countDown-1);

        }
    }


    protected void initWorkRunnables()
    {
        workerRunnables=new Runnable[workerThreadSize];

        workerRunnables[0]=new Runnable() {
            @Override
            public void run() {
                drawTopLeftQuadrant(centerX, centerY, circleRadius,iterations-1);
                repeatBarrierAwait();
            }
        };

        workerRunnables[1]=new Runnable() {
            @Override
            public void run() {
                drawTopRightQuadrant(centerX,centerY,circleRadius,iterations-1);
                repeatBarrierAwait();
            }
        };

        workerRunnables[2]=new Runnable() {
            @Override
            public void run() {
                drawBottomLeftQuadrant(centerX,centerY,circleRadius,iterations-1);
                repeatBarrierAwait();
            }
        };

        workerRunnables[3]=new Runnable() {
            @Override
            public void run() {
                drawBottomRightQuadrant(centerX,centerY,circleRadius,iterations-1);
                repeatBarrierAwait();
            }
        };

        executorService = Executors.newFixedThreadPool(workerThreadSize);
    }

    @Override
    protected void initActionRunnable()
    {
        actionRunnable=new Runnable() {
            @Override
            public void run() {
                draw();
                drawBarrier.reset();
            }
        };
    }

    @Override
    protected void restart()
    {
        initWorkRunnables();
    }

    protected void initRepeatRunnable()
    {
        restartRunnable=new Runnable() {
            @Override
            public synchronized void run() {

                repeatBarrier.reset();

                try {
                    wait(5000);
                } catch (InterruptedException ie){}
                restart();
                paintBackground();
                startWorkers();
            }
        };
    }



}
