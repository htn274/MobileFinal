package com.example.root.mobilefinal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class AddShopForm extends AppCompatActivity {

    ImageView avatar;
    Button btn_uploadAvatar, btn_next;
    EditText editText_shopName, editText_location, editText_openHour, editText_closeHour;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shop_form);
        AddShopForm.this.setTitle("New Shop");
        initView();
    }

    void initView(){
        avatar = findViewById(R.id.imageView_avatarShop);
        btn_uploadAvatar = findViewById(R.id.btn_uploadAvatar);
        btn_next = findViewById(R.id.btn_nextStep);
        editText_shopName = findViewById(R.id.editText_shopname);
        editText_location = findViewById(R.id.editText_location);

        editText_openHour = findViewById(R.id.editText_openHour);
        editText_closeHour = findViewById(R.id.EditText_closeHour);
        setTime();
    }

    void setTime(){
        TimePickerFragment pickOpenHour = new TimePickerFragment(editText_openHour, this);
        TimePickerFragment pickCloseHour = new TimePickerFragment(editText_closeHour, this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
