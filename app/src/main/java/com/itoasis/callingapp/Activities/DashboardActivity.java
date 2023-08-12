package com.itoasis.callingapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.itoasis.callingapp.R;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_activty);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }// Replace with the correct layout name
    }
}
