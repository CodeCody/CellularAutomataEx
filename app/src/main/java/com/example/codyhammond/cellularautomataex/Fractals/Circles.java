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
    private SurfaceHolder surfaceHolder;
    private int version=0;
    private CyclicBarrier cyclicBarrier;
    private Runnable updateSurfaceRunnable;
    private LinkedList<Runnable> circleRunnables;
    private ExecutorService executors;
    private int circleRadius=400;
    private int iterations=10;
    private int centerX=0;
    private int centerY=0;


    public Circles(Point point, SurfaceHolder surfaceHolder)
    {
        super(point,surfaceHolder);
        this.surfaceHolder=surfaceHolder;
        paint.setStyle(Paint.Style.STROKE);
        centerX=screenDimen.x/2;
        centerY=screenDimen.y/2;
        circleRunnables=new LinkedList<>();
        setUpCircleRunnables();

        updateSurfaceRunnable=new Runnable() {
            @Override
            public void run() {
                updateSurface();
                cyclicBarrier.reset();
                Log.i("ActionRunnable","executed");
            }
        };

            cyclicBarrier=new CyclicBarrier(4,updateSurfaceRunnable);


    }

    public void setUpCircleRunnables()
    {
        circleRunnables.add(new Runnable() {
            @Override
            public void run() {
                drawTopLeftQuadrant(centerX, centerY, circleRadius,iterations-1);
            }
        });

        circleRunnables.add(new Runnable() {
            @Override
            public void run() {
               drawTopRightQuadrant(centerX,centerY,circleRadius,iterations-1);
            }
        });

        circleRunnables.add(new Runnable() {
            @Override
            public void run() {
                drawBottomLeftQuadrant(centerX,centerY,circleRadius,iterations-1);
            }
        });

        circleRunnables.add(new Runnable() {
            @Override
            public void run() {
                drawBottomRightQuadrant(centerX,centerY,circleRadius,iterations-1);
            }
        });

        executors=Executors.newFixedThreadPool(4);


    }


    private void checkInterrupt()
    {

    }


    @Override
    public synchronized void run()
    {
        updateSurface();

        for(Runnable r : circleRunnables)
        {
            executors.execute(r);
        }

        while(true)
        {
            if(Thread.currentThread().isInterrupted())
            {
                executors.shutdownNow();
            }
        }

      /*  try {
            while (!Thread.currentThread().isInterrupted()) {
                draw();
                updateSurface();
            }
        }
        catch(NullPointerException  e)
        {

        } */

    }



    @Override @SuppressWarnings("NewApi")
    public void draw()
    {
       // BuffCanvas.drawCircle(screenDimen.x/2,screenDimen.y/2,400,paint);

       // recurseCircle();
    }

    private void drawTopLeftQuadrant(int x,int y,int radius,int countDown)
    {
        if(countDown!=1 && !Thread.currentThread().isInterrupted() && y <= centerY && y >= 0  && x <= centerX) {
            BuffCanvas.drawCircle(x, y, radius, paint);

           try {
                cyclicBarrier.await();
            }
            catch (InterruptedException  | BrokenBarrierException ie){}

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
            try {
                cyclicBarrier.await();
            }
            catch (InterruptedException  | BrokenBarrierException ie){}

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
            try {
                cyclicBarrier.await();
            }
            catch (InterruptedException  | BrokenBarrierException ie){}

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
            try {
                cyclicBarrier.await();
            }
            catch (InterruptedException  | BrokenBarrierException ie){}

            drawBottomRightQuadrant(x,y + radius,radius/2,countDown-1);
            drawBottomRightQuadrant(x,y - radius,radius/2,countDown-1);
            drawBottomRightQuadrant(x-radius,y,radius/2,countDown-1);
            drawBottomRightQuadrant(x+radius,y,radius/2,countDown-1);

        }
    }

    private void paintCircle(int i, int j,int CellStatus) {
        Point p = new Point(i,j);
        int x = p.x;
        int y = p.y;
        int color= Color.BLACK;

        if(CellStatus==1)
            color=Color.CYAN;

        paint.setColor(color);
        Rect rect = new Rect(
                x * 20,
                y * 20,
                (x + 1) * 20,
                (y + 1) * 20
        );

        BuffCanvas.drawRect(rect, paint);
    }

    static class CircleData
    {

    }
}
