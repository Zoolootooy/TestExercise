package com.example.testexercise;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
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
    BroadcastReceiver br;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btOn = findViewById(R.id.actrivity_main_btOn);
        btOff = findViewById(R.id.actrivity_main_btOff);
        tvTime = findViewById(R.id.actrivity_main_tv_LastServiceStartTime);
        tvCounter = findViewById(R.id.actrivity_main_tv_Counter);

        sPref = getPreferences(MODE_PRIVATE);
        Time = sPref.getString("Time", "");
        tvTime.setText(Time);
        On = sPref.getBoolean("On", false);

        if (On){
            btOff.setEnabled(On);
            btOn.setEnabled(!On);
            Toast.makeText(MainActivity.this, "On is true", Toast.LENGTH_SHORT).show();
        }
        else {
            btOff.setEnabled(On);
            btOn.setEnabled(!On);
            Toast.makeText(MainActivity.this, "On is false", Toast.LENGTH_SHORT).show();
        }


        Toast.makeText(MainActivity.this, "Counter = " + Counter, Toast.LENGTH_SHORT).show();




        IntentFilter intFilt = new IntentFilter("com.example.testexercise");
        registerReceiver(br, intFilt);
    }

    @Override
    protected void onStop() {
        super.onStop();
        sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putBoolean("On", On);
        ed.commit();
        Toast.makeText(MainActivity.this, "Activity OnStop", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putBoolean("On", false);
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

        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("backTime")){
                    tvTime.setText(intent.getStringExtra("Time"));
                }
            }
        };

        registerReceiver(receiver, intentFilter);
/*
        sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        Time = DateFormat.getDateTimeInstance().format(new Date());
        ed.putString("Time", Time);
        ed.commit();
*/
        //tvTime.setText(Time);

    }

    public void actrivity_main_btOff_OnClick (View view ){
        On = !On;
        btOn.setEnabled(!On);
        btOff.setEnabled(On);

        StopServ();

        tvTime.setText("Not today!");
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
