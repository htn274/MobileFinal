package com.example.root.mobilefinal;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Login extends AppCompatActivity implements View.OnClickListener{

    Button btn_login;
    EditText editText_username, editText_password;
    TextView signup;

    FirebaseAuth auth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setView();
    }
    void signInWithPhoneCredential(PhoneAuthCredential phoneAuthCredential) {
        auth.signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Logged in", Toast.LENGTH_LONG).show();
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("btag", "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            Log.d("btag", "display name " + user.getDisplayName());
                            Log.d("btag", "uid " + user.getUid());

                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w("btag", "signInWithCredential:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Login faled", Toast.LENGTH_LONG).show();
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }
    void signInWithEmailPassword(String email, String password) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Logged in", Toast.LENGTH_LONG).show();
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("btag", "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            Log.d("btag", "display name " + user.getDisplayName());
                            Log.d("btag", "uid " + user.getUid());

                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w("btag", "signInWithCredential:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Login faled", Toast.LENGTH_LONG).show();
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }

    private void setView() {
        setUpFireBase();
        editText_username = findViewById(R.id.editText_username);
        editText_password = findViewById(R.id.editText_password);
//        assert editText_username != null;
//        assert editText_password != null;

        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);

        signup = findViewById(R.id.tv_signup);
        signup.setOnClickListener(this);
    }

    private void setUpFireBase() {
        FirebaseApp.initializeApp(this);
        auth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View view) {
        if (view == btn_login) {
            assert editText_username != null;
            assert editText_password != null;
            Log.d("btag", editText_username.getText().toString());
            Log.d("btag", editText_password.getText().toString());
            signInWithEmailPassword(editText_username.getText().toString() + "@gmail.com", editText_password.getText().toString());
        }
        else if (view == signup) {
            Intent intent = new Intent(getApplicationContext(), Signup.class);
            startActivity(intent);
        }
    }
}
