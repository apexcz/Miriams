package com.example.chineduoty.miriams;

import android.Manifest;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chineduoty.miriams.adapter.ItemOffsetDecoration;
import com.example.chineduoty.miriams.adapter.RecipeAdapter;
import com.example.chineduoty.miriams.model.Recipe;
import com.example.chineduoty.miriams.utilities.ApiInterface;
import com.example.chineduoty.miriams.utilities.BaseUtils;
import com.example.chineduoty.miriams.utilities.NetworkUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements RecipeAdapter.RecipeAdapterOnClickHandler {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String RECIPE_LIST_KEY = "recipes list";

    private List<Recipe> recipeList;
    private ApiInterface apiService;
    private RecyclerView recyclerView;
    private TextView textErrorMessage;
    private ProgressBar loaderRecipe;
    private RecipeAdapter recipeAdapter;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        gson = new Gson();

        recyclerView = (RecyclerView)findViewById(R.id.recipe_rv);
        textErrorMessage = (TextView)findViewById(R.id.error_message_text);
        loaderRecipe = (ProgressBar)findViewById(R.id.recipe_loader);

        recipeAdapter = new RecipeAdapter(MainActivity.this,null,MainActivity.this);
        recyclerView.setAdapter(recipeAdapter);

        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(this,R.dimen.item_offset);
        int orientation = getResources().getConfiguration().orientation;
        if(orientation == Configuration.ORIENTATION_PORTRAIT){
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }else {
            recyclerView.setLayoutManager(new GridLayoutManager(this,3));
        }

        apiService = NetworkUtils.getClient().create(ApiInterface.class);

        if(savedInstanceState != null && savedInstanceState.containsKey(RECIPE_LIST_KEY)
                && savedInstanceState.getString(RECIPE_LIST_KEY) != null ){

            TypeToken<List<Recipe>> token = new TypeToken<List<Recipe>>(){};
            List<Recipe> savedRecipes = gson.fromJson(savedInstanceState.getString(RECIPE_LIST_KEY),
                    token.getType());
            recipeAdapter.updateRecipes(savedRecipes);
        }else {
            recipeList = new ArrayList<Recipe>();
            loadRecipes();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String recipeListString = gson.toJson(recipeList);
        outState.putString(RECIPE_LIST_KEY,recipeListString);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadRecipes(){

        if (!BaseUtils.isOnline(this)) {
            Toast.makeText(this, getString(R.string.poor_network), Toast.LENGTH_LONG).show();
            return;
        }

        loaderRecipe.setVisibility(View.VISIBLE);

        Call<List<Recipe>> call = apiService.getRecipes();
        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                recipeList = response.body();
                recipeAdapter.updateRecipes(recipeList);
                loaderRecipe.setVisibility(View.INVISIBLE);
                showRecipe();
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Log.e(TAG,t.getMessage());
                call.cancel();
                loaderRecipe.setVisibility(View.INVISIBLE);
                showError();
            }
        });
    }

    private void showRecipe() {
        textErrorMessage.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void showError() {
        recyclerView.setVisibility(View.INVISIBLE);
        textErrorMessage.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(Recipe recipe) {
        Intent intent = new Intent(MainActivity.this,RecipeListActivity.class);
        String recipeString = gson.toJson(recipe);
        intent.putExtra(Intent.EXTRA_TEXT,recipeString);
        startActivity(intent);
    }
}
