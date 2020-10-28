package com.example.mercurymessaging;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class LoginPage extends AppCompatActivity {
    private TextView textView;
    private CardView Login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        Login = findViewById(R.id.LoginCview);
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHome();
            }
        });

        textView = findViewById(R.id.textView2);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegister();
            }
        });

    }
    public void openRegister(){
        Intent intent = new Intent(this, RegisterPage.class);
        startActivity(intent);
        finish();
    }
    public void openHome(){
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
        finish();
    }
}