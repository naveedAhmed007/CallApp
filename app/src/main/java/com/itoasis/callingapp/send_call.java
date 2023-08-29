package com.itoasis.callingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.itoasis.callingapp.Fragments.History;
import com.itoasis.callingapp.Fragments.Home;
import com.itoasis.callingapp.Fragments.Message;
import com.itoasis.callingapp.Fragments.Notification;
import com.itoasis.callingapp.Fragments.Payment;
import com.itoasis.callingapp.Fragments.PhoneCall;
import com.itoasis.callingapp.Fragments.Search;
import com.itoasis.callingapp.Fragments.profile_call;
import com.itoasis.callingapp.Fragments.send_Notification;
import com.itoasis.callingapp.utils.Singleton;

public class send_call extends AppCompatActivity {
FrameLayout frameLayout;
    Fragment selectedFragment;
    Singleton singleton;
    private com.google.android.material.floatingactionbutton.FloatingActionButton fab;

BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottom_navigation);
        singleton=Singleton.getInstance();
        fab=findViewById(R.id.fab);
        frameLayout=findViewById(R.id.f1);
        bottomNavigationView=findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setBackground(null);
        bottomNavigationView.getMenu().getItem(2).setEnabled(false);
        getSupportActionBar().hide();
        getSupportFragmentManager().beginTransaction().replace(R.id.f1, new Message()).commit();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.f1, new Home()).commit();

            }
        });


        selectedFragment = new profile_call();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.profile:

                        selectedFragment = new Home();
                        singleton.setCallScreenFrom("client");
                        break;
                    case R.id.placeholder:
                        selectedFragment = new Home();
                        break;
                    case R.id.chat:
                        selectedFragment = new Message();
                        break;
                    case R.id.history:
                        selectedFragment = new History();
                        break;
                    case R.id.Payment:
                        selectedFragment = new Payment();
                        break;
                    default:
                        return false;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.f1,
                        selectedFragment).commit();
                return true;

            }
        });

        

    }
}
