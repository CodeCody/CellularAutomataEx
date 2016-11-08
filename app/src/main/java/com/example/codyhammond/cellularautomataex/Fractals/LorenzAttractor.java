package com.example.codyhammond.cellularautomataex.Fractals;

import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;
import android.view.SurfaceHolder;

import java.util.concurrent.Executors;

/**
 * Created by codyhammond on 10/29/16.
 */

public class LorenzAttractor extends FractalandChaos
{

    double x0 = 0.1, y0 = 0, z0 = 2;

    double t = 0.01;

    double a = 10.0;

    double b = 28.0;

    double c = 8.0 / 3.0;

   // var locations = Array();

    int count = 0;

    public LorenzAttractor(Point point, SurfaceHolder surfaceHolder)
    {
        super(point,surfaceHolder,1);
        paint.setStyle(Paint.Style.STROKE);
        initRepeatRunnable();
        initWorkerRunnable();
        initCyclicBarriers();
        //path=new Path();

    }

    private void restartCoords()
    {
        x0 = 0.1; y0 = 0; z0 = 2;
    }

    private void initWorkerRunnable()
    {
        workerRunnables=new Runnable[1];

        workerRunnables[0]=new Runnable() {
            @Override
            public void run() {
                draw();
                repeatBarrierAwait();
            }
        };

        executorService= Executors.newFixedThreadPool(1);
    }


    @Override
    public void run() {
        startWorkers();

        while(true)
        {
            if(Thread.currentThread().isInterrupted()) {
                executorService.shutdownNow();
                break;
            }
        }
    }

    @Override
    protected void restart()
    {
        initWorkerRunnable();
        restartCoords();
    }

    @Override
    public void draw() {
        count=0;
        while(count < 2000)
        {
            if(Thread.currentThread().isInterrupted())
                break;

            BuffCanvas.drawCircle((int)(540 + 20*x0),(int)(400 + 20*z0),15,paint);
            updateSurface();
            iterate();
        }

        Log.e("before await()","leaving draw");
    }

    private void iterate()
    {

        double x1 = x0 + t * a * (y0 - x0);

        double y1 = y0 + t * (x0 * (b - z0) - y0);

        double z1 = z0 + t * (x0 * y0 - c * z0);

        x0 = x1;

        y0 = y1;

        z0 = z1;

        count++;

    }


}
