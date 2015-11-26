package com.mksens.devicespecs;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Calendar;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.Response;
import com.mksens.devicespecs.event.ProximityEvent;
import com.mksens.devicespecs.model.Detection;
import com.mksens.devicespecs.model.DeviceSpecs;
import com.mksens.devicespecs.rest.RestClient;

public class LoggerService extends Service {
    private static final String TAG = LoggerService.class.getCanonicalName();

    private DeviceSpecs deviceSpecs = new DeviceSpecs();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate");

        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();

        deviceSpecs.mac = wifiInfo.getMacAddress();
        deviceSpecs.detections.clear();

        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand");

        deviceSpecs.timestamp = Calendar.getInstance().getTime().toString();
        deviceSpecs.detections.clear();

        EventBus.getDefault().register(this);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy");

        EventBus.getDefault().unregister(this);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(deviceSpecs);
        Log.d(TAG, "json: " + json);

        RestClient.get().postSpecs(deviceSpecs).enqueue(new Callback<com.mksens.devicespecs.rest.Response>() {
            @Override
            public void onResponse(Response<com.mksens.devicespecs.rest.Response> response) {
                Log.i(TAG, "onResponse");

                String message = response.message();
                Log.d(TAG, "message: " + message);
            }

            @Override
            public void onFailure(Throwable t) {
                Log.i(TAG, "onFailure");
            }
        });

        deviceSpecs.detections.clear();

        super.onDestroy();
    }

    public void onEventMainThread(ProximityEvent event) {
        Log.i(TAG, "onEventMainThread");
        Log.d(TAG, "event: " + event);

        Detection detection = new Detection();
        detection.values = event.values;
        detection.timestamp = event.timestamp;

        deviceSpecs.sensor = "Proximity";
        deviceSpecs.detections.add(detection);
    }
}
