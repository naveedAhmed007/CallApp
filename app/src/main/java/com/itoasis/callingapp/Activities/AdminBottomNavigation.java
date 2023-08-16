package com.itoasis.callingapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.itoasis.callingapp.Fragments.Home;
import com.itoasis.callingapp.R;

public class AdminBottomNavigation extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_bottom_navigation);
        getSupportActionBar().hide();
        getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, new Home()).commit();



        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.prices:
                    setCurrentFragment(new Home());
                    break;
                case R.id.details:
                    setCurrentFragment(new Home());
                    break;
                case R.id.chat:
                    setCurrentFragment(new Home());
                    break;
                case R.id.summary:
                    setCurrentFragment(new Home());
                    break;
//                case R.id.history:
//                    setCurrentFragment(new Home());
//                    break;
//                case R.id.logout:
//                    setCurrentFragment(new Home());
//                    break;
            }
            return true;
        });
    }

    private void setCurrentFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flFragment, fragment);
        fragmentTransaction.commit();
    }
    }
