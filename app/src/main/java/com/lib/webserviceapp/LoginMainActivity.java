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

        if (isLoggedIn()) {
            Intent intent = new Intent(LoginMainActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
        // Load saved credentials if "Remember Me" is checked
        if (rememberMe.isChecked()) {
            loadSavedCredentials();
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredUsername = UsernameEditTxt.getText().toString();
                String enteredPassword = PasswordEditTxt.getText().toString();

                // Retrieve saved user data from SharedPreferences
                String savedUsername = getUserData("username");
                String savedPassword = getUserData("password");

                // Check if entered credentials match saved credentials
                if (enteredUsername.equals(savedUsername) && enteredPassword.equals(savedPassword)) {
                    // Login successful
                    String message = "Login successful";
                    Toast.makeText(LoginMainActivity.this, message, Toast.LENGTH_SHORT).show();

                    // Save credentials if "Remember Me" is checked
                    if (rememberMe.isChecked()) {
                        saveCredentials(enteredUsername, enteredPassword);
                    }
                    setLoggedIn(true);
                    Intent intent = new Intent(LoginMainActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    // Login failed
                    String message = "Login failed. Please check your credentials.";
                    Toast.makeText(LoginMainActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            }
        });
        toSignupTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the SignupActivity
                Intent intent = new Intent(LoginMainActivity.this, Signup.class);
                startActivity(intent);
            }
        });

    }
    private boolean isLoggedIn() {
        // Retrieve the login status from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        return sharedPreferences.getBoolean("isLoggedIn", false);
    }

    private void setLoggedIn(boolean isLoggedIn) {
        // Save the login status to SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", isLoggedIn);
        editor.apply();
    }

    private String getUserData(String key) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }

    private void saveCredentials(String username, String password) {
        SharedPreferences sharedPreferences = getSharedPreferences("RememberMeData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("rememberedUsername", username);
        editor.putString("rememberedPassword", password);
        editor.apply();
    }

    private void loadSavedCredentials() {
        SharedPreferences sharedPreferences = getSharedPreferences("RememberMeData", MODE_PRIVATE);
        String rememberedUsername = sharedPreferences.getString("rememberedUsername", "");
        String rememberedPassword = sharedPreferences.getString("rememberedPassword", "");

        // Set the saved credentials in the EditText fields
        UsernameEditTxt.setText(rememberedUsername);
        PasswordEditTxt.setText(rememberedPassword);
    }
}
