package com.frish.pixel;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;

import com.frish.pixel.Model.PixelDataConverter;
import com.frish.pixel.Model.PixelMatrix;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by komatsu on 5/6/15.
 */
public class FileOperator {
    static final String PIXEL_DIR = "PixelMatrix";

    public static boolean savePng(Activity activity, String fileName, PixelMatrix pixelMatrix) {
        boolean result;

        String filePath =
                Environment.getExternalStorageDirectory() + "/" + PIXEL_DIR + "/" + fileName;
        File file = new File(filePath);
        if (!file.getParentFile().isDirectory() && !file.getParentFile().mkdirs()) {
            return false;
        }

        FileOutputStream fos;
        try {
            fos = new FileOutputStream(file, true);
            //fos = activity.openFileOutput(fileName, Context.MODE_PRIVATE);
            result = PixelDataConverter.compressToPng(pixelMatrix, fos);
            fos.close();

            /*
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(out, "UTF-8"));

            writer.append(data);
            writer.close();
            */
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return result;
    }

    public static boolean saveData(Activity activity, String fileName, String data) {
        OutputStream out;
        try {
            out = activity.openFileOutput(fileName, Context.MODE_PRIVATE);

            /*
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(out, "UTF-8"));

            writer.append(data);
            writer.close();
            */
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
