package com.frish.pixel.UI;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.frish.pixel.Model.FloodFill;
import com.frish.pixel.Model.PixelDataConverter;
import com.frish.pixel.Model.PixelMatrix;

import java.util.Calendar;

public class PixelView extends View
{
    private int numColumns, numRows;
    private float cellWidth, cellHeight;

    private enum BRUSH_MODE {DRAW, FILL};
    private BRUSH_MODE brushMode = BRUSH_MODE.DRAW;
    private Paint brushPaint;
    private Paint blackPaint;

    private PixelMatrix mPixelMatrix;

    private Context context;
    private static final int MAX_CLICK_DURATION = 50;

    private long startClickTime;

    //private boolean[][] cellChecked;

    public PixelView(Context context)
    {
        this(context, null);
    }

    public PixelView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        this.context = context;
        blackPaint = new Paint();
        brushPaint = new Paint();
        brushPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        brushPaint.setColor(Color.BLACK);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
        calculateDimensions();
    }

    private void calculateDimensions()
    {
        int height = getHeight();
        int width = getWidth();

        if (numColumns == 0 || numRows == 0 || width == 0 || height == 0)
            return;

        width = height < width ? height : width;

        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = width;
        setLayoutParams(layoutParams);

        cellWidth = (float)width / numColumns;
        cellHeight = (float)width / numRows;

        //cellChecked = new boolean[numColumns][numRows];

        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        Log.d("PixelMatrix Log", String.format("Screnn Width:%d, Screen Height:%d, Cell Width:%f, Cell Height:%f, " +
                        "Num Cells Columns:%d, Num Cells Rows:%d, View Width:%d, View Height:%d", displaymetrics.widthPixels, displaymetrics.heightPixels,
                cellWidth, cellHeight, numColumns, numRows, width, height));

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        if (numColumns == 0 || numRows == 0)
            return;

        //Draw canvas background
        canvas.drawColor(Color.WHITE);

        int width = getWidth();
        int height = getHeight();

        for (int i = 0; i < numColumns; i++)
        {
            for (int j = 0; j < numRows; j++)
            {
                int pixelColor = mPixelMatrix.getPixelColor(j, i);
                if (pixelColor != 0)
                {
                    Paint pixelPaint = new Paint();
                    pixelPaint.setColor(pixelColor);
                    canvas.drawRect(i * cellWidth, j * cellHeight, (i + 1) * cellWidth, (j + 1) * cellHeight, pixelPaint);
                }
            }
        }

        for (int i = 1; i < numColumns; i++)
        {
            canvas.drawLine(i * cellWidth, 0, i * cellWidth, height, blackPaint);
        }

        for (int i = 1; i < numRows; i++)
        {
            canvas.drawLine(0, i * cellHeight, width, i * cellHeight, blackPaint);
        }

        //Set Borders for picture
        canvas.drawLine(0, 0, canvas.getWidth(), 0, blackPaint);
        canvas.drawLine(0, 0, 0, canvas.getHeight(), blackPaint);
        canvas.drawLine(canvas.getWidth() - 1, 0, canvas.getWidth() - 1, canvas.getHeight(), blackPaint);
        canvas.drawLine(0, canvas.getHeight() - 1, canvas.getWidth(), canvas.getHeight() - 1, blackPaint);
    }

    /**
     * This function draw or fills the target area
     * @param row
     * @param column
     *
     */
    private void drawOnPixel(int row, int column){
        int brushColor = brushPaint.getColor();
        if(brushMode == BRUSH_MODE.DRAW)
            mPixelMatrix.setPixelColor(row, column, brushColor);
        else if(brushMode == BRUSH_MODE.FILL){
            int pixelColor = mPixelMatrix.getPixelColor(row, column);
            Bitmap img = PixelDataConverter.convertToBitmap(mPixelMatrix);
            img = FloodFill.floodFill(img, new Point(column, row), pixelColor, brushColor);
            //qlff.floodFill(row, column);
            mPixelMatrix = PixelDataConverter.convertToPixelMatrix(/*qlff.getImage()*/img);
        }

        invalidate();
    }

    private void calculateEvent(float x, float y){
        int column = (int)(x / cellWidth);
        int row = (int)(y/ cellHeight);

        if(column >= 0 && column < numColumns && row >= 0 && row < numRows)
            drawOnPixel(row, column);

        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        long clickDuration = Calendar.getInstance().getTimeInMillis() - startClickTime;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startClickTime = Calendar.getInstance().getTimeInMillis();
                break;
            case MotionEvent.ACTION_UP:
                if (clickDuration > MAX_CLICK_DURATION) {
                    calculateEvent(event.getX(), event.getY());
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if(brushMode != BRUSH_MODE.FILL)
                    calculateEvent(event.getX(), event.getY());
                break;
        }
        return true;
    }


    public void clear(){
        mPixelMatrix.clearMatrix();
        invalidate();
    }

    public int getBrushColor() {
        return brushPaint.getColor();
    }
    public void setBrushColor(int brushPaint) {
        this.brushPaint.setColor(brushPaint);
    }
    public void setNumColumns(int numColumns)
    {
        this.numColumns = numColumns;
    }
    public int getNumColumns()
    {
        return numColumns;
    }
    public void setNumRows(int numRows)
    {
        this.numRows = numRows;
    }
    public int getNumRows()
    {
        return numRows;
    }


    public void setPixelData(PixelMatrix pixelMatrix) {
        mPixelMatrix = pixelMatrix;
        setNumRows(mPixelMatrix.getRows());
        setNumColumns(mPixelMatrix.getColumns());
        calculateDimensions();
    }
    public PixelMatrix getPixelMatrix(){
        return mPixelMatrix;
    }
    /**
     * Set mode for brush
     * @param mode 0 = DRAW, 1 = FILL
     */
    public void setBrushMode(int mode){
        if(mode == 0)
            brushMode = BRUSH_MODE.DRAW;
        else
            brushMode = BRUSH_MODE.FILL;
    }
}