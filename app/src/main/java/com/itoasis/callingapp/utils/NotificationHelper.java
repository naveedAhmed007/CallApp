package com.itoasis.callingapp.utils;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.service.notification.NotificationListenerService;
import android.telecom.Call;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.itoasis.callingapp.R;
import com.itoasis.callingapp.call_screen;
import com.itoasis.callingapp.receivers.ActionReceiver;

public class NotificationHelper extends NotificationListenerService {

    public static int NOTIFICATION_ID = 834831;
    static Singleton singleton=Singleton.getInstance();
    private static int outgoingCallCount = 0;


    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void createIncomingNotification(Context context, Call call) {

        String callerPhoneNumber = call.getDetails().getHandle().getSchemeSpecificPart();
        String callerName = ContactsHelper.getContactNameByPhoneNumber(callerPhoneNumber, context);


        String CHANNEL_ID = "Hidden_Pirates_Phone_App";

        Uri ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);

        NotificationChannel channel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = new NotificationChannel(CHANNEL_ID, "Incoming Call Notification", NotificationManager.IMPORTANCE_DEFAULT);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel.setSound(ringtoneUri, new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build());
        }

        NotificationManager manager = context.getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel);


        Intent incomingCallIntent = new Intent(context, call_screen.class);
        incomingCallIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        PendingIntent incomingCallPendingIntent;
        incomingCallPendingIntent = PendingIntent.getActivity(context, 0, incomingCallIntent, PendingIntent.FLAG_MUTABLE);


        Intent answerCallIntent = new Intent(context, ActionReceiver.class);
        answerCallIntent.putExtra("pickUpCall", "YES");
        PendingIntent pickUpCallYesPendingIntent;

        pickUpCallYesPendingIntent = PendingIntent.getBroadcast(context, 1, answerCallIntent, PendingIntent.FLAG_MUTABLE);


        Intent rejectCallIntent = new Intent(context, ActionReceiver.class);
        rejectCallIntent.putExtra("pickUpCall", "NO");
        PendingIntent pickUpCallNoPendingIntent;
        pickUpCallNoPendingIntent = PendingIntent.getBroadcast(context, 2, rejectCallIntent, PendingIntent.FLAG_MUTABLE);


        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID);
        builder.setOngoing(true);
        builder.setPriority(Notification.PRIORITY_DEFAULT);
        builder.setContentIntent(incomingCallPendingIntent);
        builder.setFullScreenIntent(incomingCallPendingIntent, true);
//        builder.setSmallIcon(R.drawable.ic_call_green);
        builder.setContentTitle(callerName);
        builder.setContentText(callerPhoneNumber);
        builder.setCategory(Notification.CATEGORY_CALL);
        builder.setChannelId(CHANNEL_ID);
//        builder.addAction(R.drawable.ic_call_green, "Answer", pickUpCallYesPendingIntent);
//        builder.addAction(R.drawable.ic_call_end_red, "Reject", pickUpCallNoPendingIntent);


        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.cancel(NOTIFICATION_ID);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

//    _____________________________________________________________________________________________________________
//    _____________________________________________________________________________________________________________

    public static void createIngoingCallNotification(Context context, Call call, String callDuration, String speakerBtnTxt, String muteBtnTxt) {

        String callerPhoneNumber, callerName;

        if (call.getDetails().hasProperty(Call.Details.PROPERTY_CONFERENCE)) {
            callerPhoneNumber = "Conference";
            callerName = "Conference";
        } else {
            callerPhoneNumber = call.getDetails().getHandle().getSchemeSpecificPart();
            callerName = ContactsHelper.getContactNameByPhoneNumber(callerPhoneNumber, context);
        }

        String CHANNEL_ID = "com.itoasis.Calling_APP";

        NotificationChannel channel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = new NotificationChannel(CHANNEL_ID, "Ingoing Call Notification", NotificationManager.IMPORTANCE_DEFAULT);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel.setSound(null, null);
        }

        NotificationManager manager = context.getSystemService(NotificationManager.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(channel);
            singleton.incrementCounterCalls();
        }


        Intent ingoingCallIntent = new Intent(context, call_screen.class);
        ingoingCallIntent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent ingoingCallPendingIntent;

        ingoingCallPendingIntent = PendingIntent.getActivity(context, 0, ingoingCallIntent, PendingIntent.FLAG_MUTABLE);


        Intent endCallIntent = new Intent(context, ActionReceiver.class);
        endCallIntent.putExtra("endCall", "YES");

        PendingIntent endCallPendingIntent;

        endCallPendingIntent = PendingIntent.getBroadcast(context, 1, endCallIntent, PendingIntent.FLAG_MUTABLE);


        Intent speakerCallIntent = new Intent(context, ActionReceiver.class);
        speakerCallIntent.putExtra("speakerCall", "YES");

        PendingIntent speakerCallPendingIntent;

        speakerCallPendingIntent = PendingIntent.getBroadcast(context, 2, speakerCallIntent, PendingIntent.FLAG_MUTABLE);


        Intent muteCallIntent = new Intent(context, ActionReceiver.class);
        muteCallIntent.putExtra("muteCall", "YES");

        PendingIntent muteCallPendingIntent;

        muteCallPendingIntent = PendingIntent.getBroadcast(context, 3, muteCallIntent, PendingIntent.FLAG_MUTABLE);

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID);
        builder.setOngoing(true);
        builder.setPriority(Notification.PRIORITY_DEFAULT);
        builder.setContentIntent(ingoingCallPendingIntent);
        builder.setFullScreenIntent(ingoingCallPendingIntent, true);
        builder.setSmallIcon(R.drawable.pen_icon);
