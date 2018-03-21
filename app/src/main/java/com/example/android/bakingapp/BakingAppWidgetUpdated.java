package com.example.android.bakingapp;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by sweety on 3/21/2018.
 */


public class BakingAppWidgetUpdated extends IntentService {

    private static final String ACTION_UPDATE_RECIPE_WIDGETS = "com.example.android.bakingapp.update_baking_app_widget";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public BakingAppWidgetUpdated() {
        super("BakingAppWidgetUpdated");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        assert intent != null;
        final String action = intent.getAction();
        if (ACTION_UPDATE_RECIPE_WIDGETS.equals(action)) {
            handleActionUpdateRecipeWidgets();
        }

    }

    private void handleActionUpdateRecipeWidgets() {

        String title = "";
        String ingredients = "";
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        title = prefs.getString("recipeTitle", null);
        ingredients = prefs.getString("ingredient", null);
        Log.d("Stringtitle", "" + title);


        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(
                new ComponentName(this, BakingAppWidget.class));

        //Now update all widgets
        BakingAppWidget.updateRecipeWidgets(this, appWidgetManager, title,
                ingredients, appWidgetIds);
    }


    public static void startActionUpdateRecipeWidgets(Context context) {
        Intent intent = new Intent(context, BakingAppWidgetUpdated.class);
        intent.setAction(ACTION_UPDATE_RECIPE_WIDGETS);
        context.startService(intent);
    }
}
