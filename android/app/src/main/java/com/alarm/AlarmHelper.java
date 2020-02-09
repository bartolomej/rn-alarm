package com.alarm;

import android.app.AlarmManager;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.alarm.receivers.AlarmReceiver;
import com.alarm.receivers.DismissReceiver;
import com.app.R;

import java.util.Date;

public class AlarmHelper {

    private static final String TAG = "AlarmHelper";

    public static void scheduleAlarm (Context context, long triggerAtMillis, int notificationID) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("alarm", "test intent message");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                notificationID,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
        }

        Log.d(TAG, "scheduling alarm with notification id: " + notificationID);
        Log.d(TAG, "alarm scheduled to fire in " + (((float)(triggerAtMillis - System.currentTimeMillis())) / (1000 * 60)) + "min");
    }

    public static void cancelAlarm (Context context, int notificationID) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                notificationID,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        alarmManager.cancel(pendingIntent);
    }

    public static void sendNotification(Application context, Alarm alarm, Date date) {
        try {
            int notificationID = Alarm.getNotificationId(date);
            NotificationCompat.Builder mBuilder = getNotificationBuilder(context, notificationID, alarm.title, alarm.description);
            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(notificationID, mBuilder.build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static PendingIntent createOnClickedIntent(Context context, int notificationID) {
        Intent resultIntent = new Intent(context, Utils.getMainActivityClass(context));
        //resultIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        //resultIntent.putExtras(alarm.toBundle());

        return PendingIntent.getActivity(
                context,
                notificationID,
                resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private static PendingIntent createOnDismissedIntent(Context context, int notificationId) {
        Intent intent = new Intent(context, DismissReceiver.class);
        intent.putExtra("notification_id", notificationId);
        return PendingIntent.getBroadcast(context.getApplicationContext(), notificationId, intent, 0);
    }

    public static void cancelNotification (Context context, int notificationId) {
        NotificationManager manager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(notificationId);
        manager.cancelAll();
    }

    public static void createNotificationChannel(Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String id = context.getResources().getString(R.string.notification_channel_id);
            String name = context.getResources().getString(R.string.notification_channel_name);
            String description = context.getResources().getString(R.string.notification_channel_desc);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(id, name, importance);
            channel.setDescription(description);
            channel.enableLights(true);
            // Sets the notification light color for notifications posted to this
            // channel, if the device supports this feature.
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = ContextCompat.getSystemService(context, NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

            Log.d(TAG, "created a notification channel " + channel.toString());
        } else {
            Log.d(TAG, "didn't have to create a notification channel");
        }
    }

    private static NotificationCompat.Builder getNotificationBuilder (Context context, int id, String title, String description) {
        Resources res = context.getResources();
        String packageName = context.getPackageName();
        int smallIconResId = res.getIdentifier("ic_launcher", "mipmap", packageName);
        String channelId = context.getResources().getString(R.string.notification_channel_id);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(smallIconResId)
                .setContentTitle(title)
                .setTicker(null)
                .setContentText(description)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setAutoCancel(false)
                .setSound(null)
                .setVibrate(null)
                .setContentIntent(createOnClickedIntent(context, id))
                .setDeleteIntent(createOnDismissedIntent(context, id));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int largeIconResId = res.getIdentifier("ic_launcher", "mipmap", packageName);
            Bitmap largeIconBitmap = BitmapFactory.decodeResource(res, largeIconResId);

            if (largeIconResId != 0) builder.setLargeIcon(largeIconBitmap);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setCategory(NotificationCompat.CATEGORY_CALL);
            builder.setColor(Color.parseColor("blue"));
        }

        return builder;
    }
}
