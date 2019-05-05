package com.hyipc.uhf_r2000.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class FragmentAdapter extends FragmentPagerAdapter{
	private List<Fragment> fragments;
	public FragmentAdapter(FragmentManager fm,List<Fragment> fragments){
		super(fm);
		this.fragments = fragments;
	}

	@Override
	public Fragment getItem(int position) {
		// TODO Auto-generated method stub
		if (fragments == null || fragments.size() == 0) {
			return null;
		}
		return fragments.get(position);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return fragments.size();
	}

}
