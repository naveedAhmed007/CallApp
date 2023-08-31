package com.itoasis.callingapp.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

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
            if (activeNetwork != null && activeNetwork.isConnected()) {
                // Check if the device can actually reach the internet by making a network request
//                if (isInternetAvailable(context)) {
//                    return true;
//                }
            }
        }
        return false;
    }

//    private boolean isInternetAvailable(Context context) {
//        try {
//            HttpURLConnection urlConnection = (HttpURLConnection) (new URL("https://www.google.com").openConnection());
//            urlConnection.setRequestProperty("User-Agent", "Android");
//            urlConnection.setRequestProperty("Connection", "close");
//            urlConnection.setConnectTimeout(1500); // Adjust the timeout as needed
//            urlConnection.connect();
//
//            int responseCode = urlConnection.getResponseCode();
//
//            if (responseCode == 200) {
//                // Internet is available
//                return true;
//            } else if (responseCode == 204) {
//                // Internet is connected but no data package
//                Toast.makeText(context, "Not Avalablity", Toast.LENGTH_SHORT).show();
//                return false;
//            } else {
//                // Other response codes (potential issues)
//                return false;
//            }
//        } catch (IOException e) {
//            // There was an error connecting to the server (no internet)
//            return false;
//        }
//    }
}