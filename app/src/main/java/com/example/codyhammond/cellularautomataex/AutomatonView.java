package com.example.codyhammond.cellularautomataex;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.codyhammond.cellularautomataex.Fractals.Circles;
import com.example.codyhammond.cellularautomataex.Fractals.Fractal;
import com.example.codyhammond.cellularautomataex.Grid.GameOfLifeGrid;
import com.example.codyhammond.cellularautomataex.Grid.Grid;
import com.example.codyhammond.cellularautomataex.Grid.OneDCellularAutomataGrid;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by codyhammond on 10/4/16.
 */
public class AutomatonView extends SurfaceView implements SurfaceHolder.Callback
{
    private Display display;
    private ExecutorService executorService=null;
   // private PaintThread paintThread;
    private Point point;
    private int choice=0;
    private Grid cellGrid;
    private Fractal fractal;
    private SurfaceHolder surfaceHolder;
    public AutomatonView(Context context)
    {
        super(context);
    }

    public AutomatonView(Context context, AttributeSet attributeSet)
    {
        super(context,attributeSet);
    }

    public AutomatonView(Context context,AttributeSet attributeSet,int defstyleAttr)
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
        if(choice == 0) {
            executorService.execute((OneDCellularAutomataGrid)cellGrid);
        }
        else if(choice == 1 )
        {
            executorService.execute((GameOfLifeGrid)cellGrid);
        }
        else
        {

        }
    }

    public void setNewGridMode(int selection)
    {
        if(selection == choice)
            return;

        shutDownPaintThread();
        choice=selection;
        setNewGrid();
        startThread();
    }

    public void shutDownPaintThread()
    {
        executorService.shutdown();
        executorService.shutdownNow();
    }
    public void setNewRule(String rule)
    {
        shutDownPaintThread();
        int size=getOptimalCellSize(point,false,choice);
        cellGrid=new OneDCellularAutomataGrid(point,rule,surfaceHolder);
        cellGrid.setPixelSize(size);
        startThread();
    }


    private void setNewGrid()
    {
       if(Category.OneDCellularAutomata.ordinal() == choice)
       {
          cellGrid=new OneDCellularAutomataGrid(point,surfaceHolder);
       }
        else if(Category.GameOfLife.ordinal() == choice)
       {
           cellGrid=new GameOfLifeGrid(point,surfaceHolder);
       }
        else if(Category.Fractals.ordinal() == choice)
       {
           fractal=new Circles(point,surfaceHolder);
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

    }

    @Override
    public void onDetachedFromWindow()
    {
        super.onDetachedFromWindow();
        shutDownPaintThread();
    }

}
