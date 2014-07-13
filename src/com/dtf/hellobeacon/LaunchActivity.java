package com.dtf.hellobeacon;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.example.hellobeacon.R;


public class LaunchActivity extends Activity{

	private SharedPreferences prefs;
	private Boolean authtoken;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//get authtoken value from sharedPreferences
		prefs = this.getSharedPreferences("com.dtf.hellobeacon", 0);
		authtoken = prefs.getBoolean("authtoken", false);

		if(authtoken == false){
			Log.i("LaunchActivity", "not logged in - the authtoken is false");
			
			//if not registered, then start the Register activity
			Intent i = new Intent(this, RegisterActivity.class);
			startActivity(i);

		}else{
			Log.i("LaunchActivity", "logged in - the authtoken is true");

			//if registered, then start the Traffic activity
			Intent i = new Intent(this, TrafficActivity.class);
			startActivity(i);
		}

	}
	
	@Override
	protected void onPause() {
		super.onPause();
		finish();
	}


}
