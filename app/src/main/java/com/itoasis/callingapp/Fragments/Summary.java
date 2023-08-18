package com.itoasis.callingapp.Fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.PopupMenu;

import com.itoasis.callingapp.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class Summary extends Fragment {
int mDay,mMonth,mYear;






Button monthPicker;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_summary, container, false);
monthPicker=v.findViewById(R.id.button);
monthPicker.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        showMonthPickerMenu();
    }
});
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
}