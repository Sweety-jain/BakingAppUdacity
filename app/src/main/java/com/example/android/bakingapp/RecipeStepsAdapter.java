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
    private int pos;
    String var[];

    public RecipeStepsAdapter(ArrayList<RecipeSteps> verticalList, String mrecipeJsonString,String recipeStepDetails, Context context) {
        this.mRecipeStepDetails = recipeStepDetails;
        this.verticalRecipesList = verticalList;
        this.recipeJSONStr = mrecipeJsonString;
        Log.d("inRecipeAdapter", "steps" + mRecipeStepDetails);
        this.context = context;
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
        //  holder.recipeTextView.setTag(i);
        recipeName = verticalRecipesList.get(i).getRecipeStepName();
        holder.recipeStepTextView.setText(recipeName);
    }

    @Override
    public int getItemCount() {
        return verticalRecipesList.size();
    }


    @SuppressWarnings("JavaDoc")
    public  String[] getRecipeDetails(String recipeJson, String recipeName) throws JSONException{

        String[] getRecipeDetails;

        JSONArray recipeJsonArray = new JSONArray(recipeJson);

        //get name and store it in an array
        for (int i = 0; i < recipeJsonArray.length(); i++ ){

            JSONObject recipeDetails = recipeJsonArray.getJSONObject(i);

            //get recipe name
            String name = recipeDetails.getString(RECIPE_NAME);

            if (name.equals(recipeName)){

                String RECIPE_STEPS = "steps";
                JSONArray steps = recipeDetails.getJSONArray(RECIPE_STEPS);
                Log.i(TAG,"steps: "+ steps);

                int stepLength = steps.length();
                Log.i(TAG,"stepLength: "+ stepLength);

                getRecipeDetails = new String[stepLength];

                //get recipe details
                for (int j = 0; j < stepLength; j++ ){



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

                    getRecipeDetails[j] = shortDescription + "> " + description + "> "+
                            videoURL + "> " + thumbnailUrl;
                    Log.d("Steps of recipedetails",""+getRecipeDetails[j]);

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
            int i=0;
            final String[] recipeSteps = mRecipeStepDetails.split("end");

            // Log.d("Sweety",""+recipeStepsDetails);
            //String[] array;

         //  for(i=0;i<recipeSteps.length;i++){
            //   Log.d("InRecipeStepViewHolder",""+ Arrays.toString(recipeSteps));
           //}
            String s = Integer.toString(pos);
            recipeStepTextView = (Button) itemView.findViewById(R.id.recipeStepButtonId);
            Log.d("recipeStringng",""+recipeJSONStr);
            recipeStepTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = (int) recipeStepTextView.getTag();
                    String s = Integer.toString(position);
                    Log.d("stepclicked",""+recipeSteps[position]);
                    Intent intent = new Intent(view.getContext(),RecipeDetailStepActivity.class);
                    intent.putExtra("recipeStepTitle",recipeStepTextView.getText().toString());
                    intent.putExtra("recipeStepDetails",recipeSteps[position]);
                    context.startActivity(intent);
                    //getRecipeDetails(recipeJSONStr,)
//                    try {
//                     //   getRecipeDetails(recipeJSONStr,recipeStepTextView.getText().toString());
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
                 //   Intent intent = new Intent(view.getContext(), RecipeStepActivity.class);
                   // Bundle bundle = new Bundle();
                  /*  String ingredient;
                    Intent intent = new Intent(view.getContext(), RecipeDetailActivity.class);
                    Bundle bundle = new Bundle();
                    try {
                        ingredient=getRecipeIngredients(recipeJSONStr,recipeTextView.getText().toString());
                        bundle.putString("recipeTitle", recipeTextView.getText().toString());
                        bundle.putString("jsonstring", ingredient);
                        Log.d("string", "recipetitle" + recipeTextView.getText());
                        intent.putExtra("recipeTitle", recipeTextView.getText());
                        intent.putExtra("jsonstring", ingredient);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }*/

// set Fragmentclass Arguments
                    // RecipeDetailFragment fragobj = new RecipeDetailFragment();
                    //fragobj.setArguments(bundle);
                    //   Log.d("inRecipeAdapter","vertical list"+recipeJSONStr);

                   // context.startActivity(intent);

                }
            });
        }

    }


}

