package com.example.codyhammond.cellularautomataex.Grid;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.v4.util.ArrayMap;
import android.util.Log;
import android.view.SurfaceHolder;

import com.example.codyhammond.cellularautomataex.Grid.Grid;
import com.example.codyhammond.cellularautomataex.PaintRunnable;
import com.example.codyhammond.cellularautomataex.RuleSets.Ruleset;

/**
 * Created by codyhammond on 10/13/16.
 */

public class OneDCellularAutomataGrid extends Grid implements PaintRunnable {

    private int currentRow=0;
    private boolean isChaotic=false;
    private Integer[]ruleset= Ruleset.ruleSets.get("Rule 90");
    private SurfaceHolder surfaceHolder;
    private Point screenDimen;
    private Paint paint;
    private Canvas BuffCanvas;
    private static String ruleSelection="Rule 90";
    public static final int TYPE_ID=0;
    private Bitmap buffCanvasBitmap;


    public OneDCellularAutomataGrid(Point point, SurfaceHolder surfaceHolder)
    {
        super(point.y,point.x,getOptimalCellSize(point));
        screenDimen=point;
        this.surfaceHolder=surfaceHolder;
        initCanvas();
        setStartPattern();
    }

    public OneDCellularAutomataGrid(Point point,String rule,SurfaceHolder surfaceHolder)
    {
        super(point.y,point.x,getOptimalCellSize(point));
        ruleSelection=rule;
        isChaotic=rule.contains("Chaotic");
        this.surfaceHolder=surfaceHolder;
        screenDimen=point;
        initCanvas();
        ruleset=Ruleset.ruleSets.get(rule);
        setStartPattern();
    }

    public void initCanvas()
    {
        paint=new Paint();
        buffCanvasBitmap = Bitmap.createBitmap(screenDimen.x, screenDimen.y, Bitmap.Config.ARGB_8888);
        BuffCanvas=new Canvas();
        BuffCanvas.setBitmap(buffCanvasBitmap);
        initializeGridColor();
    }

    private static int getOptimalCellSize(Point displaySize) {
        int cellSize = 1;
        int limitX = 160;
        int limitY = 200;



        while (displaySize.x / cellSize > limitX || displaySize.y / cellSize > limitY) {
            cellSize++;
        }


        return cellSize/2;

    }
    public int getCurrentRow()
    {
        return currentRow;
    }

    @Override
    public void updateGrid()
    {
        for (int i = 1; i < grid[0].length-1; i++)
        {
            int left = grid[currentRow][i - 1];
            int middle = grid[currentRow][i];
            int right = grid[currentRow][i + 1];
            int newstate = rules(left, middle, right);
            if (currentRow + 1 < grid.length)
                grid[currentRow + 1][i] = newstate;
        }
        currentRow++;
    }


    public void restartGrid()
    {
        for(int i=0; i < grid.length; i++)
        {
            for(int j=0; j < grid[0].length;j++)
            {
                grid[i][j]=0;
            }
        }

        setStartPattern();
        currentRow=0;
    }

