package com.example.root.mobilefinal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Login extends AppCompatActivity implements View.OnClickListener{

    Button btn_login;
    EditText editText_username, editText_password;
    TextView signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setView();
    }

    private void setView(){
        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener((View.OnClickListener) this);

        editText_username = findViewById(R.id.editText_username);
        editText_password = findViewById(R.id.editText_password);

        signup = findViewById(R.id.tv_signup);
        signup.setOnClickListener((View.OnClickListener) this);
    }

    @Override
    public void onClick(View view) {
        if (view == btn_login){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
        else if (view == signup){
            Intent intent = new Intent(getApplicationContext(), Signup.class);
            startActivity(intent);
        }
    }
}
