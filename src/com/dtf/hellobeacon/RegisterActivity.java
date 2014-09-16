package com.dtf.hellobeacon;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.dtf.hellobeacon.model.User;
import com.example.hellobeacon.R;
import com.firebase.client.Firebase;
import com.firebase.client.core.Context;


public class RegisterActivity extends Activity {

	private View registerLayout;
	private EditText firstname_field;
	private EditText lastname_field;
	private EditText email_field;
	private Spinner gymselection;
	private Button registerbutton;
	private SharedPreferences prefs;
	private Editor editor;
	private User user;

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);

		//obtain sharedpreferences
		prefs = this.getSharedPreferences("com.dtf.hellobeacon", 0);

		registerLayout = findViewById(R.id.registerLayout);
		firstname_field = (EditText) findViewById(R.id.et_firstname);
		lastname_field = (EditText) findViewById(R.id.et_lastname);
		email_field = (EditText) findViewById(R.id.et_email);
		gymselection = (Spinner) findViewById(R.id.spinner_gyms);
		registerbutton = (Button) findViewById(R.id.b_register);

	}

	@Override
	protected void onPause() {
		super.onPause();
		finish();
	}
	

	public void register(View v){
		//get a reference to the FireBase root

		String firstname = firstname_field.getText().toString();
		String lastname = lastname_field.getText().toString();
		String gym = gymselection.getSelectedItem().toString();
		String email = email_field.getText().toString();
		boolean isInGym = false;
		
		String gymWithoutSpaces = gym.replaceAll("\\s+","");
		
		user = new User(firstname, lastname, gym, email, isInGym);

		Firebase newpushref = new Firebase("https://hellobeacon.firebaseio.com/Gyms/" + gymWithoutSpaces + "/Users/" + firstname + lastname);
		newpushref.setValue(user);

		//create a new editor for the prefs object
		editor = prefs.edit();

		//store the authtoken as "true" and store the user's attributes
		editor.putBoolean("authtoken", true);
		editor.putString("firstName", user.getFirstName());
		editor.putString("lastName", user.getLastName());
		editor.putString("email", user.getEmail());
		editor.putString("gym", user.getGym());
	
		//editor.putInt("visits", user.getVisits());
		editor.putBoolean("is in Gym", user.getInGym());
		editor.commit();

		//after registering, start the next activity
		Intent i = new Intent(this, HomeActivity.class);
		startActivity(i);
		

	}
	
	
}
