package com.example.mercurymessaging;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Home extends AppCompatActivity {
    private Button newReminder;
    private Button sendNow;
    private Button upcomingReminders;
    private TextView logout;
    private static String user;
    private TextView greeting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        user = generateTestUser();

        greeting = this.findViewById(R.id.greeting);
        greeting.setText("Hello, " + user);
        newReminder = findViewById(R.id.newReminder);
        newReminder.setOnClickListener(v -> onNewReminderClick());

        sendNow = findViewById(R.id.sendNow);
        sendNow.setOnClickListener(v -> onSendNowClick());

        upcomingReminders = findViewById(R.id.upcomingReminders);
        upcomingReminders.setOnClickListener(v -> upcomingRemindersAction());

        logout = findViewById(R.id.logout);
        logout.setOnClickListener(v -> launchLogin());
    }

    public String generateTestUser() {
        return "Test" + (int) (Math.random() * 1000);
    }

    public static String getUser() {
        return user;
    }

    public void upcomingRemindersAction() {
        Intent i = new Intent(this, Upcoming.class);
        startActivity(i);
    }

    public void onSendNowClick() {
        Intent intent = new Intent(this, SendNow.class);
        startActivity(intent);
    }

    public void onNewReminderClick()
    {
        Intent intent = new Intent(this, SetReminder.class);
        startActivity(intent);
    }

    public void launchLogin(){
        Intent intent = new Intent(this, LoginPage.class);
        startActivity(intent);
        finish();
    }

}