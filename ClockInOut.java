package com.firstapp.mazarinilaura.clockingapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DigitalClock;
import android.widget.ImageButton;

import java.util.List;


public class ClockInOut extends ActionBarActivity {


    ImageButton ci;
    ImageButton co;
    public  List<String> hours;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clockinout);
        DigitalClock ac = (DigitalClock) findViewById(R.id.digitalClock1);

        ci=(ImageButton)findViewById(R.id.clockin);
        co=(ImageButton)findViewById(R.id.clockout);

        final SharedPreferences prefs = this.getSharedPreferences(
                "com.example.app", Context.MODE_PRIVATE);
        final String parola = prefs.getString("parola", "spersamearga");

        ci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UserLog.date.updateClockIn(parola);
                Intent intent=new Intent(ClockInOut.this, Hours.class);
                startActivity(intent);
            }
        });

        co.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UserLog.date.updateClockOut(parola);
                UserLog.date.updateHours(parola);
                String s=UserLog.date.getHoursWorked(parola);
                hours.add(s);
                System.out.print(hours.get(0).toString());
                Intent intent=new Intent(ClockInOut.this, Hours.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_clock_in_out, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
