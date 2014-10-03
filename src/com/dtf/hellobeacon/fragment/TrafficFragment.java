package com.dtf.hellobeacon.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hellobeacon.R;

public class TrafficFragment extends HomePageFragment {
	
	public static TrafficFragment newInstance() {
		TrafficFragment fragment = new TrafficFragment();
		/*
		Bundle args = new Bundle();
		args.putSOMETHING IN DA ARGS(A KEY, a VALUE);
		fragment.setArguments(args);
		*/
		
		return fragment;
	}
	
	//TODO - use this pattern/constructor if distinct parameters are needed for each subclass of HomePageFragment
	/*
	public TrafficFragment() {
		super();
	}
	*/
	
	 @Override
	 public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
	 {
	    return paramLayoutInflater.inflate(R.layout.fragment_traffic_page, paramViewGroup, false);
	 }
}
