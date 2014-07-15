package com.dtf.hellobeacon;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.dtf.hellobeacon.util.RangingTask;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.example.hellobeacon.R;


public class TrafficActivity extends Activity{

	private static final int REQUEST_ENABLE_BT = 0;
	private static final Region ALL_ESTIMOTE_BEACONS_REGION = new Region("rid", null, null, null);

	private SharedPreferences prefs;
	private String gym;
	private TextView gymname;

	BeaconManager beaconManager;
	Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.traffic);

		//set gym name
		prefs = this.getSharedPreferences("com.dtf.hellobeacon", 0);
		gym = prefs.getString("gym", "No Gym Selected");
		gymname = (TextView) findViewById(R.id.tv_GymName);
		gymname.setText(gym);

		//ranging
		beaconManager = new BeaconManager(this);
		context = this;
		RangingTask rTask = new RangingTask(context, beaconManager);
		rTask.execute(beaconManager);

	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private Context getContext() {
		return null;
	}

	@Override
	protected void onDestroy() {
		beaconManager.disconnect();
		super.onDestroy();
	}

	@Override
	protected void onStart() {
		super.onStart();

		// Check if device supports Bluetooth Low Energy.
		if (!beaconManager.hasBluetooth()) {
			Toast.makeText(this, "Device does not have Bluetooth Low Energy", Toast.LENGTH_LONG).show();
			return;
		}

		// If Bluetooth is not enabled, let user enable it.
		if (!beaconManager.isBluetoothEnabled()) {
			Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
		} else {
			connectToService();
		}
	}

	private void connectToService() {
		getActionBar().setSubtitle("Scanning...");
		beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
			@Override
			public void onServiceReady() {
				try {
					beaconManager.startRanging(ALL_ESTIMOTE_BEACONS_REGION);
				} catch (RemoteException e) {
					Toast.makeText(TrafficActivity.this, "Cannot start ranging, something terrible happened",
							Toast.LENGTH_LONG).show();
					Log.d("fail", "Cannot start ranging", e);
				}
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_ENABLE_BT) {
			if (resultCode == Activity.RESULT_OK) {
				connectToService();
			} else {
				Toast.makeText(this, "Bluetooth not enabled", Toast.LENGTH_LONG).show();
				getActionBar().setSubtitle("Bluetooth not enabled");
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}



}





