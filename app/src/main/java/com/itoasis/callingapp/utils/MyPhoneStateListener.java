package com.itoasis.callingapp.utils;

import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public class MyPhoneStateListener  extends PhoneStateListener {
    @Override
    public void onCallStateChanged(int state, String incomingNumber) {
        super.onCallStateChanged(state, incomingNumber);

        switch (state) {
            case TelephonyManager.CALL_STATE_IDLE:
                Log.d("PhoneState", "Call state: Idle");
                // Outgoing call has ended, perform actions here.
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                Log.d("PhoneState+++++", "Call state: Offhook");
                // Outgoing call is answered and active.
                break;
            case TelephonyManager.CALL_STATE_RINGING:
                Log.d("PhoneState-----", "Call state: Ringing");
                // Incoming call is ringing.
                break;
        }
    }
}
