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

import org.w3c.dom.Text;

import java.util.Map;

public class Upcoming extends AppCompatActivity {
    private static String userCache = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming);

        if(!userCache.equals("")) {
            // fetch data if user set
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("Users").document(userCache).get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Map<String, Object> user = document.getData();
                                TextView text = findViewById(R.id.reminder);
                                text.setText(user.get("AppointmentDate").toString());
                                TextView time = findViewById(R.id.time);
                                time.setText(user.get("TimeScheduled").toString());
                                time.setVisibility(View.VISIBLE);
                                TextView msg = findViewById(R.id.message);
                                msg.setText(user.get("message").toString());
                                msg.setVisibility(View.VISIBLE);
                                TextView recipients = findViewById(R.id.recipients);
                                recipients.setText(user.get("recipients").toString());
                                recipients.setVisibility(View.VISIBLE);
                            } else {
                                Log.d("BUG", "No such document");
                            }
                        } else {
                            Log.d("BUG", "get failed with ", task.getException());
                        }
                    });
        }
    }

    public static void setUserCache(String s) {
        userCache = s;
    }

}
