package com.example.android.bakingapp;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.android.bakingapp.databinding.ActivityRecipeDetailBinding;
import com.example.android.bakingapp.databinding.ActivityRecipeDetailStepBinding;

public class RecipeDetailStepActivity extends AppCompatActivity {
    ActivityRecipeDetailStepBinding binding;
    RecipeDetailStepsFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail_step);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recipe_detail_step);
        Intent myIntent = getIntent();
        Bundle b = myIntent.getExtras();
        String title = b.getString("recipeStepTitle");
        int position = b.getInt("adapterposition");
        String pos = String.valueOf(position);
        setToolbarTitle(title);
        if (savedInstanceState == null) {
            RecipeDetailStepsFragment recipestepFragment = new RecipeDetailStepsFragment();
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            recipestepFragment.setArguments(b);
            fragmentManager.beginTransaction()
                    .add(R.id.step_container, recipestepFragment).commit();
        }
    }

    public void setToolbarTitle(String title) {
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.titleStepToolbar);
        binding.titleStepToolbar.setTitle(title);

    }
}
