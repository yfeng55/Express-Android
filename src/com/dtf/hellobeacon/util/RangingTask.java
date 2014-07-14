package com.dtf.hellobeacon.util;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;

public class RangingTask extends AsyncTask<BeaconManager, Void, Void> {

	protected final String beaconName = "estimote";
	
	protected Context context;
	protected BeaconManager bMan;


	public RangingTask(Context context, BeaconManager bMan)
	{
		this.context = context;
		this.bMan = bMan;
	}

	@Override
	protected Void doInBackground(BeaconManager... beaconManager) {
		int count = beaconManager.length;
		BeaconManager b;
		return null;
	}

	@Override
	protected void onPreExecute() {

		Activity refToActivity = (Activity)context;

		bMan.setRangingListener(new BeaconManager.RangingListener() {



			@Override
			public void onBeaconsDiscovered(Region region, final List<Beacon> rangedBeacons) {
				// Note that results are not delivered on UI thread.
				// Just in case if there are multiple beacons with the same uuid, major, minor.
				//refToActivity.runOnUiThread(new Runnable() {
				//@Override
				//public void run() {

				for (Beacon rangedBeacon : rangedBeacons) {
					Log.d("tracking", "tracking beacons mang");
					Log.d("tracking", "beaconName is " + rangedBeacon.getName());
					if(rangedBeacon.getName().equals(beaconName))
					{
						
					}


				}
			}
		});		
	}



	public interface RangingTaskListener{
		void onBeaconTracked();
	}
}