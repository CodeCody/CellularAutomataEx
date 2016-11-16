package com.example.codyhammond.cellularautomataex.Fractals;

import android.graphics.Point;
import android.view.SurfaceHolder;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by codyhammond on 11/1/16.
 */

public class BarnsleyFern extends FractalandChaos {


    public BarnsleyFern(Point point, SurfaceHolder holder)
    {
        super(point,holder,20);
        initWorkRunnables();
        initActionRunnable();
        initRepeatRunnable();
        initCyclicBarriers();
    }




    private void initWorkRunnables()
    {
        workerRunnables=new Runnable[workerThreadSize];

        for(int i=0; i < workerRunnables.length; i++)
        {
            workerRunnables[i]=new Runnable() {
                @Override
                public void run() {
                    draw();
                }
            };
        }



        executorService = Executors.newFixedThreadPool(workerThreadSize);
    }

    @Override
    public synchronized void run()
    {
        startWorkers();
        while(true)
        {
            if(Thread.currentThread().isInterrupted())
            {
                restartTimer.cancel();
                executorService.shutdownNow();
                return;
            }
        }
    }

    @Override
    protected void restart()
    {
        initWorkRunnables();
    }

    @Override
    public void draw()
    {
        double x = 0;
        double y = 0;

        for (int i = 0; i < 3000; i++) {

            if(Thread.currentThread().isInterrupted())
                return;

            double tmpx, tmpy;
            double r = Math.random();

            if (r <= 0.01) {
                tmpx = 0;
                tmpy = 0.16 * y;
            } else if (r <= 0.08) {
                tmpx = 0.2 * x - 0.26 * y;
                tmpy = 0.23 * x + 0.22 * y + 1.6;
            } else if (r <= 0.15) {
                tmpx = -0.15 * x + 0.28 * y;
                tmpy = 0.26 * x + 0.24 * y + 0.44;
            } else {
                tmpx = 0.85 * x + 0.04 * y;
                tmpy = -0.04 * x + 0.85 * y + 1.6;
            }
            x = tmpx;
            y = tmpy;

            BuffCanvas.drawCircle((int) Math.round(screenDimen.x / 2 + x * screenDimen.x / 11),
                    (int) Math.round(screenDimen.y - y * screenDimen.y / 11),1,paint);

           drawBarrierAwait();
        }

        repeatBarrierAwait();
    }

}
