package com.gist.unican.beaconsseta;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class RestartBluetooth extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("SERVICE STOPS", "Service Stops!");
        context.startService(new Intent(context, BackgroundService.class));;
    }
}
