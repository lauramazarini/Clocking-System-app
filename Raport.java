package com.firstapp.mazarinilaura.clockingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class Raport extends ActionBarActivity {


    Button raportTotal;
    Button raportIndividual;
    DataBaseToCSV csv;
    DataBaseToExcel excel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raport);
        raportIndividual=(Button)findViewById(R.id.raportIndividual);
        raportTotal=(Button)findViewById(R.id.raportTotal);


        raportIndividual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Raport.this, RaportIndividual.class);
                startActivity(intent);
            }
        });
        raportTotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                csv=new DataBaseToCSV(UserLog.date.myContext);
                excel=new DataBaseToExcel();
                csv.exportDataBaseIntoCSV();
                excel.doInBackground(databaseList());
                Toast.makeText(getApplicationContext(), "A fost realizat o foaie de calcul excel cu date despre angajati", Toast.LENGTH_LONG).show();
                

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_raport, menu);
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
