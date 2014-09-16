package com.dtf.hellobeacon;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dtf.hellobeacon.util.DateUtil;
import com.dtf.hellobeacon.views.ProgressBarAnimation;
import com.example.hellobeacon.R;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;


public class MyStatsActivity extends Activity {

	private SharedPreferences prefs;
	private String firstname;
	private String lastname;
	private String gym;
	private TextView lastvisitheader;
	private ProgressBar spinner;
	
	private ProgressBar weekProgress;
	private ProgressBar monthProgress;
	private ProgressBar ytdProgress;
	private LinearLayout mainLayout;
	
	private TextView weekvisitsTV;
	private TextView monthvisitsTV;
	private TextView ytdvisitsTV;
	private TextView lastvisitTV;
	
	Typeface boldfont;
	Typeface font;
	
	private int week_visits;
	private int month_visits;
	private int ytd_visits;
	List<Long> visits;
	DateFormat df;
	
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
		lastvisitheader = (TextView) findViewById(R.id.tv_lastvisitheader);
		
		weekvisitsTV = (TextView) findViewById(R.id.tv_weeklyvisits);
		monthvisitsTV = (TextView) findViewById(R.id.tv_monthlyvisits);
		ytdvisitsTV = (TextView) findViewById(R.id.tv_ytdvisits);
		lastvisitTV = (TextView) findViewById(R.id.tv_lastvisit);
		
		weekProgress = (ProgressBar) findViewById(R.id.weeklyprogress);
		monthProgress = (ProgressBar) findViewById(R.id.monthlyprogress);
		ytdProgress = (ProgressBar) findViewById(R.id.ytd_progress);
		
		
		//set person's name
		prefs = this.getSharedPreferences("com.dtf.hellobeacon", 0);
		firstname = prefs.getString("firstName", "No First Name");
		lastname = prefs.getString("lastName", "No Last Name");
		gym = prefs.getString("gym", "No Gym Selected");
		//set header and font
		String gymWithoutSpaces = gym.replaceAll("\\s+","");
		
		boldfont = Typeface.createFromAsset(getAssets(), "Montserrat-Bold.ttf");
		font = Typeface.createFromAsset(getAssets(), "Montserrat-Regular.ttf");
		
		
		
		//get the user's visits from firebase
		Firebase visitsref = new Firebase("https://hellobeacon.firebaseio.com/Gyms/" + gymWithoutSpaces + "/Users/" + firstname + lastname + "/Visits/");
		
		visitsref.addValueEventListener(new ValueEventListener() {
			
			@Override
			public void onDataChange(DataSnapshot snapshot) {
				
				week_visits = 0;
				month_visits = 0;
				ytd_visits = 0;
				
				
				String list_items = snapshot.getValue().toString();
				String[] list_values = list_items.split(",");
				
				Long t, last_t=(long) 0;
				int i = 1;
				for (String s : list_values){
					
					//store a string of just the time value
					String e = s.substring(s.lastIndexOf("=") + 1, s.lastIndexOf("=") + 14);
					t = Long.valueOf(e).longValue();
					
					if(t > last_t){
						last_t = t;
					}
					
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
				
				
				
//				Log.i("EEE", "ytd visits: " + Long.toString(ytd_visits));
//				Log.i("EEE", "monthly visits: " + Long.toString(month_visits));
//				Log.i("EEE", "weekly visits: " + Long.toString(week_visits));
				
				//hide the loading spinner before displaying content
				spinner.setVisibility(View.GONE);
				mainLayout.setVisibility(LinearLayout.VISIBLE);
				
				//display progress bars with the number of visits set as values
				//weekProgress.setProgress(week_visits);
				//monthProgress.setProgress(month_visits);
				//ytdProgress.setProgress(ytd_visits);
				
				//set text for number of visits
				weekvisitsTV.setText(Integer.toString(week_visits) + " Visits");
				weekvisitsTV.setTypeface(font);
				
				monthvisitsTV.setText(Integer.toString(month_visits) + " Visits");
				monthvisitsTV.setTypeface(font);
				
				ytdvisitsTV.setText(Integer.toString(ytd_visits) + " Visits");
				ytdvisitsTV.setTypeface(font);
				
				df = new SimpleDateFormat("MM/dd/yyyy K:mm a");
				Date date = new Date(Long.valueOf(last_t).longValue());
				lastvisitTV.setText(df.format(date));
				
				lastvisitheader.setTypeface(font);
				lastvisitTV.setTypeface(font);
				

				//animate progressbars (setting max values higher and multiplying final values for a smoother animation)
				weekProgress.setMax(700);
				weekProgress.startAnimation(ProgressBarAnimation.setAnimation(weekProgress, 0, week_visits*100));
				
				monthProgress.setMax(3000);
				monthProgress.startAnimation(ProgressBarAnimation.setAnimation(monthProgress, 0, month_visits*100));
				
				ytdProgress.setMax(3650);
				ytdProgress.startAnimation(ProgressBarAnimation.setAnimation(ytdProgress, 0, ytd_visits*10));
				
				
				
				
				
			}

			@Override
			public void onCancelled(FirebaseError error) {
				System.err.println("Listener was cancelled");
			}

		});
		
		
	}
		
	
	
}







