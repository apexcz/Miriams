package com.example.chineduoty.miriams.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.chineduoty.miriams.MainActivity;
import com.example.chineduoty.miriams.R;
import com.example.chineduoty.miriams.model.Ingredient;
import com.example.chineduoty.miriams.model.Recipe;
import com.example.chineduoty.miriams.provider.RecipeContract;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Random;

/**
 * Created by chineduoty on 6/24/17.
 */

public class MiriamsRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private Cursor cursor;



    public MiriamsRemoteViewsFactory(Context applicationContext, Intent intent) {
        mContext = applicationContext;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        if (cursor != null) {
            cursor.close();
        }
        final long identityToken = Binder.clearCallingIdentity();
        String[] projection = new String[] { RecipeContract.RecipeEntry.COLUMN_INGREDIENT_NAME};

        cursor = mContext.getContentResolver().query(RecipeContract.RecipeEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
        Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public void onDestroy() {
        if (cursor != null) {
            cursor.close();
            cursor = null;
        }

    }

    @Override
    public int getCount() {
        return cursor == null ? 0 : cursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        if (i == AdapterView.INVALID_POSITION ||
                cursor == null || !cursor.moveToPosition(i)) {
            return null;
        }

        RemoteViews rv = new RemoteViews(mContext.getPackageName(),
                R.layout.collection_widget_list_item);

        rv.setTextViewText(R.id.widgetItemTaskNameLabel,
                cursor.getString(0));

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
