package com.mksens.devicespecs;

import android.hardware.Sensor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import retrofit.Callback;
import retrofit.Response;
import com.mksens.devicespecs.model.Statistic;
import com.mksens.devicespecs.rest.RestClient;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getCanonicalName();

    private SensorsAdapter sensorsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_main);

        sensorsAdapter = new SensorsAdapter(this);
        sensorsAdapter.clear();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rvDevices);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(sensorsAdapter);
    }

    @Override
    protected void onResume() {
        Log.i(TAG, "onResume");

        refresh();

        super.onResume();
    }

    private void refresh() {
        Log.i(TAG, "refresh");

        RestClient.get().getStat(DeviceSpecsApp.macAddress, "Proximity").enqueue(new Callback<Statistic>() {
            @Override
            public void onResponse(Response<Statistic> response) {
                Log.i(TAG, "onResponse");

                Statistic stat = response.body();

                if (stat != null) {
                    stat.name = "Proximity";

                    Log.d(TAG, "stat: " + stat.toString());

                    sensorsAdapter.clear();
                    sensorsAdapter.add(stat);
                    sensorsAdapter.notifyDataSetChanged();
                } else {
                    stat = new Statistic();
                    stat.name = "Proximity";
                    stat.ranking = -1;

                    sensorsAdapter.clear();
                    sensorsAdapter.add(stat);
                    sensorsAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.i(TAG, "onFailure");
                Log.d(TAG, "t: " + t.getMessage());

                Statistic stat = new Statistic();
                stat.name = "Proximity";
                stat.ranking = -1;

                sensorsAdapter.clear();
                sensorsAdapter.add(stat);
                sensorsAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onPause() {
        Log.i(TAG, "onPause");

        super.onPause();
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "onDestroy");

        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i(TAG, "onCreateOptionsMenu");

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.m_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(TAG, "onOptionsItemSelected");

        switch (item.getItemId()) {
            case R.id.action_refresh:
                refresh();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
