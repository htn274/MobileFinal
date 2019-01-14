package com.example.root.mobilefinal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class AddItem extends AppCompatActivity implements View.OnClickListener {

    Spinner spnCategory;
    ImageView imageView_item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        initViews();
        initCategory();
        loadImage();
    }

    void initViews(){
        spnCategory = findViewById(R.id.spiner_category);
        imageView_item = findViewById(R.id.imageView_item);
    }

    void initCategory(){
        String []categories = {"Clothes", "Shoes", "Accessories"};
        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spnCategory.setAdapter(adapter);
        spnCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    void loadImage(){
        Glide.with(this).load(R.drawable.profile_default).into(imageView_item);
    }

    @Override
    public void onClick(View view) {

    }
}
