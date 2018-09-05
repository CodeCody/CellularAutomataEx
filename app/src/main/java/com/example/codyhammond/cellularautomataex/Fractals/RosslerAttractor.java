package com.example.codyhammond.cellularautomataex.Fractals;

import android.graphics.Point;
import android.view.SurfaceHolder;

/**
 * Created by codyhammond on 10/31/16.
 */

public class RosslerAttractor extends FractalandChaos
{
    private final int left = screenDimen.x/4;//(screenDimen.x/2)/2;
    private final int w = screenDimen.x-(screenDimen.x/3);
    private final double a = 0.2;
    private final double b = 0.2;
    private final double c = 5.7;
    private final double dt = 0.04;
    private final double dt2 = dt/2;
    private final int hide = 5000;

    public RosslerAttractor(Point point, SurfaceHolder surfaceHolder)
   {
      super(point,surfaceHolder,1);

   }

    @Override
    public void run()
    {
      draw();
    }

    @Override
    public void draw()
    {
       paint();
    }

    @Override
    protected void restart()
    {
        paintBackground();
    }

    public void paint()
    {
        while(true) {
            double xmin = 2000;
            double xmax = -1000;
            double ymin = 2000;
            double ymax = -1000;
            double xn = 0;
            double yn = 1;
            double zn = 0;
            double xfac = 4000;
            double yfac = 4000;
            double fn[] = new double[3];
            for (int i = 0; i < 20000 || !Thread.currentThread().isInterrupted(); i++) {
                f(xn, yn, zn, fn);
                double xn0 = xn;
                double yn0 = yn;
                double zn0 = zn;
                double xfn0 = fn[0];
                double yfn0 = fn[1];
                double zfn0 = fn[2];
                xn = xn0 + dt * xfn0;
                yn = yn0 + dt * yfn0;
                zn = zn0 + dt * zfn0;
                f(xn, yn, zn, fn);
                xn = xn0 + dt2 * (fn[0] + xfn0);
                yn = yn0 + dt2 * (fn[1] + yfn0);
                zn = zn0 + dt2 * (fn[2] + zfn0);
                if (i < hide) {
                    if (xn > xmax) xmax = xn;
                    if (xn < xmin) xmin = xn;
                    if (yn + zn > ymax) ymax = yn + zn;
                    if (yn + zn < ymin) ymin = yn + zn;
                    if (i == hide - 1) {
                        xfac = w / (xmax - xmin);
                        yfac = w / (ymax - ymin);
                        BuffCanvas.drawCircle((int) (left - xfac * (1 + xmin)), (int) (left + w + yfac * ymin), 1, paint);
                        BuffCanvas.drawCircle((int) (left + xfac * (1 - xmin)), (int) (left + w + yfac * ymin), 1, paint);
                        BuffCanvas.drawCircle((int) (left - xfac * xmin), (int) (left + w - yfac * (1 - ymin)), 1, paint);
                        BuffCanvas.drawCircle((int) (left - xfac * xmin), (int) (left + w + yfac * (1 + ymin)), 1, paint);

                /*    BuffCanvas.drawCircle((screenDimen.x/2)-100,(screenDimen.y/2),1,paint);
                    BuffCanvas.drawCircle((screenDimen.x/2)+100,(screenDimen.y/2),1,paint);
                    BuffCanvas.drawCircle((screenDimen.x/2),(screenDimen.y/2)+50,1,paint);
                    BuffCanvas.drawCircle((screenDimen.x/2),(screenDimen.y/2)-50,1,paint); */

                        updateSurface();
                        BuffCanvas.drawCircle((int) (left + xfac * (xn - xmin)), (int) (left + w - yfac * (yn + zn - ymin)), 1, paint);
                    }
                } else {
                    BuffCanvas.drawCircle((int) (left + xfac * (xn - xmin)), (int) (left + w - yfac * (yn + zn - ymin)), 1, paint);
                    updateSurface();
                }
            }
            restart();
        }
    }

    void f(double xn, double yn, double zn, double[] fn)
    {
        fn[0] = - yn - zn;
        fn[1] = xn + a*yn;
        fn[2] = b+xn*zn-c*zn;
    }

}
