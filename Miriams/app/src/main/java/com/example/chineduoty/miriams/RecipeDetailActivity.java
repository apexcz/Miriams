package com.example.chineduoty.miriams;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chineduoty.miriams.adapter.IngredientsAdapter;
import com.example.chineduoty.miriams.adapter.StepsAdapter;
import com.example.chineduoty.miriams.model.Recipe;
import com.example.chineduoty.miriams.model.Step;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * An activity representing a list of Details. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link StepDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class RecipeDetailActivity extends AppCompatActivity implements StepsAdapter.StepAdapterClickHandler {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private static final String TAG = RecipeDetailActivity.class.getSimpleName();
    public static final String STEP_KEY = "step";
    private RecyclerView ingredientRecyclerView;
    private IngredientsAdapter ingredientsAdapter;
    private RecyclerView stepRecyclerView;
    private StepsAdapter stepsAdapter;
    private Recipe recipe;
    private Gson gson;
    private List<Step> lstStep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gson = new Gson();
        lstStep = new ArrayList<Step>();

        Intent intent = getIntent();
        if(intent.resolveActivity(getPackageManager()) != null && intent.hasExtra(Intent.EXTRA_TEXT)){
            recipe = gson.fromJson(intent.getStringExtra(Intent.EXTRA_TEXT),Recipe.class);
        }else {
            Log.e(TAG,"Recipe not found");
            return;
        }

        setContentView(R.layout.activity_recipe_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(recipe.getName());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        TextView recipeName = (TextView)findViewById(R.id.recipe_name_text);
        TextView recipeServing  = (TextView)findViewById(R.id.recipe_serving_text);

        ingredientRecyclerView = (RecyclerView) findViewById(R.id.ingredient_rv);

        int orientation = getResources().getConfiguration().orientation;
        if(orientation == Configuration.ORIENTATION_PORTRAIT){
            ingredientRecyclerView.setLayoutManager(new GridLayoutManager(this,3));
        }else {
            ingredientRecyclerView.setLayoutManager(new GridLayoutManager(this,5));
        }

        ingredientsAdapter = new IngredientsAdapter(this,recipe.getIngredients());
        ingredientRecyclerView.setAdapter(ingredientsAdapter);

        stepRecyclerView = (RecyclerView)findViewById(R.id.steps_rv);
        stepRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        lstStep = recipe.getSteps();
        stepsAdapter = new StepsAdapter(this,lstStep,RecipeDetailActivity.this);
        stepRecyclerView.setAdapter(stepsAdapter);

        recipeName.setText(recipe.getName());
        recipeServing.setText(""+recipe.getServings());

        if (findViewById(R.id.step_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
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
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(int position) {
        //Toast.makeText(RecipeDetailActivity.this,"Step = " + step.getId(),Toast.LENGTH_LONG).show();

        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putString(StepDetailFragment.ARG_ITEM_ID,""+position);
            StepDetailFragment fragment = new StepDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.step_detail_container, fragment)
                    .commit();
        } else {
            Intent intent = new Intent(this, StepDetailActivity.class);
            intent.putExtra(StepDetailFragment.ARG_ITEM_ID,position);
            intent.putExtra(StepDetailFragment.RECIPE_NAME,recipe.getName());

            Toast.makeText(this, "Clicked item "+position, Toast.LENGTH_LONG).show();
            String stepString = gson.toJson(lstStep);
            intent.putExtra(STEP_KEY,stepString);
            startActivity(intent);
        }
    }

}
