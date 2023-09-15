package com.itoasis.callingapp.Fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.hbb20.CountryCodePicker;
import com.itoasis.callingapp.R;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class add_user extends Fragment {

    private boolean isEditMode = false;
    ImageView passwordPostfixIcon;
    EditText nameEditText, emailEditText, passwordField, creditField,numberEditText;
    View addUser;
    TextView nameError, emailError, passwordError, creditError, countryError;
    CountryCodePicker countryCodePicker;
    String RemainingCredits="";
    private String userId;

    private FirebaseAuth mAuth; // Firebase Authentication
    private FirebaseFirestore db; // Firestore

    private CollectionReference userDetailsCollection; // Define your collection reference

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_user, container, false);

        mAuth = FirebaseAuth.getInstance(); // Initialize Firebase Authentication
        db = FirebaseFirestore.getInstance(); // Initialize Firestore

        userDetailsCollection = db.collection("users"); // Initialize the collection reference

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
        numberEditText =v.findViewById(R.id.numberEditText);
        Bundle args = getArguments();
        if (args != null) {
            userId = args.getString("userId");
            isEditMode = args.getBoolean("isEditMode", false);
            // If userId is not null, load user data for editing
            if (userId != null) {
                loadUserDataForEditing(userId);
            }
        }

        // Find the TextView with the id 'add_user'
        TextView addUserTextView = v.findViewById(R.id.add_user);

        // Check if it's in edit mode or add mode and set the text accordingly
        if (isEditMode) {
            // If in edit mode, change the text
            addUserTextView.setText("Edit User");
        } else {
            // If in add mode, keep the default text
            addUserTextView.setText("Add User");
        }

        addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = nameEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                String password = passwordField.getText().toString().trim();
                String selectedCountryCode = countryCodePicker.getSelectedCountryNameCode();
                String credits = creditField.getText().toString().trim();
                String phoneNumber = numberEditText.getText().toString().trim();

                if (userName.isEmpty() || userName.length() < 3) {
                    nameError.setText(userName.isEmpty() ? "Enter a name" : "Name must be greater than 3 characters");
                    nameError.setVisibility(View.VISIBLE);
                } else if (email.isEmpty() || !isValidEmail(email)) {
                    emailError.setText(email.isEmpty() ? "Enter an email" : "Enter a valid email");
                    emailError.setVisibility(View.VISIBLE);
//                } else if (password.isEmpty()) {
//                    passwordError.setVisibility(View.VISIBLE);
//                } else if (password.length() < 8 || !containsUpperCase(password) || !containsLowerCase(password) || !containsSpecialCharacter(password)) {
//                    passwordError.setText("Password must have at least 8 characters, an uppercase letter, a lowercase letter, and a special character");
//                    passwordError.setVisibility(View.VISIBLE);
                } else if (selectedCountryCode.isEmpty() || selectedCountryCode.equals("0")) {
                    countryError.setVisibility(View.VISIBLE);
                } else if (credits.isEmpty()) {
                    creditError.setVisibility(View.VISIBLE);
                }
                if (!selectedCountryCode.isEmpty() && !selectedCountryCode.equals("0")) {
                    countryError.setVisibility(View.GONE);

                    // If all data is valid, proceed
                    if (isValidInput(userName, email, password, credits)) {
                        if (userId != null) {
                            // Edit mode: Update existing user data
                            editUserData(userId, userName, email, password, selectedCountryCode, credits,phoneNumber);
                        } else {
                            // Create mode: Add a new user
                            uploadUserData(userName, email, password, selectedCountryCode, credits,phoneNumber, credits);
                        }
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
                && !password.isEmpty() && password.length() < 10
               // && containsUpperCase(password) && containsLowerCase(password) && containsSpecialCharacter(password)
                && !credits.isEmpty();
    }

    private void uploadUserData(String userName, String email, String password, String selectedCountryCode, String credits,String phoneNumber, String remainingCredits) {
        // Create a Firestore instance
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Create a Firebase Authentication user with the provided email and password
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // User creation was successful
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            String uid = firebaseUser.getUid(); // Get the unique user ID

                            // Create a HashMap to store user data for Firestore
                            Map<String, Object> user = new HashMap<>();
                            user.put("name", userName);
                            user.put("email", email);
                            user.put("password", password);
                            user.put("countryCode", selectedCountryCode);
                            user.put("credits", credits);
                            user.put("phoneNumber", phoneNumber);

                            user.put("remainingCredit",Integer.parseInt(credits)*60000);
                            // Upload the user data to Firestore
                            // Create a chat room for the new user
                            String chatRoomId = email + "_admin@test.com"; // Adjust the chat room ID format as needed
                            createChatRoom(chatRoomId);

                            db.collection("users")
                                    .document(uid) // Use the UID as the document ID in Firestore
                                    .set(user)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // Data uploaded successfully to Firestore
                                            Fragment fragment = new UserDetails();
                                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                            fragmentTransaction.replace(R.id.flFragment, fragment);
                                            fragmentTransaction.addToBackStack(null);
                                            fragmentTransaction.commit();
                                            Toast.makeText(getContext(), "User data added successfully", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Handle Firestore data upload failure
                                            Toast.makeText(getContext(), "Error adding user data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            // User creation failed, handle the error here
                            Toast.makeText(getContext(), "Error creating user: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void createChatRoom(String chatRoomName) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference chatRoomsRef = db.collection("chatRooms");

        // Create a new document with the chat room name
        chatRoomsRef.document(chatRoomName).set(new HashMap<>())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Chat room created successfully
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle errors here
                        Log.e("Firestore", "Failed to create chat room: " + e.getMessage());
                        Toast.makeText(requireContext(), "Failed to create chat room", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadUserDataForEditing(String userEmail) {
        // Query Firestore to find the user with the provided email
        db.collection("users")
                .whereEqualTo("email", userEmail)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            // User data found in Firestore
                            DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                            String userName = documentSnapshot.getString("name");
                            String userEmail = documentSnapshot.getString("email");
                            String userCredits = documentSnapshot.getString("credits");
                            String userCountryCode = documentSnapshot.getString("countryCode");
                            String userPassword = documentSnapshot.getString("password");
                            String userPhoneNumber=documentSnapshot.getString("phoneNumber");

                            Toast.makeText(requireContext(), userCountryCode, Toast.LENGTH_SHORT).show();

                            // Populate EditText fields with user data
                            nameEditText.setText(userName);
                            emailEditText.setText(userEmail);
                            creditField.setText(userCredits);
                            passwordField.setText(userPassword);
                            countryCodePicker.setCountryForNameCode(userCountryCode);
                            numberEditText.setText(userPhoneNumber);

                        } else {
                            // User data not found in Firestore, handle the case
                            Toast.makeText(getContext(), "User not found", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle any errors while retrieving user data
                        Toast.makeText(getContext(), "Error fetching user data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    // Method to edit user data
    private void editUserData(String userEmail, String userName, String email, String password, String selectedCountryCode, String credits,String phoneNumber) {
        // Query Firestore to find the user with the provided email
        Query query = userDetailsCollection.whereEqualTo("email", userEmail);

        query.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            // User data found in Firestore
                            DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                            String userId = documentSnapshot.getId(); // Get the user's unique ID
                            // Now you have the user's ID, you can update their data
                            DocumentReference userRef = db.collection("users").document(userId);

                            // Create a HashMap to store updated user data
                            Map<String, Object> updatedData = new HashMap<>();
                            updatedData.put("name", userName);
                            updatedData.put("email", email);
                            updatedData.put("countryCode", selectedCountryCode);
                            updatedData.put("credits", credits);
                            updatedData.put("password", password);
                            updatedData.put("phoneNumber",phoneNumber);

                            // Update the document in Firestore
                            userRef.update(updatedData)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // User data updated successfully
                                            Fragment fragment = new UserDetails();
                                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                            fragmentTransaction.replace(R.id.flFragment, fragment);
                                            fragmentTransaction.addToBackStack(null);
                                            fragmentTransaction.commit();
                                            Toast.makeText(getContext(), "User data updated successfully", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Handle Firestore data update failure
                                            Toast.makeText(getContext(), "Error updating user data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            // User data not found in Firestore, handle the case
                            Toast.makeText(getContext(), "User not found", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle any errors while retrieving user data
                        Toast.makeText(getContext(), "Error fetching user data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
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

}
