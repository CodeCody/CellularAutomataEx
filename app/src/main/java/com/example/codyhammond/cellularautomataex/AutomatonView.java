package com.example.codyhammond.cellularautomataex;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.codyhammond.cellularautomataex.Fractals.AizawaAttractor;
import com.example.codyhammond.cellularautomataex.Fractals.BarnsleyFern;
import com.example.codyhammond.cellularautomataex.Fractals.Circles;
import com.example.codyhammond.cellularautomataex.Fractals.FractalandChaos;
import com.example.codyhammond.cellularautomataex.Fractals.LorenzAttractor;
import com.example.codyhammond.cellularautomataex.Fractals.RosslerAttractor;
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
    private int category_choice=0;
    private int fractalChaosChoice=0;
    private Grid cellGrid;
    private FractalandChaos fractalandChaos;
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
        setNewSurface();
        startThread();
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
      //  startThread();
    }

    public void startThread()
    {
        executorService = Executors.newSingleThreadExecutor();
        if(category_choice == 0) {
            executorService.execute((OneDCellularAutomataGrid)cellGrid);
        }
        else if(category_choice == 1 )
        {
            executorService.execute((GameOfLifeGrid)cellGrid);
        }
        else
        {
           executorService.execute(fractalandChaos);
        }
    }

    public void setNewSurfaceMode(int selection)
    {
        if(selection == category_choice)
            return;

        shutDownPaintThread();
        category_choice=selection;
        setNewSurface();
        startThread();
    }

    public int getCategory_choice() {
        return category_choice;
    }
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if(cellGrid instanceof GameOfLifeGrid) {

            int activePointerID=event.getPointerId(0);

            int pointerIndex=event.findPointerIndex(activePointerID);
            float x = event.getX(pointerIndex);
            float y = event.getY(pointerIndex);

            ((GameOfLifeGrid) cellGrid).touchModify(x,y);

            return true;
        }

        return false;
    }

    public void shutDownPaintThread()
    {
        executorService.shutdownNow();
    }
    public void setNewRule(String rule)
    {
        shutDownPaintThread();
        cellGrid=new OneDCellularAutomataGrid(point,rule,surfaceHolder);
        startThread();
    }

    public void setNewFractalChaos(int i)
    {
        fractalChaosChoice=i;
        shutDownPaintThread();
        setFractalChaosSelection();
        startThread();

    }

    private void setFractalChaosSelection()
    {
        if(fractalChaosChoice == 0) {
            fractalandChaos=new Circles(point,surfaceHolder);
        }
        else if(fractalChaosChoice == 1)
        {
            fractalandChaos=new BarnsleyFern(point,surfaceHolder);
        }
        else if(fractalChaosChoice == 2) {
            fractalandChaos=new RosslerAttractor(point,surfaceHolder);
        }
        else if(fractalChaosChoice == 3) {
            fractalandChaos=new LorenzAttractor(point,surfaceHolder);
        }
        else {
            fractalandChaos=new AizawaAttractor(point,surfaceHolder);
        }

    }

    private void setNewSurface()
    {
       if(Category.OneDCellularAutomata.ordinal() == category_choice)
       {
           cellGrid=new OneDCellularAutomataGrid(point,surfaceHolder);
       }
        else if(Category.GameOfLife.ordinal() == category_choice)
       {
           cellGrid=new GameOfLifeGrid(point,surfaceHolder);
       }
        else if(Category.Fractals.ordinal() == category_choice)
       {
           setFractalChaosSelection();
       }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder,int format,int width,int height)
    {
          setNewSurface();
        shutDownPaintThread();
        startThread();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
       Log.i("surfaceDetroyed","destroyed");
  //      shutDownPaintThread();
    }

    @Override
    public void onDetachedFromWindow()
    {
        super.onDetachedFromWindow();
        Log.i("windowDetached","TRUE");
        shutDownPaintThread();
    }

}
