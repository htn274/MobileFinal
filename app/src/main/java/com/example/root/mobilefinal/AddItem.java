package com.example.root.mobilefinal;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class AddItem extends AppCompatActivity implements View.OnClickListener {

    private static final int RC_PICK_IMAGE = 8846;
    Spinner spnCategory;
    ImageView imageView_item;
    EditText editText_itemName;
    EditText editText_description;
    EditText editText_price;
    EditText editText_setVariations;
    EditText editText_quantity;
    TextView btn_uploadPhoto;
    Button btn_done;

    String selectedCategory;
    String chosenColor;
    String chosenSize;
    String sid;
    Bitmap chosenAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        Intent intent = getIntent();
        sid = intent.getStringExtra("sid");
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
        editText_itemName = findViewById(R.id.editText_itemName);
        editText_description = findViewById(R.id.editText_description);
        editText_price = findViewById(R.id.editText_price);
        editText_quantity = findViewById(R.id.editText_quantity);
        btn_uploadPhoto = findViewById(R.id.btn_uploadPhoto);
        btn_uploadPhoto.setOnClickListener(this);
        btn_done = findViewById(R.id.btn_done);
        btn_done.setOnClickListener(this);
    }

    void initCategory(){
        String[] categories = {"Clothes", "Shoes", "Accessories"};
        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spnCategory.setAdapter(adapter);
        spnCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedCategory = spnCategory.getSelectedItem().toString();
//                Toast.makeText(getApplicationContext(), spnCategory.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    void loadImage() {
        Glide.with(this).load(R.drawable.profile_default).into(imageView_item);
    }

    @Override
    public void onClick(View view) {
        if (view == editText_setVariations){
            DialogFragment dialog = VariationDialog.newInstance();
            ((VariationDialog) dialog).setCallback(new VariationDialog.Callback() {
                @Override
                public void onActionClick(String color, String size) {
                    chosenColor = color;
                    chosenSize = size;
                    editText_setVariations.setText(String.format("%s; %s", color, size));
                    Toast.makeText(getApplicationContext(), String.format("%s %s", color, size), Toast.LENGTH_SHORT).show();
                }
            });
            dialog.show(getSupportFragmentManager(), "tag");
        }
        else if (view == btn_done) {
            String name = editText_itemName.getText().toString();
            String description = editText_description.getText().toString();
            String price = editText_price.getText().toString();
            String quantity = editText_quantity.getText().toString();

            Backend.addItem(sid, name, description, selectedCategory, price, quantity, chosenColor, chosenSize, new Backend.Callback<String>() {
                @Override
                public void call(String iid) {
                    if (iid != null) {
                        Log.d("btag", "item added successfully, iid " + iid);
                        if (chosenAvatar != null) {
                            Backend.uploadAvatar("avatar/item/" + iid + ".jpg", chosenAvatar);
                        }
                        finish();
                    }
                    else {
                        Log.d("btag", "add item failed " + sid);
                    }
                }
            });
        }
        else if (view == btn_uploadPhoto) {
            Intent intent = ImagePicker.getPickImageIntent(this);
            startActivityForResult(intent, RC_PICK_IMAGE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_PICK_IMAGE) {
            chosenAvatar = ImagePicker.getImageFromResult(this, resultCode, data);
            imageView_item.setImageBitmap(chosenAvatar);
        }
    }
}
