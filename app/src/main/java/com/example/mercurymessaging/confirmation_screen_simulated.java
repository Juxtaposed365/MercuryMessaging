package com.example.mercurymessaging;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class confirmation_screen_simulated extends AppCompatActivity {
    String RemDate;
    String RemTime;
    String RemMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation_screen_simulated);
        Button Done = (Button)this.findViewById(R.id.Done);
        Done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });
        TextView RMessage = (TextView) findViewById(R.id.ReminderMessage);
        TextView RTime = (TextView) findViewById(R.id.ReminderTime);
        TextView RDate =(TextView) findViewById(R.id.ReminderDate);
        RemTime = getIntent().getStringExtra("Rtime");
        RemDate = getIntent().getStringExtra("Rdate");
        RemMessage = getIntent().getStringExtra("Rmessage");
        Log.d("BUG", "RemTime: " + RemTime);
        Log.d("BUG", "RemDate: " + RemDate);
        Log.d("BUG", "RemMessage: " + RemMessage);
        //set the viewtext values
        RMessage.setText(RemMessage);
        RDate.setText(RemDate);
        RTime.setText(RemTime);


    }
}