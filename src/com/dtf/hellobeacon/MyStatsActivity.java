package com.dtf.hellobeacon;
import com.example.hellobeacon.R;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;


public class MyStatsActivity extends Activity{
	
	private SharedPreferences prefs;
	private String firstname;
	private String lastname;
	private TextView myname;

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
		
		
	}
	
}
