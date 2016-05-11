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
import android.widget.EditText;
import android.widget.Toast;


public class HRManager extends ActionBarActivity {

    EditText name;
    EditText salary;
    EditText position;
    EditText cnp;
    Button takePicture;
    EditText securitate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hrmanager);
        name=(EditText)findViewById(R.id.name);
        salary=(EditText)findViewById(R.id.position);
        position=(EditText)findViewById(R.id.position);
        cnp=(EditText)findViewById(R.id.ssn);
        securitate=(EditText)findViewById(R.id.securitate);
        takePicture=(Button)findViewById(R.id.takePicture);
        final SharedPreferences prefs = this.getSharedPreferences(
                "com.example.app", Context.MODE_PRIVATE);
        takePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

             try {
                    Bundle bundle = new Bundle();
                    bundle.putString("nume", name.getEditableText().toString());
                    bundle.putString("salariu", (salary.getEditableText().toString()));
                    bundle.putString("pozitie", position.getEditableText().toString());
                    bundle.putString("cnp", cnp.getEditableText().toString());
                    bundle.putString("securitate",securitate.getEditableText().toString());
                    prefs.edit().putString("parolaCreataNou", cnp.getEditableText().toString().substring(0, 5)).apply();
                    Intent intent = new Intent(HRManager.this, Camera.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
             catch (Exception e)
             {e.printStackTrace();
                 Toast.makeText(getApplicationContext(),"Unul dintre campuri nu a fost completat. Toate campurile sunt obligatorii",Toast.LENGTH_LONG).show();}

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_hrmanager, menu);
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
