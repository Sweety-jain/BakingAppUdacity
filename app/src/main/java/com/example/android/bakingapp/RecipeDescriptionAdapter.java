
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
        import java.util.List;

/**
 * Created by sweety on 2/8/2018.
 */

public class RecipeDescriptionAdapter extends RecyclerView.Adapter<RecipeDescriptionAdapter.RecipesDescriptionViewHolder> {
    private static final String RECIPE_NAME = "name";
    private static final String TAG = "RecipeDetailFragment";
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

    public RecipeDescriptionAdapter(ArrayList<Recipes> verticalList, String mrecipeJsonString, Context context) {
        this.verticalRecipesList = verticalList;
        this.recipeJSONStr = mrecipeJsonString;
        Log.d("inRecipeAdapter", "json string" + recipeJSONStr);
        this.context = context;
    }

    @Override
    public RecipesDescriptionViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recipes_list_view_item, viewGroup, false);
        view.setTag(pos);
        return new RecipesDescriptionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipesDescriptionViewHolder holder, int i) {
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

    public class RecipesDescriptionViewHolder extends RecyclerView.ViewHolder {
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

                        getIngredients = getIngredients  + ingredient + ": " + quantity + " " + measure + "\n ";
                    }

                    Log.i(TAG,"Ingredients: "+ getIngredients);
                    return getIngredients;
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

        public RecipesDescriptionViewHolder(View itemView) {
            super(itemView);

            String s = Integer.toString(pos);
            recipeTextView = (Button) itemView.findViewById(R.id.recipeTextViewId);
            recipeTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = (int) recipeTextView.getTag();
                    String s = Integer.toString(position);
                    String ingredient;
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

