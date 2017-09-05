package com.gist.unican.beaconsseta;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.WakefulBroadcastReceiver;

public class LaunchServiceOnBoot extends IntentService {

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public LaunchServiceOnBoot(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        WakefulBroadcastReceiver.completeWakefulIntent(intent);
        Intent startServiceIntent = new Intent(getApplicationContext(),BackgroundService.class);
        startService(startServiceIntent);
    }
}
