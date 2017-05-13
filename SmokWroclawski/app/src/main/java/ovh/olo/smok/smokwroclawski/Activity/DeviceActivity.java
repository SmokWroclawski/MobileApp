package ovh.olo.smok.smokwroclawski.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ovh.olo.smok.smokwroclawski.R;
import ovh.olo.smok.smokwroclawski.Service.ChatService;

public class DeviceActivity extends Activity {

	private ArrayList<BluetoothDevice> devices;
	private List<Map<String, String>> listItems = new ArrayList<Map<String, String>>();
	private Map<String, String> map = null;
	private String DEVICE_NAME = "name";
	private String DEVICE_ADDRESS = "address";
	public final static String EXTRA_DEVICE_ADDRESS = "EXTRA_DEVICE_ADDRESS";
	public final static String EXTRA_DEVICE_NAME = "EXTRA_DEVICE_NAME";

	private final static String DEFAULT_DEVICE_NAME = "SMOK";

	private volatile int counter = 0;
	private int sensorCounter = 0;

	private int devicesCount = 0;
	private int sensorDevicesCount = 0;

	private ProgressDialog progressDialog = ProgressDialog.show(MainActivity.instance, "", "Processing...",true);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setTitle("Devices");

		registerReceiver(bReceiver, new IntentFilter("message"));

		devices = (ArrayList<BluetoothDevice>) MainActivity.mDevices;

		if(devices.isEmpty()) {
			Toast.makeText(getApplicationContext(), R.string.no_devices_found, Toast.LENGTH_SHORT).show();
			this.finish();
		}

		for (BluetoothDevice device : devices) {
			map = new HashMap<>();
			map.put(DEVICE_NAME, device.getName());
			map.put(DEVICE_ADDRESS, device.getAddress());
			listItems.add(map);
			if(device.getName().contains(DEFAULT_DEVICE_NAME)) sensorDevicesCount++;
		}

		devicesCount = listItems.size();
		Log.i("Devices count: ", listItems.size() + "");

		if(devicesCount == 0) return;
		connectNext();

	}

	private void connectNext() {
        if(counter >= devicesCount) return;
		Map<String, String> map = listItems.get(counter);
		String addr = map.get(DEVICE_ADDRESS);
		String name = map.get(DEVICE_NAME);
		counter++;

        Log.i("ConnectNext", "Addr: " + addr + " Name: " + name);

		if(name == null || addr == null) connectNext();

		if(name.equals(DEFAULT_DEVICE_NAME)
				&& !MainActivity.instance.tabuList.contains(addr)) {
			MainActivity.instance.tabuList.add(addr);
			sensorCounter++;
			runOnUiThread(changeMessage);
			Log.i("Intent", "Addr: " + addr + " Name: " + name);
			startIntent(addr, name);
		} else {
            connectNext();
        }
	}

	private Runnable changeMessage = new Runnable() {
		@Override
		public void run() {
			progressDialog.setMessage("Data processing... " + sensorCounter + " of " + sensorDevicesCount);
		}
	};

	private void startIntent(String addr, String name) {
		Log.i("Device: ", "Address: " + addr + " Name:" + name);
		Intent intent = new Intent(DeviceActivity.this, ChatService.class);
		intent.putExtra(EXTRA_DEVICE_ADDRESS, addr);
		intent.putExtra(EXTRA_DEVICE_NAME, name);
		startService(intent);
}


	private BroadcastReceiver bReceiver = new BroadcastReceiver(){

		@Override
		public void onReceive(Context context, Intent intent) {
			System.out.println("OnReceive!");
			System.out.println(counter + "|" + devicesCount);
			if(counter < devicesCount) {
				connectNext();
			} else {
				Toast.makeText(DeviceActivity.this, "No more devices!", Toast.LENGTH_LONG).show();
				finish();
			}
		}
	};

	@Override
	protected void onDestroy() {
		System.out.println("Unregister!");
		unregisterReceiver(bReceiver);

		progressDialog.dismiss();

		super.onDestroy();
	}
}

