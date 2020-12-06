package com.example.mercurymessaging;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Credentials;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SendNow extends AppCompatActivity {
    List<String> phones = new ArrayList<>();
    List<String> webhooks = new ArrayList<>();
    List<String> recipients = new ArrayList<>();
    String discordEndpoint = "";
    String phone = "";
    Button dis;
    Button sms;
    EditText et;
    ListView list;
    boolean hasRecipients = false;
    String twilioURI = "https://api.twilio.com/2010-04-01/Accounts/";
    boolean success = true;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendnow);
        name = Home.getUser();
        et = this.findViewById(R.id.msg);
        Button send = this.findViewById(R.id.send);
        list = this.findViewById(R.id.recipientList);
        send.setOnClickListener(v -> {
            if(!et.getText().toString().equals("") && recipients.size() > 0) {
                new SendMessages().execute();
            } else {
                Toast.makeText(SendNow.this,
                        "Enter all necessary fields",
                        Toast.LENGTH_SHORT)
                .show();
            };
        });

        sms = this.findViewById(R.id.sendnow_sms);
        sms.setOnClickListener(v -> {
            hasRecipients = true;
            AlertDialog.Builder builder = new AlertDialog.Builder(SendNow.this);
            builder.setTitle("Phone Number (US only)");
            final EditText phoneInput = new EditText(SendNow.this);
            phoneInput.setInputType(InputType.TYPE_CLASS_PHONE);
            builder.setView(phoneInput);

            builder.setPositiveButton("Ok", (dialog, which) -> {
                phone = phoneInput.getText().toString().replaceAll(" ", "");

                if(phone.length() != 10) {
                    Toast.makeText(SendNow.this,
                            "Incorrect size for phone number (10 digits)",
                            Toast.LENGTH_SHORT)
                            .show();
                } else {
                    phones.add(phone);
                    recipients.add(phone);
                    list.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, recipients.toArray()));
                    Toast.makeText(SendNow.this,
                            "Added phone number: " + phone,
                            Toast.LENGTH_SHORT)
                            .show();
                }
                phone = "";
            });

            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

            builder.show();
        });

        dis = this.findViewById(R.id.sendnow_dis);
        dis.setOnClickListener(v -> {
            hasRecipients = true;
            AlertDialog.Builder builder = new AlertDialog.Builder(SendNow.this);
            builder.setTitle("Discord Webhook Link");
            final EditText linkInput = new EditText(SendNow.this);
            builder.setView(linkInput);

            builder.setPositiveButton("Ok", (dialog, which) -> {
                discordEndpoint = linkInput.getText().toString();
                boolean valid = discordEndpoint.matches("https:\\/\\/discordapp\\.com\\/api\\/webhooks\\/\\d*\\/.*");

                if(valid) {
                    Toast.makeText(SendNow.this,
                            "Discord webhook link: " + discordEndpoint,
                            Toast.LENGTH_LONG)
                            .show();
                    webhooks.add(discordEndpoint);
                    recipients.add(discordEndpoint);
                    list.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, recipients.toArray()));
                } else {
                    Toast.makeText(SendNow.this,
                            "Invalid webhook link",
                            Toast.LENGTH_SHORT)
                            .show();
                }
                discordEndpoint = "";
            });

            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

            builder.show();
        });

        Button cancel = this.findViewById(R.id.nowCancel);
        cancel.setOnClickListener(v -> finish());
    }

    class SendMessages extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient();
            if(webhooks.size() > 0) {
                for(String link : webhooks) {
                    String message = "{\"content\":\""+ et.getText().toString() + "\"}";
                    RequestBody requestBody = RequestBody.create(
                            MediaType.parse("application/json"), message);

                    Request request = new Request.Builder()
                            .url(link)
                            .post(requestBody)
                            .build();

                    String res;
                    try {
                        Response response = client.newCall(request).execute();

                        res = response.body().string();

                        Log.d("BUG", ">>>>>>>> RESPONSE: " + res);
                        Log.d("BUG", ">>>>>>>> STATUS CODE: " + response.code());
                        if(response.code() != 204) {
                            success = false;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            if(phones.size() > 0){
                final String TOKEN = BuildConfig.TWILIO_AUTH_TOKEN;
                final String SID = BuildConfig.TWILIO_ACCOUNT_SID;
                twilioURI += SID+"/Messages.json";

                for(String num : phones) {
                    okhttp3.RequestBody requestBody = new FormBody.Builder()
                            .addEncoded("To", "+1"+ num)
                            .addEncoded("From", "+13157562819")
                            .addEncoded("Body", et.getText().toString())
                            .build();

                    Request req = new Request.Builder()
                            .url(twilioURI)
                            .header("Content-Type", "application/x-www-form-urlencoded")
                            .addHeader("Authorization", Credentials.basic(SID, TOKEN))
                            .post(requestBody)
                            .build();


                    String res;
                    try {
                        Response response = client.newCall(req).execute();
                        res = response.body().string();

                        Log.d("BUG", ">>>>>>>> RESPONSE: " + res);
                        Log.d("BUG", ">>>>>>>> STATUS CODE: " + response.code());
                        if(response.code() != 201) {
                            success = false;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                }

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            Calendar calendar = Calendar.getInstance();
            Map<String, Object> data = new HashMap<>();
            data.put("UserID", Home.getUser());
            data.put("imDate", (calendar.get(Calendar.MONTH) + 1)+"/"+calendar.get(Calendar.DAY_OF_MONTH)+"/"+calendar.get(Calendar.YEAR));
            String mins;
            if(calendar.get(Calendar.MINUTE) < 10) {
                mins = "0" + calendar.get(Calendar.MINUTE);
            } else {
                mins = String.valueOf(calendar.get(Calendar.MINUTE));
            }
            data.put("imTime", calendar.get(Calendar.HOUR) + ":" + mins);
            data.put("imContent", et.getText().toString());
            data.put("imRecipients", recipients);

            db.collection("Users")
                    .document(name)
                    .set(data, SetOptions.merge())
            .addOnFailureListener(e -> success = false);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(success) {
                Snackbar.make(findViewById(R.id.sendNowLayout),
                        "Message(s) sent successfully.",
                        Snackbar.LENGTH_SHORT)
                        .addCallback(new Snackbar.Callback() {
                            @Override
                            public void onDismissed(Snackbar transientBottomBar, int event) {
                                super.onDismissed(transientBottomBar, event);
                                finish();
                            }
                        })
                        .show();
            } else {
                Snackbar.make(findViewById(R.id.sendNowLayout),
                        "Error when sending requests.",
                        Snackbar.LENGTH_SHORT)
                        .addCallback(new Snackbar.Callback() {
                            @Override
                            public void onDismissed(Snackbar transientBottomBar, int event) {
                                super.onDismissed(transientBottomBar, event);
                                finish();
                            }
                        })
                        .show();
            }
        }
    }

}

