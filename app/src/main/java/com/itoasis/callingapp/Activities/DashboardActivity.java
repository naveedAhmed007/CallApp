package com.itoasis.callingapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.itoasis.callingapp.R;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_activty);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Find the box4 ImageView
        ImageView box4ImageView = findViewById(R.id.box4);

        // Set an OnClickListener for box4
        box4ImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open your desired activity here
                Intent intent = new Intent(DashboardActivity.this, AdminBottomNavigation.class);
                startActivity(intent);
            }
        });
    }
}
