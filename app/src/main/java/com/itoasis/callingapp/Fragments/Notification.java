package com.itoasis.callingapp.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.itoasis.callingapp.R;
import com.itoasis.callingapp.adapter.NotificationAdapter;
import com.itoasis.callingapp.modal.NotificationModal;

import java.util.ArrayList;

public class Notification extends Fragment {

    public Notification() {
        // Required empty public constructor
    }
    private RecyclerView recyclerView;

    // variable for our adapter
    // class and array list
    private NotificationAdapter adapter;
    private ArrayList<NotificationModal> notiList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_notification, container, false);
        recyclerView= v.findViewById(R.id.recyclerNotification);
        buildRecyclerView();
        return v;
    }

    private void buildRecyclerView() {

        // below line we are creating a new array list
        notiList = new ArrayList<NotificationModal>();

        // below line is to add data to our array list.
        notiList.add(new NotificationModal("Lorem Ipsum is simply dummy text of the printing and typesetting", "8:59 PM"));
        notiList.add(new NotificationModal("Lorem Ipsum is simply dummy text of the printing and typesetting", "8:59 PM"));
        notiList.add(new NotificationModal("Lorem Ipsum is simply dummy text of the printing and typesetting", "8:59 PM"));
        notiList.add(new NotificationModal("Lorem Ipsum is simply dummy text of the printing and typesetting", "8:59 PM"));
        notiList.add(new NotificationModal("Lorem Ipsum is simply dummy text of the printing and typesetting", "8:59 PM"));

        // initializing our adapter class.
        adapter = new NotificationAdapter(notiList, getContext());

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
