package com.example.mercurymessaging;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Home extends AppCompatActivity {
    private Button NewReminder;
    private TextView Logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        NewReminder = (Button) findViewById(R.id.newReminder);
        NewReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNewReminderClick();
            }
        });
        Logout = (TextView) findViewById(R.id.logout);
        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    public void onNewReminderClick()
    {
        //Log.d("BUG", "NReminder executed, but probably crashed");
        Intent intent = new Intent(this, SetReminder.class);
        startActivity(intent);
    }
}