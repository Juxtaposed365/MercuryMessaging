package com.example.mercurymessaging;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class SetReminder extends AppCompatActivity{
    EditText et;//declared edittext variable
    String time;
    String date;
    String message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setreminder);
        Button cancelButton = (Button)this.findViewById(R.id.cancelButton);
        et = (EditText)this.findViewById(R.id.e1);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button save = (Button)this.findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                populateReminderInfo();
            }
        });
    }

    public void showTimePickerDialog(View v) {
        Intent intent = new Intent(this, TimeScheduling.class);
        startActivity(intent);
        finish();
        //newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public void populateReminderInfo()
    {
     time = getIntent().getStringExtra("time");
     date = getIntent().getStringExtra("date");
     message= et.getText().toString();
     Log.d("BUG", "time: " + time);
     Log.d("BUG", "date: " + date);
     Log.d("BUG", "message: " + message);
     Intent i = new Intent(this, confirmation_screen_simulated.class);
     i.putExtra("Rtime", time);
     i.putExtra("Rdate", date);
     i.putExtra("Rmessage",message);
     startActivity(i);
    }
}