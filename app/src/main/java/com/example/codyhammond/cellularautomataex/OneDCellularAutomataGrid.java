package com.example.codyhammond.cellularautomataex;

import android.graphics.Point;

/**
 * Created by codyhammond on 10/13/16.
 */

public class OneDCellularAutomataGrid extends Grid {

    private int currentRow=0;
    public OneDCellularAutomataGrid(int y,int x)
    {
        super(y,x);
        setStartPattern();
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



    @Override
    public void setStartPattern()
    {
        grid[0][grid[0].length/2]=1;
    }
}
