package com.itoasis.callingapp.receivers;

import static android.content.Context.TELECOM_SERVICE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.itoasis.callingapp.Activities.AdminBottomNavigation;
import com.itoasis.callingapp.Activities.MainActivity2;
import com.itoasis.callingapp.utils.CallListHelper;
import com.itoasis.callingapp.utils.CallManager;


public class PingAlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("TAGReceive==================================", "onReceive: ");
        cutTheCall(context);
//        Intent intent1 = new Intent(context, AdminBottomNavigation.class);
//        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP);
//
//
//        context.startActivity(intent1);


    }
    private boolean cutTheCall(Context context) {
        TelecomManager telecomManager = (TelecomManager) context.getApplicationContext().getSystemService(TELECOM_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED || telecomManager == null) {
            return false;
        }

        if (telecomManager.isInCall()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                telecomManager.endCall();
            }
        }
        return true;
    }

    }
