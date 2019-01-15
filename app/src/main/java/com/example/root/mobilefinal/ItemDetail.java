package com.example.root.mobilefinal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

public class ItemDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
    }


    void initViews(){

    }

    void initSpiner(Spinner spiner, ArrayList<String> dataSpiner){
        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, dataSpiner);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spiner.setAdapter(adapter);
    }
}
