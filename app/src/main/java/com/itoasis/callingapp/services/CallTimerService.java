package com.itoasis.callingapp.services;

import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.telecom.TelecomManager;
import android.util.Log;
import android.widget.Toast;

import com.itoasis.callingapp.receivers.PingAlarmReceiver;


public class CallTimerService extends Service {


    private static final long CALL_DURATION = 600000/10; // 10 minutes in milliseconds
    private CountDownTimer callTimer;
    private TelecomManager telecomManager;

    @Override
    public void onCreate() {
        super.onCreate();
        telecomManager = getSystemService(TelecomManager.class);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Start a countdown timer for 10 minutes
        callTimer = new CountDownTimer(CALL_DURATION, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Update UI or perform actions for each second of the countdown
            }

            @Override
            public void onFinish() {
                // End the call after 10 minutes
                endCall();
            }
        }.start();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (callTimer != null) {
            callTimer.cancel();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void endCall() {
        // Retrieve the current active call and end it

        Toast.makeText(getApplicationContext(),"call is end",Toast.LENGTH_LONG).show();
        Log.d("TAGEnd=============================================", "endCall: ");
        stopSelf(); // Stop the service when the call is ended
    }
}
