package com.itoasis.callingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

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
import com.itoasis.callingapp.Fragments.ClientHome;
import com.itoasis.callingapp.Fragments.History;
import com.itoasis.callingapp.Fragments.Home;
import com.itoasis.callingapp.Fragments.Message;
import com.itoasis.callingapp.Fragments.Payment;
import com.itoasis.callingapp.Fragments.chatRoom;
import com.itoasis.callingapp.Fragments.profile_call;
import com.itoasis.callingapp.utils.Singleton;

public class send_call extends AppCompatActivity {
    FrameLayout frameLayout;
    Fragment selectedFragment;
    Singleton singleton;
    private com.google.android.material.floatingactionbutton.FloatingActionButton fab;
    BottomNavigationView bottomNavigationView;
    private String userEmail; // User's email
    private String userName; // User's name
    private String credits;
    private String phoneNumberr;
    private FirebaseFirestore db;
    private ListenerRegistration userDataListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottom_navigation);

        singleton=Singleton.getInstance();
        fab=findViewById(R.id.fab);
        frameLayout=findViewById(R.id.f1);
        bottomNavigationView=findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setBackground(null);
        bottomNavigationView.getMenu().getItem(2).setEnabled(false);
        getSupportActionBar().hide();

        getSupportFragmentManager().beginTransaction().replace(R.id.f1, new ClientHome()).commit();

        // getSupportFragmentManager().beginTransaction().replace(R.id.f1, new profile_call()).commit();

        // Retrieve the user's email from the Intent extras
        userEmail = getIntent().getStringExtra("user_email");
        Toast.makeText(this, userEmail, Toast.LENGTH_SHORT).show();
        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Fetch user's name using their email
        fetchUserName(userEmail);

       
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.f1, new ClientHome()).commit();
            }
        });

        selectedFragment = new profile_call();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.profile:

                        selectedFragment = new ClientHome();
                        singleton.setCallScreenFrom("client");
                        break;
                    case R.id.placeholder:
                        

                        selectedFragment = new profile_call();

                        break;
                    case R.id.chat:
                        selectedFragment = new chatRoom();
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
            Query query = db.collection("users").whereEqualTo("email", userEmail);

            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        QuerySnapshot snapshot = task.getResult();

                        if (snapshot != null && !snapshot.isEmpty()) {
                            // Assuming "name" is the field in Firestore where the user's name is stored
                            DocumentSnapshot document = snapshot.getDocuments().get(0); // Assuming there's only one result
                            userName = document.getString("name");
//                         updateUIWithUserName(userName);
                            credits=document.getString("credits");
                            phoneNumberr=document.getString("phoneNumber");
                            Toast.makeText(send_call.this, credits, Toast.LENGTH_SHORT).show();
                            // Store the user's name in Singleton
                            Singleton.getInstance().setUserName(userName);
                            Singleton.getInstance().setPhoneNumber(phoneNumberr);
                            Singleton.getInstance().setPhoneNumber(credits);
                            Singleton.getInstance().setChar(userName.charAt(0));

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

    private void updateUIWithUserName(String userName) {
        if (userName != null) {
            // Update your UI elements, e.g., a TextView to display the user's name
            // For example, if you have a TextView with the id "User_Name":
            TextView userNameTextView = findViewById(R.id.User_Name);
            userNameTextView.setText(userName);

            char firstChar = userName.charAt(0);
            TextView nameProfileAlphabet = findViewById(R.id.name_profle_alphabet);
            nameProfileAlphabet.setText(String.valueOf(firstChar));
        } else {
            Toast.makeText(this, "null", Toast.LENGTH_SHORT).show();
        }
    }
}
