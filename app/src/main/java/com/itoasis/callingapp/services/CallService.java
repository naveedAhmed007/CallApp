package com.itoasis.callingapp.services;

import android.content.Intent;
import android.telecom.Call;
import android.telecom.InCallService;
import android.widget.Toast;

import com.itoasis.callingapp.call_screen;
import com.itoasis.callingapp.utils.Singleton;

public class CallService extends InCallService {
    int call_state;
    Singleton singleton=Singleton.getInstance();
    @Override
    public void onCallAdded(Call call) {
        super.onCallAdded(call);
        if (call_state == Call.STATE_RINGING){


        }
        else if (call_state == Call.STATE_CONNECTING || call_state == Call.STATE_DIALING){
            singleton.incrementActivityCall();
            Intent intent = new Intent(this, call_screen.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

            Toast.makeText(this, "Dialing to " + call.getDetails().getHandle().getSchemeSpecificPart(), Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onCallRemoved(Call call) {


    }

}
