package com.dtf.hellobeacon;

import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import pl.flex_it.androidplot.MultitouchPlot;

import com.androidplot.series.XYSeries;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYStepMode;
import com.dtf.hellobeacon.model.Gym;
import com.dtf.hellobeacon.util.DateUtil;
import com.dtf.hellobeacon.util.GraphClickListener;
import com.dtf.hellobeacon.util.GraphUtil;
import com.dtf.hellobeacon.util.RangingTask;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.example.hellobeacon.R;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends Activity implements GraphClickListener {
	
	private static final int REQUEST_ENABLE_BT = 0;
	private static final Region ALL_ESTIMOTE_BEACONS_REGION = new Region("rid", null, null, null);
	RangingTask rTask;
	
	private SharedPreferences prefs;
	private ProgressBar spinner;
	
	protected TextView gymName;
	protected TextView openCloseTime;
	protected TextView currentCapacity;
	
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
	
	BeaconManager beaconManager;
	Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_home);
		//get authtoken value from sharedPreferences
		prefs = this.getSharedPreferences("com.dtf.hellobeacon", 0);
		prefs = this.getSharedPreferences("com.dtf.hellobeacon", 0);
		gym = prefs.getString("gym", "No Gym Selected");
		gymAddress = prefs.getString("gymaddress", "dummy address");
		gymCity = prefs.getString("gymcity", "dummy city");
		gymState = prefs.getString("gymstate", "dummy state");
		gymPhoneNumber = prefs.getString("gymphone", "555-555-5555");
		openHour = prefs.getInt("gymopenhour", 6);
		closeHour = prefs.getInt("gymclosehour", 22);
		capacity = prefs.getInt("gymcapacity", 300);

		gymData = new Gym.GymBuilder()
		.buildName(gym)
		.buildContactInfo(gymAddress, gymCity, gymState, gymPhoneNumber)
		.buildHours(openHour, closeHour)
		.build();
		
		setTextViews();
		
		//ranging
		beaconManager = new BeaconManager(this);
		context = this;
		rTask = new RangingTask(context, beaconManager);
		rTask.execute(beaconManager);
		//drawGraph();
	}
	
	public void setTextViews() {
		
		Typeface font = Typeface.createFromAsset(getAssets(), "Montserrat-Regular.ttf");
		
		
		gymName = (TextView)findViewById(R.id.gymNameText);
		openCloseTime = (TextView)findViewById(R.id.openCloseTimeText);
		currentCapacity = (TextView)findViewById(R.id.currentCapacity);
			
		gymName.setTypeface(font);
		openCloseTime.setTypeface(font);
		currentCapacity.setTypeface(font);
		
		Log.d("name", "gym data name is " + gymData.getName() + " " + gym);
		gymName.setText(gymData.getName());
		openCloseTime.setText(DateUtil.convertHourToTime(openHour, 0) + " to " + DateUtil.convertHourToTime(closeHour, 0));
		currentCapacity.setText(String.valueOf(capacity));
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

	
	/**
	 * On Destroy LifeCycle Method - RangingTask is cancelled so ranginglistener stops running
	 * and will reset to correct values when application reopened
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		rTask.setHasEntered(false);
		rTask.cancel(true);
		Log.d("destroyed", "destroyedddd");
		beaconManager.disconnect();
	}
	

	@Override
	protected void onStart() {
		super.onStart();

		// Check if device supports Bluetooth Low Energy.
		if (!beaconManager.hasBluetooth()) {
			Toast.makeText(this, "Device does not have Bluetooth Low Energy", Toast.LENGTH_LONG).show();
			return;
		}

		// If Bluetooth is not enabled, let user enable it.
		if (!beaconManager.isBluetoothEnabled()) {
			Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
		} else {
			connectToService();
		}
	}


	/**
	 * connect to beacon service for ranging
	 */
	private void connectToService() {
		getActionBar().setSubtitle("Scanning...");
		beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
			@Override
			public void onServiceReady() {
				try {
					beaconManager.startRanging(ALL_ESTIMOTE_BEACONS_REGION);
				} catch (RemoteException e) {
					Toast.makeText(HomeActivity.this, "Cannot start ranging, something terrible happened",
							Toast.LENGTH_LONG).show();
					Log.d("fail", "Cannot start ranging", e);
				}
			}
		});
	}
	
	/**
	 * TODO - document this later, from estimote SDK - don't quite understand
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_ENABLE_BT) {
			if (resultCode == Activity.RESULT_OK) {
				connectToService();
			} else {
				Toast.makeText(this, "Bluetooth not enabled", Toast.LENGTH_LONG).show();
				getActionBar().setSubtitle("Bluetooth not enabled");
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	/*
	
	private void drawGraph(){
		MultitouchPlot plot = (MultitouchPlot) findViewById(R.id.multitouchPlot1);
		plot.setGraphClickListener(this);
		//show spinner loading
		//spinner = (ProgressBar) findViewById(R.id.progressBar);
		//spinner.setVisibility(View.VISIBLE);
		plot.setClickableIntent(true);
		//styling
		GraphUtil.setBordersandPadding(plot);
		GraphUtil.setColors(plot);
        GraphUtil.configureLegend(plot);
		
        // Create an array of y-values to plot:
        Number[] y_values = new Number[closeHour-openHour];
        for(int index = 0; index < closeHour - openHour; index++) {
        	//y_values[index] = getVisitsAtHour(index + openHour);
        	y_values[index] = 0;
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
		//spinner.setVisibility(View.GONE);
        
	}
	 */
	
	/**
	 * GraphClickListener method for graph - goes to traffic activity on click
	 * TODO - figure out how to do this efficiently, have to back out of traffic 
	 * activity 3 times to get back to home when graph is shown in this activity
	 */
	@Override
	public void onMiniGraphClick() {
		Intent i = new Intent(this, TrafficActivity.class);
		startActivity(i);
	}
	
	public void toTrafficActivity(View v) {
		Intent i = new Intent(this, TrafficActivity.class);
		startActivity(i);
	}
	public void toClassPage(View v) {
		Intent i = new Intent(this, ClassScheduleActivity.class);
		startActivity(i);
	}
}
