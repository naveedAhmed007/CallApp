package com.itoasis.callingapp.Fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.itoasis.callingapp.R;
import com.itoasis.callingapp.adapter.SummaryAdapter;
import com.itoasis.callingapp.modal.SummaryModal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class Summary extends Fragment {
int mDay,mMonth,mYear;
    private RecyclerView recyclerView;

    // variable for our adapter
    // class and array list
    private SummaryAdapter adapter;
    private ArrayList<SummaryModal> summaryModelArrayList;







TextView monthPicker,dayPicker;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_summary, container, false);
monthPicker=v.findViewById(R.id.Month);

dayPicker=v.findViewById(R.id.Day);
recyclerView=v.findViewById(R.id.recycler);
monthPicker.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        showMonthPickerMenu();

    }
});
dayPicker.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        showDatePickerDialog();
    }
});
buildRecyclerView();
        return v;
    }

    private void showMonthPickerMenu() {
        PopupMenu popupMenu = new PopupMenu(requireContext(), monthPicker);
        popupMenu.getMenuInflater().inflate(R.menu.month_picker_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                String selectedMonth = item.getTitle().toString();

                // Do something with the selected month
                return true;
            }
        });

        popupMenu.show();
    }

    private void showDatePickerDialog() {
        final Calendar c = Calendar.getInstance();

        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear,
                                          int selectedMonth, int selectedDay) {
                        String formattedDate = selectedDay + "-" + (selectedMonth + 1) + "-" + selectedYear;
//                        selectedDateTV.setText(formattedDate);
                    }
                },
                year, month, day);

        datePickerDialog.show();
    }
    private void buildRecyclerView() {

        // below line we are creating a new array list
        summaryModelArrayList = new ArrayList<SummaryModal>();

        // below line is to add data to our array list.
        summaryModelArrayList.add(new SummaryModal("USA", "3400 min",""));
        summaryModelArrayList.add(new SummaryModal("USA1", "3400 min",""));
        summaryModelArrayList.add(new SummaryModal("USA2", "3400 min",""));
        summaryModelArrayList.add(new SummaryModal("USA3", "3400 min",""));
        summaryModelArrayList.add(new SummaryModal("USA4", "3400 min",""));
        summaryModelArrayList.add(new SummaryModal("USA5", "3400 min",""));
        summaryModelArrayList.add(new SummaryModal("USA6", "3400 min",""));
        // initializing our adapter class.
        adapter = new SummaryAdapter(summaryModelArrayList, getContext());

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