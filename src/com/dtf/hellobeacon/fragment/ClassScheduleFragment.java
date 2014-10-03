package com.dtf.hellobeacon.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hellobeacon.R;

public class ClassScheduleFragment extends HomePageFragment {

	
	public static ClassScheduleFragment newInstance() {
		ClassScheduleFragment fragment = new ClassScheduleFragment();
		return fragment;
	}
	
	 @Override
	 public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
	 {
	    return paramLayoutInflater.inflate(R.layout.fragment_class_page, paramViewGroup, false);
	 }
}
