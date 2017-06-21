package ovh.olo.smok.smokwroclawski.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ovh.olo.smok.smokwroclawski.Manager.BluetoothController;
import ovh.olo.smok.smokwroclawski.Validator.InternetValidator;
import ovh.olo.smok.smokwroclawski.Object.SensorConfigData;
import ovh.olo.smok.smokwroclawski.R;
import ovh.olo.smok.smokwroclawski.Service.ChatService;
import ovh.olo.smok.smokwroclawski.ThingSpeak.ThingSpeakReceiver;

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

	private ProgressDialog progressDialog;

	public static DeviceActivity instance;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setTitle("Devices");

		instance = DeviceActivity.this;

		registerReceiver(bReceiver, new IntentFilter("message"));

		devices = (ArrayList<BluetoothDevice>) BluetoothController.mDevices;


		if (devices.isEmpty()) {
			Toast.makeText(getApplicationContext(), R.string.no_devices_found, Toast.LENGTH_SHORT).show();
			runAndDestory();
			return;
		}

		for (BluetoothDevice device : devices) {
			map = new HashMap<>();
			map.put(DEVICE_NAME, device.getName());
			map.put(DEVICE_ADDRESS, device.getAddress());
			listItems.add(map);
			if (device.getName() == null) continue;
			if (device.getName().equals(DEFAULT_DEVICE_NAME)) sensorDevicesCount++;
		}

		if(sensorDevicesCount == 0) {
			runAndDestory();
			return;
		}

		devicesCount = listItems.size();
		Log.i("Devices count: ", listItems.size() + "");

		progressDialog = ProgressDialog.show(MainActivity.instance, "", "Processing...", true);
		MainActivity.instance.tabuList.clear();
		connectNext();
	}

	public ProgressDialog getProgressDialog() {
		return progressDialog;
	}

	private void connectNext() {
		System.out.println("Device: " + counter + "/" + devicesCount);
        if(counter >= devicesCount) {
			runAndDestory();
			return;
		}
		Map<String, String> map = listItems.get(counter);
		String addr = map.get(DEVICE_ADDRESS);
		String name = map.get(DEVICE_NAME);
		counter++;

        Log.i("ConnectNext", "Addr: " + addr + " Name: " + name);

		if(name == null || addr == null) {
			connectNext();
			return;
		}
		if (name.equals(DEFAULT_DEVICE_NAME)
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
			System.out.println(counter + "|" + devicesCount);
			System.out.println(sensorCounter + "|" + sensorDevicesCount);
			if(sensorCounter < sensorDevicesCount) {
				connectNext();
			} else {
				Toast.makeText(DeviceActivity.this, "No more devices!", Toast.LENGTH_LONG).show();
				runAndDestory();
			}
		}
	};

	public volatile Boolean done = false;

	private void runAndDestory() {
		if(!InternetValidator.isOnline()) {
			if(progressDialog != null)
				progressDialog.dismiss();
			this.finish();
            return;
		}

		if(progressDialog == null || !progressDialog.isShowing()) {
			progressDialog = ProgressDialog.show(MainActivity.instance, "", "Processing...", true);
		}

		progressDialog.setMessage("Receiving and analysing data...");


		new Handler().post(new Runnable() {
			@Override
			public void run() {
				for (SensorConfigData scd : MainActivity.instance.getSensorConfigDatas()) {
					ThingSpeakReceiver receiver = new ThingSpeakReceiver(
							Integer.parseInt(scd.getChannelId()),
							scd.getReadAPIkey()
					);
					receiver.run();
					while (true) {
						if (done) {
							break;
						}
					}

					done = false;
				}

				DeviceActivity.instance.finish();
			}
		});



//		this.finish();
	}

	@Override
	protected void onDestroy() {
		try {
			unregisterReceiver(bReceiver);
		} catch (Exception ignored) {}

		super.onDestroy();
	}
}

