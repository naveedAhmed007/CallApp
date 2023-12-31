package com.itoasis.callingapp.Activities;

import static android.app.PendingIntent.getActivity;
import static android.content.ContentValues.TAG;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.content.Intent;
import android.telecom.TelecomManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.itoasis.callingapp.Fragments.UserDetails;
import com.itoasis.callingapp.Fragments.add_user;
import com.itoasis.callingapp.R;
import com.itoasis.callingapp.utils.Singleton;

public class DashboardActivity extends AppCompatActivity {
    FirebaseFirestore db;

    // Reference to the "users" collection
    CollectionReference usersCollection;
    Singleton singleton;
    String phonenumber1,phonenumber2;
    ImageView box4ImageView,setPrices,addUser,callButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_activty);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
initVariables();



        usersCollection.orderBy("length", Query.Direction.ASCENDING).limit(1)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }


                        for (DocumentChange dc : snapshots.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                // New item added, perform your desired action here
                                DocumentSnapshot newItemSnapshot = dc.getDocument();
                                // Extract necessary data and show a toast
                                String itemName = newItemSnapshot.getString("length");
                                 phonenumber1=newItemSnapshot.getString("number1");
                                phonenumber2=newItemSnapshot.getString("number2");

                                int i=Integer.parseInt(itemName);
                                if(i==0) {
                                    box4ImageView.performClick();



                                }




                            }
                        }
                    }
                });



        // Find the box4 ImageView


        // Set an OnClickListener for box4
        box4ImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                // Open your desired activity here

                Intent intent = new Intent(DashboardActivity.this, AdminBottomNavigation.class);
                startActivity(intent);
            }
        });
        setPrices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AdminBottomNavigation.class);

                intent.putExtra("key", "addUser");
                // start the Intent
                startActivity(intent);
                Toast.makeText(DashboardActivity.this, Singleton.getInstance().getUserEmail(), Toast.LENGTH_SHORT).show();


            }
        });
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AdminBottomNavigation.class);

                intent.putExtra("key", "home");
                singleton.setCallScreenFrom("admin");
                // start the Intent
                startActivity(intent);


            }
        });
    }
    private void initVariables() {
        db = FirebaseFirestore.getInstance();
        usersCollection=db.collection("numbers");
        singleton=Singleton.getInstance();
         box4ImageView = findViewById(R.id.box4);
         setPrices=findViewById(R.id.box3);
        addUser=findViewById(R.id.box5);
        callButton =findViewById(R.id.callButton);
    }
}
