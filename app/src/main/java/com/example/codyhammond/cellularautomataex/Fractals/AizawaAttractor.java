package com.example.codyhammond.cellularautomataex.Fractals;

import android.graphics.Point;
import android.view.SurfaceHolder;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by codyhammond on 10/31/16.
 */

public class AizawaAttractor extends FractalandChaos {

    private double a = 0.95, b = 0.7, c = 0.6, d = 3.5, e = 0.25, f = 0.1, dt = .01;
    private double x, y, z;
    private int count = 0;


    public AizawaAttractor(Point point, SurfaceHolder surfaceHolder) {
        super(point, surfaceHolder, 2);
        initRepeatRunnable();
        initWorkRunnables();
        initActionRunnable();
        initCyclicBarriers();
    }

    @Override
    protected void restart() {
        initWorkRunnables();
    }

    private void setStartValues()
    {
        x = 0.1; y = 0; z = 0;
    }
    private void initWorkRunnables()
    {
        setStartValues();
        workerRunnables=new Runnable[workerThreadSize];
        final int startX=screenDimen.x/2;
        final int startZ=screenDimen.y/2;

        if(workerThreadSize !=2)
            return;

        workerRunnables[0]=new Runnable() {
            @Override
            public void run() {
                for(int i=0; i < 12000; i++) {
                    if(Thread.currentThread().isInterrupted())
                        break;

                    BuffCanvas.drawCircle((int) (startX + 300 * x), (int) (startZ-(startZ/2) + 500 * z), 1, paint);
                    drawBarrierAwait();
                }
                repeatBarrierAwait();
            }
        };

        workerRunnables[1]=new Runnable() {
            @Override
            public void run() {
                for(int i=0; i < 12000; i++) {

                    if(Thread.currentThread().isInterrupted())
                        break;

                    BuffCanvas.drawCircle((int) (startX - 300 * x), (int) (startZ-(startZ/2) + 500 * z), 1, paint);
                    drawBarrierAwait();
                }
                repeatBarrierAwait();
            }
        };

        executorService=Executors.newFixedThreadPool(workerThreadSize);
    }



    @Override
    protected void initActionRunnable()
    {
        actionRunnable=new Runnable() {
            @Override
            public void run() {
                updateSurface();
                iterate();
                drawBarrier.reset();
            }
        };
    }


    @Override
    public void run()
    {
        draw();

        while(true)
        {
            if(Thread.currentThread().isInterrupted())
            {
                executorService.shutdownNow();
                break;
            }
        }
    }

    @Override
    public void draw()
    {
       startWorkers();
    }

    private void iterate()
    {
        double x1 = (z-b)*x - d*y;

        double y1 = d * x + (z - b) * y;

        double z1 = c + a*z - (Math.pow(z, 3)/3d) - (Math.pow(x, 2) + Math.pow(y, 2)) * (1 + e * z) + f * z * ( Math.pow(x, 3));


        x=x + dt * x1;
        y=y + dt * y1;
        z=z + dt * z1;

        count++;
    }
}
