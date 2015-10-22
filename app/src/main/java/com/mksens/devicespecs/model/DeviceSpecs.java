package com.mksens.devicespecs.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by nichel on 9/17/15.
 */
public class DeviceSpecs {
    @SerializedName("mac")
    public String mac;

    @SerializedName("timestamp")
    public String timestamp;

    @SerializedName("sensor")
    public String sensor;

    @SerializedName("detections")
    public ArrayList<Detection> detections = new ArrayList<>();
}
