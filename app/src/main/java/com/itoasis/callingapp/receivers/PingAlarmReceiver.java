package com.itoasis.callingapp.receivers;

import static android.content.Context.TELECOM_SERVICE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.itoasis.callingapp.Activities.AdminBottomNavigation;
import com.itoasis.callingapp.Activities.MainActivity2;
import com.itoasis.callingapp.utils.CallListHelper;
import com.itoasis.callingapp.utils.CallManager;


public class PingAlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("TAGReceive==================================", "onReceive: ");
//        Intent intent1 = new Intent(context, MainActivity2.class);
//        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP);
//
//
//        context.startActivity(intent1);

    }
    }
