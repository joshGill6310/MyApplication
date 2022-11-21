package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MessageDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_details);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        MessageFragment mf = new MessageFragment();
        mf.setArguments(savedInstanceState);
        ft.add(R.id.frameRightOfChat,mf);
        ft.commit();
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button b = findViewById(R.id.deleteButton);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MessageDetails.this,ChatWindow.class);
            }
        });
    }
}