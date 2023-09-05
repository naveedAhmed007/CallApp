package com.itoasis.callingapp.services;


import static com.itoasis.callingapp.call_screen.muteBtnName;
import static com.itoasis.callingapp.call_screen.speakerBtnName;
import static com.itoasis.callingapp.utils.NotificationHelper.*;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.telecom.Call;
import android.telecom.InCallService;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.itoasis.callingapp.call_screen;
import com.itoasis.callingapp.utils.CallListHelper;
import com.itoasis.callingapp.utils.CallManager;
import com.itoasis.callingapp.utils.NotificationHelper;
import com.itoasis.callingapp.utils.Singleton;

public class CallService extends InCallService {

    int call_state;
    Singleton singleTon=Singleton.getInstance();

    @SuppressLint({"SetTextI18n", "UseCompatTextViewDrawableApis"})
    @Override
    public void onCallAdded(Call call) {
        super.onCallAdded(call);


        if (call.getDetails().hasProperty(Call.Details.PROPERTY_CONFERENCE)){

            CallListHelper.callList.clear();
//            CallActivity.callerNameTV.setText("Conference Call");
//            CallActivity.callerPhoneNumberTV.setText("");

//            CallActivity.addCallBtn.setEnabled(true);
//            CallActivity.addCallBtn.setClickable(true);
//            CallActivity.addCallBtn.setCompoundDrawableTintList(ColorStateList.valueOf(getColor(R.color.my_theme)));
//            CallActivity.addCallBtn.setTextColor(getColor(R.color.my_theme));
//
//            CallActivity.holdBtn.setEnabled(true);
//            CallActivity.holdBtn.setClickable(true);
//            CallActivity.holdBtn.setCompoundDrawableTintList(ColorStateList.valueOf(getColor(R.color.my_theme)));
//            CallActivity.holdBtn.setTextColor(getColor(R.color.my_theme));
//
//            CallActivity.mergeCallBtn.setVisibility(View.GONE);
//
//            String speakerBtnName, muteBtnName;
//
//            if (CallActivity.isSpeakerOn){
//                speakerBtnName = "Speaker Off";
//            }
//            else{
//                speakerBtnName = "Speaker On";
//            }
//
//            if (CallActivity.isMuted){
//                muteBtnName = "Unmute";
//            }
//            else{
//                muteBtnName = "Mute";
//            }

            NotificationHelper.createIngoingCallNotification(this, call, "12:00:4", speakerBtnName, muteBtnName);
        }

        CallListHelper.callList.add(call);
        CallManager.inCallService = this;
        CallManager.NUMBER_OF_CALLS = CallListHelper.callList.size();

//        Log.d(MainActivity.TAG, "onCallAdded: NUM " + CallManager.NUMBER_OF_CALLS);

        call.registerCallback(CallManager.callback);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            call_state = call.getDetails().getState();
        }

        CallManager.HP_CALL_STATE = call_state;

        if (call_state == Call.STATE_RINGING){

            Intent intent = new Intent(this, call_screen.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

            Toast.makeText(this, "Incoming call from " + call.getDetails().getHandle().getSchemeSpecificPart(), Toast.LENGTH_SHORT).show();
        }
        else if (call_state == Call.STATE_CONNECTING || call_state == Call.STATE_DIALING){
            singleTon.incrementActivityCall();
            Intent intent = new Intent(this, call_screen.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);


            Toast.makeText(this, "Dialing to " + call.getDetails().getHandle().getSchemeSpecificPart(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCallRemoved(Call call) {
        Toast.makeText(this, "Call ended", Toast.LENGTH_SHORT).show();

        if (CallListHelper.callList.size() > 0){

//            NotificationHelper.createIngoingCallNotification(this, CallListHelper.callList.get(CallManager.NUMBER_OF_CALLS - 1), "00:12:45", CallActivity.speakerBtnName, CallActivity.muteBtnName);
            NotificationHelper.createIngoingCallNotification(this, CallListHelper.callList.get(CallManager.NUMBER_OF_CALLS - 1), "00:12:45", speakerBtnName, muteBtnName);
            CallManager.HP_CALL_STATE = Call.STATE_ACTIVE;

//            Log.d(MainActivity.TAG, "onCallRemoved:........ " + call.getDetails().getDisconnectCause().toString());
        }
        else{
//            Log.d(MainActivity.TAG, "onCallRemoved:________ " + call.getDetails().getDisconnectCause().toString());
        }
    }
}