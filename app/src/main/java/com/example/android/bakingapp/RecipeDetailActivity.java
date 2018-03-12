package com.example.android.bakingapp;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toolbar;

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
    private ArrayList<Recipes> mRecipeNamesList = new ArrayList();
    private static final String RECIPE_NAME = "name";
    private static final String TAG = "RecipeDetailFragment";
    private static final String RECIPE_INGREDIENTS = "ingredients";
    private static final String RECIPE_QUANTITY = "quantity";
    private static final String RECIPE_MEASURE = "measure";
    private static final String RECIPE_INGREDIENT = "ingredient";
    String mParam1;
    String strtext;
    private String recipeJSONStr;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        Intent iin= getIntent();
        Bundle b = iin.getExtras();
        String j;
        String jsonStringToParse;
        String steps;
        String stepDetails;
        String videoUrl;
        String description;
        String thumbnailUrl;
//
             j =(String) b.get("recipeTitle");
             jsonStringToParse = (String)b.get("jsonstring") ;
             steps = (String)b.get("recipeSteps") ;
            stepDetails = (String)b.get("recipeStepDetails");
        description = (String)b.get("recipeStepDesc") ;
        videoUrl = (String)b.get("recipeVideoUrl") ;
        thumbnailUrl = (String)b.get("recipeThumbnail") ;
        Log.d("videoUrl",""+videoUrl);

       // String[] recipeShortDesc = steps.split(",");

          //  String[] recipeSteps = iin.getStringArrayExtra("recipesteps");
          //   Log.d("InRecipeDetailsActivity",""+ recipeShortDesc[1]);

        android.support.v7.widget.Toolbar toolbar =(android.support.v7.widget.Toolbar) findViewById(R.id.titleStepToolbar);
        toolbar.setTitle(j);
                RecipeDetailFragment recipeTitleFragment =new RecipeDetailFragment();
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        recipeTitleFragment.setArguments(b);
        fragmentManager.beginTransaction()
                .add(R.id.title_container, recipeTitleFragment)
                .commit();




     /*   Bundle extras = getIntent().getExtras();
        if(extras!=null){
            Log.d("RecipeDetailActivity","extras:"+extras.getString("recipeTitle"));
        }
        Bundle bundle = new Bundle();
        bundle.putString("recipeTitle", extras.getString("recipeTitle"));*/
// set Fragmentclass Arguments
    //    Log.d("inRecipeDetailActivity",""+bundle.getString("recipeTitle"));
      //  RecipeDetailFragment fragment = (RecipeDetailFragment) getFragmentManager().findFragmentById(R.id.recipe_detail_fragment);
       // RecipeDetailFragment frag = RecipeDetailFragment.newInstance(extras.getString("recipeTitle"));
      //  RecipeDetailFragment fragobj = new RecipeDetailFragment();
      //  fragobj.setArguments(bundle);
    }

    public class Connection extends AsyncTask<Void, Void, String[]> {
        private final String LOG_TAG = RecipeFragment.Connection.class.getSimpleName();
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

                return recipeArray;

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
                // mRecipesAdapter.notifyDataSetChanged();
            }
            super.onPostExecute(strings);
        }
    }
}
