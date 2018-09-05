package com.example.codyhammond.cellularautomataex.Grid;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.view.SurfaceHolder;

import com.example.codyhammond.cellularautomataex.CellStateChange;
import com.example.codyhammond.cellularautomataex.PaintRunnable;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by codyhammond on 10/13/16.
 */

public class GameOfLifeGrid implements PaintRunnable,Grid {

    //private List<CellStateChange>cellStateChanges;
    private ConcurrentLinkedQueue<CellStateChange>cellStateChanges;
    private SurfaceHolder surfaceHolder;
    private Canvas BuffCanvas;
    private Bitmap buffCanvasBitmap;
    private Paint paint;
    private Point screenDimen;
    private int grid[][];
    private int cellSizeinPixels=0;
    public GameOfLifeGrid(Point point, SurfaceHolder surfaceHolder)
    {
        cellSizeinPixels=getOptimalCellSize(point);
        grid=new int[point.y/cellSizeinPixels][point.x/cellSizeinPixels];
        screenDimen=point;
        cellStateChanges=new ConcurrentLinkedQueue<>();
        this.surfaceHolder=surfaceHolder;
        initCanvas();
        setStartPattern();
    }

    private void initCanvas()
    {
        paint=new Paint();
        buffCanvasBitmap = Bitmap.createBitmap(screenDimen.x, screenDimen.y, Bitmap.Config.ARGB_8888);
        BuffCanvas=new Canvas();
        BuffCanvas.setBitmap(buffCanvasBitmap);
        initializeGridColor();
    }

    @Override
       public int getOptimalCellSize(Point displaySize) {
        int cellSize = 1;
        int limitX = 160;
        int limitY = 200;


        while (displaySize.x / cellSize > limitX || displaySize.y / cellSize > limitY) {
            cellSize++;
        }


        return cellSize;
    }

    public void touchModify(float pointX, float pointY) {

        int cellpointCol=(int)pointX/cellSizeinPixels;
        int cellpointRow=(int)pointY/cellSizeinPixels;

        grid[cellpointRow][cellpointCol]=1;
        cellStateChanges.add(new CellStateChange(cellpointRow,cellpointCol,1));

        Log.i("Touch",String.valueOf(cellpointRow)+' '+String.valueOf(cellpointCol));
    }

    @Override
    public synchronized void run() {
        Canvas canvas;

        try {
            while (!Thread.currentThread().isInterrupted()) {

                updateGrid();
                draw();
                canvas = surfaceHolder.lockCanvas();
                canvas.drawBitmap(buffCanvasBitmap, 0, 0, null);
                surfaceHolder.unlockCanvasAndPost(canvas);

            }
        }catch(NullPointerException e )
        {

        }

    }

    @Override
    public void draw()
    {
        for( CellStateChange csc : cellStateChanges)
        {
            paintCell(csc.getX(),csc.getY(),csc.getStatus());
        }
        cellStateChanges.clear();
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

    @Override
    public void updateGrid()
    {
        int[][] next = new int[grid.length][grid[0].length];


        for (int x = 0; x < next.length; x++) {
            for (int y = 0; y < next[0].length; y++) {

                //Add up all the neighbor states to calculate the number of live neighbors.
                int neighbors = 0;
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        if(x+i < 0 && y+j < 0)//(-x,-y)
                        {
                          neighbors+=grid[next.length-1][next[0].length-1];
                        }
                        else if( x+i < 0 && y+j >= 0 && y+j <= next[0].length-1)//(-x,y)
                        {
                            neighbors+=grid[next.length-1][y+j];
                        }
                        else if(x+i < 0 && y+j > next[0].length-1)//(-x,+y)
                        {
                            neighbors+=grid[next.length-1][0];
                        }
                        else if(x+i >=0 && x+i <=next.length-1 && y+j > next[0].length-1)//(x,+y)
                        {
                            neighbors+=grid[x+i][0];
                        }
                        else if(x+i > next.length-1 && y+j > next[0].length-1)//(+x,+y)
                        {
                            neighbors+=grid[0][0];
                        }
                        else if(x+i > next.length-1 && y+j >=0 && y+j <= next[0].length-1)//(+x,y)
                        {
                            neighbors+=grid[0][y+j];
                        }
                        else if(x+i > next.length-1 && y+j < 0)//(+x,-y)
                        {
                            neighbors+=grid[0][next[0].length-1];
                        }
                        else if(x+i >= 0 && x+i <= next.length-1 && y+j < 0)//(x,-y)
                        {
                            neighbors+=grid[x+i][next[0].length-1];
                        }
                        else {
                            neighbors += grid[x + i][y + j];
                        }
                    }
                }

                //    Correct by subtracting the cell state itself.
                neighbors -= grid[x][y];

                //The rules of life!

                if      ((grid[x][y] == 1) && (neighbors <  2)) {
                    next[x][y] = 0;
                    cellStateChanges.add(new CellStateChange(x,y,0));
                }
                else if ((grid[x][y] == 1) && (neighbors >  3))
                {
                    next[x][y] = 0;
                    cellStateChanges.add(new CellStateChange(x,y,0));
                }
                else if ((grid[x][y] == 0) && (neighbors == 3)) {
                    next[x][y] = 1;
                    cellStateChanges.add(new CellStateChange(x,y,1));
                }
                else {
                    next[x][y] = grid[x][y];
                   // cellStateChanges.add(new CellStateChange(x,y,next[x][y]));
                }

            }
        }

        grid=next;
    }

    public ConcurrentLinkedQueue<CellStateChange> getCellStateChanges()
    {
        return cellStateChanges;
    }

    @Override
    public void setStartPattern()
    {
        for(int i=0; i < grid.length; i++)
        {
            for(int j=0; j < grid[0].length; j++)
            {
                grid[i][j]=(Math.random() < 0.5) ? 0 : 1;
            }
        }
    }

    private void initializeGridColor()
    {
        for(int j=0; j < getHeight(); j++)
        {
            for(int i=0; i < getWidth(); i++)
            {
                paintCell(i,j,grid[j][i]);
            }
        }
    }

    private int getHeight()
    {
        return grid.length;
    }

    private int getWidth()
    {
        return grid[0].length;
    }


}
