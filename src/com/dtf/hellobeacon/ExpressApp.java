package com.dtf.hellobeacon;

import java.util.HashMap;
import java.util.List;

import com.dtf.hellobeacon.model.Visit;
import com.dtf.hellobeacon.util.RangingTask;
import com.estimote.sdk.BeaconManager;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

public class ExpressApp extends Application {

	BeaconManager beaconManager;
	Context context;
	
	RangingTask rTask;
	
	//TODO - use as singleton later when service has been implemented
	//public static HashMap<String, List<Visit>> listOfVisits = new HashMap<String, List<Visit>>();
	
	@Override
	public void onCreate() {
		super.onCreate();
		
	}
	
}
