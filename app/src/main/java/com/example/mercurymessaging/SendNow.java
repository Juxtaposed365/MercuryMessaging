package com.example.mercurymessaging;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;

import okhttp3.Credentials;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SendNow extends AppCompatActivity {
    String discordEndpoint = "";
    String phone = "";
    CheckBox dis;
    CheckBox sms;
    EditText et;
    boolean hasRecipients = false;
    String twilioURI = "https://api.twilio.com/2010-04-01/Accounts/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendnow);
        et = this.findViewById(R.id.msg);
        Button send = this.findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!et.getText().toString().equals("")) {
                    new SendMessages().execute();
                } else {
                    Toast.makeText(SendNow.this,
                            "Enter a message",
                            Toast.LENGTH_SHORT)
                    .show();
                };
            }
        });

        sms = this.findViewById(R.id.sendnow_sms);
        sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sms.isChecked()) {
                    hasRecipients = true;
                    AlertDialog.Builder builder = new AlertDialog.Builder(SendNow.this);
                    builder.setTitle("Phone Number (US only)");
                    final EditText phoneInput = new EditText(SendNow.this);
                    phoneInput.setInputType(InputType.TYPE_CLASS_PHONE);
                    builder.setView(phoneInput);

                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            phone = phoneInput.getText().toString();
                            Toast.makeText(SendNow.this,
                                    "Phone Number is set to: " + phone,
                                    Toast.LENGTH_LONG)
                            .show();
                        }
                    });

                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    builder.show();
                } else if(dis.isChecked()) {
                    phone = "";
                } else {
                    phone = "";
                    hasRecipients = false;
                }
            }
        });

        dis = this.findViewById(R.id.sendnow_dis);
        dis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dis.isChecked()) {
                    hasRecipients = true;
                    AlertDialog.Builder builder = new AlertDialog.Builder(SendNow.this);
                    builder.setTitle("Discord Webhook Link");
                    final EditText linkInput = new EditText(SendNow.this);
                    builder.setView(linkInput);

                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            discordEndpoint = linkInput.getText().toString();
                            Toast.makeText(SendNow.this,
                                    "Discord webhook link: " + discordEndpoint,
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    builder.show();
                } else if(sms.isChecked()) {
                    discordEndpoint = "";
                } else {
                    discordEndpoint = "";
                    hasRecipients = false;
                }
            }
        });

        Button cancel = this.findViewById(R.id.nowCancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    class SendMessages extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient();
            if(!discordEndpoint.equals("")) {
                String message = "{\"content\":\""+ et.getText().toString() + "\"}";
                RequestBody requestBody = RequestBody.create(
                        MediaType.parse("application/json"), message);

                Request request = new Request.Builder()
                        .url(discordEndpoint)
                        .post(requestBody)
                        .build();

                try {
                    client.newCall(request).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if(!phone.equals("")){
                final String TOKEN = BuildConfig.TWILIO_AUTH_TOKEN;
                final String SID = BuildConfig.TWILIO_ACCOUNT_SID;
                twilioURI += SID+"/Messages.json";

                okhttp3.RequestBody requestBody = new FormBody.Builder()
                        .addEncoded("To", "+1"+phone)
                        .addEncoded("From", "+13157562819")
                        .addEncoded("Body", et.getText().toString())
                        .build();

                Request req = new Request.Builder()
                        .url(twilioURI)
                        .header("Content-Type", "application/x-www-form-urlencoded")
                        .addHeader("Authorization", Credentials.basic(SID, TOKEN))
                        .post(requestBody)
                        .build();


                String res = "";
                try {
                    Response response = client.newCall(req).execute();
                    res = response.body().string();

                    Log.d("BUG", ">>>>>>>> RESPONSE: " + res);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
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
        }
    }

}

