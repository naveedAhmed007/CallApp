package com.itoasis.callingapp.Fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.itoasis.callingapp.R;
import com.itoasis.callingapp.call_screen;

public class Home extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        // ... (Existing code for handling country code dropdowns)

        // Find the ImageView for the postfix icon
        ImageView postfixIcon = rootView.findViewById(R.id.postfixIcon);

        // Set a click listener for the postfix icon
        postfixIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the new activity when the postfix icon is clicked
                openNewActivity();
            }
        });

        return rootView;
    }

    private void showCountryCodeDropdown(View anchorView, final TextView countryCodeTextView) {
        // ... (Existing code for showing country code dropdown)

    }

    // Function to open the new activity
    private void openNewActivity() {
        Intent intent = new Intent(requireContext(), call_screen.class);
        startActivity(intent);
    }
}
