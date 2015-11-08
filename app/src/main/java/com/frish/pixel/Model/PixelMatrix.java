package com.frish.pixel.Model;

import java.io.Serializable;

/**
 * Created by komatsu on 5/6/15.
 */
public class PixelMatrix implements Serializable{

    // PixelMatrix data
    private int rows;
    private int columns;
    private int[][] mPixel;

    public PixelMatrix() {
        this(16, 16);
    }

    public PixelMatrix(int rows, int columns) {
        setRows(rows);
        setColumns(columns);
        clearMatrix();
    }

    public void clearMatrix(){
        mPixel = new int[rows][columns];
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public void setPixelColor(int x, int y, int color) {
        mPixel[x][y] = color;
    }

    public int getPixelColor(int x, int y) {
        return mPixel[x][y];
    }


}
