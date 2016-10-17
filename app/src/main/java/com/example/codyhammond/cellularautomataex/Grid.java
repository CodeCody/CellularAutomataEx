package com.example.codyhammond.cellularautomataex;

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



    public Grid(int rows,int cols)
    {
        grid=new int[rows][cols];
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
    public void setRuleset(short[] rule)
    {
      //  ruleset=rule;
    }
}

