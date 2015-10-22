package com.mksens.devicespecs.event;

/**
 * Created by nichel on 9/17/15.
 */
public class ProximityEvent {
    public int name;
    public float[] values;
    public long timestamp;

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("ProximityEvent{");
        sb.append("name='").append(name).append('\'');
        sb.append(", values=");
        if (values == null) sb.append("null");
        else {
            sb.append('[');
            for (int i = 0; i < values.length; ++i)
                sb.append(i == 0 ? "" : ", ").append(values[i]);
            sb.append(']');
        }
        sb.append(", timestamp=").append(timestamp);
        sb.append('}');
        return sb.toString();
    }
}
