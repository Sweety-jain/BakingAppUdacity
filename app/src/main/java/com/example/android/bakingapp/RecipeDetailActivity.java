package com.example.android.bakingapp;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.android.bakingapp.databinding.ActivityRecipeDetailBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class RecipeDetailActivity extends AppCompatActivity {
    ActivityRecipeDetailBinding binding;
    private ArrayList<Recipes> mRecipeNamesList = new ArrayList();
    private static final String RECIPE_NAME = "name";
    private static final String TAG = "RecipeDetailFragment";
    private static final String RECIPE_INGREDIENTS = "ingredients";
    private static final String RECIPE_QUANTITY = "quantity";
    private static final String RECIPE_MEASURE = "measure";
    private static final String RECIPE_INGREDIENT = "ingredient";
    public static boolean mTwoPane;
    public static boolean flag;
    String mParam1;
    String strtext;
    private String recipeJSONStr;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recipe_detail);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        String j;
        String jsonStringToParse;
        String steps;
        String stepDetails;
        String videoUrl;
        String description;
        String thumbnailUrl;
        String ingredient;
        j = (String) b.get("recipeTitle");
        ingredient = (String)b.get("ingredient");
        jsonStringToParse = (String) b.get("jsonstring");
        steps = (String) b.get("recipeSteps");
        stepDetails = (String) b.get("recipeStepDetails");
        description = (String) b.get("recipeStepDesc");
        String[] descArray = description.split(",");

        videoUrl = (String) b.get("recipeVideoUrl");
        String[] videoUrlArray = videoUrl.split(",");

        thumbnailUrl = (String) b.get("recipeThumbnail");
        editor.putString("recipeTitle",j);
        editor.putString("ingredient",ingredient);
        //editor.apply();
        editor.commit();
        BakingAppWidgetUpdated.startActionUpdateRecipeWidgets(RecipeDetailActivity.this);
        String restoredText = sharedpreferences.getString("recipeTitle", null);
        String ingredients = sharedpreferences.getString("ingredient", null);
        Log.d("restored text",""+restoredText);
        Log.d("ingredientsinsf",""+ingredients);
        if (savedInstanceState == null) {
            if (binding.stepContainer != null) {
                mTwoPane = true;
                Log.d("in two pane", "mtwopane");
                RecipeDetailFragment recipeTitleFragment = new RecipeDetailFragment();
                android.support.v4.app.FragmentManager ftManager = getSupportFragmentManager();
                recipeTitleFragment.setArguments(b);
                ftManager.beginTransaction()
                        .add(R.id.title_container, recipeTitleFragment)
                        .commit();
                flag = true;
                RecipeDetailStepsFragment recipeDetailStepsFragment = new RecipeDetailStepsFragment();
                android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
                Bundle bundle = new Bundle();
                bundle.putBoolean("mTwoPane", mTwoPane);
                bundle.putString("recipeDescription", String.valueOf(descArray[0]).replace("[", ""));
                bundle.putString("recipeVideoUrl", String.valueOf(videoUrlArray[0]).replace("[", ""));
                recipeDetailStepsFragment.setArguments(bundle);
                fm.beginTransaction().add(R.id.step_container, recipeDetailStepsFragment).commit();
            } else {
                mTwoPane = false;
                binding.titleStepToolbar.setTitle(j);
                RecipeDetailFragment recipeTitleFragment = new RecipeDetailFragment();
                android.support.v4.app.FragmentManager ftManager = getSupportFragmentManager();
                recipeTitleFragment.setArguments(b);
                ftManager.beginTransaction()
                        .add(R.id.title_container, recipeTitleFragment)
                        .commit();
            }
        }


    }


}



