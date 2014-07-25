package com.dtf.hellobeacon.util;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.example.hellobeacon.R;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class RangingTask extends AsyncTask<BeaconManager, Void, Void> {

	protected final String beaconName = "estimote";
	
	protected Context mContext;
	protected BeaconManager bMan;
	protected SharedPreferences prefs;
	private Boolean hasEntered = false;
	private Boolean hasRetrievedVisits = false;
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

						addVisitToUser();


				}
			}
		});		
	}
	

	/**
	 * increment visit value to firebase if 
	 * 		- visit value has been retrieved from Database AND IF
	 * 		- user has not already entered the gym 
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

			Firebase newpushref = new Firebase("https://hellobeacon.firebaseio.com/" + firstname + lastname + "/visits");

			//get current visit value
			newpushref.addValueEventListener(new ValueEventListener() {
				@Override
				public void onDataChange(DataSnapshot snapshot) {
					if(snapshot.getValue() != null){
						visits = (Long) snapshot.getValue();
						hasRetrievedVisits = true;
						}
					}
					
				
				@Override
				public void onCancelled(FirebaseError error) {
					System.err.println("Listener was cancelled");
				}
				
				
			});
			if(hasRetrievedVisits && !hasEntered)
			{
				newpushref.setValue(visits + 1);
				hasEntered = true;
				
				//show toast message that says "checked in to gymname"
				String gymname = prefs.getString("gym", "No Gym Selected");
				Toast.makeText(mContext, "Checked In to " + gymname, Toast.LENGTH_LONG).show();
				
				//play sound effect
				if(soundeffect != 0){
					sp.play(soundeffect, 1, 1, 0, 0, 1);
				}
					
			}
		}

	}
	
	public void setHasEnteredAndHasRetrieved(boolean bool){
		hasEntered = bool;
		hasRetrievedVisits = bool;
	}


	public interface RangingTaskListener{
		void onBeaconTracked();
	}
}