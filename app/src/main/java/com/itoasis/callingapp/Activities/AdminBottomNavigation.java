package com.itoasis.callingapp.Activities;

import static android.content.ContentValues.TAG;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import androidx.core.app.ActivityCompat;
import androidx.core.view.MenuCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telecom.TelecomManager;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.itoasis.callingapp.Fragments.History;
import com.itoasis.callingapp.Fragments.Home;
import com.itoasis.callingapp.Fragments.Message;
import com.itoasis.callingapp.Fragments.Payment;
import com.itoasis.callingapp.Fragments.Summary;
import com.itoasis.callingapp.Fragments.UserDetails;
import com.itoasis.callingapp.Fragments.add_user;
import com.itoasis.callingapp.Fragments.setPrices;
import com.itoasis.callingapp.R;
import com.itoasis.callingapp.utils.Singleton;

public class AdminBottomNavigation extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_bottom_navigation);
        getSupportActionBar().hide();
        Intent intent = getIntent();
        String str = intent.getStringExtra("key");
        switch(str){
            case "addUser":
                setCurrentFragment(new add_user());

                break;
            case "home":
                setCurrentFragment(new Home());

                break;
            default:
                setCurrentFragment(new UserDetails());
                break;
        }

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.details:
                    setCurrentFragment(new UserDetails());
                    break;
                case R.id.chat:
                    setCurrentFragment(new Message());
                    break;

                case R.id.set_prices:
                    setCurrentFragment(new setPrices());
                    break;
                                case R.id.more:
                    showPopupMenu(bottomNavigationView);
                    break;


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
                    case R.id.summary:
                        setCurrentFragment(new Summary());
                        break;case R.id.history:
                        setCurrentFragment(new History());
                        break;
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
