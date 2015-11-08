package com.frish.pixel.UI;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.frish.pixel.R;

import java.util.concurrent.Callable;

public class NewFileDialog extends Dialog {

    int width;
    int height;
    Context context;
    Callable callback;

    public NewFileDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View view = inflater.inflate(R.layout.new_dialog_layout, null);
        builder.setView(view);

        final EditText widthText = (EditText)view.findViewById(R.id.editWidthPixels);
        final EditText heightText = (EditText)view.findViewById(R.id.editHeightPixels);

        view.findViewById(R.id.createNewBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                width = Integer.parseInt(widthText.getText().toString());
                height = Integer.parseInt(heightText.getText().toString());
                try {
                    callback.call();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void setCallback(Callable callback) {
        this.callback = callback;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
