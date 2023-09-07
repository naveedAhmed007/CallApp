package com.itoasis.callingapp.Fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.itoasis.callingapp.R;
import com.itoasis.callingapp.adapter.SetPricesAdapter;
import com.itoasis.callingapp.modal.SetPricesModal;
import java.util.ArrayList;

public class setPrices extends Fragment {

    private RecyclerView recycler;
    private EditText searchEditText;
    private SetPricesAdapter adapter;
    private ArrayList<SetPricesModal> SetPricesModalArrayList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_set_prices, container, false);
        recycler = v.findViewById(R.id.recycler);
        searchEditText = v.findViewById(R.id.search_edit_text);

        buildRecyclerView();
        setupSearch();
        fetchDataFromFirestore(); // Call this to load data from Firestore

        return v;
    }

    private void buildRecyclerView() {
        SetPricesModalArrayList = new ArrayList<>();
        adapter = new SetPricesAdapter(SetPricesModalArrayList, getContext());
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(manager);
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

    private void fetchDataFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Replace "setPrices" with the actual name of your Firestore collection
        db.collection("setPrices")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            SetPricesModalArrayList.clear(); // Clear the existing data

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String countryName = document.getString("countryName");
                                String hourlyRate = document.getString("Hourly_Rate");
                                String decHourlyRate = document.getString("Decreasing_Hourly_Rate");

                                SetPricesModal item = new SetPricesModal(countryName, hourlyRate, decHourlyRate);
                                SetPricesModalArrayList.add(item);
                            }

                            // Notify the adapter that the data has changed
                            adapter.notifyDataSetChanged();
                        } else {
                            // Handle errors here
                            // You can display a toast message or handle the error in another way
                        }
                    }
                });
    }
}
