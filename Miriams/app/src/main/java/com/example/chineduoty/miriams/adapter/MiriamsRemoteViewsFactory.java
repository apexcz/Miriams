package com.example.chineduoty.miriams.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.chineduoty.miriams.MainActivity;
import com.example.chineduoty.miriams.R;
import com.example.chineduoty.miriams.model.Ingredient;
import com.example.chineduoty.miriams.model.Recipe;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Random;

/**
 * Created by chineduoty on 6/24/17.
 */

public class MiriamsRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private List<Recipe> lstRecipe;
    private List<Ingredient> lstIngredients;


    public MiriamsRemoteViewsFactory(Context applicationContext, Intent intent) {
        mContext = applicationContext;
        Gson gson = new Gson();
        TypeToken<List<Recipe>> token = new TypeToken<List<Recipe>>(){};
        lstRecipe = gson.fromJson(intent.getStringExtra(MainActivity.RECIPE_LIST_KEY),
                token.getType());
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        final long identityToken = Binder.clearCallingIdentity();
        Random random = new Random();
        int item = random.nextInt(lstRecipe.size());
        lstIngredients = lstRecipe.get(item).getIngredients();
        Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (lstIngredients == null)
            return 0;

        return lstIngredients.size();

    }

    @Override
    public RemoteViews getViewAt(int i) {
        if (i == AdapterView.INVALID_POSITION || lstIngredients == null || i > lstIngredients.size())
            return null;

        RemoteViews rv = new RemoteViews(mContext.getPackageName(),
                R.layout.collection_widget_list_item);

        rv.setTextViewText(R.id.widgetItemTaskNameLabel, lstIngredients.get(i).getIngredient());

        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
