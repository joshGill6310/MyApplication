package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    protected static final String ACTIVITY_NAME = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(ACTIVITY_NAME,"in onCreate()");
        Button imAButton = findViewById(R.id.button);
        Button startChat = findViewById(R.id.button2);
        startChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(ACTIVITY_NAME,"User clicked start chat");
                Intent toChat = new Intent(MainActivity.this,ChatWindow.class);
                startActivity(toChat);
            }
        });

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
    public void onClickMain(View view){
        Intent intent = new Intent(MainActivity.this,ListItemsActivity.class);
        startActivityForResult(intent,10);
    }
    public void onActivityResult(int requestCode, int responseCode, Intent Data){
        super.onActivityResult(requestCode,responseCode,Data);
        if(requestCode == Activity.RESULT_OK){
            Log.i(ACTIVITY_NAME, "Returned to MainActivity.onActivityResult");
            String messagePassed = Data.getStringExtra("Response");
            Toast toast = Toast.makeText(this,messagePassed,Toast.LENGTH_LONG);
            toast.show();
        }
    }

}