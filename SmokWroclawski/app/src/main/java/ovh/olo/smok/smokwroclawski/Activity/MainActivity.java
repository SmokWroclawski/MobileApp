package ovh.olo.smok.smokwroclawski.Activity;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.ProgressBar;
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
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import ovh.olo.smok.smokwroclawski.Adapter.DrawerListAdapter;
import ovh.olo.smok.smokwroclawski.Adapter.MyInfoWindowAdapter;
import ovh.olo.smok.smokwroclawski.ConnectionRefresher;
import ovh.olo.smok.smokwroclawski.Github.GithubReader;
import ovh.olo.smok.smokwroclawski.InternetChecker;
import ovh.olo.smok.smokwroclawski.LocationGPSManager;
import ovh.olo.smok.smokwroclawski.Object.NavItem;
import ovh.olo.smok.smokwroclawski.Object.SensorConfigData;
import ovh.olo.smok.smokwroclawski.R;
import ovh.olo.smok.smokwroclawski.ThingSpeak.ChartData;
import ovh.olo.smok.smokwroclawski.ThingSpeak.ThingSpeakData;
import ovh.olo.smok.smokwroclawski.Worker.FileWorker;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    private BluetoothAdapter mBluetoothAdapter;
    private static final int REQUEST_ENABLE_BT = 1;

    public static final int REQUEST_CHECK_SETTINGS = 3;

    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    private static final int PERMISSION_REQUEST_EXTERNAL_STORAGE = 2;
    private static final long SCAN_PERIOD = 3000;

    public static final String SENSOR_NAME = "SENSOR";

    public List<Marker> markers = new ArrayList<>();
    public List<Polyline> polylines = new ArrayList<>();

    private Dialog mDialog;
    public static List<BluetoothDevice> mDevices = new ArrayList<BluetoothDevice>();
    public static MainActivity instance = null;
    public List<String> tabuList = new ArrayList<>();
    public List<SensorConfigData> sensorConfigDatas = new ArrayList<>();
    private ChartData data = null;

    private LocationManager manager;
    private GoogleMap mMap;
    private Location myLocation;
    private GoogleApiClient googleApiClient;
    private CameraPosition currCameraPosition;

    private ListView mDrawerList;
    private RelativeLayout mDrawerPane;
    private DrawerLayout mDrawerLayout;
    private DrawerListAdapter adapter;
    public ArrayList<NavItem> mNavItems = new ArrayList<>();

    private volatile boolean searchStarted = false;

    private CheckBox reconnectCheckBox;
    private ProgressBar sendProgressBar;
    private int progressBarStatus = 0;

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
        instance = MainActivity.this;

        data = new ChartData();
        sendProgressBar = (ProgressBar) findViewById(R.id.sendProgressBar);
        reconnectCheckBox = (CheckBox) findViewById(R.id.reconnectCheckBox);
        getLayoutItems();


        preStartForLocation();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

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

    public void updateProgressBar() {
        System.out.println("MAX: " + sendProgressBar.getMax());
        progressBarStatus++;
        Handler progressBarHandler = new Handler();
        if(progressBarStatus > 0 ) {
            sendProgressBar.setVisibility(View.VISIBLE);
            reconnectCheckBox.setEnabled(false);
        }
        if(progressBarStatus >= sendProgressBar.getMax() ) {
            sendProgressBar.setVisibility(View.INVISIBLE);
            sendProgressBar.setMax(0);
            progressBarStatus = 0;
            reconnectCheckBox.setEnabled(true);
        }
        progressBarHandler.post(new Runnable() {
            public void run() {
                ObjectAnimator animation = ObjectAnimator.ofInt(sendProgressBar, "progress", progressBarStatus);
                animation.setDuration(1000);
                animation.setInterpolator(new DecelerateInterpolator());
                animation.start();
//                sendProgressBar.setProgress(progressBarStatus);
            }
        });
    }

    public void setMaxProgressBar(int max) {
        sendProgressBar.setMax(max);
    }

    public void updateMaxProgressBar(int max) {
        sendProgressBar.setMax(sendProgressBar.getMax() + max);
    }

    public void checkPermissionsAndStartSearching() {
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
        manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            new LocationGPSManager(googleApiClient).requestGpsFromSettingsApi();
        }

        startFindDevices();
    }

    private void setListeners() {
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItemFromDrawer(position);
            }
        });

        reconnectCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            ConnectionRefresher connectionRefresher = new ConnectionRefresher();
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    connectionRefresher.start();
                    mDrawerLayout.closeDrawer(mDrawerPane);
                }
                else {
                    connectionRefresher.stop();
                }
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
        }

        switch (position) {
            case 0:
                Intent settingIntent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(settingIntent);
                break;
            default:
                break;
        }
        // Close the drawer
        mDrawerLayout.closeDrawer(mDrawerPane);
    }

    private void addMenuItems() {
        mNavItems.add(new NavItem("Settings", "Application settings", R.mipmap.ic_launcher));
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
                    if(!searchStarted) {
                        searchStarted = true;
                        startFindDevices();
                    }
                } else {

                    if (isAndroidApi(23)) {
                        this.finishAffinity();
                    }
                    // permission denied
                }
                return;
            }

            case PERMISSION_REQUEST_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay!

                    if(!searchStarted) {
                        searchStarted = true;
                        startFindDevices();
                    }
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

    private void startFindDevices() {
        if ( manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) &&
                mBluetoothAdapter.isEnabled()

                && ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED

                &&  ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED)
        {
            findDevices();
        }
    }

    public void findDevices() {
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

        if(requestCode == REQUEST_CHECK_SETTINGS || requestCode == REQUEST_ENABLE_BT) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    if(!searchStarted) {
                        searchStarted = true;
                        startFindDevices();
                    }
                    break;
                case Activity.RESULT_CANCELED:
                    this.finish();
                    break;
                default:
                    break;
            }
        }
