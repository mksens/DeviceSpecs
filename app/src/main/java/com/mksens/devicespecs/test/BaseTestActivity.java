package com.mksens.devicespecs.test;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;


public abstract class BaseTestActivity extends AppCompatActivity {
    private static final String TAG = ProximityTestActivity.class.getCanonicalName();

    private static final int TEST_LENGHT_MILLISEC = 10000;

    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");

        super.onCreate(savedInstanceState);

        countDownTimer = new CountDownTimer(TEST_LENGHT_MILLISEC, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.i(TAG, "onTick");

                long seconds = millisUntilFinished / 1000;

                onProgressTest(seconds);
            }

            @Override
            public void onFinish() {
                Log.i(TAG, "onFinish");

                onProgressTest(0);
                onStopTest();

                showEndInfoDialog();
            }
        };

        showDescriptionDialog();
    }

    private void showDescriptionDialog() {
        Log.i(TAG, "showDescriptionDialog");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getDescription());
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Log.i(TAG, "onClick");

                onStartTest();

                countDownTimer.start();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showEndInfoDialog() {
        Log.i(TAG, "showEndInfoDialog");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Proxity test completed");
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Log.i(TAG, "onClick");

                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onResume() {
        Log.i(TAG, "onResume");

        super.onResume();

        //countDownTimer.start();
    }

    @Override
    protected void onPause() {
        Log.i(TAG, "onPause");

        super.onPause();

        countDownTimer.cancel();
    }

    protected abstract String getDescription();

    protected abstract void onStartTest();

    protected abstract void onProgressTest(long seconds);

    protected abstract void onStopTest();
}
