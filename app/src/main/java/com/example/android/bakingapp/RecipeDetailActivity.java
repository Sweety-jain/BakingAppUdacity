package com.example.android.bakingapp;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
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
import com.example.android.bakingapp.databinding.ActivityRecipeDetailBindingSw600dpImpl;

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
    ActivityRecipeDetailBindingSw600dpImpl bindingSw600dp;
    private ArrayList<Recipes> mRecipeNamesList = new ArrayList();
    private static final String RECIPE_NAME = "name";
    private static final String TAG = "RecipeDetailFragment";
    private static final String RECIPE_INGREDIENTS = "ingredients";
    private static final String RECIPE_QUANTITY = "quantity";
    private static final String RECIPE_MEASURE = "measure";
    private static final String RECIPE_INGREDIENT = "ingredient";
    public static boolean mTwoPane;
    String mParam1;
    String strtext;
    private String recipeJSONStr;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  binding = DataBindingUtil.setContentView(this, R.layout.activity_recipe_detail);
       setContentView(R.layout.activity_recipe_detail);
//
        if(savedInstanceState == null) {
            if (findViewById(R.id.step_container)!= null) {
                Log.d("tabview", "mtwopane" + mTwoPane);
                mTwoPane = true;
            } else {
                Log.d("tabview", "notabview" + mTwoPane);
                mTwoPane = false;
            }
        }
        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        String j;
        String jsonStringToParse;
        String steps;
        String stepDetails;
        String videoUrl;
        String description;
        String thumbnailUrl;
        j = (String) b.get("recipeTitle");
        jsonStringToParse = (String) b.get("jsonstring");
        steps = (String) b.get("recipeSteps");
        stepDetails = (String) b.get("recipeStepDetails");
        description = (String) b.get("recipeStepDesc");
        videoUrl = (String) b.get("recipeVideoUrl");
        thumbnailUrl = (String) b.get("recipeThumbnail");
        Log.d("videoUrl", "" + videoUrl);
        if (findViewById(R.id.step_container)!=null) {
            mTwoPane = true;

            //  binding.titleStepToolbar.setTitle(j);
            // toolbar.setTitle(j);
            RecipeDetailFragment recipeTitleFragment = new RecipeDetailFragment();
            android.support.v4.app.FragmentManager ftManager = getSupportFragmentManager();
            recipeTitleFragment.setArguments(b);

            ftManager.beginTransaction()
                    .add(R.id.title_container, recipeTitleFragment)
                    .commit();
         //            recipestepFragment.setArguments(b);
//
//            fragmentManager.beginTransaction()
//                    .add(R.id.step_container, recipestepFragment).commit();
//            RecipeDetailStepsFragment recipestepFragment = new RecipeDetailStepsFragment();
//            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
//            recipestepFragment.setArguments(b);
//
//            fragmentManager.beginTransaction()
//                    .add(R.id.step_container, recipestepFragment).commit();

        } else {
            mTwoPane = false;
            binding.titleStepToolbar.setTitle(j);

            //  binding.titleStepToolbar.setTitle(j);
            // toolbar.setTitle(j);
            RecipeDetailFragment recipeTitleFragment = new RecipeDetailFragment();
            android.support.v4.app.FragmentManager ftManager = getSupportFragmentManager();
            recipeTitleFragment.setArguments(b);
            ftManager.beginTransaction()
                    .add(R.id.title_container, recipeTitleFragment)
                    .commit();
        }


    }



}



