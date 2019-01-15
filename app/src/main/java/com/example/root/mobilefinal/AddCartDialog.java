package com.example.root.mobilefinal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

@SuppressLint("ValidFragment")
public class AddCartDialog extends BottomSheetDialogFragment {

    String mName, mPrice;
    TextView textView_name, textView_price;
    Button viewCart;
    private AddCartListener mListener;

    public AddCartDialog(String name, String price){
        mName = name;
        mPrice = price;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_cart_dialog, container, false);
        textView_name = view.findViewById(R.id.textView_productName);
        textView_name.setText(mName);
        textView_price = view.findViewById(R.id.textView_price);
        textView_price.setText(mPrice);

        viewCart = view.findViewById(R.id.btn_viewCart);
        viewCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onButtonCLick(true);
                dismiss();
            }
        });
        return view;
    }

    public interface AddCartListener{
        void onButtonCLick(boolean state);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (AddCartListener) context;
    }
}

