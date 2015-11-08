package com.frish.pixel.UI;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.frish.pixel.Model.PixelDataConverter;
import com.frish.pixel.Model.PixelMatrix;
import com.frish.pixel.R;

public class PreviewActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_preview);

        ImageView imgView = (ImageView)findViewById(R.id.imgPreview);
        PixelMatrix pixelMatrix = (PixelMatrix)getIntent().getSerializableExtra("image");
        Bitmap image = PixelDataConverter.convertToBitmap(pixelMatrix);
        imgView.setImageBitmap(image);

        ImageView excView = (ImageView)findViewById(R.id.imgExact);
        //PixelMatrix pixelMatrix = (PixelMatrix)getIntent().getSerializableExtra("image");
        //Bitmap image = PixelDataConverter.convertToBitmap(pixelMatrix);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)excView.getLayoutParams();
        params.width = image.getWidth(); params.height = image.getHeight();
        excView.setLayoutParams(params);
        excView.setImageBitmap(image);
    }

}
