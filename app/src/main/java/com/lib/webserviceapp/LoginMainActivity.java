package com.lib.webserviceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginMainActivity extends AppCompatActivity {

    private EditText UsernameEditTxt;
    private EditText PasswordEditTxt;
    private Button loginButton;
    private CheckBox rememberMe;
    private TextView toSignupTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);

        UsernameEditTxt = findViewById(R.id.login_username);
        PasswordEditTxt = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_button);
        rememberMe = findViewById(R.id.rememberMeCheckbox);
        toSignupTxt = findViewById(R.id.tosignuptxt);
        // Check if remembered data is passed from the ProfileFragment
        String rememberedUsername = getIntent().getStringExtra("username");
        String rememberedPassword = getIntent().getStringExtra("password");

        if (rememberedUsername != null && rememberedPassword != null) {
            // Fill the login fields with remembered data
            UsernameEditTxt.setText(rememberedUsername);
            PasswordEditTxt.setText(rememberedPassword);
        }

        if (isLoggedIn()) { //if true
            Intent intent = new Intent(LoginMainActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredUsername = UsernameEditTxt.getText().toString();
                String enteredPassword = PasswordEditTxt.getText().toString();

                String savedUsername = getUserData("username");
                String savedPassword = getUserData("password");

                if (enteredUsername.equals(savedUsername) && enteredPassword.equals(savedPassword)) {
                    //successful
                    String message = "Login successful";
                    Toast.makeText(LoginMainActivity.this, message, Toast.LENGTH_SHORT).show();
                    setLoggedIn(true);
                    if (rememberMe.isChecked()) {
                        //Save the user if "Remember Me" is checked
                        RememberTheUser(enteredUsername, enteredPassword);
                    }
                    Intent intent = new Intent(LoginMainActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    String message = "failed, Please check your Information.";
                    Toast.makeText(LoginMainActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            }
        });
        toSignupTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginMainActivity.this, Signup.class);
                startActivity(intent);
            }
        });
    }
    private boolean isLoggedIn() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        return sharedPreferences.getBoolean("isLoggedIn", false);
    }

    private void setLoggedIn(boolean isLoggedIn) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", isLoggedIn);
        editor.apply();
    }

    private String getUserData(String key) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }
    private void RememberTheUser(String username, String password) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("rememberedUsername", username);
        editor.putString("rememberedPassword", password);
        editor.apply();
    }

}
