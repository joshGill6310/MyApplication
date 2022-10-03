package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    protected static final String ACTIVITY_NAME = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.i(ACTIVITY_NAME,"in onCreate()");
        Button loginbutton = findViewById(R.id.login);
        SharedPreferences sharedPreferences = getSharedPreferences("strings.xml",MODE_PRIVATE);
        String defaultEmail = "";
        defaultEmail = sharedPreferences.getString("DefaultEmail","email@domain.com");
        EditText emailfield = findViewById(R.id.emailSpace);
        emailfield.setText(defaultEmail);
    }
    @Override
    protected void onStart(){
        super.onStart();
        Log.i(ACTIVITY_NAME,"in onStart()");
    }
    @Override
    protected void onPause(){
        super.onPause();
        Log.i(ACTIVITY_NAME,"in onPause()");
    }
    @Override
    protected void onStop(){
        super.onStop();
        Log.i(ACTIVITY_NAME,"in onStop()");
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.i(ACTIVITY_NAME,"in onDestroy()");
    }


    public void onClickLogin(View view) {
        EditText email = findViewById(R.id.emailSpace);
        String emailText = email.getText().toString();
        CharSequence error = "Email is in incorrect format";
        CharSequence errorPassword = "Password is blank";
        SharedPreferences sharedPreferences = getSharedPreferences("strings.xml",MODE_PRIVATE);
        EditText password = findViewById(R.id.editTextTextPassword);
        String passwordText = password.getText().toString();
        if (passwordText.equals("")) {
            password.setText("");
            password.setHint(errorPassword);
            if (!validateEmail(emailText)) {
                email.setText("");
                email.setHint(error);
            }
        }
        else {
            SharedPreferences.Editor myEdit = sharedPreferences.edit();
            myEdit.putString("strings.xml",emailText);
            myEdit.apply();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }
    private boolean validateEmail(String text){
        boolean isValid = false;
        String[] emailParts = text.split("@");
        if(emailParts.length>=2 && emailParts[1].contains(".")) isValid = true;
        return isValid;
    }
}
