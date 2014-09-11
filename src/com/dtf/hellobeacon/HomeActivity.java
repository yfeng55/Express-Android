package com.dtf.hellobeacon;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;

public class HomeActivity extends Activity {
	
	private SharedPreferences prefs;
	private ProgressBar spinner;
	
	protected Gym gymData;
	protected String gym;
	protected String gymAddress;
	protected String gymPhoneNumber;
	protected String gymCity;
	protected String gymState;
	protected int openHour;
	protected int closeHour;
	protected int capacity;
	//
	private String firstname;
	private String lastname;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//get authtoken value from sharedPreferences
		prefs = this.getSharedPreferences("com.dtf.hellobeacon", 0);
		prefs = this.getSharedPreferences("com.dtf.hellobeacon", 0);
		gym = prefs.getString("gym", "No Gym Selected");
		gymAddress = prefs.getString("gymaddress", "dummy address");
		gymCity = prefs.getString("gymcity", "dummy city");
		gymState = prefs.getString("gymstate", "dummy state");
		gymPhoneNumber = prefs.getString("gymphone", "555-555-5555");
		openHour = prefs.getInt("gymopenhour", 0);
		closeHour = prefs.getInt("gymclosehour", 24);
		capacity = prefs.getInt("gymcapacity", 300);

	}
}
