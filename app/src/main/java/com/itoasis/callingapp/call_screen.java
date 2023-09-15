package com.itoasis.callingapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.provider.Settings;
import android.telecom.Call;
import android.telecom.TelecomManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.itoasis.callingapp.R;
import com.itoasis.callingapp.receivers.PingAlarmReceiver;
import com.itoasis.callingapp.utils.AirplaneModeChangeReceiver;
import com.itoasis.callingapp.utils.CallListHelper;
import com.itoasis.callingapp.utils.CallManager;
import com.itoasis.callingapp.utils.ContactsHelper;
import com.itoasis.callingapp.utils.MyDatabaseHelper;
import com.itoasis.callingapp.utils.MyPhoneStateListener;
import com.itoasis.callingapp.utils.NotificationHelper;
import com.itoasis.callingapp.utils.OutgoingCallReceiver;
import com.itoasis.callingapp.utils.Singleton;

import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class call_screen extends AppCompatActivity {
    private static final int CALL_STATE_OFFHOOK_CHECK_DELAY = 30000; // 25 seconds


    private Handler handler = new Handler(Looper.getMainLooper());
    FirebaseFirestore db;
    MyDatabaseHelper dbHelper;
    SQLiteDatabase sqDb;
    TextView counterTV, callerNumber1, callerNumber2, callerNames;
    public static String PHONE_NUMBER, CALLER_NAME;
    Singleton singleTon = Singleton.getInstance();
    public static String speakerBtnName = "Speaker On", muteBtnName = "Mute";

    // Reference to the "users" collection
    CollectionReference usersCollection;
    private CountDownTimer countUpTimer;
    private boolean isCountdownRunning = false;
    private long elapsedTime = 0;

    ImageButton imageButton2;
    private TelephonyManager telephonyManager;
    private MyPhoneStateListener phoneStateListener;


    AlarmManager alarmManager;

    // Create an Intent for your BroadcastReceiver
    Intent intent;

    // Create a PendingIntent to be triggered when the alarm fires
    PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calling_screen);

        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        intent = new Intent(getApplicationContext(), PingAlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        dbHelper = new MyDatabaseHelper(call_screen.this);
        sqDb = dbHelper.getWritableDatabase();


        callerNumber1 = findViewById(R.id.textView4);
        callerNumber2 = findViewById(R.id.textView5);
        callerNames = findViewById(R.id.textView3);
        callerNumber1.setText(singleTon.getPhoneNumber1());
        callerNumber2.setText(singleTon.getPhoneNumber2());
        callerNames.setText(singleTon.getCallerName());


        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();

        }// Replace with the correct layout name

        imageButton2 = findViewById(R.id.imageButton2);
        counterTV = findViewById(R.id.textView2);
        if (singleTon.getActivityCall() == 2) {
            startCountup();

        }


        imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (singleTon.getActivityCall() == 1) {
                    CallManager.hangUpCall(CallListHelper.callList.get(CallManager.NUMBER_OF_CALLS - 1));
                    singleTon.resetActivityCall();
                    singleTon.resetAnswerCall();


                } else {
                    CallManager.hangUpCall(CallListHelper.callList.get(CallManager.NUMBER_OF_CALLS - 2));
                    CallManager.hangUpCall(CallListHelper.callList.get(CallManager.NUMBER_OF_CALLS - 1));
                    singleTon.resetActivityCall();
                    singleTon.resetAnswerCall();


                }
            }
        });

        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onReceive(Context arg0, Intent intent) {


                String action = intent.getAction();


                if (action.equals("call_ended")) {
                    finishAndRemoveTask();
//                    CallManager.hangUpCall(CallListHelper.callList.get(CallManager.NUMBER_OF_CALLS - 1));
                } else if (action.equals("call_answered")) {

                    getLastDocumentId();
                    singleTon.incrementAnsweredCall();


//                    inProgressCallRLView.setVisibility(View.VISIBLE);
//                    incomingRLView.setVisibility(View.GONE);

                    if (CallListHelper.callList.get(CallManager.NUMBER_OF_CALLS - 1).getDetails().hasProperty(Call.Details.PROPERTY_CONFERENCE)) {
//                        PHONE_NUMBER = "Conference";
//                        CALLER_NAME = "Conference";
                    } else {
                        PHONE_NUMBER = CallListHelper.callList.get(CallManager.NUMBER_OF_CALLS - 1).getDetails().getHandle().getSchemeSpecificPart();
                        CALLER_NAME = ContactsHelper.getContactNameByPhoneNumber(PHONE_NUMBER, call_screen.this);
                    }

//                    callerPhoneNumberTV.setText(PHONE_NUMBER);
//                    callerNameTV.setText(CALLER_NAME);

                    Log.d(TAG, PHONE_NUMBER + "  " + CALLER_NAME);
//                    ------------------------------------------------------------------------------


                    if (singleTon.getActivityCall() == 1) {


                        placeCall(singleTon.getPhoneNumber2());
                        Intent intent1 = getIntent();
                        finish();
                        startActivity(intent1);


                    }
                    if (singleTon.getActivityCall() == 2) {
                       
                        if(!singleTon.getType().equals("admin"))
                            getUsersCredit();






                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                setupPhoneStateListener();


                                if (singleTon.getListener() == true) {


// Set the alarm to trigger after 30 seconds, even if the app is in the background


                                    SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
                                    String currentTime = dateFormat.format(Calendar.getInstance().getTime());

                                    ContentValues values = new ContentValues();
                                    values.put("name", currentTime);
                                    values.put("email", singleTon.getClientEmailForTime());


                                    sqDb.insert("MyTable", null, values);


                                    stopCountdown();

                                    singleTon.resetListener();
                                    singleTon.resetActivityCall();
                                    singleTon.resetAnswerCall();
                                    CallListHelper.callList.get(CallManager.NUMBER_OF_CALLS - 2).conference(CallListHelper.callList.get(CallManager.NUMBER_OF_CALLS - 1));
                                    CallListHelper.callList.get(CallManager.NUMBER_OF_CALLS - 1).mergeConference();
                                    muteMicrophone(getApplicationContext());

                                    NotificationHelper.cancelNotification(getApplicationContext(), NotificationHelper.NOTIFICATION_ID);


                                    //add functionality to hold dialer Call


                                    android.os.Process.killProcess(android.os.Process.myPid());
                                    System.exit(1);

                                }
                            }
                        }, CALL_STATE_OFFHOOK_CHECK_DELAY);


                    }


                }
            }

        };

        IntentFilter filter = new IntentFilter();
        filter.addAction("call_ended");
        filter.addAction("call_answered");

        registerReceiver(broadcastReceiver, filter);


    }

    private void getLastDocumentId() {
        db = FirebaseFirestore.getInstance();
        usersCollection = db.collection("numbers");
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
                        Map<String, Object> updates = new HashMap<>();
                        updates.put("length", "1");
                        updates.put("isCallBusy", true);
                        db.collection("numbers").document(documentId)
                                .update(updates)
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


//                        Toast.makeText(getApplicationContext(), documentId, Toast.LENGTH_SHORT).show();


                        // Now you have the last document ID based on the condition
                        // You can use it as needed
                    } else {
//                        Toast.makeText(getApplicationContext(), "No matching document found", Toast.LENGTH_SHORT).show();

                        // No document found
                    }

                }
            }
        });
    }

    private void placeCall(String phoneNumber) {

        @SuppressLint("ServiceCast")
        TelecomManager telecomManager = (TelecomManager) getSystemService(Context.TELECOM_SERVICE);
        Uri uri = Uri.fromParts("tel", phoneNumber, null);
        Bundle extras = new Bundle();
        extras.putBoolean(TelecomManager.EXTRA_START_CALL_WITH_SPEAKERPHONE, false);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            if (telecomManager.getDefaultDialerPackage().equals(getPackageName())) {
                // Directly place the call using TelecomManager API
                telecomManager.placeCall(uri, extras);
            } else {
                // If your app is not the default dialer, you cannot use the TelecomManager API directly.
                // You'll need to request the CALL_PHONE permission and start the call using ACTION_CALL intent.
                Uri phoneNumberUri = Uri.parse("tel:" + phoneNumber);
                Intent callIntent = new Intent(Intent.ACTION_CALL, phoneNumberUri);
                callIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(callIntent);
            }
        } else {
            Toast.makeText(this, "Please allow permission", Toast.LENGTH_SHORT).show();
        }


    }


    @SuppressLint("UseCompatTextViewDrawableApis")
    @Override
    protected void onResume() {
        super.onResume();

        if (CallListHelper.callList.size() >= 2) {

//            mergeCallBtn.setVisibility(View.VISIBLE);
//
//            holdBtn.setEnabled(false);
//            holdBtn.setClickable(false);
//            holdBtn.setCompoundDrawableTintList(ColorStateList.valueOf(getColor(R.color.light_grey)));
//            holdBtn.setTextColor(getColor(R.color.light_grey));
//
//            addCallBtn.setEnabled(false);
//            addCallBtn.setClickable(false);
//            addCallBtn.setCompoundDrawableTintList(ColorStateList.valueOf(getColor(R.color.light_grey)));
//            addCallBtn.setTextColor(getColor(R.color.light_grey));
        } else {

//            holdBtn.setEnabled(true);
//            holdBtn.setClickable(true);
//

            int call_state = CallManager.HP_CALL_STATE;

            if (call_state == Call.STATE_CONNECTING || call_state == Call.STATE_DIALING) {

//            inProgressCallRLView.setVisibility(View.VISIBLE);
//            incomingRLView.setVisibility(View.GONE);

                if (CallManager.NUMBER_OF_CALLS > 0 && CallListHelper.callList.size() > 0) {

                    PHONE_NUMBER = CallListHelper.callList.get(CallManager.NUMBER_OF_CALLS - 1).getDetails().getHandle().getSchemeSpecificPart();
                    CALLER_NAME = ContactsHelper.getContactNameByPhoneNumber(PHONE_NUMBER, this);

//                callerPhoneNumberTV.setText(PHONE_NUMBER);
//                callerNameTV.setText(CALLER_NAME);

                    NotificationHelper.createOutgoingNotification(this, CallListHelper.callList.get(CallManager.NUMBER_OF_CALLS - 1));
                }
            } else if (call_state == Call.STATE_ACTIVE || call_state == Call.STATE_HOLDING) {

                Intent broadCastIntent = new Intent("call_answered");
                sendBroadcast(broadCastIntent);
            } else if (call_state == Call.STATE_RINGING) {

//            inProgressCallRLView.setVisibility(View.GONE);
//            incomingRLView.setVisibility(View.VISIBLE);

                if (CallManager.NUMBER_OF_CALLS > 0 && CallListHelper.callList.size() > 0) {

                    PHONE_NUMBER = CallListHelper.callList.get(CallManager.NUMBER_OF_CALLS - 1).getDetails().getHandle().getSchemeSpecificPart();
                    CALLER_NAME = ContactsHelper.getContactNameByPhoneNumber(PHONE_NUMBER, this);

//                incomingCallerPhoneNumberTV.setText(PHONE_NUMBER);
//                incomingCallerNameTV.setText(CALLER_NAME);

                    NotificationHelper.createIncomingNotification(this, CallListHelper.callList.get(CallManager.NUMBER_OF_CALLS - 1));
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


    }

    private void startCountup() {
        isCountdownRunning = true;
        countUpTimer = new CountDownTimer(Long.MAX_VALUE, 1000) { // Count up every 1 second
            public void onTick(long millisUntilFinished) {
                elapsedTime += 1000;
                counterTV.setText("Countup: " + (elapsedTime / 1000));
            }

            public void onFinish() {
                // This method is not used in count up timer
            }
        }.start();
    }

    private void stopCountdown() {
        if (countUpTimer != null) {
            countUpTimer.cancel();
        }
        counterTV.setText("Countdown: Stopped");
        isCountdownRunning = false;
    }

    private void setupPhoneStateListener() {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        OutgoingCallReceiver outgoingCallReceiver = new OutgoingCallReceiver();
        telephonyManager.listen(outgoingCallReceiver, PhoneStateListener.LISTEN_CALL_STATE);
    }



    public static void muteMicrophone(Context context) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setMode(AudioManager.MODE_IN_CALL);
        audioManager.setMicrophoneMute(true);
        audioManager.setSpeakerphoneOn(true);


    }




    private void getUsersCredit() {
        db = FirebaseFirestore.getInstance();
        CollectionReference usersRef = db.collection("users");

// Create a query to find documents where the "email" field matches the provided email
        Query query1 = usersRef.whereEqualTo("email", singleTon.getClientEmailForTime());

// Execute the query
        query1.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        // Access the data in the document
                        String documentId = document.getId();
                        double remainingCredit= document.getDouble("remainingCredit");
                        long alarmTime = SystemClock.elapsedRealtime() + 30000+(long)remainingCredit;
                        alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, alarmTime, pendingIntent);




                        // Update the document with the new name


                    }
                } else {
                    Log.d("Firestore", "Error getting documents: ", task.getException());
                }
            }
        });


    }

    }











