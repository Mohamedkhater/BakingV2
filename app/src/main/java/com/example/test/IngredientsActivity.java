package com.example.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class IngredientsActivity extends AppCompatActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients);
        Intent intent=getIntent();
        ArrayList<RecipeCard.RecipeIngredients> ingredients=intent.getParcelableArrayListExtra("ingredients");
        RecyclerView ingredientsRecyclerView= findViewById(R.id.ingredients_rv);
        ingredientsRecyclerView.setAdapter(new IngredientsAdapter(this,ingredients));
        ingredientsRecyclerView.setLayoutManager(new LinearLayoutManager(this));



    }
}
