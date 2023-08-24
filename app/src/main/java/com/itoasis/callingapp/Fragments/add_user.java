package com.itoasis.callingapp.Fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hbb20.CountryCodePicker;
import com.itoasis.callingapp.R;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class add_user extends Fragment {
    ImageView passwordPostfixIcon;
    EditText nameEditText, emailEditText, passwordField, creditField;
    View addUser;
    TextView nameError, emailError, passwordError, creditError, countryError;
    CountryCodePicker countryCodePicker;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_user, container, false);

        // Initialize views
        nameEditText = v.findViewById(R.id.nameEditText);
        passwordPostfixIcon = v.findViewById(R.id.PasswordpostfixIcon);
        emailEditText = v.findViewById(R.id.editText);
        addUser = v.findViewById(R.id.add_user_btn);
        nameError = v.findViewById(R.id.nameErrorTextView);
        emailError = v.findViewById(R.id.emailErrorTextView);
        creditError = v.findViewById(R.id.credit_error);
        passwordError = v.findViewById(R.id.passwordErrorTextView);
        passwordField = v.findViewById(R.id.password_field);
        creditField = v.findViewById(R.id.credit_field);
        countryCodePicker = v.findViewById(R.id.countryCodePicker);
        countryError = v.findViewById(R.id.countryError);



        addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = nameEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                String password = passwordField.getText().toString().trim();
                String selectedCountryCode = countryCodePicker.getSelectedCountryCode();
                String credits = creditField.getText().toString().trim();

                // Rest of your validation code here...

                if (userName.isEmpty() || userName.length() < 3) {
                    nameError.setText(userName.isEmpty() ? "Enter a name" : "Name must be greater than 3 characters");
                    nameError.setVisibility(View.VISIBLE);
                } else if (email.isEmpty() || !isValidEmail(email)) {
                    emailError.setText(email.isEmpty() ? "Enter an email" : "Enter a valid email");
                    emailError.setVisibility(View.VISIBLE);
                } else if (password.isEmpty()) {
                    passwordError.setVisibility(View.VISIBLE);
                } else if (password.length() < 8 || !containsUpperCase(password) || !containsLowerCase(password) || !containsSpecialCharacter(password)) {
                    passwordError.setText("Password must have at least 8 characters, an uppercase letter, a lowercase letter, and a special character");
                    passwordError.setVisibility(View.VISIBLE);
                } else if (selectedCountryCode.isEmpty() || selectedCountryCode.equals("0")) {
                    countryError.setVisibility(View.VISIBLE);
                } else if (credits.isEmpty()) {
                    creditError.setVisibility(View.VISIBLE);
                }
                if (!selectedCountryCode.isEmpty() && !selectedCountryCode.equals("0")) {
                    countryError.setVisibility(View.GONE);

                    // If all data is valid, proceed to upload to Firestore
                    if (isValidInput(userName, email, password, credits)) {
                        uploadUserData(userName, email, password, selectedCountryCode, credits);
                    }
                }
            }
        });

        passwordPostfixIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int inputType = passwordField.getInputType();
                if (inputType == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                    // Password is visible, switch to password mode
                    passwordField.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    passwordPostfixIcon.setImageResource(R.drawable.baseline_remove_red_eye_24); // Change to closed eye icon
                } else {
                    // Password is hidden, switch to visible mode
                    passwordField.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    passwordPostfixIcon.setImageResource(R.drawable.crosseye); // Change to open eye icon
                }

                // Move cursor to the end of the text
                passwordField.setSelection(passwordField.getText().length());
            }
        });

        applyValidation(nameEditText, nameError);
        applyValidation(emailEditText, emailError);
        applyValidation(passwordField, passwordError);
        applyValidation(creditField, creditError);

        return v;
    }

    private boolean isValidInput(String userName, String email, String password, String credits) {
        return !userName.isEmpty() && userName.length() >= 3
                && !email.isEmpty() && isValidEmail(email)
                && !password.isEmpty() && password.length() >= 8
                && containsUpperCase(password) && containsLowerCase(password) && containsSpecialCharacter(password)
                && !credits.isEmpty();
    }

    private void uploadUserData(String userName, String email, String password, String selectedCountryCode, String credits) {
        // Create a Firestore instance
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Create a HashMap to store user data
        Map<String, Object> user = new HashMap<>();
        user.put("name", userName);
        user.put("email", email);
        user.put("password", password);
        user.put("countryCode", selectedCountryCode);
        user.put("credits", credits);

        // Upload the user data to Firestore
        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        // Data uploaded successfully
                        Toast.makeText(getContext(), "User added successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle failure
                        Toast.makeText(getContext(), "Error adding user: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    public void applyValidation(EditText editText, TextView error) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                error.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
    public static boolean isValidEmail(CharSequence target) {
        return (Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    private boolean containsSpecialCharacter(String password) {
        String specialCharacters = "!@#$%^&*()-_+=<>?.,';/:|\"\\";
        for (char c : password.toCharArray()) {
            if (specialCharacters.contains(String.valueOf(c))) {
                return true;
            }
        }
        return false;
    }

    private boolean containsUpperCase(String password) {
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                return true;
            }
        }
        return false;
    }

    private boolean containsLowerCase(String password) {
        for (char c : password.toCharArray()) {
            if (Character.isLowerCase(c)) {
                return true;
            }
        }
        return false;
    }


    // Rest of your code...
}
