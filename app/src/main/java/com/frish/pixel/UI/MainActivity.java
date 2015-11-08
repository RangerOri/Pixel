package com.frish.pixel.UI;

import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.frish.pixel.FileOperator;
import com.frish.pixel.GoogleDriveFragment;
import com.frish.pixel.Model.PixelDataConverter;
import com.frish.pixel.Model.PixelMatrix;
import com.frish.pixel.R;
import com.pes.androidmaterialcolorpickerdialog.ColorPicker;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";

    private PixelView mPixelView;
    private PixelMatrix mPixelMatrix;

    private GoogleDriveFragment mGoogleDriveFragment;
    private final String GOOGLE_DRIVE_TAG = "GoogleDrive";
    private final int PIXEL_ROWS = 32;
    private final int PIXEL_COLS = 32;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mPixelView = (PixelView)findViewById(R.id.PixelView);
        setUpCanvas(PIXEL_ROWS, PIXEL_COLS);

        mGoogleDriveFragment = new GoogleDriveFragment();

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(mGoogleDriveFragment, GOOGLE_DRIVE_TAG).commit();

        setUpToolbar();
    }

    private void setUpCanvas(int rows, int cols){
        mPixelMatrix = new PixelMatrix(rows, cols);
        mPixelView.setPixelData(mPixelMatrix);

    }

    private void setUpToolbar() {

        View.OnTouchListener listener = new View.OnTouchListener() {
            private Rect rect;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    ((ImageView)v).setColorFilter(Color.argb(50, 0, 0, 0));
                    rect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
                    //return true;
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    ((ImageView)v).setColorFilter(Color.argb(0, 0, 0, 0));
                    //return true;
                }
                if(event.getAction() == MotionEvent.ACTION_MOVE){
                    if(!rect.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY())){
                        ((ImageView)v).setColorFilter(Color.argb(0, 0, 0, 0));
                        //return true;
                    }
                }
                return false;
            }
        };

        (findViewById(R.id.btnPaint)).setOnTouchListener(listener);
        (findViewById(R.id.btnNew)).setOnTouchListener(listener);
        (findViewById(R.id.btnUndo)).setOnTouchListener(listener);
        (findViewById(R.id.btnRedo)).setOnTouchListener(listener);
        (findViewById(R.id.btnZoom)).setOnTouchListener(listener);
        (findViewById(R.id.btnFill)).setOnTouchListener(listener);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("MainActivity", "onActivityResult");
        mGoogleDriveFragment.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClearButton(View view){
        mPixelView.clear();
    }

    public void onSaveButton(View view) {
         // TODO: do it in a thread.
        boolean result = FileOperator.savePng(this, "datasd.png", mPixelMatrix);

        if (result) {
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }
    }

    public void onSaveButtonWithDrive(View view) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PixelDataConverter.compressToPng(mPixelMatrix, baos);
        try {
            baos.close();
        } catch (IOException e) {
            Log.e(TAG, "Exception while starting resolution activity", e);
        }
        mGoogleDriveFragment.createFileActivity("data.png", baos.toByteArray(), "image/png");
    }

    public void onColorPick(final View view){
        int color = mPixelView.getBrushColor();
        final ColorPicker cp = new ColorPicker(MainActivity.this,
                Color.red(color), Color.green(color), Color.blue(color));

        if(view.getId() == R.id.btnPaint)
            mPixelView.setBrushMode(0);
        else if(view.getId() == R.id.btnFill)
            mPixelView.setBrushMode(1);

        cp.show();

        Button okColor = (Button)cp.findViewById(R.id.okColorButton);
        okColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /* Or the android RGB Color (see the android Color class reference) */
                mPixelView.setBrushColor(cp.getColor());
                view.setBackgroundColor(cp.getColor());
                cp.dismiss();
            }
        });
    }

    public void openFromDrive() {
        String[] mimeType = {};
        mGoogleDriveFragment.getFile(mimeType, new GoogleDriveFragment.DriveContentsCallback() {
            @Override
            public void driveContentsCallback(String driveId, String contents) {
                Toast.makeText(MainActivity.this,
                        "Contents: " + contents,
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    public void onPreviewButton(View view) {
        Intent prevIntent = new Intent(this, PreviewActivity.class);
        prevIntent.putExtra("image", mPixelView.getPixelMatrix());
        startActivity(prevIntent);
    }

    public void onNewCanvasButton(View v){

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(View.inflate(this, R.layout.new_dialog_layout, null));

        final EditText widthText = (EditText)dialog.findViewById(R.id.editWidthPixels);
        final EditText heightText = (EditText)dialog.findViewById(R.id.editHeightPixels);

        dialog.findViewById(R.id.createNewBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int width = Integer.parseInt(widthText.getText().toString());
                int height = Integer.parseInt(heightText.getText().toString());
                setUpCanvas(width, height);
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
