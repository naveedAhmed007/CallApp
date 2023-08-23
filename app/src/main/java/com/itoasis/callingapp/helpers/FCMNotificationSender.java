package com.itoasis.callingapp.helpers;

import okhttp3.*;
import java.io.IOException;

public class FCMNotificationSender {
    public static void main(String[] args) throws IOException {
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        String json = "{ \"to\": \"TARGET_DEVICE_FCM_TOKEN\", \"notification\": { \"title\": \"Notification Title\", \"body\": \"Notification Body\" } }";
        RequestBody body = RequestBody.create(mediaType, json);

        Request request = new Request.Builder()
                .url("https://fcm.googleapis.com/fcm/send")
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "key=YOUR_SERVER_KEY")
                .build();

        Response response = client.newCall(request).execute();
        System.out.println(response.body().string());
    }
}
