package com.dtf.hellobeacon.adapter;

import com.dtf.hellobeacon.fragment.HomePageFragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;

public class HomeStatePagerAdapter extends FragmentStatePagerAdapter {
    
	public HomeStatePagerAdapter(FragmentManager fm) {
		super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return HomePageFragment.create(position);
    }

    @Override
    public int getCount() {
        return 0;
    }


}
