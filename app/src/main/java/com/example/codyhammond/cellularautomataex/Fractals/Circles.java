package com.example.codyhammond.cellularautomataex.Fractals;

import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.SurfaceHolder;

import com.example.codyhammond.cellularautomataex.PaintRunnable;

/**
 * Created by codyhammond on 10/26/16.
 */

public class Circles extends Fractal
{

    public Circles(Point point, SurfaceHolder surfaceHolder)
    {
        super(point);
    }



    @Override
    public synchronized void run()
    {

    }

    @Override
    public void draw()
    {

    }

    private void paintCell(int i, int j,int CellStatus) {
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

}
