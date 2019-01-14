package com.example.root.mobilefinal;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class AddItem extends AppCompatActivity implements View.OnClickListener {

    Spinner spnCategory;
    ImageView imageView_item;
    EditText editText_price;
    EditText editText_setVariations;

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
        editText_setVariations = findViewById(R.id.editText_setVariations);
        editText_setVariations.setInputType(InputType.TYPE_NULL);
        editText_setVariations.setOnClickListener(this);
    }

    void initCategory(){
        String []categories = {"Clothes", "Shoes", "Accessories"};
        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spnCategory.setAdapter(adapter);
        spnCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(), spnCategory.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
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
        if (view == editText_setVariations){
            DialogFragment dialog = VariationDialog.newInstance();
            ((VariationDialog) dialog).setCallback(new VariationDialog.Callback() {
                                                       @Override
                                                       public void onActionClick(String name) {
                                                           Toast.makeText(getApplicationContext(), name, Toast.LENGTH_SHORT).show();
                                                       }
                                                   });
            dialog.show(getSupportFragmentManager(), "tag");
        }
    }
}
