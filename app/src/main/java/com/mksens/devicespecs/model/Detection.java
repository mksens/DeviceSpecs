package com.mksens.devicespecs.model;

import com.google.gson.annotations.SerializedName;

public class Detection {
    @SerializedName("timestamp")
    public long timestamp;

    @SerializedName("values")
    public float[] values;
}
