package com.itoasis.callingapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.itoasis.callingapp.R;

public class call_screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calling_screen);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }// Replace with the correct layout name
    }
}
