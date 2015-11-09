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

    public int[] getPixelsOneDimension() {
        int[] pixels = new int[getRows()*getColumns()];
        for(int i = 0; i<getRows(); i++)
            for (int j = 0; j < getColumns(); ++j) {
                pixels[(i*getColumns())+j] = mPixel[i][j];
            }

        return pixels;
    }

    public int[][] getPixels() {
        return mPixel;
    }

    public void setPixels(int[][] pixels) {
        mPixel = pixels;
    }
}
