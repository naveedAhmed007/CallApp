package com.itoasis.callingapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.itoasis.callingapp.R;
import com.itoasis.callingapp.send_call;

public class MainActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private View usernameSeparator;
    private TextView usernameErrorTextView;

    private EditText passwordEditText;
    private View passwordSeparator;
    private TextView passwordErrorTextView;


    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        usernameEditText = findViewById(R.id.usernameEditText);
        usernameSeparator = findViewById(R.id.usernameSeparator);
        usernameErrorTextView = findViewById(R.id.usernameErrorTextView);

        passwordEditText = findViewById(R.id.passwordEditText);
        passwordSeparator = findViewById(R.id.passwordSeparator);
        passwordErrorTextView = findViewById(R.id.passwordErrorTextView);

        // Find the password icon view and set an OnClickListener
        View passwordIcon = findViewById(R.id.password_icon);
        passwordIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatImageView passwordPostfixIcon=findViewById(R.id.password_icon);
                int inputType = passwordEditText.getInputType();
                if (inputType == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                    // Password is visible, switch to password mode
                    passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    passwordPostfixIcon.setImageResource(R.drawable.gary_eye); // Change to closed eye icon
                } else {
                    // Password is hidden, switch to visible mode
                    passwordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    passwordPostfixIcon.setImageResource(R.drawable.gray_corss_eye); // Change to open eye icon
                }

                // Move cursor to the end of the text
                passwordEditText.setSelection(passwordEditText.getText().length());
            }
        });

        usernameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                usernameSeparator.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                usernameErrorTextView.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                passwordSeparator.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                passwordErrorTextView.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAndHandleEmptyUsername();
                checkAndHandleEmptyPassword();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // User is signed in, you can reload or update the UI as needed
            reload();
        }
    }

    private void reload() {
        // Perform any actions needed when the user is signed in or their state changes
        // This could include updating UI elements or loading user-specific data
        // You can customize this method according to your app's requirements
    }

    private void checkAndHandleEmptyUsername() {
        String username = usernameEditText.getText().toString().trim();
        if (username.isEmpty()) {
            usernameSeparator.setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
            usernameErrorTextView.setVisibility(View.VISIBLE);
        } else if (!isValidEmail(username)) {
            usernameSeparator.setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
            usernameErrorTextView.setText("Enter a valid email!");
            usernameErrorTextView.setVisibility(View.VISIBLE);
        } else if (!username.isEmpty() && passwordEditText.getText().toString().isEmpty()) {
            passwordErrorTextView.setText("Enter a password!");
            passwordSeparator.setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
            passwordErrorTextView.setVisibility(View.VISIBLE);
        } else {
            usernameSeparator.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
            usernameErrorTextView.setVisibility(View.GONE);

            // Attempt to sign in with the provided email and password
            mAuth.signInWithEmailAndPassword(username, passwordEditText.getText().toString().trim())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                String userEmail = user.getEmail();

                                if (userEmail != null && userEmail.equals("umarninjauuuu@gmail.com")) {
                                    // If the email matches, open DashboardActivity
                                    Intent intent = new Intent(MainActivity.this, send_call.class);
                                    startActivity(intent);
                                } else {
                                    // If the email does not match, open send_call
                                    Intent intent = new Intent(MainActivity.this, send_call.class);
                                    startActivity(intent);
                                }
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(MainActivity.this, "Sign-in failed. Please check your email and password and try again.", Toast.LENGTH_SHORT).show();
                                passwordSeparator.setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
                                passwordErrorTextView.setText("Incorrect Password");
                                passwordErrorTextView.setVisibility(View.VISIBLE);
                            }
                        }
                    });
        }
    }

    @Override
    public void onBackPressed() {
        this.finishAffinity();
    }

    public static boolean isValidEmail(CharSequence target) {
        return Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private void checkAndHandleEmptyPassword() {
        String password = passwordEditText.getText().toString().trim();
        if (password.isEmpty()) {
            passwordSeparator.setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
            passwordErrorTextView.setVisibility(View.VISIBLE);
        } else {
            passwordSeparator.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
            passwordErrorTextView.setVisibility(View.GONE);
        }
    }
}
