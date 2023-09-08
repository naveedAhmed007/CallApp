package com.itoasis.callingapp.Fragments;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telecom.TelecomManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.itoasis.callingapp.R;
import com.itoasis.callingapp.call_screen;
import com.itoasis.callingapp.utils.CallListHelper;
import com.itoasis.callingapp.utils.CallManager;
import com.itoasis.callingapp.utils.MyDatabaseHelper;
import com.itoasis.callingapp.utils.Singleton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Home extends Fragment {
    private EditText firstNumberEditText,secondNumberEditText;
    FirebaseFirestore firestore;
    Button button;

    // Reference to the "users" collection
    CollectionReference usersCollection;
    String phonenumber1,phonenumber2;
    Singleton singleton;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        singleton=Singleton.getInstance();
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        firstNumberEditText= rootView.findViewById(R.id.first_Number);
        secondNumberEditText= rootView.findViewById(R.id.second_number);
        button=rootView.findViewById(R.id.button);

        // Find the ImageView for the postfix icon inside the rootView
        AppCompatImageView postfixIcon = rootView.findViewById(R.id.contact_one);

        // Set a click listener for the postfix icon
        postfixIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the contacts app
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, 1); // You can use any request code you prefer
            }
        });

        final TextView countryCodeTextView = rootView.findViewById(R.id.countryCodeTextView);
        ImageView countryCodeDropdown = rootView.findViewById(R.id.countryCodeDropdown);
        countryCodeDropdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show the country code dropdown using a PopupMenu
                showCountryCodeDropdown(v, countryCodeTextView);
            }
        });
        ImageView secondIcon = rootView.findViewById(R.id.passworCountryCodeDropdownExpend);

        // Set a click listener for the second icon
        secondIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show the country code dropdown using a PopupMenu
                showCountryCodeDropdown(v, countryCodeTextView);
            }
        });

        final TextView passwordCountryCodeTextView = rootView.findViewById(R.id.passwordCountryCodeTextView);
        ImageView passwordCountryCodeDropdown = rootView.findViewById(R.id.passwordCountryCodeDropdown);
        passwordCountryCodeDropdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show the country code dropdown using a PopupMenu
                showCountryCodeDropdown(v, passwordCountryCodeTextView);
            }
        });
        ImageView second_expend_icon = rootView.findViewById(R.id.passwordCountryCodeDropdownExpend);

        // Set a click listener for the second icon
        second_expend_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show the country code dropdown using a PopupMenu
                showCountryCodeDropdown(v, passwordCountryCodeTextView);
            }
        });

        // Find the ImageView for the postfix icon
        Button call_button = rootView.findViewById(R.id.call_button);

        // Set a click listener for the postfix icon
        call_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String inputNumber1=secondNumberEditText.getText().toString().trim();
                String inputNumber=firstNumberEditText.getText().toString().trim();
                String callerName = retrieveCallerName(inputNumber);

                String callerName1 = retrieveCallerName(inputNumber1);
                String x=callerName+" "+callerName1;
                singleton.setCallerName(x);
                    if (!inputNumber.isEmpty() && !inputNumber1.isEmpty()) {
                        singleton.setPhoneNumber2(inputNumber1);
                        singleton.setPhoneNumber1(inputNumber);
                        @SuppressLint("ServiceCast") TelecomManager telecomManager = (TelecomManager) getContext().getSystemService(Context.TELECOM_SERVICE);
                        Uri uri = Uri.fromParts("tel", inputNumber, null);
                        Bundle extras = new Bundle();
                        extras.putBoolean(TelecomManager.EXTRA_START_CALL_WITH_SPEAKERPHONE, false);

                        if (ActivityCompat.checkSelfPermission(getContext().getApplicationContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {

                            if (telecomManager.getDefaultDialerPackage().equals(getContext().getApplicationContext().getPackageName())) {
                                telecomManager.placeCall(uri, extras);
                            } else {
                                Uri phoneNumber = Uri.parse("tel:" + inputNumber);
                                Intent callIntent = new Intent(Intent.ACTION_CALL, phoneNumber);
                                callIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(callIntent);
                            }
                        } else {
                            Toast.makeText(getContext().getApplicationContext(), "Please allow permission", Toast.LENGTH_SHORT).show();
                        }
                    }





            }
        });

        firestore= FirebaseFirestore.getInstance();
        usersCollection=firestore.collection("numbers");
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
                                String email=newItemSnapshot.getString("email");
                                singleton.setClientEmailForTime(email);



                                int i=Integer.parseInt(itemName);
                                if(i==0) {

//                                  button.performClick();



                                }




                            }
                        }
                    }
                });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                String callerName = retrieveCallerName(phonenumber1);
