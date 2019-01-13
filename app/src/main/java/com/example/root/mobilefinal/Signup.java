package com.example.root.mobilefinal;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class Signup extends AppCompatActivity implements View.OnClickListener {
    private static final int RC_SET_AVATAR = 3214;
    Button btn_signup;
    EditText editText_username, editText_password, editText_passwordConfirm;
    FirebaseAuth auth;
    FirebaseStorage storage;
    TextView textView_uploadAvatar;
    ImageView imageView_avatar;
    Bitmap chosenAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        setView();
    }

    private void setView() {
        setFirebase();

        btn_signup = findViewById(R.id.btn_signup);
        editText_username = findViewById(R.id.editText_username);
        editText_password = findViewById(R.id.editText_password);
        editText_passwordConfirm = findViewById(R.id.editText_passwordConfirm);
        textView_uploadAvatar = findViewById(R.id.textView_uploadAvatar);
        imageView_avatar = findViewById(R.id.imageView_avatar);

        btn_signup.setOnClickListener(this);
        textView_uploadAvatar.setOnClickListener(this);
    }

    private void setFirebase() {
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
    }

    void setAvatar() {
        Intent setAvatarIntent = ImagePicker.getPickImageIntent(this);
        startActivityForResult(setAvatarIntent, RC_SET_AVATAR);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SET_AVATAR) {
            chosenAvatar = ImagePicker.getImageFromResult(this, resultCode, data);
            imageView_avatar.setImageBitmap(chosenAvatar);
        }
    }

    @Override
    public void onClick(View view) {
        if (view == btn_signup) {
            final String username = editText_username.getText().toString();
            String password = editText_password.getText().toString();
            String passwordConfirm = editText_passwordConfirm.getText().toString();

            if (username == "" || password == "" || passwordConfirm == "") {
                Toast.makeText(this, "Username and password may not be empty", Toast.LENGTH_LONG).show();
                return;
            }

            if (password.equals(passwordConfirm) == false) {
                Toast.makeText(this, "Password not matched", Toast.LENGTH_SHORT).show();
                return;
            }

            auth.createUserWithEmailAndPassword(username + "@gmail.com", password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "User registered successfully", Toast.LENGTH_LONG).show();
                        Backend.uploadAvatar("avatar/user/" + task.getResult().getUser().getUid() + ".jpg", chosenAvatar);
                        finish();
                    }
                }
            });
        }
        else if (view == textView_uploadAvatar) {
            setAvatar();
        }
    }
}
