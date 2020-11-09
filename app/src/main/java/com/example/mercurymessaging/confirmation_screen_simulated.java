package com.example.mercurymessaging;

import android.content.Intent;
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

    //this also needs a method that closes all previous activities and launches the home page
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation_screen_simulated);
        Button Done = (Button)this.findViewById(R.id.Confirm);
        Done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               goHome();
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

    public void goHome(){
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
        finish();
    }

}