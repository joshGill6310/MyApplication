package com.example.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.myapplication.databinding.ActivityTestToolbarBinding;

public class TestToolbar extends AppCompatActivity {
    protected static final String ACTIVITY_NAME = "TestToolbar";
    private AppBarConfiguration appBarConfiguration;
    private ActivityTestToolbarBinding binding;
    AppCompatActivity TestToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    public boolean onOptionsItemSelected(MenuItem mi){
        int id = mi.getItemId();
        switch(id){
            case R.id.action_one:
                Log.d(ACTIVITY_NAME, "Option 1 Selected");
                Snackbar.make(findViewById(R.id.action_one), "Well hello there", Snackbar.LENGTH_LONG);
                break;
            case R.id.action_two:
                Log.d(ACTIVITY_NAME, "Option 2 Selected");

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.pick_color);
// Add the buttons
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                        finish();
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
// Create the AlertDialog
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
            case R.id.action_three:
                case3();
            case R.id.action_four:
                String About = "Version 1.0, by Joshua Gill";
                Toast toast = Toast.makeText(this,About,Toast.LENGTH_LONG);
                toast.show();
                break;
        }
        return true;
    }

    public boolean onCreateOptionsMenu (Menu m){
        getMenuInflater().inflate(R.menu.toolbar_menu, m );
        return true;
    }
    public void onClickFAB(View view) {
        Snackbar.make(view, "Well hello there", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
    public void case3() {
        AlertDialog.Builder customDialog =
                new AlertDialog.Builder(TestToolbar);
        // Get the layout inflater
        LayoutInflater inflater = TestToolbar.getLayoutInflater();
        final View view = inflater.inflate(R.layout.custom_dialog, null);
        customDialog.setView(view)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        EditText edit = view.findViewById(R.id.dialog_message_box);
                        String message = edit.getText().toString();
                        Snackbar snackbar = Snackbar.make(findViewById(R.id.action_three),message,Snackbar.LENGTH_LONG);
                    }
                })
                .setNegativeButton(R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
        Dialog dialog = customDialog.create();
        dialog.show();
    }
}