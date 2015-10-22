package com.mksens.devicespecs.test;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.PointsGraphSeries;

import de.greenrobot.event.EventBus;
import com.mksens.devicespecs.LoggerService;
import com.mksens.devicespecs.R;
import com.mksens.devicespecs.SensorsService;
import com.mksens.devicespecs.event.ProximityEvent;

public class ProximityTestActivity extends BaseTestActivity {
    private static final String TAG = ProximityTestActivity.class.getCanonicalName();

    private Intent logService;
    private Intent sensorService;

    private TextView info;
    private TextView timer;

    private PointsGraphSeries<DataPoint> series;
    private double graph2LastXValue = 0d;

    private static final int GRAPH_SIZE = 1000; //millis
    private static final int GRAPH_STEP = 50; //millis

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            graph2LastXValue += 1d;

            series.appendData(new DataPoint(graph2LastXValue, proximity), true,  GRAPH_SIZE / GRAPH_STEP);
            handler.postDelayed(this, GRAPH_STEP);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");

        super.onCreate(savedInstanceState);

        setContentView(R.layout.a_proximiy_test);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        info = (TextView) findViewById(R.id.info);
        timer = (TextView) findViewById(R.id.timer);

        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor sensors = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        float maxY = sensors.getMaximumRange();

        GraphView graph = (GraphView) findViewById(R.id.graph);

        // set manual X bounds
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(GRAPH_SIZE / GRAPH_STEP);

        // set manual Y bounds
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(maxY);

        series = new PointsGraphSeries<>();
        graph.addSeries(series);

        logService = new Intent(this, LoggerService.class);
        sensorService = new Intent(this, SensorsService.class);
    }

    @Override
    protected String getDescription() {
        //FIXME
        return getString(R.string.proximitytest_descr);
    }

    @Override
    public void onStartTest() {
        Log.i(TAG, "onStartTest");

        proximity = 0;
        proximityTick = 0;

        handler.post(runnable);

        EventBus.getDefault().register(this);

        startService(logService);
        startService(sensorService);
    }

    @Override
    public void onProgressTest(long seconds) {
        Log.i(TAG, "onProgressTest");
        Log.d(TAG, "seconds: " + seconds);
        Log.d(TAG, "proximity tick: " + proximityTick);

        if (seconds != 0) {
            float hertz = proximityTick / seconds;
            info.setText(String.format("Avg. Frequency = %.3f Hz", hertz));
        }

        timer.setText(String.valueOf(seconds));
    }

    @Override
    public void onStopTest() {
        Log.i(TAG, "onStopTest");

        proximity = 0;
        proximityTick = 0;

        handler.removeCallbacks(runnable);

        EventBus.getDefault().unregister(this);

        stopService(logService);
        stopService(sensorService);
    }

    @Override
    public void onPause() {
        Log.i(TAG, "onPause");
        super.onPause();

        onStopTest();
        finish();
    }

    private float proximityTick = 0;
    private float proximity = 0;
    public void onEventMainThread(ProximityEvent event) {
        Log.i(TAG, "onEventMainThread");
        Log.d(TAG, "event: " + event);

        proximity = event.values[0];
        proximityTick++;
    }
}
