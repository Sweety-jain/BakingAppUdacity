package com.example.android.bakingapp;

import android.databinding.DataBindingUtil;
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

import com.example.android.bakingapp.databinding.FragmentRecipeDetailsBinding;
import com.example.android.bakingapp.databinding.RecipeStepCardBinding;

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
    FragmentRecipeDetailsBinding binding;
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
  //  private RecyclerView recipesStepsRecyclerView;
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
       // final View rootView = inflater.inflate(R.layout.fragment_recipe_details, container, false);
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_recipe_details,container,false);
        //  final View rootView = inflater.inflate(R.layout.fragment_activity_recipe_detail_steps, container, false);
        final View rootView = binding.getRoot();
    //Connection connection = new Connection();
        RecyclerView.LayoutManager verticalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        binding.idRecyclerViewRecipeStepsList.setLayoutManager(verticalLayoutManager);
       // recipesStepsRecyclerView = (RecyclerView) rootView.findViewById(R.id.idRecyclerViewRecipeStepsList);
        //recipesStepsRecyclerView.setLayoutManager(verticalLayoutManager);
        Log.d("RecipeDetailFragment","this");
        boolean mTwoPane =RecipeDetailActivity.mTwoPane;
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

            recipeStepsAdapter = new RecipeStepsAdapter(mTwoPane,mRecipeStepsList,recipeJSONStr,recipeStepDetails,getContext());
         //   recipesStepsRecyclerView.setAdapter(recipeStepsAdapter);
            binding.idRecyclerViewRecipeStepsList.setAdapter(recipeStepsAdapter);
            recipeStepsAdapter.notifyDataSetChanged();
            Log.d("ssssssss", ""+s);
           // if (strtext == null) {
             //   Log.d("ssssssss", "" + strtext);
            //}
         //   RecipeStepsAdapter recipeStepsAdapter = new RecipeStepsAdapter();
          // TextView tv = (TextView)rootView.findViewById(R.id.recipeStepTVId);
           binding.recipeStepTVId.setMovementMethod(new ScrollingMovementMethod());
           // tv.setMovementMethod(new ScrollingMovementMethod());
            binding.recipeStepTVId.setText(ingredientText);
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




}
