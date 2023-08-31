package com.itoasis.callingapp.utils;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

public class CallDetection  extends AccessibilityService {
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
            Log.d("myaccess","in window changed");
            AccessibilityNodeInfo info = event.getSource();
            if (info != null && info.getText() != null) {
                String duration = info.getText().toString();
                String zeroSeconds = String.format("%02d:%02d", new Object[]{Integer.valueOf(0), Integer.valueOf(0)});
                String firstSecond = String.format("%02d:%02d", new Object[]{Integer.valueOf(0), Integer.valueOf(1)});
                Log.d("myaccess","after calculation - "+ zeroSeconds + " --- "+ firstSecond + " --- " + duration);
                if (zeroSeconds.equals(duration) || firstSecond.equals(duration)) {
                    Toast.makeText(getApplicationContext(),"Call answered",Toast.LENGTH_SHORT).show();
                    // Your Code goes here
                }
                info.recycle();
            }
        }

    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Toast.makeText(this,"Service connected",Toast.LENGTH_SHORT).show();
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.eventTypes = AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
        info.notificationTimeout = 0;
        info.packageNames = null;
        setServiceInfo(info);
    }

    @Override
    public void onInterrupt() {

    }
}
