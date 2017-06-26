package com.example.chineduoty.miriams;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

import com.example.chineduoty.miriams.adapter.StepsViewPagerAdapter;
import com.example.chineduoty.miriams.fragment.StepFragment;
import com.example.chineduoty.miriams.model.Recipe;
import com.example.chineduoty.miriams.model.Step;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * An activity representing a single Detail detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link RecipeDetailActivity}.
 */
public class StepDetailActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        String recipeName = getIntent().getStringExtra(RecipeDetailActivity.RECIPE_NAME);
        int stepIndex = getIntent().getIntExtra(StepFragment.STEP_INDEX, 0);
        List<Step> lstStep = getIntent().getParcelableArrayListExtra(StepFragment.STEP_LIST);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(recipeName);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        StepFragment stepFragment = new StepFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(StepFragment.STEP_LIST, (ArrayList<Step>) lstStep);
        bundle.putInt(StepFragment.STEP_INDEX, stepIndex);
        stepFragment.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.step_detail_wrapper, stepFragment)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            //NavUtils.navigateUpTo(this, new Intent(this, RecipeDetailActivity.class));
            onBackPressed();
            //NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