//        startFindDevices(coarseLocFlag, storageFlag);

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

    public Location getMyLocation() {
        return myLocation;
    }

    public List<SensorConfigData> getSensorConfigDatas() {
        return sensorConfigDatas;
    }

    public void setSensorConfigDatas(List<SensorConfigData> sensorConfigDatas) {
        this.sensorConfigDatas = sensorConfigDatas;
    }

    public ChartData getData() {
        return data;
    }

    public void setData(ChartData data) {
        this.data = data;
    }

    public void addData(ChartData data) {
        List<ThingSpeakData> existingList = this.data.getThingSpeakDataList();
//        if(existingList == null) {
//            this.data = data;
//            return;
//        }
        List<ThingSpeakData> listToAdd = data.getThingSpeakDataList();
        List<ThingSpeakData> newList = new ArrayList<>(existingList);
        newList.addAll(listToAdd);
        this.data.setThingSpeakDataList(newList);
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

    private void setMyLocation() {
        myLocation = LocationServices.FusedLocationApi.getLastLocation(
                googleApiClient);
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
    protected void onResume() {
        super.onResume();
        if(mMap != null && currCameraPosition != null) {
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(currCameraPosition));
            currCameraPosition = null;
        }
    }

    @Override
    protected void onStart() {
        googleApiClient.connect();
        if(mMap != null && currCameraPosition != null) {
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(currCameraPosition));
            currCameraPosition = null;
        }
        super.onStart();
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        if(mMap != null) {
            currCameraPosition = mMap.getCameraPosition();
        }
        super.onStop();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setMapToolbarEnabled(false);


        mMap.setInfoWindowAdapter(new MyInfoWindowAdapter(MainActivity.this));
        if(InternetChecker.isOnline())
            new GithubReader().execute();
        else
            checkPermissionsAndStartSearching();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        setMyLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        myLocation = location;
        Log.i("LocationChanged!", location.getLongitude() + " " + location.getLatitude());
    }
}