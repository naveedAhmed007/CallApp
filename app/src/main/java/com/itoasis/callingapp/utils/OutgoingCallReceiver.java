package com.itoasis.callingapp.utils;

import android.os.Handler;
import android.os.Looper;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public class OutgoingCallReceiver extends PhoneStateListener {
    Singleton singleton=Singleton.getInstance();
    @Override
    public void onCallStateChanged(int state, String phoneNumber) {
        super.onCallStateChanged(state, phoneNumber);

        switch (state) {
            case TelephonyManager.CALL_STATE_IDLE:
                // Call is ended
                Log.d("OutgoingCallReceiver", "Call ended");
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                // Call is active
                Log.d("OutgoingCallReceiver=================", "Call active");
                singleton.changeListener();


                break;
            case TelephonyManager.CALL_STATE_RINGING:
                // Call is ringing
                Log.d("OutgoingCallReceiver", "Call ringing: " + phoneNumber);
                break;


        }
    }
}