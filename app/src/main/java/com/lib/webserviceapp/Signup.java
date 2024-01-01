package com.lib.webserviceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Signup extends AppCompatActivity {

    private EditText nameEditTxt;
    private EditText emailEditTxt;
    private EditText usernameEditTxt;
    private EditText passwordEditTxt;
    private Button signupButton;
    private TextView tologinTxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        nameEditTxt = findViewById(R.id.signup_name);
        emailEditTxt = findViewById(R.id.signup_email);
        usernameEditTxt = findViewById(R.id.signup_username);
        passwordEditTxt = findViewById(R.id.signup_password);
        signupButton = findViewById(R.id.signup_button);
        tologinTxt = findViewById(R.id.loginText);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditTxt.getText().toString();
                String email = emailEditTxt.getText().toString();
                String username = usernameEditTxt.getText().toString();
                String password = passwordEditTxt.getText().toString();
                saveInformation(name, email, username, password);
                String message = "successfully registered";
                Toast.makeText(Signup.this, message, Toast.LENGTH_SHORT).show();
            }
        });
        tologinTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
        private void saveInformation(String name, String email, String username, String password) {
            SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("name", name);
            editor.putString("email", email);
            editor.putString("username", username);
            editor.putString("password", password);
            editor.commit();
    }
}