//
//                String callerName1 = retrieveCallerName(phonenumber2);
//                String x=callerName+" "+callerName1;
//                singleton.setCallerName(x);


                singleton.setPhoneNumber2(phonenumber2);
                @SuppressLint("ServiceCast") TelecomManager telecomManager = (TelecomManager) getActivity().getSystemService(Context.TELECOM_SERVICE);
                Uri uri = Uri.fromParts("tel", phonenumber1, null);
                Bundle extras = new Bundle();
                extras.putBoolean(TelecomManager.EXTRA_START_CALL_WITH_SPEAKERPHONE, false);

                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {

                    if (telecomManager.getDefaultDialerPackage().equals(getContext().getPackageName())){
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
                    Toast.makeText(getContext(), "Please allow permission", Toast.LENGTH_SHORT).show();
                }
            }
        });








        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == 1) {
                // Handle contact selection for contact one button
                // You can use the data Intent to retrieve the selected contact details

                // Assuming you want to retrieve the contact's phone number
                String phoneNumber = retrievePhoneNumber(data);

                // Set the retrieved phone number in the EditText field
                firstNumberEditText.setText(phoneNumber);
            }
        }
    }

    // Helper method to retrieve the selected contact's phone number
    @SuppressLint("Range")
    private String retrievePhoneNumber(Intent data) {
        String phoneNumber = null;

        try {
            // Get the contact URI from the Intent
            Uri contactData = data.getData();

            // Query the contact data for the phone number
            String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER};
            Cursor cursor = getActivity().getContentResolver().query(contactData, projection, null, null, null);

            if (cursor.moveToFirst()) {
                phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            }

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return phoneNumber;
    }

    private void showCountryCodeDropdown(View anchorView, final TextView countryCodeTextView) {
        PopupMenu popupMenu = new PopupMenu(requireContext(), anchorView);
        popupMenu.getMenuInflater().inflate(R.menu.country_code_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // Handle the selected country code here
                String selectedCountryCode = item.getTitle().toString();

                // Set the selected country code in the TextView with icon
                countryCodeTextView.setText(selectedCountryCode);
                // countryCodeTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.contact_list_icon, 0, 0, 0);

                return true;
            }
        });

        popupMenu.show();
    }

    private void openNewActivity() {
        Intent intent = new Intent(requireContext(), call_screen.class);
        startActivity(intent);
    }


    public void addData(Map<String, Object> numbers){
        firestore.collection("numbers")
                .add(numbers)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        Toast.makeText(getContext(), "numbers are added", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }
    @SuppressLint("Range")
    private String retrieveCallerName(String phoneNumber) {
        ContentResolver contentResolver = getContext().getContentResolver();
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

    @SuppressLint("Range")
    @Override
    public void onResume() {
        super.onResume();

        Query query =  usersCollection.whereEqualTo("isCallBusy", true).limit(1);

        // Execute the query
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {

            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                    // Handle each document here
                    String documentId = documentSnapshot.getId();
                    Map<String, Object> data = documentSnapshot.getData();

                    usersCollection.document(documentId).update("isCallBusy", false)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // Document updated successfully
//                                    Toast.makeText(YourActivity.this, "isCallBusy set to false", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Handle any errors that occurred during the update
//                                    Toast.makeText(YourActivity.this, "Failed to update isCallBusy", Toast.LENGTH_SHORT).show();
                                }
                            });

                }
            }
        });

        MyDatabaseHelper dbHelper = new MyDatabaseHelper(getContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

// Check if the table exists
        boolean tableExists = false;
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='MyTable'", null);
        if (cursor != null) {
            tableExists = cursor.getCount() > 0;
            cursor.close();
        }

        if (tableExists) {
            String[] projection = {"id", "name","email"};

            cursor = db.query(
                    "MyTable",
                    projection,
                    null,
                    null,
                    null,
                    null,
                    null,
                    "1"  // LIMIT 1 to retrieve only the first row
            );

            if (cursor.moveToFirst()) {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String email = cursor.getString(cursor.getColumnIndex("email"));


                try {
                    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());


                    String currentTime = timeFormat.format(Calendar.getInstance().getTime());

                    // Parse the start and end times as Date objects
                    Date startTime = timeFormat.parse(name);
                    Date endTime = timeFormat.parse(currentTime);

                    // Calculate the time difference in milliseconds
                    long timeDifferenceMillis = endTime.getTime() - startTime.getTime();

                    // Convert the time difference from milliseconds to minutes
                    long timeDifferenceMinutes = (timeDifferenceMillis / (60 * 1000))%60;
                    long totalSeconds = (int) (timeDifferenceMillis / 1000);
                    long minutes = totalSeconds / 60; // Calculate the minutes
                    long seconds = totalSeconds % 60;


                    String time=minutes+" minutes &" +seconds+" seconds";


                    // Create a reference to the "users" collection
                    CollectionReference usersRef = firestore.collection("users");

// Create a query to find documents where the "email" field matches the provided email
                    Query query1 = usersRef.whereEqualTo("email",email);

// Execute the query
                    query1.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    // Access the data in the document
                                    String documentId = document.getId();



                                    Map<String, Object> updates = new HashMap<>();
                                    updates.put("remainingCredit", time);

                                    // Update the document with the new name
                                    usersRef.document(documentId).update(updates)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d("Firestore", "Document updated successfully.");
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.e("Firestore", "Error updating document: " + e.getMessage());
                                                }
                                            });

                                }
                            } else {
                                Log.d("Firestore", "Error getting documents: ", task.getException());
                            }
                        }
                    });



                } catch (ParseException e) {
                    e.printStackTrace();
                }


                // Display the retrieved data
                Log.d("Data====================================", "ID: " + id + ", Name: " + name + ", Email: " + email);

                // Delete the retrieved record
                String selection = "id=?";
                String[] selectionArgs = {String.valueOf(id)};
                db.delete("MyTable", selection, selectionArgs);
            } else {
                // No records found
                Log.d("Data", "No records found.");
            }

            cursor.close();
        } else {
            // Table doesn't exist
            Log.d("Data", "Table 'MyTable' does not exist.");
        }


        db.close();



    }
    public void getDatafromTable(){
        byte value=2;
        MyDatabaseHelper dbHelper = new MyDatabaseHelper(getContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

// Check if the table exists
        boolean tableExists = false;
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='MyTable1'", null);
        if (cursor != null) {
            tableExists = cursor.getCount() > 0;
            cursor.close();
        }

        if (tableExists) {
            String[] projection = {"id", "name"};

            cursor = db.query(
                    "MyTable1",
                    projection,
                    null,
                    null,
                    null,
                    null,
                    null,
                    "1"  // LIMIT 1 to retrieve only the first row
            );

            if (cursor.moveToFirst()) {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));

                // Display the retrieved data
                Log.d("Data1===============================", "ID: " + id + ", Name: " + name);
                if(name!=null){
                    Toast.makeText(getContext(), name, Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getContext(), "no name found", Toast.LENGTH_SHORT).show();
                }



                // Delete the retrieved record
                String selection = "id=?";
                String[] selectionArgs = {String.valueOf(id)};
                db.delete("MyTable", selection, selectionArgs);
            } else {
                // No records found
                Log.d("Data11111111111111111111111111111111111111", "No records found.");
            }

            cursor.close();
        } else {
            // Table doesn't exist
            Log.d("Data111111111111111111111111", "Table 'MyTable1' does not exist.");
        }

        db.close();
    }




}
