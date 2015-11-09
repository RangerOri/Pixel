package com.frish.pixel;

import android.util.Log;

/**
 * Created by orifr on 09/11/2015.
 */
public class LogData {

    public static void print(String str){
        Log.e("Pixel", str);
    }

    public static void print(Exception e){
        e.printStackTrace();
    }

    public static void print(String tag, String str){
        Log.e(tag, str);
    }

    public static void print(String tag, String str, Exception e){
        Log.e(tag, str, e);
    }

    public static void printWarning(String tag, String str, Exception e){
        Log.w(tag, str, e);
    }

    public static void printInfo(String tag, String str){
        Log.i(tag, str);
    }
}
