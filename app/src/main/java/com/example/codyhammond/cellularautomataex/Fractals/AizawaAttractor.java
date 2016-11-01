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

    private double a = 0.95, b = 0.7, c = 0.6, d = 3.5, e = 0.25, f = 0.1,dt=.01;
    private double x=0.1,y=0,z=0;
    private int count=0;
    private ExecutorService executors=Executors.newFixedThreadPool(2);
    private CyclicBarrier cyclicBarrier;
    private Runnable [] workRunnables;
    private Runnable updateRunnable;

    public AizawaAttractor(Point point, SurfaceHolder surfaceHolder)
    {
        super(point,surfaceHolder);
        initRunnables();

        cyclicBarrier=new CyclicBarrier(2,updateRunnable);
    }

    private void initRunnables()
    {
        workRunnables=new Runnable[2];

        workRunnables[0]=new Runnable() {
            @Override
            public void run() {
                for(int i=0; i < 10000; i++) {
                    if(Thread.currentThread().isInterrupted())
                        break;

                    BuffCanvas.drawCircle((int) (540 + 300 * x), (int) (500 + 500 * z), 1, paint);
                    try {
                        cyclicBarrier.await();
                    } catch (BrokenBarrierException | InterruptedException e) {
                    }
                }
            }
        };

        workRunnables[1]=new Runnable() {
            @Override
            public void run() {
                for(int i=0; i < 10000; i++) {

                    if(Thread.currentThread().isInterrupted())
                        break;

                    BuffCanvas.drawCircle((int) (540 - 300 * x), (int) (500 + 500 * z), 1, paint);
                    try {
                        cyclicBarrier.await();
                    } catch (BrokenBarrierException | InterruptedException be) {

                    }
                }
            }
        };

        updateRunnable=new Runnable() {
            @Override
            public void run() {
                updateSurface();
                iterate();
                cyclicBarrier.reset();
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
                executors.shutdownNow();
                break;
            }
        }
    }

    @Override
    public void draw()
    {
       for(Runnable r : workRunnables)
       {
           executors.execute(r);
       }
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
