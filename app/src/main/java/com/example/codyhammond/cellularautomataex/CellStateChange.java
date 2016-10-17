package com.example.codyhammond.cellularautomataex;

/**
 * Created by codyhammond on 10/14/16.
 */

public class CellStateChange
{
    private int x,y,status;

    public CellStateChange(int row,int col,int status)
    {
        this.x=col;
        this.y=row;
        this.status=status;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public int getStatus()
    {
        return status;
    }
}
