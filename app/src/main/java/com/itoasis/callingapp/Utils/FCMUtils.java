package com.itoasis.callingapp.Utils;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FCMUtils {

    private static final String FCM_API = "https://fcm.googleapis.com/fcm/send";
    private static final String SERVER_KEY = "AAAAkJ3ZxWA:APA91bHvEvzbyP3ORzr4zcmIvDBwt_X0So3mzuMw5IyMjS2Ite_AiHQHB1gOLRghrV55CA8u4hwnZRxOF_fqFXJnzO8mlrs3AenAjnIXgKzG29pAorTlNpmi3azs_a4Exom0WOcwHucl"; // Replace with your FCM server key

    public static void sendPushNotification(String to, String title, String body) {
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody bodyData = RequestBody.create(mediaType, buildPushPayload(to, title, body));

        Request request = new Request.Builder()
                .url(FCM_API)
                .post(bodyData)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "key=" + SERVER_KEY)
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                System.out.println("Push notification sent successfully!");
            } else {
                System.out.println("Failed to send push notification: " + response.message());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String buildPushPayload(String to, String title, String body) {
        return "{"
                + "\"to\": \"" + to + "\","
                + "\"notification\": {"
                + "\"title\": \"" + title + "\","
                + "\"body\": \"" + body + "\""
                + "}"
                + "}";
    }
}
