package com.example.chineduoty.miriams;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.support.v4.app.TaskStackBuilder;
import android.widget.RemoteViews;

import com.example.chineduoty.miriams.provider.RecipeContract;
import com.example.chineduoty.miriams.services.MiriamsRemoteViewsService;

/**
 * Implementation of App Widget functionality.
 */
public class MiriamsWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            //updateAppWidget(context, appWidgetManager, appWidgetId);

            RemoteViews views = new RemoteViews(context.getPackageName(),R.layout.miriams_widget);

            String[] projection = new String[] { RecipeContract.RecipeEntry.COLUMN_RECIPE_NAME};
            Cursor cursor = context.getContentResolver().query(RecipeContract.RecipeEntry.CONTENT_URI,
                    projection,
                    null,
                    null,
                    null);
            String recipeName;
            if (cursor == null || !cursor.moveToPosition(0)) {
                recipeName = "Miriams";
            }
            recipeName = cursor.getString(0);
            views.setTextViewText(R.id.widgetTitleLabel,recipeName);

            Intent titleIntent = new Intent(context, MainActivity.class);
            PendingIntent titlePendingIntent = PendingIntent.getActivity(context, 0, titleIntent, 0);
            views.setOnClickPendingIntent(R.id.widgetTitleLabel, titlePendingIntent);

            Intent clickIntentTemplate = new Intent(context, MainActivity.class);
            PendingIntent clickPendingIntentTemplate = TaskStackBuilder.create(context)
                    .addNextIntentWithParentStack(clickIntentTemplate)
                    .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setPendingIntentTemplate(R.id.widgetListView, clickPendingIntentTemplate);

            Intent intent = new Intent(context, MiriamsRemoteViewsService.class);
            views.setRemoteAdapter(R.id.widgetListView,intent);
            appWidgetManager.updateAppWidget(appWidgetId,views);
        }
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        super.onReceive(context, intent);
        final String action = intent.getAction();
        if (action.equals(RecipeDetailActivity.ACTION_RECIPE_UPDATED)) {
            // refresh all your widgets
            AppWidgetManager mgr = AppWidgetManager.getInstance(context);
            ComponentName cn = new ComponentName(context, MiriamsWidgetProvider.class);
            mgr.notifyAppWidgetViewDataChanged(mgr.getAppWidgetIds(cn), R.id.widgetListView);
        }

    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

