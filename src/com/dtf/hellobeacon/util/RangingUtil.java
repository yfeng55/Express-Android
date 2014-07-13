package com.dtf.hellobeacon.util;
import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.app.Activity;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;

public class RangingUtil implements BeaconManager.RangingListener {

	@Override
	public void onBeaconsDiscovered(Region arg0, List<Beacon> arg1) {
		// TODO Auto-generated method stub
		
	}
	
}
