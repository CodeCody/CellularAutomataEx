package com.example.codyhammond.cellularautomataex.Grid;

import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

/**
 * Created by codyhammond on 10/4/16.
 */
 abstract public class Grid
{
    public int [][] grid=null;
    public int [] ruleset5={1,1,0,0,0,0,1,1,1,0,1,1,1,1,0,0,1,1,1,0,0,0,1,1,1,0,0,1,0,0,0,0};//solitons C1 {0,1,0,1,1,1,1,1,0,0,1,0,1,0,1,0,1,0,0,1,1,1,0,0,1,1,0,0,1,0,0,0};//threads{1,0,0,1,0,1,1,1,1,0,0,0,1,1,1,0,1,1,0,0,1,1,1,0,1,1,1,0,0,1,0,0};//tetrahedrons {1,1,0,0,0,0,1,1,1,0,1,1,1,1,0,0,1,1,1,0,0,0,1,1,1,0,0,1,0,0,0,0};// kites {1,0,1,1,1,1,1,1,1,0,0,0,1,0,1,0,0,1,0,1,1,1,0,0,1,1,0,1,1,0,0,0};// scaffolding {0,1,1,1,1,1,1,0,1,0,0,0,0,1,1,0,1,0,0,1,0,1,1,0,1,1,0,1,1,1,1,0};
    public int [] ruleset={0,1,0,1,1,0,1,0};//unknown{0,1,0,0,1,0,0,1};//inverse slanted triangle{0,1,1,0,0,1,1,0};//slanted triangle{1,0,0,1,1,0,0,1};//inverse triangle mania{0,1,1,1,1,1,1,0};   //triangle mania{1,0,0,0,0,0,0,1}; //rule 110 {0,1,1,0,1,1,1,0}//rule 30 {1,0,1,1,1,1,1,0}; //rule 222{1,1,0,1,1,1,1,0}; // rule 90{0,1,0,1,1,0,1,0};

    protected int cellSizeinPixels=0;



    public Grid(int rows,int cols,int cellSizeinPixels)
    {
        grid=new int[rows/cellSizeinPixels][cols/cellSizeinPixels];
        this.cellSizeinPixels=cellSizeinPixels;
    }

    public int getHeight()
    {
        return grid.length;
    }

    public int getWidth()
    {
        return grid[0].length;
    }

    public void setPixelSize(int size)
    {
        cellSizeinPixels=size;
    }


    abstract void updateGrid();

    abstract public void setStartPattern();

   // abstract public void getOptimalCellSize(Point displaySize, boolean isLandscape);

    public void setRuleset(short[] rule)
    {
      //  ruleset=rule;
    }
}

