package com.itoasis.callingapp.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.itoasis.callingapp.R;
import com.itoasis.callingapp.adapter.MessageAdapter;
import com.itoasis.callingapp.modal.MessageModal;

import java.util.ArrayList;


public class Message extends Fragment {

    private RecyclerView recyclerView;
    private EditText searchEditText;

    // variable for our adapter
    // class and array list
    private MessageAdapter adapter;
    private ArrayList<MessageModal> messageModelArrayList;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_message, container, false);
        recyclerView = v.findViewById(R.id.recycler);
        searchEditText=v.findViewById(R.id.search_edit_text);

        buildRecyclerView();
        setupSearch();
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
        // creating a new array list to filter our data.
        ArrayList<MessageModal> filteredlist = new ArrayList<MessageModal>();

        // running a for loop to compare elements.
        for (MessageModal item : messageModelArrayList) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredlist.add(item);
            }
        }


            adapter.filterList(filteredlist);

    }

    private void buildRecyclerView() {

        // below line we are creating a new array list
        messageModelArrayList = new ArrayList<MessageModal>();

        // below line is to add data to our array list.
        messageModelArrayList.add(new MessageModal("Jimmy Heather1", "DLorem Ipsum is simply dummy text of the printing and typesetting industry.","12.07 AM"));
        messageModelArrayList.add(new MessageModal("Jimmy Heather2", "DLorem Ipsum is simply dummy text of the printing and typesetting industry.","12.07 AM"));
        messageModelArrayList.add(new MessageModal("Jimmy Heather3", "DLorem Ipsum is simply dummy text of the printing and typesetting industry.","12.07 AM"));
        messageModelArrayList.add(new MessageModal("Jimmy Heather4", "DLorem Ipsum is simply dummy text of the printing and typesetting industry.","12.07 AM"));
        messageModelArrayList.add(new MessageModal("Jimmy Heather5", "DLorem Ipsum is simply dummy text of the printing and typesetting industry.","12.07 AM"));
        messageModelArrayList.add(new MessageModal("Jimmy Heather6", "DLorem Ipsum is simply dummy text of the printing and typesetting industry.","12.07 AM"));

        // initializing our adapter class.
        adapter = new MessageAdapter(messageModelArrayList, getContext());

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