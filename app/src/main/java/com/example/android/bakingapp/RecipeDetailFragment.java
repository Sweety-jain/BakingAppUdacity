package com.example.android.bakingapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by namit on 3/8/2018.
 */

public class RecipeDetailFragment extends Fragment {
    private ArrayList<RecipeSteps> mRecipeStepsList = new ArrayList();
    private static final String RECIPE_NAME = "name";
    private static final String TAG = "RecipeDetailFragment";
    private static final String RECIPE_INGREDIENTS = "ingredients";
    private static final String RECIPE_QUANTITY = "quantity";
    private static final String RECIPE_MEASURE = "measure";
    private static final String RECIPE_INGREDIENT = "ingredient";
    String mParam1;
    String strtext;
    String ingredientText;
    String stepsShortDesc;
    String description;
    String videoUrls;
    String thumbNails;
    String recipeStepDetails;
    private String recipeJSONStr;
    private RecipesAdapter recipeAdapter;
    private RecipeStepsAdapter recipeStepsAdapter;
    private RecyclerView recipesStepsRecyclerView;
    public RecipeDetailFragment(){

    }
  /*  public static RecipeDetailFragment newInstance(String param1) {
        RecipeDetailFragment fragment = new RecipeDetailFragment();
        Bundle args = new Bundle();
        args.putString("recipeTitle", param1);
     //   args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString("recipeTitle");
            Log.d("inoncreate",""+mParam1);
         //   mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }*/


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_recipe_details, container, false);
    //Connection connection = new Connection();
        RecyclerView.LayoutManager verticalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        recipesStepsRecyclerView = (RecyclerView) rootView.findViewById(R.id.idRecyclerViewRecipeStepsList);
        recipesStepsRecyclerView.setLayoutManager(verticalLayoutManager);
        Log.d("RecipeDetailFragment","this");

        if(getArguments()!= null) {
         strtext = getArguments().getString("recipeTitle");
         ingredientText = getArguments().getString("jsonstring");
        stepsShortDesc = getArguments().getString("recipeSteps");
        description = getArguments().getString("recipeStepDesc");
            videoUrls = getArguments().getString("recipeVideoUrl");
            thumbNails = getArguments().getString("recipeThumbnail");
            recipeStepDetails = getArguments().getString("recipeStepDetails");
        int i =stepsShortDesc.length();

        String s = String.valueOf(i);
            String[] recipeInfo = stepsShortDesc.split(",");

           // Log.d("Sweety",""+recipeStepsDetails);
        //String[] array;

            for (String recipesteps : recipeInfo) {
                while (recipesteps != null) {
                    mRecipeStepsList.add(new RecipeSteps(recipesteps));
                    break;
                }
           }
            recipeStepsAdapter = new RecipeStepsAdapter(mRecipeStepsList,recipeJSONStr,recipeStepDetails,getContext());
            recipesStepsRecyclerView.setAdapter(recipeStepsAdapter);
            recipeStepsAdapter.notifyDataSetChanged();
            Log.d("ssssssss", ""+s);
           // if (strtext == null) {
             //   Log.d("ssssssss", "" + strtext);
            //}
         //   RecipeStepsAdapter recipeStepsAdapter = new RecipeStepsAdapter();
           TextView tv = (TextView)rootView.findViewById(R.id.recipeStepTVId);
            tv.setMovementMethod(new ScrollingMovementMethod());
            tv.setText(ingredientText);
        }
Log.d(TAG,"recipestring"+recipeJSONStr);
        /*connection.execute();
        try {
         String ingredients =  connection.getRecipeIngredients(recipeJSONStr,strtext);
         Log.d(TAG,""+ingredients);
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
        //  TextView recipeTitleTextView = (TextView)rootView.findViewById(R.id.recipeTitleTextViewId);
        //recipeTitleTextView.setText(strtext);
        return rootView;

    }


    /**
     * get recipe details from json
     * @param recipeJson
     * @return
     * @throws JSONException
     */

    @SuppressWarnings("JavaDoc")
    public static String[] getRecipeDetails(String recipeJson, String recipeName) throws JSONException{

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

                int stepLength = steps.length()+1;
                Log.i(TAG,"stepLength: "+ stepLength);

                getRecipeDetails = new String[stepLength];

                //get recipe details
                for (int j = 0; j < stepLength; j++ ){

                    if (j == 0){

                        JSONArray ingredientsArray = recipeDetails.getJSONArray(RECIPE_INGREDIENTS);
                        Log.i(TAG,"ingredients: "+ingredientsArray);

                        int ingredientsLength = ingredientsArray.length();

                        String ingredients = "";

                        for (int k = 0 ; k < ingredientsLength ; k++){
                            JSONObject ingredientDetails = ingredientsArray.getJSONObject(k);

                            //get Quantity
                            String quantity = ingredientDetails.getString(RECIPE_QUANTITY);

                            //get measure
                            String measure = ingredientDetails.getString(RECIPE_MEASURE);

                            //get ingredient
                            String ingredient = ingredientDetails.getString(RECIPE_INGREDIENT);

                            ingredients = ingredients + ": " + quantity + "< "
                                    + measure + "< " + ingredient;
                        }
                        getRecipeDetails[j] = "Ingredients" +"> " + ingredients +"> " + " ";


                    }else{

                        JSONObject recipeSteps = steps.getJSONObject(j-1);

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
                    }
                }

                return getRecipeDetails;

            }

        }
        return null;
    }

/*    public class Connection extends AsyncTask<Void, Void, String[]> {
        private final String LOG_TAG = RecipeFragment.Connection.class.getSimpleName();
        public  String getRecipeIngredients(String recipeJson,String recipeName) throws JSONException{

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

                        getIngredients = getIngredients + ingredient + ": " + quantity + " " + measure + "< ";
                    }

                    Log.i(TAG,"Ingredients: "+ getIngredients);
                    return getIngredients;
                }
            }

            return null;
        }

        @Override
        protected String[] doInBackground(Void... voids) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL("https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();

                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;

                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }
                recipeJSONStr = buffer.toString();
                //  final String name = "name";
                // JSONObject moviesJson = new JSONObject(recipeJSONStr);
                // String recipeName = moviesJson.getString("name");

                JSONArray response = new JSONArray(recipeJSONStr);
                String[] recipeArray =new String[response.length()];
                for (int i = 0; i < response.length(); i++) {

                    recipeArray[i]= response.getJSONObject(i).getString("name");
                }
                //    int l = moviesArrayForJson.length();
                //  String s = String.valueOf(l);
                //String[] reviews = new String[moviesArrayForJson.length()];
              /*  for (int i = 0; i < moviesArrayForJson.length(); i++) {
                    String review;
                    JSONObject movieReview = moviesArrayForJson.getJSONObject(i);
                    reviews[i] = movieReview.getString("content");
                    Log.v("ReviewConnectionsss", "review" + reviews[i]);

                }*/

/*
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }


            }
            return null;
        }

        @Override
        protected void onPostExecute(String[] strings) {
            if (strings != null) {
                for (String recipeNamess : strings) {
                    while (recipeNamess != null) {
                        mRecipeNamesList.add(new Recipes(recipeNamess));
                        break;
                    }
                }
                mRecipesAdapter = new RecipesAdapter(mRecipeNamesList,recipeJSONStr,getContext());
                recipesRecyclerView.setAdapter(mRecipesAdapter);
                mRecipesAdapter.notifyDataSetChanged();
               // mRecipesAdapter.notifyDataSetChanged();
            }
            super.onPostExecute(strings);
        }
    }*/
}
