package com.example.android.bakingapp;

import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.Toast;

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
    private final String KEY_RECYCLER_STATE = "recycler_state";
    private Parcelable listState;
    private static final String RECIPE_NAME = "name";
    private static final String RECIPE_IMAGE = "image";
    private static final String TAG = "RecipeDetailFragment";
    private static final String RECIPE_INGREDIENTS = "ingredients";
    private static final String RECIPE_QUANTITY = "quantity";
    private static final String RECIPE_MEASURE = "measure";
    private static final String RECIPE_INGREDIENT = "ingredient";
    String mParam1;
    String strtext;

    private ArrayList<Recipes> mRecipeNamesList = new ArrayList();
    private ArrayList<Recipes> mImageUrlList = new ArrayList();
    private String recipeJSONStr;
    private RecipesAdapter mRecipesAdapter;

    public RecipeFragment() {

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_baking_recipes, container, false);
        final View rootView = binding.getRoot();
        RecyclerView.LayoutManager verticalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        if (isOnline()) {
            Connection connection = new Connection();
            connection.execute();
            binding.idRecyclerViewVerticaList.setLayoutManager(verticalLayoutManager);
        } else {
            try {
                new AlertDialog.Builder(getContext())
                        .setTitle("Internet Connection")
                        .setMessage("Please check your internet connection")
                        .setIcon(R.drawable.ic_internet)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return rootView;
    }
 /*
     * check for internet connection
     */

    private boolean isOnline() {
        ConnectivityManager conMgr = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if (netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()) {
            Toast.makeText(getContext(), "No Internet connection!", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (binding.idRecyclerViewVerticaList != null) {

            outState.putParcelable(KEY_RECYCLER_STATE, binding.idRecyclerViewVerticaList.getLayoutManager().onSaveInstanceState());
        }
    }


    public class Connection extends AsyncTask<Void, Void, String[]> {
        private final String LOG_TAG = RecipeFragment.Connection.class.getSimpleName();


        public String getRecipeIngredients(String recipeJson, String recipeName) throws JSONException {

            String getIngredients = "";
            JSONArray recipeJsonArray = new JSONArray(recipeJson);

            //get name and store it in an array
            for (int i = 0; i < recipeJsonArray.length(); i++) {

                JSONObject recipeDetails = recipeJsonArray.getJSONObject(i);

                //get recipe name
                String name = recipeDetails.getString(RECIPE_NAME);
                String imageUrl = recipeDetails.getString(RECIPE_IMAGE);

                if (name.equals(recipeName)) {

                    JSONArray ingredientsArray = recipeDetails.getJSONArray(RECIPE_INGREDIENTS);
                    Log.i(TAG, "ingredients: " + ingredientsArray);

                    //get recipe details
                    for (int j = 0; j < ingredientsArray.length(); j++) {

                        JSONObject ingredientDetails = ingredientsArray.getJSONObject(j);

                        //get Quantity
                        String quantity = ingredientDetails.getString(RECIPE_QUANTITY);

                        //get measure
                        String measure = ingredientDetails.getString(RECIPE_MEASURE);

                        //get ingredient
                        String ingredient = ingredientDetails.getString(RECIPE_INGREDIENT);

                        getIngredients = getIngredients + ingredient + ": " + quantity + " " + measure + "< ";
                    }

                    Log.i(TAG, "Ingredients: " + getIngredients);
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
                JSONArray response = new JSONArray(recipeJSONStr);
                String[] recipeArray = new String[response.length()];
                String[] recipeImageUrlArray = new String[response.length()];
                for (int i = 0; i < response.length(); i++) {

                    recipeArray[i] = response.getJSONObject(i).getString("name");
                    recipeImageUrlArray[i] = response.getJSONObject(i).getString("image");

                    JSONArray ingredientsArray = response.getJSONObject(i).getJSONArray("ingredients");

                    for (int j = 0; j < ingredientsArray.length(); j++) {
                        Double quantity = ingredientsArray.getJSONObject(j).getDouble("quantity");
                        String measure = ingredientsArray.getJSONObject(j).getString("measure");
                        String ingredient = ingredientsArray.getJSONObject(j).getString("ingredient");
                        String recipeIngredients = "" + quantity.toString() + " " + measure + ":" + ingredient;
                    }
                }

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
                mRecipesAdapter = new RecipesAdapter(mRecipeNamesList, recipeJSONStr, getContext());
                binding.idRecyclerViewVerticaList.setAdapter(mRecipesAdapter);
                mRecipesAdapter.notifyDataSetChanged();
            }
            super.onPostExecute(strings);
        }
    }
}
