package com.itoasis.callingapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.itoasis.callingapp.R;
import com.itoasis.callingapp.Utils.FCMUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;

public class call_screen extends AppCompatActivity {
    ImageButton imbtn2;
    private static final String SERVER_KEY = "AAAAkJ3ZxWA:APA91bHvEvzbyP3ORzr4zcmIvDBwt_X0So3mzuMw5IyMjS2Ite_AiHQHB1gOLRghrV55CA8u4hwnZRxOF_fqFXJnzO8mlrs3AenAjnIXgKzG29pAorTlNpmi3azs_a4Exom0WOcwHucl"; // Replace with your FCM server key
private  static  final String receiverToken="dxnGus2JhxM:APA91bE8mNQFrbgwnxq_qKtv7IliIkk18VqS7x2EhoM0TfUite7ZbDjzOigBqcIfMQlvY7HvEWNk9XOCVPzIXJNtbqBBZmmlSrVbY4S0vU-el1vHnMRpABKdkuWHDgbFE0mm7krjEYuF";
    String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calling_screen);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();

        }// Replace with the correct layout name

FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
    @Override
    public void onComplete(@NonNull Task<String> task) {
   if(!task.isSuccessful()){
       return;
   }
   token =task.getResult();
   Log.d("TAG-================================", task.getResult());
    }
});
        imbtn2=findViewById(R.id.imageButton2);
        imbtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendFCMPush();
                Toast.makeText(call_screen.this, token, Toast.LENGTH_SHORT).show();






            }
        });
    }

    private void sendFCMPush() {
        // Create the JSON payload for the notification
        JSONObject notification = new JSONObject();
        try {
            notification.put("title", "Notification Title");
            notification.put("body", "Notification Body");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Create the main JSON payload for the FCM message
        JSONObject mainObject = new JSONObject();
        try {
            mainObject.put("to", receiverToken);
            mainObject.put("notification", notification);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Create a JsonObjectRequest to send the FCM message
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                "https://fcm.googleapis.com/fcm/send",
                mainObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "FCM Message Sent");
                        // Handle successful response if needed
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "FCM Message Sending Failed: " + error.toString());
                        // Handle error response if needed
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "key=" + SERVER_KEY);
                return headers;
            }
        };

        // Set the request retry policy
        int socketTimeout = 30000; // 30 seconds
        RetryPolicy retryPolicy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(retryPolicy);

        // Add the request to the request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }





}
