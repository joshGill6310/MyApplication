package com.example.myapplication;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

public class ListItemsActivity extends AppCompatActivity {
    protected static final String ACTIVITY_NAME = "ListItemsActivity";
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_items);
        Log.i(ACTIVITY_NAME,"in onCreate()");
        Switch switchButton = (Switch)findViewById(R.id.switch1);
        print("Below Switchbutton");
        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                CharSequence text =(compoundButton.isChecked())? "Switch is On":"Switch is Off";
                int duration =(compoundButton.isChecked())? Toast.LENGTH_SHORT: Toast.LENGTH_LONG ;


                Toast toast = Toast.makeText(ListItemsActivity.this , text, duration); //this is the ListActivity
                toast.show(); //display your message box
                 }
            });

                ImageButton cameraButton = findViewById(R.id.imageButton2);
                CheckBox checkB = findViewById(R.id.checkBox);
                checkB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ListItemsActivity.this);
// 2. Chain together various setter methods to set the dialog characteristics
                        builder.setMessage(R.string.dialog_message) //Add a dialog message to strings.xml

                                .setTitle(R.string.dialog_title)
                                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Intent resultIntent = new Intent(ListItemsActivity.this,MainActivity.class  );
                                        resultIntent.putExtra("Response", "Here is my response");
                                        setResult(Activity.RESULT_OK, resultIntent);
                                        Toast toast = Toast.makeText(ListItemsActivity.this,"Ending Activity",Toast.LENGTH_SHORT);
                                        toast.show();
                                        finish();
                                    }
                                })
                                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                    }
                                })
                                .show();
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
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        try{
            if(data !=null){
                Bundle extras = data.getExtras();
                Bitmap buttonImage = (Bitmap) extras.get("data");
                ImageButton pictureReplaced = findViewById(R.id.imageButton2);
                pictureReplaced.setImageBitmap(buttonImage);

            }
        } catch(Exception e){
        }
    }
    private void print(String text){
        Toast toast = Toast.makeText(ListItemsActivity.this,text,Toast.LENGTH_SHORT);
        toast.show();
    }
    public void imageClicked(View imageView) {
        ImageButton btnImg = findViewById(R.id.imageButton2);
        Intent takePictureIntent =
                new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            print("doing something");
            startActivityForResult(takePictureIntent, 10);

    }
}