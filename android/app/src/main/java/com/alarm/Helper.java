package com.alarm;

import android.app.AlarmManager;
import android.app.Notification;
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

import java.util.Calendar;


class Helper {

    private static final String TAG = "AlarmHelper";

    static void scheduleAlarm(Context context, String alarmUid, long triggerAtMillis, int notificationID) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("ALARM_UID", alarmUid);
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
        Log.d(TAG, "SDK version: " + Build.VERSION.SDK_INT);
        Log.d(TAG, "scheduling alarm with notification id: " + notificationID);
        Log.d(TAG, "alarm scheduled to fire in " + (((float)(triggerAtMillis - System.currentTimeMillis())) / (1000 * 60)) + "min");
    }

    static void cancelAlarm(Context context, int notificationID) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                notificationID,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        alarmManager.cancel(pendingIntent);
        Log.d(TAG, "canceling alarm with notification id: " + notificationID);
    }

    static void sendNotification(Context context, Alarm alarm, int notificationID) {
        try {
            Notification mBuilder = getAlarmNotification(context, alarm, notificationID);
            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(notificationID, mBuilder);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static Notification getAlarmNotification(Context context, Alarm alarm, int notificationID) {
        return getNotification(context, notificationID, alarm.uid, alarm.title, alarm.description);
    }

    static void cancelNotification(Context context, int notificationId) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(notificationId);
        manager.cancelAll();
    }

    static void createNotificationChannel(Context context) {
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
            Log.d(TAG, "didn't need to create a notification channel");
        }
    }

    protected static Notification getNotification(Context context, int id, String alarmUid, String title, String description) {
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
                .setContentIntent(createOnClickedIntent(context, alarmUid, id))
                .setDeleteIntent(createOnDismissedIntent(context, alarmUid, id));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int largeIconResId = res.getIdentifier("ic_launcher", "mipmap", packageName);
            Bitmap largeIconBitmap = BitmapFactory.decodeResource(res, largeIconResId);
            if (largeIconResId != 0) builder.setLargeIcon(largeIconBitmap);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setCategory(NotificationCompat.CATEGORY_CALL);
            builder.setColor(Color.parseColor("blue"));
        }
        return builder.build();
    }

    private static PendingIntent createOnClickedIntent(Context context, String alarmUid, int notificationID) {
        Intent resultIntent = new Intent(context, Helper.getMainActivityClass(context));
        resultIntent.putExtra("ALARM_UID", alarmUid);
        return PendingIntent.getActivity(
                context,
                notificationID,
                resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private static PendingIntent createOnDismissedIntent(Context context, String alarmUid, int notificationId) {
        Intent intent = new Intent(context, DismissReceiver.class);
        intent.putExtra("NOTIFICATION_ID", notificationId);
        intent.putExtra("ALARM_UID", alarmUid);
        return PendingIntent.getBroadcast(context.getApplicationContext(), notificationId, intent, 0);
    }

    static Calendar getDate(int day, int hour, int minute) {
        Calendar date = Calendar.getInstance();
        Calendar today = Calendar.getInstance();
        date.set(Calendar.DAY_OF_WEEK, day);
        date.set(Calendar.HOUR_OF_DAY, hour);
        date.set(Calendar.MINUTE, minute);
        date.set(Calendar.SECOND, 0);
        if (date.before(today)) {
            date.add(Calendar.DATE, 7);
        }
        return date;
    }

    static Class getMainActivityClass(Context context) {
        String packageName = context.getPackageName();
        Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        try {
            String className = launchIntent.getComponent().getClassName();
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }
}
