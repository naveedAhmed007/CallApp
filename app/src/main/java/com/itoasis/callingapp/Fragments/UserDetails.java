package com.itoasis.callingapp.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.itoasis.callingapp.R;
import com.itoasis.callingapp.adapter.UserDetailsAdapter;
import com.itoasis.callingapp.modal.UserDetailsModal;

import java.util.ArrayList;


public class UserDetails extends Fragment  {

    private RecyclerView recyclerView;

    // variable for our adapter
    // class and array list
    private UserDetailsAdapter adapter;
    private ArrayList<UserDetailsModal> UserDetailsModalArrayList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

                View v=inflater.inflate(R.layout.fragment_user_details, container, false);
        recyclerView = v.findViewById(R.id.recycleruserDetails);
buildRecyclerView();
        return v;
    }

    private void buildRecyclerView() {

        // below line we are creating a new array list
        UserDetailsModalArrayList = new ArrayList<UserDetailsModal>();

        // below line is to add data to our array list.
        UserDetailsModalArrayList.add(new UserDetailsModal("DSA", "DSA Self Paced Course"));
        UserDetailsModalArrayList.add(new UserDetailsModal("JAVA", "JAVA Self Paced Course"));
        UserDetailsModalArrayList.add(new UserDetailsModal("C++", "C++ Self Paced Course"));
        UserDetailsModalArrayList.add(new UserDetailsModal("Python", "Python Self Paced Course"));
        UserDetailsModalArrayList.add(new UserDetailsModal("Fork CPP", "Fork CPP Self Paced Course"));

        // initializing our adapter class.
        adapter = new UserDetailsAdapter(UserDetailsModalArrayList,
                getContext());

        // adding layout manager to our recycler view.
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setHasFixedSize(true);

        // setting layout manager
        // to our recycler view.
        recyclerView.setLayoutManager(manager);

        // setting adapter to
        // our recycler view.
        recyclerView.setAdapter(adapter);
    }
}