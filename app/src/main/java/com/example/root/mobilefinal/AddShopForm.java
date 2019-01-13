package com.example.root.mobilefinal;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.auth.FirebaseAuth;

import java.io.ByteArrayOutputStream;

public class AddShopForm extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    ImageView avatar;
    Button btn_uploadAvatar, btn_next;
    EditText editText_shopName, editText_location, editText_openHour, editText_closeHour;
    private GoogleApiClient mGoogleApiClient;
    private static final int PLACE_PICKER_REQUEST = 1;
    private static final int PICK_IMAGE_ID = 2;
    Place pickedPlace;
    Bitmap chosenAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shop_form);
        AddShopForm.this.setTitle("New Shop");
        initView();

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();
    }

    void initView(){
        avatar = findViewById(R.id.imageView_avatarShop);

        btn_uploadAvatar = findViewById(R.id.btn_uploadAvatar);
        btn_uploadAvatar.setOnClickListener((View.OnClickListener) this);

        btn_next = findViewById(R.id.btn_nextStep);
        btn_next.setOnClickListener(this);

        editText_shopName = findViewById(R.id.editText_shopname);

        editText_location = findViewById(R.id.editText_location);
        editText_location.setOnClickListener(this);
        editText_location.setInputType(InputType.TYPE_NULL);

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
        else if (view == editText_location){
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            Log.d("Nunu", "onClick: Location");
            try {
                startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
                Log.d("Nunu", "onClick: Location");
            } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();
            }
        }
        else if (view == btn_next) {
            String uid = FirebaseAuth.getInstance().getUid();
            String shopname = editText_shopName.getText().toString();
            String address = editText_location.getText().toString();
            String openHour = editText_openHour.getText().toString();
            String closeHour = editText_closeHour.getText().toString();
            double lat = pickedPlace.getLatLng().latitude;
            double lng = pickedPlace.getLatLng().longitude;


            Backend.addShop(this, uid, shopname, address, lat, lng, openHour, closeHour, new Backend.Callback<String>() {
                @Override
                public void call(String sid) {
                    if (sid == null) {
                        Log.d("btag", "addShop failed");
                    }
                    else {
                        Log.d("btag", "add shop succeeded, shopid " + sid);
                        Backend.uploadAvatar("avatar/shop/" + sid + ".jpg", chosenAvatar);
                    }
                }
            });
        }
    }

    void setTime(){
        TimePickerFragment pickOpenHour = new TimePickerFragment(editText_openHour, this);
        TimePickerFragment pickCloseHour = new TimePickerFragment(editText_closeHour, this);
    }

//    Choose image

    public void onPickImage(View view) {
        Intent chooseImageIntent = ImagePicker.getPickImageIntent(this);
        startActivityForResult(chooseImageIntent, PICK_IMAGE_ID);
    }

    private byte[] bitmapToByte(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case PICK_IMAGE_ID:
                chosenAvatar = ImagePicker.getImageFromResult(this, resultCode, data);
                // TODO use bitmap
                Glide.
                        with(this)
                        .load(bitmapToByte(chosenAvatar))
                        .asBitmap()
                        .override(200,200)
                        .centerCrop()
                        .into(avatar);
                break;
            case PLACE_PICKER_REQUEST: {
                if (resultCode == RESULT_OK) {
                    pickedPlace = PlacePicker.getPlace(data, this);
//                    StringBuilder stBuilder = new StringBuilder();
//                    String placename = String.format("%s", place.getName());
//                    String latitude = String.valueOf(place.getLatLng().latitude);
//                    String longitude = String.valueOf(place.getLatLng().longitude);
                    String address = String.format("%s", pickedPlace.getAddress());
                    editText_location.setText(address);
                }
            }
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Connection failed", Toast.LENGTH_LONG).show();
    }

}
