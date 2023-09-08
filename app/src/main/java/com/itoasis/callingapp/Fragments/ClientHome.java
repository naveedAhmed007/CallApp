package com.itoasis.callingapp.Fragments;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.itoasis.callingapp.R;
import com.itoasis.callingapp.call_screen;
import com.itoasis.callingapp.utils.CallListHelper;
import com.itoasis.callingapp.utils.CallManager;
import com.itoasis.callingapp.utils.Singleton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ClientHome extends Fragment {
    private EditText firstNumberEditText,secondNumberEditText;
    FirebaseFirestore db;

    Handler handler = new Handler();
    Singleton singleton;
    Button call_button;
    Boolean clientBusy=false;
    private TextView toastTextView;
    private AppCompatImageButton cancelToastButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        db= FirebaseFirestore.getInstance();
        singleton=Singleton.getInstance();
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_client_home, container, false);
        firstNumberEditText= rootView.findViewById(R.id.first_Number);
        secondNumberEditText= rootView.findViewById(R.id.second_number);

        toastTextView = rootView.findViewById(R.id.toast_text); // Initialize toastTextView
        cancelToastButton = rootView.findViewById(R.id.cancel_toast);

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
        cancelToastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call_button.setEnabled(true);
                // Toggle visibility of the toast and cancel button
                if (toastTextView.getVisibility() != View.GONE) {
                    toastTextView.setVisibility(View.GONE);
                    cancelToastButton.setVisibility(View.GONE);
                } else {
                    toastTextView.setVisibility(View.VISIBLE);
                    cancelToastButton.setVisibility(View.VISIBLE);
                }}});

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
        call_button = rootView.findViewById(R.id.call_button);

        // Set a click listener for the postfix icon
        call_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> numbers = new HashMap<>();
                String inputNumber1=secondNumberEditText.getText().toString().trim();
                String inputNumber=firstNumberEditText.getText().toString().trim();
                String callerName = retrieveCallerName(inputNumber);

                String callerName1 = retrieveCallerName(inputNumber1);
                String x=callerName+" "+callerName1;
                singleton.setCallerName(x);

                if (!inputNumber.isEmpty() && !inputNumber1.isEmpty()) {
                    numbers.put("number1", inputNumber);
                    numbers.put("number2", inputNumber1);
                    numbers.put("isCallBusy",false);
                    numbers.put("email",singleton.getUserEmail());
                    numbers.put("length", "0");

                    CollectionReference collectionRef = db.collection("numbers");

// Create a query to fetch all documents in the collection

                    Query query = collectionRef.whereEqualTo("isCallBusy", true).limit(1);

                    // Execute the query
                    query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {

                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            loop:for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                                // Handle each document here
                                String documentId = documentSnapshot.getId();
                                Map<String, Object> data = documentSnapshot.getData();
                                // Do something with the data
                                clientBusy=true;
                                Toast.makeText(getContext(), "client is busy: " + documentId, Toast.LENGTH_SHORT).show();
                                break loop;

                                // Show a toast message when length 1 is found


                            }
                            if(clientBusy==true){
                                clientBusy=false;
                                toastTextView.setText("Dialer is busy try again later...");
                            }
                            else if(clientBusy==false)
                            {
                                toastTextView.setText("Call initializing...");
                                addData(numbers);
                            }

                        }
                    });





                    if (toastTextView.getVisibility() == View.GONE) {
                        toastTextView.setVisibility(View.VISIBLE);
                        cancelToastButton.setVisibility(View.VISIBLE);
                     }
                    else {
                        toastTextView.setVisibility(View.GONE);
                        cancelToastButton.setVisibility(View.GONE);
                    }
                }
                call_button.setEnabled(false);








            }
        });

        // Find the EditText field for "Number 1"





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
        db.collection("numbers")
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
        ContentResolver contentResolver = getActivity().getContentResolver();
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
