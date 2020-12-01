package com.example.mercurymessaging;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class SetReminder extends AppCompatActivity{
    EditText et; //declared edittext variable
    String time;
    String date;
    String message;
    CheckBox dis;
    CheckBox sms;
    List<String> services = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setreminder1);
        Button cancelButton = this.findViewById(R.id.cancelButton);
        et = this.findViewById(R.id.e1);
        cancelButton.setOnClickListener(v -> finish());

        dis = this.findViewById(R.id.checkbox_dis);
        sms = this.findViewById(R.id.checkbox_sms);

        Button save = this.findViewById(R.id.save);
        save.setOnClickListener(v -> populateReminderInfo());
    }

    public void showTimePickerDialog(View v) {
        Intent intent = new Intent(this, TimeScheduling.class);
        startActivity(intent);
        finish();
    }

    public void populateReminderInfo()
    {
        if(services.contains("Discord") && !dis.isChecked()) services.remove("Discord");
        if(services.contains("SMS") && !sms.isChecked()) services.remove("SMS");

        if(!services.contains("Discord") && dis.isChecked()) services.add("Discord");
        if(!services.contains("SMS") && sms.isChecked()) services.add("SMS");
        time = getIntent().getStringExtra("time");
        date = getIntent().getStringExtra("date");
        message = et.getText().toString();
        Log.d("BUG", "time: " + time);
        Log.d("BUG", "date: " + date);
        Log.d("BUG", "message: " + message);
        Intent i = new Intent(this, confirmation_screen_simulated.class);
        i.putExtra("Rtime", time);
        i.putExtra("Rdate", date);
        i.putExtra("Rmessage", message);
        i.putExtra("Services", services.toString());
        startActivity(i);
    }

}