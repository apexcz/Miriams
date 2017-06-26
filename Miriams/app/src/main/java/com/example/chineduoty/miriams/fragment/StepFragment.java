package com.example.chineduoty.miriams.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chineduoty.miriams.R;
import com.example.chineduoty.miriams.StepDetailFragment;
import com.example.chineduoty.miriams.adapter.StepsViewPagerAdapter;
import com.example.chineduoty.miriams.model.Step;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by chineduoty on 6/25/17.
 */

public class StepFragment extends Fragment {

    public static final String STEP_LIST = "step_list";
    public static final String STEP_INDEX = "step_index";

    private static final String TAG = "StepFragment";

    private List<Step> lstStep;
    private int stepPosition;


    @BindView(R.id.steps_viewpager)
    ViewPager viewPager;
    @BindView(R.id.previous_btn)
    FloatingActionButton btnPrevious;
    @BindView(R.id.next_btn)
    FloatingActionButton btnNext;

    private StepsViewPagerAdapter adapter;

    public StepFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        lstStep = getArguments().getParcelableArrayList(STEP_LIST);
        stepPosition = getArguments().getInt(STEP_INDEX);

        View rootView = inflater.inflate(R.layout.fragment_step, container, false);
        ButterKnife.bind(this, rootView);

        if (lstStep == null) {
            Log.e(TAG, "This fragment has a null list of steps");
        } else {
            List<Fragment> lstFragment = new ArrayList<Fragment>();
            for (int i = 0; i < lstStep.size(); i++) {
                lstFragment.add(new StepDetailFragment().newInstance(i, (ArrayList<Step>) lstStep));
            }
            adapter = new StepsViewPagerAdapter(getFragmentManager(),
                    lstFragment, lstStep);
            viewPager.setAdapter(adapter);
            viewPager.setCurrentItem(stepPosition);
        }

        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPreviousClicked();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onNextClicked();
            }
        });
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle currentState) {
        currentState.putParcelableArrayList(STEP_LIST, (ArrayList<Step>) lstStep);
        currentState.putInt(STEP_INDEX, stepPosition);
    }

    public void onPreviousClicked() {
        viewPager.setCurrentItem((viewPager.getCurrentItem() > 0) ?
                (viewPager.getCurrentItem() - 1) : lstStep.size());
    }

    public void onNextClicked() {
        viewPager.setCurrentItem((viewPager.getCurrentItem() < lstStep.size()) ?
                (viewPager.getCurrentItem() + 1) : 0);
    }
}
