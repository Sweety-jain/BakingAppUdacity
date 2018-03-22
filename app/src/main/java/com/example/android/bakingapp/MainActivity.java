package com.example.android.bakingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity implements RecipeFragment.OnTextClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.titleToolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Let's Bake with me");
    }

    @Override
    public void onTextSelected(int position) {

    }
}
