package com.example.codyhammond.cellularautomataex;

import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by codyhammond on 10/13/16.
 */

public class GameOfLifeGrid extends Grid {

    private List<CellStateChange>cellStateChanges;
    public GameOfLifeGrid(int y,int x)
    {
        super(y,x);
        cellStateChanges=new LinkedList<>();
        setStartPattern();
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

    public List<CellStateChange> getCellStateChanges()
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




}
