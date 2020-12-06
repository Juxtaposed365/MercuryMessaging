package com.example.mercurymessaging;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class SetReminder extends AppCompatActivity{
    EditText et; //declared edittext variable
    static String time = "";
    static String date = "";
    String message;
    Button dis;
    Button sms;
    String phone;
    String discordEndpoint;
    List<String> phones = new ArrayList<>();
    static List<String> webhooks = new ArrayList<>();
    static List<String> recipients = new ArrayList<>();
    static List<String> services = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setreminder1);
        Button cancelButton = this.findViewById(R.id.cancelButton);
        et = this.findViewById(R.id.e1);
        cancelButton.setOnClickListener(v -> finish());

        dis = this.findViewById(R.id.button_dis);
        sms = this.findViewById(R.id.button_sms);

        dis.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(SetReminder.this);
            builder.setTitle("Discord Webhook Link");
            final EditText linkInput = new EditText(SetReminder.this);
            builder.setView(linkInput);

            builder.setPositiveButton("Ok", (dialog, which) -> {
                discordEndpoint = linkInput.getText().toString();
                boolean valid = discordEndpoint.matches("https:\\/\\/discordapp\\.com\\/api\\/webhooks\\/\\d*\\/.*");

                if(valid) {
                    Toast.makeText(SetReminder.this,
                            "Discord webhook link: " + discordEndpoint,
                            Toast.LENGTH_LONG)
                            .show();
                    webhooks.add(discordEndpoint);
                    recipients.add(discordEndpoint);
                    if(!services.contains("Discord")) {
                        services.add("Discord");
                    }
                } else {
                    Toast.makeText(SetReminder.this,
                            "Invalid webhook link",
                            Toast.LENGTH_SHORT)
                            .show();
                }
                discordEndpoint = "";
            });

            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

            builder.show();
        });

        sms.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(SetReminder.this);
            builder.setTitle("Phone Number (US only)");
            final EditText phoneInput = new EditText(SetReminder.this);
            phoneInput.setInputType(InputType.TYPE_CLASS_PHONE);
            builder.setView(phoneInput);

            builder.setPositiveButton("Ok", (dialog, which) -> {
                phone = phoneInput.getText().toString().replaceAll(" ", "");

                if(phone.length() != 10) {
                    Toast.makeText(SetReminder.this,
                            "Incorrect size for phone number (10 digits)",
                            Toast.LENGTH_SHORT)
                            .show();
                } else {
                    phones.add(phone);
                    recipients.add(phone);
                    if(!services.contains("SMS")) {
                        services.add("SMS");
                    }
                    Toast.makeText(SetReminder.this,
                            "Added phone number: " + phone,
                            Toast.LENGTH_SHORT)
                            .show();
                }
                phone = "";
            });

            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
            builder.show();
        });

        Button save = this.findViewById(R.id.save);
        save.setOnClickListener(v -> populateReminderInfo());
    }

    public static void clearCache() {
        webhooks = new ArrayList<>();
        recipients = new ArrayList<>();
        services = new ArrayList<>();
    }

    public void showTimePickerDialog(View v) {
        Intent intent = new Intent(this, TimeScheduling.class);
        startActivity(intent);
    }

    public static void setTime(String s) {
        Log.d("BUG", "setTime is run");
        time = s;
    }

    public static void setDate(String s) {
        Log.d("BUG", "setDate is run");
        date = s;
    }

    public void populateReminderInfo()
    {
        message = et.getText().toString();

        if(recipients.size() > 0 && !message.equals("") && !time.equals("") && !date.equals("")) {
            Intent i = new Intent(this, confirmation_screen_simulated.class);
            i.putExtra("Rtime", time);
            i.putExtra("Rdate", date);
            i.putExtra("Rmessage", message);
            startActivity(i);
            finish();
        } else {
            Snackbar.make(findViewById(R.id.reminderLayout),
                    "Please enter all the necessary fields",
                    Snackbar.LENGTH_SHORT)
            .show();
        }
    }

}