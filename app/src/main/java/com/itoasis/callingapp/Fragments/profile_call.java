package com.itoasis.callingapp.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.itoasis.callingapp.R;
import com.itoasis.callingapp.utils.Singleton;

import org.w3c.dom.Text;

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
        credits= Singleton.getInstance().getCredits();
        phoneNUmber=Singleton.getInstance().getPhoneNumberr();

        // Set the user's name in your TextView
        TextView userNameTextView = v.findViewById(R.id.User_Name);
        TextView firstChar= v.findViewById(R.id.name_profle_alphabet);
        TextView userCredits=v.findViewById(R.id.credits);
        userCredits.setText(credits+" / 240");
        TextView userNumber=v.findViewById(R.id.phoneNumber);
        userNumber.setText(phoneNUmber);
        userNameTextView.setText(userName);
        String Alpha = Character.toString(Fristchar);
        firstChar.setText(Alpha);

        return v;
    }}