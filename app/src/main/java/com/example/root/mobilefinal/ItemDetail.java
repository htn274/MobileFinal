package com.example.root.mobilefinal;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

//import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemDetail extends AppCompatActivity implements View.OnClickListener, AddCartDialog.AddCartListener{
    ImageView imageView_itemPhoto;
    TextView textView_itemName, textView_price, textView_quantity, textView_shopName;
    Item thisItem;
    Spinner spinner_size, spinner_color;
    LinearLayout btn_addCart;
    Toolbar toolbar;
    ImageView btn_search, btn_cart;
//    ElegantNumberButton chosenQuantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        initViews();
        Log.d("btag", "ItemDetail onCreate iid " + getIntent().getStringExtra("iid"));
        Backend.getItem(getIntent().getStringExtra("iid"), new Backend.Callback<Item>() {
            @Override
            public void call(Item data) {
                thisItem = data;
                updateItem(data);
            }
        });
    }

    private void updateItem(Item data) {
        Backend.downloadAvatar("avatar/item/" + data.iid + ".jpg", new Backend.Callback<Bitmap>() {
            @Override
            public void call(Bitmap data) {
                imageView_itemPhoto.setImageBitmap(data);
            }
        });
        textView_itemName.setText(data.name);
        textView_price.setText(data.price.toString() + " đ");
        textView_quantity.setText(data.quantity.toString() + " remain items");
        Backend.getShop(data.sid, new Backend.Callback<Shop>() {
            @Override
            public void call(Shop data) {
                textView_shopName.setText(data.name);
            }
        });
        initSpiner(spinner_color, Arrays.asList(((String) data.variation.get("color")).split(" ")));
        initSpiner(spinner_size, Arrays.asList(((String) data.variation.get("size")).split(" ")));
//        chosenQuantity.setRange(0, Integer.valueOf(thisItem.quantity));
    }

    void initViews(){
        toolbar = findViewById(R.id.toolbar_itemDetail);
        imageView_itemPhoto = findViewById(R.id.imageView_itemPhoto);
        textView_itemName = findViewById(R.id.textView_itemName);
        textView_price = findViewById(R.id.textView_price);
        textView_quantity = findViewById(R.id.textView_quantity);
        textView_shopName = findViewById(R.id.textView_shopName);
        spinner_color = findViewById(R.id.spiner_color);
        spinner_size = findViewById(R.id.spiner_size);

        btn_addCart = findViewById(R.id.btn_addToCart);
        btn_addCart.setOnClickListener(this);

        btn_cart = toolbar.findViewById(R.id.btn_cart);
        btn_cart.setOnClickListener(this);
        btn_search = toolbar.findViewById(R.id.btn_search);
        btn_search.setOnClickListener(this);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

//        chosenQuantity = findViewById(R.id.quantityNumber);
    }

    void initSpiner(Spinner spiner, List<String> dataSpiner){
        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, dataSpiner);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spiner.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        if (view == btn_addCart){
            addCartAction();
        }
        else if (view == btn_search){
            startActivity(new Intent(this, SearchActivity.class));
        }
        else if (view == btn_cart){
            startActivity(new Intent(this, MyCart.class));
        }
    }

    @Override
    public void onButtonCLick(boolean state) {
        if (state == true){
            startActivity(new Intent(this, MyCart.class));
        }
    }

    public void addCartAction(){
//        Backend.addCart(thisItem.iid, chosenQuantity.getNumber(), new Backend.Callback<Boolean>() {
//            @Override
//            public void call(Boolean check) {
//                if (check == true){
//                    AddCartDialog addCartDialog = new AddCartDialog(textView_itemName.getText().toString(), textView_price.getText().toString());
//                    addCartDialog.show(getSupportFragmentManager(), "Dialog");
//                }
//            }
//        });

    }
}
