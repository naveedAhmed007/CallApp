package com.itoasis.callingapp.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.itoasis.callingapp.R;
import com.itoasis.callingapp.utils.MyPhoneStateListener;
import com.itoasis.callingapp.utils.Singleton;

public class FirestoreMonitoringService extends Service {
    private static final String TAG = "FirestoreService";
    private static final String CHANNEL_ID = "FirestoreServiceChannel";
    private FirebaseFirestore db;
    Singleton singleton;
    CollectionReference usersCollection;
    TelephonyManager telephonyManager;
    PhoneStateListener phoneStateListener;


    @Override
    public void onCreate() {
        super.onCreate();

        db = FirebaseFirestore.getInstance();
        singleton=Singleton.getInstance();
        usersCollection=db.collection("numbers");
         telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);




    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createNotificationChannel();
        startForeground(1, createNotification());
        final boolean[] stopLoop = {false};


        phoneStateListener = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                super.onCallStateChanged(state, incomingNumber);

                switch (state) {
                    case TelephonyManager.CALL_STATE_IDLE:
                        Log.d("PhoneState", "Call state: Idle");

                        // Outgoing call has ended, perform actions here.
                        break;
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                        Log.d("PhoneState", "Call state: Offhook");
                        Toast.makeText(getApplicationContext(), "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", Toast.LENGTH_SHORT).show();
                        // Call is active, perform actions here.

                        // Stop Firestore snapshot listener when a call is active
                        stopLoop[0] = true;
                        break;
                    case TelephonyManager.CALL_STATE_RINGING:
                        Log.d("PhoneState", "Call state: Ringing");
                        // Incoming call is ringing, perform actions here.
                        break;
                }
            }
        };





        // Start Firestore snapshot listener
        db.collection("numbers")
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e);
                        return;
                    }

                    for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                        if (stopLoop[0]==true) {
                            stopLoop[0]=false;
                            // If the flag is true, stop the loop
                            break;
                        }
                        switch (dc.getType()) {
                            case ADDED:


                                String lengthValue = dc.getDocument().getString("length");


                                if (dc.getDocument().contains("length") && lengthValue != null && lengthValue.equals("0")) {

                                    telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);

                                    stopLoop[0] = true;

                                }

                                break;
                            case MODIFIED:
                                // Handle modified document
                                // Service is triggered when there's a change
                                Log.d(TAG, "Firestore collection modified.");
                                break;
                            case REMOVED:
                                // Handle removed document
                                break;
                        }
                    }
                });

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Firestore Monitoring Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    private Notification createNotification() {
        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Firestore Monitoring Service")
                .setContentText("Monitoring changes in Firestore collection")
                .setSmallIcon(R.drawable.pen_icon)
                .build();
    }
}
