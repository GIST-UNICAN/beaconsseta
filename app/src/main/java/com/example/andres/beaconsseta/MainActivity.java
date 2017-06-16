package com.example.andres.beaconsseta;

import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.github.pwittchen.reactivebeacons.library.rx2.Beacon;
import com.github.pwittchen.reactivebeacons.library.rx2.Proximity;
import com.github.pwittchen.reactivebeacons.library.rx2.ReactiveBeacons;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.support.v4.content.PermissionChecker.PERMISSION_GRANTED;


public class MainActivity extends AppCompatActivity {

    private static final boolean IS_AT_LEAST_ANDROID_M =
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    private static final int PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION = 1000;
    private static final String ITEM_FORMAT = "MAC: %s, RSSI: %d\ndistance: %.2fm, proximity: %s\n%s";
    private ReactiveBeacons reactiveBeacons;
    private Disposable subscription;
    private ListView lvBeacons;
    private Map<String, Beacon> beacons;
    Boolean primeraVez = true;

    //guardamos una lista de macs de las beacons de estimote
    private List<String[]> listaMacsBeacons = new ArrayList<>();
    private long lastSavedTime;
    private long WAIT_TIME_REFRESH_DATA = 10000;
    private long WAIT_TIME_SAVE_VALUES = 10000; //wait time in milisecons betwen saved values

    //lista de nuestras beacons
    private List<String> gistBeacons = Arrays.asList("D7:AA:CD:F8:7E:37", "C5:94:C6:D9:38:1D", "D3:25:FA:E3:EB:FB", "D2:B1:C2:B1:0C:57", "DD:4F:5A:47:2A:BA", "D3:1B:44:EA:4C:84", "F7:63:22:02:34:6C", "D9:CD:AC:E5:12:A3", "D3:1A:FB:29:4D:53", "C6:4D:9B:DF:CB:C0", "CF:94:C6:9C:0C:3B", "C5:C0:1D:4A:6A:78", "E8:9B:59:FB:B2:40", "F2:BE:02:A5:6E:B5", "DA:67:DC:1B:86:9E", "CA:6D:C9:D8:A2:0F", "E8:BA:1D:5A:64:97", "EC:20:61:F3:E8:45", "E5:41:0C:BA:6F:8A", "D7:4F:BF:4E:57:8E", "D0:9B:11:31:0C:CA", "F0:49:44:A4:E8:55", "E1:CA:37:33:67:20", "FB:01:2D:25:2D:55", "C1:E1:A2:22:00:CE", "EE:34:31:F1:E2:C4", "E1:6D:8C:C4:8B:C2", "EF:82:9D:95:5F:2E", "CD:4B:63:E3:BC:75", "C8:18:56:64:1A:C8", "C9:F4:E8:2E:D8:C0", "FB:B7:08:A9:AA:40", "D9:27:16:DB:43:8A", "C1:8F:D3:C2:DC:FC", "C7:7B:B3:DA:B7:CC", "CA:52:EF:40:4B:5F", "FF:3E:4E:ED:79:17", "EC:5F:9C:F8:DC:48", "F7:15:6E:32:01:D4", "", "D6:C9:17:29:84:78", "CA:DC:61:60:11:D8", "FF:99:81:25:BB:D5", "E5:DE:0F:58:3A:25", "F4:8A:87:B1:94:09", "F1:25:EA:C0:3B:5A", "DA:A3:21:0F:31:92", "DB:EB:B3:09:5B:8B", "CA:77:AC:B6:CB:A6", "C6:CB:3D:7D:D2:0D", "CD:EA:07:6C:4F:9B", "FA:6E:5F:60:89:31", "C9:62:24:B2:29:4A", "C4:C2:2E:B6:94:36", "C4:F8:53:EB:F9:12", "C6:F3:F5:D2:86:C4", "CC:1D:BE:FC:3C:33", "F2:F7:OE:E7:E7:DD", "DE:31:23:DB:33:54", "D5:4A:31:5D:2F:3C", "ED:5B:50:78:29:E1", "D4:B1:72:0A:4D:03", "D1:39:C2:30:EB:E9", "DB:5D:D2:F8:5B:12", "D4:D7:03:5A:42:34", "C1:F9:9E:F8:49:B7", "CA:61:2D:9F:B6:08", "CA:42:95:1E:6A:3B", "F2:44:BE:2C:1A:BE", "FB:88:2B:AC:C3:61", "E5:CA:6B:46:CF:88", "C2:D9:6D:3F:A9:34", "E4:E4:98:78:E9:E9", "DD:2B:2D:85:CD:56", "C8:C5:30:D5:0E:A0", "DA:51:CC:FC:37:DD", "FC:54:6B:E9:8C:B7", "DC:14:94:F4:C0:8F", "E4:26:3F:66:3C:78", "D7:82:34:A7:FF:84", "EF:1C:D4:48:EE:5C", "ED:1B:59:4C:AF:AE", "D6:22:94:36:23:92", "FB:FB:BF:7B:0E:92", "D3:F6:99:F3:BF:35", "C5:31:95:A9:BB:3C", "D8:8E:6F:6B:2E:1E", "D7:5D:1B:B5:DD:2F", "FB:45:98:75:F8:5B", "DC:71:4A:5E:20:1E", "EA:ED:79:53:C1:7A", "E5:38:23:35:80:16", "E7:D0:C9:71:16:D7", "C1:8F:98:56:B8:E9", "C3:AB:83:4A:2C:73", "D1:F7:CE:27:0D:8A", "C7:BB:E3:42:OF:86", "E6:02:36:AF:4D:20", "C7:19:04:2C:9E:9E");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        reactiveBeacons = new ReactiveBeacons(this);
        beacons = new HashMap<>();

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!canObserveBeacons()) {
            return;
        }
        Intent i = new Intent(this, BackgroundService.class);
        this.startService(i);
    }


    private boolean canObserveBeacons() {
        if (!reactiveBeacons.isBleSupported()) {
            Toast.makeText(this, "BLE is not supported on this device", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!reactiveBeacons.isBluetoothEnabled()) {
            reactiveBeacons.requestBluetoothAccess(this);
            return false;
        } else if (!reactiveBeacons.isLocationEnabled(this)) {
            reactiveBeacons.requestLocationAccess(this);
            return false;
        } else if (!isFineOrCoarseLocationPermissionGranted() && IS_AT_LEAST_ANDROID_M) {
            requestCoarseLocationPermission();
            return false;
        }

        return true;
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (subscription != null && !subscription.isDisposed()) {
            subscription.dispose();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @android.support.annotation.NonNull String[] permissions,
                                           @android.support.annotation.NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        final boolean isCoarseLocation = requestCode == PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION;
        final boolean permissionGranted = grantResults[0] == PERMISSION_GRANTED;

        if (isCoarseLocation && permissionGranted && subscription == null) {
            Intent i = new Intent(this, BackgroundService.class);
            this.startService(i);
        }
    }

    private void requestCoarseLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{ACCESS_COARSE_LOCATION},
                    PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION);
        }
    }

    private boolean isFineOrCoarseLocationPermissionGranted() {
        boolean isAndroidMOrHigher = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
        boolean isFineLocationPermissionGranted = isGranted(ACCESS_FINE_LOCATION);
        boolean isCoarseLocationPermissionGranted = isGranted(ACCESS_COARSE_LOCATION);

        return isAndroidMOrHigher && (isFineLocationPermissionGranted
                || isCoarseLocationPermissionGranted);
    }

    private boolean isGranted(String permission) {
        return ActivityCompat.checkSelfPermission(this, permission) == PERMISSION_GRANTED;
    }
}

