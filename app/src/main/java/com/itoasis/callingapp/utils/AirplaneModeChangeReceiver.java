package com.itoasis.callingapp.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.telecom.TelecomManager;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

public class AirplaneModeChangeReceiver extends BroadcastReceiver {
    Singleton singleTon= Singleton.getInstance();

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context.getApplicationContext(), "jjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjj",Toast.LENGTH_SHORT).show();
        String action = intent.getAction();



        if (action.equals("call_ended")) {

//                    CallManager.hangUpCall(CallListHelper.callList.get(CallManager.NUMBER_OF_CALLS - 1));
        }
        if (action.equals("call_answered")) {
            singleTon.incrementAnsweredCall();
            if(singleTon.getActivityCall()==1){
                placeCall(singleTon.getPhoneNumber(),context);


            }
            if(singleTon.getActivityCall()==2){
                if(singleTon.getAnsweredcall()==5){
                    singleTon.resetActivityCall();
                    singleTon.resetAnswerCall();
                    CallListHelper.callList.get(CallManager.NUMBER_OF_CALLS - 2).conference(CallListHelper.callList.get(CallManager.NUMBER_OF_CALLS - 1));
                    CallListHelper.callList.get(CallManager.NUMBER_OF_CALLS - 1).mergeConference();
                    android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(1);
                }
            }

        }


    }
    private void placeCall(String phoneNumber,Context context) {

        @SuppressLint("ServiceCast")
        TelecomManager telecomManager = (TelecomManager) context.getSystemService(Context.TELECOM_SERVICE);
        Uri uri = Uri.fromParts("tel", phoneNumber, null);
        Bundle extras = new Bundle();
        extras.putBoolean(TelecomManager.EXTRA_START_CALL_WITH_SPEAKERPHONE, false);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            if (telecomManager.getDefaultDialerPackage().equals(context.getPackageName())) {
                // Directly place the call using TelecomManager API
                telecomManager.placeCall(uri, extras);
            } else {
                // If your app is not the default dialer, you cannot use the TelecomManager API directly.
                // You'll need to request the CALL_PHONE permission and start the call using ACTION_CALL intent.
                Uri phoneNumberUri = Uri.parse("tel:" + phoneNumber);
                Intent callIntent = new Intent(Intent.ACTION_CALL, phoneNumberUri);
                callIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(callIntent);
            }
        } else {
            Toast.makeText(context.getApplicationContext(), "Please allow permission", Toast.LENGTH_SHORT).show();
        }


    }
    }


