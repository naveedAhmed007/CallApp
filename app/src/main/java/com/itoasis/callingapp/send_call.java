package com.itoasis.callingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.itoasis.callingapp.Fragments.History;
import com.itoasis.callingapp.Fragments.Home;
import com.itoasis.callingapp.Fragments.Notification;
import com.itoasis.callingapp.Fragments.Search;
import com.itoasis.callingapp.Fragments.profile_call;
import com.itoasis.callingapp.Fragments.send_Notification;

public class send_call extends AppCompatActivity {
FrameLayout frameLayout;
    Fragment selectedFragment;
BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottom_navigation);
        frameLayout=findViewById(R.id.f1);
        bottomNavigationView=findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setBackground(null);
        bottomNavigationView.getMenu().getItem(2).setEnabled(false);
        getSupportActionBar().hide();
        getSupportFragmentManager().beginTransaction().replace(R.id.f1, new Home()).commit();

        selectedFragment = new Home();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.profile:
                        selectedFragment = new profile_call();
                        break;
                    case R.id.placeholder:
                        selectedFragment = new Home();
                        break;
                    case R.id.chat:
                        selectedFragment = new profile_call();
                        break;
                    case R.id.history:
                        selectedFragment = new History();
                        break;
                    case R.id.Payment:
                        selectedFragment = new Notification();
                        break;
                    default:
                        return false;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.f1,
                        selectedFragment).commit();
                return true;

            }
        });

        
//
//        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
//        bottomNavigationView.setBackground(null);
//        bottomNavigationView.getMenu().getItem(2).setEnabled(false);
        // You can perform any initialization or actions for the send_call activity here
    }
}
