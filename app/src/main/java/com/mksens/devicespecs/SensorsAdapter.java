package com.mksens.devicespecs;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import com.mksens.devicespecs.model.Statistic;
import com.mksens.devicespecs.test.ProximityTestActivity;

public class SensorsAdapter extends RecyclerView.Adapter<SensorsAdapter.ViewHolder> {
    private static final String TAG = SensorsAdapter.class.getCanonicalName();
    private static final ArrayList<Statistic> model = new ArrayList<>();
    private static Context context;

    public SensorsAdapter(final Context context) {
        Log.i(TAG, "onCreateViewHolder");

        SensorsAdapter.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i(TAG, "onCreateViewHolder");

        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.i_sensor, parent, false);

        return new ViewHolder(root);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Log.i(TAG, "onBindViewHolder");

        final Statistic stat = model.get(position);
        final String label = stat.name.substring(stat.name.lastIndexOf(".")+1);

        holder.tvValues.setText(String.valueOf(stat.ranking));
        holder.tvRate.setText(String.format("Rate: %.2f Hz", stat.rate*1000));
        holder.tvName.setText(label);
    }

    @Override
    public int getItemCount() {
        Log.i(TAG, "getItemCount");

        return model.size();
    }

    public void add(final Statistic stat) {
        Log.i(TAG, "add");
        Log.d(TAG, "stat: " + stat);

        model.add(stat);
    }

    public void clear() {
        Log.i(TAG, "delete");

        model.clear();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView tvValues;
        public final TextView tvRate;
        public final TextView tvName;

        public ViewHolder(View root) {
            super(root);

            tvValues = (TextView) root.findViewById(R.id.tvValues);
            tvRate = (TextView) root.findViewById(R.id.tvRate);
            tvName = (TextView) root.findViewById(R.id.tvName);

            root.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Log.i(TAG, "onClick");

            final int pos = getAdapterPosition();
            final Statistic stat = model.get(pos);

            Log.d(TAG, "pos: " + pos);
            Log.d(TAG, "stat: " + stat.toString());

            if (Sensor.STRING_TYPE_PROXIMITY.equals(stat.name)) {
                Intent proximity = new Intent(context, ProximityTestActivity.class);
                context.startActivity(proximity);
            } else {
                Snackbar.make(view, "Not Available", Snackbar.LENGTH_LONG).show();
            }
        }
    }
}
