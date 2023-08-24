package com.itoasis.callingapp.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.itoasis.callingapp.R;
import com.itoasis.callingapp.adapter.UserDetailsAdapter;
import com.itoasis.callingapp.modal.UserDetailsModal;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class UserDetails extends Fragment {

    private RecyclerView recyclerView;
    private UserDetailsAdapter adapter;
    private ArrayList<UserDetailsModal> UserDetailsModalArrayList;
    private com.google.android.material.button.MaterialButton addUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_user_details, container, false);
        recyclerView = v.findViewById(R.id.recycleruserDetails);
        addUser = v.findViewById(R.id.addUser);

        addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new add_user();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.flFragment, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        buildRecyclerView();
        return v;
    }

    private void buildRecyclerView() {
        UserDetailsModalArrayList = new ArrayList<>();

        // Initialize Firebase
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersRef = db.collection("users"); // Replace "users" with your Firestore collection name

        usersRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    // Iterate through the documents and retrieve user details
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String userName = document.getString("name");
                        String userEmail = document.getString("email");
                        String credit = document.getString("credits");

                        // Create a UserDetailsModal object and add it to UserDetailsModalArrayList
                        UserDetailsModal userDetails = new UserDetailsModal(userName, userEmail,credit);
                        UserDetailsModalArrayList.add(userDetails);
                    }

                    // Initialize and set up the RecyclerView
                    adapter = new UserDetailsAdapter(UserDetailsModalArrayList, getContext());
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    recyclerView.setAdapter(adapter);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Handle the failure to retrieve data
            }
        });
    }
}
