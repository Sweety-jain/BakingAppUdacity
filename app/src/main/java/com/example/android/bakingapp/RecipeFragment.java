package com.example.android.bakingapp;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.example.android.bakingapp.databinding.FragmentBakingRecipesBinding;

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

/**
 * Created by sweety on 3/7/2018.
 */

public class RecipeFragment extends Fragment {
    FragmentBakingRecipesBinding binding;
   // private RecyclerView recipesRecyclerView;
  //  private ArrayList<Recipes> mRecipeNamesList = new ArrayList();
    private static final String RECIPE_NAME = "name";
    private static final String TAG = "RecipeDetailFragment";
    private static final String RECIPE_INGREDIENTS = "ingredients";
    private static final String RECIPE_QUANTITY = "quantity";
    private static final String RECIPE_MEASURE = "measure";
    private static final String RECIPE_INGREDIENT = "ingredient";
    String mParam1;
    String strtext;
    private ArrayList<Recipes> mRecipeNamesList = new ArrayList();
    private String recipeJSONStr;
    private RecipesAdapter mRecipesAdapter;
    public RecipeFragment(){

    }

    // Define a new interface OnImageClickListener that triggers a callback in the host activity
    OnTextClickListener mCallback;

    // OnImageClickListener interface, calls a method in the host activity named onImageSelected
    public interface OnTextClickListener {
        void onTextSelected(int position);
    }

    // Override onAttach to make sure that the container activity has implemented the callback
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the host activity has implemented the callback interface
        // If not, it throws an exception
        try {
            mCallback = (OnTextClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnImageClickListener");
        }
    }



    // Inflates the GridView of all AndroidMe images
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_baking_recipes,container,false);
        //  final View rootView = inflater.inflate(R.layout.fragment_activity_recipe_detail_steps, container, false);
        final View rootView = binding.getRoot();
       // final View rootView = inflater.inflate(R.layout.fragment_baking_recipes, container, false);

        // Get a reference to the GridView in the fragment_master_list xml layout file
        RecyclerView.LayoutManager verticalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

     //   recipesRecyclerView = (RecyclerView) rootView.findViewById(R.id.idRecyclerViewVerticaList);

        Connection connection = new Connection();
        connection.execute();
binding.idRecyclerViewVerticaList.setLayoutManager(verticalLayoutManager);
        // Create the adapter
        // This adapter takes in the context and an ArrayList of ALL the image resources to display
        Log.d("InRecipeFragment","jsonstrimng: "+recipeJSONStr);
        Log.d("InRecipeFragment","recipe names: "+mRecipeNamesList);

     //   MasterListAdapter mAdapter = new MasterListAdapter(getContext(), AndroidImageAssets.getAll());

        // Set the adapter on the GridView
        //gridView.setAdapter(mAdapter);

        // Set a click listener on the gridView and trigger the callback onImageSelected when an item is clicked
     /*   gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Trigger the callback method and pass in the position that was clicked
                mCallback.onImageSelected(position);
            }
        });

        // Return the root view*/
        return rootView;
    }


    public class Connection extends AsyncTask<Void, Void, String[]>{
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
               // String[][] ingArray = new String[response.length()][];
                for (int i = 0; i < response.length(); i++) {

                    recipeArray[i]= response.getJSONObject(i).getString("name");
                    JSONArray ingredientsArray= response.getJSONObject(i).getJSONArray("ingredients");
                    // ingArray = new String[response.length()][ingredientsArray.length()];
                    for(int j = 0;j<ingredientsArray.length();j++){
                        Double quantity = ingredientsArray.getJSONObject(j).getDouble("quantity");
                        String measure = ingredientsArray.getJSONObject(j).getString("measure");
                        String ingredient = ingredientsArray.getJSONObject(j).getString("ingredient");
                        String recipeIngredients = ""+quantity.toString() + " "+measure + ":" +ingredient;
                      //  ingArray[i][j]= recipeIngredients;

                    }
                  //  Log.d("recipeIngredients",""+ingArray[i][1]);
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
                Log.d("sweety","jsonstring"+recipeJSONStr);
                mRecipesAdapter = new RecipesAdapter(mRecipeNamesList,recipeJSONStr,getContext());
                binding.idRecyclerViewVerticaList.setAdapter(mRecipesAdapter);
                mRecipesAdapter.notifyDataSetChanged();
            }
            super.onPostExecute(strings);
        }
    }
}
