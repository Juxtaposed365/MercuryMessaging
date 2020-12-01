package com.example.mercurymessaging;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class Upcoming extends AppCompatActivity {
    private String user = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming);

        if(!user.equals("")) {
            // fetch data if user set
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("Users").document(user).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Log.d("BUG", "DocumentSnapshot data: " + document.getData());
                                    TextView text = findViewById(R.id.reminder);
                                    text.setText(document.getData().toString());
                                } else {
                                    Log.d("BUG", "No such document");
                                }
                            } else {
                                Log.d("BUG", "get failed with ", task.getException());
                            }
                        }
                        });


        }

    }


}
