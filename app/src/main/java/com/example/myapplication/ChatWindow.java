package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ChatWindow extends AppCompatActivity {
    protected static final String ACTIVITY_NAME = "ChatWindow";
    private ArrayList<String> chatHistory = new ArrayList<String>();
    SQLiteDatabase BaseHolder;
    String[] allItems = new String[]{String.valueOf(ChatDatabaseHelper.KEY_ID),ChatDatabaseHelper.KEY_MESSAGE};
    Cursor pointer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ChatDatabaseHelper tempBase = new ChatDatabaseHelper(this);
        BaseHolder = tempBase.getWritableDatabase();
        setContentView(R.layout.activity_chat_window);
        ListView listView = findViewById(R.id.chatWindow);
        EditText chatField = findViewById(R.id.editTextTextPersonName3);
        Button sendButton = findViewById(R.id.button3);
        addMessages();

        print("help me");
        boolean isLoaded;
        if(findViewById(R.id.frameRightOfChat)==null) isLoaded=false;
        else isLoaded = true;
        ChatAdapter messageAdapter =new ChatAdapter( this );
        listView.setAdapter(messageAdapter);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String message = chatField.getText().toString().trim();

                print(message);
                chatHistory.add(message);
                print(""+chatHistory.size());
                ContentValues value = new ContentValues();
                value.put(ChatDatabaseHelper.KEY_MESSAGE,message);
                BaseHolder.insert(ChatDatabaseHelper.TABLE_Of_My_ITEMS,null,value);
                messageAdapter.notifyDataSetChanged();
            }
        });
        ListView listView1 = findViewById(R.id.chatWindow);
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tV = (TextView) adapterView.getItemAtPosition(i);
                Bundle args = new Bundle();
                String text = tV.getText()+"";
                args.putLong("ID",l);
                args.putCharSequence("Message",text);
                if(isLoaded==true){

                    MessageFragment mf = new MessageFragment();
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();



                    mf.setArguments(args);
                    ft.add(R.id.frameRightOfChat,mf);
                    ft.commit();
                }
                else{
                    Intent toMessageDetails = new Intent(ChatWindow.this,MessageDetails.class);
                    toMessageDetails.putExtra("ID",l);
                    toMessageDetails.putExtra("Message",text);
                    startActivityForResult(toMessageDetails,200);
                }

            }
        });
    }
    @SuppressLint("Range")
    public void addMessages(){
        pointer = BaseHolder.query(ChatDatabaseHelper.TABLE_Of_My_ITEMS,allItems,null,null,null,null,null);
        if(pointer.getCount()>=0) {
            pointer.moveToFirst();
            while (!pointer.isAfterLast()) {
                Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + pointer.getString(pointer.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE)));
                String message = pointer.getString(pointer.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE));
                chatHistory.add(message);
                Log.i(ACTIVITY_NAME, "Cursor’s  column count =" + pointer.getColumnCount());
                pointer.moveToNext();
            }
        }
        for(int columnIndex=0;columnIndex<pointer.getColumnCount();columnIndex++){
            Log.i(ACTIVITY_NAME,"Column Name="+pointer.getColumnName(columnIndex));
        }
        pointer.close();

    }
    private class ChatAdapter extends ArrayAdapter<String> {
        private Cursor checker;
        public ChatAdapter(Context ctx){
            super(ctx,0);
        }
        public int getCount(){
            return chatHistory.size();
        }
        public String getItem(int position){
            return chatHistory.get(position);
        }
        @SuppressLint("Range")
        public Long getItemID(int position){
            ChatDatabaseHelper data = new ChatDatabaseHelper(getContext());
            pointer.moveToPosition(position);
            if(pointer.getColumnIndex(ChatDatabaseHelper.KEY_ID)==-1){
                Long l = Long.valueOf(-1);
                return l;
            }
            else return pointer.getLong(pointer.getColumnIndex(ChatDatabaseHelper.KEY_ID));
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
    @Override
    protected void onDestroy(){
        super.onDestroy();
        pointer.close();
        BaseHolder.close();

    }
}