    @Override
    public void setStartPattern()
    {
        if(!isChaotic) {
            grid[0][grid[0].length / 2] = 1;
        }
        else {
            for(int i=0; i < grid[0].length; i++)
            {
                grid[0][i]=(Math.random() < 0.5) ? 0 : 1;
            }
        }
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
                    if (currentRow == getHeight() - 1) {
                        try {
                           restartGrid();
                            wait(3000);
                            initializeGridColor();
                        } catch (InterruptedException IE) {
                            break;
                        }
                    }
                }
        }
        catch(NullPointerException  e)
        {

        }

    }

    @Override
    public void draw()
    {
        try {
             {
                for (int i = 0; i < getWidth(); i++) {
                    paintCell(i, currentRow, grid[currentRow][i]);
                }
            }
        }
        catch (IndexOutOfBoundsException e)
        {
            Log.e("initialDraw()",e.getMessage());
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


    public int rules (int a, int b, int c) {


        if      (a == 1 && b == 1 && c == 1) return ruleset[0];
        else if (a == 1 && b == 1 && c == 0) return ruleset[1];
        else if (a == 1 && b == 0 && c == 1) return ruleset[2];
        else if (a == 1 && b == 0 && c == 0) return ruleset[3];
        else if (a == 0 && b == 1 && c == 1) return ruleset[4];
        else if (a == 0 && b == 1 && c == 0) return ruleset[5];
        else if (a == 0 && b == 0 && c == 1) return ruleset[6];
        else if (a == 0 && b == 0 && c == 0) return ruleset[7];

        return 0;
    }


    public int rules5(int a,int b,int c,int d,int e)
    {
        if     (a==1 && b==1 && c==1 && d==1 && e==1) return ruleset5[31];//31

        else if(a==1 && b==1 && c==1 && d==1 && e==0) return ruleset5[30];//30

        else if(a==1 && b==1 && c==1 && d==0 && e==1) return ruleset5[29];//29

        else if(a==1 && b==1 && c==1 && d==0 && e==0) return ruleset5[28];//28

        else if(a==1 && b==1 && c==0 && d==1 && e==1) return ruleset5[27];//27

        else if(a==1 && b==1 && c==0 && d==1 && e==0) return ruleset5[26];//26

        else if(a==1 && b==1 && c==0 && d==0 && e==1) return ruleset5[25];//25

        else if(a==1 && b==1 && c==0 && d==0 && e==0) return ruleset5[24];//24

        else if(a==1 && b==0 && c==1 && d==1 && e==1) return ruleset5[23];//23

        else if(a==1 && b==0 && c==1 && d==1 && e==0) return ruleset5[22];//22

        else if(a==1 && b==0 && c==1 && d==0 && e==1) return ruleset5[21];//21

        else if(a==1 && b==0 && c==1 && d==0 && e==0) return ruleset5[20];//29

        else if(a==1 && b==0 && c==0 && d==1 && e==1) return ruleset5[19];//18

        else if(a==1 && b==0 && c==0 && d==1 && e==0) return ruleset5[18];//17

        else if(a==1 && b==0 && c==0 && d==0 && e==1) return ruleset5[17];//17

        else if(a==1 && b==0 && c==0 && d==0 && e==0) return ruleset5[16];//16

        else if(a==0 && b==1 && c==1 && d==1 && e==1) return ruleset5[15];//15

        else if(a==0 && b==1 && c==1 && d==1 && e==0) return ruleset5[14];//14

        else if(a==0 && b==1 && c==1 && d==0 && e==1) return ruleset5[13];//13

        else if(a==0 && b==1 && c==1 && d==0 && e==0) return ruleset5[12];//12

        else if(a==0 && b==1 && c==0 && d==1 && e==1) return ruleset5[11];//11

        else if(a==0 && b==1 && c==0 && d==1 && e==0) return ruleset5[10];//10

        else if(a==0 && b==1 && c==0 && d==0 && e==1) return ruleset5[9];//9

        else if(a==0 && b==1 && c==0 && d==0 && e==0) return ruleset5[8];//8

        else if(a==0 && b==0 && c==1 && d==1 && e==1) return ruleset5[7];//7

        else if(a==0 && b==0 && c==1 && d==1 && e==0) return ruleset5[6];//6

        else if(a==0 && b==0 && c==1 && d==0 && e==1) return ruleset5[5];//5

        else if(a==0 && b==0 && c==1 && d==0 && e==0) return ruleset5[4];//4

        else if(a==0 && b==0 && c==0 && d==1 && e==1) return ruleset5[3];//3

        else if(a==0 && b==0 && c==0 && d==1 && e==0) return ruleset5[2];//2

        else if(a==0 && b==0 && c==0 && d==0 && e==1) return ruleset5[1];//1

        else if(a==0 && b==0 && c==0 && d==0 && e==0) return ruleset5[0]; //0

        return 0;
    }
       //rule 30 {1,0,1,1,1,1,1,0}; //rule 222{1,1,0,1,1,1,1,0}; // rule 90{0,1,0,1,1,0,1,0};

}
