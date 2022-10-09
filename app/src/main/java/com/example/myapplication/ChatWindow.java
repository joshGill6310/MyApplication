package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ChatWindow extends AppCompatActivity {
    private ArrayList<String> chatHistory = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);
        ListView listView = findViewById(R.id.chatWindow);
        EditText chatField = findViewById(R.id.editTextTextPersonName3);
        Button sendButton = findViewById(R.id.button3);

        print("help me");

        ChatAdapter messageAdapter =new ChatAdapter( this );
        listView.setAdapter(messageAdapter);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String message = chatField.getText().toString().trim();

                print(message);
                chatHistory.add(message);
                print(""+chatHistory.size());
                messageAdapter.notifyDataSetChanged();
            }
        });

    }
    private class ChatAdapter extends ArrayAdapter<String> {
        public ChatAdapter(Context ctx){
            super(ctx,0);
        }
        public int getCount(){
            return chatHistory.size();
        }
        public String getItem(int position){
            return chatHistory.get(position);
        }
        public View getView(int position, View convertView, ViewGroup parent){
            LayoutInflater inflater = ChatWindow.this.getLayoutInflater();
            View result;
            TextView message;
            print("Before if");
            if(position%2 == 0) {print("test part 1");
                result = inflater.inflate(R.layout.chat_row_incoming, null);

            }
            else{ print("Test part 2");
                result = inflater.inflate(R.layout.chat_row_outgoing, null);

            }
            message = result.findViewById(R.id.message_text);


            message.setText(this.getItem(position));
            return result;
        }


    }
    private void print(String text){
        Toast toast = Toast.makeText(ChatWindow.this,text,Toast.LENGTH_SHORT);
        toast.show();
    }
}