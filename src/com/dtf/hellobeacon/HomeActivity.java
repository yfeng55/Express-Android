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
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dtf.hellobeacon.model.Gym;
import com.dtf.hellobeacon.util.DateUtil;
import com.dtf.hellobeacon.util.GraphClickListener;
import com.dtf.hellobeacon.util.MoniteringTask;
import com.dtf.hellobeacon.util.RangingTask;
import com.dtf.hellobeacon.views.ProgressWheel;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.example.hellobeacon.R;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class HomeActivity extends Activity implements GraphClickListener {
	
	private static final int REQUEST_ENABLE_BT = 0;
	private static final Region ALL_ESTIMOTE_BEACONS_REGION = new Region("rid", null, null, null);
	RangingTask rTask;
	MoniteringTask mTask;
	
	private SharedPreferences prefs;
	
	protected TextView gymName;
	protected TextView openCloseTime;
	protected TextView currentCapacity;
	protected ProgressWheel pw;
	
	protected Gym gymData;
	protected String gym;
	protected String gymAddress;
	protected String gymPhoneNumber;
	protected String gymCity;
	protected String gymState;
	protected int openHour;
	protected int closeHour;
	protected int capacity;
	protected int capacitypercent;
	
	boolean running;
	int progress = 0;
	
	int currentVisitorCount = 0;
	
	Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_home);
		
		
		//get authtoken value from sharedPreferences
		prefs = this.getSharedPreferences("com.dtf.hellobeacon", 0);
		gym = prefs.getString("gym", "No Gym Selected");
		gymAddress = prefs.getString("gymaddress", "dummy address");
		gymCity = prefs.getString("gymcity", "dummy city");
		gymState = prefs.getString("gymstate", "dummy state");
		gymPhoneNumber = prefs.getString("gymphone", "555-555-5555");
		openHour = prefs.getInt("gymopenhour", 6);
		closeHour = prefs.getInt("gymclosehour", 22);
		capacity = prefs.getInt("gymcapacity", 100);

		gymData = new Gym.GymBuilder()
		.buildName(gym)
		.buildContactInfo(gymAddress, gymCity, gymState, gymPhoneNumber)
		.buildHours(openHour, closeHour)
		.build();
		
		retrieveFirebaseValues();
		
		setTextViews();
		
		
		//ranging
		//ExpressApp.beaconManager = new BeaconManager(this);
		
		context = this;
		
		//rTask = new RangingTask(context, ExpressApp.beaconManager);
		//rTask.execute(ExpressApp.beaconManager);
		
		mTask = new MoniteringTask(context, ExpressApp.beaconManager);
		mTask.execute(ExpressApp.beaconManager);
		
	}
	
	public void setTextViews() {
		
		gymName = (TextView)findViewById(R.id.gymNameText);
		openCloseTime = (TextView)findViewById(R.id.openCloseTimeText);
//		currentCapacity = (TextView)findViewById(R.id.currentCapacity);
		
		Log.d("name", "gym data name is " + gymData.getName() + " " + gym);
		gymName.setText(gymData.getName());
		openCloseTime.setText(DateUtil.convertHourToTime(openHour, 0) + " to " + DateUtil.convertHourToTime(closeHour, 0));
		
//		capacitypercent = currentVisitorCount/capacity;
//		currentCapacity.setText(String.valueOf(capacitypercent) + "%");
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.viewPersonalStats:
			Intent i = new Intent(this, MyStatsActivity.class);
			startActivity(i);
			return true;
		case R.id.help:
			Intent k = new Intent(this, HelpActivity.class);
			startActivity(k);
			return true;
		case R.id.about:
			Intent j = new Intent(this, AboutActivity.class);
			startActivity(j);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	
	/**
	 * On Destroy LifeCycle Method - RangingTask is cancelled so ranginglistener stops running
	 * and will reset to correct values when application reopened
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		//rTask.setHasEntered(false);
		//rTask.cancel(true);
		//mTask.setHasEntered(false);
		mTask.cancel(true);
		Log.d("destroyed", "destroyedddd");
		ExpressApp.beaconManager.disconnect();
	}
	

	@Override
	protected void onStart() {
		super.onStart();

		// Check if device supports Bluetooth Low Energy.
		if (!ExpressApp.beaconManager.hasBluetooth()) {
			Toast.makeText(this, "Device does not have Bluetooth Low Energy", Toast.LENGTH_LONG).show();
			return;
		}

		// If Bluetooth is not enabled, let user enable it.
		if (!ExpressApp.beaconManager.isBluetoothEnabled()) {
			Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
		} else {
			connectToService();
		}
	}


	/**
	 * connect to beacon service for ranging
	 */
	private void connectToService() {
		getActionBar().setSubtitle("Scanning...");
		ExpressApp.beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
			@Override
			public void onServiceReady() {
				/*
				try {
					ExpressApp.beaconManager.startRanging(ALL_ESTIMOTE_BEACONS_REGION);
				} catch (RemoteException e) {
					Toast.makeText(HomeActivity.this, "Cannot start ranging, something terrible happened",
							Toast.LENGTH_LONG).show();
					Log.d("fail", "Cannot start ranging", e);
				}
				*/
				
				try {
					ExpressApp.beaconManager.startMonitoring(ALL_ESTIMOTE_BEACONS_REGION);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * TODO - document this later, from estimote SDK - don't quite understand
	 */
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

	
	protected void retrieveFirebaseValues() {
		

		String gymWithoutSpaces = gym.replaceAll("\\s+","");
		Firebase visitsref = new Firebase("https://hellobeacon.firebaseio.com/Gyms/" +gymWithoutSpaces + "/Visits");
		visitsref.addValueEventListener(new ValueEventListener() {
			
			@Override
			public void onDataChange(DataSnapshot snapshot) {	
				Log.d("retrieveFirbaseVals", "child count is " + snapshot.getChildrenCount());
				currentVisitorCount = (int)snapshot.getChildrenCount();
				
				capacitypercent = (int)((float)currentVisitorCount / (float)capacity * 100);
				if(capacitypercent > 100){
					capacitypercent = 100;
				}
				
				
								

				//draw capacity circle
				pw = (ProgressWheel) findViewById(R.id.capacity_circle);
				final Runnable r = new Runnable() {
					public void run() {
						running = true;
						
						//adjust capacity percent to be out of 120 (because there are 120 increments)
						while(progress < capacitypercent * 1.2) {
							pw.incrementProgress();
							pw.setText(Integer.toString((int)(progress/1.2)+1) + "%", "capacity");
							progress++;
							try {
								Thread.sleep(15);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						running = false;
					}
		        };
				
		        Thread s = new Thread(r);
				s.start();
			}
			@Override
			public void onCancelled(FirebaseError arg0) {
				// TODO Auto-generated method stub
				}
			});	
		
	}
	
	/**
	 * GraphClickListener method for graph - goes to traffic activity on click
	 * TODO - figure out how to do this efficiently, have to back out of traffic 
	 * activity 3 times to get back to home when graph is shown in this activity
	 */
	@Override
	public void onMiniGraphClick() {
		Intent i = new Intent(this, TrafficActivity.class);
		startActivity(i);
	}
	
	public void toTrafficActivity(View v) {
		Intent i = new Intent(this, TrafficActivity.class);
		startActivity(i);
	}
	public void toClassPage(View v) {
		Intent i = new Intent(this, ClassScheduleActivity.class);
		startActivity(i);
	}
	
	public void toViewPagerTest(View v) {
		Intent i = new Intent(this, ViewPagerTestActivity.class);
		startActivity(i);
	}
}
