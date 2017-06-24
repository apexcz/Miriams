package com.example.chineduoty.miriams.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.chineduoty.miriams.model.Step;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chineduoty on 6/19/17.
 */

public class StepsViewPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> lstFragments = new ArrayList<Fragment>();
    private int stepPosition;
    private List<Step> lstStep;

    public StepsViewPagerAdapter(FragmentManager manager, List<Fragment> fragments, List<Step> steps) {
        super(manager);
        lstFragments = fragments;
        lstStep = steps;
    }

    @Override
    public Fragment getItem(int position) {
        return lstFragments.get(position);
    }

    @Override
    public int getCount() {
        if (lstFragments == null) return 0;
        return lstFragments.size();
    }

}
