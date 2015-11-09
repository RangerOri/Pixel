package com.frish.pixel.Model;

import android.graphics.Bitmap;
import android.util.DisplayMetrics;

import java.io.OutputStream;

/**
 * Created by komatsu on 5/10/15.
 */
public class PixelDataConverter {

    public static Bitmap convertToBitmap(PixelMatrix pixelMatrix) {
        int rows = pixelMatrix.getRows(); //height
        int columns = pixelMatrix.getColumns(); //width
        Bitmap bitmap = Bitmap.createBitmap(pixelMatrix.getPixelsOneDimension(), columns, rows, Bitmap.Config.ARGB_8888);
        //for (int x = 0; x < rows; ++x) {
        //    for (int y = 0; y < columns; ++y) {
        //        bitmap.setPixel(x, y, pixelMatrix.getPixelColor(x, y));
        //    }
        //}
        return bitmap;
    }

    public static boolean compressToPng(PixelMatrix pixelMatrix, OutputStream outputStream) {
        Bitmap bitmap = PixelDataConverter.convertToBitmap(pixelMatrix);
        return bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
    }

    //Used after FloodFill for example
    public static PixelMatrix convertToPixelMatrix(Bitmap bitmap){
        int rows = bitmap.getHeight();
        int columns = bitmap.getWidth();
        PixelMatrix pixelMatrix = new PixelMatrix(rows, columns);
        for (int x = 0; x < columns; ++x)
            for (int y = 0; y < rows; ++y)
                pixelMatrix.setPixelColor(x, y, bitmap.getPixel(x, y));

        return pixelMatrix;
    }
}


