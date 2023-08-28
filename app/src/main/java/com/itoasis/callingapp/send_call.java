package com.itoasis.callingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.itoasis.callingapp.Fragments.History;
import com.itoasis.callingapp.Fragments.Home;
import com.itoasis.callingapp.Fragments.Message;
import com.itoasis.callingapp.Fragments.Notification;
import com.itoasis.callingapp.Fragments.Payment;
import com.itoasis.callingapp.Fragments.PhoneCall;
import com.itoasis.callingapp.Fragments.Search;
import com.itoasis.callingapp.Fragments.profile_call;
import com.itoasis.callingapp.Fragments.send_Notification;

public class send_call extends AppCompatActivity {
    FrameLayout frameLayout;
    Fragment selectedFragment;
    private com.google.android.material.floatingactionbutton.FloatingActionButton fab;
    BottomNavigationView bottomNavigationView;
    private String userEmail; // User's email
    private String userName; // User's name
    private FirebaseFirestore db;
    private ListenerRegistration userDataListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottom_navigation);

        fab = findViewById(R.id.fab);
        frameLayout = findViewById(R.id.f1);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setBackground(null);
        bottomNavigationView.getMenu().getItem(2).setEnabled(false);
        getSupportActionBar().hide();

        // Retrieve the user's email from the Intent extras
        userEmail = getIntent().getStringExtra("user_email");
        Toast.makeText(this, userEmail, Toast.LENGTH_SHORT).show();

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Fetch user's name using their email
        fetchUserName(userEmail);

        getSupportFragmentManager().beginTransaction().replace(R.id.f1, new profile_call()).commit();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.f1, new Home()).commit();
            }
        });

        selectedFragment = new profile_call();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.profile:
                        selectedFragment = new profile_call();
                        break;
                    case R.id.chat:
                        selectedFragment = new Message();
                        break;
                    case R.id.history:
                        selectedFragment = new History();
                        break;
                    case R.id.Payment:
                        selectedFragment = new Payment();
                        break;
                    default:
                        return false;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.f1, selectedFragment).commit();
                return true;
            }
        });
    }

    // Fetch user's name from Firestore using their email
    private void fetchUserName(String userEmail) {
        if (userEmail != null) {
            db.collection("users")
                    .whereEqualTo("email", userEmail)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                QuerySnapshot querySnapshot = task.getResult();
                                if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                    DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                                    String userName = document.getString("name");

                                    // Update your UI with the retrieved user's name
                                    updateUIWithUserName(userName);
                                } else {
                                    // Handle the case where no matching document is found
                                    Toast.makeText(send_call.this, "No user data found", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                // Handle errors in Firestore query
                                Toast.makeText(send_call.this, "Error fetching user data", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    // Update your UI with the user's name
    private void updateUIWithUserName (String userName){
        if (userName != null) {
            // Update your UI elements, e.g., a TextView to display the user's name
            // For example, if you have a TextView with the id "User_Name":
            TextView userNameTextView = findViewById(R.id.User_Name);
            userNameTextView.setText(userName);
        }
    }
}