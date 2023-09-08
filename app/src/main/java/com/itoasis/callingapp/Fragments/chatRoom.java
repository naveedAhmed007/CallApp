package com.itoasis.callingapp.Fragments;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.itoasis.callingapp.Activities.DashboardActivity;
import com.itoasis.callingapp.R;
import com.itoasis.callingapp.adapter.ChatAdapter;
import com.itoasis.callingapp.modal.ChatMessage;
import com.itoasis.callingapp.utils.Singleton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class chatRoom extends Fragment {

    private EditText editTextMessage;
    private RecyclerView recyclerView; // Declare recyclerView here
    private ChatAdapter adapter;
    private List<ChatMessage> chatMessages;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String clientEmail;
    private String adminEmail;
    private String chatRoomId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        Bundle args = getArguments();
        if (args != null) {
            chatRoomId = args.getString("chatRoomId");
        }else {
            // Get the client's email from Singleton
            clientEmail = Singleton.getInstance().getUserEmail();
            adminEmail = "admin@test.com";

            chatRoomId = clientEmail + "_" + adminEmail;
        }

        // Determine the chat room document ID based on client and admin emails


        // Initialize chat messages list
        chatMessages = new ArrayList<>();

        // Initialize the RecyclerView adapter
        adapter = new ChatAdapter(chatMessages, clientEmail,recyclerView);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_room, container, false);

        // Initialize views from the chat input layout
        editTextMessage = view.findViewById(R.id.message_editText);
        ImageView buttonSend = view.findViewById(R.id.send_message);
        recyclerView = view.findViewById(R.id.recyclerViewChat); // Initialize recyclerView here
        Bundle args = getArguments();
        if (args != null) {
            String userName = args.getString("userName");

            // Find the TextView with the id 'PersonHeadingText' and set the user's name
            TextView personHeadingText =view.findViewById(R.id.PersonHeadingText);
            personHeadingText.setText(userName);
        }

        // Set up the RecyclerView with a LinearLayoutManager and the adapter
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        // Scroll to the last item in the RecyclerView
        scrollToLastItem(); // Add this line to scroll to the last item

        // Add a click listener to the "Send" button
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the message text from the EditText
                String messageText = editTextMessage.getText().toString().trim();

                // Check if the message is not empty
                if (!messageText.isEmpty()) {
                    // Determine the sender dynamically based on the actual sender
                    String sender;
                    Bundle args = getArguments();
                    if (args != null && args.getBoolean("isAdmin", false)) {
                        // Set the sender as admin for testing
                        sender = "admin@test.com";

                    } else {
                        // Set the sender as the client's email for testing
                        sender = clientEmail;

                    }


                    // Notify the adapter that the data set has changed
                    adapter.notifyDataSetChanged();

                    // Call a method to send the actual message to Firestore
                    sendMessageToFirestore(messageText);

                    // Create a new ChatMessage with the actual sender and add it to the adapter
                    ChatMessage newMessage = new ChatMessage(messageText, sender, System.currentTimeMillis());
                    adapter.addNewMessage(newMessage);
                }

            }
        });

        loadAllPreviousChatMessages();



//        // Load chat messages for this chat room
//        loadChatMessages();
        // adapter.scrollToLastItem();
        return view;
    }

    private void sendMessageToFirestore(String messageText) {
        // Create a new message document
        createChatRoom(chatRoomId);

        Map<String, Object> message = new HashMap<>();
        message.put("text", messageText);

        // Check if the sender is admin or client
        Bundle args = getArguments();
        String sender;
        if (args != null && args.getBoolean("isAdmin", false)) {
            // Set the sender as admin
            sender = "admin@test.com";

        } else {
            // Set the sender as the client's email
            sender = clientEmail;
        }

        message.put("sender", sender);
        message.put("timestamp", System.currentTimeMillis());

        // Get a reference to the chat room's messages subcollection
        CollectionReference messagesCollection = db.collection("chatRooms")
                .document(chatRoomId)
                .collection("messages");

        // Add the message to the messages subcollection
        messagesCollection
                .add(message)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        // Message sent successfully
                        editTextMessage.setText(""); // Clear the message input field
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(requireContext(), "Issue founded", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadAllPreviousChatMessages() {
        // Get a reference to the chat room's messages collection
        String chatRoomPath = "chatRooms/" + chatRoomId + "/messages"; // Adjust this path based on your Firestore structure
        CollectionReference messagesCollection = db.collection(chatRoomPath);

        // Add a real-time snapshot listener to the messages collection
        messagesCollection.orderBy("timestamp").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot querySnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    // Handle any errors here
                    Log.e(TAG, "Error listening to chat messages", e);
                    return;
                }

                chatMessages.clear(); // Clear existing messages
                String loggedInUserEmail = Singleton.getInstance().getUserEmail();

                for (QueryDocumentSnapshot document : querySnapshot) {
                    // Extract data from Firestore document
                    String messageText = document.getString("text");
                    String senderEmail = document.getString("sender");
                    long timestamp = document.getLong("timestamp");
                    String sender;

                    // Compare the sender's email with the logged-in user's email from Singleton
                    if (loggedInUserEmail != null && loggedInUserEmail.equals(senderEmail)) {
                        // Message sent by the logged-in user
                        sender = loggedInUserEmail;
                    } else {
                        // Message received from the other party
                        sender = "Other Party"; // You can replace this with the actual sender's identifier
                    }

                    // Create a ChatMessage object with the determined sender
                    ChatMessage message = new ChatMessage(messageText, sender, timestamp);

                    // Add the message to your list
                    chatMessages.add(message);
                }

                // Notify the adapter that the data set has changed
                adapter.notifyDataSetChanged();
            }
        });
    }


    private void scrollToLastItem() {
        if (adapter.getItemCount() > 0) {
            recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
        }
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

}
