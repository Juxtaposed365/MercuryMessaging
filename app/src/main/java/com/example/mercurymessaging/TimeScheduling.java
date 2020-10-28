package com.example.mercurymessaging;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class TimeScheduling extends AppCompatActivity {

    //String representations of the date and time
    //Format: mm/dd/yyyy
    private String dateRep="";

    private String timeRep="";
    DatePicker dp;
    TimePicker tp;
    public TimeScheduling() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_scheduling);
        dp = (DatePicker) findViewById(R.id.datePicker);
        tp = (TimePicker) findViewById(R.id.timePicker);
        Button cancelButton = (Button)this.findViewById(R.id.DTcancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Button Confirm = (Button)this.findViewById(R.id.DTconfirm);
        Confirm.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                processDT();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void processDT(){
        int hours = tp.getHour();
        String strMinutes = "";
        if(hours > 12)
        {
            hours = hours-12;
        }
        else if(hours == 0)
        {
            hours = 12;
        }
        if(tp.getMinute() < 10)
        {
            strMinutes = "0" + String.valueOf(tp.getMinute());
        }
        else
        {
            strMinutes = String.valueOf(tp.getMinute());
        }

        String am_pm = (tp.getHour() < 12) ? "AM" : "PM";
        timeRep = String.valueOf(hours) + ":" + strMinutes + am_pm;
        dateRep = String.valueOf(dp.getMonth()) + "/" + String.valueOf(dp.getDayOfMonth()) +"/" + String.valueOf(dp.getYear());
        Log.d("BUG", "timeRep-> " + timeRep);
        Log.d("BUG", "dateRep-> " + dateRep);

        //pass values to setReminder
        //stored as key, value pairs
        Intent i = new Intent(this, SetReminder.class);
        Bundle bundle = new Bundle();
        i.putExtra("time", timeRep);
        i.putExtra("date", dateRep);
        startActivity(i);
        finish();


        //go back to setReminder after passing the values along
    }
}