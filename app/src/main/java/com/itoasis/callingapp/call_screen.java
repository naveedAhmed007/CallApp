package com.itoasis.callingapp;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class call_screen extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calling_screen);

        // Hide the action bar
        getSupportActionBar().hide();

        // Add any additional initialization or logic you need for this activity
    }
}
