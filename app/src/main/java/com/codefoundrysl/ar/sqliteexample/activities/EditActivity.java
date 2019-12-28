package com.codefoundrysl.ar.sqliteexample.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.codefoundrysl.ar.sqliteexample.R;

public class EditActivity extends AppCompatActivity {

    private EditText eTxtId, eTxtName, eTxtDescription, eTxtPrice, eTxtThumb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
    }

    public void onClickEdit(View v) {

    }

    public void onClickGoBack(View v) {

    }
}
