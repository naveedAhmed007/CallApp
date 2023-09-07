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
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telecom.TelecomManager;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
    FirebaseFirestore db;

    CollectionReference usersCollection;
    Singleton singleton;
    Button button2;
    String phonenumber1,phonenumber2;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_bottom_navigation);
        button2=findViewById(R.id.button2);
        singleton=Singleton.getInstance();
        db = FirebaseFirestore.getInstance();
        usersCollection=db.collection("numbers");

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String callerName = retrieveCallerName(phonenumber1);

                String callerName1 = retrieveCallerName(phonenumber2);
                String x=callerName+" "+callerName1;
                singleton.setCallerName(x);
                singleton.setPhoneNumber1(phonenumber1);
                singleton.setPhoneNumber2(phonenumber2);
                @SuppressLint("ServiceCast") TelecomManager telecomManager = (TelecomManager) getSystemService(Context.TELECOM_SERVICE);
                Uri uri = Uri.fromParts("tel", phonenumber1, null);
                Bundle extras = new Bundle();
                extras.putBoolean(TelecomManager.EXTRA_START_CALL_WITH_SPEAKERPHONE, false);

                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {

                    if (telecomManager.getDefaultDialerPackage().equals(getApplicationContext().getPackageName())){
                        telecomManager.placeCall(uri, extras);
                    }
                    else{
                        Uri phoneNumber = Uri.parse("tel:" + phonenumber1);
                        Intent callIntent = new Intent(Intent.ACTION_CALL, phoneNumber);
                        callIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(callIntent);
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "Please allow permission", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
                                String documentId = newItemSnapshot.getId();
                                singleton.setDocumentId(documentId);



                                int i=Integer.parseInt(itemName);
                                if(i==0) {
                                    button2.performClick();



                                }




                            }
                        }
                    }
                });



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




    @SuppressLint("Range")
    private String retrieveCallerName(String phoneNumber) {
        ContentResolver contentResolver = getContentResolver();
        String callerName = "";

        try {
            Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
            String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME};

            Cursor cursor = contentResolver.query(uri, projection, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                callerName = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return callerName;
    }

    }
