package com.itoasis.callingapp.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.itoasis.callingapp.Activities.AdminBottomNavigation;
import com.itoasis.callingapp.Activities.DashboardActivity;
import com.itoasis.callingapp.R;
import com.itoasis.callingapp.adapter.HistoryAdapter;
import com.itoasis.callingapp.modal.HistoryModal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class History extends Fragment {

    private RecyclerView historyRV;
    private EditText searchEditText;

    private HistoryAdapter adapter;
    private ArrayList<HistoryModal> courseModelArrayList;

    // Initialize Firestore
    private FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_history, container, false);
        searchEditText = rootView.findViewById(R.id.search_edit_text);

        historyRV = rootView.findViewById(R.id.history_recyclar);
        AppCompatImageView backButton = rootView.findViewById(R.id.History_back_btn);

        courseModelArrayList = new ArrayList<HistoryModal>();

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Adding data to the array list...
        courseModelArrayList.add(new HistoryModal("John Heather","MOM","12/03","12:45PM"));

        // Initializing our adapter class.
        adapter = new HistoryAdapter(courseModelArrayList, rootView.getContext());

        LinearLayoutManager manager = new LinearLayoutManager(rootView.getContext());
        historyRV.setHasFixedSize(true);
        historyRV.setLayoutManager(manager);
        historyRV.setAdapter(adapter);
        AppCompatImageView  goBackButton = rootView.findViewById(R.id.History_back_btn);
        goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToDesiredFragment();
            }
        });


        setupSearch();
//        uploadDataToFirestore();

        return rootView;
    }

    private void setupSearch() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void filter(String text) {
        ArrayList<HistoryModal> filteredList = new ArrayList<>();

        for (HistoryModal item : courseModelArrayList) {
            if (item.getCallerName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        adapter.filterList(filteredList);
    }

    private void navigateToDesiredFragment() {
        // Create an instance of the fragment you want to navigate to
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        // Check if there are Fragments in the back stack
        if (fragmentManager.getBackStackEntryCount() > 0) {
            // Pop the back stack to navigate to the previous Fragment
            fragmentManager.popBackStack();
        } else {
            // If there are no Fragments in the back stack, perform the default back action (e.g., close the current Fragment or Activity)
            Toast.makeText(requireContext(), "Nothing at back", Toast.LENGTH_SHORT).show();
        }
    }


    // Add this method to upload data to Firestore
//    private void uploadDataToFirestore() {
//        // Create a map to represent the data you want to store.
//        Map<String, Object> callData = new HashMap<>();
//        callData.put("callerName", "John Heather");
//        callData.put("receiverName", "MOM");
//        callData.put("date", "12/03");
//        callData.put("time", "12:45PM");
//
//        // Add the data to Firestore.
//        db.collection("Call History")
//                .add(callData)
//                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//                        // Data added successfully
//                        Toast.makeText(getContext(), "Data added to Firestore", Toast.LENGTH_SHORT).show();
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        // Handle errors
//                        Toast.makeText(getContext(), "Error adding data to Firestore", Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }
}
