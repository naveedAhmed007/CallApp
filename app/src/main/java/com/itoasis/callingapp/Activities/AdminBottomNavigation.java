package com.itoasis.callingapp.Activities;

import androidx.appcompat.app.AppCompatActivity;


import androidx.core.view.MenuCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.itoasis.callingapp.Fragments.Home;
import com.itoasis.callingapp.Fragments.Payment;
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
                case R.id.summary:
                    setCurrentFragment(new Payment());
                    break;
                case R.id.history:
                    setCurrentFragment(new Home());
                    break;
                case R.id.set_prices:
                    setCurrentFragment(new Home());
                    break;
                case R.id.log_out:

                    break;
                case R.id.more:
                    showPopupMenu(bottomNavigationView);
                    break;
                case R.id.chat:
                    setCurrentFragment(new Payment());
                    break;

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
    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.inflate(R.menu.new_menu); // Replace with your actual menu resource
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()){
                    case R.id.set_prices:
                        Toast.makeText(AdminBottomNavigation.this, "aaaaaaaaaaaaaaaaaaaaaaaaaaaa", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });

        // Adjust popup menu gravity to show at the bottom end
        popupMenu.setGravity(Gravity.END);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            popupMenu.setForceShowIcon(true);
        }
       
        popupMenu.show();
    }






    }
