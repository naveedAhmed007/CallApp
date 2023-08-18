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
import com.itoasis.callingapp.adapter.SetPricesAdapter;
import com.itoasis.callingapp.modal.HistoryModal;
import com.itoasis.callingapp.modal.SetPricesModal;

import java.util.ArrayList;


public class setPrices extends Fragment {

    private RecyclerView recycler;
    private EditText searchEditText;

    // variable for our adapter
    // class and array list
    private SetPricesAdapter adapter;
    private ArrayList<SetPricesModal> SetPricesModalArrayList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_set_prices, container, false);
        recycler = v.findViewById(R.id.recycler);
        searchEditText = v.findViewById(R.id.search_edit_text);
        buildRecyclerView();
        setupSearch();

        return v;
    }
    private void buildRecyclerView() {

        // below line we are creating a new array list
        SetPricesModalArrayList = new ArrayList<SetPricesModal>();

        // below line is to add data to our array list.
        SetPricesModalArrayList.add(new SetPricesModal("DSA1", "$2.00","$2.00"));
        SetPricesModalArrayList.add(new SetPricesModal("DSA2", "$2.00","$2.00"));
        SetPricesModalArrayList.add(new SetPricesModal("DSA3", "$2.00","$2.00"));
        SetPricesModalArrayList.add(new SetPricesModal("DSA4", "$2.00","$2.00"));
        SetPricesModalArrayList.add(new SetPricesModal("DSA5", "$2.00","$2.00"));
        SetPricesModalArrayList.add(new SetPricesModal("DSA6", "$2.00","$2.00"));

        // initializing our adapter class.
        adapter = new SetPricesAdapter(SetPricesModalArrayList, getContext());

        // adding layout manager to our recycler view.
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recycler.setHasFixedSize(true);

        // setting layout manager
        // to our recycler view.
        recycler.setLayoutManager(manager);

        // setting adapter to
        // our recycler view.
        recycler.setAdapter(adapter);
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
        ArrayList<SetPricesModal> filteredList = new ArrayList<>();

        for (SetPricesModal item : SetPricesModalArrayList) {
            if (item.getCountryName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        adapter.filterList(filteredList);
    }
}