//       builder.setSmallIcon(R.drawable.ic_call_green);
        builder.setContentInfo(callDuration);
        builder.setOnlyAlertOnce(true);
        builder.setContentTitle(callerName);
        builder.setContentText(callerPhoneNumber);
        builder.setCategory(Notification.CATEGORY_CALL);
        builder.setChannelId(CHANNEL_ID);
        builder.addAction(R.drawable.pen_icon, "End Call", endCallPendingIntent);
        builder.addAction(R.drawable.pen_icon, speakerBtnTxt, speakerCallPendingIntent);
        builder.addAction(R.drawable.pen_icon, muteBtnTxt, muteCallPendingIntent);
//builder.addAction(R.drawable.ic_call_end_red, "End Call", endCallPendingIntent);
//        builder.addAction(R.drawable.ic_volume_up, speakerBtnTxt, speakerCallPendingIntent);
//        builder.addAction(R.drawable.ic_volume_up, muteBtnTxt, muteCallPendingIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);


        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

//    _____________________________________________________________________________________________________________
//    _____________________________________________________________________________________________________________

    public static void createOutgoingNotification(Context context, Call call) {
        outgoingCallCount++;
        Log.d("OutgoingCall==================================",String.valueOf(outgoingCallCount));
        String callerPhoneNumber = call.getDetails().getHandle().getSchemeSpecificPart();
        String callerName = ContactsHelper.getContactNameByPhoneNumber(callerPhoneNumber, context);

        String CHANNEL_ID = "Hidden_Pirates_Phone_App";

        NotificationChannel channel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = new NotificationChannel(CHANNEL_ID, "Outgoing Call Notification", NotificationManager.IMPORTANCE_DEFAULT);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        }

        NotificationManager manager = context.getSystemService(NotificationManager.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(channel);
        }


        Intent outingCallIntent = new Intent(context, call_screen.class);
        outingCallIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);

        PendingIntent outgoingCallPendingIntent;

        outgoingCallPendingIntent = PendingIntent.getActivity(context, 0, outingCallIntent, PendingIntent.FLAG_MUTABLE);


        Intent cancelCallIntent = new Intent(context, ActionReceiver.class);
        cancelCallIntent.putExtra("cancelCall", "YES");

        PendingIntent pickUpCallYesPendingIntent;

        pickUpCallYesPendingIntent = PendingIntent.getBroadcast(context, 1, cancelCallIntent, PendingIntent.FLAG_IMMUTABLE);

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID);
        builder.setOngoing(true);
        builder.setPriority(Notification.PRIORITY_DEFAULT);
        builder.setContentIntent(outgoingCallPendingIntent);
        builder.setFullScreenIntent(outgoingCallPendingIntent, true);
//        builder.setSmallIcon(R.drawable.ic_call_green);
        builder.setSmallIcon(R.drawable.pen_icon);
        builder.setContentTitle(callerName);
        builder.setContentText(callerPhoneNumber);
        builder.setCategory(Notification.CATEGORY_CALL);
        builder.setChannelId(CHANNEL_ID);
        builder.addAction(R.drawable.pen_icon, "Cancel", pickUpCallYesPendingIntent);
//        builder.addAction(R.drawable.ic_call_end_red, "Cancel", pickUpCallYesPendingIntent);


        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
    public static void cancelNotification(Context context, int notificationId) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.cancel(notificationId);
    }
}
