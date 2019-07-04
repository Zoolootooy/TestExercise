package com.example.testexercise;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    boolean On;
    Button btOn;
    Button btOff;
    SharedPreferences sPref;
    String Time;
    int Counter;
    TextView tvTime, tvCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(MainActivity.this, "Activity OnCreate", Toast.LENGTH_SHORT).show();


        btOn = findViewById(R.id.actrivity_main_btOn);
        btOff = findViewById(R.id.actrivity_main_btOff);
        tvTime = findViewById(R.id.activity_main_tv_LastServiceStartTime);
        tvCounter = findViewById(R.id.activity_main_tv_Counter);

        sPref = getSharedPreferences("mysettings", Context.MODE_PRIVATE);
        Time = sPref.getString("Time", "");
        Counter = sPref.getInt("Counter", 0);

        tvTime.setText(Time);
        tvCounter.setText(String.valueOf(Counter));
        On = sPref.getBoolean("On", false);

        if (On){
            btOff.setEnabled(On);
            btOn.setEnabled(!On);
        }
        else {
            btOff.setEnabled(On);
            btOn.setEnabled(!On);

        }






    }




    @Override
    protected void onDestroy() {
        super.onDestroy();

        SharedPreferences sPref = getSharedPreferences("mysettings", Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString("Time", Time);
        ed.putInt("Counter", Counter);
        ed.commit();

        StopServ();

        Toast.makeText(MainActivity.this, "Activity OnDestroy", Toast.LENGTH_SHORT).show();

    }

    public void actrivity_main_btOn_OnClick (View view ){
        On = !On;
        btOn.setEnabled(!On);
        btOff.setEnabled(On);

        StartServ();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("backTime");

        intentFilter.addAction("backCounter");

        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("backTime")){
                    Time = intent.getStringExtra("Time");
                    tvTime.setText(Time);
                }
                if (intent.getAction().equals("backCounter")){
                    Counter = intent.getIntExtra("Counter", 0);
                    String S =  String.valueOf(Counter);
                    tvCounter.setText(S);
                }
            }
        };

        registerReceiver(receiver, intentFilter);


    }

    public void actrivity_main_btOff_OnClick (View view ){
        On = !On;
        btOn.setEnabled(!On);
        btOff.setEnabled(On);

        StopServ();



    }

    public void StartServ (){
        Intent intent = new Intent(this, MyService.class);
        startService(intent);
    }

    public void StopServ(){
        Intent intent = new Intent(this, MyService.class);
        stopService(intent);
    }
}
