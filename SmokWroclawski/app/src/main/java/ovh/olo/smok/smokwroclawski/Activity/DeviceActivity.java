package ovh.olo.smok.smokwroclawski.Activity;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ovh.olo.smok.smokwroclawski.R;

/**
 * This Activity class is responsible for listing ble devices and connecting automatically
 * to devices which name is SMOK
 *
 * @author Michal Popek
 * @author Jakub Obacz
 */

public class DeviceActivity extends Activity implements OnItemClickListener {

	private ArrayList<BluetoothDevice> devices;
	private List<Map<String, String>> listItems = new ArrayList<Map<String, String>>();
	private SimpleAdapter adapter;
	private Map<String, String> map = null;
	private ListView listView;
	private String DEVICE_NAME = "name";
	private String DEVICE_ADDRESS = "address";
	public final static String EXTRA_DEVICE_ADDRESS = "EXTRA_DEVICE_ADDRESS";
	public final static String EXTRA_DEVICE_NAME = "EXTRA_DEVICE_NAME";

	private final static String DEFAULT_DEVICE_NAME = "SMOK";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.device_list);

		setTitle("Devices");

//		listView = (ListView) findViewById(R.id.devicesListView);

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
		}

//		adapter = new SimpleAdapter(getApplicationContext(), listItems,
//				R.layout.list_item, new String[] { "name", "address" },
//				new int[] { R.id.deviceName, R.id.deviceAddr });
//		listView.setAdapter(adapter);
//		listView.setOnItemClickListener(this);

		//todo: kolejka polaczen
		for (Map<String,String> map: listItems
			 ) {
			String addr = map.get(DEVICE_ADDRESS);
			String name = map.get(DEVICE_NAME);
			if(name == null || addr == null) continue;
			if(name.equals(DEFAULT_DEVICE_NAME)
					&& !MainActivity.instance.tabuList.contains(DEFAULT_DEVICE_NAME)) {
				MainActivity.instance.tabuList.add(DEFAULT_DEVICE_NAME);
				connect(addr, name);

			}
		}

		Toast.makeText(MainActivity.instance, "Nie znaleziono więcej urządzeń!", Toast.LENGTH_LONG).show();
		this.finish();

	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view,
							int position, long id) {
		HashMap<String, String> hashMap = (HashMap<String, String>) listItems
				.get(position);
		String addr = hashMap.get(DEVICE_ADDRESS);
		String name = hashMap.get(DEVICE_NAME);
		connect(addr, name);

		finish();
	}

	private void connect(String addr, String name) {
		Intent intent = new Intent(DeviceActivity.this, ChatActivity.class);
		intent.putExtra(EXTRA_DEVICE_ADDRESS, addr);
		intent.putExtra(EXTRA_DEVICE_NAME, name);
		startActivity(intent);
		this.finish();
	}

}
