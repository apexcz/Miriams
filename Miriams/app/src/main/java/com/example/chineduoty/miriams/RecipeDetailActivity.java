package com.example.chineduoty.miriams;

import android.annotation.TargetApi;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
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
import com.example.chineduoty.miriams.model.Ingredient;
import com.example.chineduoty.miriams.model.Recipe;
import com.example.chineduoty.miriams.model.Step;
import com.example.chineduoty.miriams.provider.RecipeContract;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * An activity representing a list of Details. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link StepDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class RecipeDetailActivity extends AppCompatActivity implements StepsAdapter.StepAdapterClickHandler {

    private boolean mTwoPane;
    private static final String TAG = RecipeDetailActivity.class.getSimpleName();
    public static final String STEP_KEY = "step";
    public static final String ACTION_RECIPE_UPDATED =
            "com.example.chineduoty.miriams.ACTION_RECIPE_UPDATED";

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recipe_name_text)
    TextView recipeName;
    @BindView(R.id.recipe_serving_text)
    TextView recipeServing;
    @BindView(R.id.ingredient_rv)
    RecyclerView ingredientRecyclerView;
    @BindView(R.id.bookmark_btn)
    FloatingActionButton fabBookmark;

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
        if (intent.resolveActivity(getPackageManager()) != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            recipe = gson.fromJson(intent.getStringExtra(Intent.EXTRA_TEXT), Recipe.class);
        } else {
            Log.e(TAG, "Recipe not found");
            return;
        }

        setContentView(R.layout.activity_recipe_detail);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(recipe.getName());

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            ingredientRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        } else {
            ingredientRecyclerView.setLayoutManager(new GridLayoutManager(this, 5));
        }

        ingredientsAdapter = new IngredientsAdapter(this, recipe.getIngredients());
        ingredientRecyclerView.setAdapter(ingredientsAdapter);

        stepRecyclerView = (RecyclerView) findViewById(R.id.steps_rv);
        stepRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        lstStep = recipe.getSteps();
        stepsAdapter = new StepsAdapter(this, lstStep, RecipeDetailActivity.this);
        stepRecyclerView.setAdapter(stepsAdapter);

        recipeName.setText(recipe.getName());
        recipeServing.setText("" + recipe.getServings());

        if (findViewById(R.id.step_detail_container) != null) {
            mTwoPane = true;
        }

        if (isBookmarked()) {
            fabBookmark.setImageDrawable(ActivityCompat.getDrawable(this, R.drawable.ic_bookmark_brown_24dp));
        } else {
            fabBookmark.setImageDrawable(ActivityCompat.getDrawable(this, R.drawable.ic_bookmark_white_24dp));
        }

        fabBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isBookmarked()) {
                    removeIngredients();
                    fabBookmark.setImageDrawable(ActivityCompat.getDrawable(RecipeDetailActivity.this, R.drawable.ic_bookmark_white_24dp));
                    updateWidgetsBroadcast();
                } else {
                    storeIngredients(recipe.getIngredients());
                    fabBookmark.setImageDrawable(ActivityCompat.getDrawable(RecipeDetailActivity.this, R.drawable.ic_bookmark_brown_24dp));
                    updateWidgetsBroadcast();
                }

            }
        });
    }

    private void storeIngredients(List<Ingredient> ingredients) {
        removeIngredients();
        for (Ingredient ingredient : ingredients) {
            ContentValues values = new ContentValues();
            values.put(RecipeContract.RecipeEntry.COLUMN_RECIPE_ID, recipe.getId());
            values.put(RecipeContract.RecipeEntry.COLUMN_RECIPE_NAME, recipe.getName());
            values.put(RecipeContract.RecipeEntry.COLUMN_INGREDIENT_NAME, ingredient.getIngredient());
            values.put(RecipeContract.RecipeEntry.COLUMN_INGREDIENT_MEASURE, ingredient.getMeasure());
            values.put(RecipeContract.RecipeEntry.COLUMN_INGREDIENT_QUANTITY, ingredient.getQuantity());
            getContentResolver().insert(RecipeContract.RecipeEntry.CONTENT_URI, values);
        }
    }

    private void removeIngredients() {
        getContentResolver().delete(RecipeContract.RecipeEntry.CONTENT_URI, null, null);
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

        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putString(StepDetailFragment.ARG_ITEM_ID, "" + position);
            StepDetailFragment fragment = StepDetailFragment.newInstance(position, recipe.getSteps());
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.step_detail_container, fragment)
                    .commit();
        } else {
            Intent intent = new Intent(this, StepDetailActivity.class);
            intent.putExtra(StepDetailFragment.ARG_ITEM_ID, position);
            intent.putExtra(StepDetailFragment.RECIPE_NAME, recipe.getName());

            Toast.makeText(this, "Clicked item " + position, Toast.LENGTH_LONG).show();
            String stepString = gson.toJson(lstStep);
            intent.putExtra(STEP_KEY, stepString);
            startActivity(intent);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private boolean isBookmarked() {
        String[] projection = new String[]{RecipeContract.RecipeEntry.COLUMN_RECIPE_ID};
        String selection = RecipeContract.RecipeEntry.COLUMN_RECIPE_ID + "=?";
        String[] selectionArgs = new String[]{String.valueOf(recipe.getId())};
        Cursor cursor = getContentResolver().query(RecipeContract.RecipeEntry.CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null, null);

        return (cursor != null ? cursor.getCount() : 0) > 0;
    }

    private void updateWidgetsBroadcast() {
        Context context = getApplicationContext();
        Intent intent = new Intent(ACTION_RECIPE_UPDATED);
        intent.setComponent(new ComponentName(context, MiriamsWidgetProvider.class));
        context.sendBroadcast(intent);
    }

}

