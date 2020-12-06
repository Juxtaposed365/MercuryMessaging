package com.example.mercurymessaging;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;

public class confirmation_screen_simulated extends AppCompatActivity {
    String RemDate;
    String RemTime;
    String RemMessage;
    String name;
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation_screen_simulated);
        Button Done = this.findViewById(R.id.Confirm);
        Done.setOnClickListener(v -> new WriteData().execute());
        Button cancel = this.findViewById(R.id.Cancel);
        list = this.findViewById(R.id.confirmRecipients);

        cancel.setOnClickListener(v -> finish());

        TextView RMessage = findViewById(R.id.ReminderMessage);
        TextView RTime = findViewById(R.id.ReminderTime);
        TextView RDate = findViewById(R.id.ReminderDate);

        RemTime = getIntent().getStringExtra("Rtime");
        RemDate = getIntent().getStringExtra("Rdate");
        RemMessage = getIntent().getStringExtra("Rmessage");
        name = Home.getUser();

        Log.d("BUG", "RemTime: " + RemTime);
        Log.d("BUG", "RemDate: " + RemDate);
        Log.d("BUG", "RemMessage: " + RemMessage);

        //set the viewtext values
        RMessage.setText(RemMessage);
        RDate.setText(RemDate);
        RTime.setText(RemTime);
        list.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, SetReminder.recipients.toArray()));

    }

    class WriteData extends AsyncTask<Void, Void, Void> {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        HashMap<String, Object> user = new HashMap<>();
        Calendar calendar = Calendar.getInstance();

        @Override
        protected Void doInBackground(Void... voids) {
            user.put("AppointmentDate", RemDate);
            user.put("DiscordChannel", Arrays.asList("channel1", "channel2"));
            user.put("DiscordLoginID", "id"+ name);
            user.put("DiscordPassword", "password1");
            user.put("SlackPassword", "password");
            user.put("UserID", name);
            user.put("Webhooks", SetReminder.webhooks);
            user.put("currentDate", (calendar.get(Calendar.MONTH) + 1)+"/"+calendar.get(Calendar.DAY_OF_MONTH)+"/"+calendar.get(Calendar.YEAR));
            String mins;
            if(calendar.get(Calendar.MINUTE) < 10) {
                mins = "0" + calendar.get(Calendar.MINUTE);
            } else {
                mins = String.valueOf(calendar.get(Calendar.MINUTE));
            }
            user.put("currentTime", calendar.get(Calendar.HOUR) + ":" + mins);
            user.put("TimeScheduled", RemTime);
            user.put("email", name + "@fakeemail.com");
            user.put("message", RemMessage);
            user.put("messageSent", false);
            user.put("phone", "123-456-7890");
            user.put("recipients", SetReminder.recipients);
            user.put("services", SetReminder.services);
            user.put("slackId", "slack"+ name);

            db.collection("Users")
            .document(name)
            .set(user, SetOptions.merge())
            .addOnSuccessListener(aVoid -> {
                SetReminder.clearCache();
                Snackbar.make(findViewById(R.id.confirmedLayout),
                        "Entry was created successfully",
                        Snackbar.LENGTH_SHORT)
                        .addCallback(new Snackbar.Callback() {
                            @Override
                            public void onDismissed(Snackbar transientBottomBar, int event) {
                                super.onDismissed(transientBottomBar, event);
                                finish();
                            }
                        })
                        .show();
                Upcoming.setUserCache(name);
            })
            .addOnFailureListener(e -> Snackbar.make(findViewById(R.id.confirmedLayout),
                    "Issue with creating entry",
                    Snackbar.LENGTH_SHORT)
                    .addCallback(new Snackbar.Callback() {
                        @Override
                        public void onDismissed(Snackbar transientBottomBar, int event) {
                            super.onDismissed(transientBottomBar, event);
                            finish();
                        }
                    })
                    .show());

            return null;
        }
    }

}