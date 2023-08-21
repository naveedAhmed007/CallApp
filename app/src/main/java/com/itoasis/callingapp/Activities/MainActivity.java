package com.itoasis.callingapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.itoasis.callingapp.R;
import com.itoasis.callingapp.send_call;

public class MainActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private View usernameSeparator;
    private TextView usernameErrorTextView;

    private EditText passwordEditText; // Declare password EditText
    private View passwordSeparator; // Declare password separator
    private TextView passwordErrorTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide the action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        setContentView(R.layout.activity_main);

        // Initialize views
        usernameEditText = findViewById(R.id.usernameEditText);
        usernameSeparator = findViewById(R.id.usernameSeparator);
        usernameErrorTextView = findViewById(R.id.usernameErrorTextView);

        // Initialize password views
        passwordEditText = findViewById(R.id.passwordEditText);
        passwordSeparator = findViewById(R.id.passwordSeparator);
        passwordErrorTextView = findViewById(R.id.passwordErrorTextView);

        // Add text change listeners to reset error states when typing starts
        usernameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed for this case
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Reset the error state and appearance when typing starts
                usernameSeparator.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                usernameErrorTextView.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not needed for this case
            }
        });

        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed for this case
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Reset the error state and appearance when typing starts
                passwordSeparator.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                passwordErrorTextView.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not needed for this case
            }
        });

        Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAndHandleEmptyUsername();
                checkAndHandleEmptyPassword(); // Check password field
            }
        });
    }

    private void checkAndHandleEmptyUsername() {
        String username = usernameEditText.getText().toString().trim();
        if (username.isEmpty()) {
            // Show error message and change color
            usernameSeparator.setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
            usernameErrorTextView.setVisibility(View.VISIBLE); // Show error message
        } else {
            // Clear error state
            usernameSeparator.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
            usernameErrorTextView.setVisibility(View.GONE); // Hide error message
            // Proceed with other actions or validations
            Intent intent = new Intent(MainActivity.this, AdminBottomNavigation.class);
            startActivity(intent);
        }
    }

    private void checkAndHandleEmptyPassword() {
        String password = passwordEditText.getText().toString().trim();
        if (password.isEmpty()) {
            // Show error message and change color
            passwordSeparator.setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
            passwordErrorTextView.setVisibility(View.VISIBLE); // Show error message
        } else {
            // Clear error state
            passwordSeparator.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
            passwordErrorTextView.setVisibility(View.GONE); // Hide error message
            // Proceed with other actions or validations
            Intent intent = new Intent(MainActivity.this, send_call.class);
            startActivity(intent);
        }
    }
}
