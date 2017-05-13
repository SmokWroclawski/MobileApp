package ovh.olo.smok.smokwroclawski.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import android.support.v4.app.FragmentActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import ovh.olo.smok.smokwroclawski.Adapter.DrawerListAdapter;
import ovh.olo.smok.smokwroclawski.Maps.MarkerBuilder;
import ovh.olo.smok.smokwroclawski.Object.NavItem;
import ovh.olo.smok.smokwroclawski.R;
import ovh.olo.smok.smokwroclawski.Worker.FileWorker;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    private BluetoothAdapter mBluetoothAdapter;
    private static final int REQUEST_ENABLE_BT = 1;
    private static final int REQUEST_ENABLE_GPS = 2;
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    private static final int PERMISSION_REQUEST_EXTERNAL_STORAGE = 2;
    private static final long SCAN_PERIOD = 3000;

    public static final String API_KEY_NAME = "API_KEY";
    public static final String CHANNEL_ID_NAME = "CHANNEL_ID";
    public static final String SENSOR_NAME = "SENSOR";

    public List<Marker> markers = new ArrayList<>();
    public List<Polyline> polylines = new ArrayList<>();

    private Dialog mDialog;
    public static List<BluetoothDevice> mDevices = new ArrayList<BluetoothDevice>();
    public static MainActivity instance = null;
    public List<String> tabuList = new ArrayList<>();

    private boolean coarseLocFlag = false;
    private boolean storageFlag = false;

    private GoogleMap mMap;
    private Location myLocation;
    private GoogleApiClient googleApiClient;
    private Intent locationSettingsIntent;

    private ListView mDrawerList;
    private RelativeLayout mDrawerPane;
    private DrawerLayout mDrawerLayout;
    private DrawerListAdapter adapter;
    public ArrayList<NavItem> mNavItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_maps);

        Thread.setDefaultUncaughtExceptionHandler (new Thread.UncaughtExceptionHandler()
        {
            @Override
            public void uncaughtException (Thread thread, Throwable e)
            {
                try {
                    handleUncaughtException (thread, e);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        getLayoutItems();

        preStartForLocation();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        instance = this;

        // DrawerLayout
        adapter = new DrawerListAdapter(this, mNavItems);
        adapter.setmSelectedItem(5);
        mDrawerList.setAdapter(adapter);
        addMenuItems();
        setListeners();
    }

    private void handleUncaughtException (Thread thread, Throwable e) throws IOException {
        e.printStackTrace(); // not all Android versions will print the stack trace automatically
        new FileWorker().appendToFile("SMOK_LOG.txt", "Message: " + e.getMessage() + "\n\n");
        new FileWorker().appendToFile("SMOK_LOG.txt", "LocalizedMessage: " + e.getLocalizedMessage() + "\n\n");
        new FileWorker().appendToFile("SMOK_LOG.txt", "Cause: " + e.getCause() + "\n\n");
        new FileWorker().appendToFile("SMOK_LOG.txt", "StackTrace: " + Arrays.toString(e.getStackTrace()) + "\n\n");
        new FileWorker().appendToFile("SMOK_LOG.txt", "Suppressed: " + Arrays.toString(e.getSuppressed()) + "\n\n");
        System.exit(1); // kill off the crashed app
    }

    private void checkPermissionsAndStartSearching() {
        if (!getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT)
                    .show();
            finish();
        }

        final BluetoothManager mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT)
                    .show();
            finish();
            return;
        }

        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSION_REQUEST_COARSE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_EXTERNAL_STORAGE);
        }
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Location Services Not Active");
            builder.setMessage("Please enable Location Services and GPS!");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    // Show location settings when the user acknowledges the alert dialog
                    locationSettingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivityForResult(locationSettingsIntent, REQUEST_ENABLE_GPS);
