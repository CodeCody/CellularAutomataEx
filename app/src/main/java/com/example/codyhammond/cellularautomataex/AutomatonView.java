package com.example.codyhammond.cellularautomataex;

import android.content.Context;
import android.graphics.Point;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by codyhammond on 10/4/16.
 */
public class AutomatonView extends SurfaceView implements SurfaceHolder.Callback
{
    private Display display;
    private ExecutorService executorService=null;
    private PaintThread paintThread;
    private Point point;
    private int choice=0;
    private Grid cellGrid;
    private SurfaceHolder surfaceHolder;
    AutomatonView(Context context)
    {
        super(context);
    }

    AutomatonView(Context context, AttributeSet attributeSet)
    {
        super(context,attributeSet);
    }

    AutomatonView(Context context,AttributeSet attributeSet,int defstyleAttr)
    {
        super(context,attributeSet,defstyleAttr);
    }

    public void init(Display display)
    {
        this.display=display;
        getHolder().addCallback(this);
        surfaceHolder=getHolder();
        point=new Point();
    }
    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        Log.i("AutomatonView","surfaceCreated");
        display.getSize(point);
        setNewGrid();
        startThread();

       // paintThread.start();
    }

    public void startThread()
    {
        executorService = Executors.newSingleThreadExecutor();
        paintThread=new PaintThread(display,cellGrid,surfaceHolder);
        executorService.execute(paintThread);
    }
    public void setNewGrid(int i)
    {
        executorService.shutdown();
        executorService.shutdownNow();
        choice=i;
        setNewGrid();
        refresh();
    }

    private void refresh()
    {
      //  executorService.shutdownNow();
        startThread();
    }

    private void setNewGrid()
    {
        int size=getOptimalCellSize(point,false,choice);
       if(Category.OneDCellularAutomata.ordinal() == choice)
       {
          cellGrid=new OneDCellularAutomataGrid(point.y/size,point.x/size);
           cellGrid.setPixelSize(size);
       }
        else if(Category.GameOfLife.ordinal() == choice)
       {
           cellGrid=new GameOfLifeGrid(point.y/size,point.x/size);
           cellGrid.setPixelSize(size);
       }
    }


    public int getOptimalCellSize(Point displaySize, boolean isLandscape,int i) {
        int cellSize = 1;
        int limitX = 160;
        int limitY = 200;

        if (isLandscape) {
            int temp = limitX;
            limitX = limitY;
            limitY = temp;
        }

        while (displaySize.x / cellSize > limitX || displaySize.y / cellSize > limitY) {
            cellSize++;
        }

        if(i==0)
        return cellSize/2;
        else
            return cellSize;
    }
    public void clearGrid()
    {

    }
    @Override
    public void surfaceChanged(SurfaceHolder holder,int format,int width,int height)
    {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        executorService.shutdown();
    }


}
