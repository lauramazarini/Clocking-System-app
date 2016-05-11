package com.firstapp.mazarinilaura.clockingapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class Hours extends ActionBarActivity {


    ImageButton validate;
    ImageButton showHoursWorked;
    String parola;
    ArrayList<String> daysHours;
    ListView lv;
    TextView tv;
    int h;
    int m;
    String informatii;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hours);
        final SharedPreferences prefs = this.getSharedPreferences(
                "com.example.app", Context.MODE_PRIVATE);
        parola= prefs.getString("parola", "parola");
        validate=(ImageButton)findViewById(R.id.validate);
        lv=(ListView)findViewById(R.id.listView);
        tv=(TextView)findViewById(R.id.ore);





        lv.setVisibility(View.INVISIBLE);
        showHoursWorked=(ImageButton)findViewById(R.id.showhours);
        informatii="";
        showHoursWorked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lv.setVisibility(View.VISIBLE);

                for (int i = 0; i < UserLog.date.calculateHours(parola).size(); i++) {
                    informatii = informatii + UserLog.date.calculateHours(parola).get(i).toString();
                    //  h = h + Integer.parseInt(UserLog.date.calculateHours(parola).get(i).toString().substring(0, 2));
                    //  m = m + Integer.parseInt(UserLog.date.calculateHours(parola).get(i).toString().substring(3, 5));
                }


               /* int i=60;
               if(h<10&&m<10)
                {  tv.setText("0"+h+":"+"0"+m);}
                else if(h<10 && m>10)
                {
                    if(m-i>0)
                    {
                        h=h+1;
                        m=m-i;
                        if(h<10&&i<10)
                        {  tv.setText("0"+h+":"+"0"+i);}
                        else if(h<10 && i>10)
                        { tv.setText("0"+h+":"+i);  }
                        else if(h>10 && i<10)
                        {tv.setText(h+":"+"0"+i); }
                        else  { tv.setText(h+":"+i);}
                    }

                }
                else if(h>10 && m<10)
                {
                    tv.setText(h+":"+"0"+m);
                }
                else
                {
                    tv.setText(h+":"+m);
                }*/
            }
        });
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                UserLog.date.getDataTable2(parola) );

        lv.setAdapter(arrayAdapter);


        if(lv.getCount()>7);
        {

        }

        validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Hours.this,UserLog.class);
                startActivity(intent);
            }
        });








    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_hours, menu);
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
