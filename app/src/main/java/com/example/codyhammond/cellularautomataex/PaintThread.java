package com.example.codyhammond.cellularautomataex;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;

import java.util.HashMap;
import java.util.List;

/**
 * Created by codyhammond on 10/4/16.
 */
public class PaintThread implements Runnable {

    private Point point;
    private Paint paint;
    private SurfaceHolder surfaceHolder;
    private Canvas BuffCanvas;
    private Grid cellGrid;
    private HashMap<String,Integer>map=new HashMap<>();
    private Bitmap buffCanvasBitmap;
    private final int cellSizeinPixels;

    public PaintThread(Display display,Grid cellGrid,SurfaceHolder surfaceHolder)
    {
        point=new Point();
        paint=new Paint();
        map.put("Hello",1);
        this.surfaceHolder=surfaceHolder;
        display.getSize(point);
        buffCanvasBitmap = Bitmap.createBitmap(point.x, point.y, Bitmap.Config.ARGB_8888);
        BuffCanvas=new Canvas();
        cellSizeinPixels=cellGrid.cellSizeinPixels;
        BuffCanvas.setBitmap(buffCanvasBitmap);
        this.cellGrid=cellGrid;
        initializeGridColor();
    }

    @Override
    public void run()
    {
        Canvas canvas;

      while(!Thread.currentThread().isInterrupted()) {
           try {
               cellGrid.updateGrid();
               draw();
               canvas = surfaceHolder.lockCanvas();
               canvas.drawBitmap(buffCanvasBitmap, 0, 0, null);
               surfaceHolder.unlockCanvasAndPost(canvas);

           }
           catch (Exception e)
           {
               Log.e("run()",e.getMessage());
               break;
           }
       }
    }

    private void initializeGridColor()
    {
        for(int j=0; j < cellGrid.getHeight(); j++)
        {
            for(int i=0; i < cellGrid.getWidth(); i++)
            {
                paintCell(i,j,cellGrid.grid[j][i]);
            }
        }
    }



 /*   private void initialDraw()
    {
        try {
            for (int i = 0; i < cellGrid.grid[0].length; i++) {
                paintCell(i, cellGrid.currentRow, cellGrid.grid[cellGrid.currentRow][i]);
            }
        }
        catch (IndexOutOfBoundsException e)
        {
            Log.e("initialDraw()",e.getMessage());
        }

    } */

    private void draw()
    {

        try {
            if (cellGrid instanceof GameOfLifeGrid) {

                List<CellStateChange>cellStateChanges=((GameOfLifeGrid)cellGrid).getCellStateChanges();

               for( CellStateChange csc : cellStateChanges)
                {
                    paintCell(csc.getX(),csc.getY(),csc.getStatus());
                }
                cellStateChanges.clear();

            } else if (cellGrid instanceof OneDCellularAutomataGrid) {
                int currentRow = ((OneDCellularAutomataGrid) cellGrid).getCurrentRow();
                for (int i = 0; i < cellGrid.getWidth(); i++) {
                    paintCell(i, currentRow, cellGrid.grid[currentRow][i]);
                }
            }
        }
        catch (IndexOutOfBoundsException e)
        {
            Log.e("initialDraw()",e.getMessage());
        }
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
                x * cellSizeinPixels,
                y * cellSizeinPixels,
                (x + 1) * cellSizeinPixels,
                (y + 1) * cellSizeinPixels
        );

        BuffCanvas.drawRect(rect, paint);
    }

    private void paintCircle()
    {
      //  BuffCanvas.drawCircle();
    }






}
