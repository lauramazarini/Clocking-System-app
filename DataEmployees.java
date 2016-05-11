package com.firstapp.mazarinilaura.clockingapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class DataEmployees extends ActionBarActivity {

    TextView parola;
    Button page;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_employees);
        parola=(TextView)findViewById(R.id.parola);

        page=(Button)findViewById(R.id.sprePagina);
        final SharedPreferences prefs = this.getSharedPreferences(
                "com.example.app", Context.MODE_PRIVATE);

        final String parolaNoua = prefs.getString("parolaCreataNou", "spersamearga");
        parola.setText(parolaNoua);

        page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nou=new Intent(DataEmployees.this,UserLog.class);
                startActivity(nou);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_data_employees, menu);
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
