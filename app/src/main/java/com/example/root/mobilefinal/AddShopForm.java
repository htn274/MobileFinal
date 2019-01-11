package com.example.root.mobilefinal;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class AddShopForm extends AppCompatActivity implements View.OnClickListener {

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
        btn_uploadAvatar.setOnClickListener((View.OnClickListener) this);
        btn_next = findViewById(R.id.btn_nextStep);
        editText_shopName = findViewById(R.id.editText_shopname);
        editText_location = findViewById(R.id.editText_location);

        editText_openHour = findViewById(R.id.editText_openHour);
        editText_openHour.setInputType(InputType.TYPE_NULL);
        editText_closeHour = findViewById(R.id.EditText_closeHour);
        editText_closeHour.setInputType(InputType.TYPE_NULL);
        setTime();
    }

    @Override
    public void onClick(View view) {
        if (view == btn_uploadAvatar){
            onPickImage(view);
        }
    }

    void setTime(){
        TimePickerFragment pickOpenHour = new TimePickerFragment(editText_openHour, this);
        TimePickerFragment pickCloseHour = new TimePickerFragment(editText_closeHour, this);
    }

//    Choose image
    private static final int PICK_IMAGE_ID = 234; // the number doesn't matter

    public void onPickImage(View view) {
        Intent chooseImageIntent = ImagePicker.getPickImageIntent(this);
        startActivityForResult(chooseImageIntent, PICK_IMAGE_ID);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case PICK_IMAGE_ID:
                Bitmap bitmap = ImagePicker.getImageFromResult(this, resultCode, data);
                // TODO use bitmap
                avatar.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 200,200, false));
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
