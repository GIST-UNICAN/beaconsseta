package com.gist.unican.beaconsseta;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

/**
 * Created by andres on 05/09/2017.
 */

public class BootBroadcastReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent startService = new Intent(context,LaunchServiceOnBoot.class);
        startWakefulService(context,startService);
    }
}
