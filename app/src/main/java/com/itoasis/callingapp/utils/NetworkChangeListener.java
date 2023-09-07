package com.itoasis.callingapp.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
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
        new InternetAvailabilityTask(context).execute();
    }

    private static class InternetAvailabilityTask extends AsyncTask<Void, Void, Boolean> {
        private Context context;

        public InternetAvailabilityTask(Context context) {
            this.context = context;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            return isInternetAvailable(null);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            new Handler().postDelayed(() -> {
                // Handle the result here, update UI, show toasts, etc.
                if (result) {
                    // Internet is available
                    Toast.makeText(context, "Connected to the Internet", Toast.LENGTH_LONG).show();
                } else {
                    // Internet is not available
                    Toast.makeText(context, "Not Connected to the Internet", Toast.LENGTH_LONG).show();
                    // Show a dialog or perform any other necessary action
                }
            }, 1000); // Delay for 1 second
        }

        private boolean isInternetAvailable(Context context) {
            try {
                HttpURLConnection urlConnection = (HttpURLConnection) (new URL("https://www.example.com").openConnection());
                urlConnection.setRequestProperty("User-Agent", "Android");
                urlConnection.setRequestProperty("Connection", "close");
                urlConnection.setConnectTimeout(1500); // Adjust the timeout as needed
                urlConnection.connect();

                int responseCode = urlConnection.getResponseCode();

                if (responseCode == 200) {
                    // Internet is available
                    return true;
                } else if (isConnectedToWifiOrMobileData(context)) {
                    // Device is connected to Wi-Fi or mobile data, but internet is not available
                    return false;
                } else {
                    // Other response codes (potential issues)
                    return false;
                }
            } catch (IOException e) {
                // There was an error connecting to the server (no internet)
                return false;
            }
        }

        private boolean isConnectedToWifiOrMobileData(Context context) {
            ConnectivityManager connectivityManager =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager != null) {
                NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
                return activeNetwork != null && activeNetwork.isConnected();
            }
            return false;
        }
    }}