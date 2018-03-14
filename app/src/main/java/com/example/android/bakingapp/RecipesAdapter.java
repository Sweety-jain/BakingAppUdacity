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

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipesViewHolder> {
    private static final String RECIPE_NAME = "name";
    private static final String TAG = "RecipeAdapter";
    private static final String RECIPE_INGREDIENTS = "ingredients";
    private static final String RECIPE_QUANTITY = "quantity";
    private static final String RECIPE_MEASURE = "measure";
    private static final String RECIPE_INGREDIENT = "ingredient";
    private ArrayList<Recipes> verticalRecipesList;
    private Context context;
    private String recipeName;
    String recipeJSONStr;
    private int pos;
    String var[];

    public RecipesAdapter(ArrayList<Recipes> verticalList, String mrecipeJsonString, Context context) {
        this.verticalRecipesList = verticalList;
        this.recipeJSONStr = mrecipeJsonString;
        Log.d("inRecipeAdapter", "json string" + recipeJSONStr);
        this.context = context;
    }

    @Override
    public RecipesViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recipes_list_view_item, viewGroup, false);
        view.setTag(pos);
        return new RecipesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipesViewHolder holder, int i) {
        holder.recipeTextView.setTag(i);
        pos = i;
        //  holder.recipeTextView.setTag(i);
        recipeName = verticalRecipesList.get(i).getRecipeName();
        holder.recipeTextView.setText(recipeName);
    }

    @Override
    public int getItemCount() {
        return verticalRecipesList.size();
    }

    public class RecipesViewHolder extends RecyclerView.ViewHolder {
        Button recipeTextView;
        public  String getRecipeIngredients(String recipeJson,String recipeName) throws JSONException {

            String getIngredients = "";
            JSONArray recipeJsonArray = new JSONArray(recipeJson);

            //get name and store it in an array
            for (int i = 0; i < recipeJsonArray.length(); i++ ) {

                JSONObject recipeDetails = recipeJsonArray.getJSONObject(i);

                //get recipe name
                String name = recipeDetails.getString(RECIPE_NAME);

                if (name.equals(recipeName)) {

                    JSONArray ingredientsArray = recipeDetails.getJSONArray(RECIPE_INGREDIENTS);
                    Log.i(TAG,"ingredients: "+ingredientsArray);

                    //get recipe details
                    for (int j = 0; j < ingredientsArray.length(); j++ ){

                        JSONObject ingredientDetails = ingredientsArray.getJSONObject(j);

                        //get Quantity
                        String quantity = ingredientDetails.getString(RECIPE_QUANTITY);

                        //get measure
                        String measure = ingredientDetails.getString(RECIPE_MEASURE);

                        //get ingredient
                        String ingredient = ingredientDetails.getString(RECIPE_INGREDIENT);

                        getIngredients = getIngredients + ingredient + ": " + quantity + " " + measure + "\n ";
                    }

                    Log.i(TAG,"Ingredients: "+ getIngredients);
                    return getIngredients;
                }
            }

            return null;
        }

        public String[] getRecipeShortDesc(String recipeJson, String recipeName) throws JSONException{
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


                        getRecipeDetails[j] = shortDescription ;
                        Log.d("Steps of recipedetails",""+getRecipeDetails[j]);

                    }

                    return getRecipeDetails;

                }

            }
            return null;

        }

        public String[] getRecipeDesc(String recipeJson, String recipeName) throws JSONException{
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
                        String RECIPE_DESCRIPTION = "description";
                        String description = recipeSteps.getString(RECIPE_DESCRIPTION);


                        getRecipeDetails[j] = description ;
                        Log.d("Steps of recipedetails",""+getRecipeDetails[j]);

                    }

                    return getRecipeDetails;

                }

            }
            return null;

        }
        public String[] getRecipeThumbnail(String recipeJson, String recipeName) throws JSONException{
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
                        String RECIPE_THUMBNAILURL = "thumbnailURL";
                        String thumbnailUrl = recipeSteps.getString(RECIPE_THUMBNAILURL);


                        getRecipeDetails[j] = thumbnailUrl ;
                        Log.d("Steps of recipedetails",""+getRecipeDetails[j]);

                    }

                    return getRecipeDetails;

                }

            }
            return null;

        }
        public String[] getRecipeVideoUrl(String recipeJson, String recipeName) throws JSONException{
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
                        String RECIPE_VIDEOURL = "videoURL";
                        String videoUrl = recipeSteps.getString(RECIPE_VIDEOURL);


                        getRecipeDetails[j] = videoUrl ;
                        Log.d("Steps of recipedetails",""+getRecipeDetails[j]);

                    }

                    return getRecipeDetails;

                }

            }
            return null;

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
                                    videoURL + "> " + thumbnailUrl + "end";
                            Log.d("Steps of recipedetails",""+getRecipeDetails[j]);

                    }

                    return getRecipeDetails;

                }

            }
            return null;
        }

        public RecipesViewHolder(View itemView) {
            super(itemView);

            String s = Integer.toString(pos);
            recipeTextView = (Button) itemView.findViewById(R.id.recipeTextViewId);
            recipeTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = (int) recipeTextView.getTag();
                    String s = Integer.toString(position);
                    String ingredient;
                   String[] recipesteps;
                   String[] recipeStepDetails;
                   String[] recipeDesc;
                   String[] videoUrl;
                   String[] thumbNailUrl;
                    Intent intent = new Intent(view.getContext(), RecipeDetailActivity.class);
                    Bundle bundle = new Bundle();
                    try {
                        int i=0;
                        ingredient=getRecipeIngredients(recipeJSONStr,recipeTextView.getText().toString());
                        recipesteps =getRecipeShortDesc(recipeJSONStr,recipeTextView.getText().toString());
                        recipeDesc = getRecipeDesc(recipeJSONStr,recipeTextView.getText().toString());
                        videoUrl = getRecipeVideoUrl(recipeJSONStr,recipeTextView.getText().toString());
                        thumbNailUrl =getRecipeThumbnail(recipeJSONStr,recipeTextView.getText().toString());
                        recipeStepDetails=getRecipeDetails(recipeJSONStr,recipeTextView.getText().toString());
                        String recipeStepsDesc =Arrays.toString(recipeDesc);
                        String recipeVideoUrl = Arrays.toString(videoUrl);
                        String recipeThumbnail = Arrays.toString(thumbNailUrl);
                        String stepDetails  = Arrays.toString(recipeStepDetails);
                        String r = Arrays.toString(recipesteps);

                      Log.d("InREciepesteps",""+r);

                      //  bundle.putString("recipeTitle", recipeTextView.getText().toString());
                        //bundle.putString("jsonstring", ingredient);
                        //bundle.putStringArray("recipeSteps",recipesteps);
                     //  bundle.putString("recipesteps",recipesteps);
                        //Log.d("string", "recipetitle" + recipeTextView.getText());
                        intent.putExtra("recipeTitle", recipeTextView.getText());
                        intent.putExtra("jsonstring", ingredient);
                        intent.putExtra("recipeSteps",r);
                        intent.putExtra("recipeStepDesc",recipeStepsDesc);
                        intent.putExtra("recipeVideoUrl",recipeVideoUrl);
                        intent.putExtra("recipeThumbnail",recipeThumbnail);
                        intent.putExtra("recipeStepDetails",stepDetails);
                      //  intent.putExtra("recipeSteps",Arrays.toString(recipesteps));
                        Log.d("recipesteppppp",""+Arrays.toString(recipesteps));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

// set Fragmentclass Arguments
                    // RecipeDetailFragment fragobj = new RecipeDetailFragment();
                    //fragobj.setArguments(bundle);
                    //   Log.d("inRecipeAdapter","vertical list"+recipeJSONStr);

                    context.startActivity(intent);

                }
            });
        }

    }


}