//
                }
            });
            Dialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
        }

        if ( manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) &&
                mBluetoothAdapter.isEnabled() &&

                ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
            findDevices();
        }
    }

    private void restartActivity() {
        Intent intent = MainActivity.instance.getIntent();
        MainActivity.instance.finish();
        startActivity(intent);
    }

    private void setListeners() {
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItemFromDrawer(position);
            }
        });
    }

    private void setSelectedItemId(int position) {
        adapter.setmSelectedItem(position);
        adapter.notifyDataSetChanged();
    }

    private void selectItemFromDrawer(int position) {
        setSelectedItemId(position);
        //todo: switch case zalezny od kliknietego itemu w menu

        if (position != -1) {
            mDrawerList.setItemChecked(position, true);
//            instance.setTitle(instance.mNavItems.get(position).mTitle);
        }
        // Close the drawer
        mDrawerLayout.closeDrawer(mDrawerPane);
    }

    private void addMenuItems() {
        mNavItems.add(new NavItem("AAA", "aaaaaa", R.mipmap.ic_launcher));
        mNavItems.add(new NavItem("BBB", "bbbbbb", R.mipmap.ic_launcher));
        mNavItems.add(new NavItem("CCC", "cccccc", R.mipmap.ic_launcher));
        mNavItems.add(new NavItem("DDD", "dddddd", R.mipmap.ic_launcher));
        mNavItems.add(new NavItem("EEE", "eeeeee", R.mipmap.ic_launcher));
        mNavItems.add(new NavItem("FFF", "ffffff", R.mipmap.ic_launcher));
    }

    private void getLayoutItems() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mDrawerPane = (RelativeLayout) findViewById(R.id.drawerPane);
        mDrawerList = (ListView) findViewById(R.id.navList);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay!
                    coarseLocFlag = true;
                    startFindDevices(coarseLocFlag, storageFlag);
                } else {


                    if (Build.VERSION.SDK_INT >= 23) {
                        this.finishAffinity();
                    }
                    // permission denied, close app immediately - android 6.0 ble no location permissions bug
                }
                return;
            }

            case PERMISSION_REQUEST_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay!

                    storageFlag = true;
                    startFindDevices(coarseLocFlag, storageFlag);
                } else {

                    if (isAndroidApi(23)) {
                        this.finishAffinity();
                    }
                    //permission denied
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private boolean isAndroidApi(int apiLevel) {
        return Build.VERSION.SDK_INT >= apiLevel;
    }

    private void startFindDevices(boolean coarseLocationFlag, boolean storageFlag) {
        if (!isAndroidApi(23)) {
            coarseLocationFlag = true;
            storageFlag = true;
        }
        if (coarseLocationFlag && storageFlag) {
            findDevices();
        }
    }

    private void findDevices() {
        scanLeDevice();

        showRoundProcessDialog(MainActivity.this, R.layout.loading_process_dialog_anim);

        Timer mTimer = new Timer();
        mTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                Intent deviceListIntent = new Intent(getApplicationContext(),
                        DeviceActivity.class);
                startActivity(deviceListIntent);
                mDialog.dismiss();
            }


        }, SCAN_PERIOD);
    }

    public void showRoundProcessDialog(Context mContext, int layout) {
        DialogInterface.OnKeyListener keyListener = new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_HOME
                        || keyCode == KeyEvent.KEYCODE_SEARCH) {
                    return true;
                }
                return false;
            }
        };

        mDialog = new android.app.AlertDialog.Builder(mContext).create();
        mDialog.setOnKeyListener(keyListener);
        mDialog.setCancelable(false);
        mDialog.show();
        mDialog.setContentView(layout);
    }

    private void scanLeDevice() {
        new Thread() {

            @Override
            public void run() {
                mBluetoothAdapter.startLeScan(mLeScanCallback);

                try {
                    Thread.sleep(SCAN_PERIOD);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                mBluetoothAdapter.stopLeScan(mLeScanCallback);
            }
        }.start();
    }

    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi,
                             byte[] scanRecord) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (device == null) return;
                    if (mDevices.indexOf(device) == -1)
                        mDevices.add(device);
                }
            });
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // User choose not to enable Bluetooth.
        if (requestCode == REQUEST_ENABLE_BT
                && resultCode == Activity.RESULT_CANCELED) {
            finish();
            return;
        }
        if(requestCode == REQUEST_ENABLE_GPS) {
            //
        }

        startFindDevices(coarseLocFlag, storageFlag);
//        findDevices();
        super.onActivityResult(requestCode, resultCode, data);
    }

    public GoogleMap getmMap() {
        return mMap;
    }

    public List<Marker> getMarkers() {
        return markers;
    }

    public List<Polyline> getPolylines() {
        return polylines;
    }

    private void preStartForLocation() {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

    }

    private void setMarkerByMyLocation(String title) {
        myLocation = LocationServices.FusedLocationApi.getLastLocation(
                googleApiClient);

        LatLng latLng = new LatLng(51.0902923, 17.0141853);
        LatLng latLng2 = new LatLng(51.0913923, 17.0143853);
        LatLng latLng3 = new LatLng(51.0911923, 17.0144853);
        LatLng latLng4 = new LatLng(51.0912923, 17.0145853);
        LatLng latLng5 = new LatLng(51.0913923, 17.0142853);
        if (myLocation != null) {
            System.out.println(String.valueOf(myLocation.getLatitude()));
            System.out.println(String.valueOf(myLocation.getLongitude()));
            latLng = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
        }

        MarkerBuilder markerBuilder = new MarkerBuilder();

        markerBuilder.add(latLng, title);
        markerBuilder.add(latLng2, title + "2");

        markerBuilder.addLine(latLng, latLng2, Color.BLUE);

        markerBuilder.addCurveLine(Arrays.asList(latLng, latLng3, latLng4, latLng5));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(mDrawerPane)) {
            mDrawerLayout.closeDrawer(mDrawerPane);
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

//        System.exit(0);
    }

    @Override
    protected void onStart() {
        googleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setMapToolbarEnabled(false);

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Intent intent = new Intent(MainActivity.this, SensorActivity.class);
                intent.putExtra(CHANNEL_ID_NAME, 252240 + "");
                intent.putExtra(API_KEY_NAME, "OJZ2HLXTJ2Y75OVN");
                intent.putExtra(SENSOR_NAME, marker.getTitle());
                startActivity(intent);
                return false;
            }
        });

        mMap.setOnPolylineClickListener(new GoogleMap.OnPolylineClickListener() {
            @Override
            public void onPolylineClick(Polyline polyline) {
                Log.i(getClass().toString(), "Polyline clicked!");
            }
        });


        checkPermissionsAndStartSearching();



    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
//
//        myLocation = LocationServices.FusedLocationApi.getLastLocation(
//                googleApiClient);

        setMarkerByMyLocation("Czujnik Wroclaw");

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
//        if (location.hasAccuracy()) {
//            if (location.getAccuracy() < 30) {
//                LatLng wroclaw = new LatLng(location.getLongitude(), location.getLatitude());
//                new MarkerBuilder().add(wroclaw.longitude, wroclaw.latitude, "Czujnik Wroclaw");
//                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(wroclaw, 15));
//            }
//        }
    }
}

