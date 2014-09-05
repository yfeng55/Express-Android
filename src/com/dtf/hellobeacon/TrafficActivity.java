package com.dtf.hellobeacon;
import java.sql.Date;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import android.widget.TextView;
import android.widget.Toast;

import com.androidplot.series.XYSeries;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYStepMode;
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

	//
	private String firstname;
	private String lastname;
	List<Long> visits;
	StringBuilder builder;
	//
	
	BeaconManager beaconManager;
	Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.traffic);

		//set gym name
		prefs = this.getSharedPreferences("com.dtf.hellobeacon", 0);
		gym = prefs.getString("gym", "No Gym Selected");
		gymname = (TextView) findViewById(R.id.tv_GymName);
		gymname.setText(gym);

		//ranging
		beaconManager = new BeaconManager(this);
		context = this;
		rTask = new RangingTask(context, beaconManager);
		rTask.execute(beaconManager);
		
		
		firstname = prefs.getString("firstName", "No First Name");
		lastname = prefs.getString("lastName", "No Last Name");
		
				
		builder = new StringBuilder();

		populateGymVisits();
		//
		//DRAW GRAPH
		drawGraph();
		
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
					Toast.makeText(TrafficActivity.this, "Cannot start ranging, something terrible happened",
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

	protected void populateGymVisits() {
		/*

		final DateFormat df = new SimpleDateFormat("MM/dd/yyyy K:mm a");
		//get the user's visits from firebase
		Firebase visitsref = new Firebase("https://hellobeacon.firebaseio.com/Gyms/" +gymname.toString());
		
		ArrayList<List<Visit>> listOfVisit = new ArrayList<List<Visit>>();
		final ArrayList<Visit> test = new ArrayList<Visit>();
		
		visitsref.addValueEventListener(new ValueEventListener() {
			
			@Override
			public void onDataChange(DataSnapshot snapshot) {
				GenericTypeIndicator<List<List<Visit>>> t = new GenericTypeIndicator<List<List<Visit>>>() {};
			    List<List<Visit>> gymVisits = snapshot.getValue(t);
				
				//TODO edit accordingly
				//String list_items = snapshot.getValue().toString();
				//String[] list_values = list_items.split(",");
								
				int i = 1;
				
				for (String s : list_values){
					
					String e = s.substring(s.lastIndexOf("=") + 1, s.lastIndexOf("=") + 14);
					//e = e.replaceAll("[^\\d.]", "");
					//timestamps.add(Long.valueOf(e).longValue());
					
					Date date = new Date(Long.valueOf(e).longValue());
					String reportdate = df.format(date);
					
					builder.append("VISIT " + Integer.toString(i) + ": " + reportdate + "\n\n");
					i++;
				}
												
				test.add(builder.toString());
			}

			@Override
			public void onCancelled(FirebaseError error) {
				System.err.println("Listener was cancelled");
			}

		});*/
	}
	
	private void drawGraph(){
		MultitouchPlot plot = (MultitouchPlot) findViewById(R.id.multitouchPlot);
        
		//styling
		GraphUtil.setBordersandPadding(plot);
		GraphUtil.setColors(plot);
        GraphUtil.configureLegend(plot);
		
        // Create an array of y-values to plot:
        Number[] y_values = {30, 35, 32, 40, 38, 45, 55, 57, 67, 70, 72, 65, 55, 56, 47};
        
        // create an array of x-values: (8AM to 10PM in 1hr increments - July 25th EST)
        Number[] x_values = {
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
        		1406350800   // 10PM
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
        plot.setDomainStep(XYStepMode.SUBDIVIDE, 5);
        
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
	}

}





