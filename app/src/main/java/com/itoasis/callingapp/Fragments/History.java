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
import android.widget.Toast;

import com.itoasis.callingapp.R;
import com.itoasis.callingapp.adapter.HistoryAdapter;
import com.itoasis.callingapp.modal.HistoryModal;

import java.util.ArrayList;

public class History extends Fragment {

    private RecyclerView historyRV;
    private EditText searchEditText;

    private HistoryAdapter adapter;
    private ArrayList<HistoryModal> courseModelArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_history, container, false);
        searchEditText = rootView.findViewById(R.id.search_edit_text);

        historyRV = rootView.findViewById(R.id.history_recyclar);

        courseModelArrayList = new ArrayList<HistoryModal>();

        // Adding data to the array list...


// below line is to add data to our array list.
        courseModelArrayList.add(new HistoryModal("John Heather","MOM","12/03","12:45PM"));

// initializing our adapter class.

        adapter = new HistoryAdapter(courseModelArrayList, rootView.getContext());

        LinearLayoutManager manager = new LinearLayoutManager(rootView.getContext());
        historyRV.setHasFixedSize(true);
        historyRV.setLayoutManager(manager);
        historyRV.setAdapter(adapter);

        setupSearch();

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
}
