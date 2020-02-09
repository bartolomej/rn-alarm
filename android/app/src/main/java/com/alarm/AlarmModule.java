package com.alarm;

import android.widget.Toast;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeArray;
import com.facebook.react.bridge.WritableNativeMap;

import java.util.ArrayList;
import java.util.Arrays;

public class AlarmModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;

    public AlarmModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
        AlarmHelper.createNotificationChannel(reactContext);
    }

    @Override
    public String getName() {
        return "AlarmModule";
    }

    @ReactMethod
    public void toast (String message) {
        Toast.makeText(reactContext, message, Toast.LENGTH_LONG).show();
    }

    @ReactMethod
    public void set (ReadableMap details) {
        Alarm alarm = parseAlarmObject(details);
        Service.schedule(reactContext, alarm);
    }

    @ReactMethod
    public void getAll (Promise promise) {
        try {
            Alarm[] alarms = Storage.getAllAlarms(reactContext);
            WritableNativeArray serializedAlarms = serializeArray(alarms);
            promise.resolve(serializedAlarms);
        } catch (Exception e) {
            promise.reject(e.getMessage(), e);
        }
    }

    private Alarm parseAlarmObject (ReadableMap alarm) {
        String uid = alarm.getString("uid");
        String title = alarm.getString("title");
        String description = alarm.getString("description");
        int hour = alarm.getInt("hour");
        int minutes = alarm.getInt("minutes");
        boolean repeating = alarm.getBoolean("repeating");
        ArrayList<Integer> days = new ArrayList<>();
        if (!alarm.isNull("days")) {
            ReadableArray rawDays = alarm.getArray("days");
            for (int i = 0; i < rawDays.size(); i++) {
                days.add(rawDays.getInt(i));
            }
        }
        return new Alarm(uid, days, hour, minutes, title, description, repeating);
    }

    private WritableMap serializeAlarmObject (Alarm alarm) {
        WritableNativeMap map = new WritableNativeMap();
        map.putString("uid", alarm.uid);
        map.putString("title", alarm.title);
        map.putString("description", alarm.description);
        map.putInt("hour", alarm.hour);
        map.putInt("minutes", alarm.minutes);
        map.putArray("days", serializeArray(alarm.days));
        map.putBoolean("repeating", alarm.type.equals(AlarmType.REPEATING));
        return map;
    }

    private WritableNativeArray serializeArray (ArrayList<Integer> a) {
        WritableNativeArray array = new WritableNativeArray();
        for (int value : a) array.pushInt(value);
        return array;
    }

    private WritableNativeArray serializeArray (Alarm[] a) {
        WritableNativeArray array = new WritableNativeArray();
        for (Alarm alarm : a) array.pushMap(serializeAlarmObject(alarm));
        return array;
    }
}