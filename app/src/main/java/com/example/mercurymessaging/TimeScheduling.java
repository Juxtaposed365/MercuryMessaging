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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_scheduling);
        dp = findViewById(R.id.datePicker);
        tp = findViewById(R.id.timePicker);
        Button cancelButton = this.findViewById(R.id.DTcancel);
        cancelButton.setOnClickListener(v -> finish());
        Button Confirm = this.findViewById(R.id.DTconfirm);
        Confirm.setOnClickListener(v -> processDT());
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void processDT(){
        int hours = tp.getHour();
        String strMinutes;
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
            strMinutes = "0" + tp.getMinute();
        }
        else
        {
            strMinutes = String.valueOf(tp.getMinute());
        }

        String am_pm = (tp.getHour() < 12) ? "AM" : "PM";
        timeRep = hours + ":" + strMinutes + am_pm;
        dateRep = (dp.getMonth() + 1) + "/" + dp.getDayOfMonth() +"/" + dp.getYear();
        Log.d("BUG", "timeRep -> " + timeRep);
        Log.d("BUG", "dateRep -> " + dateRep);
        SetReminder.setDate(dateRep);
        SetReminder.setTime(timeRep);
        finish();
    }
}