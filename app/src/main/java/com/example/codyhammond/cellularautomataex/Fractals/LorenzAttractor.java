package com.example.codyhammond.cellularautomataex.Fractals;

import android.graphics.Paint;
import android.graphics.Point;
import android.view.SurfaceHolder;

/**
 * Created by codyhammond on 10/29/16.
 */

public class LorenzAttractor extends FractalandChaos
{

    double x0 = 0.1, y0 = 0, z0 = 0;

    double t = 0.01;

    double a = 10.0;

    double b = 28.0;

    double c = 8.0 / 3.0;

   // var locations = Array();

    int count = 0;

    public LorenzAttractor(Point point, SurfaceHolder surfaceHolder)
    {
        super(point,surfaceHolder);
        paint.setStyle(Paint.Style.STROKE);
        //path=new Path();

    }

    @Override
    public void run()
    {
       draw();
    }

    @Override
    public void draw() {

        while(count < 1000)
        {
            if(Thread.currentThread().isInterrupted())
                break;

            BuffCanvas.drawCircle((int)(540 + 20*x0),(int)(400 + 20*z0),30,paint);
            updateSurface();
            iterate();
        }


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
