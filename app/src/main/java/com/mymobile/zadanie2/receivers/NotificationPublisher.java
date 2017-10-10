package com.mymobile.zadanie2.receivers;

import android.app.Notification;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.mymobile.zadanie2.R;

/**
 * Created by tomek on 08.10.17.
 */

public class NotificationPublisher extends BroadcastReceiver {

    public static String NOTIFICATION_ID = "notification-id";
    public static String NOTIFICATION = "notification";

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i("receiver", "received something");

        String name = intent.getStringExtra(NOTIFICATION);
        int id = intent.getIntExtra(NOTIFICATION_ID, 0);
        Log.i("receiver", "id=" + String.valueOf(id));

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (name.equals("")) {
            notificationManager.cancel(id);
        }
        else {
            Notification notification = getNotification(context, name);
            notificationManager.notify(id, notification);
        }
    }

    private Notification getNotification(Context context, String event) {
        Notification n = new Notification.Builder(context)
        .setContentTitle("POWIADOMIENIE")
        .setContentText(event)
        .setSmallIcon(R.mipmap.ic_launcher)
        .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
        .build();

        return n;
    }
}
