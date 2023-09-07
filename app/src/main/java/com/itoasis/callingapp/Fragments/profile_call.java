package com.itoasis.callingapp.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.itoasis.callingapp.R;
import com.itoasis.callingapp.utils.Singleton;

public class profile_call extends Fragment {
    androidx.appcompat.widget.AppCompatImageView notification;

    String phoneNUmber,credits;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        notification = v.findViewById(R.id.postfixIcon);
        String userName = Singleton.getInstance().getUserName();
        char Fristchar =Singleton.getInstance().getChar();
        phoneNUmber=Singleton.getInstance().getPhoneNumberr();
        //Toast.makeText(requireContext(), , Toast.LENGTH_SHORT).show();
        // Set the user's name in your TextView
        TextView userNameTextView = v.findViewById(R.id.User_Name);
        TextView firstChar= v.findViewById(R.id.name_profle_alphabet);
        userNameTextView.setText(userName);
        String Alpha = Character.toString(Fristchar);
        firstChar.setText(Alpha);

        return v;
    }}