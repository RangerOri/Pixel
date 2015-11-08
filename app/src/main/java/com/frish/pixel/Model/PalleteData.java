package com.frish.pixel.Model;

import android.graphics.Color;

/**
 * Created by orifr on 28/10/2015.
 */
public class PalleteData {

    // Palette data
    private int mPaletteSize = 6;
    private int[] mPalette;

    public PalleteData(){

        // Initialize color palette.
        mPalette = new int[mPaletteSize];
        mPalette[0] = Color.BLACK;
        mPalette[1] = Color.WHITE;
        mPalette[2] = Color.RED;
        mPalette[3] = Color.GREEN;
        mPalette[4] = Color.BLUE;
        mPalette[5] = Color.YELLOW;
    }

    //TODO - Set custom colors by user
    public PalleteData(int size){
        mPaletteSize = size;
        mPalette = new int[mPaletteSize];
    }

    public int getMainColor(){
        return mPalette[0];
    }

    public int getSecondaryColor(){
        return mPalette[1];
    }

}
