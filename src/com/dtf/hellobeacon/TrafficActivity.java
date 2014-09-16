package com.dtf.hellobeacon;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.joda.time.DateTime;

import pl.flex_it.androidplot.MultitouchPlot;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidplot.series.XYSeries;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYStepMode;
import com.dtf.hellobeacon.model.Gym;
import com.dtf.hellobeacon.model.Visit;
import com.dtf.hellobeacon.util.GraphUtil;
import com.dtf.hellobeacon.util.RangingTask;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.example.hellobeacon.R;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.GenericTypeIndicator;
import com.firebase.client.ValueEventListener;


public class TrafficActivity extends Activity{

	private static final int REQUEST_ENABLE_BT = 0;
	private static final Region ALL_ESTIMOTE_BEACONS_REGION = new Region("rid", null, null, null);
	RangingTask rTask;
	
	private SharedPreferences prefs;
	private String gym;
	private TextView gymname;
	private ProgressBar spinner;
	
	protected Gym gymData;
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
	private boolean graphDataLoaded;
	List<Long> visits;
	//
	
	protected HashMap<String, List<Visit>> listOfVisits = new HashMap<String, List<Visit>>();
	
	DateFormat df = new SimpleDateFormat("MM/dd/yyyy K:mm a");	
	BeaconManager beaconManager;
	Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.traffic);

		//set flag for retrieving firebase data
		
		graphDataLoaded = false;
		
		//set gym name
		prefs = this.getSharedPreferences("com.dtf.hellobeacon", 0);
		gym = prefs.getString("gym", "No Gym Selected");
		gymAddress = prefs.getString("gymaddress", "dummy address");
		gymCity = prefs.getString("gymcity", "dummy city");
		gymState = prefs.getString("gymstate", "dummy state");
		gymPhoneNumber = prefs.getString("gymphone", "555-555-5555");
		openHour = prefs.getInt("gymopenhour", 0);
		closeHour = prefs.getInt("gymclosehour", 24);
		capacity = prefs.getInt("gymcapacity", 300);
		
		gymname = (TextView) findViewById(R.id.tv_GymName);
		gymname.setText(gym);

		//way to instantiate with builder pattern
		gymData = new Gym.GymBuilder()
				.buildName(gym)
				.buildContactInfo(gymAddress, gymCity, gymState, gymPhoneNumber)
				.buildHours(openHour, closeHour)
				.build();
		
		//ranging
		/*
		beaconManager = new BeaconManager(this);
		context = this;
		rTask = new RangingTask(context, beaconManager);
		rTask.execute(beaconManager);
		*/
		
		firstname = prefs.getString("firstName", "No First Name");
		lastname = prefs.getString("lastName", "No Last Name");
		
				
		populateGymVisits();
		//
		//DRAW GRAPH
		//drawGraph();
		
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
		case R.id.about:
			Intent j = new Intent(this, AboutActivity.class);
			startActivity(j);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	


	protected void populateGymVisits() {
		

		final DateFormat df = new SimpleDateFormat("MM/dd/yyyy K:mm a");
		//get the user's visits from firebase
		
		String gymWithoutSpaces = gym.replaceAll("\\s+","");
		Log.d("traffic activity", "pop graph gym name w/o spaces - " + gymWithoutSpaces);
		Firebase visitsref = new Firebase("https://hellobeacon.firebaseio.com/Gyms/" +gymWithoutSpaces + "/Visits");

		visitsref.addValueEventListener(new ValueEventListener() {
			
			@Override
			public void onDataChange(DataSnapshot snapshot) {			
			if(!graphDataLoaded) {
					for(DataSnapshot child : snapshot.getChildren()) {
						try {
							addToTimeMap(child.getValue().toString());
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					drawGraph();
					graphDataLoaded = true;
				}	
			}

			@Override
			public void onCancelled(FirebaseError error) {
				System.err.println("Listener was cancelled");
			}

		});
		
	}
	
	
	
	private void drawGraph(){
		MultitouchPlot plot = (MultitouchPlot) findViewById(R.id.multitouchPlot);
        
		//show spinner loading
		spinner = (ProgressBar) findViewById(R.id.progressBar2);
		spinner.setVisibility(View.VISIBLE);
		
		//styling
		GraphUtil.setBordersandPadding(plot);
		GraphUtil.setColors(plot);
        GraphUtil.configureLegend(plot);
		
        // Create an array of y-values to plot:
        Number[] y_values = new Number[closeHour-openHour];
        for(int index = 0; index < closeHour - openHour; index++) {
        	y_values[index] = getVisitsAtHour(index + openHour);
        }
        
        // create an array of x-values: (8AM to 10PM in 1hr increments - July 25th EST)
        Number[] x_values = {
        		1406271600,  // 12AM
        		1406275200,  // 1AM
        		1406278800,  // 2AM
        		1406282400,  // 3AM
        		1406286000,  // 4AM
        		1406289600,  // 5AM
        		1406293200,  // 6AM
        		1406296800,  // 7AM
        		1406300400,  // 8AM
        		1406304000,  // 9AM
        		1406307600,  // 10AM
        		1406311200,  // 11AM
        		1406314800,  // 12PM
        		1406318400,  // 1PM
        		1406322000,  // 2PM
        		1406325600,  // 3PM
        		1406329200,  // 4PM
        		1406332800,  // 5PM
        		1406336400,  // 6PM
        		1406340000,  // 7PM
        		1406343600,  // 8PM
        		1406347200,  // 9PM
        		1406350800,  // 10PM
        		1406354400  // 11PM
        };

        // Turn the above arrays into XYSeries:
        XYSeries series1 = new SimpleXYSeries(
        		Arrays.asList(x_values),
        		Arrays.asList(y_values),          	// SimpleXYSeries takes a List so turn our array into a List
                "Capacity Level");                  // Set the display title of the series

        // Create a formatter to use for drawing a series using LineAndPointRenderer:
        LineAndPointFormatter series1Format = new LineAndPointFormatter(
                Color.rgb(102, 204, 255),              // line color
                Color.rgb(102, 204, 255),              // point color
                Color.rgb(189, 233, 255));             // fill color
        
        //change the line width
        Paint paint = series1Format.getLinePaint();
        paint.setStrokeWidth(4);
        series1Format.setLinePaint(paint);

        // Add series1 to the xyplot:
        plot.addSeries(series1, series1Format);

        // Reduce the number of domain and range labels
        plot.setTicksPerRangeLabel(2);
        //multitouchPlot.setTicksPerDomainLabel(3);

        //set gridlines
        plot.setRangeStep(XYStepMode.SUBDIVIDE, 9);
        plot.setDomainStep(XYStepMode.SUBDIVIDE, 6);
        
        //set range and domain label widths
        plot.getGraphWidget().setRangeLabelWidth(70);
        plot.getGraphWidget().setDomainLabelWidth(50);
        
        //set textsize of y-axis and x-axis labels
        plot.getGraphWidget().getRangeLabelPaint().setTextSize(35);
        plot.getGraphWidget().getDomainLabelPaint().setTextSize(30);
        
        //set y-axis label format (display numbers without decimal places)
        plot.setRangeValueFormat(new DecimalFormat("#"));
        
        //display times as the domain labels
        plot.setDomainValueFormat(new Format() {

            private SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");

            @Override
            public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
                // because our timestamps are in seconds and SimpleDateFormat expects milliseconds
                // we multiply our timestamp by 1000:
                long timestamp = ((Number) obj).longValue() * 1000;
                Date date = new Date(timestamp);
                return dateFormat.format(date, toAppendTo, pos);
            }

            @Override
            public Object parseObject(String source, ParsePosition pos) {
                return null;
            }
        });
        
        
        // By default, AndroidPlot displays developer guides to aid in laying out your plot.
        // To get rid of them call disableAllMarkup():
        plot.disableAllMarkup();
        plot.setRangeBoundaries(0, 100, BoundaryMode.FIXED);
    
		// show graph element and hide spinner
		plot.setVisibility(View.VISIBLE);
		spinner.setVisibility(View.GONE);
        
	}
	
	
	
	public void addToTimeMap(String fbTimeKeyValue) throws ParseException {
		String timeStamp = fbTimeKeyValue.substring(fbTimeKeyValue.lastIndexOf("=") + 1, fbTimeKeyValue.lastIndexOf("=") + 14);
		DateTime date = new DateTime(Long.valueOf(timeStamp).longValue());
		Date shittyDate = new Date(Long.valueOf(timeStamp).longValue());
		String reportdate = df.format(shittyDate);
		int hour = date.getHourOfDay();
		Visit newVisit = new Visit();
		newVisit.setEnterTime(reportdate);
		
		if(!listOfVisits.containsKey(String.valueOf(hour)))
		{
			ArrayList<Visit> newVisitList = new ArrayList<Visit>();
			newVisitList.add(newVisit);
			listOfVisits.put(String.valueOf(hour), newVisitList);
		}
		else {
			//TODO-remove the remove if timestamp already there part if necessary
			if(!listOfVisits.get(String.valueOf(hour)).contains(newVisit)) {
				listOfVisits.get(String.valueOf(hour)).add(newVisit);
			}
		}
	}

	public int getVisitsAtHour(int hour) {		
		String time = String.valueOf(hour);

		if(!listOfVisits.containsKey(time)) {
			return 0;
		}
		//else return size of list

		return listOfVisits.get(time).size();
	}
}





