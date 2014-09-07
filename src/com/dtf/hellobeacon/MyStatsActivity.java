package com.dtf.hellobeacon;
import java.util.List;
import com.dtf.hellobeacon.util.DateUtil;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.hellobeacon.R;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;


public class MyStatsActivity extends Activity {

	private SharedPreferences prefs;
	private String firstname;
	private String lastname;
	private TextView myname;
	private ProgressBar spinner;
	
	private ProgressBar weekProgress;
	private ProgressBar monthProgress;
	private ProgressBar ytdProgress;
	private LinearLayout mainLayout;
	
	private int week_visits;
	private int month_visits;
	private int ytd_visits;
	List<Long> visits;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mystats);
		
		mainLayout=(LinearLayout)this.findViewById(R.id.mystats_layout);
		mainLayout.setVisibility(LinearLayout.GONE);
		
		//loading animation
		spinner = (ProgressBar)findViewById(R.id.progressBar1);
		spinner.setVisibility(View.VISIBLE);
		
		//set view objects
		myname = (TextView) findViewById(R.id.tv_MyName);
		weekProgress = (ProgressBar) findViewById(R.id.weeklyprogress);
		monthProgress = (ProgressBar) findViewById(R.id.monthlyprogress);
		ytdProgress = (ProgressBar) findViewById(R.id.ytd_progress);
		
		//set person's name
		prefs = this.getSharedPreferences("com.dtf.hellobeacon", 0);
		firstname = prefs.getString("firstName", "No First Name");
		lastname = prefs.getString("lastName", "No Last Name");
		
		myname.setText(firstname + " " + lastname + " :: PERSONAL STATS");
		
		
		//get the user's visits from firebase
		Firebase visitsref = new Firebase("https://hellobeacon.firebaseio.com/Users/" + firstname + lastname + "/Visits/");
		
		visitsref.addValueEventListener(new ValueEventListener() {
			
			@Override
			public void onDataChange(DataSnapshot snapshot) {
				
				week_visits = 0;
				month_visits = 0;
				ytd_visits = 0;
				
				
				String list_items = snapshot.getValue().toString();
				String[] list_values = list_items.split(",");
								
				int i = 1;
				for (String s : list_values){
					
					//store a string of just the time value
					String e = s.substring(s.lastIndexOf("=") + 1, s.lastIndexOf("=") + 14);
					Long t = Long.valueOf(e).longValue();
					
					//increment the appropriate progress values
					if(t >= DateUtil.getWeekStart()){
						week_visits = Math.min(week_visits+1, 7);
						month_visits = Math.min(month_visits+1, 30);
						ytd_visits = Math.min(ytd_visits+1, 365);
					}
					else if(t >= DateUtil.getMonthStart()){
						month_visits = Math.min(month_visits+1, 30);
						ytd_visits = Math.min(ytd_visits+1, 365);
					}
					else if(t >= DateUtil.getYearStart()){
						ytd_visits = Math.min(ytd_visits+1, 365);
					}
					
				}
				
				
				//display progress bars with the number of visits set as values
				Log.i("EEE", "ytd visits: " + Long.toString(ytd_visits));
				Log.i("EEE", "monthly visits: " + Long.toString(month_visits));
				Log.i("EEE", "weekly visits: " + Long.toString(week_visits));
				
				
				weekProgress.setProgress(week_visits);
				monthProgress.setProgress(month_visits);
				ytdProgress.setProgress(ytd_visits);
				
				//hide the loading spinner before displaying content
				spinner.setVisibility(View.GONE);
				mainLayout.setVisibility(LinearLayout.VISIBLE);
				
			}

			@Override
			public void onCancelled(FirebaseError error) {
				System.err.println("Listener was cancelled");
			}

		});
		
		
	}
		
	
	
}







