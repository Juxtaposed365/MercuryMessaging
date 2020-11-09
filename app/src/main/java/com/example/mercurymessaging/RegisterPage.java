package com.example.mercurymessaging;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class RegisterPage extends AppCompatActivity {
    private TextView textView;
    private CardView Continue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);
        Continue = findViewById(R.id.ContinueCview);
        Continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHome();
            }
        });

        textView = findViewById(R.id.textView12);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSignIn();
            }
        });
    }
    public void openSignIn(){
        Intent intent = new Intent(this,LoginPage.class);
        startActivity(intent);
        finish();
    }

    public void openHome(){
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
        finish();
    }
}