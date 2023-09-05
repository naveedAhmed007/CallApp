package com.itoasis.callingapp.utils;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class MyPhoneStateListener  extends PhoneStateListener {
    Context context;
    Singleton singleton;


    public MyPhoneStateListener(Context context){
        this.context=context;
        singleton=Singleton.getInstance();

    }
    @Override
    public void onCallStateChanged(int state, String incomingNumber) {
        super.onCallStateChanged(state, incomingNumber);

        switch (state) {
            case TelephonyManager.CALL_STATE_IDLE:
                Log.d("PhoneState", "Call state: Idle");
                // Outgoing call has ended, perform actions here.
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                Toast.makeText(context, "call is aCTIVAYED", Toast.LENGTH_LONG).show();
                getLastDocumentId();










                break;
            case TelephonyManager.CALL_STATE_RINGING:
                Log.d("PhoneState-----", "Call state: Ringing");
                // Incoming call is ringing.
                break;
        }
    }


    private void getLastDocumentId() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        CollectionReference usersCollection=db.collection("numbers");
        // Build a query to search for the email
        Query query = usersCollection.whereEqualTo("length", "0")
                .orderBy("length", Query.Direction.ASCENDING) // Replace "someField" with the appropriate field to order by
                .limit(1);

        // Execute the query
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    // Get the first and only document in the result
                    DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);

                    // Get the document ID

                    String documentId = documentSnapshot.getId();
                    if (documentId != "") {
                        db.collection("numbers").document(documentId)
                                .update("length", "1")
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        // Update successful
                                        Log.d(TAG, "Field updated successfully in the last document");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Handle errors
                                        Log.e(TAG, "Error updating field in the last document", e);
                                    }
                                });


//                        Toast.makeText(context, documentId, Toast.LENGTH_SHORT).show();


                        // Now you have the last document ID based on the condition
                        // You can use it as needed
                    } else {
//                        Toast.makeText(context, "No matching document found", Toast.LENGTH_SHORT).show();

                        // No document found
                    }

                }
            }
        });
    }
}
