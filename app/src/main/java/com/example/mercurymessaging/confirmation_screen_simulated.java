package com.example.mercurymessaging;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.ArrayList;

public class confirmation_screen_simulated extends AppCompatActivity {
    String RemDate;
    String RemTime;
    String RemMessage;
    String remServices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation_screen_simulated);
        Button Done = this.findViewById(R.id.Confirm);
        Done.setOnClickListener(v -> new WriteData().execute());
        Button cancel = this.findViewById(R.id.Cancel);

        cancel.setOnClickListener(v -> finish());

        TextView RMessage = findViewById(R.id.ReminderMessage);
        TextView RTime = findViewById(R.id.ReminderTime);
        TextView RDate = findViewById(R.id.ReminderDate);
        TextView RServices = findViewById(R.id.ReminderServices);

        RemTime = getIntent().getStringExtra("Rtime");
        RemDate = getIntent().getStringExtra("Rdate");
        RemMessage = getIntent().getStringExtra("Rmessage");
        remServices = getIntent().getStringExtra("Services");

        Log.d("BUG", "RemTime: " + RemTime);
        Log.d("BUG", "RemDate: " + RemDate);
        Log.d("BUG", "RemMessage: " + RemMessage);

        //set the viewtext values
        RMessage.setText(RemMessage);
        RDate.setText(RemDate);
        RTime.setText(RemTime);
        RServices.setText(remServices);
    }

    public ArrayList<String> parseList(String s) {
        return new ArrayList<>(Arrays.asList(s.replaceAll("[\\[\\]]", "").split(",")));
    }

    public void goHome(){
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
        finish();
    }

    class WriteData extends AsyncTask<Void, Void, Void> {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        HashMap<String, Object> user = new HashMap<>();
        Calendar calendar = Calendar.getInstance();

        @Override
        protected Void doInBackground(Void... voids) {
            String name = "Test" + (int) (Math.random() * 1000);

            user.put("AppointmentDate", RemDate);
            user.put("DiscordChannel", Arrays.asList("channel1", "channel2"));
            user.put("DiscordLoginID", "id"+name);
            user.put("DiscordPassword", "password1");
            user.put("SlackPassword", "password");
            user.put("UserID", name);
            user.put("Webhooks", Arrays.asList("wh1", "wh2"));
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
            user.put("recipients", parseList(remServices));
            user.put("services", parseList(remServices));
            user.put("slackId", "slack"+name);

            db.collection("Users")
            .document(name)
            .set(user)
            .addOnSuccessListener(aVoid -> Log.d("BUG", "Document written successfully."))
            .addOnFailureListener(e -> Log.w("BUG", "Error when adding document"));

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Snackbar.make(findViewById(R.id.confirmedLayout),
                    "Entry was created successfully",
                    Snackbar.LENGTH_SHORT)
                    .addCallback(new Snackbar.Callback() {
                        @Override
                        public void onDismissed(Snackbar transientBottomBar, int event) {
                            super.onDismissed(transientBottomBar, event);
                            finish();
                            goHome();
                        }
                    })
                    .show();
        }
    }

}