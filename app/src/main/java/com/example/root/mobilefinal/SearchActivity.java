package com.example.root.mobilefinal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;

import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    MaterialSearchBar searchBar;
    RecyclerView rv_search;
    TopItemAdapter itemAdapter, searchAdapter;
    List<Item> localDataSource;
    List<String> suggestList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initViews();
    }

    private void initViews(){
        rv_search = findViewById(R.id.rv_searchResult);
        rv_search.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        searchBar = findViewById(R.id.search_bar);
        loadAllItems();
        searchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                List<String> suggest = new ArrayList<>();
                for (String search : suggestList){
                    if (search.toLowerCase().contains(searchBar.getText().toString().toLowerCase())){
                        suggest.add(search);
                    }
                }
                searchBar.setLastSuggestions(suggest);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        searchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                if (!enabled){
                    rv_search.setAdapter(itemAdapter);
                }
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                startSearch(text);
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });
    }

    private void startSearch(CharSequence text) {
        List<Item> result = new ArrayList<>();
        for (Item item:localDataSource){
            if (item.name.contains(text)){
                result.add(item);
            }
        }
        searchAdapter = new TopItemAdapter(this, result);
        rv_search.setAdapter(searchAdapter);

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void loadAllItems() {
        Backend.getAllItems(new Backend.Callback<List<Item>>() {
            @Override
            public void call(List<Item> data) {
                displayListItem(data);
                buildSuggestList(data);
            }
        });
    }

    private void buildSuggestList(List<Item> itemList) {
        suggestList = new ArrayList<>();
        for (Item item: itemList){
            suggestList.add(item.name);
        }
        searchBar.setLastSuggestions(suggestList);
    }

    private void displayListItem(List<Item> itemList) {
        localDataSource = itemList;
        itemAdapter = new TopItemAdapter(this, itemList);
        rv_search.setAdapter(itemAdapter);
    }
}
