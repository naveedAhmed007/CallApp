package com.itoasis.callingapp.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.widget.Toast;
public class NetworkChangeListener extends BroadcastReceiver {

    private boolean isConnected = false; // Track the network state

    @Override
    public void onReceive(Context context, Intent intent) {
        // Introduce a delay before checking and displaying the toast message
        new Handler().postDelayed(() -> {
            boolean newConnectedState = isConnectedToInternet(context);  

            if (newConnectedState != isConnected) {
                // The network state has changed
                isConnected = newConnectedState;

                if (isConnected) {
                    // Internet is connected
                    Toast.makeText(context, "Connected to the Internet", Toast.LENGTH_LONG).show();
                } else {
                    // Internet is not connected
                    Toast.makeText(context, "Not Connected to the Internet", Toast.LENGTH_LONG).show();
                    // Show a dialog or perform any other necessary action
                }
            }
        }, 1000); // Delay for 1 second (adjust as needed)
    }


    private boolean isConnectedToInternet(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnected();
        }
        return false;
    }
}
