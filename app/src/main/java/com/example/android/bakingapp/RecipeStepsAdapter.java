package com.example.android.bakingapp;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by sweety on 2/8/2018.
 */

public class RecipeStepsAdapter extends RecyclerView.Adapter<RecipeStepsAdapter.RecipesStepsViewHolder> {
    private static final String RECIPE_NAME = "name";
    private static final String TAG = "RecipeDetailFragment";
    private static final String RECIPE_INGREDIENTS = "ingredients";
    private static final String RECIPE_QUANTITY = "quantity";
    private static final String RECIPE_MEASURE = "measure";
    private static final String RECIPE_INGREDIENT = "ingredient";
    private ArrayList<RecipeSteps> verticalRecipesList;
    private Context context;
    private String recipeName;
    String mRecipeStepDetails;
    String recipeJSONStr;
    String mimageUrl;
    private int pos;
    String var[];
    Boolean mtabView;

    public RecipeStepsAdapter(Boolean tabView, String imageUrl, ArrayList<RecipeSteps> verticalList, String mrecipeJsonString, String recipeStepDetails, Context context) {
        this.mRecipeStepDetails = recipeStepDetails;
        this.verticalRecipesList = verticalList;
        this.recipeJSONStr = mrecipeJsonString;
        this.mimageUrl = imageUrl;
        this.context = context;
        this.mtabView = tabView;
    }

    @Override
    public RecipesStepsViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recipe_step_card, viewGroup, false);
        view.setTag(pos);
        return new RecipesStepsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipesStepsViewHolder holder, int i) {
        holder.recipeStepTextView.setTag(i);
        pos = i;
        recipeName = verticalRecipesList.get(i).getRecipeStepName();
        holder.recipeStepTextView.setText(recipeName);

    }

    @Override
    public int getItemCount() {
        return verticalRecipesList.size();
    }

    @SuppressWarnings("JavaDoc")
    public String[] getRecipeDetails(String recipeJson, String recipeName) throws JSONException {

        String[] getRecipeDetails;

        JSONArray recipeJsonArray = new JSONArray(recipeJson);

        //get name and store it in an array
        for (int i = 0; i < recipeJsonArray.length(); i++) {

            JSONObject recipeDetails = recipeJsonArray.getJSONObject(i);

            //get recipe name
            String name = recipeDetails.getString(RECIPE_NAME);

            if (name.equals(recipeName)) {

                String RECIPE_STEPS = "steps";
                JSONArray steps = recipeDetails.getJSONArray(RECIPE_STEPS);
                Log.i(TAG, "steps: " + steps);

                int stepLength = steps.length();
                Log.i(TAG, "stepLength: " + stepLength);

                getRecipeDetails = new String[stepLength];

                //get recipe details
                for (int j = 0; j < stepLength; j++) {
                    JSONObject recipeSteps = steps.getJSONObject(j);

                    //get short description
                    String RECIPE_SHORT_DESCRIPTION = "shortDescription";
                    String shortDescription = recipeSteps.getString(RECIPE_SHORT_DESCRIPTION);

                    //get description
                    String RECIPE_DESCRIPTION = "description";
                    String description = recipeSteps.getString(RECIPE_DESCRIPTION);

                    //get videoUrl
                    String RECIPE_VIDEO_URL = "videoURL";
                    String videoURL = recipeSteps.getString(RECIPE_VIDEO_URL);

                    //get thumbnail url
                    String THUMBNAIL_URL = "thumbnailURL";
                    String thumbnailUrl = recipeSteps.getString(THUMBNAIL_URL);

                    getRecipeDetails[j] = shortDescription + "> " + description + "> " +
                            videoURL + "> " + thumbnailUrl;
                    getRecipeDetails[j].replaceAll("", "");

                }

                return getRecipeDetails;

            }

        }
        return null;
    }

    public class RecipesStepsViewHolder extends RecyclerView.ViewHolder {
        Button recipeStepTextView;

        public RecipesStepsViewHolder(View itemView) {
            super(itemView);
            int i = 0;
            final String[] recipeSteps = mRecipeStepDetails.split("end");
            final RecipeDetailActivity myParentActivity = (RecipeDetailActivity) context;

            String s = Integer.toString(pos);
            recipeStepTextView = (Button) itemView.findViewById(R.id.recipeStepButtonId);
            recipeStepTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = (int) recipeStepTextView.getTag();
                    String s = Integer.toString(position);
                    if (mtabView == false) {
                        Intent intent = new Intent(view.getContext(), RecipeDetailStepActivity.class);
                        intent.putExtra("recipeStepTitle", recipeStepTextView.getText().toString());
                        intent.putExtra("recipeStepDetails", recipeSteps[position]);
                        intent.putExtra("recipestepsarray", recipeSteps);
                        intent.putExtra("adapterposition", position);

                        context.startActivity(intent);
                    } else {
                        Bundle b = new Bundle();
                        b.putString("recipeStepTitle", recipeStepTextView.getText().toString());
                        b.putString("recipeStepDetails", recipeSteps[position]);
                        b.putStringArray("recipestepsarray", recipeSteps);
                        b.putInt("adapterposition", position);
                        b.putBoolean("flag", RecipeDetailActivity.flag);
                        RecipeDetailActivity.flag = false;

                        RecipeDetailStepsFragment recipestepFragment = new RecipeDetailStepsFragment();
                        android.support.v4.app.FragmentManager fragmentManager = myParentActivity.getSupportFragmentManager();
                        recipestepFragment.setArguments(b);
                        fragmentManager.beginTransaction()
                                .replace(R.id.step_container, recipestepFragment).commit();
                    }


                }
            });
        }

    }


}

