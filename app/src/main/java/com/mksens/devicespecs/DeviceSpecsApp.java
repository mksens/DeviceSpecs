package com.mksens.devicespecs;

import android.app.Application;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

/**
 * Created by nichel on 9/18/15.
 */
public class DeviceSpecsApp extends Application {
    private static final String TAG = MainActivity.class.getCanonicalName();

    public static String macAddress;

    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate");

        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        macAddress = wifiInfo.getMacAddress();

        super.onCreate();
    }
}
