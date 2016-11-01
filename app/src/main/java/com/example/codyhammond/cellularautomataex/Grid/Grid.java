package com.example.codyhammond.cellularautomataex.Grid;

import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

/**
 * Created by codyhammond on 10/4/16.
 */
 public interface Grid
{
    int getOptimalCellSize(Point point);
    void updateGrid();
    void setStartPattern();
}

