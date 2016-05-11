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
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class HrOperation extends ActionBarActivity {

    ImageButton ci;
    ImageButton co;

    ImageButton hr;
    ImageButton raport;
    ListView lv;


    public  static ArrayList<String> hours =new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hr_operation);
        lv=(ListView)findViewById(R.id.listView);
        DigitalClock ac = (DigitalClock) findViewById(R.id.digitalClock1);
        ci=(ImageButton)findViewById(R.id.clockin);
        co=(ImageButton)findViewById(R.id.clockout);
        hr=(ImageButton)findViewById(R.id.hr);
        raport=(ImageButton)findViewById(R.id.raport);
        final SharedPreferences prefs = this.getSharedPreferences(
                "com.example.app", Context.MODE_PRIVATE);
        final String parola = prefs.getString("parola", "spersamearga");

        raport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(HrOperation.this,Raport.class);
                startActivity(intent);
            }
        });
        ci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UserLog.date.updateClockIn(parola);
                Intent intent=new Intent(HrOperation.this, Hours.class);
                startActivity(intent);
            }
        });

        hr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(HrOperation.this, HRManager.class);
                startActivity(intent);


            }
        });
        co.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                   try {
                       UserLog.date.updateClockOut(parola);
                       UserLog.date.updateHours(parola);
                       UserLog.date.createNewEntryHourAndDay(parola);
                       Intent intent = new Intent(HrOperation.this, Hours.class);
                       startActivity(intent);
                   }
                   catch (Exception e)
                   {
                       e.printStackTrace();
                       Toast.makeText(getApplicationContext(),"Ati uitat sa faceti ClockIn",Toast.LENGTH_LONG).show();
                   }


            }
        });
      //  ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, hours );
        //lv.setAdapter(arrayAdapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_hr_operation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
