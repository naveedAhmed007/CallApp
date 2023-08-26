package com.itoasis.callingapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.role.RoleManager;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import com.itoasis.callingapp.R;

public class MainActivity2 extends AppCompatActivity {
    private static final int DEFAULT_DIALER_REQUEST_ID = 83;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        requestDefaultDialerRole();
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == DEFAULT_DIALER_REQUEST_ID) {
            if (resultCode != Activity.RESULT_OK) {
                Toast.makeText(this, "Please set this app as default dialer app.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Settings.ACTION_MANAGE_DEFAULT_APPS_SETTINGS));
            }
            else {
                startActivity(new Intent(this, MainActivity.class));
            }
        }
    }

    public void requestDefaultDialerRole() {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            RoleManager roleManager = (RoleManager) getSystemService(ROLE_SERVICE);
            Intent intent = roleManager.createRequestRoleIntent(RoleManager.ROLE_DIALER);
            startActivityForResult(intent, DEFAULT_DIALER_REQUEST_ID);
        }
    }
}