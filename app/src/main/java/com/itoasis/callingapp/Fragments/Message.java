package com.itoasis.callingapp.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.itoasis.callingapp.R;
import com.itoasis.callingapp.adapter.MessageAdapter;
import com.itoasis.callingapp.modal.MessageModal;
import com.itoasis.callingapp.utils.Singleton;

import java.util.ArrayList;
import java.util.HashMap;

public class Message extends Fragment implements MessageAdapter.ItemClickListener {
    private String chatRoomName;
    private RecyclerView recyclerView;
    private EditText searchEditText;
    private MessageAdapter adapter;

    String particularUserName;
    private ArrayList<MessageModal> messageModelArrayList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_message, container, false);
        recyclerView = v.findViewById(R.id.recycler);
        searchEditText = v.findViewById(R.id.search_edit_text);
        messageModelArrayList = new ArrayList<>();
        adapter = new MessageAdapter(messageModelArrayList, getContext(), this); // Pass 'this' as the ItemClickListener
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        setupSearch();
        fetchChatRoomNames(); // Load chat room names from Firestore
        return v;
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
        ArrayList<MessageModal> filteredList = new ArrayList<>();
        for (MessageModal item : messageModelArrayList) {
            if (item.getRoomName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        adapter.filterList(filteredList);
    }

    private void fetchChatRoomNames() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference chatRoomsRef = db.collection("chatRooms");

        chatRoomsRef.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    messageModelArrayList.clear(); // Clear the list before adding chat rooms
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String chatRoomName = document.getId();

                        int indexOfUnderscore = chatRoomName.indexOf('_');

                        if (indexOfUnderscore != -1) {
                            // Extract the substring before '_'
                            String clientEmail = chatRoomName.substring(0, indexOfUnderscore);

                            // Now, query the "users" collection to get the user's name based on the email
                            queryUserForName(clientEmail);
                        } else {
                            // Handle the case where '_' is not found in the string
                            System.out.println("Underscore '_' not found in the string.");
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle errors here
                    Log.e("Firestore", "Failed to fetch chat rooms: " + e.getMessage());
                });
    }

    private void queryUserForName(String userEmail) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference userDetailsCollection = db.collection("users");

        userDetailsCollection.whereEqualTo("email", userEmail)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                         particularUserName = document.getString("name");

                        // Create a MessageModal for the chat room with the user's name
                        messageModelArrayList.add(new MessageModal(particularUserName));
                        adapter.notifyDataSetChanged(); // Notify the adapter of the data change
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle errors here
                    Log.e("Firestore", "Failed to fetch user data: " + e.getMessage());
                });
    }

    // Implement the onItemClick method of the ItemClickListener
    @Override
    public void onItemClick(int position) {

        openChatRoomFragment(messageModelArrayList.get(position).getRoomName(),particularUserName);
    }
    private void openChatRoomFragment(String chatRoomId,String userName) {
        // Create an instance of the chatRoom fragment
        chatRoom chatRoomFragment = new chatRoom();
        // Pass an argument indicating that the sender is the admin
        Bundle args = new Bundle();
        // Pass an argument indicating that the sender is the admin
        args.putBoolean("isAdmin", true);
        args.putString("userName", userName);
        // Pass the chat room ID to the chatRoom fragment
        args.putString("chatRoomId", chatRoomId);

        // Set the arguments Bundle on the chatRoomFragment
        chatRoomFragment.setArguments(args);

        // Replace the current fragment with the chatRoom fragment
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.flFragment, chatRoomFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
