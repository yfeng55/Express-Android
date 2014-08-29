package com.dtf.hellobeacon;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.hellobeacon.R;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.GenericTypeIndicator;
import com.firebase.client.ValueEventListener;


public class MyStatsActivity extends Activity{
	
	private SharedPreferences prefs;
	private String firstname;
	private String lastname;
	private TextView myname;
	List<Long> visits;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mystats);
		
		//set person's name
		prefs = this.getSharedPreferences("com.dtf.hellobeacon", 0);
		firstname = prefs.getString("firstName", "No First Name");
		lastname = prefs.getString("lastName", "No Last Name");
		
		myname = (TextView) findViewById(R.id.tv_MyName);
		myname.setText(firstname + " " + lastname);
		
		visits = new ArrayList<Long>();
		
		
		//get the user's visits from firebase
		
		Firebase visitsref = new Firebase("https://hellobeacon.firebaseio.com/Users/" + firstname + lastname + "/Visits");
		
		visitsref.addValueEventListener(new ValueEventListener() {
			
			@Override
			public void onDataChange(DataSnapshot snapshot) {
				//GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<List<String>>() {};
			    //List<String> messages = snapshot.getValue(t);
			}

			@Override
			public void onCancelled(FirebaseError error) {
				System.err.println("Listener was cancelled");
			}

		});
		
		
		//display all of the user's visits on the screen
//		for(long v : visits){
//			Log.i("EEE", Long.toString(v));
//		}
//		
		
	}
	
}





