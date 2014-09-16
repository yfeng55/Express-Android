package com.dtf.hellobeacon.util;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.dtf.hellobeacon.model.Visit;
import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.Utils;
import com.example.hellobeacon.R;
import com.firebase.client.Firebase;
import com.firebase.client.ServerValue;

public class RangingTask extends AsyncTask<BeaconManager, Void, Void> {

	protected final String beaconName = "estimote";
	
	protected Context mContext;
	protected BeaconManager bMan;
	protected SharedPreferences prefs;
	private Boolean hasEntered = false;
	private SoundPool sp;
	private int soundeffect = 0;
	long visits;
	

	/**
	 * Constructor - context for getting reference to activity creating this RangingTask, and beaconmanager
	 * to set rangingListener for
	 * @param context
	 * @param bMan
	 */
	public RangingTask(Context context, BeaconManager bMan)
	{
		mContext = context;
		this.bMan = bMan;
		
		//use SoundPool for short sounds like button presses
		sp = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
		soundeffect = sp.load(mContext, R.raw.beep, 0);
	}

	@Override
	protected Void doInBackground(BeaconManager... beaconManager) {
		int count = beaconManager.length;
		BeaconManager b;
		return null;
	}

	/**
	 * Run before doInBackground - set ranging listener for beacon manager here, because
	 * preExecute is run on ui thread
	 */
	@Override
	protected void onPreExecute() {

		//Activity refToActivity = (Activity)mContext;

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

				    double distance = Utils.computeAccuracy(rangedBeacon);
				    Log.d("tracking", "distance = " + distance);
					if(distance < 100)
					{
						addVisitToUser();
					}
				}
			}
		});		
	}
	

	/**
	 * TODO - find better way of telling if user has entered and exited the gym
	 */
	public void addVisitToUser()
	{

		if(mContext != null)
		{
			Log.d("context", "tracking beacons mang - context not null");
			prefs = mContext.getSharedPreferences("com.dtf.hellobeacon", 0);
			String firstname = prefs.getString("firstName", "nobody");
			String lastname = prefs.getString("lastName", "nobody");
			
			//getting gym name and replacing all spaces in it for firebase/database purposes
			String gymname = prefs.getString("gym", "No Gym Selected");
			String gymnameWithoutSpaces = gymname.replaceAll("\\s","");
			
			boolean isInGym = prefs.getBoolean("is in Gym", false);
			
			if(!isInGym){
				Log.d("ranging task", "gym name w/o spaces - " + gymname);

				
			Log.d("current hour", "curr hour is " + DateUtil.getCurrentHour());
			Firebase visitsref = new Firebase("https://hellobeacon.firebaseio.com/Gyms/" + gymnameWithoutSpaces + "/Users/" + firstname + lastname + "/Visits/");
			
			Firebase gymVisitRef = new Firebase("https://hellobeacon.firebaseio.com/Gyms/" + gymnameWithoutSpaces + "/Visits/");
		
			
			Firebase newpushref = visitsref.push();
			Firebase gympushref = gymVisitRef.push();
			
			if(!hasEntered)
			{
				Log.d("pushing time stamp ", "Name - " + firstname + " " + lastname + " hr - " + DateUtil.getCurrentHour());
				//pushing a new visit to the server; the time value is the Server's TIMESTAMP attribute
				newpushref.setValue(ServerValue.TIMESTAMP);
				hasEntered = true;
				gympushref.setValue(ServerValue.TIMESTAMP);
				//show toast message that says "checked in to gymname"
				Toast.makeText(mContext, "Checked In to " + gymname, Toast.LENGTH_LONG).show();
				
				//play sound effect
				if(soundeffect != 0){
					sp.play(soundeffect, 1, 1, 0, 0, 1);
				}
					
				}
			}
		}

	}
	
	public void setHasEntered(boolean bool){
		hasEntered = bool;
	}


	public interface RangingTaskListener{
		void onBeaconTracked();
	}
}