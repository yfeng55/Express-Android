package com.dtf.hellobeacon.adapter;

import com.dtf.hellobeacon.fragment.ClassScheduleFragment;
import com.dtf.hellobeacon.fragment.HomeFragment;
import com.dtf.hellobeacon.fragment.HomePageFragment;
import com.dtf.hellobeacon.fragment.TrafficFragment;
import com.dtf.hellobeacon.util.Constants;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;

public class HomeStatePagerAdapter extends FragmentStatePagerAdapter {
    
	
	
	public HomeStatePagerAdapter(FragmentManager fm) {
		super(fm);
    }

    @Override
    public Fragment getItem(int position) {
    	
    	switch(position) {
    		case Constants.CLASS_FRAGMENT:
    			return ClassScheduleFragment.newInstance();
    		case Constants.HOME_FRAGMENT:
    			return HomeFragment.newInstance();
    		case Constants.TRAFFIC_FRAGMENT:
    			return TrafficFragment.newInstance();
    	}
        return HomePageFragment.create(position);
    }

    @Override
    public int getCount() {
        return 0;
    }


}
