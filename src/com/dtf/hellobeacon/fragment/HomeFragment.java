package com.dtf.hellobeacon.fragment;

import com.example.hellobeacon.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HomeFragment extends HomePageFragment{

	public static HomeFragment newInstance() {
		HomeFragment fragment = new HomeFragment();
		

		
		return fragment;
	}
	
	 @Override
	 public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
	 {
	    return paramLayoutInflater.inflate(R.layout.fragment_home_page, paramViewGroup, false);
	 }
	
}
