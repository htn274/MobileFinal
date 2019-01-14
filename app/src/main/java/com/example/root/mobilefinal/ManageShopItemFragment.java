package com.example.root.mobilefinal;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class ManageShopItemFragment extends Fragment {
    FloatingActionButton btn_addItem;
    RecyclerView rv_shopItems;

    public ManageShopItemFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_manage_shop_item, container, false);
        setUI(v);
        return v;
    }

    private void setUI(View v) {
        btn_addItem = v.findViewById(R.id.btn_addItem);
        rv_shopItems = v.findViewById(R.id.rv_shopItems);

        btn_addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddItem.class);
                startActivity(intent);
            }
        });
    }

}
